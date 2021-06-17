package tech.mcprison.prison.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

import java.util.ArrayList;
import java.util.List;

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
            @Arg(name = "playerName", description = "Player Name") String playerName
            /*
            @Wildcard(join=true)
            @Arg(name = "options", description = "Options [player, all]",
                 def = "") String options
                 */
            ){

        if( !enableHealingHeal ){
            Output.get().logInfo("Prison's utils command heal is disabled in modules.yml.");
        } else {
            SpigotPlayer player = checkPlayerPerms( sender, playerName,
                    "prison.utils.healing.heal",
                    "prison.utils.healing.heal.others" );

            // Player cannot be null.  If it is null, then there was a failure.
            utilHealingHeal( player, playerName );
        }
    }

    @Command(identifier = "prison utils feed",
            description = "Feeds a player to full",
            onlyPlayers = false,
            permissions = "prison.utils.healing.feed",
            altPermissions = "prison.utils.healing.feed.others")
    public void utilHealingFeed(CommandSender sender,
                                @Arg(name = "playerName", description = "Player Name") String playerName
            /*
            @Wildcard(join=true)
            @Arg(name = "options", description = "Options [player, all]",
                 def = "") String options
                 */
    ){

        if( !enableHealingFeed ){
            Output.get().logInfo("Prison's utils command feed is disabled in modules.yml.");
        } else {
            SpigotPlayer player = checkPlayerPerms( sender, playerName,
                    "prison.utils.healing.feed",
                    "prison.utils.healing.feed.others" );

            // Player cannot be null.  If it is null, then there was a failure.
            utilHealingFeed( player, playerName );
        }
    }

    @Command(identifier = "prison utils breath",
            description = "Gives a player air while underwater",
            onlyPlayers = false,
            permissions = "prison.utils.healing.breath",
            altPermissions = "prison.utils.healing.breath.others")
    public void utilHealingBreath(CommandSender sender,
                                @Arg(name = "playerName", description = "Player Name") String playerName
            /*
            @Wildcard(join=true)
            @Arg(name = "options", description = "Options [player, all]",
                 def = "") String options
                 */
    ){

        if( !enableHealingBreath ){
            Output.get().logInfo("Prison's utils command breath is disabled in modules.yml.");
        } else {
            SpigotPlayer player = checkPlayerPerms( sender, playerName,
                    "prison.utils.healing.breath",
                    "prison.utils.healing.breath.others" );

            // Player cannot be null.  If it is null, then there was a failure.
            utilHealingBreath( player, playerName );
        }
    }

    private void utilHealingHeal( SpigotPlayer player, String playerName ) {
        if( player == null || Bukkit.getPlayer(playerName) == null ) return;
        double maxHealth = player.getMaxHealth();

        if( maxHealth == 0 ) return;

        player.getWrapper().setHealth(maxHealth);
    }

    private void utilHealingFeed( SpigotPlayer player, String playerName ) {
        if( player == null || Bukkit.getPlayer(playerName) == null ) return;
        player.getWrapper().setFoodLevel(20);
    }

    private void utilHealingBreath( SpigotPlayer player, String playerName ) {
        if( player == null || Bukkit.getPlayer(playerName) == null ) return;
        player.getWrapper().setRemainingAir(player.getWrapper().getMaximumAir());
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