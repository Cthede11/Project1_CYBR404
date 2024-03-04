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
            connectDatabase.close();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database failed to connect!");
        }
        return null;
    }

    static void CreateRow(JTextField name, JTextField number, JTextField quantity) {

        try {
            Connection connectDatabase = DriverManager.getConnection("jdbc:sqlite:C:\\CodeResources\\SQLite\\Mechanic_Inventory.db");
            Statement statement = connectDatabase.createStatement();
            statement.executeQuery("INSERT INTO inventory (Name, Part_Number, Quantity)" + " VALUES (" + name.getText() + ", " + number.getText() + ", " + quantity.getText() + ")");
            connectDatabase.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to create row!\nCheck on database.");
            throw new RuntimeException(e);
        }

    }

}
