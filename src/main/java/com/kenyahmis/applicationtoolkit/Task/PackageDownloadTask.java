package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.RunUpgradeScriptService;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;
import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Download task.
 */
public class PackageDownloadTask extends Task {

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


        try (InputStream in = configuration.getPackageDownloadUrl().openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(configuration.getPackageUnzipDir())) {
            System.out.println("Downloading ...");
            controller.addMessageToListFlow("Downloading KenyaEMR update...");
            //addMessageToTextFlow("\nDownload started...", Color.GREEN, new Font(15));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            //addMessageToTextFlow("\nThere was an error..." + e.getCause(), Color.RED, new Font(15));

            System.out.println("An error occurred. " + e.getCause());
            controller.addMessageToListFlow("An error occurred. " + e.getCause());
            e.printStackTrace();

        }
        //addMessageToTextFlow("\nDownload completed...", Color.GREEN, new Font(15));

        System.out.println("KenyaEMR Download completed");

        controller.addMessageToListFlow("KenyaEMR Download completed");

        System.out.println("Unzipping started");

        controller.addMessageToListFlow("Unzipping KenyaEMR started");
        ToolkitUtils.unzip(configuration.getPackageUnzipDir(), configuration.getBaseDir());

        System.out.println("Unzipping completed");
        controller.addMessageToListFlow("Unzipping KenyaEMR completed");

        System.out.println("Executing upgrade script");
        controller.addMessageToListFlow("Executing KenyaEMR upgrade script");

        RunUpgradeScriptService service = new RunUpgradeScriptService(controller, configuration);
        service.start();

        //System.out.println("Successfully executed the upgrade script");

        return "Success";
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
