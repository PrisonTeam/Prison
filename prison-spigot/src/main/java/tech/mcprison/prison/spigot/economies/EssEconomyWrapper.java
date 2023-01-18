package tech.mcprison.prison.spigot.economies;

import java.math.BigDecimal;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import tech.mcprison.prison.internal.Player;

/**
 * <p>A non-static wrapper of Essentials' annoyingly static {@link net.ess3.api.Economy} API.
 * </p>
 *
 * <p><b>Warning:</b>  There is a known issue with essentials where internally
 *                    they are generating a NPE when trying it initially look up
 *                    a player when their names do not match, or their UUIDs have
 *                    been changed due to changing to an offline server.  Basically
 *                    it's an issue that has been troubling essentials for a while
 *                    now (a few years), so they are internally generating a 
 *                    stack trace when they detect a certain condition.  This 
 *                    stack trace is not able to be caught and suppressed by Prison, 
 *                    so as such, it may appear frequently within the servers
 *                    console.  The bottom line is that essentials still works
 *                    even through they are generating this stack trace (they want
 *                    to log a certain kind of condition) and prison still works.
 *                    It just produces a ton of confusing garbage.  It is unknown
 *                    when they will fix their code and remove it.
 * </p>
 * 
 * <p>View this issue 3956 with the comment title: "Problem: EssentialsX is flooding 
 * my console with UUID errors!"
 * https://github.com/EssentialsX/Essentials/issues/3956
 * </p>
 *
 * @author Faizaan A. Datoo
 */
class EssEconomyWrapper {

    double getBalance(Player player) {
        try {
            return Economy.getMoneyExact(player.getName()).doubleValue();
        } catch (UserDoesNotExistException e) {
            player.sendMessage("You don't exist in the economy plugin.");
            return 0.0;
        }
    }

    void setBalance(Player player, double amount) {
        try {
            Economy.setMoney(player.getName(), new BigDecimal(amount));
        } catch (UserDoesNotExistException | NoLoanPermittedException e) {
            player.sendMessage("You don't exist in the economy plugin.");
        }
    }

}
