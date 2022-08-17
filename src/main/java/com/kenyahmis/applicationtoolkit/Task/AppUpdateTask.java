package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.RunUpgradeScriptService;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;
import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class AppUpdateTask extends Task {

    private ToolboxServiceConfiguration configuration;


    private final ToolboxController controller;
    public AppUpdateTask(ToolboxController controller) {
        this.controller = controller;
    }

    public AppUpdateTask(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Object call() throws Exception {
        try (InputStream in = configuration.getAppulr().openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(configuration.getApppackageDir())) {
            System.out.println("Downloading ...");
            controller.addMessageToListFlow("Downloading ...");
            /*int read;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }*/
            //addMessageToTextFlow("\nDownload started...", Color.GREEN, new Font(15));
            fos.flush(); //fos.write(rbc);
           fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Exception e) {
            //addMessageToTextFlow("\nThere was an error..." + e.getCause(), Color.RED, new Font(15));

            System.out.println("An error occurred.  " + e.getCause());
            controller.addMessageToListFlow("An error occurred. " + e.getCause());
            e.printStackTrace();

        }
        //addMessageToTextFlow("\nDownload completed...", Color.GREEN, new Font(15));

        System.out.println("Download completed");

        controller.addMessageToListFlow("Download completed");

        System.out.println("Restarting the KenyaHMISToolKit. Please wait ...");

        controller.addMessageToListFlow("Restarting KenyaHMISToolKit. Please wait ...");


        return "Success";
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}

