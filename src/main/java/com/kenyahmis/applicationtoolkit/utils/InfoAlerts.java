package com.kenyahmis.applicationtoolkit.utils;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class InfoAlerts extends Alert{
    public InfoAlerts(AlertType alertType, String s, ButtonType... buttonTypes) {
        super(alertType, s, buttonTypes);
    }
    public void EMRUpdate(String version) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("KenyaEMR Upgrade Available");
        alert.setHeaderText("KenyaEMR Version "+ version +" Upgrade is available:");
        alert.setContentText("Do you want to upgrade now?");
        alert.showAndWait();
    }
    public void NoConnection(String  smg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("KenyaHMIS Toolkit. No Internet Connection Available");
        alert.setHeaderText("Please ensure you are connected to a good internet connection");
       // alert.setContentText("Details "+smg);
        alert.showAndWait();
    }


}
