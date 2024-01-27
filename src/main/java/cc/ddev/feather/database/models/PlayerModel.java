package cc.ddev.feather.database.models;

import cc.ddev.feather.api.config.Config;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "players")
public class PlayerModel extends StormModel {

    @Column(name = "uuid", unique = true)
    private UUID uniqueId;

    @Column(name = "last_known_username")
    private String username;

    @Column
    private String lastLocation;

    @Column(defaultValue = "0")
    private Double balance;

    @Column(defaultValue = "Wanderer")
    private String prefix;

    @Column(defaultValue = "<gray>")
    private String prefixcolor;

    @Column(defaultValue = "<gray>")
    private String levelcolor;

    @Column(defaultValue = "<gray>")
    private String chatcolor;

    @Column(defaultValue = "<gray>")
    private String namecolor;

    @Column(defaultValue = "0")
    private Integer level;

    @Column(name = "operator", defaultValue = "0")
    private Boolean isOperator;
}
