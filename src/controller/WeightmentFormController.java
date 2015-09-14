/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jssc.SerialPort;
import jssc.SerialPortException;
import mongla.port.DataBase;
import mongla.port.Msg;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class WeightmentFormController implements Initializable {
    private TextField weight_id;
    @FXML
    private TextField container_number;
    @FXML
    private TextField agent_name;
    @FXML
    private TextField mobile;
    @FXML
    private TextField driver_name;
    @FXML
    private TextField operator_name;
    @FXML
    private TextField vessel_name;
    @FXML
    private TextField rot_no;
    @FXML
    private DatePicker arr_date;
    @FXML
    private TextField be_no;
    @FXML
    private DatePicker be_date;
    @FXML
    private TextField aiin_number;
    @FXML
    private TextArea desc;
    @FXML
    private TextField weight_2nd;
    private TextField weight_1st;
    @FXML
    private TextField net_weight;
    @FXML
    private TextField number;
    @FXML
    private DatePicker date;
    @FXML
    private TextField container_name;
    @FXML
    private TextField container_weight;
    @FXML
    private TextField cont_ware_weight;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.container_weight.setText("0");
        this.cont_ware_weight.setText("0");
        this.weight_2nd.setText("0");
        this.net_weight.setText("0");
    }    

    private void onEmptyClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/TruckInit.fxml"));
        Scene scene = this.agent_name.getScene();
        Stage stage = (Stage)this.agent_name.getScene().getWindow();
        scene.setRoot(root);
        stage.setScene(scene);
        stage.setResizable(true);
    }

    @FXML
    private void onExitButtonClick(ActionEvent event) throws IOException {
        this.agent_name.getScene().getWindow().hide();
        
    }

    @FXML
    private void onReadWeightClick(ActionEvent event) {
        try {
            SerialPort serialPort = new SerialPort("COM1");
            serialPort.openPort();
            serialPort.setParams(9600, 8, 1, 0);
            if(!serialPort.isOpened()){
                Msg.showError("Port Problem");
                return;
            }
            try {
                byte[] buffer = serialPort.readBytes(100);
                String v = new String( buffer, Charset.forName("UTF-8") );
                System.out.println(v);
                System.out.println(buffer);
                //String s = v.charAt(1) + "";
                String s = v.charAt(2) + "";
                s += v.charAt(3) + "";
                s += v.charAt(4) + "";
                s += v.charAt(5) + "";
                s += v.charAt(6) + "";
                s += v.charAt(7) + "";
                System.out.println(s);
                this.weight_2nd.setText(String.valueOf(Integer.parseInt(s) + 500 - 500));
                /*int net = Integer.parseInt(this.weight_2nd.getText()) - Integer.parseInt(this.weight_1st.getText());
                if(net < 0){
                    net *= (-1);
                    String two = this.weight_2nd.getText();
                    String one = this.weight_1st.getText();
                    this.weight_1st.setText(two);
                    this.weight_2nd.setText(one);
                }
                this.net_weight.setText(String.valueOf(net));*/
                serialPort.closePort();
            } catch (SerialPortException ex) {
                Logger.getLogger(TruckInitController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(TruckInitController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            int one = Integer.parseInt(this.container_weight.getText());
            int two = Integer.parseInt(this.cont_ware_weight.getText());
            int three = Integer.parseInt(this.weight_2nd.getText());
            int four = Integer.parseInt(this.net_weight.getText());

            this.net_weight.setText(String.valueOf(three - (one + two)));
        }
    }

    private void onShowButtonClick(ActionEvent event) {
        String number = this.number.getText();
        System.out.println(number);
        DataBase db = new DataBase();
        Connection c = null;
        try {
            c = db.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs = null;
        try {
            rs = c.createStatement().executeQuery("select * from truck_init where number='"+ number +"'");
            int weight = 0;
            String name = "";
            while(rs.next()){
                name = rs.getString("name");
                weight = rs.getInt("weight");
                System.out.println(name);
                System.out.println(weight);
                break;
            }
            this.weight_1st.setText(String.valueOf(weight));
            this.driver_name.setText(name);
        } catch (SQLException ex) {
            Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                rs.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    @FXML
    private void onSaveButtonClick(ActionEvent event) throws ParseException {
        /*String number = "";
        String container_no = "";
        String agent_name = "";
        String mobile = "";
        String driver_name = "";
        String operator_name = "";
        String ain_no = "";
        String vessel_name = "";
        String rot_no = "";
        String arr_date = "";
        String be_no = "";
        String be_date = "";
        String desc = "";
        String second = "0";
        String first = "0";
        String net = "0";
        String date = "";*/
        
         /*number = this.number.getText();
         container_no = this.container_number.getText();
         agent_name = this.agent_name.getText();
         mobile = this.mobile.getText();
         driver_name = this.driver_name.getText();
         operator_name = this.operator_name.getText();
         ain_no = this.aiin_number.getText();
         vessel_name = this.vessel_name.getText();
         rot_no = this.rot_no.getText();
         arr_date = this.arr_date.getValue().toString().isEmpty() ? "" : this.arr_date.getValue().toString();
         be_no = this.be_no.getText();
         be_date = this.be_date.getValue().toString().isEmpty() ? "" : this.be_date.getValue().toString();
         desc = this.desc.getText();
         second = this.weight_2nd.getText();
         first = this.weight_1st.getText();
         net = this.net_weight.getText();
         date = this.date.getValue().toString().isEmpty() ? "" : this.date.getValue().toString();
        
        DataBase db = new DataBase();
        Connection c = null;
        try {
            c = db.getConnection();
            c.createStatement().execute("insert into report (truck_no,container_no,agent_name,mobile,driver_name,operator_name,ain_no,vessel_name,root_no,arr_date,be_no,be_date,desc,second_weight,first_weight,net_weight,date) values ('"+ number +"','"+ container_no +"','"+ agent_name +"','"+ mobile +"','"+ driver_name +"','"+ operator_name +"','"+ ain_no +"','"+ vessel_name +"','"+ rot_no +"','"+ arr_date +"','"+ be_no +"','"+ be_date +"','"+ desc +"',"+ Integer.parseInt(second) +","+ Integer.parseInt(net) +","+ Integer.parseInt(net) +",'"+ date +"')");
            Msg.showInformation("success");
            c.createStatement().execute("delete from truck_init where number='"+ number +"'");
        } catch (SQLException ex) {
            Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        DataBase db = new DataBase();
        Connection c= null;
        ResultSet rs = null;
        String id = "";
        try {
            c = db.getConnection();
            c.createStatement().execute("insert into id (test) values ('aaa')");
            rs = c.createStatement().executeQuery("select * from id order by id desc limit 1");
            
            while(rs.next()){
                id = String.valueOf(rs.getInt("id"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Vector v = new Vector();
        HashMap params = new HashMap();
        v.add("sdfsf");
        params.put("weight_id", id);
        params.put("agent_name", this.agent_name.getText());
        params.put("mobile", this.mobile.getText());
        params.put("operator_name", this.operator_name.getText());
        params.put("truck_no", this.number.getText());
        params.put("driver_name", this.driver_name.getText());
        params.put("vessel_name", this.vessel_name.getText());
        params.put("root", this.rot_no.getText() + "." + new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(this.arr_date.getValue().toString())));
        params.put("be_date", this.be_no.getText() + "." + new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(this.be_date.getValue().toString())));
        params.put("ain_no", this.aiin_number.getText());
        params.put("desc", this.desc.getText());
        params.put("container_no", this.container_number.getText());
        params.put("second", this.weight_2nd.getText() + ".k.g");
        params.put("container_name", this.container_name.getText() + "  =  " + this.container_weight.getText() + ".k.g");
        
        int a = Integer.parseInt(this.container_weight.getText()) + Integer.parseInt(this.cont_ware_weight.getText());
        int net_int = Integer.parseInt(this.weight_2nd.getText()) - a;
        params.put("cont_tare", "Cont Tare = " + this.cont_ware_weight.getText() + ".k.g");
        params.put("total", "Total  =  " + String.valueOf(a) + ".k.g");
        params.put("net", String.valueOf(net_int) + ".k.g");
        
        BufferedImage img = null;
        try {
                img = ImageIO.read(new File("src/report/logo.jpg"));
                params.put("logo", img);
        } catch (IOException ex) {
                Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception ex) {
                Logger.getLogger(WeightmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        Report r = new Report();
        r.getReport("src\\report\\Report.jrxml", new JRBeanCollectionDataSource(v), params);;
        
        
        
    }

    @FXML
    private void onContainerWeight(KeyEvent event) {
        int one = Integer.parseInt(this.container_weight.getText());
        int two = Integer.parseInt(this.cont_ware_weight.getText());
        int three = Integer.parseInt(this.weight_2nd.getText());
        int four = Integer.parseInt(this.net_weight.getText());
        
        this.net_weight.setText(String.valueOf(three - (one + two)));
    }
    
}
