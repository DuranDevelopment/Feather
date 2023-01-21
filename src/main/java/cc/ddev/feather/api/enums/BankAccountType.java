package cc.ddev.feather.api.enums;

public enum BankAccountType {
    PERSONAL("PERSONAL"),
    SAVINGS("SAVINGS"),
    GOVERNMENT("GOVERNMENT"),
    BUSINESS("BUSINESS");

    private final String name;

    BankAccountType(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return this.name;
    }
}