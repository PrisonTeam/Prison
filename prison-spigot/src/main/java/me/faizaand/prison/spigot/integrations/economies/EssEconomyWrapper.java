package me.faizaand.prison.spigot.integrations.economies;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import me.faizaand.prison.internal.GamePlayer;

import java.math.BigDecimal;

/**
 * A non-static wrapper of Essentials' annoyingly static {@link net.ess3.api.Economy} API.
 *
 * @author Faizaan A. Datoo
 */
class EssEconomyWrapper {

    double getBalance(GamePlayer player) {
        try {
            return Economy.getMoneyExact(player.getName()).doubleValue();
        } catch (UserDoesNotExistException e) {
            player.sendMessage("You don't exist in the economy plugin.");
            return 0.0;
        }
    }

    void setBalance(GamePlayer player, double amount) {
        try {
            Economy.setMoney(player.getName(), new BigDecimal(amount));
        } catch (UserDoesNotExistException | NoLoanPermittedException e) {
            player.sendMessage("You don't exist in the economy plugin.");
        }
    }

}
