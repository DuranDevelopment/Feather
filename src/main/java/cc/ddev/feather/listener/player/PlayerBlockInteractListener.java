package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.banking.SelectorGUI;
import cc.ddev.feather.api.config.BankingConfig;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.ATMOpenType;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.instanceguard.InstanceGuard;
import cc.ddev.instanceguard.region.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;

public class PlayerBlockInteractListener implements Listener {
    private final InstanceGuard instanceGuard = API.getInstanceGuard();

    @Listen
    public void onPlayerBlockInteract(PlayerBlockInteractEvent event) {
        Player player = event.getPlayer();

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

        if (player.getInstance() == null) return;

        if (player.getItemInMainHand().equals(Config.Plot.PLOTWAND)) {
            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
            if (playerProfile == null) return;

            playerProfile.setPlotWandPos2(event.getBlockPosition());
            player.sendMessage(ChatUtils.translateMiniMessage("<green>Position 2 set to <dark_green>" + playerProfile.getPlotWandPos2()));
            event.setCancelled(true);
        }

        String instanceName = WorldManager.getInstanceName(player.getInstance());

        if (instanceGuard.getRegionManager().getRegions().isEmpty()) {
            instanceGuard.getRegionManager().createRegion("test", instanceName, new Pos(6, -24, -2), new Pos(9, -21, 1));
        }

        for (Region region : instanceGuard.getRegionManager().getRegions()) {
            if (region.containsLocation(player.getPosition())) {
                player.sendMessage(ChatUtils.translateMiniMessage("<red>You are in a region called <dark_red>" + region.getName() + "<red>!"));
            }
        }
    }
}
