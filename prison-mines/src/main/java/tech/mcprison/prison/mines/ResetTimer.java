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

package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all the reset timer tasks for each loaded mine.
 *
 * @author SirFaizdat
 */
public class ResetTimer {

    private MinesModule minesModule;
    private int syncTaskId;
    private Map<String, Integer> individualTaskIds; // <Mine name, task ID>

    public ResetTimer(MinesModule minesModule) {
        this.minesModule = minesModule;
        this.individualTaskIds = new HashMap<>();
    }

    /**
     * Start all of the timers.
     */
    public void startAll() {
        initSyncTask();
    }

    /**
     * Cancel all of the reset timer tasks, both synchronous and individual mine timers.
     */
    public void cancelAll() {
        Prison.getInstance().getPlatform().getScheduler().cancelTask(syncTaskId);
        for (int id : individualTaskIds.values())
            Prison.getInstance().getPlatform().getScheduler().cancelTask(id);
    }

    /**
     * Individual mines reset independently from the synchronous mines.
     * This method registers the individual mine's reset timer.
     *
     * @param mine The {@link Mine} to register.
     */
    public void registerIndividualMine(Mine mine) {
        int taskId = Prison.getInstance().getPlatform().getScheduler()
            .runTaskTimerAsync(() -> this.reset(mine), mine.getResetTimeSeconds(),
                mine.getResetTimeSeconds());
        individualTaskIds.put(mine.getName(), taskId);
        System.out.println("Registered individual mine " + mine.getName());
    }

    /**
     * Unregisters an independent mine, which effectively restores it to its synchronized state.
     *
     * @param mine The {@link Mine} to unregister.
     */
    public void unregisterIndividualMine(Mine mine) {
        Prison.getInstance().getPlatform().getScheduler()
            .cancelTask(individualTaskIds.get(mine.getName()));
        individualTaskIds.remove(mine.getName());
    }

    ///
    /// Private methods
    ///

    private void initSyncTask() {
        this.syncTaskId = Prison.getInstance().getPlatform().getScheduler().runTaskTimerAsync(
            () -> minesModule.getMines().stream().filter(Mine::isSync).forEach(this::reset),
            minesModule.getConfig().resetTimeSeconds, minesModule.getConfig().resetTimeSeconds);
    }

    private void reset(Mine mine) {
        System.out.println("Resetting individual mine " + mine.getName());
        minesModule.getResetManager().reset(mine);
    }

}
