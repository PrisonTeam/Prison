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

package io.github.prison.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

/**
 * @author Camouflage100
 */
public class SpongeScheduler implements io.github.prison.Scheduler {

    private Scheduler scheduler;
    private Task.Builder taskBuilder;

    public SpongeScheduler() {
        this.scheduler = Sponge.getScheduler();
        this.taskBuilder = scheduler.createTaskBuilder();
    }

    @Override
    public int runTaskLater(Runnable run, long delay) {
        int id = (int) (Math.random() * Math.random());
        taskBuilder.execute(run).delay(delay, TimeUnit.SECONDS).name(String.valueOf(id));
        return id;
    }

    @Override
    public int runTaskLaterAsync(Runnable run, long delay) {
        int id = (int) (Math.random() * Math.random());
        taskBuilder.execute(run).delay(delay, TimeUnit.SECONDS).name(String.valueOf(id)).async();
        return id;
    }

    @Override
    public int runTaskTimer(Runnable run, long delay, long interval) {
        int id = (int) (Math.random() * Math.random());
        taskBuilder.execute(run).delay(delay, TimeUnit.SECONDS).interval(interval, TimeUnit.SECONDS).name(String.valueOf(id));
        return id;
    }

    @Override
    public int runTaskTimerAsync(Runnable run, long delay, long interval) {
        int id = (int) (Math.random() * Math.random());
        taskBuilder.execute(run).delay(delay, TimeUnit.SECONDS).interval(interval, TimeUnit.SECONDS).async();
        return id;
    }

    @Override
    public void cancelAll() {
        scheduler.getScheduledTasks().forEach(Task::cancel);
    }
}
