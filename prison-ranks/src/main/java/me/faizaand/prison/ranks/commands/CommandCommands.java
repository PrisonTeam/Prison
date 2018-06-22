package me.faizaand.prison.ranks.commands;

import me.faizaand.prison.chat.FancyMessage;
import me.faizaand.prison.commands.Arg;
import me.faizaand.prison.commands.Command;
import me.faizaand.prison.commands.Wildcard;
import me.faizaand.prison.internal.CommandSender;
import me.faizaand.prison.output.BulletedListComponent;
import me.faizaand.prison.output.ChatDisplay;
import me.faizaand.prison.output.FancyMessageComponent;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.ranks.PrisonRanks;
import me.faizaand.prison.ranks.data.Rank;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class CommandCommands {

    @Command(identifier = "ranks command add", description = "Adds a command to a rank.", onlyPlayers = false, permissions = "ranks.command")
    public void commandAdd(CommandSender sender, @Arg(name = "rank") String rankName,
        @Arg(name = "command") @Wildcard String command) {
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }
        Rank rank = rankOptional.get();

        if (rank.rankUpCommands == null) {
            rank.rankUpCommands = new ArrayList<>();
        }
        rank.rankUpCommands.add(command);

        Output.get().sendInfo(sender, "Added command '%s' to the rank '%s'.", command, rank.name);

    }

    @Command(identifier = "ranks command remove", description = "Removes a command from a rank.", onlyPlayers = false, permissions = "ranks.command")
    public void commandRemove(CommandSender sender, @Arg(name = "rank") String rankName,
        @Arg(name = "command") @Wildcard String command) {
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }
        Rank rank = rankOptional.get();

        if (rank.rankUpCommands == null) {
            rank.rankUpCommands = new ArrayList<>();
        }
        boolean did = rank.rankUpCommands.remove(command);

        if (!did) {
            Output.get()
                .sendWarn(sender, "The rank doesn't contain that command. Nothing was changed.");
        } else {
            Output.get()
                .sendInfo(sender, "Removed command '%s' from the rank '%s'.", command, rank.name);
        }

    }

    @Command(identifier = "ranks command list", description = "Lists the commands for a rank.", onlyPlayers = false, permissions = "ranks.command")
    public void commandList(CommandSender sender, @Arg(name = "rank") String rankName) {
        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }
        Rank rank = rankOptional.get();

        if (rank.rankUpCommands == null || rank.rankUpCommands.size() == 0) {
            Output.get().sendInfo(sender, "The rank '%s' contains no commands.", rank.name);
            return;
        }

        ChatDisplay display = new ChatDisplay("RankUpCommand for " + rank.tag);
        display.text("&8Click a command to remove it.");
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        for (String command : rank.rankUpCommands) {
            FancyMessage msg = new FancyMessage("&3/" + command)
                .command("/ranks command remove " + rankName + " " + command)
                .tooltip("Click to remove.");
            builder.add(msg);
        }

        display.addComponent(builder.build());
        display.addComponent(new FancyMessageComponent(
            new FancyMessage("&7[&a+&7] Add").suggest("/ranks command add " + rankName + " /")
                .tooltip("&7Add a new command.")));
        display.send(sender);
    }

}
