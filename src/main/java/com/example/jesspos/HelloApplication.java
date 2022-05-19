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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
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
    public boolean signedIn = (employeeID > 0) ? true : false;
    @Override public void start(Stage stage) throws Exception {
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
            account.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    employee(stage);
                }
            });
        } else {
            account = new MenuBtn("Currently not signed in.", "", "Sign in");
            account.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    TextInputDialog getEmployeeID = new TextInputDialog("Enter employee ID");
                    getEmployeeID.setHeaderText("Employee ID is required to punch in");
                    getEmployeeID.setTitle("Punch in");
                    getEmployeeID.showAndWait();
                    if(getEmployeeID.getEditor().getText().isBlank() || getEmployeeID.getEditor().getText() == null) {
                        UIAlert failure = new UIAlert("Failure", "ID is blank", ButtonType.OK, ButtonType.CLOSE);
                        failure.showAndWait();
                        if (failure.getResult() == ButtonType.OK) {
                            home(stage);
                        } else {
                            failure.close();
                        }
                    } else {
                        int ID = Integer.parseInt(getEmployeeID.getEditor().getText());
                        employeeID = ID;
                        signedIn = true;
                        UIAlert success = new UIAlert("Success", "Punched in successfully", ButtonType.OK, ButtonType.CANCEL);
                        success.showAndWait();
                        FH.getTimesFile().punchIn(ID);
                        home(stage);
                    }
                }
            });
        }
        MenuBtn test1 = new MenuBtn("Inventory", "btn-success", "Access inventory management");
        test1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                inventory(stage);
            }
        });
        test1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        MenuBtn test2 = new MenuBtn("Transaction", "btn-primary", "Open a new transaction");
        test2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                transaction(stage);
            }
        });
        MenuBtn test3 = new MenuBtn("View transactions", "btn-warning", "View previously finished transactions");
        test3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                transactions(stage);
            }
        });
        MenuBtn test4 = new MenuBtn("Manage my timetable", "btn-danger", "Manage your timetable and punch in/out");
        test4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                employee(stage);
            }
        });
        test2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        test3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        test4.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        content.setAlignment(Pos.CENTER);
        account.setAlignment(Pos.CENTER);
        account.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        content.add(account, 0, 0, 2, 1);
        content.add(test1, 0, 1);
        content.add(test2, 1, 1);
        content.add(test3, 0, 2);
        content.add(test4, 1, 2);
        for (int i = 0 ; i < 2 ; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/2.0);
            cc.setHgrow(Priority.ALWAYS);
            content.getColumnConstraints().add(cc);
        }
        for (int i = 0 ; i < 3 ; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/3.0);
            rc.setVgrow(Priority.ALWAYS);
            content.getRowConstraints().add(rc);
        }
    }
    public void sceneClear() {
        content.getChildren().clear();
        content.getColumnConstraints().clear();
        content.getRowConstraints().clear();
    }
    public void inventory(Stage stage)
    {
        sceneClear();
        stage.setTitle("JessPOS - Inventory Management");
        TableView tbv = new TableView();
        tbv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn SKUCol = new TableColumn("SKU");
        SKUCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("SKU"));
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("name"));
        TableColumn quantCol = new TableColumn("Quantity");
        quantCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("quantity"));
        TableColumn priceCol = new TableColumn("Price (CAD $)");
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Item, Double>("price"));

        tbv.getColumns().addAll(SKUCol, nameCol, quantCol, priceCol);
        tbv.setItems(FH.getInventoryFile().getItems());
        tbv.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        TextField addName = new TextField();
        addName.setPromptText("Product name");
        TextField addQuantity = new TextField();
        addQuantity.setPromptText("Quantity");
        TextField addPrice = new TextField();
        addPrice.setPromptText("Price (CAD $)");

        MenuBtn addButton = new MenuBtn("Add [Enter]", "btn-primary", "Add new product with above details");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FH.getInventoryFile().addItem(addName.getText(), addQuantity.getText(), addPrice.getText());
                addName.clear();
                addQuantity.clear();
                addPrice.clear();
                tbv.setItems(FH.getInventoryFile().getItems());
                addName.requestFocus();
            }
        });
        addButton.setDefaultButton(true);
        MenuBtn removeButton = new MenuBtn("Remove", "btn-danger", "Remove highlighted row");
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Item item = (Item) tbv.getSelectionModel().getSelectedItem();
                FH.getInventoryFile().removeItem(item.getSKU());
                tbv.setItems(FH.getInventoryFile().getItems());
            }
        });
        VBox entryForm = new VBox();
        VBox buttonsHolder = new VBox();
        entryForm.getChildren().addAll(addName,addQuantity,addPrice,addButton);
        content.add(tbv,0,0, 2, 3);
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                home(stage);
            }
        });
        back.setCancelButton(true);
        removeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        back.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonsHolder.getChildren().addAll(back, removeButton);
        content.add(buttonsHolder, 2, 0, 2, 1);
        content.add(entryForm,2,1, 1, 1);
    }
    public void transactions(Stage stage)
    {
        sceneClear();
        stage.setTitle("JessPOS - Transaction Management");
        TableView tbv = new TableView();
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
        tbv.setItems(FH.getTransactionsFile().getTransactions());

        TableView fullTransaction = new TableView();
        fullTransaction.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn SKUCol = new TableColumn("SKU");
        SKUCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("SKU"));
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Item, String>("name"));
        TableColumn quantCol = new TableColumn("Quantity");
        quantCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("quantity"));
        TableColumn priceCol = new TableColumn("Price (CAD $)");
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Item, Double>("price"));

        fullTransaction.getColumns().addAll(SKUCol, nameCol, quantCol, priceCol);
        fullTransaction.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Text details = new Text();
        tbv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                details.setText("Transaction completed by: " + ((Transaction) newSelection).getEmployee().getName());
                fullTransaction.setItems(FXCollections.observableArrayList(((Transaction) newSelection).getItems()));
            }
        });
        tbv.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox buttonsHolder = new VBox();
        VBox transactionDetails = new VBox();
        transactionDetails.getChildren().add(details);
        content.add(tbv,0,0);
        content.add(fullTransaction, 0, 1);
        content.add(transactionDetails, 1, 1);
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                home(stage);
            }
        });
        back.setCancelButton(true);
        back.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonsHolder.getChildren().addAll(back);
        content.add(buttonsHolder, 1, 0);
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
        TableColumn quantCol = new TableColumn("Quantity");
        quantCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("quantity"));
        TableColumn priceCol = new TableColumn("Price (CAD $)");
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Item, Double>("price"));

        tbv.getColumns().addAll(SKUCol, nameCol, quantCol, priceCol);
        tbv.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        TextField addSKU = new TextField();
        addSKU.setPromptText("Product SKU");
        ArrayList<Item> savedItems = new ArrayList<>();
        MenuBtn addButton = new MenuBtn("Add [Enter]", "btn-primary", "Add product with SKU to transaction");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                savedItems.add(FH.getInventoryFile().getItem(addSKU.getText()));
                addSKU.clear();
                tbv.setItems(FXCollections.observableArrayList(savedItems));
                addSKU.requestFocus();
            }
        });
        addButton.setDefaultButton(true);
        MenuBtn removeButton = new MenuBtn("Remove", "btn-danger", "Remove highlighted row");
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                savedItems.remove((Item) tbv.getSelectionModel().getSelectedItem());
                tbv.setItems(FXCollections.observableArrayList(savedItems));
            }
        });
        MenuBtn endButton = new MenuBtn("End transaction", "btn-warning", "End transaction");
        endButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                UIAlert endTransAlert = new UIAlert("Finish transaction", "Print current transaction and end?", ButtonType.FINISH, ButtonType.CANCEL);
                endTransAlert.showAndWait();
                if (endTransAlert.getResult() == ButtonType.FINISH) {
                    FH.getTransactionsFile().addTransaction(FH.getEmployeesFile().getEmployee(employeeID), savedItems);
                    home(stage);
                } else {
                    endTransAlert.close();
                }
            }
        });
        VBox entryForm = new VBox();
        VBox buttonsHolder = new VBox();
        entryForm.getChildren().addAll(addSKU,addButton);
        content.add(tbv,0,0);
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(savedItems.size() > 0) {
                    UIAlert pendingTransAlert = new UIAlert("Unfinished transaction", "Quit current transaction? (All data will be lost)", ButtonType.OK, ButtonType.CANCEL);
                    pendingTransAlert.showAndWait();
                    if (pendingTransAlert.getResult() == ButtonType.OK) {
                        home(stage);
                    } else {
                        pendingTransAlert.close();
                    }
                }
                else {
                    home(stage);
                }
            }
        });
        back.setCancelButton(true);
        removeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        back.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonsHolder.getChildren().addAll(back, removeButton, endButton);
        content.add(buttonsHolder, 1, 0);
        content.add(entryForm,0,1);
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
        schedule.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        tbv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int ID = ((Employee) newSelection).getID();
                schedule.setItems(FXCollections.observableArrayList((FH.getTimesFile().getTimes(ID))));
            }
        });
        MenuBtn newEmployee = new MenuBtn("Add new employee (admin)", "btn-primary", "Create new employee (admin privileges required)");
        newEmployee.setDefaultButton(true);
        newEmployee.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextInputDialog verifyAdmin = new TextInputDialog("Enter admin password");
                verifyAdmin.setHeaderText("Password is required to complete that action");
                verifyAdmin.setTitle("Admin privileges required");
                verifyAdmin.showAndWait();
                if(verifyAdmin.getEditor().getText().equals("p4$$w0rd")) {
                    UIAlert success = new UIAlert("Success", "Admin priviliges enabled and employee added", ButtonType.OK, ButtonType.CANCEL);
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
            }
        });
        tbv.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox buttonsHolder = new VBox();
        VBox entryForm = new VBox();
        content.add(tbv,0,0);
        //content.add(schedule, 0, 1);
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                home(stage);
            }
        });
        back.setCancelButton(true);
        back.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        MenuBtn punchIn = new MenuBtn("Punch in", "btn-primary", "Punch in with employee ID");
        punchIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                TextInputDialog getEmployeeID = new TextInputDialog("Enter employee ID");
                getEmployeeID.setHeaderText("Employee ID is required to punch in");
                getEmployeeID.setTitle("Punch in");
                getEmployeeID.showAndWait();
                if(getEmployeeID.getEditor().getText().isBlank() || getEmployeeID.getEditor().getText() == null) {
                    UIAlert failure = new UIAlert("Failure", "ID is blank", ButtonType.OK, ButtonType.CLOSE);
                    failure.showAndWait();
                    if (failure.getResult() == ButtonType.OK) {
                        home(stage);
                    } else {
                        failure.close();
                    }
                } else {
                    int ID = Integer.parseInt(getEmployeeID.getEditor().getText());
                    employeeID = ID;
                    signedIn = true;
                    UIAlert success = new UIAlert("Success", "Punched in successfully", ButtonType.OK, ButtonType.CANCEL);
                    success.showAndWait();
                    FH.getTimesFile().punchIn(ID);
                    buttonsHolder.getChildren().clear();
                    home(stage);
                }
            }
        });
        MenuBtn punchOut = new MenuBtn("Punch out", "btn-secondary", "Punch out from current session");
        punchOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                UIAlert success = new UIAlert("Success", "Punched out successfully", ButtonType.OK, ButtonType.CANCEL);
                success.showAndWait();
                FH.getTimesFile().punchOut(employeeID);
                employeeID = 0;
                signedIn = false;
                buttonsHolder.getChildren().clear();
                home(stage);
            }
        });
        if(signedIn==true) {
            buttonsHolder.getChildren().addAll(back, punchOut);
        } else {
            buttonsHolder.getChildren().addAll(back, punchIn);
        }
        entryForm.getChildren().addAll(addName,newEmployee);
        content.add(entryForm, 0, 1);
        content.add(schedule, 1, 0);
        content.add(buttonsHolder, 2, 0);
    }
    private class UIAlert extends Alert {
        public UIAlert(String title, String content, ButtonType bt1, ButtonType bt2) {
            super(AlertType.NONE, content, bt1, bt2);
            this.setTitle(title);
        }
    }
    private class MenuBtn extends Button {
        public MenuBtn(String text, String btnType, String tooltipText) {
            super(text);
            this.getStyleClass().addAll("btn", btnType);
            this.setMinHeight(150);
            this.setMinWidth(150);
            this.setTooltip(new Tooltip(tooltipText));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}