package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DTO.PreferredShiftDTO;

public class ShiftReqRolesDAO {

    private Connection connection;

    public ShiftReqRolesDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing ShiftReqRolesDAO: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS shift_req_roles ("
                + "shiftId INT NOT NULL, "
                + "role TEXT NOT NULL, "
                + "amount INT NOT NULL, "
                + "PRIMARY KEY (shiftId, role), "
                + "FOREIGN KEY (shiftId) REFERENCES shifts(id)"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shift_req_roles table: " + e.getMessage());
            throw e;
        }
    }

    //להוסיף פונקציות כמו בשאר הטבלאות

   

    
}
