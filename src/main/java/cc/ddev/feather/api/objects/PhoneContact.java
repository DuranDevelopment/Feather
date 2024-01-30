package cc.ddev.feather.api.objects;


import java.util.UUID;

public class PhoneContact {
    private long rowId;
    private int phoneNumber;
    private UUID uuid;

    public PhoneContact(long rowId, int phoneNumber, UUID uuid) {
        this.rowId = rowId;
        this.phoneNumber = phoneNumber;
        this.uuid = uuid;
    }

    public long getRowId() {
        return this.rowId;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public UUID getOwnerUUID() {
        return this.uuid;
    }
}