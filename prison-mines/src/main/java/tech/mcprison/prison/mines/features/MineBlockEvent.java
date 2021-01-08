package tech.mcprison.prison.mines.features;

import java.text.DecimalFormat;

public class MineBlockEvent {

	private double chance;
	private String permission;
	private String command;
	private TaskMode taskMode;
	
	private BlockEventType eventType;
	
	private String triggered;
	
	public enum BlockEventType {
		
		all,
		blockBreak,
		TEXplosion,
		
		eventTypeAll( all ),
		eventBlockBreak( blockBreak ),
		eventTEXplosion( TEXplosion );
		
		private final BlockEventType primaryEventType;
		
		private BlockEventType() {
			this.primaryEventType = null;
		}
		private BlockEventType( BlockEventType primaryEventType ) {
			this.primaryEventType = primaryEventType;
		}
		
		public static BlockEventType fromString( String type ) {
			BlockEventType results = eventTypeAll;
			
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
	}
	
	
	public enum TaskMode {
		inline, 
		inlinePlayer, 
		
		sync,
		syncPlayer;
		
		public static TaskMode fromString( String taskMode ) {
			TaskMode results = inline;
			
			if ( taskMode != null ) {
				
				for ( TaskMode mode : values() ) {
					if ( mode.name().equalsIgnoreCase( taskMode ) ) {
						results = mode;
						
						break;
					}
				}
			}
			
			return results;
		}
		
	}
	
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
	}
	
	public MineBlockEvent( double chance, String permission, 
			String command, TaskMode taskMode ) {
		this( chance, permission, command, taskMode, BlockEventType.all, null );
		
	}

	
	public String toSaveString() {
		DecimalFormat dFmt = new DecimalFormat("0.00000");
		return dFmt.format( getChance() ) + "|" + 
				(getPermission() == null || getPermission().trim().length() == 0 ? 
						"none" : getPermission())  + "|" + 
				getCommand() + "|" + getTaskMode() + "|" + getEventType().name() + "|" + 
				(getTriggered() == null ? "none" : getTriggered());
	}
	
	
	public static MineBlockEvent fromSaveString( String blockEventString ) {
		MineBlockEvent results = null;

		if ( blockEventString != null && !blockEventString.startsWith( "ver" ) ) {
			
			results = fromStringV1( blockEventString );
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
	
	
	private static MineBlockEvent fromStringV1( String chancePermCommand ) {
		MineBlockEvent results = null;
		
		if ( chancePermCommand != null && chancePermCommand.trim().length() > 0 ) {
			String[] cpc = chancePermCommand.split( "\\|" );
			
			double  chance = cpc.length >= 1 ? Double.parseDouble( cpc[0] ) : 0d;
			
			String permission = cpc.length >= 2 ? cpc[1] : "";
			if ( permission == null || "none".equalsIgnoreCase( permission) ) {
				permission = "";
			}
			
			String command = cpc.length >= 3 ? cpc[2] : "";
			
			String mode = cpc.length >= 4 ? cpc[3] : "inline";
			TaskMode taskMode = TaskMode.fromString( mode ); // defaults to inline
			
			BlockEventType eventType = cpc.length >= 5 ? BlockEventType.fromString( cpc[4] ) :
				BlockEventType.eventTypeAll;
			
			String triggered = cpc.length >= 6 && !"none".equals(cpc[5]) ? cpc[5] : null;
			
			
			if ( command != null && command.trim().length() > 0 ) {
				
				results = new MineBlockEvent( chance, permission, command, taskMode, eventType, triggered );
			}
		}
		
		return results;
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


//	public boolean isInline() {
//		return "inline".equalsIgnoreCase( getMode() );
//	}
//	public boolean isSync() {
//		return "sync".equalsIgnoreCase( getMode() );
//	}

	
}
