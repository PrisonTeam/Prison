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

/**
 * @author Faizaan A. Datoo
 */
public class TestBean {

    @Column(name = "name")
    public String name;
    @Column(name = "age")
    public int age;
    @Column(name = "weight")
    public double weight;

    public TestBean(String name, int age, double weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    @BlueprintInit
    public TestBean(InstantiatedBlueprint blueprint) {
        this.name = String.valueOf(blueprint.getValue("name"));
        this.age = Integer.parseInt(String.valueOf(blueprint.getValue("age")));
        this.weight = Double.parseDouble(String.valueOf(blueprint.getValue("weight")));
    }

    @Override public String toString() {
        return "TestBean{" + "name='" + name + '\'' + ", age=" + age + ", weight=" + weight + '}';
    }
}
