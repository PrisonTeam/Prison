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

package tech.mcprison.prison.output;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.util.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A bulleted list component for all your listing needs.
 * Use the {@link BulletedListBuilder} to create it.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class BulletedListComponent extends DisplayComponent {

    private List<FancyMessage> messages;

    BulletedListComponent(List<FancyMessage> messages) {
        this.messages = messages;
    }

    @Override public String text() {
        List<String> messageStrs = new ArrayList<>(messages.size());
        messages.forEach(message -> messageStrs.add(message.toOldMessageFormat()));
        return Text.implode(messageStrs, "\n");
    }

    @Override public void send(CommandSender sender) {
        messages.forEach(message -> message.send(sender));
    }

    public static class BulletedListBuilder {

        private List<FancyMessage> bullets;

        public BulletedListBuilder() {
            this.bullets = new ArrayList<>();
        }
        
        public BulletedListBuilder add(FancyMessage message) {
        	bullets.add(message);
        	return this;
        }

        public BulletedListBuilder add(RowComponent row) {
            bullets.add( row.getFancy() );
            return this;
        }

        public BulletedListBuilder add(String text, Object... args) {
            text = String.format(text, args);
            return add(new FancyMessage(text));
        }

        public BulletedListComponent build() {
            for (FancyMessage bullet : bullets) {
                bullet.prefix("&7* ");
            }

            return new BulletedListComponent(bullets);
        }

    }

}
