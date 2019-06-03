package ua.gram.munhauzen.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.gram.munhauzen.GameStateProvider;
import ua.gram.munhauzen.GdxTest;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Inventory;

import static org.junit.Assert.*;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InventoryServiceTest extends GdxTest {

    GameState state;
    InventoryService service;

    @Before
    public void setUp() throws Exception {
        state = GameStateProvider.create();
        service = new InventoryService(state);
    }

    @After
    public void tearDown() throws Exception {
        service = null;
        state = null;
    }

    @Test
    public void addGlobalInventory_should_add_inventory_to_global() {
        Inventory i1 = new Inventory();
        i1.isStatue = true;
        i1.name = "TEST";

        assertTrue("Inventory is not global!", i1.isGlobal());

        service.addGlobalInventory(i1);

        assertTrue("Missing inventory in global inventory", state.history.globalInventory.contains("TEST"));
    }

    @Test
    public void addInventory_should_add_inventory_to_active_save() {
        Inventory i1 = new Inventory();
        i1.name = "TEST";

        assertFalse("Inventory is global!", i1.isGlobal());

        service.addInventory(i1);

        assertTrue("Missing inventory in save inventory", state.history.activeSave.inventory.contains("TEST"));
    }

    @Test
    public void addInventory_with_relatedInventory_should_recursively_add_inventory_to_active_save() {
        Inventory i1 = new Inventory();
        i1.name = "CONDITIONAL_INVENTORY";
        i1.relatedInventory.add("CONDITION1");
        i1.relatedInventory.add("CONDITION2");

        Inventory i2 = new Inventory();
        i2.name = "CONDITION1";

        Inventory i3 = new Inventory();
        i3.name = "CONDITION2";

        service.addInventory(i2);

        assertTrue("Missing CONDITION1 inventory in save inventory",
                state.history.activeSave.inventory.contains("CONDITION1"));

        assertFalse("Found CONDITIONAL_INVENTORY inventory in save inventory",
                state.history.activeSave.inventory.contains("CONDITIONAL_INVENTORY"));

        service.addInventory(i3);

        assertTrue("Missing CONDITION2 inventory in save inventory",
                state.history.activeSave.inventory.contains("CONDITION2"));

        assertFalse("Found CONDITIONAL_INVENTORY inventory in save inventory",
                state.history.activeSave.inventory.contains("CONDITIONAL_INVENTORY"));

        service.addInventory(i1);

        assertTrue("Missing CONDITIONAL_INVENTORY inventory in save inventory",
                state.history.activeSave.inventory.contains("CONDITIONAL_INVENTORY"));
    }

    @Test
    public void addglobalInventory_with_relatedInventory_should_recursively_add_inventory_to_global() {
        Inventory i1 = new Inventory();
        i1.isMenu = true;
        i1.name = "CONDITIONAL_INVENTORY";
        i1.relatedInventory.add("CONDITION1");
        i1.relatedInventory.add("CONDITION2");

        Inventory i2 = new Inventory();
        i2.isStatue = true;
        i2.name = "CONDITION1";

        Inventory i3 = new Inventory();
        i3.isStatue = true;
        i3.name = "CONDITION2";

        service.addGlobalInventory(i2);

        assertTrue("Missing CONDITION1 inventory in save inventory",
                state.history.globalInventory.contains("CONDITION1"));

        assertFalse("Found CONDITIONAL_INVENTORY inventory in save inventory",
                state.history.globalInventory.contains("CONDITIONAL_INVENTORY"));

        service.addGlobalInventory(i3);

        assertTrue("Missing CONDITION2 inventory in save inventory",
                state.history.globalInventory.contains("CONDITION2"));

        assertFalse("Found CONDITIONAL_INVENTORY inventory in save inventory",
                state.history.globalInventory.contains("CONDITIONAL_INVENTORY"));

        service.addGlobalInventory(i1);

        assertTrue("Missing CONDITIONAL_INVENTORY inventory in save inventory",
                state.history.globalInventory.contains("CONDITIONAL_INVENTORY"));
    }

}