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
import java.util.ArrayList;

public class HelloApplication extends Application {
    GridPane content = new GridPane();
    public Scene scene = new Scene(content);
    protected InventoryLog inventory = new InventoryLog(new File("inventory.csv"));
    @Override public void start(Stage stage) throws Exception {
        home(stage);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("JessPOS - Home");
        stage.setMinWidth(800);
        stage.setMinHeight(800);
        stage.show();
    }
    public void home(Stage stage) {
        menuBtn test1 = new menuBtn("Inventory", "btn-success", "Access inventory management");
        test1.setOnAction(new InventoryView(stage));
        test1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        menuBtn test2 = new menuBtn("Transaction", "btn-primary", "Open a new transaction");
        test2.setOnAction(new TransactionView(stage));
        menuBtn test3 = new menuBtn("Click me!", "btn-warning", "");
        menuBtn test4 = new menuBtn("Click me!", "btn-danger", "");
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
    class HomeView implements EventHandler<ActionEvent>
    {
        protected Stage stage;

        public HomeView(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(ActionEvent event)
        {
            sceneClear();
            stage.setTitle("JessPOS - Home");
            home(stage);
        }
    }
    class TransactionView implements EventHandler<ActionEvent>
    {
        protected Stage stage;
        public TransactionView(Stage stage) {
            this.stage = stage;
        }
        @Override
        public void handle(ActionEvent event)
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
            menuBtn addButton = new menuBtn("Add [Enter]", "btn-primary", "Add product with SKU to transaction");
            addButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    savedItems.add(inventory.getItem(addSKU.getText()));
                    addSKU.clear();
                    tbv.setItems(FXCollections.observableArrayList(savedItems));
                    addSKU.requestFocus();
                }
            });
            addButton.setDefaultButton(true);
            menuBtn removeButton = new menuBtn("Remove", "btn-danger", "Remove highlighted row");
            removeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    savedItems.remove((Item) tbv.getSelectionModel().getSelectedItem());
                    tbv.setItems(FXCollections.observableArrayList(savedItems));
                }
            });
            VBox entryForm = new VBox();
            VBox buttonsHolder = new VBox();
            entryForm.getChildren().addAll(addSKU,addButton);
            content.add(tbv,0,0);
            menuBtn back = new menuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
            back.setOnAction(new HomeView(stage));
            back.setCancelButton(true);
            removeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            back.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            buttonsHolder.getChildren().addAll(back, removeButton);
            content.add(buttonsHolder, 1, 0);
            content.add(entryForm,0,1);
        }
    }
    class InventoryView implements EventHandler<ActionEvent>
    {
        protected Stage stage;
        public InventoryView(Stage stage) {
            this.stage = stage;
        }
        @Override
        public void handle(ActionEvent event)
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
            tbv.setItems(inventory.getItems());
            tbv.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            TextField addName = new TextField();
            addName.setPromptText("Product name");
            TextField addQuantity = new TextField();
            addQuantity.setPromptText("Quantity");
            TextField addPrice = new TextField();
            addPrice.setPromptText("Price (CAD $)");

            menuBtn addButton = new menuBtn("Add [Enter]", "btn-primary", "Add new product with above details");
            addButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    inventory.addItems(addName.getText(),addQuantity.getText(),addPrice.getText());
                    addName.clear();
                    addQuantity.clear();
                    addPrice.clear();
                    tbv.setItems(inventory.getItems());
                    addName.requestFocus();
                }
            });
            addButton.setDefaultButton(true);
            menuBtn removeButton = new menuBtn("Remove", "btn-danger", "Remove highlighted row");
            removeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Item item = (Item) tbv.getSelectionModel().getSelectedItem();
                    inventory.removeItem(item.getSKU());
                    tbv.setItems(inventory.getItems());
                }
            });
            VBox entryForm = new VBox();
            VBox buttonsHolder = new VBox();
            entryForm.getChildren().addAll(addName,addQuantity,addPrice,addButton);
            content.add(tbv,0,0);
            menuBtn back = new menuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
            back.setOnAction(new HomeView(stage));
            back.setCancelButton(true);
            removeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            back.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            buttonsHolder.getChildren().addAll(back, removeButton);
            content.add(buttonsHolder, 1, 0);
            content.add(entryForm,0,1);
        }
    }
    private class menuBtn extends Button {
        public menuBtn(String text, String btnType, String tooltipText) {
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