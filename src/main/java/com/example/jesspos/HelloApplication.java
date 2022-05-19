package com.example.jesspos;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class HelloApplication extends Application {
    public GridPane content = new GridPane();
    public Scene scene = new Scene(content);
    public final FileHandler FH = new FileHandler();
    public int employeeID = FH.getTimesFile().getSignedInID();
    public boolean signedIn = employeeID > 0;
    @Override public void start(Stage stage) {
        home(stage);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setMinWidth(800);
        stage.setMinHeight(800);
        stage.show();
    }
    public void home(Stage stage) {
        sceneClear();
        stage.setTitle("JessPOS - Home");
        MenuBtn account;
        if(signedIn) {
            account = new MenuBtn(("Currently signed in as: " + FH.getEmployeesFile().getEmployee(employeeID).getName() + ". (ID: " + employeeID + ")"), "btn-secondary", "Access your timetable");
            account.setOnAction(actionEvent -> employee(stage));
        } else {
            account = new MenuBtn("Currently not signed in.", "", "Sign in");
            account.setOnAction(actionEvent -> signIn(stage));
        }
        MenuBtn inventoryBtn = new MenuBtn("Open Inventory", "btn-success", "Access inventory management");
        inventoryBtn.setOnAction(actionEvent -> inventory(stage));
        MenuBtn transactionBtn = new MenuBtn("New Transaction", "btn-primary", "Open a new transaction");
        transactionBtn.setOnAction(actionEvent -> {
            if(signedIn) {
                transaction(stage);
            } else {
                if(signIn(stage)) {
                    transaction(stage);
                }
            }
        });
        MenuBtn viewTransactionsBtn = new MenuBtn("View transactions", "btn-warning", "View previously finished transactions");
        viewTransactionsBtn.setOnAction(actionEvent -> transactions(stage));
        MenuBtn employeesBtn = new MenuBtn("Manage employees and timetables", "btn-danger", "Manage your timetable and punch in/out");
        employeesBtn.setOnAction(actionEvent -> employee(stage));
        content.setAlignment(Pos.CENTER);
        account.setAlignment(Pos.CENTER);

        content.add(account, 0, 0, 2, 1);
        content.addRow(1, inventoryBtn, transactionBtn);
        content.addRow(2, viewTransactionsBtn, employeesBtn);
        sceneInit(2, 3);
    }
    public boolean signIn(Stage stage) {
        TextInputDialog getEmployeeID = new TextInputDialog("Enter employee ID");
        getEmployeeID.setHeaderText("Employee ID is required to punch in");
        getEmployeeID.setTitle("Punch in");
        getEmployeeID.showAndWait();
        String input = getEmployeeID.getEditor().getText();
        if(input.matches("^[^0-9]+$")) {
            UIAlert failure = new UIAlert("Failure", "ID is blank or invalid, please try again.", ButtonType.OK, ButtonType.CLOSE);
            failure.showAndWait();
            if (failure.getResult() == ButtonType.OK) {
                home(stage);
            } else {
                failure.close();
            }
            return false;
        } else {
            int ID = Integer.parseInt(getEmployeeID.getEditor().getText());
            employeeID = ID;
            signedIn = true;
            UIAlert success = new UIAlert("Success", "Punched in successfully", ButtonType.OK, ButtonType.CANCEL);
            success.showAndWait();
            FH.getTimesFile().punchIn(ID);
            home(stage);
            return true;
        }
    }
    public void sceneClear() {
        content.getChildren().clear();
        content.getColumnConstraints().clear();
        content.getRowConstraints().clear();
    }
    public void sceneInit(int x, int y) {
        content.setAlignment(Pos.CENTER);
        for (int i = 0 ; i < x ; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/x);
            cc.setHgrow(Priority.ALWAYS);
            content.getColumnConstraints().add(cc);
        }
        for (int i = 0 ; i < y; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/y);
            rc.setVgrow(Priority.ALWAYS);
            content.getRowConstraints().add(rc);
        }
        for(Node n : content.getChildren()) {
            if(n instanceof Button
                    || n instanceof VBox
                    || n instanceof HBox
                    || n instanceof TableView<?>) {
                ((Region) n).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            }
        }
    }
    public void inventory(Stage stage)
    {
        sceneClear();
        stage.setTitle("JessPOS - Inventory Management");
        TableView tbv = new TableView(FH.getInventoryFile().getItems());
        tbv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn SKUCol = new TableColumn("SKU");
        SKUCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("SKU"));
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("name"));
        TableColumn quantCol = new TableColumn("Quantity in stock");
        quantCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("quantity"));
        TableColumn priceCol = new TableColumn("Price (CAD $)");
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Item, Double>("price"));

        tbv.getColumns().addAll(SKUCol, nameCol, quantCol, priceCol);
        TextField addName = new TextField("Product name");
        addName.setMinHeight(50);
        TextField addQuantity = new TextField("Quantity in stock");
        addQuantity.setMinHeight(50);
        TextField addPrice = new TextField("Price (CAD $)");
        addPrice.setMinHeight(50);

        MenuBtn addButton = new MenuBtn("Add [Enter]", "btn-primary", "Add new product with above details");
        addButton.setOnAction(e -> {
            if(addName.getText().matches("^[A-Za-z\s]+$")
                    && addQuantity.getText().matches("^[0-9]+$")
                    && addPrice.getText().matches("^[0-9]+.[0-9]{2}$")) {
                FH.getInventoryFile().addItem(addName.getText(), addQuantity.getText(), addPrice.getText());
                addName.clear();
                addQuantity.clear();
                addPrice.clear();
                tbv.setItems(FH.getInventoryFile().getItems());
                addName.requestFocus();
            } else {
                UIAlert invalidEntries = new UIAlert("Invalid product properties", "Some or all of your inputs are blank or invalid.\nDon't forget that product names should contain only letters or spaces, quantity only integers, and price only doubles (number with 2 decimals).", ButtonType.OK, ButtonType.CLOSE);
                invalidEntries.showAndWait();
                addName.clear();
                addQuantity.clear();
                addPrice.clear();
                addName.requestFocus();
            }
        });
        addButton.setDefaultButton(true);
        MenuBtn removeButton = new MenuBtn("Remove", "btn-danger", "Remove highlighted row");
        removeButton.setOnAction(actionEvent -> {
            Item item = (Item) tbv.getSelectionModel().getSelectedItem();
            FH.getInventoryFile().removeItem(item.getSKU());
            tbv.setItems(FH.getInventoryFile().getItems());
        });
        VBox entryForm = new VBox();
        VBox buttonsHolder = new VBox();
        entryForm.getChildren().addAll(addName,addQuantity,addPrice,addButton);
        content.add(tbv,0,0, 2, 2);
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(actionEvent -> home(stage));
        back.setCancelButton(true);
        buttonsHolder.getChildren().addAll(back, removeButton);
        content.add(buttonsHolder, 2, 0, 1, 1);
        content.add(entryForm,2,1, 1, 1);
        content.getStyleClass().add("bg-success");
        sceneInit(3, 2);
    }
    public void transactions(Stage stage)
    {
        sceneClear();
        stage.setTitle("JessPOS - Transaction Management");
        TableView tbv = new TableView(FH.getTransactionsFile().getTransactions());
        tbv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn IDCol = new TableColumn("ID");
        IDCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, Integer>("ID"));
        TableColumn DateCol = new TableColumn("Date");
        DateCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, Instant>("date"));
        TableColumn ItemsCountCol = new TableColumn("Number of items");
        ItemsCountCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, Integer>("itemsCount"));
        TableColumn PriceDeltaCol = new TableColumn("Total cost");
        PriceDeltaCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, Integer>("priceDelta"));

        tbv.getColumns().addAll(IDCol, DateCol, ItemsCountCol, PriceDeltaCol);

        TableView fullTransaction = new TableView();
        fullTransaction.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn SKUCol = new TableColumn("SKU");
        SKUCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("SKU"));
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("name"));
        TableColumn quantCol = new TableColumn("Quantity in stock");
        quantCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("quantity"));
        TableColumn priceCol = new TableColumn("Price (CAD $)");
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Item, Double>("price"));

        fullTransaction.getColumns().addAll(SKUCol, nameCol, quantCol, priceCol);
        Text details = new Text();
        tbv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                details.setText("Transaction completed by: " + ((Transaction) newSelection).getEmployee().getName());
                fullTransaction.setItems(FXCollections.observableArrayList(((Transaction) newSelection).getItems()));
            }
        });
        VBox buttonsHolder = new VBox();
        VBox transactionDetails = new VBox();
        transactionDetails.getChildren().add(details);
        content.add(tbv,0,0);
        content.add(fullTransaction, 0, 1);
        content.add(transactionDetails, 1, 1);
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(actionEvent -> home(stage));
        back.setCancelButton(true);
        buttonsHolder.getChildren().addAll(back);
        content.add(buttonsHolder, 1, 0);
        content.getStyleClass().add("bg-warning");
        sceneInit(2, 2);
    }
    public void transaction(Stage stage)
    {
        sceneClear();
        stage.setTitle("JessPOS - New transaction");
        TableView tbv = new TableView();
        tbv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn SKUCol = new TableColumn("SKU");
        SKUCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("SKU"));
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("name"));
        TableColumn quantCol = new TableColumn("Quantity in stock");
        quantCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("quantity"));
        TableColumn priceCol = new TableColumn("Price (CAD $)");
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Item, Double>("price"));

        tbv.getColumns().addAll(SKUCol, nameCol, quantCol, priceCol);
        TextField addSKU = new TextField("Product SKU");
        addSKU.setMinHeight(50);
        ArrayList<Item> savedItems = new ArrayList<>();
        MenuBtn addButton = new MenuBtn("Add [Enter]", "btn-primary", "Add product with SKU to transaction");
        addButton.setOnAction(e -> {
            try {
                if(addSKU.getText().matches("^[0-9]+$")) {
                    FH.getInventoryFile().decrementItem(Integer.parseInt(addSKU.getText()));
                    savedItems.add(FH.getInventoryFile().getItem(addSKU.getText()));
                }
                else {
                    UIAlert SKUInvalid = new UIAlert("Invalid SKU", "Sorry, that's not a valid SKU.\nSKUs should consist of only numerical digits.", ButtonType.OK, ButtonType.CLOSE);
                    SKUInvalid.showAndWait();
                }
            } catch (InventoryLog.ItemNotFoundException ex) {
                UIAlert itemNotFound = new UIAlert("Item not found", "Sorry, no item with that SKU could be found.", ButtonType.OK, ButtonType.CLOSE);
                itemNotFound.showAndWait();
                return;
            }
            addSKU.clear();
            tbv.setItems(FXCollections.observableArrayList(savedItems));
            addSKU.requestFocus();
        });
        addButton.setDefaultButton(true);
        MenuBtn removeButton = new MenuBtn("Remove", "btn-danger", "Remove highlighted row");
        removeButton.setOnAction(actionEvent -> {
            savedItems.remove((Item) tbv.getSelectionModel().getSelectedItem());
            tbv.setItems(FXCollections.observableArrayList(savedItems));
        });
        MenuBtn endButton = new MenuBtn("End transaction", "btn-warning", "End transaction");
        endButton.setOnAction(actionEvent -> {
            UIAlert endTransAlert = new UIAlert("Finish transaction", "Print current transaction and end?", ButtonType.FINISH, ButtonType.CANCEL);
            endTransAlert.showAndWait();
            if (endTransAlert.getResult() == ButtonType.FINISH) {
                FH.getTransactionsFile().addTransaction(FH.getEmployeesFile().getEmployee(employeeID), savedItems);
                home(stage);
            } else {
                endTransAlert.close();
            }
        });
        VBox entryForm = new VBox();
        VBox buttonsHolder = new VBox();
        entryForm.getChildren().addAll(addSKU,addButton);
        content.add(tbv,0,0, 1, 2);
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(actionEvent -> {
            if(savedItems.size() > 0) {
                UIAlert pendingTransAlert = new UIAlert("Unfinished transaction", "Quit current transaction? (All data will be lost)", ButtonType.OK, ButtonType.CANCEL);
                pendingTransAlert.showAndWait();
                if (pendingTransAlert.getResult() == ButtonType.OK) {
                    for(Item i : savedItems) {
                        FH.getInventoryFile().incrementItem(i.getSKU());
                    }
                    home(stage);
                } else {
                    pendingTransAlert.close();
                }
            }
            else {
                home(stage);
            }
        });
        back.setCancelButton(true);
        buttonsHolder.getChildren().addAll(back, removeButton, endButton);
        content.add(buttonsHolder, 1, 0);
        content.add(entryForm,1,1);
        content.getStyleClass().add("bg-primary");
        sceneInit(2, 2);
    }
    public void employee(Stage stage) {
        sceneClear();
        stage.setTitle("JessPOS - Employee Timetable");
        TableView tbv = new TableView();
        tbv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn IDCol = new TableColumn("ID");
        IDCol.setCellValueFactory(
                new PropertyValueFactory<Employee, Integer>("ID"));
        TableColumn NameCol = new TableColumn("Name");
        NameCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("name"));

        TextField addName = new TextField();
        addName.setPromptText("Employee name");

        tbv.getColumns().addAll(IDCol, NameCol);
        tbv.setItems(FH.getEmployeesFile().getEmployees());

        TableView schedule = new TableView();
        schedule.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn pinCol = new TableColumn("Punch in");
        pinCol.setCellValueFactory(
                new PropertyValueFactory<Time, Instant>("pin"));
        TableColumn poutCol = new TableColumn("Punch out");
        poutCol.setCellValueFactory(
                new PropertyValueFactory<Time, Instant>("pout"));
        TableColumn deltaCol = new TableColumn("Shift length (minutes)");
        deltaCol.setCellValueFactory(
                new PropertyValueFactory<Time, Long>("delta"));

        schedule.getColumns().addAll(pinCol, poutCol, deltaCol);
        tbv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int ID = ((Employee) newSelection).getID();
                schedule.setItems(FXCollections.observableArrayList((FH.getTimesFile().getTimes(ID))));
            }
        });
        MenuBtn newEmployee = new MenuBtn("Add new employee (admin)", "btn-primary", "Create new employee (admin privileges required)");
        newEmployee.setDefaultButton(true);
        newEmployee.setOnAction(actionEvent -> {
            TextInputDialog verifyAdmin = new TextInputDialog("Enter admin password");
            verifyAdmin.setHeaderText("Password is required to complete that action");
            verifyAdmin.setTitle("Admin privileges required");
            verifyAdmin.showAndWait();
            if(verifyAdmin.getEditor().getText().equals("p4$$w0rd")) {
                UIAlert success = new UIAlert("Success", "Admin privileges enabled and employee added", ButtonType.OK, ButtonType.CANCEL);
                success.showAndWait();
                if (success.getResult() == ButtonType.OK) {
                    FH.getEmployeesFile().addEmployee(addName.getText());
                    tbv.setItems(FH.getEmployeesFile().getEmployees());
                } else {
                    success.close();
                }
            } else {
                UIAlert failure = new UIAlert("Failure", "Password incorrect", ButtonType.OK, ButtonType.CLOSE);
                failure.showAndWait();
                if (failure.getResult() == ButtonType.OK) {
                    home(stage);
                } else {
                    failure.close();
                }
            }
        });
        VBox buttonsHolder = new VBox();
        VBox entryForm = new VBox();
        content.add(tbv,0,0);
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(actionEvent -> home(stage));
        back.setCancelButton(true);
        MenuBtn punchIn = new MenuBtn("Punch in", "btn-primary", "Punch in with employee ID");
        punchIn.setOnAction(e -> signIn(stage));
        MenuBtn punchOut = new MenuBtn("Punch out", "btn-secondary", "Punch out from current session");
        punchOut.setOnAction(e -> {
            UIAlert success = new UIAlert("Success", "Punched out successfully", ButtonType.OK, ButtonType.CANCEL);
            success.showAndWait();
            FH.getTimesFile().punchOut(employeeID);
            employeeID = 0;
            signedIn = false;
            buttonsHolder.getChildren().clear();
            home(stage);
        });
        if(signedIn) {
            buttonsHolder.getChildren().addAll(back, punchOut);
        } else {
            buttonsHolder.getChildren().addAll(back, punchIn);
        }
        entryForm.getChildren().addAll(addName,newEmployee);
        content.add(entryForm, 1, 1);
        content.add(schedule, 0, 1);
        content.add(buttonsHolder, 1, 0);
        content.getStyleClass().add("bg-danger");
        sceneInit(2, 2);
    }
    private static class UIAlert extends Alert {
        public UIAlert(String title, String content, ButtonType bt1, ButtonType bt2) {
            super(AlertType.NONE, content, bt1, bt2);
            this.setTitle(title);
        }
    }
    private static class MenuBtn extends Button {
        public MenuBtn(String text, String btnType, String tooltipText) {
            super(text);
            this.getStyleClass().addAll("btn", btnType);
            this.setMinHeight(100);
            this.setMaxSize(Double.MAX_VALUE, 150);
            this.setTooltip(new Tooltip(tooltipText));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}