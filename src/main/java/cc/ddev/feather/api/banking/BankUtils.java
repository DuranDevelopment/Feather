package cc.ddev.feather.api.banking;

import cc.ddev.feather.api.config.BankingConfig;
import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.api.enums.BankPermission;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.BankAccountModel;
import cc.ddev.feather.database.models.BankAccountUserModel;
import com.craftmend.storm.api.enums.Where;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BankUtils {
    private static BankUtils instance;
    private static final HashMap<Integer, Bankaccount> cachedAccounts;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private final DecimalFormat decimalFormatNoComma = new DecimalFormat("#,###");

    public static BankUtils getInstance() {
        if (instance == null) {
            instance = new BankUtils();
        }
        return instance;
    }

    public Bankaccount getBankAccount(int id) {
        return cachedAccounts.get(id);
    }

    public List<Bankaccount> getAccounts(BankAccountType ... accountTypes) {
        return cachedAccounts.values().stream().filter(acc -> Arrays.asList(accountTypes).contains((Object)acc.getType())).collect(Collectors.toList());
    }

    public List<Bankaccount> getAccounts(UUID uuid) {
        return cachedAccounts.values().stream().filter(account -> account.getAuthorisedUsers().contains(uuid)).collect(Collectors.toList());
    }

    public List<Bankaccount> getAccounts(UUID uuid, BankAccountType ... accountTypes) {
        return this.getAccounts(uuid).stream().filter(acc -> Arrays.asList(accountTypes).contains((Object)acc.getType())).collect(Collectors.toList());
    }

    public void pullCache() throws Exception {
        cachedAccounts.clear();
        Collection<BankAccountModel> bankAccountModels =
                StormDatabase.getInstance().getStorm().buildQuery(BankAccountModel.class)
                        .execute()
                        .join();

        Collection<BankAccountUserModel> bankAccountUserModels =
                StormDatabase.getInstance().getStorm().buildQuery(BankAccountUserModel.class)
                        .execute()
                        .join();

        for (BankAccountModel bankAccountModel : bankAccountModels) {
            int id = bankAccountModel.getId();
            double balance = bankAccountModel.getBalance();
            String name = bankAccountModel.getName();
            boolean frozen = bankAccountModel.getFrozen();
            BankAccountType type = BankAccountType.valueOf(bankAccountModel.getType());
            Bankaccount acc = new Bankaccount(id, type, balance, name, frozen, new HashMap<>());
            cachedAccounts.put(id, acc);
        }

        for (BankAccountUserModel bankAccountUserModel : bankAccountUserModels) {
            int id = bankAccountUserModel.getId();
            UUID uuid = bankAccountUserModel.getUuid();
            BankPermission permission = BankPermission.valueOf(bankAccountUserModel.getPermission());
            if (cachedAccounts.get(id) == null) continue;
            cachedAccounts.get(id).addUserSilent(uuid, permission);
        }
    }

    public void toDatabase() throws Exception {
        for (Bankaccount bank : cachedAccounts.values()) {
            Collection<BankAccountModel> bankAccountModels = StormDatabase.getInstance().getStorm().buildQuery(BankAccountModel.class)
                    .where("bankId", Where.EQUAL, bank.getId())
                    .limit(1)
                    .execute()
                    .join();

            BankAccountModel bankAccountModel = bankAccountModels.iterator().next();
            bankAccountModel.setId(bank.getId());
            bankAccountModel.setBalance(bank.getBalance());
            bankAccountModel.setName(bank.getName());
            bankAccountModel.setFrozen(bank.isFrozen());
            bankAccountModel.setType(bank.getType().name());
            StormDatabase.getInstance().getStorm().save(bankAccountModel);
        }
    }

    public void clearCache() {
        cachedAccounts.clear();
    }

    public String format(double number) {
        if (BankingConfig.HAS_DECIMALS) {
            return this.decimalFormat.format(number).replace(",", "tmp").replace(".", ",").replace("tmp", ".");
        }
        return this.decimalFormatNoComma.format(number).replace(",", ".");
    }

//    public int create(BankAccountType type) throws IllegalArgumentException {
//        int n;
//        block11: {
//            if (type == BankAccountType.PERSONAL) {
//                throw new IllegalArgumentException("Can't create a personal bank account!");
//            }
//
//
//
//            Connection connection = Database.getDatabase().getConnection();
//            try {
//                PreparedStatement statement = Database.getDatabase().executeUpdate(connection, "INSERT INTO `Bankaccount`(`balance`, `bankAccountType`, `bankAccountName`, `frozen`) VALUES (?, ?, ?, ?)", 1);
//                statement.setDouble(1, 0.0);
//                statement.setString(2, type.getTypeName());
//                statement.setString(3, "Loading..");
//                statement.setBoolean(4, false);
//                statement.executeUpdate();
//                ResultSet rs = statement.getGeneratedKeys();
//                int last_inserted_id = -1;
//                if (rs.next()) {
//                    last_inserted_id = rs.getInt(1);
//                    PreparedStatement updateName = connection.prepareStatement("UPDATE `Bankaccount` SET `bankAccountName`=? WHERE bankId=?");
//                    updateName.setString(1, "&fID: " + last_inserted_id);
//                    updateName.setInt(2, last_inserted_id);
//                    updateName.execute();
//                    updateName.close();
//                }
//                statement.close();
//                rs.close();
//                if (MinetopiaSDBPlugin.getChannelManager() != null) {
//                    MinetopiaSDBPlugin.getChannelManager().send("bank", "create", ImmutableMap.of("id", "" + last_inserted_id, "type", type.name()));
//                }
//                this.silentlyCreateBankaccount(last_inserted_id, type);
//                n = last_inserted_id;
//                if (connection == null) break block11;
//            } catch (Throwable throwable) {
//                try {
//                    if (connection != null) {
//                        try {
//                            connection.close();
//                        } catch (Throwable throwable2) {
//                            throwable.addSuppressed(throwable2);
//                        }
//                    }
//                    throw throwable;
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    return -1;
//                }
//            }
//            connection.close();
//        }
//        return n;
//    }
//
//    public void delete(int id) {
//        try (Connection connection = Database.getDatabase().getConnection();){
//            PreparedStatement statement = Database.getDatabase().executeUpdate(connection, "DELETE FROM `Bankaccount` WHERE bankId=?");
//            statement.setInt(1, id);
//            statement.execute();
//            statement.close();
//            PreparedStatement userStatement = connection.prepareStatement("DELETE FROM `BankaccountUsers` WHERE bankId=?");
//            userStatement.setInt(1, id);
//            userStatement.execute();
//            userStatement.close();
//            cachedAccounts.remove(id);
//            if (MinetopiaSDBPlugin.getChannelManager() != null) {
//                MinetopiaSDBPlugin.getChannelManager().send("bank", "delete", ImmutableMap.of("id", "" + id));
//            }
//            this.silentlyDeleteBankaccount(id);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void silentlyCreateBankaccount(int id, BankAccountType type) {
        cachedAccounts.put(id, new Bankaccount(id, type, 0.0, "&fID: " + id, false, new HashMap<UUID, BankPermission>()));
    }

    public void silentlyDeleteBankaccount(int id) {
        cachedAccounts.remove(id);
    }

    public int getSlots(int accounts) {
        if (accounts <= 18) {
            return 18;
        }
        if (accounts > 45) {
            return 54;
        }
        if (accounts > 36) {
            return 45;
        }
        if (accounts > 27) {
            return 36;
        }
        if (accounts > 18) {
            return 27;
        }
        return 18;
    }

    static {
        cachedAccounts = new HashMap<>();
    }
}