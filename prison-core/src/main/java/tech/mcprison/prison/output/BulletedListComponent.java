/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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

import tech.mcprison.prison.util.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A bulleted list component for all your listing needs.
 * Use the {@link BulletedListBuilder} to create it.
 *
 * @author Faizaan A. Datoo
 * @since API 0.1
 */
public class BulletedListComponent extends TextComponent {

    public BulletedListComponent(String text) {
        super(text);
    }

    public static class BulletedListBuilder {

        private List<String> bullets;

        public BulletedListBuilder() {
            this.bullets = new ArrayList<>();
        }

        public BulletedListBuilder add(String text, Object... args) {
            text = String.format(text, args);
            this.bullets.add("&7•&r " + text);
            return this;
        }

        public BulletedListComponent build() {
            String list = Text.translateAmpColorCodes(
                Text.implode(bullets.toArray(new String[bullets.size()]), "\n"));

            return new BulletedListComponent(list);
        }

    }

}
