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

package tech.mcprison.prison.ranks.commands;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class LadderCommands {

    @Command(identifier = "ranks ladder create", description = "Creates a new rank ladder.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderAdd(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        Optional<RankLadder> ladderOptional =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if (ladderOptional.isPresent()) {
            Output.get()
                .sendError(sender, "A ladder with the name '%s' already exists.", ladderName);
            return;
        }

        ladderOptional = PrisonRanks.getInstance().getLadderManager().createLadder(ladderName);

        if (!ladderOptional.isPresent()) {
            Output.get().sendError(sender,
                "An error occurred while creating your ladder. &8Check the console for details.");
            return;
        }

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladderOptional.get());
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while creating your ladder. &8Check the console for details.");
            Output.get().logError("Could not save ladder.", e);
            return;
        }

        Output.get().sendInfo(sender, "The ladder '%s' has been created.", ladderName);
    }

    @Command(identifier = "ranks ladder delete", description = "Deletes a rank ladder.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemove(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        boolean success = PrisonRanks.getInstance().getLadderManager().removeLadder(ladder.get());
        if (success) {
            Output.get().sendInfo(sender, "The ladder '%s' has been deleted.", ladderName);
        } else {
            Output.get().sendError(sender,
                "An error occurred while removing your ladder. &8Check the console for details.");
        }
    }

    @Command(identifier = "ranks ladder list", description = "Lists all rank ladders.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderList(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("Ladders");
        BulletedListComponent.BulletedListBuilder list =
            new BulletedListComponent.BulletedListBuilder();
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()) {
            list.add(ladder.name);
        }
        display.addComponent(list.build());

        display.send(sender);
    }

    @Command(identifier = "ranks ladder listranks", description = "Lists the ranks within a ladder.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderInfo(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        ChatDisplay display = new ChatDisplay(ladder.get().name);
        display.text("&7This ladder contains the following ranks:");

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        for (RankLadder.PositionRank rank : ladder.get().ranks) {
            Optional<Rank> rankOptional =
                PrisonRanks.getInstance().getRankManager().getRank(rank.getRankId());
            if(!rankOptional.isPresent()) {
                continue; // Skip it
            }

            builder.add("&3#%d &8- &3%s", rank.getPosition(),
                rankOptional.get().name);
        }

        display.addComponent(builder.build());

        display.send(sender);
    }

    @Command(identifier = "ranks ladder addrank", description = "Adds a rank to a ladder.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderAddRank(CommandSender sender, @Arg(name = "ladderName") String ladderName,
        @Arg(name = "rankName") String rankName,
        @Arg(name = "position", def = "0", verifiers = "min[0]") int position) {
        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rank.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }

        if (ladder.get().containsRank(rank.get().id)) {
            Output.get()
                .sendError(sender, "The ladder '%s' already contains the rank '%s'.", ladderName,
                    rankName);
            return;
        }

        if (position > 0) {
            ladder.get().addRank(position, rank.get());
        } else {
            ladder.get().addRank(rank.get());
        }

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladder.get());
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while adding a rank to your ladder. &8Check the console for details.");
            Output.get().logError("Error while saving ladder.", e);
            return;
        }

        Output.get().sendInfo(sender, "Added rank '%s' to ladder '%s'.", rank.get().name,
            ladder.get().name);
    }

    @Command(identifier = "ranks ladder delrank", description = "Removes a rank from a ladder.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemoveRank(CommandSender sender, @Arg(name = "ladderName") String ladderName,
        @Arg(name = "rankName") String rankName) {
        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rank.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }

        ladder.get().removeRank(ladder.get().getPositionOfRank(rank.get()));

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladder.get());
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while removing a rank from your ladder. &8Check the console for details.");
            Output.get().logError("Error while saving ladder.", e);
            return;
        }

        Output.get().sendInfo(sender, "Removed rank '%s' from ladder '%s'.", rank.get().name,
            ladder.get().name);
    }

}
