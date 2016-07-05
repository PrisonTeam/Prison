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

package io.github.prison.util;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import io.github.prison.Prison;

import java.lang.reflect.Method;

/**
 * A custom exception handler that prints out a pretty and informational message to the console
 * when an exception occurs. This allows us to avoid making try... catch blocks in each subscriber method (listener)
 * if we wanted to see exception data, which is quite nice, don't you think?
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class EventExceptionHandler implements SubscriberExceptionHandler {

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        Method method = context.getSubscriberMethod();
        Prison.getInstance().getPlatform().log("&7> &cEvent Exception!");
        Prison.getInstance().getPlatform().log("&cException thrown by subscriber method "
                + method.getName() + '(' + method.getParameterTypes()[0].getName() + ')'
                + " on listener " + context.getSubscriber().getClass().getName());
        Prison.getInstance().getPlatform().log("&6Here's the stack trace:");
        exception.printStackTrace();
        Prison.getInstance().getPlatform().log("&6Report this entire message to the developers if you can't solve the problem yourself.");
        Prison.getInstance().getPlatform().log("&7> &cEvent Exception!");
    }

}
