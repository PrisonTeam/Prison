package tech.mcprison.prison.ranks.commands;

import java.util.List;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.tasks.PrisonCommandTask;
import tech.mcprison.prison.tasks.PrisonRunnable;

public class RankupCommandTastk
	implements PrisonRunnable
{
	
	private Player player;
	private List<PrisonCommandTask> cmdTasks;
	
	private int cmTasksPosition = 0;
	
	public static void submitTasks( Player player, List<PrisonCommandTask> cmdTasks ) {
		
		RankupCommandTastk rcTask = new RankupCommandTastk();
		rcTask.setPlayer( player );
		rcTask.setCmdTasks( cmdTasks );
		
		
	}
	
	@Override
	public void run() {
		
	}
	

	public Player getPlayer() {
		return player;
	}
	public void setPlayer( Player player ) {
		this.player = player;
	}

	public List<PrisonCommandTask> getCmdTasks() {
		return cmdTasks;
	}
	public void setCmdTasks( List<PrisonCommandTask> cmdTasks ) {
		this.cmdTasks = cmdTasks;
	}

}
