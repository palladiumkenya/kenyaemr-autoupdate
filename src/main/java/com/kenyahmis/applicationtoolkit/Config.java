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
    public static String shFileDestination ="/home/ojwang/Documents/testDownload/KenyaEMR_18.2.1/setup_script.sh";
//    public static JSONParser jsonParser = new JSONParser();
//    public static FileReader reader;
//
//    static {
//        try {
//            reader = new FileReader(".\\jsonfiles\\data.json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Object obj;
//
//    static {
//        try {
//            obj = jsonParser.parse(reader);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

   // public static JSONObject dataJsonobj = (JSONObject)obj;
    //public static  String name = (String) dataJsonobj.get("name");
    public static  String name = "HMIS";
  //  public String version = (String) dataJsonobj.get("version");
    //public static String url = (String) dataJsonobj.get("url");
    public static String url = "https://github.com/palladiumkenya/kenyahmis-kenyaemr-autoupdate/releases/download/v18.2.1/KenyaEMR_18.2.1.zip";




    public Config() throws IOException, ParseException {
    }
}
