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

package tech.mcprison.prison.chat;

import org.junit.Test;
import tech.mcprison.prison.util.ChatColor;

import static org.junit.Assert.assertEquals;

/**
 * @author Faizaan A. Datoo
 */
public class ChatTest {

    @Test public void testFancyChat() throws Exception {
        FancyMessage message =
            new FancyMessage("Test").link("http://google.com").color(ChatColor.AQUA).then("ing")
                .color(ChatColor.BLACK);

        String expected =
            "{\"text\":\"\",\"extra\":[{\"text\":\"Test\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://google.com\"}},{\"text\":\"ing\",\"color\":\"black\"}]}";
        String actual = message.toJSONString();

        assertEquals(expected, actual);
    }

}
