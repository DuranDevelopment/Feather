package cc.ddev.feather.database;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.player.PlayerWrapper;
import com.craftmend.storm.Storm;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.connection.hikaricp.HikariDriver;
import com.craftmend.storm.connection.sqlite.SqliteFileDriver;
import com.zaxxer.hikari.HikariConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StormDatabase {
    private static @Getter @Setter(AccessLevel.PRIVATE) StormDatabase instance;
    private @Getter Storm storm;
    private static @Getter ExecutorService executorService;

    private final String currentDirectory = System.getProperty("user.dir");

    public StormDatabase() {
        setInstance(this);
        executorService = Executors.newFixedThreadPool(10);
    }

    public void init() throws SQLException, ClassNotFoundException {
        if (Config.Database.TYPE.equalsIgnoreCase("sqlite")) {
            storm = new Storm(new SqliteFileDriver(new File(currentDirectory, "database.db")));
        } else {

            Class.forName("com.mysql.cj.jdbc.MysqlDataSource");
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl("jdbc:mysql://" + Config.Database.HOST + ":" + Config.Database.PORT + "/" + Config.Database.NAME);
            config.setUsername(Config.Database.USERNAME);
            config.setPassword(Config.Database.PASSWORD);

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("jdbcCompliantTruncation", "false");
            config.setPoolName("FeatherPool");

            storm = new Storm(new HikariDriver(config));
        }

        storm.registerModel(new PlayerModel());
        storm.runMigrations();

        Log.getLogger().info("Succesfully connected to the database.");
    }

    public CompletableFuture<Integer> saveStormModel(StormModel stormModel) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                completableFuture.complete(storm.save(stormModel));
            } catch (SQLException exception) {
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    public CompletableFuture<Optional<PlayerModel>> findPlayerModel(@NotNull UUID uuid) {
        CompletableFuture<Optional<PlayerModel>> completableFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                Collection<PlayerModel> playerModel;
                playerModel = storm.buildQuery(PlayerModel.class)
                        .where("unique_id", Where.EQUAL, uuid.toString())
                        .limit(1)
                        .execute()
                        .join();

                MinecraftServer.getSchedulerManager().buildTask(() -> completableFuture.complete(playerModel.stream().findFirst()));
            } catch (Exception exception) {
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    public CompletableFuture<PlayerModel> loadPlayerModel(UUID uuid) {
        CompletableFuture<PlayerModel> completableFuture = new CompletableFuture<>();
        findPlayerModel(uuid).thenAccept(playerModel -> {
            PlayerWrapper.playerModels.remove(uuid);

            if (playerModel.isEmpty()) {
                PlayerModel createdModel = new PlayerModel();
                createdModel.setUniqueId(uuid);
                PlayerWrapper.playerModels.put(uuid, createdModel);
                completableFuture.complete(createdModel);

                this.saveStormModel(createdModel);
                return;
            }

            PlayerWrapper.playerModels.put(uuid, playerModel.get());
            completableFuture.complete(playerModel.get());
        });

        return completableFuture;
    }
}