package cc.ddev.feather.api.phone;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.api.objects.Phone;
import cc.ddev.feather.api.objects.PhoneContact;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PhoneContactsModel;
import cc.ddev.feather.database.models.PhoneModel;
import com.craftmend.storm.api.enums.Where;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PhoneManager {
    private static PhoneManager instance;

    public static PhoneManager getInstance() {
        if (instance == null) {
            instance = new PhoneManager();
        }
        return instance;
    }

    public CompletableFuture<Phone> getPhone(int phonenumber) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Collection<PhoneModel> phoneModels =
                        StormDatabase.getInstance().getStorm().buildQuery(PhoneModel.class)
                                .where("phone_number", Where.EQUAL, phonenumber)
                                .limit(1)
                                .execute()
                                .join();
                PhoneModel phoneModel = phoneModels.iterator().next();

                double credit = phoneModel.getCredit();
                UUID owner = phoneModel.getOwner();

                return new Phone(phonenumber, credit, owner);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Phone> createNewPhone(int phoneNumber, UUID owner) {
        return CompletableFuture.supplyAsync(() -> {
            Phone phone;
            try {
                PhoneModel phoneModel = new PhoneModel();
                phoneModel.setPhoneNumber(phoneNumber);
                phoneModel.setCredit(Config.Phone.DEFAULT_CREDIT);
                phoneModel.setOwner(owner);
                StormDatabase.getInstance().saveStormModel(phoneModel);
            } catch (Exception e) {
                e.printStackTrace();
            }

            phone = new Phone(phoneNumber, Config.Phone.DEFAULT_CREDIT, owner);
            return phone;
        });
    }

    public CompletableFuture<List<Phone>> getAllPhones() {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<Phone> phones = new ArrayList<>();

            try {
                Collection<PhoneModel> phoneModels =
                        StormDatabase.getInstance().getStorm().buildQuery(PhoneModel.class)
                                .execute()
                                .join();
                for (PhoneModel phoneModel : phoneModels) {
                    phones.add(new Phone(phoneModel.getPhoneNumber(), phoneModel.getCredit(), phoneModel.getOwner()));
                }
                return phones;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return phones;
        });
    }

    public CompletableFuture<List<PhoneContact>> getAllContacts(int phonenumber) {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<PhoneContact> contacts = new ArrayList<>();

            try {
                Collection<PhoneContactsModel> phoneContactsModels =
                        StormDatabase.getInstance().getStorm().buildQuery(PhoneContactsModel.class)
                                .execute()
                                .join();
                for (PhoneContactsModel phoneContactsModel : phoneContactsModels) {
                    contacts.add(new PhoneContact(phoneContactsModel.getId(), phoneContactsModel.getContactPhoneNumber(), getPhone(phonenumber).get().getOwner()));
                }
                return contacts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return contacts;
        });
    }

    public CompletableFuture<Integer> addContact(int phonenumber, int newContact) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PhoneContactsModel phoneContactsModel = new PhoneContactsModel();
                phoneContactsModel.setPhoneNumber(phonenumber);
                phoneContactsModel.setContactPhoneNumber(newContact);

                StormDatabase.getInstance().saveStormModel(phoneContactsModel);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    public CompletableFuture<Integer> removeContact(int phonenumber, int contact) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Collection<PhoneContactsModel> phoneContactsModels =
                        StormDatabase.getInstance().getStorm().buildQuery(PhoneContactsModel.class)
                                .where("phone_number", Where.EQUAL, phonenumber)
                                .where("contact_phone_number", Where.EQUAL, contact)
                                .limit(1)
                                .execute()
                                .join();
                PhoneContactsModel phoneContactsModel = phoneContactsModels.iterator().next();
                StormDatabase.getInstance().getStorm().delete(phoneContactsModel);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    public int getRandomPhoneNumber() {
        int number = 10000000 + (int) (Math.random() * 9.0E7);
        if (String.valueOf(number).startsWith("06")) {
            return this.getRandomPhoneNumber();
        }
        return number;
    }

    public boolean isAvailable(int phonenumber) {
        boolean available = false;
        try {
            Collection<PhoneModel> phoneModels =
                    StormDatabase.getInstance().getStorm().buildQuery(PhoneModel.class)
                            .where("phone_number", Where.EQUAL, phonenumber)
                            .limit(1)
                            .execute()
                            .join();
            if (phoneModels.isEmpty()) {
                available = true;
            }
            return available;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }
}