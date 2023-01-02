package tech.mcprison.prison.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.CommandSender;

public class TabCompleaterData
{
	private String name;
	private TreeMap<String, TabCompleaterData> data;
	
	private boolean leafNode;
	
	private RegisteredCommand registeredCommand;
	
	public TabCompleaterData() {
		this( "", null );
		
		this.registeredCommand = null;
		
		setLeafNode( false );
	}
	private TabCompleaterData( String name, String[] args ) {
		super();
		
		this.name = name;
		this.data = new TreeMap<>();
		
		this.leafNode = ( args == null || args.length == 0 );
		
		this.registeredCommand = null;

		// Cannot build out child nodes here... has to be done within the
		// addCommand() function.
//		if ( !this.leafNode ) {
//			add( args );
//		}
	}

	/**
	 * <p>This must only be used internally to this class to ensure that the
	 * structure will be built correctly.
	 * </p>
	 * 
	 * <p>This will add the given usage String array to the current TabCompleterData
	 * object, creating a new node for ever array element in usage, or add to 
	 * any existing one that is found.
	 * </p>
	 * 
	 * <p>If this function is to be called iteratively, then the first element of
	 * usage must be removed so it does not go in to an endless loop. The 
	 * size of usage must decrease by one every time it is called.
	 * </p>
	 * 
	 * @param usage
	 */
	private void addCommand( RegisteredCommand registeredCommand, String... usage ) {
		
		if ( usage.length > 0 ) {
			String name = usage[0];
			String key = name.toLowerCase();
			
			
			String[] subArray = Arrays.copyOfRange( usage, 1, usage.length );
			
			// If getData() does not contain a key, then add it before processing it:
			if ( !getData().containsKey( key ) ) {
				TabCompleaterData tcd = new TabCompleaterData( name, subArray );
				getData().put( key, tcd );
			}
			
//			else {
//				TabCompleaterData tcd = getData().get( key );
//				tcd.addCommand( registeredCommand, subArray );
//			}
			
			TabCompleaterData tcd = getData().get( key );
			tcd.addCommand( registeredCommand, subArray );

			boolean storeRegisteredCommand = usage.length == 1;
			if ( storeRegisteredCommand ) {
				tcd.setRegisteredCommand( registeredCommand );
			}
			
		}
	

	}
	
	/**
	 * <p>Every time a RegisteredCommand is added to the <b>allRegisteredCommands</b>
	 * collection, it should also be added here too.  This function will take the data
	 * that is within it and parse it out and store it in a hierarchical structure
	 * using b-trees.
	 * </p>
	 * 
	 * <p>Do not indirectly add any aliases to this function.  Through the full and 
	 * normal process, aliases will be assigned their own RegisteredCommand objects
	 * so you never have to artificially feed them to this function.
	 * </p>
	 * 
	 */
	public void add( RegisteredCommand registeredCommand ) {
		String usageStr = registeredCommand.getUsage().replace( "/", "" );
		
		String[] usage = usageStr.split( " " );
		
		addCommand( registeredCommand, usage );
		
//		if ( usage.length > 0 ) {
//			String key = usage[0];
//			
//			String[] subArray = Arrays.copyOfRange( usage, 1, usage.length );
//			
//			if ( !getData().containsKey( key ) ) {
//				TabCompleterData tcd = new TabCompleterData( key, subArray );
//				getData().put( key, tcd );
//			}
//			else {
//				getData().get( key ).add( subArray );
//			}
//		}
		
	}
	
	/**
	 * <p>This function is to be used within the org.bukkit.command.Command.tabComplete()
	 * function to add lookup auto complete items to return.  The alias and args should
	 * be unmodified from what bukkit supplies.  This function will iteratively traverse
	 * all nodes and return the results.  This should provide the best performance.
	 * </p>
	 *  
	 * @param alias
	 * @param args
	 * @return
	 */
	public List<String> check( String alias, String... args ) {
		return check( null, alias, args );
	}
		
	public List<String> check( CommandSender commandSender, String alias, String... args ) {
		List<String> results = new ArrayList<>();
		
		if ( alias != null ) {
			if ( getData().containsKey( alias ) ) {
				results.addAll( getData().get( alias.toLowerCase() ).checkLabel( commandSender, alias, args ) );
			}
			
		}
		
		
		return results;
	}
	
	
	private List<String> checkLabel( CommandSender commandSender, String label, String... args ) {
		List<String> results = new ArrayList<>();
		
		String newLabel = label + " ";
		
		if ( args == null || args.length == 0 || 
				args.length == 1 && args[0].length() == 0 ) {
			
			// usage length of zero means return all children for this node.
			// Must get each child's name:
			Set<String> keys = getData().keySet();
			for ( String key : keys ) {
				
				TabCompleaterData tabComplete = getData().get( key );

				if ( tabComplete.validateAccess( commandSender, newLabel + key, "" ) ) {
					
					results.add( getData().get( key ).getName() );
				}
				
			}

		}
		else if ( args.length > 1  
//				|| args.length == 1 && getData().containsKey( args[0] ) 
				) {
			
			// if length is greater than 1 then that means that we need to 
			// traverse to the next level of depth if we have a hit for
			// one of the data elements:
			
			String key = args[0].toLowerCase();
			
			if ( getData().containsKey( key )) {
				
				String[] subArray = Arrays.copyOfRange( args, 1, args.length );

				TabCompleaterData tabComplete = getData().get( key );

				results.addAll( tabComplete.checkLabel( commandSender, newLabel + key, subArray ) );
			}
		}
		else {
			// args length is one.  See if anything either matches it, or
			// begins with it.
			
			String prefix = args[0].toLowerCase();
			
			Set<String> keys = getData().keySet();
			for ( String key : keys ) {
				if ( key.startsWith( prefix )) {
					
					TabCompleaterData tabComplete = getData().get( key );
					
					String[] subArray = Arrays.copyOfRange( args, 1, args.length );
					if ( tabComplete.validateAccess( commandSender, newLabel + key, subArray ) ) {
						
						results.add( getData().get( key ).getName() );
					}
					
				}
			}
		}
	
		return results;

	}
	
	private boolean validateAccess( CommandSender commandSender, String label, String... args ) {
		boolean hasAccess = true;

		// Unit tests will have null for commandSender so skip the command access validation:
		if ( commandSender != null && !commandSender.isOp() ) {
			
			hasAccess = Prison.get().getCommandHandler()
							.checkCommand(commandSender, getRegisteredCommand(), label, args );
		}
		
		return hasAccess;
	}

	
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}

	public TreeMap<String, TabCompleaterData> getData() {
		return data;
	}
	public void setData( TreeMap<String, TabCompleaterData> data ) {
		this.data = data;
	}

	public boolean isLeafNode() {
		return leafNode;
	}
	public void setLeafNode( boolean leafNode ) {
		this.leafNode = leafNode;
	}
	
	public RegisteredCommand getRegisteredCommand() {
		return registeredCommand;
	}
	public void setRegisteredCommand(RegisteredCommand registeredCommand) {
		this.registeredCommand = registeredCommand;
	}
	
}
