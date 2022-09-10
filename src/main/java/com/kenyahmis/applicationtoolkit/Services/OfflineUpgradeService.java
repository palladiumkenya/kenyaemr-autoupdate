package com.kenyahmis.applicationtoolkit.Services;

import com.kenyahmis.applicationtoolkit.Task.OfflineUpgradeTask;
import com.kenyahmis.applicationtoolkit.Task.PackageDownloadTask;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class OfflineUpgradeService extends Service {
    private ToolboxServiceConfiguration configuration;

    private final ToolboxController controller;

    public OfflineUpgradeService(ToolboxController controller) {
        this.controller = controller;
    }

    public OfflineUpgradeService(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Task createTask() {
        controller.addMessageToListFlow("Initializing a background Copy offline files task");
        System.out.println("Initializing a background download task");
        return new OfflineUpgradeTask(controller, configuration);
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
