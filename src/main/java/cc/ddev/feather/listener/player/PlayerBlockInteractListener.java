package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.banking.SelectorGUI;
import cc.ddev.feather.api.config.BankingConfig;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.ATMOpenType;
import cc.ddev.feather.api.playerdata.PlayerManager;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.instanceguard.InstanceGuard;
import cc.ddev.instanceguard.region.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;

import java.util.UUID;

public class PlayerBlockInteractListener implements Listener {
    private final InstanceGuard instanceGuard = API.getInstanceGuard();

    @Listen
    public void onPlayerBlockInteract(PlayerBlockInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUuid();
        if (event.getBlock() == Block.RED_SANDSTONE_STAIRS) {
            if (BankingConfig.ATM.REQUIRE_DEBIT_CARD) {
                if (!player.getItemInMainHand().equals(ItemStack.of(BankingConfig.ATM.DEBIT_CARD_ITEM))) {
                    event.setCancelled(true);
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.ATM.DEBIT_CARD_REQUIRED));
                    return;
                }
            }
            SelectorGUI.openSelectionMenu(player, player, ATMOpenType.CLICK_ATM);
        }

        // Get current region
        Region region = instanceGuard.getRegionManager().getRegion(player.getPosition(), player.getInstance());

        if (event.getBlock().name().endsWith("sign")) {
            if (player.getPermissionLevel() == 4 || region.isOwner(player)) {
                OpenSignEditorPacket openSignEditorPacket = new OpenSignEditorPacket(event.getBlockPosition(), true);
                event.getPlayer().getPlayerConnection().sendPacket(openSignEditorPacket);
            }
        }
        if (player.getInstance() == null) return;

        if (player.getItemInMainHand().equals(Config.Plot.PLOTWAND)) {
            PlayerManager.setPlotWandPos2(uuid, event.getBlockPosition());
            player.sendMessage(ChatUtils.translateMiniMessage("<green>Position 2 set to <dark_green>"
                    + PlayerManager.getPlotWandPos2(uuid).blockX()
                    + ", " + PlayerManager.getPlotWandPos2(uuid).blockY()
                    + ", " + PlayerManager.getPlotWandPos2(uuid).blockZ()));
            event.setCancelled(true);
        }
    }
}
