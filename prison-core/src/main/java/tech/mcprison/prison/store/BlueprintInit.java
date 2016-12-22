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

import java.lang.annotation.*;

/**
 * Denotes the constructor that should be used when creating a new object for a loaded row in a database.
 * This constructor must only contain the @Column-annoted variables as parameters, or deserialization <i>will not work</i>.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.CONSTRUCTOR)
public @interface BlueprintInit {

}
