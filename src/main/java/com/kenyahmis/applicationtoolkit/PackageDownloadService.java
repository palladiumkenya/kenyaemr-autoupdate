package com.kenyahmis.applicationtoolkit;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.net.URL;

public class PackageDownloadService extends Service {

    private URL downloadUrl;
    private String destination;
    private ToolboxServiceConfiguration configuration;

    private final ToolboxController controller;

    public PackageDownloadService(URL downloadUrl, String destination, ToolboxController controller) {
        this.downloadUrl = downloadUrl;
        this.destination = destination;
        this.controller = controller;
    }

    public PackageDownloadService(URL downloadUrl, String destination, ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.downloadUrl = downloadUrl;
        this.destination = destination;
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Task createTask() {
        controller.addMessageToListFlow("Initializing a background download task");
        System.out.println("Initializing a background download task");
        return new PackageDownloadTask(downloadUrl, destination, controller, configuration);
    }

    public URL getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(URL downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
