package tech.mcprison.prison.spigot.utils;

import org.bukkit.Bukkit;
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

    public enum HealingOptions {

        heal,
        feed,
        breath,
        ;

        public static PrisonUtilsHealing.HealingOptions fromString(String option ) {
            PrisonUtilsHealing.HealingOptions results = null;

            for ( PrisonUtilsHealing.HealingOptions rOp : values() )
            {
                if ( rOp.name().equalsIgnoreCase( option ) ) {
                    results = rOp;
                    break;
                }
            }

            return results;
        }
    }

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
    public void utilHealingheal(CommandSender sender,
            @Arg(name = "playerName", description = "Player Name") String playerName
            /*
            @Wildcard(join=true)
            @Arg(name = "options", description = "Options [player, all]",
                 def = "") String options
                 */
            ){

        if( !enableHealingHeal ){
            Output.get().logInfo("Prison's utils command healingHeal is disabled in modules.yml.");
        } else {
            SpigotPlayer player = checkPlayerPerms( sender, playerName,
                    "prison.utils.healing.heal",
                    "prison.utils.healing.heal.others" );

            // Player cannot be null.  If it is null, then there was a failure.
            if ( player != null ) {

                utilHealingHeal( player, playerName );
            }
        }
    }

    private void utilHealingHeal( SpigotPlayer player, String playerName ) {
        player.setHealth(20);
    }

    private void utilHealingFeed( SpigotPlayer player, String playerName ) {

    }

    private void utilHealingBreath( SpigotPlayer player, String playerName ) {

    }

    public boolean isEnableHealingHeal(){
        return enableHealingHeal;
    }

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
