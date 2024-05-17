package lk.ijse.citroessentional.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.citroessentional.Util.Regex;
import lk.ijse.citroessentional.model.Customer;
import lk.ijse.citroessentional.model.Employee;
import lk.ijse.citroessentional.model.Machine;
import lk.ijse.citroessentional.model.tm.CustomerTm;
import lk.ijse.citroessentional.model.tm.EmployeeTm;
import lk.ijse.citroessentional.repository.CustomerRepo;
import lk.ijse.citroessentional.repository.EmployeeRepo;
import lk.ijse.citroessentional.repository.MachineRepo;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<EmployeeTm> tblEmployee;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXComboBox<String> cmbMachineID;


    private List<Employee> employeeList = new ArrayList<>();

    public void initialize() throws SQLException {
        this.employeeList = getAllemployee();
        setCellValueFactory();
        loadEmployeeTable();
        setComboBoxValue();
    }

    private void setComboBoxValue() throws SQLException {
        List<Machine> all = MachineRepo.getAll();

            ObservableList obList = FXCollections.observableArrayList();
        for (Machine machine :all) {
            obList.add(machine.getId());
        }
        cmbMachineID.setItems(obList);
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void loadEmployeeTable() {
        ObservableList<EmployeeTm> tmList = FXCollections.observableArrayList();

        for (Employee employee : employeeList) {
            EmployeeTm employeeTm = new EmployeeTm(
                    employee.getId(),
                    employee.getName(),
                    employee.getTel(),
                    employee.getAddress()
            );

            tmList.add(employeeTm);
        }
        tblEmployee.setItems(tmList);
        EmployeeTm selectedItem = (EmployeeTm) tblEmployee.getSelectionModel().getSelectedItem();
        System.out.println("selectedItem = " + selectedItem);
    }

    private List<Employee> getAllemployee() {
        List<Employee> employeeList = null;
        try {
            employeeList = EmployeeRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtContact.getText();
        String mashId = cmbMachineID.getValue();

        Employee employee = new Employee(id, name, address, tel,mashId);

        if (isValid()) {
            try {
                boolean isSaved = EmployeeRepo.save(employee);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "employee saved!").show();
                   // loadEmployeeTable();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean isDeleted = EmployeeRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtContact.getText();
        String mashId = cmbMachineID.getValue();

        Employee employee = new Employee(id, name, address, tel,mashId);

        try {
            boolean isUpdated = EmployeeRepo.update(employee);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            Employee employee = EmployeeRepo.searchById(id);

            if (employee != null) {
                txtId.setText(employee.getId());
                txtName.setText(employee.getName());
                txtAddress.setText(employee.getAddress());
                txtContact.setText(employee.getTel());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void txtEmployeeIDOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId);
    }

    public void txtEmployeeNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.NAME,txtName);
    }

    public void txtEmployeeAddressOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ADDRESS,txtAddress);
    }

    public void txtEmplooyeeContactOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.CONTACT,txtContact);
    }


    public boolean isValid(){
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.CONTACT,txtContact)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.NAME,txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ADDRESS,txtAddress)) return false;

        return true;
    }

    public void cmbMachineIDOnAction(ActionEvent actionEvent) {

    }
}
