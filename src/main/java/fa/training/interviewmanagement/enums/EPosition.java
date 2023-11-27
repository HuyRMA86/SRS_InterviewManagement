package fa.training.interviewmanagement.enums;

public enum EPosition {

    BACKEND_DEVELOPER("Backend Developer"),
    BUSINESS_ANALYST("Business Analyst"),
    TESTER("Tester"),
    HR("HR"),
    PROJECT_MANAGER("Project manager");

    final private String value;

    EPosition(String value) {
        this.value = value;
    }

    public String getPosition(){
        return value;
    }
}
