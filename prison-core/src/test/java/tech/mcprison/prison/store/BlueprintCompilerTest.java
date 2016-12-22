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

package tech.mcprison.prison.store;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Faizaan A. Datoo
 */
public class BlueprintCompilerTest {

    @Test public void compile() throws Exception {

        Map<String, Object> expected = new HashMap<>();
        expected.put("name", "Faizaan");
        expected.put("age", 15);
        expected.put("weight", 133.6);

        TestBean bean = new TestBean("Faizaan", 15, 133.6);
        BlueprintCompiler compiler = new BlueprintCompiler(new TestBlueprint(), bean);

        Map<String, Object> result = compiler.compile();
        Assert.assertEquals(expected, result);
    }

    @Test public void decompile() throws Exception {

        String expected = "TestBean{name='Faizaan', age=15, weight=133.6}";

        InstantiatedBlueprint instantiatedBlueprint =
            new InstantiatedBlueprint(new TestBlueprint());
        instantiatedBlueprint.addValue("name", "Faizaan");
        instantiatedBlueprint.addValue("age", 15);
        instantiatedBlueprint.addValue("weight", 133.6);

        BlueprintCompiler compiler = new BlueprintCompiler(instantiatedBlueprint);
        TestBean bean = (TestBean) compiler.decompile();

        Assert.assertEquals(expected, bean.toString());

    }

}
