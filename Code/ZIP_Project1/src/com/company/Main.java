package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main extends Application {

    Label appLabel;
    TextField usernameTF, searchTF;
    JTextField partNameTF, partNumberTF, partQuantityTF, partNumberDeleteTF, updatePartNumber, updateQuantity;
    PasswordField passwordTF;
    Button loginButton, updateButton, searchButton, resetButton;
    MenuButton editMButton;
    Separator separator;

    MenuItem createItem = new MenuItem("Create Row");
    MenuItem deleteItem = new MenuItem("Delete Row");

    TableView inventoryTable = new TableView<Inventory>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage)
    {
        appLabel = new Label("Zebras in Pajamas");
        searchTF = new TextField();
        usernameTF = new TextField("username");
        passwordTF = new PasswordField();

        partNameTF = new JTextField("Name");
        partNumberTF = new JTextField("Part Number");
        partNumberDeleteTF = new JTextField("Part Number");
        partQuantityTF = new JTextField("Qty");
        updatePartNumber = new JTextField("Part Number");
        updateQuantity = new JTextField("Qty");

        loginButton = new Button("Login");
        updateButton = new Button("Update Qty");
        searchButton = new Button("Search");
        resetButton = new Button("Reset");
        editMButton = new MenuButton("Edit Row");
        separator = new Separator();

        JOptionPane createWindow = new JOptionPane();

        JPanel createPanel = new JPanel();
        createPanel.add(partNameTF);
        createPanel.add(partNumberTF);
        createPanel.add(partQuantityTF);

        JPanel deletePanel = new JPanel();
        deletePanel.add(partNumberDeleteTF);

        JPanel updatePanel = new JPanel();
        updatePanel.add(updatePartNumber);
        updatePanel.add(updateQuantity);

        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);

        appLabel.setFont(new Font("Book Antiqua", 20));

        Image icon = new Image("ZebraFace.png");
        primaryStage.getIcons().add(icon);

        GridPane gridpane = new GridPane();
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setMinSize(500, 500);
        gridpane.setHgap(5);
        gridpane.setVgap(5);

        gridpane.add(appLabel, 0, 0);
        gridpane.add(separator, 0,1);
        gridpane.add(usernameTF, 0, 2);
        gridpane.add(passwordTF, 0, 3);
        gridpane.add(loginButton, 0, 4);

        primaryStage.setTitle("ZIP Inventory Manager");
        primaryStage.setResizable(false);
        Scene scene = new Scene(gridpane);
        primaryStage.setScene(scene);

        primaryStage.show();

// Hash table 256
        class LoginEvent implements EventHandler<ActionEvent>
        {
            public static byte[] getSHA(String password) throws NoSuchAlgorithmException {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                return md.digest(password.getBytes(StandardCharsets.UTF_8));
            }

            public static String toHexString(byte[] hashedPassword) {
                BigInteger number = new BigInteger(1, hashedPassword);

                StringBuilder hexString = new StringBuilder(number.toString(16));

                while (hexString.length() < 64) {
                    hexString.insert(0, '0');
                }
                return hexString.toString();
            }
//Made non-static to allow catch statement to show "File not found" message in front of GUI.
            public Boolean verifyUser(String username, String password) {

                boolean userValidated = false;

                try {
                    Scanner sc = new Scanner(new File("C:\\CodeResources\\noShadowFileHere.csv"));

                    while (sc.hasNext() && ! userValidated) {
                        String line = sc.next();
                        String[] userParts = line.split(",");
                        userParts[0] = userParts[0].replace("\uFEFF", "");
                        if (userParts[0].equals(username) && userParts[1].equals(password)) {
                            userValidated = true;
                        }
                    }
                }
                catch (FileNotFoundException e) {
                    createWindow.showMessageDialog(dialog, "File not found: " + e);
                }
                return userValidated;
            }

            @Override
            public void handle(ActionEvent event)
            {
                try {
                    if (verifyUser(usernameTF.getText(), toHexString(getSHA(passwordTF.getText())))) {

                        inventoryTable.setEditable(true);

                        TableColumn partNameColumn = new TableColumn<Inventory, String>("Name");
                        TableColumn partNumberColumn = new TableColumn<Inventory, Integer>("Part #");
                        TableColumn partQuantityColumn = new TableColumn<Inventory, Integer>("Quantity");
    //For aesthetics the column width is set to fit the table
                        partQuantityColumn.setPrefWidth(73.0d);

                        partNameColumn.setCellValueFactory(new PropertyValueFactory<Inventory, String>("partName"));
                        partNumberColumn.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("partNumber"));
                        partQuantityColumn.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("partQuantity"));
    //Columns are added to the table and then populated with the connect method
                        inventoryTable.getColumns().addAll(partNameColumn, partNumberColumn, partQuantityColumn);
                        DatabaseConnection.Populate(inventoryTable);

                        final GridPane gridpane2 = new GridPane();

                        gridpane2.setHgap(5);
                        gridpane2.setVgap(5);
                        gridpane2.setAlignment(Pos.CENTER);
                        gridpane2.setMinSize(500, 500);

                        editMButton.getItems().addAll(createItem, deleteItem);

                        gridpane2.add(appLabel,1,0);
                        gridpane2.add(searchTF,1,1);
                        gridpane2.add(inventoryTable,1,2);
                        gridpane2.add(editMButton,0,3);
                        gridpane2.add(searchButton, 2, 1);
                        gridpane2.add(resetButton, 2, 2);
                        gridpane2.add(updateButton, 2, 3);

                        Scene inventoryScene = new Scene(gridpane2);
                        primaryStage.setScene(inventoryScene);
                        primaryStage.show();

                    }
                    else
                    {
                        createWindow.showMessageDialog(dialog, "Login credentials invalid!");
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        loginButton.setOnAction(new LoginEvent());


        class CreateEvent implements EventHandler<ActionEvent>
        {
            @Override
            public void handle(ActionEvent actionEvent) {
                int userOption = createWindow.showConfirmDialog(dialog, createPanel, "Create Item", JOptionPane.OK_CANCEL_OPTION);
//Print statements were for testing, will add functionality to add data to table.
                if (userOption == JOptionPane.OK_OPTION) {
                    DatabaseConnection.CreateRow(inventoryTable, partNameTF, partNumberTF, partQuantityTF);
                }

            }
        }
        createItem.setOnAction(new CreateEvent());

        class DeleteEvent implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent actionEvent) {

                int userOption = createWindow.showConfirmDialog(dialog, deletePanel, "Delete Item", JOptionPane.OK_CANCEL_OPTION);

                if (userOption == JOptionPane.OK_OPTION) {
                    int confirmUserOption = createWindow.showConfirmDialog(dialog, "Are you sure?", "Confirm DELETE?", JOptionPane.YES_NO_OPTION);
                    if (confirmUserOption == JOptionPane.YES_OPTION)
                    DatabaseConnection.DeleteRow(inventoryTable, partNumberDeleteTF);
                }
            }
        }
        deleteItem.setOnAction(new DeleteEvent());

        class SearchEvent implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent actionEvent) {
                DatabaseConnection.SearchRows(inventoryTable, searchTF);
            }
        }
        searchButton.setOnAction(new SearchEvent());

        class ResetEvent implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent actionEvent) {
                inventoryTable.getItems().clear();
                DatabaseConnection.Populate(inventoryTable);
            }
        }
        resetButton.setOnAction(new ResetEvent());

        class UpdateEvent implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent actionEvent) {

                int userOption = createWindow.showConfirmDialog(dialog, updatePanel, "Update Quantity", JOptionPane.OK_CANCEL_OPTION);
                if (userOption == JOptionPane.OK_OPTION) {
                    int confirmUserOption = createWindow.showConfirmDialog(dialog, "Are you sure?", "Confirm Update", JOptionPane.YES_NO_OPTION);
                    DatabaseConnection.UpdateQuantity(updatePartNumber, updateQuantity);

                    inventoryTable.getItems().clear();
                    DatabaseConnection.Populate(inventoryTable);
                 }
            }
        }
        updateButton.setOnAction(new UpdateEvent());
    }
}