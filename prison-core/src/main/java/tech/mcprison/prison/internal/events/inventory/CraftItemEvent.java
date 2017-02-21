package tech.mcprison.prison.internal.events.inventory;

import tech.mcprison.prison.internal.inventory.CraftingInventory;
import tech.mcprison.prison.internal.inventory.Recipe;
import tech.mcprison.prison.internal.inventory.Viewable;
import tech.mcprison.prison.util.InventoryType;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class CraftItemEvent extends InventoryClickEvent {
    protected Recipe recipe;
    public CraftItemEvent(Recipe recipe, Viewable what, InventoryType.SlotType type, int slot, Click click, Action action)
    {
        super(what,type,slot,click,action);
        this.recipe = recipe;
    }
    public CraftItemEvent(Recipe recipe, Viewable what, InventoryType.SlotType type, int slot, Click click, Action action, int key)
    {
        super(what,type,slot,click,action,key);
        this.recipe = recipe;
    }
    public CraftingInventory getInventory() {
        return (CraftingInventory) transaction.getTopInventory();
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
