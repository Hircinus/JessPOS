# JessPOS

JessPOS is a simple and efficient POS system built in Java with BootstrapFX-assisted UI. In short, it allows for simple inventory and employee management for small- to medium-sized businesses and handles transactions integrated with those two systems. Using Java makes for fairly straightforward development that can be easily deployed to any system that a business might already be using.

In the future, I hope to extend JessPOS to have a more sophisticated logging and statistics system that would allow it to also serve as a way for managers and business owners to keep track of sales in a more intelligent manner.

## Features

- [Inventory management](#inventory-management)
- [Employee punch system](#employee-timetable-system)
- [Transaction handling](#transaction-handling)

### Inventory Management

The inventory management system allows users to create new items with a name, quantity in stock, and price (and an automatically created SKU number). They can view previously created items, remove a selected item, edit a selected item or sort through the list to find what they're looking for faster. The quantities of items adjusts automatically when they are added to completed transactions.

### Employee Timetable System

The employee timetable system allows employees and managers to punch in when they come to their workstation, punch out at the end of their shift, and view their previous shifts as well as the durations of each shift, while also keeping track of the salary of each employee and displaying their pay per shift stored. This helps managers ensure payroll is accurate to when employees showed up, and allows employees to ensure that all the time they are at work is accounted for. Managers can also create new employee accounts using an administrator password with a name and a hardcoded password ID.

### Transaction Handling

The transaction handling system allows workers to start transactions after being signed in, add products to a transaction, remove any unwanted ones afterwards, and finally complete the transaction. All previous transactions are then viewable afterwards, with each transaction holding each item that was purchased as well as the employee that completed the transaction. Transactions are immutable after completion.

## Limitations

 - As of right now, only one employee can punch in at a workstation at a time.
 - To prevent conflicts with transactions logging, removing an item will not remove it from the system, nor will it decrement the SKU list so that previous transactions with said item could retrieve the item's properties when vieweing that transaction.
