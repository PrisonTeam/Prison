/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.commands;

import tech.mcprison.prison.internal.CommandSender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) 
public @interface Command {

    /**
     * The description of this command
     */
    public String description() default "";

    /**
     * The identifier describes what command definition this will bind to. Spliced by spaces, you can
     * define as many sub commands as you want, as long as the first command (the root) is defined in
     * the plugin.yml file.<br><br> Example: {@code @Command(identifier="root sub1 sub2")}<br> The
     * first command "root" needs to be defined in the plugin.yml. The user will be able to access the
     * command by writing (if the root command does not choose an alias instead):<br> {@code /root
     * sub1 sub2}<br>
     */
    public String identifier();

    /**
     * If this command can only be executed by players (default true).<br> If you turn this to false,
     * the first parameter in the method must be the {@link CommandSender} to avoid {@link
     * ClassCastException}
     */
    public boolean onlyPlayers() default true;

    /**
     * The permissions to check if the user have before execution. If it is empty the command does not
     * require any permission.<br><br> If the user don't have one of the permissions, they will get an
     * error message stating that they do not have permission to use the command.
     */
    public String[] permissions() default {};

    
    /**
     * AltPermissions are alternative permissions that are not checked internally, or automatically.
     * It is up to the programmer to put hooks in to the code to check on these altPermissions.
     * This field of altPermissions is strictly for displaying helpful information to the end users
     * and it is only helpful if it is included.
     * 
     * For example the command /rankup has an optional parameter ladderName.  If a ladderName is 
     * provided, then it checks to see if the player has the permission: ranks.rankup.[ladderName].
     * 
     * Because these permissions are not ever used to check for actual permissions, it is very
     * important to provide parameters such as [ladderName] to signify where the server owner, or
     * admin, must place the real ladder name within the permission.
     * 
     * This is such a critically helpful feature because otherwise the only way you would know 
     * that you need this permission is to look at the source code, of which many cannot do, and 
     * those who can, may not know where to look.  So this provides very important information that
     * was not available before.
     * 
     */
    public String[] altPermissions() default {};
    
    
    /**
     * The aliases field provides the ability to define one or more aliases to register a given 
     * command with Bukkit.
     * 
     * @return
     */
    public String[] aliases() default {};
    
    
    /**
     * This is a list of URLs that should point back to the documentation that is appropriate 
     * to the command that these URLs are tied to.
     * 
     * @return
     */
    public String[] docURLs() default {};
    
    
}
