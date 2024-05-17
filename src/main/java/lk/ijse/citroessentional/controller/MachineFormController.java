package lk.ijse.citroessentional.controller;

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
import lk.ijse.citroessentional.model.Item;
import lk.ijse.citroessentional.model.Machine;
import lk.ijse.citroessentional.model.tm.CustomerTm;
import lk.ijse.citroessentional.model.tm.MachineTm;
import lk.ijse.citroessentional.repository.CustomerRepo;
import lk.ijse.citroessentional.repository.ItemRepo;
import lk.ijse.citroessentional.repository.MachineRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MachineFormController {

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<MachineTm> tblMachine;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private ComboBox<String> cmbProID;


    private List<Machine> machineList = new ArrayList<>();

    public void initialize() throws SQLException {
        this.machineList = getAllMachine();
        setCellValueFactory();
        loadMachineTable();
        setComboBoxValue();
    }

    private void setComboBoxValue() throws SQLException {
        List<Item> all = ItemRepo.getAll();

        ObservableList obList = FXCollections.observableArrayList();
        for (Item item :all) {
            obList.add(item.getId());
        }
        cmbProID.setItems(obList);
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("desc"));
    }

    private void loadMachineTable() {
        ObservableList<MachineTm> tmList = FXCollections.observableArrayList();

        for (Machine machine : machineList) {
            MachineTm machineTm = new MachineTm(
                    machine.getId(),
                    machine.getName(),
                    machine.getDesc()
            );

            tmList.add(machineTm);
        }
        tblMachine.setItems(tmList);
        MachineTm selectedItem = (MachineTm) tblMachine.getSelectionModel().getSelectedItem();
        System.out.println("selectedItem = " + selectedItem);
    }

    private List<Machine> getAllMachine() {
        List<Machine> machineList = null;
        try {
            machineList = MachineRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return machineList;
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String desc = txtDescription.getText();
        String proId = cmbProID.getValue();


        Machine machine = new Machine(id, name, desc, proId);

        if (isValid()) {
            try {
                boolean isSaved = MachineRepo.save(machine);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "machine saved!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }


    @FXML
    void btnBackOnAction(ActionEvent event)throws IOException {
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
            boolean isDeleted = MachineRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "machine deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String desc = txtDescription.getText();
        String proId = cmbProID.getValue();


        Machine machine = new Machine(id, name, desc,proId);

        try {
            boolean isUpdated = MachineRepo.update(machine);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "machine updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            Machine machine = MachineRepo.searchById(id);

            if (machine != null) {
                txtId.setText(machine.getId());
                txtName.setText(machine.getName());
                txtDescription.setText(machine.getDesc());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void txtIMachineIDOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId);
    }


    public void txtIMachineNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.NAME,txtName);
    }

    public void txtIMachineDescOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.DESCRIPTION,txtDescription);
    }
    public boolean isValid(){
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.NAME,txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.DESCRIPTION,txtDescription)) return false;

        return true;
    }

}
