package cc.ddev.feather.api.banking;

import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.api.enums.BankPermission;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.BankAccountModel;
import cc.ddev.feather.database.models.BankAccountUserModel;
import cc.ddev.feather.logger.Log;
import com.craftmend.storm.api.enums.Where;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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
        try {
            Collection<BankAccountModel> bankAccountModels =
                    StormDatabase.getInstance().getStorm().buildQuery(BankAccountModel.class)
                            .where("id", Where.EQUAL, this.id)
                            .limit(1)
                            .execute()
                            .join();
            BankAccountModel bankAccountModel = bankAccountModels.iterator().next();
            bankAccountModel.setBalance(newBalance);
            StormDatabase.getInstance().saveStormModel(bankAccountModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.balance = newBalance;
    }

    public void setBalanceSilent(double newBalance) {
        this.balance = newBalance;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public void setFrozen(boolean frozen) {
        try {
            Collection<BankAccountModel> bankAccountModels =
                    StormDatabase.getInstance().getStorm().buildQuery(BankAccountModel.class)
                            .where("id", Where.EQUAL, this.id)
                            .execute()
                            .join();
            BankAccountModel bankAccountModel = bankAccountModels.iterator().next();
            bankAccountModel.setFrozen(frozen);
            StormDatabase.getInstance().saveStormModel(bankAccountModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            return true;
        }
        BankPermission userPerm = this.getUsers().get(uuid);
        return userPerm != BankPermission.ADMIN && userPerm != permission;
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
        bankAccountUserModel.setBankAccountId(this.getId());
        bankAccountUserModel.setUuid(uuid);
        bankAccountUserModel.setPermission(permission.name());
        StormDatabase.getInstance().saveStormModel(bankAccountUserModel);
        Log.getLogger().info("Added user " + uuid + " to bank account " + this.getId() + " with permission " + permission.name());
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
                        .where("id", Where.EQUAL, this.getId())
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
}