package tech.mcprison.prison.spigot.utils.tasks;

import java.util.HashMap;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.MineTargetBlockKey;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.tasks.PrisonCommandTaskData;
import tech.mcprison.prison.tasks.PrisonCommandTasks;
import tech.mcprison.prison.util.Location;

public interface PrisonUtilsTaskTypes
{
	public UtilTaskType getTaskType();
	
	public void run();
	
	public boolean isAsyc();
	
	
	public enum UtilTaskType {
		delay,
		replaceBlock,
		runCommand,
		
		removeUnbreakableBlock
	}
	
	
	/**
	 * A PrisonUtilsTaskTypeDelay only supplies a delay value that is to be applied
	 * to the next runnable task.  This will not do anything directly, except
	 * submit the next task for the specified number of ticks.  By default, 
	 * tasks are submitted with a delay of zero ticks so you should never need to
	 * use a value of zero since that will not produce a different result.
	 *
	 */
	public class PrisonUtilsTaskTypeDelay
		implements PrisonUtilsTaskTypes {

		private int delayTicks;
		
		public PrisonUtilsTaskTypeDelay( int delayTicks ) {
			super();
			
			this.delayTicks = delayTicks;
		}
		
		@Override
		public UtilTaskType getTaskType() {
			return UtilTaskType.delay;
		}

		@Override
		public void run() {
			// nothing to run:
		}

		@Override
		public boolean isAsyc() {
			return false;
		}
		public int getDelayTicks() {
			return delayTicks;
		}
	}
	
	public class PrisonUtilsTaskTypReplaceBlock
		implements PrisonUtilsTaskTypes {
		
		private Location location;
		private PrisonBlock prisonBlock;
		
		public PrisonUtilsTaskTypReplaceBlock( Location location, PrisonBlock prisonBlock ) {
			super();
			
			this.location = location;
			this.prisonBlock = prisonBlock;
		}

		@Override
		public UtilTaskType getTaskType() {
			return UtilTaskType.replaceBlock;
		}

		@Override
		public void run() {
			
			if ( getLocation() != null ) {
				Block block = getLocation().getBlockAt();
				block.setPrisonBlock( getPrisonBlock() );
			}
			
		}

		@Override
		public boolean isAsyc() {
			return false;
		}

		private Location getLocation() {
			return location;
		}

		private PrisonBlock getPrisonBlock() {
			return prisonBlock;
		}
	}

	
	public class PrisonUtilsTaskTypRunCommand
		implements PrisonUtilsTaskTypes {
		
		private Player player;
		private String command;
		
		public PrisonUtilsTaskTypRunCommand( Player player, String command ) {
			super();
			
			this.player = player;
			this.command = command;
			
		}

		@Override
		public UtilTaskType getTaskType() {
			return UtilTaskType.runCommand;
		}

		@Override
		public void run() {
			
			PrisonCommandTaskData pCmd = new PrisonCommandTaskData( 
					"PrisonUtilsTaskTypRunCommand", getCommand(), 1 );
				
			PrisonCommandTasks.submitTasks( pCmd );
//			pCmd.submitCommandTask( getPlayer() );
		}

		@Override
		public boolean isAsyc() {
			return false;
		}

		@SuppressWarnings("unused")
		private Player getPlayer() {
			return player;
		}

		private String getCommand() {
			return command;
		}

	}

	
	public class PrisonUtilsTaskTypRemoveUnbreakableBlock
		implements PrisonUtilsTaskTypes {
		
		private MineTargetBlockKey blockKey;
		private HashMap<MineTargetBlockKey, SpigotBlock> unbreakableBlockList;
		
		public PrisonUtilsTaskTypRemoveUnbreakableBlock( 
					MineTargetBlockKey blockKey, HashMap<MineTargetBlockKey, SpigotBlock> hashMap ) {
			super();
			
			this.blockKey = blockKey;
			
			this.unbreakableBlockList = hashMap;
		}

		@Override
		public UtilTaskType getTaskType() {
			return UtilTaskType.removeUnbreakableBlock;
		}

		@Override
		public void run() {
			
			getUnbreakableBlockList().remove( getBlockKey() );
		}

		@Override
		public boolean isAsyc() {
			return true;
		}

		private MineTargetBlockKey getBlockKey() {
			return blockKey;
		}

		private HashMap<MineTargetBlockKey, SpigotBlock> getUnbreakableBlockList() {
			return unbreakableBlockList;
		}
		
	}


}
