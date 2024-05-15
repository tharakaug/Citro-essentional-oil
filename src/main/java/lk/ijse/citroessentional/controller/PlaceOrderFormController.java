package lk.ijse.citroessentional.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.citroessentional.model.*;
import lk.ijse.citroessentional.model.tm.CartTm;
import lk.ijse.citroessentional.repository.CustomerRepo;
import lk.ijse.citroessentional.repository.ItemRepo;
import lk.ijse.citroessentional.repository.PlaceOrderRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceOrderFormController {

    @FXML
    private ComboBox<String> cmbCusId;

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private TableColumn<?, ?> colCusID;

    @FXML
    private TableColumn<?, ?> colCusName;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colItemID;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private Label lblCusName;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CartTm> tblOrder;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtQty;
    private String itemId;


    private ObservableList<CartTm> cartList = FXCollections.observableArrayList();

    public void initialize() {
        setCellValueFactory();
        //loadNextOrderId();
        getCustomerIds();
        getItemCodes();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colItemID.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colCusID.setCellValueFactory(new PropertyValueFactory<>("cusID"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
    }

    private void getItemCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = ItemRepo.getId();
            for (String code : codeList) {
                obList.add(code);
            }

            cmbItemId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCustomerIds() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = CustomerRepo.getId();

            for (String id : idList) {
                obList.add(id);
            }
            cmbCusId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*private void loadNextOrderId() {
        try {
            String currentId = OrderRepo.currentId();
            String nextId = nextId(currentId);

            lblOrderId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String nextId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("O");
            System.out.println("Arrays.toString(split) = " + Arrays.toString(split));
            int id = Integer.parseInt(split[1]);    //2
            return "O" + ++id;

        }
        return "O1";
    }*/


    @FXML
    void btnAddtocartOnAction(ActionEvent event) {
        String orderId = txtId.getText();
        String orderDate = txtDate.getText();

        String qty = txtQty.getText();
        String cusId = cmbCusId.getValue();
        String cusName = lblCusName.getText();



        CartTm cartTm = new CartTm(orderId, orderDate, itemId, qty, cusId,cusName);

        cartList.add(cartTm);

        tblOrder.setItems(cartList);
        txtQty.setText("");

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

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String orderId = txtId.getText();
        String date = txtDate.getText();
        String qty = txtQty.getText();
        String cusId = cmbCusId.getValue();


        var order = new Order(orderId, date, qty,cusId);

        List<OrderDetail> odList = new ArrayList<>();
        for (int i = 0; i < tblOrder.getItems().size(); i++) {
            CartTm tm = cartList.get(i);

            OrderDetail od = new OrderDetail(
                    tm.getId(),
                    tm.getCusID(),
                    tm.getQty()
            );
            odList.add(od);
        }

        PlaceOrder po = new PlaceOrder(order, odList);
        try {
            boolean isPlaced =PlaceOrderRepo.placeOrder(po);
            if(isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "order placed!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "order not placed!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

    @FXML
    void cmbCustomerIDOnAction(ActionEvent event) {
        String cusId = cmbCusId.getValue();

        try {
            Customer customer = CustomerRepo.searchById(cusId);

            lblCusName.setText(customer.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbItemIDOnAction(ActionEvent event) {
        itemId =  cmbItemId.getValue();
        try {
            Item item = ItemRepo.searchById(itemId);
            if (item != null) {
                lblCusName.setText(String.valueOf(item.getName()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtQty.requestFocus();
    }

}
