package lk.ijse.citroessentional.repository;

import lk.ijse.citroessentional.db.DbConnection;
import lk.ijse.citroessentional.model.Customer;
import lk.ijse.citroessentional.model.Machine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MachineRepo {
    public static boolean save(Machine machine) throws SQLException {
        String sql = "INSERT INTO machine VALUES(?, ?, ?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, machine.getId());
        pstm.setObject(2, machine.getName());
        pstm.setObject(3, machine.getDesc());
        pstm.setObject(4, machine.getProId());


        return pstm.executeUpdate() > 0;

    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM machine WHERE  machine_mashID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Machine machine) throws SQLException {
        String sql = "UPDATE machine SET  machine_machineName  = ?, machine_machineDecs = ?,product_proID =? WHERE machine_mashID  = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, machine.getId());
        pstm.setObject(2, machine.getName());
        pstm.setObject(3, machine.getDesc());
        pstm.setObject(4, machine.getProId());

        return pstm.executeUpdate() > 0;
    }

    public static Machine searchById(String id) throws SQLException {
        String sql = "SELECT * FROM machine WHERE machine_mashID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Machine machine = null;

        if (resultSet.next()) {
            String mash_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String desc = resultSet.getString(3);
            String proId = resultSet.getString(4);

            machine = new Machine(mash_id, name, desc,proId);
        }
        return machine;
    }

    public static List<Machine> getAll() throws SQLException {
        String sql = "SELECT * FROM machine";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Machine> machineList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String desc = resultSet.getString(3);
            String proId = resultSet.getString(4);


            Machine machine = new Machine(id, name, desc,proId);
            machineList.add(machine);
        }
        return machineList;
    }

    public static List<String> getId() throws SQLException {
        String sql = "SELECT machine_mashID  FROM machine";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;

}
}