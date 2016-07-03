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

package io.github.prison.internal;

/**
 * Allows tasks to be scheduled to run later or at intervals, on the server thread or on its own thread.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public interface Scheduler {

    /**
     * Run a task on the server thread, after a certain amount of time.
     *
     * @param run   The {@link Runnable} with the task inside.
     * @param delay The time to wait, in seconds, until the task is run.
     * @return The task ID.
     */
    int runTaskLater(Runnable run, long delay);

    /**
     * Run a task on its own thread, after a certain amount of time.
     *
     * @param run   The {@link Runnable} with the task inside.
     * @param delay The time to wait, in seconds, until the task is run.
     * @return The task ID.
     */
    int runTaskLaterAsync(Runnable run, long delay);

    /**
     * Run a task on the server thread, at the specified interval.
     *
     * @param run      The {@link Runnable} with the task inside.
     * @param delay    The time to wait, in seconds, until the timer is started.
     * @param interval The time between runs, in seconds.
     * @return The task ID.
     */
    int runTaskTimer(Runnable run, long delay, long interval);

    /**
     * Run a task on its own thread, at the specified interval.
     *
     * @param run      The {@link Runnable} with the task inside.
     * @param delay    The time to wait, in seconds, until the timer is started.
     * @param interval The time between runs, in seconds.
     * @return The task ID.
     */
    int runTaskTimerAsync(Runnable run, long delay, long interval);

    /**
     * Cancels all tasks registered through this scheduler.
     */
    void cancelAll();

}
