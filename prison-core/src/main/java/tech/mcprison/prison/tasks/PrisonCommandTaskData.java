package tech.mcprison.prison.tasks;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		
		player(CommandEnvironment.all_commands),
		player_uid(CommandEnvironment.all_commands),

		msg(CommandEnvironment.all_commands),
		broadcast(CommandEnvironment.all_commands),
		title(CommandEnvironment.all_commands ),
		actionBar(CommandEnvironment.all_commands ),

		inline(CommandEnvironment.all_commands),
		inlinePlayer(CommandEnvironment.all_commands),
		sync(CommandEnvironment.all_commands),
		syncPlayer(CommandEnvironment.all_commands),

		
		firstJoin(CommandEnvironment.rank_commands),

		balanceInitial(CommandEnvironment.rank_commands),
		balanceFinal(CommandEnvironment.rank_commands),
		currency(CommandEnvironment.rank_commands),
		originalRankCost(CommandEnvironment.rank_commands),
		rankupCost(CommandEnvironment.rank_commands),

		ladder(CommandEnvironment.rank_commands),
		rank(CommandEnvironment.rank_commands),
		rankTag(CommandEnvironment.rank_commands),
		targetRank(CommandEnvironment.rank_commands),
		targetRankTag(CommandEnvironment.rank_commands),

		
		
		blockName(CommandEnvironment.blockevent_commands),
		mineName(CommandEnvironment.blockevent_commands),
		
		locationWorld(CommandEnvironment.blockevent_commands),
		locationX(CommandEnvironment.blockevent_commands),
		locationY(CommandEnvironment.blockevent_commands),
		locationZ(CommandEnvironment.blockevent_commands),
		
		coordinates(CommandEnvironment.blockevent_commands),
		worldCoordinates(CommandEnvironment.blockevent_commands),
		blockCoordinates(CommandEnvironment.blockevent_commands),

		
		blockChance(CommandEnvironment.blockevent_commands),
		blockIsAir(CommandEnvironment.blockevent_commands),
		
		blocksPlaced(CommandEnvironment.blockevent_commands),
		blockRemaining(CommandEnvironment.blockevent_commands),
		blocksMinedTotal(CommandEnvironment.blockevent_commands),
		mineBlocksRemaining(CommandEnvironment.blockevent_commands),

		mineBlocksRemainingPercent(CommandEnvironment.blockevent_commands),
		mineBlocksTotalMined(CommandEnvironment.blockevent_commands),
		mineBlocksSize(CommandEnvironment.blockevent_commands),

		blockMinedName(CommandEnvironment.blockevent_commands),
		blockMinedNameFormal(CommandEnvironment.blockevent_commands),
		blockMinedBlockType(CommandEnvironment.blockevent_commands),
		
		eventType(CommandEnvironment.blockevent_commands),
		eventTriggered(CommandEnvironment.blockevent_commands),
		
		utilsDecay(CommandEnvironment.blockevent_commands)

		;
		
		private final CommandEnvironment environment;
		private CustomPlaceholders( CommandEnvironment environment ) {
			this.environment = environment;
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
			return "{" + name() + "}";
		}
		
		public CommandEnvironment getEnvironment() {
			return environment;
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
		
		DecimalFormat dFmt = new DecimalFormat( "#,000.0000" );
		
		for ( Long elapsedNano : elapsedTimes )
		{
			if ( sb.length() > 0 ) {
				sb.append( ", " );
			}
			nanoTotal += elapsedNano;
			
			double elapsedMs = 1000000.0d / elapsedNano;
			sb.append( dFmt.format( elapsedMs ) );
		}
		
		sb.insert( 0, "[" );
		sb.append( "]" );
		
		double totalNano = 1000000.0 / nanoTotal;
		
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
