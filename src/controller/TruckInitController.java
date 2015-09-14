/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;
import mongla.port.DataBase;
import mongla.port.Msg;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class TruckInitController implements Initializable {
    @FXML
    private TextField number;
    @FXML
    private TextField name;
    @FXML
    private TextField weight;
    private Label info;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    @FXML
    private void onReadWeightButtonClick(ActionEvent event) {
        try {
            SerialPort serialPort = new SerialPort("COM1");
            serialPort.openPort();
            if(!serialPort.isOpened()){
                Msg.showError("Port Problem");
                return;
            }
            try {
                byte[] buffer = serialPort.readBytes(11);
                String v = new String( buffer, Charset.forName("UTF-8") );
                //String s = v.charAt(1) + "";
                String s = v.charAt(2) + "";
                s += v.charAt(3) + "";
                s += v.charAt(4) + "";
                s += v.charAt(5) + "";
                s += v.charAt(6) + "";
                s += v.charAt(7) + "";
                this.weight.setText(String.valueOf(Integer.parseInt(s) + 500 - 500));
                
                serialPort.closePort();
            } catch (SerialPortException ex) {
                Logger.getLogger(TruckInitController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(TruckInitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onSaveButtonClickl(ActionEvent event) {
        String number = this.number.getText();
        String name = this.name.getText();
        int weright = Integer.parseInt(this.weight.getText());
        
        DataBase db = new DataBase();
        Connection c = null;
        try {
            c = db.getConnection();
            c.createStatement().execute("insert into truck_init (number,name,weight) values ('"+ number +"','"+ name +"',"+ weright +")");
            Msg.showInformation("success");
            this.name.setText("");
            this.number.setText("");
            this.weight.setText("");
        } catch (SQLException ex) {
            Logger.getLogger(TruckInitController.class.getName()).log(Level.SEVERE, null, ex);
            Msg.showError("");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TruckInitController.class.getName()).log(Level.SEVERE, null, ex);
            Msg.showError("");
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(TruckInitController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void onExitButtonClick(ActionEvent event) throws IOException {
        //this.name.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/view/WeightmentForm.fxml"));
        Scene scene = this.name.getScene();
        Stage stage = (Stage)name.getScene().getWindow();
        scene.setRoot(root);
        stage.setScene(scene);
        stage.setResizable(true);
    }
    
}
