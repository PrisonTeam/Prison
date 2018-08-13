/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.selection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import xyz.faizaan.prison.Prison;
import xyz.faizaan.prison.TestPlatform;
import xyz.faizaan.prison.TestPlayer;
import xyz.faizaan.prison.TestWorld;
import xyz.faizaan.prison.internal.ItemStack;
import xyz.faizaan.prison.internal.World;
import xyz.faizaan.prison.internal.events.player.PlayerInteractEvent;
import xyz.faizaan.prison.util.BlockType;
import xyz.faizaan.prison.util.Location;
import xyz.faizaan.prison.util.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Faizaan A. Datoo
 */
public class SelectionTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before public void setUp() throws Exception {
        Prison.get().init(new TestPlatform(temporaryFolder.newFolder("test"), false));
    }

    @Test public void testSelection() throws Exception {
        Prison.get().getSelectionManager(); // init

        World ourWorld = new TestWorld("TestWorld");
        TestPlayer ourPlayer = new TestPlayer();

        ItemStack coloredToolItemStack = SelectionManager.SELECTION_TOOL;
        coloredToolItemStack
            .setDisplayName(Text.translateAmpColorCodes(coloredToolItemStack.getDisplayName()));

        Prison.get().getEventBus().post(new PlayerInteractEvent(ourPlayer, coloredToolItemStack,
            PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, new Location(ourWorld, 10, 20, 30)));

        assertTrue(ourPlayer.getInput().contains("&7First position set to &8(10, 20, 30)"));

        Prison.get().getEventBus().post(new PlayerInteractEvent(ourPlayer, coloredToolItemStack,
            PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, new Location(ourWorld, 30, 20, 40)));

        assertTrue(ourPlayer.getInput().contains("&7Second position set to &8(30, 20, 40)"));
    }

    @Test public void testSelectionToolCheck() throws Exception {
        Prison.get().getSelectionManager(); // init

        World ourWorld = new TestWorld("TestWorld");
        TestPlayer ourPlayer = new TestPlayer();

        int initialAmount = ourPlayer.getInput().size();

        Prison.get().getEventBus().post(
            new PlayerInteractEvent(ourPlayer, new ItemStack("test", 1, BlockType.ACACIA_SAPLING),
                PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, new Location(ourWorld, 10, 20, 30)));

        assertEquals(initialAmount, ourPlayer.getInput()
            .size()); // nothing should have happened because we have the wrong item in our hand

    }
}
