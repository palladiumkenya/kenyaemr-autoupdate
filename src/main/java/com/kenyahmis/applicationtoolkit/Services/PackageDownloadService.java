package com.kenyahmis.applicationtoolkit.Services;

import com.kenyahmis.applicationtoolkit.Task.PackageDownloadTask;
import com.kenyahmis.applicationtoolkit.ToolboxController;
import com.kenyahmis.applicationtoolkit.ToolboxServiceConfiguration;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Background service for downloading files
 */
public class PackageDownloadService extends Service {
    private ToolboxServiceConfiguration configuration;

    private final ToolboxController controller;

    public PackageDownloadService(ToolboxController controller) {
        this.controller = controller;
    }

    public PackageDownloadService(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Task createTask() {
        controller.addMessageToListFlow("Initializing a background download task");
        System.out.println("Initializing a background download task");
        return new PackageDownloadTask(controller, configuration);
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
