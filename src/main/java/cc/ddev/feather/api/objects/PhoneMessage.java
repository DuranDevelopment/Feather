package cc.ddev.feather.api.objects;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PhoneMessagesModel;
import cc.ddev.feather.database.models.PhoneModel;
import com.craftmend.storm.api.enums.Where;

import java.util.Collection;
import java.util.UUID;

public class PhoneMessage {
    private long id;
    private int phoneNumberSender;
    private int phoneNumberReceiver;
    private UUID sender;
    private String message;
    private boolean read;

    public PhoneMessage(long id, int phoneNumberSender, UUID sender, int phoneNumberReceiver, String message, boolean read) {
        this.id = id;
        this.phoneNumberReceiver = phoneNumberReceiver;
        this.sender = sender;
        this.phoneNumberSender = phoneNumberSender;
        this.message = message;
        this.read = read;
    }

    public long getId() {
        return this.id;
    }

    public int getPhoneNumberSender() {
        return this.phoneNumberSender;
    }

    public UUID getSenderUUID() {
        return this.sender;
    }

    public int getPhoneNumberReceiver() {
        return this.phoneNumberReceiver;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isRead() {
        return this.read;
    }

    public void setRead(boolean read) {
        this.read = read;
        try {
            Collection<PhoneMessagesModel> phoneMessagesModels =
                    StormDatabase.getInstance().getStorm().buildQuery(PhoneMessagesModel.class)
                            .where("id", Where.EQUAL, this.getId())
                            .limit(1)
                            .execute()
                            .join();
            PhoneMessagesModel phoneMessagesModel = phoneMessagesModels.iterator().next();
            phoneMessagesModel.setMessageRead(read);
            StormDatabase.getInstance().saveStormModel(phoneMessagesModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}