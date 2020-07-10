package LibraryManager.datamodel;

public enum Action {
    checkout("貸出"),
    returned("返却");

    private String jpName;

    Action(String nameOfAction) {
        jpName = nameOfAction;
    }
}
