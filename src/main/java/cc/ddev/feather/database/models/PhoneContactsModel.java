package cc.ddev.feather.database.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "phonecontacts")
public class PhoneContactsModel extends StormModel {

    @Column(name = "phone_number")
    private Integer phoneNumber;

    @Column(name = "contact_phone_number")
    private Integer contactPhoneNumber;
}
