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

public class Main extends Application {

    Label appLabel;
    TextField usernameTF, searchTF;
    JTextField partNameTF, partNumberTF, partQuantityTF;
    PasswordField passwordTF;
    Button loginButton, createButton, removeButton;
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
        searchTF = new TextField("Search");
        usernameTF = new TextField("username");
        passwordTF = new PasswordField();

        partNameTF = new JTextField("Name:");
        partNumberTF = new JTextField("Part Number:");
        partQuantityTF = new JTextField("Qty:");

        loginButton = new Button("Login");
        createButton = new Button("Create");
        removeButton = new Button("Remove");
        editMButton = new MenuButton("Edit");
        separator = new Separator();

        JOptionPane createWindow = new JOptionPane();

        JPanel createPanel = new JPanel();
        createPanel.add(partNameTF);
        createPanel.add(partNumberTF);
        createPanel.add(partQuantityTF);

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
            @Override
            public void handle(ActionEvent event)
            {
                if (usernameTF.getText().equals("ZIP") && passwordTF.getText().equals("1234")) {

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
                    DatabaseConnection.Connect(inventoryTable);

                    final GridPane gridpane2 = new GridPane();

                    gridpane2.setHgap(5);
                    gridpane2.setVgap(5);
                    gridpane2.setAlignment(Pos.CENTER);
                    gridpane2.setMinSize(500, 500);

                    editMButton.getItems().addAll(createItem, deleteItem);

                    gridpane2.add(appLabel,0,0);
                    gridpane2.add(searchTF,0,1);
                    gridpane2.add(inventoryTable,0,2);
                    gridpane2.add(editMButton,0,4);
                    gridpane2.add(createButton, 0,5);

                    Scene inventoryScene = new Scene(gridpane2);
                    primaryStage.setScene(inventoryScene);
                    primaryStage.show();

                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Login credentials invalid!");
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
                    System.out.println(partNameTF.getText());
                    System.out.println(partNumberTF.getText());
                    System.out.println(partQuantityTF.getText());
                }

            }
        }
        createButton.setOnAction(new CreateEvent());

    }
}
