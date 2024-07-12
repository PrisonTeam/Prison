package tech.mcprison.prison.tasks;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.RankLadder;

public class PrisonCommandTaskData {
	
	private RankLadder ladder;
	private PlayerRank rankTarget;
	private PlayerRank rankOriginal;
	
	private int commandRow = 0;
	private String cmd;
	
	private TaskMode taskMode;
	
	private String errorMessagePrefix;
	
	private int taskId;

	private List<PrisonCommandTaskPlaceholderData> customPlaceholders;
	
	
	private List<String> tasks;
	private List<Long> elapsedTimes; 
	
	private String errorMessage;
	

	
	public enum TaskMode {
		inline, 
		inlinePlayer(true), 
		
		sync,
		syncPlayer(true);
		
		private final boolean playerTask;
		private TaskMode() {
			this(false);
		}
		private TaskMode( boolean playerTask ) {
			this.playerTask = playerTask;
		}
		
		public boolean isPlayerTask() {
			return playerTask;
		}
		
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
	
	public enum CommandEnvironment {
		all_commands,
		rank_commands,
		mine_commands,
		blockevent_commands
		;
	}
	
	public enum CustomPlaceholders {
		
		player(CommandEnvironment.all_commands, 
				"{player} provides a player's name."),
		player_uid(CommandEnvironment.all_commands, 
				"{player_uid} provides a player's uuid."),

		msg(CommandEnvironment.all_commands, 
				"{msg} sends a message to a player's chat."),
		broadcast(CommandEnvironment.all_commands, 
				"{broadcast} sends a message to all players on the server."),
		title(CommandEnvironment.all_commands, 
				"{title} sends a message to the player's title."),
		actionBar(CommandEnvironment.all_commands, 
				"{actionBar} sends a message to the player's actionBar." ),

		inline(CommandEnvironment.all_commands, 
				"{inline} runs the command as console in the same task as this action." ),
		inlinePlayer(CommandEnvironment.all_commands, 
				"{inlinePlayer} runs the command as the player in the same task as this action."),
		sync(CommandEnvironment.all_commands, 
				"{sync} runs the command as console in a new sync task." ),
		syncPlayer(CommandEnvironment.all_commands, 
				"{syncPlayer} runs the command as the payer in a new sync task."),

		
		ifPerm(CommandEnvironment.all_commands, 
				"{ifPerm:<perm>} Continues executing commands in the chain if "
				+ "the player has the perm '<perm>'.",
				"ifPerm:<perm>" ),
		ifNotPerm(CommandEnvironment.all_commands, 
				"{ifNotPerm:<perm>} Stops executing commands in the chain if "
						+ "the player has the perm '<perm>'.",
						"ifNotPerm:<perm>" ),
		
		
		firstJoin(CommandEnvironment.rank_commands, 
				"{firstJoin} runs the command on first join events for new players"),
		promote(CommandEnvironment.rank_commands, 
				"{promote} runs the command only on promotions such as rankup, promote, and setRank."),
		demote(CommandEnvironment.rank_commands, 
				"{demote} runs the command only on demotions such as demote."),
		

		balanceInitial(CommandEnvironment.rank_commands, 
				"{balanceInitial} a player's initial balance before the promotion/demotion."),
		balanceFinal(CommandEnvironment.rank_commands, 
				"{balanceFinal} a player's final balance after the promotion/demotion."),
		currency(CommandEnvironment.rank_commands, 
				"{currency} if a rank has a custom currency, then this will contain it's name."),
		originalRankCost(CommandEnvironment.rank_commands),
		rankupCost(CommandEnvironment.rank_commands),

		ladder(CommandEnvironment.rank_commands, 
				"{ladder} the ladder which has the ranks."),
		rank(CommandEnvironment.rank_commands,
				"{rank} the original rank the player started off with."),
		rankTag(CommandEnvironment.rank_commands, 
				"{rankTag} the original rank's tag."),
		targetRank(CommandEnvironment.rank_commands, 
				"{targetRank} the new rank."),
		targetRankTag(CommandEnvironment.rank_commands,
				"{targetRankTag} the new rank's tag."),

		
		
		blockName(CommandEnvironment.blockevent_commands,
				"{blockName} returns the name of the block. Custom blocks will be " +
				"prefixed with their namespace."),
		mineName(CommandEnvironment.blockevent_commands, 
				"{mineName} returns the name of a mine where the block was broke. " +
				"Returns an empty String if outside of a mine."),
		
		locationWorld(CommandEnvironment.blockevent_commands, 
				"{locationWorld} returns the world name for the coordinates of the block."),
		locationX(CommandEnvironment.blockevent_commands, 
				"{locationX} returns the integer x value for the coordinates of the block."),
		locationY(CommandEnvironment.blockevent_commands, 
				"{locationY} returns the integer y value for the coordinates of the block."),
		locationZ(CommandEnvironment.blockevent_commands, 
				"{locationZ} returns the integer z value for the coordinates of the block."),
		
		coordinates(CommandEnvironment.blockevent_commands,
				"{coordinates} returns the block coordinates in the format of '(x, y, x)' " +
				"where x, y, and z are doubles."),
		worldCoordinates(CommandEnvironment.blockevent_commands,
				"{worldCoordinates} returns the block coordinates in the format of '(world,x,y,x)' " +
				"where x, y, and z are integers."),
		blockCoordinates(CommandEnvironment.blockevent_commands,
				"{blockCoordinates} is similar to worldCoordinates, but prefixed with the " +
				"block name 'blockName::(world,x,y,z)'."),

		
		blockChance(CommandEnvironment.blockevent_commands, 
				"{blockChance} if the block is in a mine and is one of the placement blocks, then " +
				"this will be the block's spawn percent chance."), 
		blockIsAir(CommandEnvironment.blockevent_commands, 
				"{blockIsAir} boolean value if the original block is AIR. Technically this can " +
				"never happen since you cannot 'break' AIR blocks with tools."),
		
		blocksPlaced(CommandEnvironment.blockevent_commands, 
				"{blocksPlaced} the number of blocks that were placed within a mine at the " +
				"last mine reset. This may not be the mine size (see {mineBlockSize}) if " +
				"some blocks were placed as AIR."),
		blockRemaining(CommandEnvironment.blockevent_commands, 
				"{blocksRemaining} don't use. Unknown value."),
		blocksMinedTotal(CommandEnvironment.blockevent_commands,
				"{blocksMinedTotal} total blocks mined in the mine."),
		mineBlocksRemaining(CommandEnvironment.blockevent_commands,
				"{mineBlocksRemaining} the number of blocks remaining in a mine."),

		mineBlocksRemainingPercent(CommandEnvironment.blockevent_commands),
		mineBlocksTotalMined(CommandEnvironment.blockevent_commands),
		mineBlocksSize(CommandEnvironment.blockevent_commands),

		blockMinedName(CommandEnvironment.blockevent_commands, 
				"{blockMinedName} the name of the mined block."),
		blockMinedNameFormal(CommandEnvironment.blockevent_commands, 
				"{blockMinedNameFormal} the formal name of the mined block, which includes " +
				"the namespace such as 'namespace:blockName'."),
		blockMinedBlockType(CommandEnvironment.blockevent_commands),
		
		eventType(CommandEnvironment.blockevent_commands),
		eventTriggered(CommandEnvironment.blockevent_commands),
		
		utilsDecay(CommandEnvironment.blockevent_commands)

		;
		
		private final CommandEnvironment environment;
		private final String description;
		private final String exampleUsage;
		
		private CustomPlaceholders( CommandEnvironment environment ) {
			this.environment = environment;
			this.description = null;
			this.exampleUsage = null;
		}
		private CustomPlaceholders( CommandEnvironment environment, String description ) {
			this.environment = environment;
			this.description = description;
			this.exampleUsage = null;
		}
		private CustomPlaceholders( CommandEnvironment environment, String description,
				String exampleUsage ) {
			this.environment = environment;
			this.description = description;
			this.exampleUsage = exampleUsage;
		}

		public static String listPlaceholders( CommandEnvironment environment ) {
			StringBuilder sb = new StringBuilder();
			
			if ( environment != null ) {
				
				for ( CustomPlaceholders cp : values() ) {
					if ( environment.equals( cp.getEnvironment() ) ) {
						
						if ( sb.length() > 0 ) {
							sb.append( " " );
						}
						sb.append( cp.getPlaceholder() );
					}
				}
			}
			
			return sb.toString();
		}
		
		/**
		 * <p>This function returns the enum's name wrapped in a 
		 * placeholder.
		 * </p>
		 * 
		 * @return
		 */
		public String getPlaceholder() {
			return "{" + 
					( getExampleUsage() != null ? 
							getExampleUsage() : 
								name() )
					 + "}";
		}
		
		public CommandEnvironment getEnvironment() {
			return environment;
		}

		public String getDescription() {
			return description;
		}
		
		public String getExampleUsage() {
			return exampleUsage;
		}
	}
	
	
	public PrisonCommandTaskData( String errorMessagePrefix, 
			String command ) {
		this( errorMessagePrefix, command, 0 );
	}
	public PrisonCommandTaskData( String errorMessagePrefix, 
					String command, int commandRow ) {
		super();
		
		this.commandRow = commandRow;
		
		this.errorMessagePrefix = errorMessagePrefix;
		this.taskId = 0;
		
		this.customPlaceholders = new ArrayList<>();
		this.elapsedTimes = new ArrayList<>();
		
		TaskMode taskMode = TaskMode.sync;
		
		if ( command.contains( "{inline}" ) ) {
			taskMode = TaskMode.inline;
			command = command.replace( "{inline}", "" );
		}
		
		if ( command.contains( "{inlinePlayer}" ) ) {
			taskMode = TaskMode.inlinePlayer;
			command = command.replace( "{inlinePlayer}", "" );
		}
		
		if ( command.contains( "{sync}" ) ) {
			taskMode = TaskMode.sync;
			command = command.replace( "{sync}", "" );
		}
		
		if ( command.contains( "{syncPlayer}" ) ) {
			taskMode = TaskMode.syncPlayer;
			command = command.replace( "{syncPlayer}", "" );
		}
		
		this.cmd = command;
		this.taskMode = taskMode;
		
	}
	
	public String getDebugDetails() {
		StringBuilder sb = new StringBuilder();
		
		long nanoTotal = 0;
		
		DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,000.0000" );
		
		for ( Long elapsedNano : elapsedTimes )
		{
			if ( sb.length() > 0 ) {
				sb.append( ", " );
			}
			nanoTotal += elapsedNano;
			
			double elapsedMs = elapsedNano / 1000000.0d;
			sb.append( dFmt.format( elapsedMs ) );
		}
		
		sb.insert( 0, "[" );
		sb.append( "]" );
		
		double totalNano = nanoTotal / 1000000.0;
		
		String message = null;
		
		if ( ladder != null && rankTarget != null ) {
			
			message = String.format( 
					"  Cmd Debug: %s Ladder: %s %s row: %d  tasks: %d  elapsedTime: %s  %s", 
					errorMessagePrefix, ladder.getName(), rankTarget.getRank().getName(),
					commandRow, tasks.size(), 
					dFmt.format( totalNano ),
					sb.toString()
					);
		}
		else {
			message = String.format( 
					"  Cmd Debug: %s  row: %d  tasks: %d  elapsedTime: %s  %s", 
					errorMessagePrefix, 
					commandRow, tasks.size(), 
					dFmt.format( totalNano ),
					sb.toString()
					);
		}
		
		return message;
	}
	
	public void runCommandTask() {
		runCommandTask( null );
	}
	
	public void runCommandTask( Player player ) {
		
//		if ( command.contains( "{inline}" ) ) {
//			taskMode = TaskMode.inline;
//			command = command.replace( "{inline}", "" );
//		}
//		
//		if ( command.contains( "{inlinePlayer}" ) ) {
//			taskMode = TaskMode.inlinePlayer;
//			command = command.replace( "{inlinePlayer}", "" );
//		}
//		
//		if ( command.contains( "{sync}" ) ) {
//			taskMode = TaskMode.sync;
//			command = command.replace( "{sync}", "" );
//		}
//		
//		if ( command.contains( "{syncPlayer}" ) ) {
//			taskMode = TaskMode.syncPlayer;
//			command = command.replace( "{syncPlayer}", "" );
//		}
//		
		String commandTranslated = translateCommand( player, getCmd() );
		
		// Split multiple commands in to a List of individual tasks:
		List<String> tasks = new ArrayList<>( 
				Arrays.asList( commandTranslated.split( ";" ) ));
		
		if ( tasks.size() > 0 ) {
			
			this.errorMessage = errorMessagePrefix + ": " +
						(player == null ? "" : "Player: " + player.getName() + " ");
			
			
			this.tasks = tasks;
			
			runTask( player );
			
//			PrisonDispatchCommandTask task = 
//					new PrisonDispatchCommandTask( tasks, errorMessage, 
//									player, taskMode.isPlayerTask() );
			
			
			// Ignore taskMode since it's already running in a new sync task:
//			task.run();
			
			
			// NOTE: taskMode is no longer used, since all tasks are being ran 
			//       within a sync task that has already been submitted.
//			switch ( taskMode )
//			{
//				case inline:
//				case inlinePlayer:
//					// Don't submit, but run it here within this thread:
//					task.run();
//					break;
//					
//				case sync:
//				case syncPlayer:
//				//case "async": // async will cause failures so run as sync:
//					
//					// submit task: 
//					setTaskId( PrisonTaskSubmitter.runTaskLater(task, 0) );
//					break;
//					
//				default:
//					break;
//			}
			
		}

	}
	
	
	public void runTask( Player player ) {
		if ( tasks != null && tasks.size() > 0 ) {
			
			for ( String task : tasks ) {
				
				long start = System.nanoTime();
				
				
				// was failing with leading spaces after spliting after a ";" so trim to fix it:
				task = task == null ? "" : task.trim();

				
				// If the task is '{ifPerm:<perm>}' then the player must have the perm to 
				// continue:
				if ( task.toLowerCase().startsWith( "{ifperm:" ) ) {
					String perm = task.substring( 8, task.length() - 1 );
					
					boolean hasPerm = player.hasPermission( perm );
					
					if ( !hasPerm ) {
						break;
					}
				}
				if ( task.toLowerCase().startsWith( "{ifnotperm:" ) ) {
					String perm = task.substring( 11, task.length() - 1 );
					
					boolean hasPerm = player.hasPermission( perm );
					
					if ( hasPerm ) {
						break;
					}
				}
				
				
				// Apply the custom placeholders:
				for ( PrisonCommandTaskPlaceholderData cPlaceholder : getCustomPlaceholders() ) {
					if ( cPlaceholder.contains( task ) ) {
						task = cPlaceholder.replace( task );
					}
				}
				
				try {
					if ( taskMode.isPlayerTask() && player != null ) {
//						double start = System.currentTimeMillis();
						
						PrisonAPI.dispatchCommand( player, task );
						
//						double stop = System.currentTimeMillis();
//						Output.get().logDebug( "PrisonDispatchCommandTask.run: (player) " + 
//									(stop - start) + " ms  player= " + player.getName() + 
//									"  task: " + task );
					}
					else {
//						double start = System.currentTimeMillis();
						
						PrisonAPI.dispatchCommand( task );

//						double stop = System.currentTimeMillis();
//						Output.get().logDebug( "PrisonDispatchCommandTask.run: (console) " + 
//									(stop - start) + " ms" +
//									"  task: " + task );
					}
				}
				catch ( Exception e ) {

					Output.get().logError( "PrisonDispatchCommand: Error trying to run task: " + errorMessage + 
							"  Task: [" + task + "] " + e.getMessage() );
				}
				
				long stop = System.nanoTime();
				long elapsed = stop - start;
				
				elapsedTimes.add( Long.valueOf( elapsed ) );

			}
			
		}
	}
	
	private String translateCommand( Player player, String command ) {
		
		String formatted = command
				.replace( "{broadcast}", "prison utils broadcast " );
		
		if ( player != null ) {
			formatted = formatted
					.replace( "{msg}", "prison utils msg {player} " )
					.replace( "{actionBar}", "prison utils titles actionBar {player} " )
					.replace( "{title}", "prison utils titles title {player} " )
					.replace( "{player}", player.getName())
					.replace( "{player_uid}", player.getUUID().toString())
					.replace( "{utilsDecay}", "prison utils decay" );
		}
		
		if ( getCustomPlaceholders() != null && getCustomPlaceholders().size() > 0 ) {
			for ( PrisonCommandTaskPlaceholderData cph : getCustomPlaceholders() ) {
				String placeholder = "{" + cph.getPlaceholder() + "}";
				if ( formatted.contains( placeholder ) ) {
					formatted = formatted.replace( placeholder, cph.getValue() );
				}
			}
		}
		
		return formatted;
	}

	/**
	 * <p>Add a custom placeholder that will be applied to each task when its running.
	 * </p>
	 * 
	 * @param placeholder The String placeholder that should include the { } escape 
	 * 						characters.
	 * @param value The value that is used to replace the placeholder.
	 */
	public void addCustomPlaceholder( CustomPlaceholders placeholder, String value ) {
		PrisonCommandTaskPlaceholderData cph = new PrisonCommandTaskPlaceholderData( placeholder, value);
		getCustomPlaceholders().add( cph );
	}
	
	public RankLadder getLadder() {
		return ladder;
	}
	public void setLadder( RankLadder ladder ) {
		this.ladder = ladder;
	}

	public PlayerRank getRankTarget() {
		return rankTarget;
	}
	public void setRankTarget( PlayerRank rankTarget ) {
		this.rankTarget = rankTarget;
	}

	public PlayerRank getRankOriginal() {
		return rankOriginal;
	}
	public void setRankOriginal( PlayerRank rankOriginal ) {
		this.rankOriginal = rankOriginal;
	}

	public String getCmd() {
		return cmd;
	}
	public void setCmd( String cmd ) {
		this.cmd = cmd;
	}
	
	public int getCommandRow() {
		return commandRow;
	}
	public void setCommandRow( int commandRow ) {
		this.commandRow = commandRow;
	}

	public TaskMode getTaskMode() {
		return taskMode;
	}
	public void setTaskMode( TaskMode taskMode ) {
		this.taskMode = taskMode;
	}

	public String getErrorMessagePrefix() {
		return errorMessagePrefix;
	}
	public void setErrorMessagePrefix( String errorMessagePrefix ) {
		this.errorMessagePrefix = errorMessagePrefix;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId( int taskId ) {
		this.taskId = taskId;
	}
	
	public List<PrisonCommandTaskPlaceholderData> getCustomPlaceholders() {
		return customPlaceholders;
	}
	public void setCustomPlaceholders( List<PrisonCommandTaskPlaceholderData> customPlaceholders ) {
		this.customPlaceholders = customPlaceholders;
	}
}
