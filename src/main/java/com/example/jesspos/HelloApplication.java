package com.example.jesspos;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.ArrayList;

public class HelloApplication extends Application {
    // initialize global variables
    public GridPane content = new GridPane();
    public Scene scene = new Scene(content);
    public final FileHandler FH = new FileHandler();
    public int employeeID = FH.getTimesFile().getSignedInID();
    @Override public void start(Stage stage) {
        // open homepage
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
        if(employeeID>0) {
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    UIAlert stillSignedIn = new UIAlert("Exiting without signing out", "Are you sure you want to exit without signing out?\nThis will keep your session active until the next time you open the app and punch out.", ButtonType.OK, ButtonType.CANCEL);
                    if(stillSignedIn.getResult() != ButtonType.OK) {
                        event.consume();
                    }
                }
            });
        }
        stage.setTitle("JessPOS - Home");
        // Create top button that displays if the user is signed in on the homepage
        // and since what date and time
        MenuBtn account;
        if(employeeID>0) {
            account = new MenuBtn(("Currently signed in as: " + FH.getEmployeesFile().getEmployee(employeeID).getName() + ". (ID: " + employeeID + ")" +
                    "\nSince: " + FH.getTimesFile().getLastTime(employeeID)), "btn-secondary", "Access your timetable");
            // If signed in, make the button go to the employee management view
            account.setOnAction(actionEvent -> employee(stage));
        } else {
            // if not signed in, prompt the user to sign in
            account = new MenuBtn("Currently not signed in.", "", "Sign in");
            account.setOnAction(actionEvent -> signIn(stage));
        }
        MenuBtn inventoryBtn = new MenuBtn("Open Inventory", "btn-success", "Access inventory management");
        inventoryBtn.setOnAction(actionEvent -> inventory(stage));
        MenuBtn transactionBtn = new MenuBtn("New Transaction", "btn-primary", "Open a new transaction");
        transactionBtn.setOnAction(actionEvent -> {
            if(employeeID>0) {
                transaction(stage);
            } else {
                // prompt sign-in to open transaction view if not already
                if(signIn(stage)) {
                    transaction(stage);
                }
            }
        });
        MenuBtn viewTransactionsBtn = new MenuBtn("View transactions", "btn-warning", "View previously finished transactions");
        viewTransactionsBtn.setOnAction(actionEvent -> transactions(stage));
        MenuBtn employeesBtn = new MenuBtn("Manage employees and timetables", "btn-danger", "Manage your timetable and punch in/out");
        employeesBtn.setOnAction(actionEvent -> employee(stage));
        account.setAlignment(Pos.CENTER);
        content.add(account, 0, 0, 2, 1);
        content.addRow(1, inventoryBtn, transactionBtn);
        content.addRow(2, viewTransactionsBtn, employeesBtn);
        sceneInit(2, 3);
    }
    // Give prompt to user to sign in
    public boolean signIn(Stage stage) {
        TextInputDialog getEmployeeID = new TextInputDialog("", "Employee ID is required to punch in", "Punch in");
        String input = getEmployeeID.getEditor().getText();
        // check if input contains a non-number
        if(!input.matches("^[0-9]+$") || input.isBlank()) {
            UIAlert failure = new UIAlert("Input error", "ID is blank or invalid, please try again.\nRemember: ID must be an integer.", ButtonType.OK, ButtonType.CLOSE);
            if (failure.getResult() == ButtonType.OK) {
                home(stage);
            } else {
                failure.close();
            }
        } else {
            int ID = Integer.parseInt(getEmployeeID.getEditor().getText());
            // check if valid ID matches an Employee
            if(FH.getEmployeesFile().employeeExists(ID)) {
                employeeID = ID;
                UIAlert success = new UIAlert("Success", "Punched in successfully", ButtonType.OK, ButtonType.CANCEL);
                FH.getTimesFile().punchIn(ID);
                home(stage);
                return true;
            } else {
                UIAlert failure = new UIAlert("Employee not found", "ID does not match a user, please try again.", ButtonType.OK, ButtonType.CLOSE);
                if (failure.getResult() == ButtonType.OK) {
                    home(stage);
                } else {
                    failure.close();
                }
            }
        }
        return false;
    }
    public void sceneClear() {
        content.getChildren().clear();
        content.getColumnConstraints().clear();
        content.getRowConstraints().clear();
    }
    public void sceneInit(int x, int y) {
        // initialize scene with `x` columns and `y` rows
        content.setAlignment(Pos.CENTER);
        for (int i = 0 ; i < x ; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/x);
            cc.setHgrow(Priority.ALWAYS);
            cc.setHalignment(HPos.CENTER);
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
        TableView tbv = new TableView(FH.getInventoryFile().getFilteredItems());
        tbv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn SKUCol = new TableColumn("SKU");
        SKUCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("ID"));
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
        ArrayList<TextField> fields = new ArrayList<>();
        TextField addName = new TextField("Product name", 50);
        TextField addQuantity = new TextField("Quantity in stock", 50);
        TextField addPrice = new TextField("Price (CAD $)", 50);
        fields.add(addName);
        fields.add(addQuantity);
        fields.add(addPrice);

        MenuBtn addButton = new MenuBtn("Add [Enter]", "btn-primary", "Add new product with above details");
        addButton.setOnAction(e -> {
            // Check input strings for correct patterns:
            // Name: only alphabetical and space characters
            // Quantity: only non-zero, positive integers
            // Price: only positive doubles (can be zero)
            String[] checks = {"^[A-Za-z\s]+$","^[0-9]+$","^[0-9]+.[0-9]{2}$"};
            if(addName.getText().matches("^[A-Za-z\s]+$")
                    && addQuantity.getText().matches("^[0-9]+$")
                    && addPrice.getText().matches("^[0-9]+.[0-9]{2}$")) {

                if(Integer.parseInt(addQuantity.getText()) > 0 && Double.parseDouble(addPrice.getText()) >= 0) {
                    FH.getInventoryFile().addItem(addName.getText(), addQuantity.getText(), addPrice.getText());
                    for(TextField f : fields)
                        f.clear();
                    tbv.setItems(FH.getInventoryFile().getFilteredItems());
                    addName.requestFocus();
                } else {
                    UIAlert invalidEntries = new UIAlert("Invalid product properties", "Some or all of your inputs are blank or invalid.\nDon't forget that product names should contain only letters or spaces, quantity only integers, and price only doubles (number with 2 decimals).", ButtonType.OK, ButtonType.CLOSE);
                    for(TextField f : fields)
                        f.clear();
                    addName.requestFocus();
                }
            } else {
                UIAlert invalidEntries = new UIAlert("Invalid product properties", "Some or all of your inputs are blank or invalid.\nDon't forget that product names should contain only letters or spaces, quantity only integers, and price only doubles (number with 2 decimals).", ButtonType.OK, ButtonType.CLOSE);
                for(TextField f : fields)
                    f.clear();
                addName.requestFocus();
            }
        });
        addButton.setDefaultButton(true);
        MenuBtn removeButton = new MenuBtn("Remove", "btn-danger", "Remove highlighted item");
        removeButton.setOnAction(actionEvent -> {
            if(tbv.getSelectionModel().getSelectedItem() != null) {
                Item item = (Item) tbv.getSelectionModel().getSelectedItem();
                FH.getInventoryFile().removeItem(item.getID());
                tbv.setItems(FH.getInventoryFile().getFilteredItems());
            } else {
                UIAlert fail = new UIAlert("Cannot remove item", "Please select an item to remove", ButtonType.OK, ButtonType.CLOSE);
            }
        });
        MenuBtn viewAllItems = new MenuBtn("View all items", "btn-secondary", "View all including removed items (listed as having quantity of 0)");
        viewAllItems.setOnAction(actionEvent -> {
            tbv.setItems(FH.getInventoryFile().getItems());
        });
        MenuBtn viewButton = new MenuBtn("View non-removed items", "btn-secondary", "View non-removed items (listed as having quantity greater than 0)");
        viewButton.setOnAction(actionEvent -> {
            tbv.setItems(FH.getInventoryFile().getFilteredItems());
        });
        MenuBtn editButton = new MenuBtn("Edit", "btn-success", "Edit highlighted item");
        editButton.setOnAction(actionEvent -> {
            if(tbv.getSelectionModel().getSelectedItem() != null) {
                Item newSelection = (Item) tbv.getSelectionModel().getSelectedItem();
                if(addName.getText().matches("^[A-Za-z\s]+$")
                        && addQuantity.getText().matches("^[0-9]+$")
                        && addPrice.getText().matches("^[0-9]+.[0-9]{2}$")) {

                    if(Integer.parseInt(addQuantity.getText()) > 0 && Double.parseDouble(addPrice.getText()) >= 0) {
                        FH.getInventoryFile().setItem(newSelection.getID(), addName.getText(), addQuantity.getText(), addPrice.getText());
                        for(TextField f : fields)
                            f.clear();
                        tbv.setItems(FH.getInventoryFile().getFilteredItems());
                        addName.requestFocus();
                    } else {
                        UIAlert invalidEntries = new UIAlert("Invalid product properties", "Some or all of your inputs are blank or invalid.\nDon't forget that product names should contain only letters or spaces, quantity only integers, and price only doubles (number with 2 decimals).", ButtonType.OK, ButtonType.CLOSE);
                        for(TextField f : fields)
                            f.clear();
                        addName.requestFocus();
                    }
                } else {
                    UIAlert invalidEntries = new UIAlert("Invalid product properties", "Some or all of your inputs are blank or invalid.\nDon't forget that product names should contain only letters or spaces, quantity only integers, and price only doubles (number with 2 decimals).", ButtonType.OK, ButtonType.CLOSE);
                    for(TextField f : fields)
                        f.clear();
                    addName.requestFocus();
                }
            } else {
                UIAlert fail = new UIAlert("Cannot edit item", "Please select an item to edit", ButtonType.OK, ButtonType.CLOSE);
            }
        });
        VBox entryForm = new VBox();
        VBox buttonsHolder = new VBox();
        entryForm.getChildren().addAll(addName,addQuantity,addPrice,addButton, editButton);
        content.add(tbv,0,0, 2, 2);
        MenuBtn back = new BackBtn();
        back.setOnAction(actionEvent -> home(stage));
        buttonsHolder.getChildren().addAll(back, removeButton, viewAllItems, viewButton);
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
                new PropertyValueFactory<Transaction, String>("date"));
        TableColumn ItemsCountCol = new TableColumn("Number of items");
        ItemsCountCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, Integer>("itemsCount"));
        TableColumn PriceDeltaCol = new TableColumn("Total cost (CAD $)");
        PriceDeltaCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, Integer>("priceDelta"));

        tbv.getColumns().addAll(IDCol, DateCol, ItemsCountCol, PriceDeltaCol);

        TableView fullTransaction = new TableView();
        fullTransaction.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn SKUCol = new TableColumn("SKU");
        SKUCol.setCellValueFactory(
                new PropertyValueFactory<Item, Integer>("ID"));
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
        Label details = new Label();
        details.setPadding(new Insets(25));
        details.setAlignment(Pos.CENTER);
        tbv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Employee e = ((Transaction) newSelection).getEmployee();
                details.setText("Transaction completed by: " + e.getName() + " (ID: " + e.getID() + ")");
                fullTransaction.setItems(FXCollections.observableArrayList(((Transaction) newSelection).getItems()));
            }
        });
        VBox buttonsHolder = new VBox();
        VBox transactionSingleView = new VBox();
        transactionSingleView.setAlignment(Pos.CENTER);
        transactionSingleView.getChildren().addAll(details, fullTransaction);
        content.add(tbv,0,0);
        content.add(transactionSingleView, 0, 1);
        MenuBtn back = new BackBtn();
        back.setOnAction(actionEvent -> home(stage));
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
                new PropertyValueFactory<Item, Integer>("ID"));
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

        TextField addSKU = new TextField("Product SKU", 50);
        ArrayList<Item> savedItems = new ArrayList<>();
        MenuBtn addButton = new MenuBtn("Add [Enter]", "btn-primary", "Add product with SKU to transaction");
        addButton.setOnAction(e -> {
            try {
                // check SKU input pattern: must be integer
                if(addSKU.getText().matches("^[0-9]+$")) {
                    Item newItem = FH.getInventoryFile().getItem(addSKU.getText());
                    if(newItem.getQuantity()==0) {
                        UIAlert SKUInvalid = new UIAlert("Invalid SKU", "Sorry, no item with that SKU could be found. (Item has no more quantity to add)", ButtonType.OK, ButtonType.CLOSE);
                    } else {
                        // remove 1 from quantity of added SKU so that it displays the updated quantity in the tableview
                        FH.getInventoryFile().decrementItem(newItem.getID());
                        savedItems.add(FH.getInventoryFile().getItem(addSKU.getText()));
                    }
                }
                else {
                    UIAlert SKUInvalid = new UIAlert("Invalid SKU", "Sorry, that's not a valid SKU.\nSKUs should be an integer.", ButtonType.OK, ButtonType.CLOSE);
                }
                // if item not found, show error
                // (maybe find a way to provide view of inventory in transaction for easier adding?)
            } catch (InventoryLog.ItemNotFoundException ex) {
                UIAlert itemNotFound = new UIAlert("Item not found", "Sorry, no item with that SKU could be found.", ButtonType.OK, ButtonType.CLOSE);
            }
            addSKU.clear();
            tbv.setItems(FXCollections.observableArrayList(savedItems));
            addSKU.requestFocus();
        });
        addButton.setDefaultButton(true);
        MenuBtn removeButton = new MenuBtn("Remove", "btn-danger", "Remove highlighted row");
        removeButton.setOnAction(actionEvent -> {
            if(tbv.getSelectionModel().getSelectedItem() != null) {
                Item i = (Item) tbv.getSelectionModel().getSelectedItem();
                savedItems.remove(i);
                // add 1 to quantity to cancel out adding the item when removing
                FH.getInventoryFile().incrementItem(i.getID());
                tbv.setItems(FXCollections.observableArrayList(savedItems));
            } else {
                UIAlert fail = new UIAlert("Could not remove", "Please select an item to remove", ButtonType.OK, ButtonType.CLOSE);
            }
        });
        MenuBtn endButton = new MenuBtn("End transaction", "btn-warning", "End transaction");
        endButton.setOnAction(actionEvent -> {
            if(savedItems.size() > 0) {
                UIAlert endTransAlert = new UIAlert("Finish transaction", "Print current transaction and end?", ButtonType.FINISH, ButtonType.CANCEL);
                if (endTransAlert.getResult() == ButtonType.FINISH) {
                    FH.getTransactionsFile().addTransaction(FH.getEmployeesFile().getEmployee(employeeID), savedItems);
                    home(stage);
                } else {
                    endTransAlert.close();
                }
            } else {
                UIAlert emptyTrans = new UIAlert("Empty transaction", "Please add at least one item to the transaction.", ButtonType.OK, ButtonType.CANCEL);
            }
        });
        VBox entryForm = new VBox();
        VBox buttonsHolder = new VBox();
        entryForm.getChildren().addAll(addSKU,addButton);
        content.add(tbv,0,0, 1, 2);
        // custom back button, does not use BackBtn class
        MenuBtn back = new MenuBtn("Return [Esc]", "btn-warning", "Return to the homepage");
        back.setOnAction(actionEvent -> {
            if(savedItems.size() > 0) {
                UIAlert pendingTransAlert = new UIAlert("Unfinished transaction", "Quit current transaction? (All data will be lost)", ButtonType.OK, ButtonType.CANCEL);
                // on unfinished transaction, send user to home view if they press ok;
                // otherwise, close dialog and remain in transaction view
                if (pendingTransAlert.getResult() == ButtonType.OK) {
                    for(Item i : savedItems) {
                        FH.getInventoryFile().incrementItem(i.getID());
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
        TableColumn salCol = new TableColumn("Salary (CAD $/hour)");
        salCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("salary"));

        TextField addName = new TextField("Employee name", 50);
        TextField addSalary = new TextField("Employee salary (CAD $/hour)", 50);

        tbv.getColumns().addAll(IDCol, NameCol, salCol);
        tbv.setItems(FH.getEmployeesFile().getEmployees());

        TableView schedule = new TableView();
        schedule.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn pinCol = new TableColumn("Punch in");
        pinCol.setCellValueFactory(
                new PropertyValueFactory<Time, String>("conpin"));
        TableColumn poutCol = new TableColumn("Punch out");
        poutCol.setCellValueFactory(
                new PropertyValueFactory<Time, String>("conpout"));
        TableColumn deltaCol = new TableColumn("Shift length (minutes)");
        deltaCol.setCellValueFactory(
                new PropertyValueFactory<Time, Long>("delta"));
        TableColumn payCol = new TableColumn("Pay for shift (CAD $)");
        payCol.setCellValueFactory(
                new PropertyValueFactory<Time, Double>("pay"));

        schedule.getColumns().addAll(pinCol, poutCol, deltaCol, payCol);
        tbv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int ID = ((Employee) newSelection).getID();
                schedule.setItems(FXCollections.observableArrayList((FH.getTimesFile().getTimes(ID))));
            } else {
                schedule.setItems(FXCollections.observableArrayList(new ArrayList<Item>()));
            }
        });
        MenuBtn newEmployee = new MenuBtn("Add new employee [Enter] (admin)", "btn-primary", "Create new employee with above properties (admin privileges required)");
        newEmployee.setDefaultButton(true);
        newEmployee.setOnAction(actionEvent -> {
            // Verify name is not already taken
            ObservableList<Employee> employees = FH.getEmployeesFile().getEmployees();
            for(Employee e : employees) {
                if(e.getName().equalsIgnoreCase(addName.getText())) {
                    UIAlert failure = new UIAlert("Failure", "Name already taken; please choose another name.", ButtonType.OK, ButtonType.CLOSE);
                    return;
                }
            }
            TextInputDialog verifyAdmin = new AdminPassDialog();
            // Check password against hash stored in "shadow"
            if(FH.passIsCorrect(verifyAdmin.getEditor().getText())) {
                // Check that salary is number with 2 decimal places
                if(addSalary.getText().matches("^[0-9]+.[0-9]{2}$")) {
                    UIAlert success = new UIAlert("Success", "Admin privileges enabled and employee added", ButtonType.OK, ButtonType.CANCEL);
                    // Add employee to file
                    FH.getEmployeesFile().addEmployee(addName.getText(), Double.parseDouble(addSalary.getText()));
                    tbv.setItems(FH.getEmployeesFile().getEmployees());
                    addName.clear();
                    addSalary.clear();
                } else {
                    UIAlert failure = new UIAlert("Failure", "Salary invalid; please ensure salary is a double with two decimals", ButtonType.OK, ButtonType.CLOSE);
                }
            } else {
                UIAlert failure = new UIAlert("Failure", "Password incorrect", ButtonType.OK, ButtonType.CLOSE);
                if (failure.getResult() == ButtonType.OK) {
                    home(stage);
                } else {
                    failure.close();
                }
            }
        });
        MenuBtn editEmployee = new MenuBtn("Edit selected employee (admin)", "btn-success", "Edit selected employee with above properties (admin privileges required)");
        editEmployee.setOnAction(actionEvent -> {
            if(tbv.getSelectionModel().getSelectedItem() != null) {
                TextInputDialog verifyAdmin = new AdminPassDialog();
                // Check password against hash stored in "shadow"
                if(FH.passIsCorrect(verifyAdmin.getEditor().getText())) {
                    // Check that salary is number with 2 decimal places
                    if(addSalary.getText().matches("^[0-9]+.[0-9]{2}$")) {
                        UIAlert success = new UIAlert("Success", "Admin privileges enabled and employee " + addName.getText() + " edited successfully", ButtonType.OK, ButtonType.CANCEL);
                        // Set new properties to file
                        FH.getEmployeesFile().setEmployee(addName.getText(), Double.parseDouble(addSalary.getText()));
                        tbv.setItems(FH.getEmployeesFile().getEmployees());
                        schedule.setItems(FXCollections.observableArrayList((FH.getTimesFile().getTimes(FH.getEmployeesFile().getEmployee(addName.getText()).getID()))));
                        addName.clear();
                        addSalary.clear();
                    } else {
                        UIAlert failure = new UIAlert("Failure", "Salary invalid; please ensure salary is a double with two decimals", ButtonType.OK, ButtonType.CLOSE);
                    }
                } else {
                    UIAlert failure = new UIAlert("Failure", "Password incorrect", ButtonType.OK, ButtonType.CLOSE);
                    if (failure.getResult() == ButtonType.OK) {
                        home(stage);
                    } else {
                        failure.close();
                    }
                }
            } else {
                UIAlert fail = new UIAlert("Could not edit", "Please select an employee to edit", ButtonType.OK, ButtonType.CLOSE);
            }
        });
        MenuBtn editPassword = new MenuBtn("Edit admin password (admin)", "btn-secondary", "Edit the administrative password (admin privileges required)");
        editPassword.setOnAction(actionEvent -> {
            TextInputDialog verifyAdmin = new AdminPassDialog();
            // Check password against hash stored in "shadow"
            if(FH.passIsCorrect(verifyAdmin.getEditor().getText())) {
                TextInputDialog newPass = new TextInputDialog("Enter new admin password", "New admin password", "New admin password");
                // Make sure new password is at least 8 non-whitespace characters
                if(!newPass.getEditor().getText().matches("^[\s]{8}$")) {
                    FH.setPassword(newPass.getEditor().getText());
                } else {
                    UIAlert failure = new UIAlert("Failure", "Password invalid: please ensure the password has no whitespace and is at least 8 characters long", ButtonType.OK, ButtonType.CLOSE);
                }
            } else {
                UIAlert failure = new UIAlert("Failure", "Password incorrect", ButtonType.OK, ButtonType.CLOSE);
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
        MenuBtn back = new BackBtn();
        back.setOnAction(actionEvent -> home(stage));
        MenuBtn punchIn = new MenuBtn("Punch in", "btn-primary", "Punch in with employee ID");
        punchIn.setOnAction(e -> signIn(stage));
        MenuBtn punchOut = new MenuBtn("Punch out", "btn-secondary", "Punch out from current session");
        punchOut.setOnAction(e -> {
            UIAlert success = new UIAlert("Success", "Punched out successfully", ButtonType.OK, ButtonType.CANCEL);
            FH.getTimesFile().punchOut(employeeID);
            employeeID = 0;
            buttonsHolder.getChildren().clear();
            home(stage);
        });
        if(employeeID>0) {
            buttonsHolder.getChildren().addAll(back, punchOut);
        } else {
            buttonsHolder.getChildren().addAll(back, punchIn);
        }
        buttonsHolder.getChildren().add(editPassword);
        entryForm.getChildren().addAll(addName,addSalary,newEmployee, editEmployee);
        content.add(entryForm, 1, 1);
        content.add(schedule, 0, 1);
        content.add(buttonsHolder, 1, 0);
        content.getStyleClass().add("bg-danger");
        sceneInit(2, 2);
    }
    private static class TextField extends javafx.scene.control.TextField {
        public TextField(String s, double minH) {
            super(s);
            this.setMinHeight(minH);
        }
    }
    private static class TextInputDialog extends javafx.scene.control.TextInputDialog {
        public TextInputDialog(String s, String header, String title) {
            super(s);
            this.setHeaderText(header);
            this.setTitle(title);
            this.showAndWait();
        }
    }
    private static class AdminPassDialog extends TextInputDialog {
        public AdminPassDialog() {
            super("Enter admin password", "Password is required to complete that action", "Admin privileges required");
        }
    }
    private static class UIAlert extends Alert {
        public UIAlert(String title, String content, ButtonType bt1, ButtonType bt2) {
            super(AlertType.NONE, content, bt1, bt2);
            this.setTitle(title);
            this.showAndWait();
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
    private static class BackBtn extends MenuBtn {
        public BackBtn() {
            super("Return [Esc]", "btn-warning", "Return to the homepage");
            this.setCancelButton(true);
        }
    }
    public static void main(String[] args) {
        launch();
    }
}