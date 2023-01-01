package cc.ddev.feather.database.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "worlds")
public class WorldModel extends StormModel {

    @Column(name = "uuid", unique = true, keyType = KeyType.PRIMARY)
    private String worldUniqueId;

    @Column(name = "name", unique = true)
    private String worldName;

}
