package cc.ddev.feather.command.plots.subcommands;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.config.CalculateConfig;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.feather.wrapper.Location;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlotCalculateCommand extends Command {

    private static final List<String> BLOCK_TYPES = Arrays.asList(
            "CONCRETE_POWDER", "CONCRETE", "LEAVES", "BANNER", "CARPET",
            "CLAY", "TERRACOTTA", "STAINED_GLASS_PANE", "STAINED_GLASS",
            "BUTTON", "WOOL", "TRAPDOOR", "DOOR", "SIGN"
    );

    public PlotCalculateCommand() {
        super("calculate");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /plot calculate"));

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;

            if (API.getPlayerManager().getPlotWandPos1(player.getUuid()) != null
                    && API.getPlayerManager().getPlotWandPos2(player.getUuid()) != null) {

                // TODO: Add to Messages.toml
                player.sendMessage(ChatUtils.translateMiniMessage("<white>[<red><bold>Calculate<reset><white>] <gray>Calculating...."));

                Location firstLocation = new Location(new Pos(API.getPlayerManager().getPlotWandPos1(player.getUuid())), player.getInstance());
                Location secondLocation = new Location(new Pos(API.getPlayerManager().getPlotWandPos2(player.getUuid())), player.getInstance());

                PlotCalculateCommand.calculatePrices(firstLocation, secondLocation).thenAccept(result -> {
                    double buildingPrice = result.getKey();
                    double buildersPrice = result.getValue();
                    double totalPrice = buildingPrice + buildersPrice;

                    // TODO: Add to Messages.toml
                    String costMessage = "<white>[<red><bold>Calculate<reset><white>] <white>Kosten voor dit gebouw:%newline%" +
                            "<white>[<red><bold>Calculate<reset><white>] <white>Prijs gebouw: <red>€ <buildingprice>%newline%" +
                            "<white>[<red><bold>Calculate<reset><white>] <white>Bouwers kosten: <red>€ <buildersprice>%newline%" +
                            "<white>[<red><bold>Calculate<reset><white>] <white>Totale kosten: <red>€ <totalprice>";

                    player.sendMessage(ChatUtils.translateMiniMessage(costMessage
                            .replace("<buildingprice>", BankUtils.getInstance().format(buildingPrice))
                            .replace("<buildersprice>", BankUtils.getInstance().format(buildersPrice))
                            .replace("<totalprice>", BankUtils.getInstance().format(totalPrice))
                            .replace("%newline%", "\n")));
                });
            } else {
                player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>You don't have a <aqua>PlotWand <dark_aqua>selection."));
            }
        });
    }

    public static CompletableFuture<Map.Entry<Double, Double>> calculatePrices(Location firstLocation, Location secondLocation) {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<String> missingBlocks = new ArrayList<>();
            double buildingPrice = 0.0;

            List<Block> blocks = PlotCalculateCommand.getBlocks(firstLocation, secondLocation);

            for (Block block : blocks) {
                boolean found = false;
                String blockName = block.name().replace("minecraft:", "").toUpperCase();

                for (String blockType : BLOCK_TYPES) {
                    if (blockName.contains(blockType)) {
                        buildingPrice += CalculateConfig.getInstance().getBlockCost(blockType);
                        found = true;
                        break;
                    }
                }

                if (!found && CalculateConfig.getInstance().getBlockCost(blockName) != -1.0) {
                    buildingPrice += CalculateConfig.getInstance().getBlockCost(blockName);
                } else if (!found) {
                    missingBlocks.add(blockName);
                }
            }

            if (!missingBlocks.isEmpty()) {
                Log.getLogger().info("==== FEATHER CALCULATING MISSING ====");
                for (String blockType : missingBlocks) {
                    Log.getLogger().info("Missing block " + blockType);
                }
                Log.getLogger().info("==== FEATHER CALCULATING MISSING ====");
            }

            double buildersCostPerBlock = CalculateConfig.BUILDERS_COST_PER_BLOCK;
            double buildersPrice = (double) blocks.size() * buildersCostPerBlock;

            return new AbstractMap.SimpleEntry<>(buildingPrice, buildersPrice);
        });
    }

    private static List<Block> getBlocks(Location firstLocation, Location secondLocation) {
        ArrayList<Block> blocks = new ArrayList<>();

        double minX = Math.min(firstLocation.getX(), secondLocation.getX());
        double maxX = Math.max(firstLocation.getX(), secondLocation.getX());

        double minY = Math.min(firstLocation.getY(), secondLocation.getY());
        double maxY = Math.max(firstLocation.getY(), secondLocation.getY());

        double minZ = Math.min(firstLocation.getZ(), secondLocation.getZ());
        double maxZ = Math.max(firstLocation.getZ(), secondLocation.getZ());

        for (double x = minX; x <= maxX; ++x) {
            for (double z = maxZ; z >= minZ; --z) {
                for (double y = maxY; y >= minY; --y) {
                    Block block = firstLocation.getInstance().getBlock(firstLocation.getPos());
                    if (!block.equals(Block.AIR)) {
                        blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }
}
