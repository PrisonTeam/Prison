package tech.mcprison.prison.mines.features;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonCommandTask.TaskMode;

public class MineBlockEvent {

	private double chance;
	private String permission;
	private String command;
	private TaskMode taskMode;
	
	private BlockEventType eventType;
	
	private String triggered;
	
	private Set<PrisonBlock> prisonBlocks;
	
	public enum BlockEventType {
		
		all,
		blockBreak,
		TEXplosion,
		CEXplosion,
		PEExplosive, // PrisonEnchant: Pulsi_'s plugin
		PrisonExplosion,
		
		eventTypeAll( all ),
		eventBlockBreak( blockBreak ),
		eventTEXplosion( TEXplosion )
		;
		
		private final BlockEventType primaryEventType;
		
		private BlockEventType() {
			this.primaryEventType = null;
		}
		private BlockEventType( BlockEventType primaryEventType ) {
			this.primaryEventType = primaryEventType;
		}
		
		public static BlockEventType fromString( String type ) {
			BlockEventType results = all;
			
			for ( BlockEventType eType : values() ) {
				if ( eType.name().equalsIgnoreCase( type ) ) {
					
					if ( eType.getPrimaryEventType() != null ) {
						results = eType.getPrimaryEventType();
					}
					else {
						results = eType;
					}
					
					break;
				}
			}
			
			return results;
		}
		
		public BlockEventType getPrimaryEventType() {
			return primaryEventType;
		}
		
		public static String getPrimaryEventTypes() {
			StringBuilder sb = new StringBuilder();
			
			for ( BlockEventType eType : values() ) {
				if ( eType.getPrimaryEventType() != null ) {
					if ( sb.length() > 0 ) {
						sb.append( " " );
					}
					
					sb.append( eType.name() );
				}
			}

			return sb.toString();
		}
	}
	
	
//	public enum TaskMode {
//		inline, 
//		inlinePlayer, 
//		
//		sync,
//		syncPlayer;
//		
//		public static TaskMode fromString( String taskMode ) {
//			TaskMode results = inline;
//			
//			if ( taskMode != null ) {
//				
//				for ( TaskMode mode : values() ) {
//					if ( mode.name().equalsIgnoreCase( taskMode ) ) {
//						results = mode;
//						
//						break;
//					}
//				}
//			}
//			
//			return results;
//		}
//		
//	}
	
	public MineBlockEvent( double chance, String permission, 
								String command, TaskMode taskMode, 
								BlockEventType eventType, String triggered ) {
		super();
		
		this.chance = chance;
		this.permission = permission;
		this.command = command;
		this.taskMode = taskMode;
		
		this.eventType = eventType;
		
		this.triggered = null;
		
		this.prisonBlocks = new TreeSet<>();
	}
	
	public MineBlockEvent( double chance, String permission, 
			String command, TaskMode taskMode ) {
		this( chance, permission, command, taskMode, BlockEventType.all, null );
		
	}

	
	public String toSaveString() {
		
		NumberFormat nFmt = DecimalFormat.getInstance( Locale.US );
		if ( nFmt instanceof DecimalFormat ) {
			DecimalFormat df = (DecimalFormat) nFmt;
			df.applyLocalizedPattern( "0.00000" );
		}
		nFmt.format( getChance() );
		
//		DecimalFormat dFmt = new DecimalFormat("0.00000");
		
		return nFmt.format( getChance() ) + "|" + 
				(getPermission() == null || getPermission().trim().length() == 0 ? 
						"none" : getPermission())  + "|" + 
				getCommand() + "|" + getTaskMode().name() + "|" + getEventType().name() + "|" + 
				(getTriggered() == null ? "none" : getTriggered()) + "|" +
				getPrisonBlockStrings( "," );
	}
	
	
	public static MineBlockEvent fromSaveString( String blockEventString, String mineName ) {
		MineBlockEvent results = null;

		if ( blockEventString != null && !blockEventString.startsWith( "ver" ) ) {
			
			results = fromStringV1( blockEventString, mineName );
		}
		
//		if ( chancePermCommand != null && chancePermCommand.trim().length() > 0 ) {
//			String[] cpc = chancePermCommand.split( "\\|" );
//			
//			double  chance = cpc.length >= 1 ? Double.parseDouble( cpc[0] ) : 0d;
//			
//			String permission = cpc.length >= 2 ? cpc[1] : "";
//			if ( permission == null || "none".equalsIgnoreCase( permission) ) {
//				permission = "";
//			}
//			
//			String command = cpc.length >= 3 ? cpc[2] : "";
//
//			String mode = cpc.length >= 4 ? cpc[3] : "inline";
//			
//			if ( !"sync".equalsIgnoreCase( mode ) && !"inline".equalsIgnoreCase( mode ) ) {
//				mode = "sync";
//			}
////			boolean async = (asyncStr != null && 
////											"true".equalsIgnoreCase( asyncStr ) );
//			
//			BlockEventType eventType = cpc.length >= 5 ? BlockEventType.fromString( cpc[4] ) :
//											BlockEventType.eventTypeAll;
//			
//			String triggered = cpc.length >= 6 && !"none".equals(cpc[5]) ? cpc[5] : null;
//			
//			
//			if ( command != null && command.trim().length() > 0 ) {
//				
//				results = new MineBlockEvent( chance, permission, command, mode, eventType, triggered );
//			}
//		}
		
		return results;
	}
	
	
	private static MineBlockEvent fromStringV1( String chancePermCommand, String mineName ) {
		MineBlockEvent results = null;
		
		if ( chancePermCommand != null && chancePermCommand.trim().length() > 0 ) {
			String[] cpc = chancePermCommand.split( "\\|" );
			
			
			double  chance = 0.00001;
			try {
				String dbl = cpc.length >= 1 ? cpc[0] : "0";
				
				NumberFormat nFmt = DecimalFormat.getInstance( Locale.US );
				if ( nFmt instanceof DecimalFormat ) {
					DecimalFormat df = (DecimalFormat) nFmt;
					df.applyLocalizedPattern( "0.00000" );
				}
				Number nDbl = nFmt.parse( dbl );
				if ( nDbl != null ) {
					chance = nDbl.doubleValue();
				}
				
//				chance = cpc.length >= 1 ? Double.parseDouble( cpc[0] ) : 0d;
			}
			catch ( ParseException | NumberFormatException e ) {
				Output.get().logError( "Failure parsing a mine " + mineName + 
						"'s blockEven percent chance. " +
						"It is not a double number. Please use the command " +
						"'/mines blockEvent add help' when creating blockEvents instead of " +
						"manually editing the files. The incorrect value was [" + cpc[0] + 
						"].  Using a value of [0.00001] instead.");
			}
			
			String permission = cpc.length >= 2 ? cpc[1] : "";
			if ( permission == null || "none".equalsIgnoreCase( permission) ) {
				permission = "";
			}
			
			String command = cpc.length >= 3 ? cpc[2] : "";
			
			String mode = cpc.length >= 4 ? cpc[3] : "inline";
			TaskMode taskMode = TaskMode.fromString( mode ); // defaults to inline
			
			BlockEventType eventType = cpc.length >= 5 ? BlockEventType.fromString( cpc[4] ) :
				BlockEventType.all;
			
			String triggered = cpc.length >= 6 && !"none".equals(cpc[5]) ? cpc[5] : null;
			
			
			String blocks = cpc.length >= 7 && !"".equalsIgnoreCase( cpc[6] ) ? cpc[6] : "";
			
			
			if ( command != null && command.trim().length() > 0 ) {
				
				results = new MineBlockEvent( chance, permission, command, taskMode, eventType, triggered );
				
				PrisonBlockTypes pBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
				
				for ( String block : blocks.toLowerCase().split( "," ) ) {
					PrisonBlock blockType = pBlockTypes.getBlockTypesByName().get( block );
					if ( blockType != null ) {
						results.getPrisonBlocks().add( blockType );
					}
				}
			}
		}
		
		return results;
	}
	
	/**
	 * Adds a prisonBlock filter if it does not already exist.
	 * 
	 * @param block
	 */
	public void addPrisonBlock( PrisonBlock block ) {
		
		if ( !getPrisonBlocks().contains( block ) ) {
			getPrisonBlocks().add( block );
		}
	}


	/**
	 * This will remove a specific block based upon row number.
	 * 
	 * @param rowBlockName
	 * @return if successfully removed
	 */
	public boolean removePrisonBlock( Integer rowBlockName ) {
		boolean results = false;
		
		if ( rowBlockName != null && rowBlockName > 0 && getPrisonBlocks().size() <= rowBlockName ) {
			
			PrisonBlock targetBlock = null;
			
			int count = 0;
			for ( PrisonBlock prisonBlock : getPrisonBlocks() ) {
				if ( ++count == rowBlockName.intValue() ) {
					targetBlock = prisonBlock;
					break;
				}
			}

			if ( targetBlock != null ) {
				
				results = getPrisonBlocks().remove( targetBlock );
			}
		}
		
		return results;
	}
	 
	
	/**
	 * <p>This simple function checks to see if the chance applies to this event.
	 * If it does, then it checks to ensure if the eventType matches the targeted
	 * even type that this BlockEvent is targeting.  
	 * </p>
	 * 
	 * <p><b>NOTE:</b> This does not check the player's permissions.  That
	 * must be done outside of this class.
	 * </p>
	 * 
	 * 
	 * @param eventType
	 * @param chance
	 * @param blockName
	 * @param triggered
	 * @return
	 */
	public boolean isFireEvent( double chance, BlockEventType eventType, 
			MineTargetPrisonBlock targetBlock, String triggered ) {
		boolean results = false;
		
		// First check chance, since that's perhaps the quickest check:
		if ( chance <= getChance() &&
				
				isValidBlock( targetBlock ) &&
				
				// Make sure we have the correct eventTypes:
			(eventType == BlockEventType.TEXplosion && 
				eventType == getEventType() && 
					( getTriggered() == null || 
						getTriggered().equalsIgnoreCase( triggered )) ||
					
				getEventType() == BlockEventType.all || 
				getEventType() == eventType) ) {
			
			// The check for the player's perms will have to be done outside of this 
			// function.
			
			results = true;
		}
		
		
		return results;
	}
	
	
	private boolean isValidBlock( MineTargetPrisonBlock targetBlock) {

		// If no prisonBlocks have been setup, return true:
		return getPrisonBlocks().size() == 0 || hasBlockType( targetBlock );
	}
	
	private boolean hasBlockType( MineTargetPrisonBlock targetBlock ) {
//		PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
//    	PrisonBlock block = prisonBlockTypes.getBlockTypesByName( blockName );
    	
		return getPrisonBlocks().contains( targetBlock.getPrisonBlock() );
	}

	public double getChance() {
		return chance;
	}
	public void setChance( double chance ) {
		this.chance = chance;
	}

	public String getPermission() {
		return permission;
	}
	public void setPermission( String permission ) {
		this.permission = permission;
	}

	public String getCommand() {
		return command;
	}
	public void setCommand( String command ) {
		this.command = command;
	}

	public TaskMode getTaskMode() {
		return taskMode;
	}
	public void setTaskMode( TaskMode taskMode ) {
		this.taskMode = taskMode;
	}

	public BlockEventType getEventType() {
		return eventType;
	}
	public void setEventType( BlockEventType eventType ) {
		this.eventType = eventType;
	}

	public String getTriggered() {
		return triggered;
	}
	public void setTriggered( String triggered ) {
		this.triggered = triggered;
	}

	public String getPrisonBlockStrings() {
		return getPrisonBlockStrings( " " );
	}
	
	public String getPrisonBlockStrings( String deliminator ) {
		StringBuilder sb = new StringBuilder();
		
		for ( PrisonBlock block : getPrisonBlocks() ) {
			if ( sb.length() > 0 ) {
				sb.append( deliminator );
			}
			sb.append( block.getBlockName() );
		}

		return sb.toString().trim();
	}
	public Set<PrisonBlock> getPrisonBlocks() {
		return prisonBlocks;
	}
	public void setPrisonBlocks( Set<PrisonBlock> prisonBlocks ) {
		this.prisonBlocks = prisonBlocks;
	}



//	public boolean isInline() {
//		return "inline".equalsIgnoreCase( getMode() );
//	}
//	public boolean isSync() {
//		return "sync".equalsIgnoreCase( getMode() );
//	}

	
}
