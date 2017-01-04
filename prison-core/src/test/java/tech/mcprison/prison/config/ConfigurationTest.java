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

package tech.mcprison.prison.config;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.TestPlatform;
import tech.mcprison.prison.platform.config.ConfigurationLoader;
import tech.mcprison.prison.platform.config.LoadResult;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Faizaan A. Datoo
 */
public class ConfigurationTest {

    @Rule public TemporaryFolder folder = new TemporaryFolder();

    File pluginDir;
    ConfigurationLoader loader;

    @Before public void setUp() throws Exception {

        pluginDir = folder.newFolder("PrisonTest");

        Prison.get().init(new TestPlatform(pluginDir, true));

        loader = new ConfigurationLoader(pluginDir, "test.json", TestConfigurable.class,
            TestConfigurable.VERSION);
    }

    @Test public void testLoad() throws Exception {
        LoadResult res = loader.loadConfiguration();

        assertTrue(res == LoadResult.SUCCESS || res == LoadResult.CREATED);
    }

    @Test public void testGet() throws Exception {
        loader.loadConfiguration();
        TestConfigurable test = (TestConfigurable) loader.getConfig();

        assertNotNull(test);
        assertEquals("Testing", test.testValue);
    }

    @Test public void testVersion() throws Exception {
        loader = new ConfigurationLoader(pluginDir, "test.json", TestConfigurable.class, 0);
        LoadResult res = loader.loadConfiguration();

        // if the file is being created for the first time, the loader skips the version check
        // so, let's load it again if this is the case
        if(res == LoadResult.CREATED) {
            res = loader.loadConfiguration();
        }

        assertEquals(LoadResult.REGENERATED, res);
    }
}
