package fa.training.interviewmanagement.enums;

public enum EHighestLevel {

    HIGH_SCHOOL(" High school"),

    BACHELOR_S_DEGREE("Bachelor's Degree"),

    MASTER_DEGREE_PhD("Master degree, PhD ");

    final private String value;

    EHighestLevel(String value) {
        this.value = value;
    }

    public String getHighestLevel(){
        return this.value;
    }
}
