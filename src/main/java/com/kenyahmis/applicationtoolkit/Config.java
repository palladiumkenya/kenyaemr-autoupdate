package com.kenyahmis.applicationtoolkit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Config {

    public static String zipFilePath = "C:\\Users\\User\\Downloads\\KenyaEMR_18.2.1.zip";
    public static String destDir = "C:\\Projects\\zipFiles";
    public static String batFileDestination ="C:\\Projects\\zipFiles\\echo.bat";
    public static String shFileDestination ="/opt/tmp/toolbox/bbb/KenyaEMR_18.2.1/setup_script.sh";
    public static  String name = "HMIS";
    public static String url = "https://github.com/palladiumkenya/kenyahmis-kenyaemr-autoupdate/releases/download/v18.2.1/KenyaEMR_18.2.1.zip";
    ///Trim to get version number
    public static String firstVersion = url.replace("https://github.com/palladiumkenya/kenyahmis-kenyaemr-autoupdate/releases/download/v18.2.1/KenyaEMR_","").trim();
    public static String secondVersion = firstVersion.replace("zip","").trim();
    public static int newVersion = Integer.parseInt(secondVersion.replace(".","").trim());
    //public static String url = "https://github.com/palladiumkenya/kenyahmis-kenyaemr-autoupdate/releases/download/v18.2.1/KenyaEMR_18.2.1.zip";
}
