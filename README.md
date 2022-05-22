# JessPOS
![Last commit badge](https://img.shields.io/github/last-commit/Hircinus/JessPOS)
![Alerts](https://img.shields.io/lgtm/alerts/github/Hircinus/JessPOS)
![Code quality](https://img.shields.io/lgtm/grade/java/github/Hircinus/JessPOS)
![License](https://img.shields.io/github/license/Hircinus/JessPOS)

JessPOS is a simple and efficient POS system built in Java with BootstrapFX-assisted UI. In short, it allows for simple inventory and employee management for small- to medium-sized businesses and handles transactions integrated with those two systems. Using Java makes for fairly straightforward development that can be easily deployed to any system that a business might already be using.

In the future, I hope to extend JessPOS to have a more sophisticated logging and statistics system that would allow it to also serve as a way for managers and business owners to keep track of sales in a more intelligent manner.

## Table of contents

 - [Features](#features)
 - [Screenshots](#screenshots)
   - [Home page](#home-page)
   - [New transaction page](#new-transaction-page)
   - [Transactions history page](#transactions-history-page)
   - [Employees and timetables page page](#employees-and-timetables-page)
 - [Design](#design)
   - [Overview](#overview)
   - [UML Diagram](#uml-diagram)
 - [Known limitations](#known-limitations)

## Features

### Inventory Management

The inventory management system allows users to create new items with a name, quantity in stock, and price (and an automatically created SKU number). They can view previously created items, remove a selected item, edit a selected item or sort through the table to find what they're looking for faster. The quantities of items adjusts automatically when they are added to completed transactions.

### Employee Timetable System

The employee timetable system allows employees and managers to punch in when they come to their workstation, punch out at the end of their shift, and view their previous shifts as well as the durations of each shift, while also keeping track of the salary of each employee and displaying their pay per shift. This helps managers ensure payroll is accurate to when employees showed up, and allows employees to ensure that all the time they are at work is accounted for. Managers can also create new employee accounts using an administrator password with a name and a hardcoded password ID.

### Transaction Handling

The transaction handling system allows workers to start transactions after being signed in, add products to a transaction, remove any unwanted ones afterwards, and finally complete the transaction. All previous transactions are then viewable afterwards, with each transaction holding each item that was purchased as well as the employee that completed the transaction. Transactions are immutable after completion.

## Screenshots

### Home page
![Home page](https://github.com/Hircinus/JessPOS/blob/master/screenshots/jesspos_home.PNG)
### New transaction page
![New transaction page](https://github.com/Hircinus/JessPOS/blob/master/screenshots/jesspos_newtransaction.PNG)
### Transactions history page
![Transactions history page](https://github.com/Hircinus/JessPOS/blob/master/screenshots/jesspos_viewtransactions.PNG)
### Employees and timetables page
![Employees and timetables page](https://github.com/Hircinus/JessPOS/blob/master/screenshots/jesspos_employees.PNG)

## Design

### Overview

For this app, every class that describes an object stored by the program has an associated ``FileHandler`` subclass stored privately in it. FileHandler is not abstract since it is accessed directly for accessing each of its subclasses as well as the administrator password. Files that store inventory, transactions history, employees and employee punch-in/out times are simply ``.csv`` files that are parsed by its associated class. 
For instance, to get all stored employee objects, use: 
```java
FileHandler FH = new FileHandler(); // Initialize a new FileHandler to use throughout the class
...
FH.getEmployeesFile().getEmployees(); // returns an ObservableList<Employee> object
```
The UI is lightly styled using BootstrapFX and uses GridPanes to organize the root.

#### Custom classes and class constructors

For most buttons in the app, they are created using a custom ``MenuBtn`` class that extends ``Button``, which adds some parameters to its constructor to automatically add the necessary style classes and tooltip text:
```java
private static class MenuBtn extends Button {
    public MenuBtn(String text, String btnType, String tooltipText) {
        super(text);
        this.getStyleClass().addAll("btn", btnType);
        this.setMinHeight(100);
        this.setMaxSize(Double.MAX_VALUE, 150);
        this.setTooltip(new Tooltip(tooltipText));
    }
}
```

### UML diagram

![UML diagram](https://github.com/Hircinus/JessPOS/blob/master/uml_diagram.png)

## Known Limitations

 - As of right now, only one employee can punch in at a workstation at a time.
 - To prevent conflicts with transactions logging, removing an item will not remove it from the system, nor will it decrement the SKU list so that previous transactions with said item could retrieve the item's properties when vieweing that transaction.
