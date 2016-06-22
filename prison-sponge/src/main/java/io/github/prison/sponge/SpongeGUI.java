/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.prison.sponge;

import io.github.prison.gui.Button;
import io.github.prison.gui.GUI;
import io.github.prison.internal.Player;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.custom.CustomInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.translation.FixedTranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SirFaizdat
 */
public class SpongeGUI implements GUI {

    private String title;
    private int numRows;
    private Map<Integer, Button> buttons;
    CustomInventory inventory;

    public SpongeGUI(String title, int numRows) {
        this.title = title;
        this.numRows = numRows;
        this.buttons = new HashMap<>();
    }

    @Override
    public void show(Player... players) {
        for (Player player : players)
            Sponge.getServer().getPlayer(player.getName()).get().openInventory(inventory, Cause.source(title).build());
    }

    @Override
    public GUI build() {
        inventory = CustomInventory.builder().name(new FixedTranslation(title)).size(numRows).build();
        for (Map.Entry<Integer, Button> entry : buttons.entrySet())
            inventory.set(new SlotIndex(entry.getKey()), buttonToItemStack(entry.getValue()));
        return this;
    }

    private ItemStack buttonToItemStack(Button button) {
        List<Text> text = new ArrayList<>();
        for (String lore : button.getLore()) text.add(TextSerializers.FORMATTING_CODE.deserialize(lore));
        return ItemStack.builder()
                .itemType(Sponge.getGame().getRegistry().getType(ItemType.class, "").get())
                .add(Keys.ITEM_LORE, text)
                .build();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getNumRows() {
        return numRows;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        return buttons;
    }

    @Override
    public GUI addButton(int slot, Button button) {
        buttons.put(slot, button);
        return this;
    }
}
