package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.RunUpgradeScriptService;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;
import javafx.concurrent.Task;

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


        return "Success";
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
