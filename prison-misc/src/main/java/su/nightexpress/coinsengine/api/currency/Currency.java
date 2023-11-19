package su.nightexpress.coinsengine.api.currency;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Currency  {

    default boolean isUnlimited() {
        return true;
    }

    default boolean isLimited() {
        return false;
    }

    default boolean isInteger() {
        return false;
    }

    default double fine(double amount) {
        return 0d;
    }

    default double limit(double amount) {
        return 0d;
    }

    default double fineAndLimit(double amount) {
        return 0d;
    }

    @NotNull
    default String getPermission() {
        return "";
    }

    @NotNull
    default String formatValue(double balance) {
        return "";
    }

    @NotNull
    default String format(double balance) {
        return "";
    }

    @NotNull String getId();

    @NotNull String getName();

    //void setName(@NotNull String name);

    @NotNull String getSymbol();

    @NotNull String getFormat();

    //void setSymbol(@NotNull String symbol);

    @NotNull String[] getCommandAliases();

    @NotNull ItemStack getIcon();

    //void setCommandAliases(@NotNull String... aliases);

    boolean isDecimal();

    //void setDecimal(boolean decimal);

    boolean isPermissionRequired();

    //void setPermissionRequired(boolean permissionRequired);

    boolean isTransferAllowed();

    double getMinTransferAmount();

    //void setTransferAllowed(boolean transferAllowed);

    double getStartValue();

    //void setStartValue(double startValue);

    double getMaxValue();

    //void setMaxValue(double maxValue);

    boolean isVaultEconomy();
}
