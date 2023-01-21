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