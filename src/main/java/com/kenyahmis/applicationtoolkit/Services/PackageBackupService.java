package com.kenyahmis.applicationtoolkit.Services;

import com.kenyahmis.applicationtoolkit.Task.RunBackupScriptTask;
import com.kenyahmis.applicationtoolkit.ToolboxController;
import com.kenyahmis.applicationtoolkit.ToolboxServiceConfiguration;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PackageBackupService extends Service {
    private ToolboxServiceConfiguration configuration;

    private final ToolboxController controller;

    public PackageBackupService(ToolboxController controller) {
        this.controller = controller;
    }

    public PackageBackupService(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Task createTask() {
        controller.addMessageToListFlow("Initializing a background Database Backup task");
        System.out.println("Initializing a background Database Backup task");
        return new RunBackupScriptTask(controller, configuration);
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }


}
