package tech.mcprison.prison.util;

/**
 * An enum with the different types of inventory available in minecraft
 */
public enum InventoryType {

    /**
     * A chest inventory, with 0, 9, 18, 27, 36, 45, or 54 slots of type
     * CONTAINER.
     */
    CHEST(27, "Chest"), /**
     * A dispenser inventory, with 9 slots of type DEFAULT.
     */
    DISPENSER(9, "Dispenser"), /**
     * A dropper inventory, with 9 slots of type DEFAULT.
     */
    DROPPER(9, "Dropper"), /**
     * A furnace inventory, with a RESULT slot, a CRAFTING slot, and a FUEL
     * slot.
     */
    FURNACE(3, "Furnace"), /**
     * A workbench inventory, with 9 CRAFTING slots and a RESULT slot.
     */
    WORKBENCH(10, "Crafting"), /**
     * A player's crafting inventory, with 4 CRAFTING slots and a RESULT slot.
     * Also implies that the 4 ARMOR slots are accessible.
     */
    CRAFTING(5, "Crafting"), /**
     * An enchantment table inventory, with two CRAFTING slots and three
     * enchanting buttons.
     */
    ENCHANTING(2, "Enchanting"), /**
     * A brewing stand inventory, with one FUEL slot and three CRAFTING slots.
     */
    BREWING(5, "Brewing"), /**
     * A player's inventory, with 9 HOTBAR slots, 27 DEFAULT slots, 4 ARMOR
     * slots and 1 offhand slot. The ARMOR and offhand slots may not be visible
     * to the player, though.
     */
    PLAYER(41, "Player"), /**
     * The creative mode inventory, with only 9 QUICKBAR slots and nothing
     * else. The contents of the creative inventory with all the items is
     * fully client-side, meaning that servers cannot modify it.
     */
    CREATIVE(9, "Creative"), /**
     * The merchant inventory, with 2 CRAFTING slots, and 1 RESULT slot.
     */
    VILLAGER(3, "Villager"), /**
     * The ender chest inventory, with 27 DEFAULT slots.
     */
    ENDER_CHEST(27, "Ender Chest"), /**
     * An anvil inventory, with 2 CRAFTING slots and 1 RESULT slot
     */
    ANVIL(3, "Anvil"), /**
     * A beacon inventory, with 1 CRAFTING slot
     */
    BEACON(1, "Beacon"), /**
     * A hopper inventory, with 5 slots of type DEFAULT.
     */
    HOPPER(5, "Hopper"),;

    private final int size;
    private final String title;

    InventoryType(int defaultSize, String defaultTitle) {
        size = defaultSize;
        title = defaultTitle;
    }

    public int getDefaultSize() {
        return size;
    }

    public String getDefaultTitle() {
        return title;
    }

    public enum SlotType {
        /**
         * A result slot in a furnace or crafting inventory.
         */
        RESULT, /**
         * A slot on a crafting table or in the player's inventory crafting,
         * or the input slot in a furnace, the trade slot in a villager, the
         * potion slot in the brewing stand, or the enchantment slot on an
         * enchanting table.
         */
        CRAFTING, /**
         * An armor slot in the player's inventory.
         */
        ARMOR, /**
         * A regular slot in a container or the player's inventory
         */
        DEFAULT, /**
         * A slot in the bottom 9 slots ("hotbar").
         */
        HOTBAR, /**
         * A slot representing no slots. This is generally the area outside the inventory.
         */
        NONE, /**
         * The fuel slot in a furnace inventory, or the ingredient slot in a
         * brewing stand inventory.
         */
        FUEL;
    }
}