package cc.ddev.feather.database.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "worlds")
public class WorldModel extends StormModel {
    @Column(name = "worldname", unique = true)
    private String worldName;

    @Column(name = "color", defaultValue = "<gray>")
    private String color;

    @Column(name = "title")
    private String title;

    @Column(name = "loadingname")
    private String loadingName;

    @Column(name = "temperature", defaultValue = "21.64")
    private Double temperature;
}
