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

package tech.mcprison.prison.store;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.TestPlatform;
import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class StorageTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before public void setUp() throws Exception {
        Prison.get().init(new TestPlatform(temporaryFolder.newFolder("test"), false));
    }

    @Test public void testStorage() throws Exception {
        TestData data = new TestData("SirFaizdat",
            new Location(Prison.get().getPlatform().getWorld("test").get(), 10, 30, 45), "Faizaan");

        Prison.get().getPlatform().getStorage().write("sirfaizdat", data);

        TestData readData =
            Prison.get().getPlatform().getStorage().read("sirfaizdat", TestData.class);

        Assert.assertTrue(data.equals(readData));

    }
}
