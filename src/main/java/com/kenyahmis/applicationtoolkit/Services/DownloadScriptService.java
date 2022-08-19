package com.kenyahmis.applicationtoolkit.Services;

import com.kenyahmis.applicationtoolkit.Task.DownloadScriptTask;
import com.kenyahmis.applicationtoolkit.Task.PackageDownloadTask;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DownloadScriptService extends Service {
    private ToolboxServiceConfiguration configuration;

    private final ToolboxController controller;

    public DownloadScriptService(ToolboxController controller) {
        this.controller = controller;
    }

    public DownloadScriptService(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Task createTask() {
        controller.addMessageToListFlow("Initializing a background download task");
        System.out.println("Initializing a background download task");
        return new DownloadScriptTask(controller, configuration);
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}