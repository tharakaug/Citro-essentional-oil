package lk.ijse.citroessentional.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.citroessentional.Util.Regex;
import lk.ijse.citroessentional.model.Customer;
import lk.ijse.citroessentional.model.Material;
import lk.ijse.citroessentional.model.tm.CustomerTm;
import lk.ijse.citroessentional.model.tm.MaterialTm;
import lk.ijse.citroessentional.repository.CustomerRepo;
import lk.ijse.citroessentional.repository.MachineRepo;
import lk.ijse.citroessentional.repository.MaterialRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialFormController {

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<MaterialTm> tblMaterial;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtQty;

    private List<Material> materialList = new ArrayList<>();

    public void initialize() {
        this.materialList = getAllmaterial();
        setCellValueFactory();
        loadMaterialTable();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void loadMaterialTable() {
        ObservableList<MaterialTm> tmList = FXCollections.observableArrayList();

        for (Material material : materialList) {
            MaterialTm materialTm = new MaterialTm(
                    material.getId(),
                    material.getName(),
                    material.getQty(),
                    material.getPrice()
            );

            tmList.add(materialTm);
        }
        tblMaterial.setItems(tmList);
        MaterialTm selectedItem = (MaterialTm) tblMaterial.getSelectionModel().getSelectedItem();
        System.out.println("selectedItem = " + selectedItem);
    }

    private List<Material> getAllmaterial() {
        List<Material> materialList = null;
        try {
            materialList = MaterialRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return materialList;
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtDescription.getText();
        String qty = txtQty.getText();
        String price = txtPrice.getText();

        Material material = new Material(id, name, qty, price);

        if (isValid()) {
            try {
                boolean isSaved = MaterialRepo.save(material);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "material saved!").show();
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
            boolean isDeleted = MaterialRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "material deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtDescription.getText();
        String qty = txtQty.getText();
        String price = txtPrice.getText();

        Material material = new Material(id, name, qty, price);

        try {
            boolean isUpdated = MaterialRepo.update(material);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "material updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            Material material = MaterialRepo.searchById(id);

            if (material != null) {
                txtId.setText(material.getId());
                txtDescription.setText(material.getName());
                txtQty.setText(material.getQty());
                txtPrice.setText(material.getPrice());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void txtMaterialIDOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId);
    }

    public void txtMaterialDescOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.DESCRIPTION,txtDescription);
    }

    public void txtMaterialUnitpriceOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.PRICE,txtPrice);
    }

    public void txtMaterialQtyOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.QTY,txtQty);
    }
    public boolean isValid(){
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.DESCRIPTION,txtDescription)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.PRICE,txtPrice)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.QTY,txtQty)) return false;

        return true;
    }

}
