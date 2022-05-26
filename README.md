# JessPOS
![Last commit badge](https://img.shields.io/github/last-commit/Hircinus/JessPOS)
![Code quality](https://img.shields.io/lgtm/grade/java/github/Hircinus/JessPOS)
![License](https://img.shields.io/github/license/Hircinus/JessPOS)

JessPOS is a simple and efficient POS system built in Java with BootstrapFX-assisted UI. In short, it allows for simple inventory and employee management for small- to medium-sized businesses and handles transactions integrated with those two systems. Using Java makes for fairly straightforward development that can be easily deployed to any system that a business might already be using.

In the future, I hope to extend JessPOS to have a more sophisticated logging and statistics system that would allow it to also serve as a way for managers and business owners to keep track of sales in a more intelligent manner.

## Table of contents

 - [Description](#description)
 - [Screenshots](#screenshots)
   - [Home page](#home-page)
   - [Inventory page](#inventory-page)
   - [New transaction page](#new-transaction-page)
   - [Transactions history page](#transactions-history-page)
   - [Employees and timetables page page](#employees-and-timetables-page)
 - [Design](#design)
   - [Overview](#overview)
   - [UML Diagram](#uml-diagram)
 - [Known limitations](#known-limitations)

## Description

### Features

#### Inventory Management

The inventory management system allows users to create new items with a name, quantity in stock, and price (and an automatically created SKU number). They can view previously created items, remove a selected item, edit a selected item or sort through the table to find what they're looking for faster. The quantities of items adjusts automatically when they are added to completed transactions.

#### Employee Timetable System

The employee timetable system allows employees and managers to punch in when they come to their workstation, punch out at the end of their shift, and view their previous shifts as well as the durations of each shift, while also keeping track of the salary of each employee and displaying their pay per shift. This helps managers ensure payroll is accurate to when employees showed up, and allows employees to ensure that all the time they are at work is accounted for. Managers can also create new employee accounts using an administrator password with a name and an admin password, which can be changed (default is "p4$$w0rd").

#### Transaction Handling

The transaction handling system allows workers to start transactions after being signed in, add products to a transaction, remove any unwanted ones afterwards, and finally complete the transaction. All previous transactions are then viewable afterwards, with each transaction holding each item that was purchased as well as the employee that completed the transaction. Transactions are immutable after completion.

### Development

Developing this application was a fun endeavour, as its more of a personal project than a school one. Throughout my many customer service jobs I have used a variety of POS systems, but I always wondered how difficult it could be to make my own. Well, I certainly got that answer today. The main concept is that I store all my raw data in locally stored files that I then use methods of various file-handler classes to input, output, and edit data. This goes for everything from storing inventory items to keeping track of employee timetables.
Other than some pretty basic Java classes, BootstrapFX was used to help style things a bit, and I used GridPanes to organize the content on my various pages.

I think the biggest point of improvement for my project would be to have serialized my output (to make getting/setting items to a file more efficient), and a more sophisiticated hashing system for the administrator password (since simply hashing a String creates a number that can be achieved with other, different Strings, due to the limitations of basic hashing). A big issue I faced when creating the application was just trying to fathom the way everything was connected together, for instance, to show previous transactions I had to both get the previous transaction and then cross-reference the data from that transaction in the inventory to display the right information for each item. There were a lot of moving parts and the former potential solution in particular could've made things easier for me probably.

## Screenshots

### Home page
![Home page](https://github.com/Hircinus/JessPOS/blob/master/screenshots/jesspos_home.PNG)
### Inventory page
![Inventory page](https://github.com/Hircinus/JessPOS/blob/master/screenshots/jesspos_inventory.PNG)
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

### UML diagram

![UML diagram](https://github.com/Hircinus/JessPOS/blob/master/uml_diagram.png)

## Known Limitations

 - As of right now, only one employee can punch in at a workstation at a time.
 - To prevent conflicts with transactions logging, removing an item will not remove it from the system, nor will it decrement the SKU list so that previous transactions with said item could retrieve the item's properties when vieweing that transaction.
