package cc.ddev.feather.api.enums;

import java.util.Arrays;

public enum BankPermission {
    WITHDRAW("opnemen", "withdraw"),
    DEPOSIT("storten", "deposit"),
    ADMIN("beide", "both");

    private final String dutch;
    private final String english;

    BankPermission(String dutch, String english) {
        this.dutch = dutch;
        this.english = english;
    }

    public String getDutch() {
        return this.dutch;
    }

    public String getEnglish() {
        return this.english;
    }

    public static BankPermission getPermission(String name) {
        return Arrays.stream(
                BankPermission.values()).filter(
                        enumValue -> enumValue.name().equalsIgnoreCase(name)
                                || enumValue.getDutch().equalsIgnoreCase(name)
                                || enumValue.getEnglish().equalsIgnoreCase(name)
                ).findFirst().orElseThrow(() -> new IllegalArgumentException("No enum constant " + BankPermission.class.getCanonicalName() + "." + name)
        );
    }
}