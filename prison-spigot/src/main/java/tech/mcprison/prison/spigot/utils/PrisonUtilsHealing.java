package tech.mcprison.prison.spigot.utils;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public class PrisonUtilsHealing
        extends PrisonUtils
{
    //Add freeze heal
    private boolean enableHealingHeal = false;
    private boolean enableHealingFeed = false;
    private boolean enableHealingBreath = false;

    public PrisonUtilsHealing() {
        super();
    }
    /**
     * <p>There is no initialization needed for these commands.
     * <p>
     *
     * <p>This function must return a value of true to indicate that this
     * set of commands are enabled.  If it is set to false, then these
     * commands will not be registered when prison is loaded.
     * </p>
     *
     * @return
     */
    @Override
    protected Boolean initialize()
    {
        return true;
    }

    @Command(identifier = "prison utils heal",
            description = "Heals a player to full health",
            onlyPlayers = false,
            permissions = "prison.utils.healing.heal",
            altPermissions = "prison.utils.healing.heal.others")
    public void utilHealingHeal(CommandSender sender,
                                @Arg(name = "playerName", description = "Player Name") String playerName,
                                @Arg(name = "amount", description = "amount of air given") String amount
            ){

        if( !enableHealingHeal ){
            Output.get().logInfo("Prison's utils command heal is disabled in modules.yml.");
        } else {
            SpigotPlayer player = checkPlayerPerms( sender, playerName,
                    "prison.utils.healing.heal",
                    "prison.utils.healing.heal.others" );

            utilHealingHeal( player, amount );
        }
    }

    @Command(identifier = "prison utils feed",
            description = "Feeds a player to full",
            onlyPlayers = false,
            permissions = "prison.utils.healing.feed",
            altPermissions = "prison.utils.healing.feed.others")
    public void utilHealingFeed(CommandSender sender,
                                @Arg(name = "playerName", description = "Player Name") String playerName,
                                @Arg(name = "amount", description = "amount of food given") String amount
    ){

        if( !enableHealingFeed ){
            Output.get().logInfo("Prison's utils command feed is disabled in modules.yml.");
        } else {
            SpigotPlayer player = checkPlayerPerms( sender, playerName,
                    "prison.utils.healing.feed",
                    "prison.utils.healing.feed.others" );

            utilHealingFeed( player, amount );
        }
    }

    @Command(identifier = "prison utils breath",
            description = "Gives a player air while underwater",
            onlyPlayers = false,
            permissions = "prison.utils.healing.breath",
            altPermissions = "prison.utils.healing.breath.others")
    public void utilHealingBreath(CommandSender sender,
                                @Arg(name = "playerName", description = "Player Name") String playerName,
                                @Arg(name = "amount", description = "amount of air given") String amount
    ){

        if( !enableHealingBreath ){
            Output.get().logInfo("Prison's utils command breath is disabled in modules.yml.");
        } else {
            SpigotPlayer player = checkPlayerPerms( sender, playerName,
                    "prison.utils.healing.breath",
                    "prison.utils.healing.breath.others" );

            utilHealingBreath( player, amount );
        }
    }

    private void utilHealingHeal( SpigotPlayer player, String amount ) {
        if(player == null){
            return;
        }

        double maxHealth = player.getMaxHealth();

        if (maxHealth != 0) {
            double newHealth = player.getWrapper().getHealth() + parseInt(amount);
            double health = amount == null ? maxHealth :
                            newHealth < 0 ? 0 :
                            Math.min(newHealth, maxHealth);

            player.getWrapper().setHealth(health);
        }
    }

    private void utilHealingFeed( SpigotPlayer player, String amount ) {
        if(player == null){
            return;
        }

        int newFood = player.getWrapper().getFoodLevel() + parseInt(amount);
        int food = amount == null ? 20 :
                   newFood < 0 ? 0 :
                   Math.min(newFood, 20);

        player.getWrapper().setFoodLevel(food);
    }

    private void utilHealingBreath( SpigotPlayer player, String amount ) {
        if( player == null ) {
            return;
        }

        int maxAir = player.getWrapper().getMaximumAir();

        int newAir = player.getWrapper().getRemainingAir() + parseInt(amount);
        int air = amount == null ? maxAir :
                  newAir < 0 ? 0 :
                  Math.min(newAir, maxAir);

        player.getWrapper().setRemainingAir(air);
    }

    public boolean isEnableHealingHeal() { return enableHealingHeal; }

    public boolean isEnableHealingFeed() { return enableHealingFeed; }

    public boolean isEnableHealingBreath() { return enableHealingBreath; }

    public void setEnableHealingFeed(boolean enableHealingFeed) {
        this.enableHealingFeed = enableHealingFeed;
    }

    public void setEnableHealingHeal(boolean enableHealingHeal) {
        this.enableHealingHeal = enableHealingHeal;
    }

    public void setEnableHealingBreath(boolean enableHealingBreath) {
        this.enableHealingBreath = enableHealingBreath;
    }
}