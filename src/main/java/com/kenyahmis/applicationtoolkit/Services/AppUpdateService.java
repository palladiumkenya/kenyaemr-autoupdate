package com.kenyahmis.applicationtoolkit.Services;

import com.kenyahmis.applicationtoolkit.Task.AppUpdateTask;
import com.kenyahmis.applicationtoolkit.Task.PackageDownloadTask;
import com.kenyahmis.applicationtoolkit.Task.UpgradeToolkitTask;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class AppUpdateService extends Service {
    private ToolboxServiceConfiguration configuration;

    private final ToolboxController controller;

    public AppUpdateService(ToolboxController controller) {
        this.controller = controller;
    }

    public AppUpdateService(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Task createTask() {
        controller.addMessageToListFlow("Initializing a background App update task");
        System.out.println("Initializing a background app update task");
        return new AppUpdateTask(controller, configuration);
    }
    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
