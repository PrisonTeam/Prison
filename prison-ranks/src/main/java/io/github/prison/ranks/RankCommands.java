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

package io.github.prison.ranks;

import io.github.prison.Prison;
import io.github.prison.commands.Arg;
import io.github.prison.commands.Command;
import io.github.prison.internal.CommandSender;
import io.github.prison.internal.Player;
import io.github.prison.ranks.events.RankPromoteEvent;
import io.github.prison.ranks.events.RankSetEvent;
import io.github.prison.util.TextUtil;

/**
 * @author Camouflage100
 */
public class RankCommands {

    private RanksModule ranksModule;

    public RankCommands(RanksModule ranksModule) {
        this.ranksModule = ranksModule;
    }


    @Command(identifier = "ranks newrank", description = "Make a new rank", permissions = {"prison.ranks.newrank"})
    public void addRank(CommandSender sender, @Arg(name = "rankid") int id,
                        @Arg(name = "rankname") String name, @Arg(name = "cost") double cost,
                        @Arg(name = "tag") String tag) {
        //TODO: Implemend the code for this? lol
    }

    @Command(identifier = "ranks reload", description = "Reload ranks", permissions = {"prison.ranks.reload"})
    public void reloadRanks(CommandSender sender) {
        for (Rank rank : ranksModule.getRanks()) {
            ranksModule.unloadRank(rank);
        }

        ranksModule.loadAllRanks();
        sender.sendMessage(Messages.commandReload);
    }


    @Command(identifier = "ranks list", description = "List the ranks that are currently added", permissions = {"prison.ranks.list"})
    public void ranksListCommand(CommandSender sender) {
        if (ranksModule.getRanks().size() == 0) {
            sender.sendMessage(Messages.errorNoRanksLoaded);
        } else {
            sender.sendMessage(Messages.commandListHeadFood);
            for (Rank rank : ranksModule.getRanks()) {

                String tag;
                if (rank.getTag() == null) tag = "n/a";
                else tag = TextUtil.parse(rank.getTag());

                sender.sendMessage(String.format(
                        Messages.commandList,
                        rank.getName(),
                        rank.getRankId(),
                        rank.getRankLadder(),
                        tag,
                        rank.getCost()
                ));
            }
            sender.sendMessage(Messages.commandListHeadFood);
        }
    }

    @Command(identifier = "ranks checkrank", description = "Check the rank of a user", permissions = {"prison.ranks.checkrank"})
    public void checkRankCommand(CommandSender sender, @Arg(name = "player") String name) {
        Player targ = Prison.getInstance().getPlatform().getPlayer(name);
        RankUser targRank = null;
        if (targ != null)
            targRank = ranksModule.getUser(targ.getUUID());

        if (targ != null) {
            if (targRank.getRank() == null)
                targRank.setRank(ranksModule.getBottomRank());

            sender.sendMessage(String.format(
                    Messages.commandCheckRank,
                    targ.getName(),
                    ranksModule.getUser(targ.getUUID()).getRank().getName()
            ));
        } else {
            sender.sendMessage(String.format(Messages.errorPlayerNotFound, name));
        }
    }

    @Command(identifier = "ranks setrank", description = "Set the rank of a player", permissions = {"prison.ranks.setrank"})
    public void setRankCommand(CommandSender sender, @Arg(name = "player") String name, @Arg(name = "rank") String rank) {
        Player targ = Prison.getInstance().getPlatform().getPlayer(name);

        if (targ != null) {
            RankUser targRank = ranksModule.getUser(targ.getUUID());
            Rank oldRank = targRank.getRank();
            Rank newRank = null;

            for (Rank rank1 : ranksModule.getRanks()) {
                if (rank1.getName().equalsIgnoreCase(String.valueOf(rank)))
                    newRank = rank1;
            }

            if (newRank != null) {
                targRank.setRank(newRank);

                RankSetEvent event = new RankSetEvent(targ, oldRank, newRank);
                Prison.getInstance().getEventBus().post(event);
                if (event.isCanceled()) return;

                sender.sendMessage(String.format(
                        Messages.commandSetRank,
                        targ.getName(),
                        oldRank.getName(),
                        newRank.getName()
                ));
            } else {
                sender.sendMessage(String.format(Messages.errorInvalidRank, rank));
            }

        } else {
            sender.sendMessage(String.format(Messages.errorPlayerNotFound, name));
        }
    }

    @Command(identifier = "ranks promote", description = "Promote a player's rank", permissions = {"prison.ranks.promote"})
    public void promoteRankCommand(CommandSender sender, @Arg(name = "player") String name) {
        Player targ = Prison.getInstance().getPlatform().getPlayer(name);

        if (targ != null) {
            RankUser targRank = ranksModule.getUser(targ.getUUID());

            Rank currentRank = targRank.getRank();

            if (currentRank == ranksModule.getTopRank()) {
                sender.sendMessage(String.format(Messages.errorPlayerTopRank, targ.getName()));
                return;
            }
            Rank newRank = ranksModule.getRankByLadder(true, currentRank);

            RankPromoteEvent event = new RankPromoteEvent(targ, currentRank, newRank);
            Prison.getInstance().getEventBus().post(event);
            if (event.isCanceled()) return;
            targRank.setRank(newRank);

            sender.sendMessage(String.format(
                    Messages.commandPromote, targ.getName(),
                    currentRank.getName(),
                    newRank.getName())
            );
        } else {
            sender.sendMessage(String.format(Messages.errorPlayerNotFound, name));
        }
    }
}
