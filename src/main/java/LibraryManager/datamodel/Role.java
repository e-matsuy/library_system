package LibraryManager.datamodel;

import java.util.Arrays;

public enum Role{
    user("利用者"),
    admin("管理者"),
    bot( "ボット");

    private String jpName;

    Role(String nameOfRole) {
        jpName = nameOfRole;
    }

    public String jpName(){
        return this.jpName;
    }

    /**
     * @param name 役職の日本語名
     * @return 引数に該当するRoleオブジェクト
     */
    public static Role getByJpName(String name){
        return Arrays.stream(Role.values())
                .filter(data -> data.jpName().equals(name))
                .findFirst()
                .orElse(null);
    }
}