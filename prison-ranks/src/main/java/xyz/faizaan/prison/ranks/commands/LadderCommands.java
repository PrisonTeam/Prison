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

package xyz.faizaan.prison.ranks.commands;

import xyz.faizaan.prison.commands.Arg;
import xyz.faizaan.prison.commands.Command;
import xyz.faizaan.prison.internal.CommandSender;
import xyz.faizaan.prison.output.BulletedListComponent;
import xyz.faizaan.prison.output.ChatDisplay;
import xyz.faizaan.prison.output.LogLevel;
import xyz.faizaan.prison.output.Output;
import xyz.faizaan.prison.ranks.PrisonRanks;
import xyz.faizaan.prison.ranks.data.Rank;
import xyz.faizaan.prison.ranks.data.RankLadder;

import java.io.IOException;
import java.util.Optional;

import static xyz.faizaan.prison.ranks.PrisonRanks.loc;

/**
 * @author Faizaan A. Datoo
 */
public class LadderCommands {

    @Command(identifier = "ranks ladder create", description = "Creates a new rank ladder.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderAdd(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        Optional<RankLadder> ladderOptional =
                PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if (ladderOptional.isPresent()) {
            loc("ladder_exist").withReplacements(ladderName).sendTo(sender);
            return;
        }

        ladderOptional = PrisonRanks.getInstance().getLadderManager().createLadder(ladderName);

        if (!ladderOptional.isPresent()) {
            loc("ladder_create_fail").sendTo(sender, LogLevel.ERROR);
            return;
        }

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladderOptional.get());
        } catch (IOException e) {
            loc("ladder_create_fail").sendTo(sender, LogLevel.ERROR);
            Output.get().logError("Could not save ladder.", e);
            return;
        }

        loc("ladder_create_success").sendTo(sender);
    }

    @Command(identifier = "ranks ladder delete", description = "Deletes a rank ladder.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemove(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        Optional<RankLadder> ladder =
                PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if (!ladder.isPresent()) {
            loc("ladder_not_exist").withReplacements(ladderName).sendTo(sender, LogLevel.ERROR);
            return;
        }

        boolean success = PrisonRanks.getInstance().getLadderManager().removeLadder(ladder.get());
        if (success) {
            loc("ladder_delete_success").withReplacements(ladderName).sendTo(sender);
        } else {
            loc("ladder_delete_fail").withReplacements(ladderName).sendTo(sender, LogLevel.ERROR);
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
            loc("ladder_not_exist").withReplacements(ladderName).sendTo(sender);
            return;
        }

        ChatDisplay display = new ChatDisplay(ladder.get().name);
        display.text("&7This ladder contains the following ranks:");

        BulletedListComponent.BulletedListBuilder builder =
                new BulletedListComponent.BulletedListBuilder();
        for (RankLadder.PositionRank rank : ladder.get().ranks) {
            Optional<Rank> rankOptional =
                    PrisonRanks.getInstance().getRankManager().getRank(rank.getRankId());
            if (!rankOptional.isPresent()) {
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
            loc("ladder_not_exist").withReplacements(ladderName).sendTo(sender, LogLevel.ERROR);
            return;
        }

        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rank.isPresent()) {
            loc("rank_not_exist").withReplacements(rankName).sendTo(sender, LogLevel.ERROR);
            return;
        }

        if (ladder.get().containsRank(rank.get().id)) {
            loc("ladder_contains").withReplacements(ladderName, rankName).sendTo(sender, LogLevel.ERROR);
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
            loc("ladder_rankadd_fail").sendTo(sender, LogLevel.ERROR);
            Output.get().logError("Error while saving ladder.", e);
            return;
        }

        loc("ladder_rankadd_success").withReplacements(rank.get().name, ladder.get().name).sendTo(sender);
    }

    @Command(identifier = "ranks ladder delrank", description = "Removes a rank from a ladder.", onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemoveRank(CommandSender sender, @Arg(name = "ladderName") String ladderName,
                                 @Arg(name = "rankName") String rankName) {
        Optional<RankLadder> ladder =
                PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if (!ladder.isPresent()) {
            loc("ladder_not_exist").withReplacements(ladderName).sendTo(sender, LogLevel.ERROR);
            return;
        }

        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rank.isPresent()) {
            loc("rank_not_exist").withReplacements(rankName).sendTo(sender, LogLevel.ERROR);
            return;
        }

        ladder.get().removeRank(ladder.get().getPositionOfRank(rank.get()));

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladder.get());
        } catch (IOException e) {
            loc("ladder_rankremove_fail").sendTo(sender, LogLevel.ERROR);
            Output.get().logError("Error while saving ladder.", e);
            return;
        }

        loc("ladder_rankremove_success").withReplacements(rank.get().name, ladder.get().name).sendTo(sender);
    }

}
