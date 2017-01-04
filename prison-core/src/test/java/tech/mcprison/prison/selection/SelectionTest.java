/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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

package tech.mcprison.prison.selection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.TestPlatform;
import tech.mcprison.prison.TestPlayer;
import tech.mcprison.prison.TestWorld;
import tech.mcprison.prison.platform.ItemStack;
import tech.mcprison.prison.platform.Player;
import tech.mcprison.prison.platform.World;
import tech.mcprison.prison.platform.events.PlayerInteractEvent;
import tech.mcprison.prison.util.Block;
import tech.mcprison.prison.util.Location;

import static org.junit.Assert.*;

/**
 * @author Faizaan A. Datoo
 */
public class SelectionTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before public void setUp() throws Exception {
        Prison.get().init(new TestPlatform(temporaryFolder.newFolder("test"), true));
    }

    @Test public void testSelection() throws Exception {
        Prison.get().getSelectionManager(); // init

        World ourWorld = new TestWorld("TestWorld");
        TestPlayer ourPlayer = new TestPlayer();

        Prison.get().getEventBus().post(new PlayerInteractEvent(ourPlayer, SelectionManager.SELECTION_TOOL, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, new Location(ourWorld, 10, 20, 30)));

        assertTrue(ourPlayer.getInput().contains("&7First position set to &8(10.0, 20.0, 30.0)"));

        Prison.get().getEventBus().post(new PlayerInteractEvent(ourPlayer, SelectionManager.SELECTION_TOOL, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, new Location(ourWorld, 30, 20, 40)));

        assertTrue(ourPlayer.getInput().contains("&7Second position set to &8(30.0, 20.0, 40.0)"));
    }

    @Test public void testSelectionToolCheck() throws Exception {
        Prison.get().getSelectionManager(); // init

        World ourWorld = new TestWorld("TestWorld");
        TestPlayer ourPlayer = new TestPlayer();

        int initialAmount = ourPlayer.getInput().size();

        Prison.get().getEventBus().post(new PlayerInteractEvent(ourPlayer, new ItemStack("test", 1, Block.ACACIA_SAPLING), PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, new Location(ourWorld, 10, 20, 30)));

        assertEquals(initialAmount, ourPlayer.getInput().size()); // nothing should have happened because we have the wrong item in our hand

    }
}
