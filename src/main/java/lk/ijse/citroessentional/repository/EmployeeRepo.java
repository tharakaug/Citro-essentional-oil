package lk.ijse.citroessentional.repository;
import lk.ijse.citroessentional.db.DbConnection;
import lk.ijse.citroessentional.model.Customer;
import lk.ijse.citroessentional.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class EmployeeRepo {
    public static boolean save(Employee employee)throws SQLException {
        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, employee.getId());
        pstm.setObject(2, employee.getName());
        pstm.setObject(3, employee.getAddress());
        pstm.setObject(4, employee.getTel());
        pstm.setObject(5, employee.getMashId());


        return pstm.executeUpdate() > 0;

    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET employee_name  = ?, employee_address  = ?,employee_contactNumber   = ? WHERE employee_empID  = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, employee.getId());
        pstm.setObject(2, employee.getName());
        pstm.setObject(4, employee.getTel());
        pstm.setObject(3, employee.getAddress());
      //  pstm.setObject(5, employee.getMashId());

        return pstm.executeUpdate() > 0;
    }

    public static Employee searchById(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE employee_empID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Employee employee = null;

        if (resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String tel = resultSet.getString(3);
            String address = resultSet.getString(4);
            String mashId = resultSet.getString(5);

            employee = new Employee(emp_id, name, address,tel,mashId);
        }
        return employee;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_empID = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employee";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Employee> employeeList = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String tel = resultSet.getString(4);
            String mashId = resultSet.getString(5);

            Employee employee = new Employee(id, name, address, tel,mashId);employeeList.add(employee);
        }
        return employeeList;
    }

    public static List<String> getId() throws SQLException {
        String sql = "SELECT id FROM employee";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }
}