package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.TimerTask;

public class UpdateScheduler extends TimerTask {

    String localversion="";
    String remoteVersion="";

    public UpdateScheduler() throws IOException {
        ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration("","");
        URL propresources = getClass().getClassLoader().getResource("application.properties");

        //Check local application.properties
        Properties props=new Properties();
        try {
            props.load(propresources.openStream());
            localversion= props.getProperty("toolkit.emrversion");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        configuration.setRemoteproperties(props.getProperty("toolkit.remoteproperties"));
        String propFileName = configuration.getRemoteproperties();


        //Check Remote application.properties
        System.out.println("Remote props "+propFileName);
        URL u = new URL(propFileName);
        InputStream inputStream = u.openStream();
        Properties prop=new Properties();
        if (inputStream != null) {
            prop.load(inputStream);
            remoteVersion = prop.getProperty("toolkit.emrversion");

        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
        System.out.println("Local Version "+ localversion+" remote version "+remoteVersion);
        //Some stuffs
    }

    @Override
    public void run() {
        System.out.println("Hi see you after 10 seconds");
    }

}
