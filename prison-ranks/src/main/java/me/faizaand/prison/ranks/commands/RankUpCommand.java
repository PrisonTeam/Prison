/*
 * Copyright (C) 2017 The MC-Prison Team
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

package me.faizaand.prison.ranks.commands;

import me.faizaand.prison.commands.Arg;
import me.faizaand.prison.commands.Command;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.ranks.PrisonRanks;
import me.faizaand.prison.ranks.RankUtil;
import me.faizaand.prison.ranks.data.RankLadder;
import me.faizaand.prison.ranks.data.RankPlayer;

import java.util.Optional;

/**
 * The commands for this module.
 *
 * @author Faizaan A. Datoo
 */
public class RankUpCommand {

    /*
     * /rankup command
     */

    @Command(identifier = "rankup", description = "Ranks up to the next rank.", permissions = {
        "ranks.user"}) public void rankUp(GamePlayer sender,
        @Arg(name = "ladder", description = "The ladder to rank up on.", def = "default")
            String ladderName) {

        // RETRIEVE THE LADDER

        // This player has to have permission to rank up on this ladder.
        if (!ladderName.equalsIgnoreCase("default") && !sender
            .hasPermission("ranks.rankup." + ladderName.toLowerCase())) {
            Output.get()
                .sendError(sender, "You need the permission '%s' to rank up on this ladder.",
                    "ranks.rankup." + ladderName.toLowerCase());
            return;
        }

        Optional<RankLadder> ladderOptional =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        // The ladder doesn't exist
        if (!ladderOptional.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' does not exist.", ladderName);
            return;
        }

        // RETRIEVE THE PLAYER

        Optional<RankPlayer> playerOptional =
            PrisonRanks.getInstance().getPlayerManager().getPlayer(sender.getUUID());

        // Well, this isn't supposed to happen...
        if (!playerOptional.isPresent()) {
            Output.get().sendError(sender,
                "You don't exist! The server has no records of you. Try rejoining, or contact a server administrator for help.");
            return;
        }

        // RANK-UP THE PLAYER

        RankPlayer player = playerOptional.get();

        RankUtil.RankUpResult result = RankUtil.rankUpPlayer(player, ladderName);

        switch (result.status) {
            case RankUtil.RANKUP_SUCCESS:
                Output.get().sendInfo(sender, "Congratulations! You have ranked up to rank '%s'.",
                    result.rank.name);
                break;
            case RankUtil.RANKUP_CANT_AFFORD:
                Output.get().sendError(sender,
                    "You don't have enough money to rank up! The next rank costs %s.",
                    RankUtil.doubleToDollarString(result.rank.cost));
                break;
            case RankUtil.RANKUP_HIGHEST:
                Output.get().sendInfo(sender, "You are already at the highest rank!");
                break;
            case RankUtil.RANKUP_FAILURE:
                Output.get().sendError(sender,
                    "Failed to retrieve or write data. Your files may be corrupted. Alert a server administrator.");
                break;
            case RankUtil.RANKUP_NO_RANKS:
                Output.get().sendError(sender, "There are no ranks in this ladder.");
                break;
        }

    }

}
