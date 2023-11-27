package fa.training.interviewmanagement.enums;

public enum EGender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    final private String value;

    EGender(String value) {
        this.value = value;
    }

    public String getGender() {
        return value;
    }
}
