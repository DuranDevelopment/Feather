package cc.ddev.feather.database.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "phones")
public class PhoneModel extends StormModel {

    @Column(name = "phone_number")
    private Integer phoneNumber;

    @Column
    private Double credit;

    @Column
    private UUID owner;
}