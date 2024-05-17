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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.citroessentional.Util.Regex;
import lk.ijse.citroessentional.Util.TextField;
import lk.ijse.citroessentional.model.Customer;
import lk.ijse.citroessentional.model.Employee;
import lk.ijse.citroessentional.model.Item;
import lk.ijse.citroessentional.model.tm.CustomerTm;
import lk.ijse.citroessentional.model.tm.ItemTm;
import lk.ijse.citroessentional.repository.CustomerRepo;
import lk.ijse.citroessentional.repository.EmployeeRepo;
import lk.ijse.citroessentional.repository.ItemRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static sun.security.krb5.internal.crypto.KeyUsage.isValid;

public class ItemFormController {

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<ItemTm> tblItem;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtQty;

    private List<Item> itemList = new ArrayList<>();

    public void initialize() {
        this.itemList = getAllItem();
        setCellValueFactory();
        loadItemTable();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
    }

    private void loadItemTable() {
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();

        for (Item item : itemList) {
            ItemTm itemTm = new ItemTm(
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    item.getQty()
            );

            tmList.add(itemTm);
        }
        tblItem.setItems(tmList);
        ItemTm selectedItem = (ItemTm) tblItem.getSelectionModel().getSelectedItem();
        System.out.println("selectedItem = " + selectedItem);
    }

    private List<Item> getAllItem() {
        List<Item> itemList = null;
        try {
            itemList = ItemRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itemList;
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());

        Item item = new Item(id, name, price, qty);

        if (isValid()) {
            try {
                boolean isSaved = ItemRepo.save(item);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "item saved!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());

        Item item = new Item(id, name, price, qty);

        try {
            boolean isUpdated = ItemRepo.update(item);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "item updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            Item item = ItemRepo.searchById(id);

            if (item != null) {
                txtId.setText(item.getId());
                txtName.setText(item.getName());
                txtPrice.setText(String.valueOf(item.getPrice()));
                txtQty.setText(String.valueOf(item.getQty()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void txtItemIDOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId);
    }

    public void txtItemNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.NAME,txtName);
    }

    public void txtItemPriceOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.PRICE,txtPrice);
    }

    public void txtItemQtyOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.QTY,txtQty);
    }


    public boolean isValid(){
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId)) return false;
        if (!Regex.setTextColor(TextField.NAME,txtName)) return false;
        if (!Regex.setTextColor(TextField.PRICE,txtPrice)) return false;
        if (!Regex.setTextColor(TextField.QTY,txtQty)) return false;

        return true;
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();

        try {
            boolean isDeleted = ItemRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "item deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    }

