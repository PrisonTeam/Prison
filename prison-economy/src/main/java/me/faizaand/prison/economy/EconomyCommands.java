package me.faizaand.prison.economy;

import me.faizaand.prison.commands.Arg;
import me.faizaand.prison.commands.Command;
import me.faizaand.prison.internal.CommandSender;
import me.faizaand.prison.internal.GamePlayer;

class EconomyCommands {

    // todo figure out command aliases

    @Command(identifier = "economy give", description = "Gives a player a certain amount of in-game currency.", onlyPlayers = false, permissions = "prison.economy.admin")
    void economyGive(CommandSender sender, @Arg(name = "amount", description = "The amount of money to give.") String amount) {

    }

    @Command(identifier = "economy take", description = "Takes from a player a certain amount of in-game currency.", onlyPlayers = false, permissions = "prison.economy.admin")
    void economyTake(CommandSender sender, @Arg(name = "amount", description = "The amount of money to take.") String amount) {

    }

    @Command(identifier = "economy set", description = "Sets a player's balance to a certain amount of in-game currency.", onlyPlayers = false, permissions = "prison.economy.admin")
    void economySet(CommandSender sender, @Arg(name = "amount", description = "The amount of money to set the account to.") String amount) {

    }

    @Command(identifier = "pay", description = "Pay another player with some in-game currency.", onlyPlayers = false, permissions = "prison.economy.pay")
    void pay(CommandSender sender,
             @Arg(name = "other", description = "Who you're sending money to.") GamePlayer other,
             @Arg(name = "amount", description = "The amount of money to give.") String amount) {

    }

    @Command(identifier = "balance", description = "See the balance of yourself or another player.", onlyPlayers = false, permissions = "prison.economy.balance")
    void balance(CommandSender sender,
                 @Arg(name = "player", description = "The player to see the balance of.", def = "") String player) {

    }

}
