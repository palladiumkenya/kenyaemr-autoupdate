package com.kenyahmis.applicationtoolkit;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.net.URL;

public class RunUpgradeScriptService extends Service {

    private final ToolboxController controller;
    private ToolboxServiceConfiguration configuration;

    public RunUpgradeScriptService(ToolboxController controller) {
        this.controller = controller;
    }

    public RunUpgradeScriptService(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }


    @Override
    protected Task createTask() {
        System.out.println("running upgrade service");
        controller.addMessageToListFlow("running upgrade service");
        return new RunUpgradeScriptTask(controller, configuration);
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
