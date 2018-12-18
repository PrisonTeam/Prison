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

package tech.mcprison.prison.troubleshoot.inbuilt;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.troubleshoot.TroubleshootResult;
import tech.mcprison.prison.troubleshoot.Troubleshooter;
import tech.mcprison.prison.util.ItemManager;

import java.io.File;

/**
 * Inbuilt troubleshooter to scan the 'items.csv' file to ensure it's valid.
 *
 * @author Faizaan A. Datoo
 */
public class ItemTroubleshooter extends Troubleshooter {

    public ItemTroubleshooter() {
        super("item_scan", "Run this if you have trouble with the items.csv file.");
    }

    @Override public TroubleshootResult invoke(CommandSender invoker) {

        // Let's do our own test of initializing the ItemManager.
        try {
            ItemManager ourManager = new ItemManager();
            ourManager.getItems();
        } catch (Exception e) {
            // OK, so something's wrong
            // Let's try deleting the file and telling the user to relaunch.

            File itemsCsv = new File(PrisonAPI.getPluginDirectory(), "items.csv");
            boolean deleted = itemsCsv.delete();
            if (deleted) {
                return new TroubleshootResult(TroubleshootResult.Result.USER_ACTION,
                    "We've found a problem with your items.csv file. We deleted it so that a new and non-corrupted one is generated. Please restart your server for the changes to take effect.");
            } else {
                // We can only hot delete on *NIX systems.
                return new TroubleshootResult(TroubleshootResult.Result.FAILURE,
                    "We've found a problem with your items.csv file. We tried deleting it, but it could not be successfully deleted. Please stop your server, delete '/plugins/Prison/items.csv', and start your server again.");
            }
        }

        // Nothing is wrong.
        return new TroubleshootResult(TroubleshootResult.Result.SUCCESS,
            "No problems were found with your item manager or items.csv file.");
    }
}
