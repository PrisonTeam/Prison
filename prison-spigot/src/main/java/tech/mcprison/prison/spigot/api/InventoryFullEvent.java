/**
 * 
 */
package tech.mcprison.prison.spigot.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * This event is used by plugins that want to get access to notifications when
 * prison detects a full inventory.
 * 
 * In general, this only currently supports a player's inventory, but if 
 * a backpack is also registered with backpack events, then this could be 
 * fired when a backpack is also full.
 */
public class InventoryFullEvent
	extends Event
	implements Cancellable {
	
	private final Player player;
	private boolean cancel;

	private final Inventory inventory;
	
	/**
	 * <p><b>Warning:</b> BlastUseEvent does not identify the block the player actually hit, so the dummyBlock
	 * is just a random first block from the explodedBlocks list and may not be the block
	 * that initiated the explosion event.  Such events are identified by 
	 * BlockEventType.CEXplosion.  Make sure you do not process the first block twice,
	 * which is also passed as the org.bukkit.block.Block and the SpigotBlock to prevent have to 
	 * pass nulls.  They are the exact same objects.  
	 * </p>
	 *
	 * Due to this behavior, since BlockBreakEvent's handlers is static, it 
	 * will "share" the handlers (listeners).  To prevent this unwanted behavior,
	 * since prison's BlockBreakEvent listeners will be called, this class
	 * defines and overrides the the handlers with it's own instance.
	 * 
	 */
	private static final HandlerList handlers = new HandlerList();

	public InventoryFullEvent() {
		super();
		
		this.player = null;
		this.cancel = false;
		this.inventory = null;
	}
	
	public InventoryFullEvent( Player player ) {
		super();
		
		this.player = player;
		this.cancel = false;
		
		this.inventory = player.getInventory();
	}
	
	public PlayerInventory getPlayerInventory() {
		PlayerInventory inv = null;
		
		if ( getInventory() != null && getInventory() instanceof PlayerInventory ) {
			inv = (PlayerInventory) getInventory();
		}
		
		return inv;
	}
	
	
	/**
	 * This is an example of how to fire this event for a player's inventory.
	 * 
	 * Generally this would not be used in your plugin, but this is how prison
	 * would fire the event internally.  Your plugin could use it too, if it 
	 * would cause the player's inventory to be full.
	 * 
	 * Each time an event is fired, a new instance of the event is created
	 * so bukkit can pass that instance to all registered listeners.
	 * 
	 * @param org.bukkit.entity.Player player
	 * @return boolean if the event was cancelled
	 */
	public static boolean fireInventoryFullEvent( Player player ) {
		boolean isCancelled = false;
		
		InventoryFullEvent ifEvent = new InventoryFullEvent( player );
	
		Bukkit.getServer().getPluginManager().callEvent( ifEvent );
		
		if ( !ifEvent.isCancelled() ) {
			// You would handle your plugin's actions here if this was called 
			// in line within your code.
		}
		
		return isCancelled;
	}
	
	
	
	/**
	 * This is an example of how you would setup a listener in your 
	 * plugin, and how you would use it.  It should generally be a class
	 * with a function of any name. The important key element is that it
	 * contains a parameter of class InventoryFullEvent.  Specifying an
	 * @EventHandler annotation is optional, but it's included here to 
	 * show you how you can use it to sent the event's priority.
	 * 
	 * Dynamically setting the event priority based upon configs is 
	 * beyond the purpose of this example.
	 * 
	 * Make sure within your function, you put your code that will react
	 * to when this event is fired.  You can refer to other classes 
	 * within your plugin and do whatever you need to do.
	 * 
	 * Your listener class must implement Listener.
	 * 
	 */
	public class SampleInventoryFullListener
		implements Listener {
		
		public SampleInventoryFullListener() {
			super();
			
		}
		
		@EventHandler(priority=EventPriority.NORMAL)
		public void onInventoryFullEvent( InventoryFullEvent ifEvent ) {
			
			// If the event has been canceled, then do not process this event
			// since another plugin already processed it, or has denyed that it
			// should be processed.
			if ( !ifEvent.isCancelled() ) {
				
				// Do something with this event here.  
				// You will have access to all of the functions in the event.
				if ( ifEvent.getPlayerInventory() != null ) {
					
					// A playerInventory was registered with this event:
					@SuppressWarnings("unused")
					PlayerInventory pi = ifEvent.getPlayerInventory();
					
					boolean success = true;
					
					if ( success ) {
						
						// If what we're doing in this example is successful and we need to
						// cancel the event:
						ifEvent.setCancelled( true );
					}
					// etc...
				}
			}
		}
		
		
		/**
		 * Somewhere in your plugin, you need to register your event listener with 
		 * the Bukkit PluginManager.  This is an example of how to do that.
		 * 
		 * This will register your listener with the  EventPriority of NORMAL if you
		 * have not used the @EventHandler annotation.
		 * 
		 * When prison fires the InventoryEvent, Bukkit will go through all registered
		 * event listeners and will call the registered function (the one with the parameter
		 * that includes InventoryFullEvent) so it can run all of your code that you 
		 * have placed in that function.
		 * 
		 * Please note that this function is placed in the inner class of 
		 * SampleInventoryFullListener only for demonstration purposes. You do not
		 * have to put this function, or the registration of your listener in 
		 * your listener class.  Generally, you may find it better suited to register
		 * all of your listeners in your primary plugin class.
		 * 
		 * As a bonus, I have included a secondary registration at a custom priority
		 * that is not set by an annotation, but could be set within a config file.
		 * 
		 */
		public void sampleUsageRegisterListenerEvent() {
			
			Plugin prison = SpigotPrison.getInstance();  // Your plugin. Don't use prison.
			PluginManager pm = Bukkit.getServer().getPluginManager();
			
			SampleInventoryFullListener sifListener = new SampleInventoryFullListener();
			
			pm.registerEvents(  sifListener, prison );
			
			// Hint: to dynamically control the listener's EventPriority you would use
			//       the other pm.registerEvents() functions that allows you to set them
			//       upon registration.
			
			// WARNING: Do not register both events at the same time, or they will be
			//          be called twice and could duplicate everything. The following is 
			//          included ONLY to show you how it can be done and is strictly 
			//          for demonstration purposes.  It's not needed if you can use
			//          the pm.registerEvents() function+.
			
			// The following shows how you can dynamically register your listener with
			// a dynamically set EventPriority as defined in a config file. Assume
			// that ePriority is based upon such a config that is requesting a LOW priority.
			EventPriority ePriority = EventPriority.LOW;
			
			pm.registerEvent(InventoryFullEvent.class, sifListener, ePriority,
					new EventExecutor() {
						public void execute(Listener l, Event e) { 
							
							InventoryFullEvent iffEvent = (InventoryFullEvent) e;
							
							((SampleInventoryFullListener)l)
									.onInventoryFullEvent( iffEvent );
						}
					},
					prison);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
}
