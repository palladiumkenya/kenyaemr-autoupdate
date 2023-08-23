package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.PackageBackupService;
import com.kenyahmis.applicationtoolkit.Services.RunUpgradeScriptService;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Download task.
 */
public class PackageDownloadTask extends Task implements ShowProgress {

    private ToolboxServiceConfiguration configuration;


    private final ToolboxController controller;
    public PackageDownloadTask(ToolboxController controller) {
        this.controller = controller;
    }

    public PackageDownloadTask(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Object call() throws Exception {
        boolean downloaded =false;

        try (InputStream in = configuration.getPackageDownloadUrl().openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(configuration.getPackageUnzipDir())) {
             BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
             System.out.println("Downloading ...");
             controller.addMessageToListFlow("Downloading KenyaEMR update...");
             fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
             downloaded=true;
             fos.close();


        } catch (Exception e) {
            System.out.println("An error occurred. " + e.getCause());
            controller.addMessageToListFlow("An error occurred. " + e.getCause());
            e.printStackTrace();

        }
        System.out.println("KenyaEMR Download completed");

        controller.addMessageToListFlow("KenyaEMR Download completed");

        System.out.println("Unzipping started");

        controller.addMessageToListFlow("Unzipping KenyaEMR started");
        ToolkitUtils.unzip(configuration.getPackageUnzipDir(), configuration.getBaseDir());

        System.out.println("Unzipping completed");
        controller.addMessageToListFlow("Unzipping KenyaEMR completed");

        System.out.println("Executing upgrade script");
        controller.addMessageToListFlow("Executing KenyaEMR upgrade script");


        if(downloaded==true) {

            PackageBackupService packageBackupService = new PackageBackupService(controller,configuration);
            packageBackupService.start();

            RunUpgradeScriptService service = new RunUpgradeScriptService(controller, configuration);
            service.start();
        }

        return "Success";
    }

    /** 
     * Show or hide the progress spinner
     * @param status - boolean - true: show spinner, false: hide spinner
    */
    public void showProgress(boolean status) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.showProgress(status);
            }
        });
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
