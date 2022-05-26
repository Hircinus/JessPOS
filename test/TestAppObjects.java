import com.example.jesspos.*;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestAppObjects {
    @Test
    public void testEmployee() {
        Employee e1 = new Employee(1, "John", 19.99);
        Employee e2 = new Employee();
        e2.setName("Bella");
        assertEquals(e1.getID(), 1);
        assertEquals(e1.getName(), "John");
        assertEquals(e1.getSalary(), 19.99, 0.01);
        assertEquals(e2.getID(), 0);
        assertEquals(e2.getName(), "Bella");
        assertEquals(e2.getSalary(), 0.00, 0.01);
    }
    @Test
    public void testItem() {
        Item i1 = new Item(12, "hammer", 7, 99.99);
        Item i2 = new Item();
        i2.setName("screwdriver");
        assertEquals(i1.getID(), 12);
        assertEquals(i1.getName(), "hammer");
        assertEquals(i1.getQuantity(), 7);
        assertEquals(i1.getPrice(), 99.99, 0.01);
        assertEquals(i2.getID(), 0);
        assertEquals(i2.getName(), "screwdriver");
        assertEquals(i2.getQuantity(), 0);
        assertEquals(i2.getPrice(), 0.00, 0.01);
    }

    public void testItem(Item i, int SKU, String name, int quant, double price) {
        assertEquals(i.getID(), SKU);
        assertEquals(i.getName(), name);
        assertEquals(i.getQuantity(), quant);
        assertEquals(i.getPrice(), price, 0.01);
    }
    /*
    // Cannot reliably test `Transaction` since it is dependent on `Time` which has a hardcoded link to the main "employees" file when calculating the "pay" field
    @Test
    public void testTransaction() {}

    @Test
    public void testTime() {}
     */
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
        }
        FH.moveFileTo(f1, f2);
        // Cannot reliably check "passIsCorrect(String)" since it requires knowing the admin password that is unreachable due to being hashed in the "shadow" file
        // assertTrue(FH.passIsCorrect("p4$$w0rd"));
        FH.setPassword("poggers1234");
        assertTrue(FH.passIsCorrect("poggers1234"));
        assertTrue(f1.exists());
        assertFalse(f2.exists());
        assertEquals(FH.getInventoryFile().getSource().getName(), "inventory");
        assertEquals(FH.getEmployeesFile().getSource().getName(), "employees");
        assertEquals(FH.getTransactionsFile().getSource().getName(), "transactions");
        assertEquals(FH.getTimesFile().getSource().getName(), "times");
        f1.delete();
    }
    @Test
    public void testInventoryLog() {
        InventoryLog inventory = new InventoryLog(new File("testinventory"));
        inventory.addItem("hammer", "12", "45.99");
        inventory.addItem("screwdriver", "5", "26.99");
        inventory.addItem("bucket", "10", "12.99");
        inventory.addItem("paint brush", "6", "8.99");
        inventory.incrementItem(1); // increment first item "hammer"
        inventory.decrementItem(2); // decrement second item "screwdriver"
        inventory.removeItem(3); // remove item "bucket" (sets quantity to 0)
        ObservableList<Item> items = inventory.getItems();
        assertEquals(items.size(), 4);
        testItem(items.get(0), 1, "hammer", 13, 45.99);
        testItem(items.get(1), 2, "screwdriver", 4, 26.99);
        testItem(items.get(2), 3, "bucket", 0, 12.99);
        testItem(items.get(3), 4, "paint brush", 6, 8.99);
        inventory.setItem(4, "paint brush kit", "5", "10.99");
        items = inventory.getItems();
        testItem(items.get(3), 4, "paint brush kit", 5, 10.99);
        ObservableList<Item> filteredItems = inventory.getFilteredItems();
        assertEquals(filteredItems.size(), 3);
        testItem(filteredItems.get(0), 1, "hammer", 13, 45.99);
        testItem(filteredItems.get(1), 2, "screwdriver", 4, 26.99);
        inventory.getSource().delete();
    }
    /*
    // Cannot reliably test TransactionLog, TimeLog or EmployeeLog since they all rely on Time which has hardcoded link to EmployeeLog when calculating "pay" field
    @Test
    public void testTransactionLog() {}

    @Test
    public void testTimeLogAndEmployeeLog() {}
     */
}
