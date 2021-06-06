package tech.mcprison.prison.spigot.utils;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

import java.util.List;

public class PrisonUtilsHealing
        extends PrisonUtils
{
    //Add freeze heal
    private boolean enableHealingHeal = false;
    private boolean enableHealFeed = false;

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

    @Command(identifier = "prison utils healPlayer",
            description = "Heals a player to full health",
            onlyPlayers = false,
            permissions = "prison.utils.healing.heal",
            altPermissions = "prison.utils.healing.heal.others")
    public void utilHealingheal(CommandSender sender,
            @Arg(name = "playerName", description = "Player Name") String playerName,

            @Wildcard(join=true)
            @Arg(name = "options", description = "Options [health, saturation, breath]",
                 def = "") String options ) {

        if( !enableHealingHeal ){
            Output.get().logInfo("Prison's utils command healingHeal is disabled in modules.yml.");
        } else {
            //List<PrisonUtilsRepair.RepairOptions> repairOptions = getOptions( PrisonUtilsRepair.RepairOptions.repairAll, options, playerName );


            SpigotPlayer player = checkPlayerPerms( sender, playerName,
                    "prison.utils.repair.all",
                    "prison.utils.repair.all.others" );

            // Player cannot be null.  If it is null, then there was a failure.
            if ( player != null ) {

                //utilRepair( player, playerName, repairOptions );
            }
        }
    }

    public boolean isEnableHealingHeal(){
        return enableHealingHeal;
    }

    public boolean isEnableHealFeed() {
        return enableHealFeed;
    }

    public void setEnableHealFeed(boolean enabled) {
        this.enableHealFeed = enableHealFeed;
    }

    public void setEnableHealingHeal(boolean enabled) {
        this.enableHealingHeal = enableHealingHeal;
    }
}
