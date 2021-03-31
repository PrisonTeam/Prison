package tech.mcprison.prison.spigot.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.Module;

public class PrisonUtilsModule
	extends Module 
{
	public static final String MODULE_NAME = "Utils";
	
	private YamlConfiguration modulesConf;

    
    public PrisonUtilsModule(String version, YamlConfiguration modulesConf) {
        super(MODULE_NAME, version, 3);

        this.modulesConf = modulesConf;
    }


	@Override
	public String getBaseCommands() {
		return "/prison utils";
	}

	@Override
	public void enable() {
			
		if ( isEnabled( "utils.enabled", true ) ) {
			
			if ( isEnabled( "utils.repair.enabled", true ) ) {
			
				PrisonUtilsRepair utils = new PrisonUtilsRepair();
				
				utils.setEnableRepairAll( isEnabled( "utils.repair.repairAll", true ) );
				utils.setEnableRepairHand( isEnabled( "utils.repair.repairHand", true ) );
				
				Prison.get().getCommandHandler().registerCommands( utils );

			}
			
			if ( isEnabled( "utils.messages.enabled", true ) ) {
				
				PrisonUtilsMessages utils = new PrisonUtilsMessages();
				
				utils.setEnableMessageMsg( isEnabled( "utils.messages.msg", true ) );
				utils.setEnableMessageBroadcast( isEnabled( "utils.messages.broadcast", true ) );
				
				Prison.get().getCommandHandler().registerCommands( utils );
				
			}
			
			if ( isEnabled( "utils.mining.enabled", true ) ) {
				
				PrisonUtilsMining utils = new PrisonUtilsMining();
				
				utils.setEnableMiningSmelt( isEnabled( "utils.mining.smelt", true ) );
				utils.setEnableMiningBlock( isEnabled( "utils.mining.block", true ) );
				
				Prison.get().getCommandHandler().registerCommands( utils );
				
			}
			
			
			
		}
		
		if ( isEnabled( "utils.potions.enabled", true ) ) {
			
			PrisonUtilsPotions utils = new PrisonUtilsPotions();
			
			utils.setEnablePotionEffects( isEnabled( "utils.potions.potionEffects.enabled", true ) );
			// utils.setEnablePotions( isEnabled( "utils.potions.potions.enabled", true ) );
			
			Prison.get().getCommandHandler().registerCommands( utils );
			
		}
		
	}

	@Override
	public void deferredStartup() {
		
	}

	@Override
	public void disable() {
		
	}


	public YamlConfiguration getModulesConf() {
		return modulesConf;
	}
	
	private boolean isEnabled( String configPath, boolean defaultValue ) {
		return getModulesConf().getBoolean( configPath, defaultValue );
	}
}
