/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongla.port;

import java.awt.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author hp
 */
public class Msg {
    public static void showInformation(String msg){
        if(msg.equals("")){
            msg = "Successfully Saved!!!";
        }
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        //alert.setGraphic(new ImageView(new Image("images/success.jpg")));
        alert.showAndWait();
    }
    
    public static void showError(String msg){
        if(msg.equals("")){
            msg = "Sorry there is an error. Please try again!!!";
        }
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        //alert.setGraphic(new ImageView(new Image("images/error.jpg")));
        alert.showAndWait();
    }
}
