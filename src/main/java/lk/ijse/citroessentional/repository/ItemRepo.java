package lk.ijse.citroessentional.repository;

import lk.ijse.citroessentional.db.DbConnection;
import lk.ijse.citroessentional.model.Customer;
import lk.ijse.citroessentional.model.Employee;
import lk.ijse.citroessentional.model.Item;
import lk.ijse.citroessentional.model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepo {
    public static boolean save(Item item)throws SQLException {
        String sql = "INSERT INTO product VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, item.getId());
        pstm.setObject(2, item.getName());
        pstm.setObject(3, item.getPrice());
        pstm.setObject(4, item.getQty());


        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException{
        String sql = "DELETE FROM product WHERE product_proID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Item item) throws SQLException {
        String sql = "UPDATE product SET product_proname  = ?, product_price = ?, product_proQTY = ? WHERE product_proID = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, item.getId());
        pstm.setObject(2, item.getName());
        pstm.setObject(3, item.getPrice());
        pstm.setObject(4, item.getQty());

        return pstm.executeUpdate() > 0;
    }

    public static Item searchById(String id) throws SQLException{
        String sql = "SELECT * FROM product WHERE product_proID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Item item = null;

        if (resultSet.next()) {
            String pro_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            double price = resultSet.getDouble(3);
            int qty = resultSet.getInt(4);

            item = new Item(pro_id, name, price, qty);
        }
        return item;
    }

    public static List<Item> getAll() throws SQLException {
        String sql = "SELECT * FROM product";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Item> itemList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            double price = resultSet.getDouble(3);
            int qty = resultSet.getInt(4);

            Item item = new Item(id, name, price, qty);
            itemList.add(item);
        }
        return itemList;
    }

    public static List<String> getId() throws SQLException {
        String sql = "SELECT product_proID FROM product";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }
    public static boolean updateQty(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            if(!updateQty(od)) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(OrderDetail od) throws SQLException {
        String sql = "UPDATE product SET product_proQTY  = product_proQTY  - ? WHERE product_proID  = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, String.valueOf(od.getQty()));
        pstm.setString(2, od.getItemId());

        return pstm.executeUpdate() > 0;
    }
}
