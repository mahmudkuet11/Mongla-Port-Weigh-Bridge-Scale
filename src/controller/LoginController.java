/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mongla.port.DataBase;
import mongla.port.Msg;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class LoginController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = this.username.getText();
        String password = this.password.getText();
        DataBase db = new DataBase();
        Connection c = null;
        ResultSet rs = null;
        try {
            c = db.getConnection();
            rs = c.createStatement().executeQuery("select * from users where username='"+ username +"' and password='"+ password +"'");
            boolean success = false;
            while(rs.next()){
                success = true;
            }
            if(success){
                try {
                    this.password.getScene().getWindow().hide();
                    Parent root;
                    root = FXMLLoader.load(getClass().getResource("/view/WeightmentForm.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    scene.setRoot(root);
                    stage.setResizable(false);
                    stage.setTitle("Empty Truck Weight");
                    stage.setScene(scene);
                    stage.showAndWait();
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                Msg.showError("Login Failed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                rs.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
}
