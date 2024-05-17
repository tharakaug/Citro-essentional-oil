package lk.ijse.citroessentional.repository;

import lk.ijse.citroessentional.db.DbConnection;
import lk.ijse.citroessentional.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRepo {
    /*public static String currentId() throws SQLException {
        String sql = "SELECT orders_orderID FROM orders ORDER BY orders_orderID desc LIMIT 1";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }*/

    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO orders VALUES(?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, order.getId());
        pstm.setString(2, order.getDate());
        pstm.setString(3, order.getCusId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Order order)throws SQLException {
        String sql = "UPDATE orders SET  orders_orderID  = ?, orders_orderDate = ?,customer_cusID  =? WHERE orders_orderID  = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, order.getId());
        pstm.setObject(2, order.getDate());
        pstm.setObject(3, order.getCusId());

        return pstm.executeUpdate() > 0;
    }
}
