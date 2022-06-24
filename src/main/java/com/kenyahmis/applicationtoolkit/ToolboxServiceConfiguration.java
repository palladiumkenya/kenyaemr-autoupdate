package com.kenyahmis.applicationtoolkit;

public class ToolboxServiceConfiguration {
    private String userPass;
    private String mysqlPass;

    public ToolboxServiceConfiguration(String userPass, String mysqlPass) {
        this.userPass = userPass;
        this.mysqlPass = mysqlPass;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getMysqlPass() {
        return mysqlPass;
    }

    public void setMysqlPass(String mysqlPass) {
        this.mysqlPass = mysqlPass;
    }
}
