package com.example.jesspos;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;

public class HelloApplication extends Application {
    GridPane content = new GridPane();
    public Scene scene = new Scene(content);
    @Override public void start(Stage stage) throws Exception {
        home();
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
        stage.setTitle("JessPOS");
        stage.show();
    }
    public void home() {
        menuBtn test = new menuBtn("Click me!", "btn-success");
        test.setOnAction(new InventoryView());
        menuBtn test2 = new menuBtn("Click me!", "btn-primary");
        menuBtn test3 = new menuBtn("Click me!", "btn-warning");
        menuBtn test4 = new menuBtn("Click me!", "btn-danger");
        content.setAlignment(Pos.CENTER);
        content.add(test, 0, 0);
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
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        content.getRowConstraints().add(rc);
    }
    public void sceneClear() {
        content.getChildren().clear();
        content.getColumnConstraints().clear();
        content.getRowConstraints().clear();
    }
    class HomeView implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            sceneClear();
            home();
        }
    }
    class InventoryView implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            ListView inventory = new ListView();
            sceneClear();
            content.add(inventory,0,0);
            menuBtn back = new menuBtn("Return", "btn-warning");
            back.setOnAction(new HomeView());
            content.add(back,1,0);
            inventory.getItems().addAll("hello","this","is","a","test");
        }
    }
    private class menuBtn extends Button {
        public menuBtn(String text, String btnType) {
            super(text);
            this.getStyleClass().addAll("btn", btnType);
            this.setMinHeight(150);
            this.setMinWidth(150);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}