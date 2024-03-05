package com.company;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.sql.*;

public class DatabaseConnection {

    //Populates TableView with current database info.
    static void Populate(TableView inventoryTable) {

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
    }

    static void CreateRow(TableView inventoryTable, JTextField name, JTextField number, JTextField quantity) {

        try {
            Connection connectDatabase = DriverManager.getConnection("jdbc:sqlite:C:\\CodeResources\\SQLite\\Mechanic_Inventory.db");
            Statement statement = connectDatabase.createStatement();
            statement.executeUpdate("INSERT INTO inventory (Name, Part_Number, Quantity)" + " VALUES ('" + name.getText() + "', '"+ number.getText() + "', '" + quantity.getText() + "')");
            connectDatabase.close();
//TextField values are inserted into database and then added/displayed into our TableView.
            Inventory partInfo = new Inventory(name.getText(), Integer.parseInt(number.getText()), Integer.parseInt(quantity.getText()));
            inventoryTable.getItems().add(partInfo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to create row!\nCheck on database.");
            throw new RuntimeException(e);
        }

    }

    static void DeleteRow(TableView inventoryTable, JTextField number) {
        try {
            Connection connectDatabase = DriverManager.getConnection("jdbc:sqlite:C:\\CodeResources\\SQLite\\Mechanic_Inventory.db");
            Statement statement = connectDatabase.createStatement();
            statement.executeUpdate("DELETE FROM inventory WHERE Part_Number='" + number.getText() + "'");
            connectDatabase.close();

//.remove(number) refused to work so we clear and repopulate.
            inventoryTable.getItems().clear();
            DatabaseConnection.Populate(inventoryTable);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Delete Failed");
            throw new RuntimeException(e);
        }
    }

    static void SearchRows(TableView inventoryTable, TextField value) {

        Inventory partInfo;
        try {
            Connection connectDatabase = DriverManager.getConnection("jdbc:sqlite:C:\\CodeResources\\SQLite\\Mechanic_Inventory.db");
            Statement statement = connectDatabase.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM inventory WHERE Name LIKE '%" + value.getText() + "%' " + "OR Part_Number LIKE '" + value.getText() + "%' " + "OR Quantity LIKE '%" + value.getText() + "%'");

            inventoryTable.getItems().clear();

            while (rs.next()) {
                partInfo = new Inventory(rs.getString("Name"), rs.getInt("Part_Number"), rs.getInt("Quantity"));
                inventoryTable.getItems().add(partInfo);
            }
            connectDatabase.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Search Failed!\nMake sure you're inputting integers.");
            throw new RuntimeException(e);
        }
    }

}
