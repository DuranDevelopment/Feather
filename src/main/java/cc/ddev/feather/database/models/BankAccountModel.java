package cc.ddev.feather.database.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bankaccounts")
public class BankAccountModel extends StormModel {
    @Column(name = "balance", defaultValue = "0")
    private Double balance;
    @Column(name = "bankAccountType")
    private String type;
    @Column(name = "bankAccountName")
    private String name;
    @Column(name = "frozen", defaultValue = "0")
    private Boolean frozen;
}
