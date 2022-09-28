package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.RunUpgradeScriptService;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;
import javafx.concurrent.Task;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class OfflineUpgradeTask extends Task {

    private ToolboxServiceConfiguration configuration;


    private final ToolboxController controller;
    public OfflineUpgradeTask(ToolboxController controller) {
        this.controller = controller;
    }

    public OfflineUpgradeTask(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Object call() throws Exception {
        boolean downloaded =false;

        System.out.println("KenyaEMR Download completed");

        controller.addMessageToListFlow("Copying Files Please Wait ...");

        System.out.println("KenyaEMR Copied successfully");

        controller.addMessageToListFlow("KenyaEMR Copied successfully");

        System.out.println("Unzipping started");
        String Baa = configuration.getBaseDir();
        System.out.println("Bases url "+Baa);
        controller.addMessageToListFlow("Unzipping KenyaEMR started");
        ToolkitUtils.unzip(configuration.getPathToOfflineScript(), configuration.getBaseDir());

        System.out.println("Unzipping completed");
        controller.addMessageToListFlow("Unzipping KenyaEMR completed");

        System.out.println("Executing upgrade script");
        controller.addMessageToListFlow("Executing KenyaEMR upgrade script");

            RunUpgradeScriptService service = new RunUpgradeScriptService(controller, configuration);
            service.start();

        FileInputStream ip= new FileInputStream(configuration.getPathToOfflineApplicationProperties());
        Properties wdirprop=new Properties();
        wdirprop.load(ip);
        String tookitversion=wdirprop.getProperty("toolkit.version");
        String localappversion=wdirprop.getProperty("toolkit.version");
        String emrversion=wdirprop.getProperty("toolkit.emrversion");
        String scriptversion=wdirprop.getProperty("toolkit.scriptversion");
        controller.addMessageToListFlow("Local offline Application Props "+emrversion);
        controller.addMessageToListFlow("Local Application Props "+configuration.getPathToLocalApplicationProperties());

        FileOutputStream output = new FileOutputStream(configuration.getPathToLocalApplicationProperties());
        Properties props = new Properties();
      /*  props.setProperty("toolkit.localproperties",localproperties);
        props.setProperty("toolkit.version",tookitversion);
        props.setProperty("toolkit.emrurl",remoteurl);*/
        props.setProperty("toolkit.emrversion",emrversion);
        /*props.setProperty("toolkit.remoteproperties",configuration.getRemoteproperties());
        props.setProperty("toolkit.scriptversion",scriptversion);
        props.setProperty("toolkit.version",localappversion);*/
        props.store(output, null);

       controller.addMessageToListFlow("Local offline Application Props "+props.getProperty("toolkit.emrversion"));
        return "Success";
    }
    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
