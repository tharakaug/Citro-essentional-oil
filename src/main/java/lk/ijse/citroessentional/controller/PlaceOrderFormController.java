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
import lk.ijse.citroessentional.model.*;
import lk.ijse.citroessentional.model.tm.CartTm;
import lk.ijse.citroessentional.repository.CustomerRepo;
import lk.ijse.citroessentional.repository.ItemRepo;
import lk.ijse.citroessentional.repository.OrderRepo;
import lk.ijse.citroessentional.repository.PlaceOrderRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlaceOrderFormController {

    @FXML
    private ComboBox<String> cmbCusId;

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colCusName;

    @FXML
    private TableColumn<?, ?> colCusID;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

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
    private Label lblUnitPrice;

    @FXML
    private Label lblNetTotal;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CartTm> tblOrder;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtQty;

    private ObservableList<CartTm> cartList = FXCollections.observableArrayList();
    private double netTotal = 0;

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
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
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
        String itemId = cmbItemId.getValue();
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double total = unitPrice * qty;
        String cusID = cmbCusId.getValue();
        String cusName = lblCusName.getText();

        for (int i = 0; i < tblOrder.getItems().size(); i++) {
            if (orderId.equals(colItemID.getCellData(i))) {
                qty += cartList.get(i).getQty();
                total = unitPrice * qty;

                cartList.get(i).setQty(qty);
                cartList.get(i).setTotal(total);

                tblOrder.refresh();
                calculateNetTotal();
                txtQty.setText("");
                return;
            }
        }

        CartTm cartTm = new CartTm(orderId, orderDate, itemId,unitPrice, qty,total,cusID,cusName);

        cartList.add(cartTm);

        tblOrder.setItems(cartList);
        txtQty.setText("");
        calculateNetTotal();


    }

    private void calculateNetTotal() {
        netTotal = 0;
        for (int i = 0; i < tblOrder.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }
        lblNetTotal.setText(String.valueOf(netTotal));
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
        String cusId = cmbCusId.getValue();


        var order = new Order(orderId, date, cusId);

        List<OrderDetail> odList = new ArrayList<>();
        for (int i = 0; i < tblOrder.getItems().size(); i++) {
            CartTm tm = cartList.get(i);

            OrderDetail od = new OrderDetail(
                    tm.getId(),
                    tm.getItemID(),
                    tm.getQty()
            );
            odList.add(od);
        }
        PlaceOrder po = new PlaceOrder(order, odList);

        if (isValid()) {
            try {
                boolean isPlaced = PlaceOrderRepo.placeOrder(po);
                if (isPlaced) {
                    new Alert(Alert.AlertType.CONFIRMATION, "order placed!").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "order not placed!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String date = txtDate.getText();
        String cusId = cmbCusId.getValue();

        Order order = new Order(id,date,cusId);

        try {
            boolean isUpdated = OrderRepo.update(order);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "order updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
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
        String itemId =  cmbItemId.getValue();
        try {
            Item item = ItemRepo.searchById(itemId);
            if (item != null) {
                lblUnitPrice.setText(String.valueOf(item.getPrice()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtQty.requestFocus();
    }

    public void btnPrintBillOnAction(ActionEvent actionEvent) {
        HashMap hashmap = new HashMap<>();
        hashmap.put("name", lblCusName.getText());
        hashmap.put("cusID", cmbCusId.getValue());
        hashmap.put("orderID", txtId.getText());
        hashmap.put("unitPrice", lblUnitPrice.getText());
        hashmap.put("total", lblNetTotal.getText());

        try {
            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/Report/citro.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hashmap, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            throw new RuntimeException(e);
}
    }

    public void txtOrderIDOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId);
    }

    public void txtOrderQtyOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.QTY,txtQty);
    }

    public void txtOrderDateOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.DATE,txtDate);
    }
    public boolean isValid(){
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.ID,txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.DATE,txtDate)) return false;
        if (!Regex.setTextColor(lk.ijse.citroessentional.Util.TextField.QTY,txtQty)) return false;

        return true;
    }

}
