package tech.mcprison.prison.spigot.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import tech.mcprison.prison.spigot.SpigotPrison;

public class BackpackEvent
		extends Event
		implements Cancellable {

	private Player player;
	
	private BackpackCallback callback;
	
	private boolean cancel;

	private final List<Inventory> inventory;
	
	private BackpackAction action;
	
	private BackpackResults results;
	
	/**
	 * <p>These backpack actions just represents the Integration's backpack
	 * function that was called.  You may have special processing you would 
	 * like to do before or after handling these events. It's optional.
	 * </p>
	 * 
	 * <ul>
	 * 
	 * </ul>
	 */
	public enum BackpackAction {
		undefined,
		addItems,
		smeltItems,
		removeAll,
		sellItems
	}
	
	/**
	 * <p>These are the backpack transaction results which would only be
	 * valid after the events are fired and the results are being processed
	 * in the BackpackCallback function.
	 * </p>
	 * 
	 * <ul>
	 * 		<li><b>undefined</b> : The status before the backpack inventories are processed.</li>
	 * 		<li><b>noChange</b> : The inventory has not been changed.</li>
	 * 		<li><b>contentsChanged</b> : The inventory contents has been changed.</li>
	 * </ul>
	 */
	public enum BackpackResults {
		undefined, // The status before the backpack inventories are processed
		noChange,
		contentsChanged
	}
	
	public interface BackpackCallback {
		public void run();
	}
	
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

	public BackpackEvent() {
		super();
		
		this.player = null;
		this.cancel = false;
		
		this.callback = null;
		
		this.inventory = new ArrayList<>();
		
		this.action = BackpackAction.undefined;

		this.results = BackpackResults.undefined;
	}
	
	public BackpackEvent( Player player ) {
		this();
		
		this.player = player;
	}
	
	
	/**
	 * <p>This is an example of how to fire this event for a player's backpack 
	 * to take action.  This function is flawed because you would need to use
	 * and access contents of the BackpackEvent, which this function will not
	 * return.  So do not use this static function, but use the 4 lines of code
	 * as an example in your code.
	 * </p>
	 * 
	 * <p>Generally this would not be used in your plugin, but this is how prison
	 * would fire the event internally.  Your plugin could use it too, if it 
	 * would cause the player's inventory to be full.
	 * </p>
	 * 
	 * <p>Each time an event is fired, a new instance of the event is created
	 * so bukkit can pass that instance to all registered listeners.
	 * </p>
	 * 
	 * @param org.bukkit.entity.Player player
	 * @return boolean if the event was cancelled
	 */
	public static boolean fireBackpackEvent( Player player, BackpackAction action ) {
		
		BackpackEvent bpEvent = new BackpackEvent( player );
		bpEvent.setAction( action );
	
		Bukkit.getServer().getPluginManager().callEvent( bpEvent );
		
		if ( !bpEvent.fireBackpackEvent() ) {
			// You would handle your plugin's actions here if this was called 
			// in line within your code.
		}
		
		return bpEvent.isCancelled();
	}
	
	public boolean fireBackpackEvent() {
		
		Bukkit.getServer().getPluginManager().callEvent( this );
		
		return isCancelled();
	}
	
	
	/**
	 * This is an example of how you would setup a listener in your 
	 * plugin, and how you would use it.  It should generally be a class
	 * with a function of any name. The important key element is that it
	 * contains a parameter of class BackpackEvent.  Specifying an
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
	public class SampleBackpackListener
		implements Listener {
		
		public SampleBackpackListener() {
			super();
			
		}
		
		/**
		 * This is for demonstration only.  Do not use.
		 */
		public class DemoBackpack {
			public DemoBackpack() {
				super();
			}
			public Inventory getPlayerBackpack( Player player ) {
				return player.getInventory();
			}
			public void setChanged() {}
			public void save() {}
		}
		
		
		@EventHandler(priority=EventPriority.NORMAL)
		public void onBackpackEvent( BackpackEvent bpEvent ) {
			
			// If the event has been canceled, then do not process this event
			// since another plugin already processed it, or has denied that it
			// should be processed.
			if ( !bpEvent.isCancelled() ) {
				
				// Do something with this event here.  
				// You will have access to all of the functions in the event.
				Player player = bpEvent.getPlayer();
				
				// Pretend this represents a backpack object, whatever it may be.
				// Just using an Object since we don't have your code for your backpacks,
				// plus this is just an example. I've added dummy functions to this 
				// example backpack object so consider whatever you need to use 
				// with your own backpack.
				
				// Pretend we call our backpack function, passing the player to get
				// the backpack for that player.  In your code, the backpack object
				// probably has been already created.  For this demo, I'm just creating
				// this dummy object here:
				DemoBackpack backpack = new DemoBackpack();
				
				
				// Check to ensure the player has a valid backpack(s):
				if ( backpack != null ) {

					// This example is just getting the player's inventory, which is wrong,
					// for your code, return the inventory object from your backpack.
					Inventory inventory = backpack.getPlayerBackpack( player );
					
					// If you have more than one inventory object associated with your backpack,
					// then add them all to bpEvent.getInventory().
					if ( inventory != null ) {
						
						// Add your backpack's inventory list. If you have more than one, then
						// add them all.
						bpEvent.getInventory().add( inventory );
						
						
						// Then setup your callback, which will run whatever code you need to use
						// to process the backpack's action that was performed:
						BackpackCallback callback = new BackpackCallback() {
							public void run() {
								
								
								switch ( bpEvent.getResults() ) {
								case contentsChanged:
									
									// Process whatever you need to do after prison handles the 
									// inventory transactions:
									backpack.setChanged();
									backpack.save();
									
									break;
									
								default:
									break;
								}
								
								
							}
						};
						
						bpEvent.setCallback( callback );
						
						
						boolean cancel = false;
						
						if ( cancel ) {
							
							// Canceling the event will prevent prison from process the backpack action:
							bpEvent.setCancelled( true );
						}
						// etc...
					}
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
			
			Plugin prison = SpigotPrison.getInstance(); // your plug. Don't use prison.
			PluginManager pm = Bukkit.getServer().getPluginManager();
			
			SampleBackpackListener sbpListener = new SampleBackpackListener();
			
			pm.registerEvents( sbpListener, prison );
			
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
			
			pm.registerEvent( BackpackEvent.class, sbpListener, ePriority,
					new EventExecutor() {
						public void execute( Listener l, Event e ) { 
							
							BackpackEvent bpEvent = (BackpackEvent) e;
							
							((SampleBackpackListener)l)
									.onBackpackEvent( bpEvent );
						}
					},
					prison );
		}
	}
	
	
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public Player getPlayer() {
		return player;
	}

	public List<Inventory> getInventory() {
		return inventory;
	}

	public BackpackCallback getCallback() {
		return callback;
	}
	public void setCallback(BackpackCallback callback) {
		this.callback = callback;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	public HandlerList getHandlers() {
		return handlers;
	}

	public BackpackAction getAction() {
		return action;
	}
	public void setAction(BackpackAction action) {
		this.action = action;
	}

	public BackpackResults getResults() {
		return results;
	}
	public void setResults(BackpackResults results) {
		this.results = results;
	}

}
