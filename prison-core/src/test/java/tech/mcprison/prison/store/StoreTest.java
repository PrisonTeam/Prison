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

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.MethodSorters;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.TestPlatform;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Faizaan A. Datoo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) public class StoreTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    File file;

    @Before public void setUp() throws Exception {
        Prison.get().init(new TestPlatform(temporaryFolder.newFolder("test"), true));

        // temp files weren't persisting so we're using normal files
        file = new File("temp.json");
        file.createNewFile();
        file.deleteOnExit();
    }

    // prefixed with a so it runs first
    @Test public void a_saveTest() throws Exception {
        TestJsonable jsonable = new TestJsonable();
        jsonable.toFile(file);
    }

    // prefixed with b so it runs after the saving
    @Test public void b_loadTest() throws Exception {
        TestJsonable jsonable = new TestJsonable();
        TestJsonable test = jsonable.fromFile(file);

        assertNotNull(test);
        assertEquals("Testing", test.test);
        assertEquals(jsonable.loc, test.loc);
    }
}
