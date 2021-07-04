package tech.mcprison.prison.spigot.tasks;

import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.PrisonSpigotAPI;
import tech.mcprison.prison.tasks.PrisonRunnable;

public class PrisonInitialStartupTask
	implements PrisonRunnable {
	
	public static final long INITIAL_SUBMISSION_DELALY = 20 * 5; // 5 seconds
	
	private SpigotPrison prison;
	
	public PrisonInitialStartupTask( SpigotPrison prison ) {
		super();
		
		this.prison = prison;
	}

	public static boolean isInitialStartup() {
		PrisonSpigotAPI prisonApi = new PrisonSpigotAPI();
		boolean firstStartup = 
						prisonApi.getMines().size() == 0 && 
						prisonApi.getRanks().size() == 0;
		
		return firstStartup;
	}
	
	public void submit() {
		
		prison.getScheduler().runTaskLater( this, INITIAL_SUBMISSION_DELALY );
	}
	
	@Override
	public void run() {

        ChatDisplay display = new ChatDisplay("Setting up a new Prison Server");
        
        display.addText("&aWelcome to &2Prison&a!");
        
        display.addText("");
        display.addText("To quickly get started, it is suggested to use the following command " +
        		"which will setup Ranks, Mines, link the Mines to the Ranks, setup automatic " +
        		"Access by Ranks, auto assign blocks to the generated Mines in increasing value," +
        		"Enable the auto features (auto pickup, smelt, and block), setup the sellall's " +
        		"default shop pricing on about 95 items, etc...");
        display.addText(". &7/ranks autoConfigure");
        display.addText("");
        display.addText("For more information on what to do next, after running autoConfigure, " +
        		"check out this document: ");
        display.addText(". &7 https://prisonteam.github.io/Prison/prison_docs_100_setting_up_auto_configure.html");
        display.addText("");
        display.addText("");
        display.addText("For more information on how to setup Prison, see our extensive " +
        		"documentation that is online:");
        display.addText(". &7 https://prisonteam.github.io/Prison/prison_docs_000_toc.html");
        display.addText("");
        display.addText("Information on suggested plugins can be found here:");
        display.addText(". &7 https://prisonteam.github.io/Prison/prison_docs_012_setting_up_prison_basics.html");
        display.addText("");
        display.addText("If you need help with setting up prison, please see our documentation.");
        display.addText("If you find an issue with Prison, or need help for things not in the documenation, " +
        		"then please visit our discord server:");
        display.addText("");
        display.addText("");
        
		
        display.sendtoOutputLogInfo();
	}
	
	
}
