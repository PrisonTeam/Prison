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

package xyz.faizaan.prison.convert;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton which runs all registered conversion agents.
 *
 * @author Faizaan A. Datoo
 */
public class ConversionManager {

    private static ConversionManager instance;
    private List<ConversionAgent> agents;

    public ConversionManager() {
        this.agents = new ArrayList<>();
    }

    public static ConversionManager getInstance() {
        if (instance == null) {
            instance = new ConversionManager();
        }
        return instance;
    }

    public void registerConversionAgent(ConversionAgent agent) {
        agents.add(agent);
    }

    public List<ConversionResult> runConversion() {
        List<ConversionResult> results = new ArrayList<>();
        for (ConversionAgent agent : agents) {
            ConversionResult result = agent.convert();
            results.add(result);
        }
        return results;
    }


}
