package nl.softcause.jsontemplates.model;

public enum  TestEnum {
    FIRST("1st"),
    SECOND("2nd");

    private final String value;

    TestEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
