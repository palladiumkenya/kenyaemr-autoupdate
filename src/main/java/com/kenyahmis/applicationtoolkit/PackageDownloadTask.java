package com.kenyahmis.applicationtoolkit;

import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class PackageDownloadTask extends Task {

    private URL downloadUrl;
    private String destination;

    private ToolboxServiceConfiguration configuration;


    private final ToolboxController controller;
    public PackageDownloadTask(URL downloadUrl, String destination, ToolboxController controller) {
        this.downloadUrl = downloadUrl;
        this.destination = destination;
        this.controller = controller;
    }

    public PackageDownloadTask(URL downloadUrl, String destination, ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.downloadUrl = downloadUrl;
        this.destination = destination;
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Object call() throws Exception {


        try (InputStream in = downloadUrl.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(destination)) {
            System.out.println("Downloading ...");
            controller.addMessageToListFlow("Downloading ...");

            //addMessageToTextFlow("\nDownload started...", Color.GREEN, new Font(15));

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            //addMessageToTextFlow("\nThere was an error..." + e.getCause(), Color.RED, new Font(15));

            System.out.println("An error occured. " + e.getCause());
            controller.addMessageToListFlow("An error occured. " + e.getCause());
            e.printStackTrace();

        }
        //addMessageToTextFlow("\nDownload completed...", Color.GREEN, new Font(15));

        System.out.println("Download completed");

        controller.addMessageToListFlow("Download completed");

        System.out.println("Unzipping started");

        controller.addMessageToListFlow("Unzipping started");
        ToolkitUtils.unzip(destination, "/home/ojwang/Documents/testDownload/");

        System.out.println("Unzipping completed");
        controller.addMessageToListFlow("Unzipping completed");

        System.out.println("Executing upgrade script");
        controller.addMessageToListFlow("Executing upgrade script");

        RunUpgradeScriptService service = new RunUpgradeScriptService(controller, configuration);
        service.start();

        //System.out.println("Successfully executed the upgrade script");

        return "Success";
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
