package tech.mcprison.prison.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.mcprison.prison.internal.Player;

public class PrisonCommandTask {
	
	private String errorMessagePrefix;
	
	private int taskId;

	private List<PrisonCommandTaskPlaceholderData> customPlaceholders;
	
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
	
	
	public PrisonCommandTask( String errorMessagePrefix ) {
		super();
		
		this.errorMessagePrefix = errorMessagePrefix;
		this.taskId = 0;
		
		this.customPlaceholders = new ArrayList<>();
	}
	
	
	
	public void submitCommandTask( String command ) {
		submitCommandTask( null, command, TaskMode.sync );
	}
	
	public void submitCommandTask( Player player, String command ) {
		submitCommandTask( player, command, TaskMode.sync );
	}
	
	public void submitCommandTask( Player player, String command, 
			TaskMode taskMode ) {
		
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
		
		String commandTranslated = translateCommand( player, command );
		
		// Split multiple commands in to a List of individual tasks:
		List<String> tasks = new ArrayList<>( 
				Arrays.asList( commandTranslated.split( ";" ) ));
		
		if ( tasks.size() > 0 ) {
			
			String errorMessage = errorMessagePrefix + ": " +
						(player == null ? "" : "Player: " + player.getName() + " ");
			
			
			PrisonDispatchCommandTask task = 
					new PrisonDispatchCommandTask( tasks, errorMessage, 
									player, taskMode.isPlayerTask() );
			
			
			switch ( taskMode )
			{
				case inline:
				case inlinePlayer:
					// Don't submit, but run it here within this thread:
					task.run();
					break;
					
				case sync:
				case syncPlayer:
				//case "async": // async will cause failures so run as sync:
					
					// submit task: 
					setTaskId( PrisonTaskSubmitter.runTaskLater(task, 0) );
					break;
					
				default:
					break;
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
