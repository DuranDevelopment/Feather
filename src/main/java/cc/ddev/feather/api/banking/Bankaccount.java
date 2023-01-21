package cc.ddev.feather.api.banking;

import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.api.enums.BankPermission;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.BankAccountUserModel;
import com.craftmend.storm.api.enums.Where;

import java.util.*;

public class Bankaccount {
    private final int id;
    private String name;
    private final BankAccountType type;
    private double balance;
    private boolean frozen;
    private final HashMap<UUID, BankPermission> users;

    public Bankaccount(int id, BankAccountType type, double balance, String name, boolean frozen, HashMap<UUID, BankPermission> users) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.frozen = frozen;
        this.users = users;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public BankAccountType getType() {
        return this.type;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double newBalance) {
        double balanceChange = newBalance - this.balance;
        this.balance = newBalance;
    }

    public void setBalanceSilent(double newBalance) {
        this.balance = newBalance;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public void setFrozenSilent(boolean frozen) {
        this.frozen = frozen;
    }

    public List<UUID> getAuthorisedUsers() {
        return List.copyOf(this.users.keySet());
    }

    public Map<UUID, BankPermission> getUsers() {
        return Collections.unmodifiableMap(this.users);
    }

    public boolean hasPermission(UUID uuid, BankPermission permission) {
        if (!this.getUsers().containsKey(uuid)) {
            return false;
        }
        BankPermission userPerm = this.getUsers().get(uuid);
        return userPerm == BankPermission.ADMIN || userPerm == permission;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameSilent(String name) {
        this.name = name;
    }

    public void addUser(final UUID uuid, final BankPermission permission) {
        this.addUserSilent(uuid, permission);
        BankAccountUserModel bankAccountUserModel = new BankAccountUserModel();
        bankAccountUserModel.setId(this.getId());
        bankAccountUserModel.setUuid(uuid);
        bankAccountUserModel.setPermission(permission.name());
        StormDatabase.getInstance().saveStormModel(bankAccountUserModel);
    }

    public void addUserSilent(UUID uuid, BankPermission permission) {
        if (!this.users.containsKey(uuid)) {
            this.users.put(uuid, permission);
        }
    }

    public void removeUser(final UUID uuid) throws Exception {
        this.removeUserSilent(uuid);
        Collection<BankAccountUserModel> bankAccountUserModels =
                StormDatabase.getInstance().getStorm().buildQuery(BankAccountUserModel.class)
                        .where("bankId", Where.EQUAL, this.getId())
                        .where("uuid", Where.EQUAL, uuid)
                        .limit(1)
                        .execute()
                        .join();

        BankAccountUserModel bankAccountUserModel = bankAccountUserModels.iterator().next();
        StormDatabase.getInstance().getStorm().delete(bankAccountUserModel);
    }

    public void removeUserSilent(UUID uuid) {
        this.users.remove(uuid);
    }

    @Deprecated
    public void addUser(UUID user) {
        this.users.put(user, BankPermission.ADMIN);
    }

    @Deprecated
    public void addUserFromCommand(final UUID user) {
        BankAccountUserModel bankAccountUserModel = new BankAccountUserModel();
        bankAccountUserModel.setId(this.getId());
        bankAccountUserModel.setUuid(user);
        bankAccountUserModel.setPermission(BankPermission.ADMIN.name());
        StormDatabase.getInstance().saveStormModel(bankAccountUserModel);
    }

    @Deprecated
    public void removeUserFromCommand(UUID user) throws Exception {
        this.removeUser(user);
    }

    @Deprecated
    public void setNameWithoutChannels(String name) {
        this.setNameSilent(name);
    }

    @Deprecated
    public void setFrozenWithoutChannels(boolean frozen) {
        this.setFrozenSilent(frozen);
    }

    @Deprecated
    public void setBalanceWithoutChannels(double newBalance) {
        this.setBalanceSilent(newBalance);
    }
}