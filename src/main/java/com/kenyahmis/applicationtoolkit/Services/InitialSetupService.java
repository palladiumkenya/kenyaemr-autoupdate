package com.kenyahmis.applicationtoolkit.Services;

import com.kenyahmis.applicationtoolkit.Task.InitialSetupTask;
import com.kenyahmis.applicationtoolkit.Task.RunRollBackTask;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class InitialSetupService extends Service {
    private ToolboxServiceConfiguration configuration;

    private final ToolboxController controller;

    public InitialSetupService(ToolboxController controller) {
        this.controller = controller;
    }

    public InitialSetupService(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Task createTask() {
        controller.addMessageToListFlow("Initializing a background Toolkit task");
        System.out.println("Initializing a background Toolkit task");


        return new InitialSetupTask(controller, configuration);
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }


}
