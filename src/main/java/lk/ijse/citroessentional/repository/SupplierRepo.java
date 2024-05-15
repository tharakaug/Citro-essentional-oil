package lk.ijse.citroessentional.repository;

import lk.ijse.citroessentional.db.DbConnection;
import lk.ijse.citroessentional.model.Customer;
import lk.ijse.citroessentional.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {
    public static boolean save(Supplier supplier) throws SQLException {

        String sql = "INSERT INTO supplier VALUES(?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, supplier.getId());
        pstm.setObject(2, supplier.getName());
        pstm.setObject(3, supplier.getTel());


        return pstm.executeUpdate() > 0;

    }

    public static boolean update(Supplier supplier) throws SQLException {
        String sql = "UPDATE supplier SET supplier_supName   = ?, supplier_contactNO  = ? WHERE supplier_supID  = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, supplier.getId());
        pstm.setObject(2, supplier.getName());
        pstm.setObject(3, supplier.getTel());

        return pstm.executeUpdate() > 0;
    }

    public static Supplier searchById(String id) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE supplier_supID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Supplier supplier = null;

        if (resultSet.next()) {
            String sup_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String tel = resultSet.getString(3);

            supplier = new Supplier(sup_id, name, tel);
        }
        return supplier;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM supplier WHERE supplier_supID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM supplier";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supplierList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String tel = resultSet.getString(3);

            Supplier supplier = new Supplier(id, name, tel);
            supplierList.add(supplier);
        }
        return supplierList;
    }

    public static List<String> getId() throws SQLException {
        String sql = "SELECT id FROM supplier";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }
}
