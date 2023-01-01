package cc.ddev.feather.database.models;

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

    @Column(defaultValue = "0")
    private String lastLocation;

    @Column(defaultValue = "0")
    private Double money;
}
