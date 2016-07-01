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
import io.github.prison.ranks.events.RankDemoteEvent;
import io.github.prison.ranks.events.RankPromoteEvent;
import io.github.prison.ranks.events.RankSetEvent;
import io.github.prison.ranks.events.RankupEvent;
import io.github.prison.util.TextUtil;

/**
 * @author Camouflage100
 */
public class RankCommands {

    private RanksModule ranksModule;

    public RankCommands(RanksModule ranksModule) {
        this.ranksModule = ranksModule;
    }

    @Command(identifier = "ranks", description = "User Use: Displays ranks - Admin Use: Displays help command", onlyPlayers = false)
    public void ranksCommand(CommandSender sender) {
        if (!sender.hasPermission("prison.ranks.list.admin"))
            sender.dispatchCommand("ranks list");
        else
            sender.dispatchCommand("ranks help");
    }

    @Command(identifier = "ranks list", description = "List the ranks that are currently added", onlyPlayers = false)
    public void ranksListCommand(CommandSender sender) {
        if (ranksModule.getRanks().size() == 0) {
            sender.sendMessage(Messages.errorNoRanksLoaded);
        } else {
            sender.sendMessage(Messages.commandListHeadFood);
            for (Rank rank : ranksModule.getRanks()) {

                String tag;
                if (rank.getTag() == null) tag = "n/a";
                else tag = rank.getTag();

                String formattedCost = TextUtil.formatNumber(rank.getCost());

                if (sender.hasPermission("prison.ranks.list.admin"))
                    sender.sendMessage(String.format(
                            Messages.commandListAdmin,
                            rank.getName(),
                            rank.getRankId(),
                            rank.getRankLadder(),
                            tag,
                            formattedCost
                    ));

                else
                    sender.sendMessage(String.format(
                            Messages.commandListUser,
                            rank.getName(),
                            formattedCost
                    ));
            }
            sender.sendMessage(Messages.commandListHeadFood);
        }
    }

    @Command(identifier = "ranks newrank", description = "Make a new rank", permissions = {"prison.ranks.newrank"}, onlyPlayers = false)
    public void addRankCommand(CommandSender sender, @Arg(name = "rankid") int id,
                               @Arg(name = "rankname") String name, @Arg(name = "cost") double cost,
                               @Arg(name = "tag") String tag) {

        if (ranksModule.getRank(id) != null) {
            sender.sendMessage(String.format(Messages.errorRankExists, id));
            return;
        } else if (ranksModule.getRankByName(name) != null) {
            sender.sendMessage(String.format(Messages.errorRankExists, name));
            return;
        }

        ranksModule.getRanks().add(new Rank(id, cost, ranksModule.getNextLadder(), name, tag));
        sender.sendMessage(String.format(Messages.commandCreateRank, name));
    }

    @Command(identifier = "ranks settag", description = "Change the tag of a rank", permissions = {"prison.ranks.settag"}, onlyPlayers = false)
    public void setTagCommand(CommandSender sender, @Arg(name = "rankname") String name, @Arg(name = "tag") String tag) {
        if (ranksModule.getRankByName(name) == null) {
            sender.sendMessage(String.format(Messages.errorInvalidRank, name));
            return;
        }
        Rank rank = ranksModule.getRankByName(name);
        rank.setTag(tag);

        sender.sendMessage(String.format(Messages.commandSetTag, rank.getName(), rank.getTag()));
    }

    @Command(identifier = "ranks setrank", description = "Set the rank of a player", permissions = {"prison.ranks.setrank"}, onlyPlayers = false)
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

    @Command(identifier = "ranks setcost", description = "set the cost of a rank", permissions = {"prison.ranks.setcost"}, onlyPlayers = false)
    public void setCostCommand(CommandSender sender, @Arg(name = "rankname") String rankName, @Arg(name = "cost") double cost) {

        if (ranksModule.getRankByName(rankName) == null) {
            sender.sendMessage(String.format(Messages.errorInvalidRank, rankName));
            return;
        }

        ranksModule.getRankByName(rankName).setCost(cost);
        sender.sendMessage(String.format(Messages.commandSetRankCost, ranksModule.getRankByName(rankName).getName(), TextUtil.formatNumber(cost)));
    }

    @Command(identifier = "ranks checkrank", description = "Check the rank of a user", permissions = {"prison.ranks.checkrank"}, onlyPlayers = false)
    public void checkRankCommand(CommandSender sender, @Arg(name = "player") String name) {
        Player targ = Prison.getInstance().getPlatform().getPlayer(name);
        RankUser targRank = null;
        if (targ != null)
            targRank = ranksModule.getUser(targ.getUUID());

        if (targ != null) {
            if (targRank.getRank() == null) {
                targRank.setRank(ranksModule.getBottomRank());
                ranksModule.saveRankUser(targRank);
            }

            sender.sendMessage(String.format(
                    Messages.commandCheckRank,
                    targ.getName(),
                    ranksModule.getUser(targ.getUUID()).getRank().getName()
            ));
        } else {
            sender.sendMessage(String.format(Messages.errorPlayerNotFound, name));
        }
    }

    @Command(identifier = "ranks promote", description = "Promote a player's rank", permissions = {"prison.ranks.promote"}, onlyPlayers = false)
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
            ranksModule.saveRankUser(targRank);

            sender.sendMessage(String.format(
                    Messages.commandPromote, targ.getName(),
                    currentRank.getName(),
                    newRank.getName()
            ));
        } else {
            sender.sendMessage(String.format(Messages.errorPlayerNotFound, name));
        }
    }

    @Command(identifier = "ranks demote", description = "Demote a player's rank", permissions = {"prison.ranks.demote"}, onlyPlayers = false)
    public void demoteRankCommand(CommandSender sender, @Arg(name = "player") String name) {
        Player targ = Prison.getInstance().getPlatform().getPlayer(name);

        if (targ != null) {
            RankUser targRank = ranksModule.getUser(targ.getUUID());
            Rank currentRank = targRank.getRank();

            if (currentRank == ranksModule.getBottomRank()) {
                sender.sendMessage(String.format(Messages.errorPlayerBottomRank, targ.getName()));
                return;
            }

            Rank newRank = ranksModule.getRankByLadder(true, currentRank);

            RankDemoteEvent event = new RankDemoteEvent(targ, currentRank, newRank);
            Prison.getInstance().getEventBus().post(event);
            if (event.isCanceled()) return;
            targRank.setRank(newRank);
            ranksModule.saveRankUser(targRank);

            sender.sendMessage(String.format(
                    Messages.commandDemote, targ.getName(),
                    currentRank.getName(),
                    newRank.getName()
            ));
        } else {
            sender.sendMessage(String.format(Messages.errorPlayerNotFound, name));
        }
    }

    @Command(identifier = "rankup", description = "Rankup :D")
    public void rankupCommand(Player sender) {
        RankUser targRank = ranksModule.getUser(sender.getUUID());

        if (targRank.getRank() == ranksModule.getTopRank()) {
            sender.sendMessage(Messages.errorTopRank);
            return;
        }

        Rank nextRank = ranksModule.getRankByLadder(true, targRank.getRank());

        if (Prison.getInstance().getPlatform().getEconomy().canAfford(sender, nextRank.getCost())) {

            RankupEvent event = new RankupEvent(sender, targRank.getRank(), nextRank);
            Prison.getInstance().getEventBus().post(event);
            if (event.isCanceled()) return;

            Prison.getInstance().getPlatform().getEconomy().removeBalance(sender, nextRank.getCost());

            targRank.setRank(nextRank);
            sender.sendMessage(String.format(Messages.commandRankup, targRank.getRank().getName()));
        } else {
            sender.sendMessage(String.format(
                    Messages.errorNotEnoughMoney,
                    nextRank.getName(),
                    TextUtil.formatNumber(nextRank.getCost()),
                    TextUtil.formatNumber(Prison.getInstance().getPlatform().getEconomy().getBalance(sender))
            ));
        }
    }
}
