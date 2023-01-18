package cc.ddev.feather.database.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "prefixes")
public class PrefixesModel extends StormModel {

    @Column(name = "uuid", unique = true)
    private UUID uniqueId;
    @Column(name = "prefix")
    private String prefix;
}
