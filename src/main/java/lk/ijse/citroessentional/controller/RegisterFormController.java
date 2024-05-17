package lk.ijse.citroessentional.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lk.ijse.citroessentional.Util.Regex;
import lk.ijse.citroessentional.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterFormController {

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    JFXTextField txtUserId;



    @FXML
    void btnSigninOnAction(ActionEvent event) {
        String user_id = txtUserId.getText();
        String name = txtName.getText();
        String pw = txtPassword.getText();

        saveUser(user_id, name, pw);
    }

    private void saveUser(String user_id, String name, String pw) {
        if (isValid()) {
        try {
            String sql = "INSERT INTO user VALUES(?, ?, ?)";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setObject(1, user_id);
            pstm.setObject(2, name);
            pstm.setObject(3, pw);


                if (pstm.executeUpdate() > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "user saved!").show();
                }
            } catch(SQLException e){
                new Alert(Alert.AlertType.ERROR, "something happend!").show();
            }
        }
    }

    public void txtRegisterNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.NAME,txtName);
    }

    public void txtRegisterIDOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtUserId);
    }

    public void txtRegisterPasswordOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.PASSWORD,txtPassword);
    }
    public boolean isValid(){
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.NAME,txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtUserId)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.PASSWORD,txtPassword)) return false;

        return true;
    }

}


