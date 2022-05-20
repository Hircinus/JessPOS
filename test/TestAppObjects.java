import com.example.jesspos.*;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

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
    }
    /*
    @Test
    public void testTime() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(5, "screws", 44, 3.99));
        items.add(new Item(3, "nails", 34, 5.99));
        Instant now = Instant.now();
        Employee john = new Employee(1, "John");
        Time t1 = new Time(2, now, now);
        Time t2 = new Time();
        assertEquals(t1.getRawDate(), now);
        assertEquals(t1.getID(), 12);
        assertEquals(t1.getItemsCount(), 2);
        assertEquals(t1.getPriceDelta(), (items.get(0).getPrice()+items.get(1).getPrice()), 0.01);
        assertEquals(t1.getEmployee(), john);
        assertEquals(t1.getItems(), items);
    }

     */
}
