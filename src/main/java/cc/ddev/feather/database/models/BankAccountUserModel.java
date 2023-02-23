package cc.ddev.feather.database.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bankaccountusers")
public class BankAccountUserModel extends StormModel {

    @Column(name = "bankAccountId")
    private Integer bankAccountId;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "permission")
    private String permission;
}
