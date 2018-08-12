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
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.output.LogLevel;
import xyz.faizaan.prison.output.Output;
import xyz.faizaan.prison.ranks.PrisonRanks;
import xyz.faizaan.prison.ranks.RankUtil;
import xyz.faizaan.prison.ranks.data.RankLadder;
import xyz.faizaan.prison.ranks.data.RankPlayer;

import java.util.Optional;

import static xyz.faizaan.prison.ranks.PrisonRanks.loc;

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
            "ranks.user"})
    public void rankUp(Player sender,
                       @Arg(name = "ladder", description = "The ladder to rank up on.", def = "default")
                               String ladderName) {

        // RETRIEVE THE LADDER

        // This player has to have permission to rank up on this ladder.
        String perm = "ranks.rankup." + ladderName.toLowerCase();
        if (!ladderName.equalsIgnoreCase("default") && !sender
                .hasPermission(perm)) {
            loc("rankup_need_permission").withReplacements(perm).sendTo(sender, LogLevel.ERROR);
            return;
        }

        Optional<RankLadder> ladderOptional =
                PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        // The ladder doesn't exist
        if (!ladderOptional.isPresent()) {
            loc("ladder_not_exist").withReplacements(ladderName).sendTo(sender, LogLevel.ERROR);
            return;
        }

        // RETRIEVE THE PLAYER

        Optional<RankPlayer> playerOptional =
                PrisonRanks.getInstance().getPlayerManager().getPlayer(sender.getUUID());

        // Well, this isn't supposed to happen...
        if (!playerOptional.isPresent()) {
            loc("you_not_exist").withReplacements(perm).sendTo(sender, LogLevel.ERROR);
            return;
        }

        // RANK-UP THE PLAYER

        RankPlayer player = playerOptional.get();

        RankUtil.RankUpResult result = RankUtil.rankUpPlayer(player, ladderName, true);

        switch (result.status) {
            case RankUtil.RANKUP_SUCCESS:
                loc("rankup_success").withReplacements(result.rank.name).sendTo(sender);
                break;
            case RankUtil.RANKUP_CANT_AFFORD:
                loc("rankup_cant_afford").withReplacements(RankUtil.doubleToDollarString(result.rank.cost)).sendTo(sender, LogLevel.ERROR);
                break;
            case RankUtil.RANKUP_HIGHEST:
                loc("rankup_highest").sendTo(sender, LogLevel.ERROR);
                break;
            case RankUtil.RANKUP_FAILURE:
                loc("rankup_failure").sendTo(sender, LogLevel.ERROR);
                break;
            case RankUtil.RANKUP_NO_RANKS:
                Output.get().sendError(sender, "There are no ranks in this ladder.");
                loc("rankup_no_ranks").sendTo(sender, LogLevel.ERROR);
                break;
        }

    }

}
