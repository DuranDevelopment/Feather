package cc.ddev.feather.api.objects;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.BankAccountModel;
import cc.ddev.feather.database.models.PhoneModel;
import com.craftmend.storm.api.enums.Where;

import java.util.Collection;
import java.util.UUID;

public class Phone {
    private int phonenumber;
    private double credit;
    private UUID owner;

    public Phone(int phonenumber, double credit, UUID owner) {
        this.phonenumber = phonenumber;
        this.credit = credit;
        this.owner = owner;
    }

    public int getPhoneNumber() {
        return this.phonenumber;
    }

    public double getCredit() {
        return this.credit;
    }

    public void setCredit(double newCredit) {
        this.credit = newCredit;
        try {
            Collection<PhoneModel> phoneModels =
                    StormDatabase.getInstance().getStorm().buildQuery(PhoneModel.class)
                            .where("phone_number", Where.EQUAL, this.phonenumber)
                            .limit(1)
                            .execute()
                            .join();
            PhoneModel phoneModel = phoneModels.iterator().next();
            phoneModel.setCredit(newCredit);
            StormDatabase.getInstance().saveStormModel(phoneModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        try {
            Collection<PhoneModel> phoneModels =
                    StormDatabase.getInstance().getStorm().buildQuery(PhoneModel.class)
                            .where("phone_number", Where.EQUAL, this.phonenumber)
                            .limit(1)
                            .execute()
                            .join();
            PhoneModel phoneModel = phoneModels.iterator().next();
            phoneModel.setOwner(owner);
            StormDatabase.getInstance().saveStormModel(phoneModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}