import com.example.jesspos.*;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestAppObjects {
    @Test
    public void testEmployee() {
        Employee e1 = new Employee(1, "John");
        Employee e2 = new Employee();
        e2.setName("Bella");
        assertEquals(e1.getID(), 1);
        assertEquals(e1.getName(), "John");
        assertEquals(e2.getID(), 0);
        assertEquals(e2.getName(), "Bella");
    }
    @Test
    public void testItem() {
        Item i1 = new Item(12, "hammer", 7, 99.99);
        Item i2 = new Item();
        i2.setName("screwdriver");
        assertEquals(i1.getSKU(), 12);
        assertEquals(i1.getName(), "hammer");
        assertEquals(i1.getQuantity(), 7);
        assertEquals(i1.getPrice(), 99.99, 0.01);
        assertEquals(i2.getSKU(), 0);
        assertEquals(i2.getName(), "screwdriver");
        assertEquals(i2.getQuantity(), 0);
        assertEquals(i2.getPrice(), 0.00, 0.01);
    }

    public void testItem(Item i, int SKU, String name, int quant, double price) {
        assertEquals(i.getSKU(), SKU);
        assertEquals(i.getName(), name);
        assertEquals(i.getQuantity(), quant);
        assertEquals(i.getPrice(), price, 0.01);
    }
    @Test
    public void testTransaction() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(5, "screws", 44, 3.99));
        items.add(new Item(3, "nails", 34, 5.99));
        Instant now = Instant.now();
        Employee john = new Employee(1, "John");
        Transaction t1 = new Transaction(12, john, now, items);
        Transaction t2 = new Transaction();
        assertEquals(t1.getRawDate(), now);
        assertEquals(t1.getID(), 12);
        assertEquals(t1.getItemsCount(), 2);
        assertEquals(t1.getPriceDelta(), (items.get(0).getPrice()+items.get(1).getPrice()), 0.01);
        assertEquals(t1.getEmployee(), john);
        assertEquals(t1.getItems(), items);
        // `getRawDate()` of empty transaction cannot be reliably verified since the Instant instantiated inside the object
        // assertEquals(t2.getRawDate(), t2.getRawDate());
        assertEquals(t2.getID(), 0);
        assertEquals(t2.getItemsCount(), 0);
        assertEquals(t2.getPriceDelta(), 0.00, 0.01);
        // `getEmployee()` of empty transaction cannot be reliably verified since the Employee instantiated inside the object
        // assertEquals(t2.getEmployee(), t2.getEmployee());
        assertEquals(t2.getItems(), new ArrayList<Item>());
    }

    @Test
    public void testTime() {
        Instant now = Instant.now();
        LocalDateTime ldt = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        Time t1 = new Time(2, now, now);
        Time t2 = new Time();
        assertEquals(t1.getID(), 2);
        assertEquals(t1.getConpin(), ldt.getMonth() + " " + ldt.getDayOfMonth() + " ; " + t1.generateNewHour(ldt) + ":" + t1.generateNewMinute(ldt));
        assertEquals(t2.getID(), 0);
        assertEquals(t2.getConpin(), ldt.getMonth() + " " + ldt.getDayOfMonth() + " ; " + t2.generateNewHour(ldt) + ":" + t2.generateNewMinute(ldt));
    }

    @Test
    public void testFileHandler() {
        FileHandler FH = new FileHandler();
        File f1 = new File("test1");
        File f2 = new File("test2");
        try {
            if (f1.createNewFile()) {
                System.out.println("File created: " + f1.getName());
            } else {
                System.out.println("File already exists.");
            }
            if (f2.createNewFile()) {
                System.out.println("File created: " + f2.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        FH.moveFileTo(f1, f2);
        assertTrue(f1.exists());
        assertFalse(f2.exists());
        assertEquals(FH.getInventoryFile().getSource().getName(), "inventory");
        assertEquals(FH.getEmployeesFile().getSource().getName(), "employees");
        assertEquals(FH.getTransactionsFile().getSource().getName(), "transactions");
        assertEquals(FH.getTimesFile().getSource().getName(), "times");
    }
    @Test
    public void testInventoryLog() {
        InventoryLog inventory = new InventoryLog(new File("testinventory"));
        inventory.addItem("hammer", "12", "45.99");
        inventory.addItem("screwdriver", "5", "26.99");
        inventory.addItem("bucket", "10", "12.99");
        inventory.incrementItem(1); // increment first item "hammer"
        inventory.decrementItem(2); // decrement second item "screwdriver"
        inventory.removeItem(3); // remove item "bucket" (sets quantity to 0)
        ObservableList<Item> items = inventory.getItems();
        assertEquals(items.size(), 3);
        testItem(items.get(0), 1, "hammer", 13, 45.99);
        testItem(items.get(1), 2, "screwdriver", 4, 26.99);
        testItem(items.get(2), 3, "bucket", 0, 12.99);
        ObservableList<Item> filteredItems = inventory.getFilteredItems();
        assertEquals(filteredItems.size(), 2);
        testItem(filteredItems.get(0), 1, "hammer", 13, 45.99);
        testItem(filteredItems.get(1), 2, "screwdriver", 4, 26.99);
        inventory.getSource().delete();
    }
    /*
    @Test
    public void testTimeLogAndEmployeeLog() {
        TimeLog times = new TimeLog(new File("testtimes"));
        EmployeeLog employees = new EmployeeLog(new File("testemployees"));
        times.addItem("hammer", "12", "45.99");
        times.addItem("screwdriver", "5", "26.99");
        times.addItem("bucket", "10", "12.99");
        times.incrementItem(1); // increment first item "hammer"
        times.decrementItem(2); // decrement second item "screwdriver"
        times.removeItem(3); // remove item "bucket" (sets quantity to 0)
        ObservableList<Item> items = times.getItems();
        assertEquals(items.size(), 3);
        testItem(items.get(0), 1, "hammer", 13, 45.99);
        testItem(items.get(1), 2, "screwdriver", 4, 26.99);
        testItem(items.get(2), 3, "bucket", 0, 12.99);
        ObservableList<Item> filteredItems = times.getFilteredItems();
        assertEquals(filteredItems.size(), 2);
        testItem(filteredItems.get(0), 1, "hammer", 13, 45.99);
        testItem(filteredItems.get(1), 2, "screwdriver", 4, 26.99);
        times.getSource().delete();
    }

     */
}
