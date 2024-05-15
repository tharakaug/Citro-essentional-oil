package lk.ijse.citroessentional.repository;

import lk.ijse.citroessentional.db.DbConnection;
import lk.ijse.citroessentional.model.Customer;
import lk.ijse.citroessentional.model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialRepo {
    public static boolean save(Material material) throws SQLException {
        String sql = "INSERT INTO material VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, material.getId());
        pstm.setObject(2, material.getName());
        pstm.setObject(3, material.getQty());
        pstm.setObject(4, material.getPrice());

        return pstm.executeUpdate() > 0;
        }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM material WHERE material_MID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Material material) throws SQLException {
        String sql = "UPDATE material SET  material_matDesc  = ?, material_matQTY  = ?, material_unitprice  = ? WHERE material_MID = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, material.getId());
        pstm.setObject(2, material.getName());
        pstm.setObject(3, material.getQty());
        pstm.setObject(4, material.getPrice());

        return pstm.executeUpdate() > 0;
    }

    public static Material searchById(String id) throws SQLException {
        String sql = "SELECT * FROM material WHERE material_MID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Material material = null;

        if (resultSet.next()) {
            String mat_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String qty = resultSet.getString(3);
            String price = resultSet.getString(4);

            material = new Material(mat_id, name, qty, price);
        }
        return material;
    }

    public static List<Material> getAll() throws SQLException {
        String sql = "SELECT * FROM material";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Material> materialList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String qty = resultSet.getString(3);
            String price = resultSet.getString(4);

            Material material = new Material(id, name, qty, price);
            materialList.add(material);
        }
        return materialList;
    }

    public static List<String> getId() throws SQLException {
        String sql = "SELECT material_MID FROM material";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }
}





