package com.kenyahmis.applicationtoolkit.Services;

import com.kenyahmis.applicationtoolkit.Task.RunUpgradeScriptTask;
import com.kenyahmis.applicationtoolkit.Task.UpgradeToolkitTask;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UpgradeToolkitService extends Service {

    private final ToolboxController controller;
    private ToolboxServiceConfiguration configuration;

    public UpgradeToolkitService(ToolboxController controller) {
        this.controller = controller;
    }

    public UpgradeToolkitService(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }
    @Override
    protected Task createTask() {
        System.out.println("running upgrade service");
        controller.addMessageToListFlow("running upgrade service");
        return new UpgradeToolkitTask(controller, configuration);
    }
    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }
    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
