package lk.ijse.citroessentional.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lk.ijse.citroessentional.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterFormController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    TextField txtUserId;



    @FXML
    void btnSigninOnAction(ActionEvent event) {
        String user_id = txtUserId.getText();
        String name = txtName.getText();
        String pw = txtPassword.getText();

        saveUser(user_id, name, pw);
    }

    private void saveUser(String user_id, String name, String pw) {
        try {
            String sql = "INSERT INTO user VALUES(?, ?, ?)";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setObject(1, user_id);
            pstm.setObject(2, name);
            pstm.setObject(3, pw);

            if(pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "user saved!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happend!").show();
        }
    }
    }


