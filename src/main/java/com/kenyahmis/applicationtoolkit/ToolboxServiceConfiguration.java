package com.kenyahmis.applicationtoolkit;

import java.net.URL;

/**
 * A configuration model that can be used to pass information between classes/scenes
 */
public class ToolboxServiceConfiguration {
    private String userPass; // to execute shell script
    private String mysqlPass; // mysql password to be used within shell script

    private String baseDir; // where the package is downloaded and unzipped

    private String pathToSetupScript; // a concatenation of baseDir, package name, and setup_script.sh

    private URL packageDownloadUrl;
    private String packageUnzipDir;

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

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getPathToSetupScript() {
        return pathToSetupScript;
    }

    public void setPathToSetupScript(String pathToSetupScript) {
        this.pathToSetupScript = pathToSetupScript;
    }

    public URL getPackageDownloadUrl() {
        return packageDownloadUrl;
    }

    public void setPackageDownloadUrl(URL packageDownloadUrl) {
        this.packageDownloadUrl = packageDownloadUrl;
    }

    public String getPackageUnzipDir() {
        return packageUnzipDir;
    }

    public void setPackageUnzipDir(String packageUnzipDir) {
        this.packageUnzipDir = packageUnzipDir;
    }
}
