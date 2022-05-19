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
        test2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        test3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        test4.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        content.setAlignment(Pos.CENTER);
        content.add(test1, 0, 0);
        content.add(test2, 1, 0);
        content.add(test3, 0, 1);
        content.add(test4, 1, 1);
        for (int i = 0 ; i < 2 ; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/2.0);
            cc.setHgrow(Priority.ALWAYS);
            content.getColumnConstraints().add(cc);
        }
        for (int i = 0 ; i < 2 ; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0/2.0);
            rc.setVgrow(Priority.ALWAYS);
            content.getRowConstraints().add(rc);
        }
        content.setHgap(100);
        content.setVgap(100);
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
        content.add(tbv,0,0);
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
        content.add(buttonsHolder, 1, 0);
        content.add(entryForm,0,1);
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
        tbv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fullTransaction.setItems(FXCollections.observableArrayList(((Transaction) newSelection).getItems()));
            }
        });
        tbv.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox buttonsHolder = new VBox();
        content.add(tbv,0,0);
        content.add(fullTransaction, 0, 1);
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
                    FH.getTransactionsFile().addTransaction(new Employee("John"), savedItems);
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
        tbv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fullTransaction.setItems(FXCollections.observableArrayList(((Transaction) newSelection).getItems()));
            }
        });
        tbv.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox buttonsHolder = new VBox();
        content.add(tbv,0,0);
        content.add(fullTransaction, 0, 1);
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