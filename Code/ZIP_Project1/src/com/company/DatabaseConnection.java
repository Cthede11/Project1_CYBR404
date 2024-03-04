package com.company;

import javafx.scene.control.TableView;

import javax.swing.*;
import java.sql.*;

public class DatabaseConnection {

    //Populates TableView with current database info.
    static Inventory Connect(TableView inventoryTable) {

        Inventory partInfo;
        try {
            Connection connectDatabase = DriverManager.getConnection("jdbc:sqlite:C:\\CodeResources\\SQLite\\Mechanic_Inventory.db");
            Statement statement = connectDatabase.createStatement();
            ResultSet rs = statement.executeQuery("select * from inventory");

            while (rs.next()) {
                partInfo = new Inventory(rs.getString("Name"), rs.getInt("Part_Number"), rs.getInt("Quantity"));
                inventoryTable.getItems().add(partInfo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database failed to connect!");
            e.printStackTrace();
        }
        return null;
    }

}
