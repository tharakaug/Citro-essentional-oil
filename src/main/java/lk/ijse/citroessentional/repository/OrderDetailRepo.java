package lk.ijse.citroessentional.repository;

import lk.ijse.citroessentional.db.DbConnection;
import lk.ijse.citroessentional.model.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailRepo {
    public static boolean save(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            boolean isSave = save(od);
            if(!isSave) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(OrderDetail od) throws SQLException {
        String sql = "INSERT INTO order_detail VALUES(?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(2, od.getId());
        pstm.setString(1, od.getItemId());
        pstm.setString(3, String.valueOf(od.getQty()));
        return pstm.executeUpdate() > 0;

    }
}
