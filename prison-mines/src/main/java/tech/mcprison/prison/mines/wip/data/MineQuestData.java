package tech.mcprison.prison.mines.wip.data;

public class MineQuestData {
	
	private String questName;
	
	private MineQuestType questType;
	
	private boolean completed = false;
	
	
	
	public enum MineQuestSource {
		byMine,
		byBlockType
		;
	}
	
	public enum MineQuestType {
		money,
		token,
		block,
		time
		;
	}
	
	public MineQuestData() {
		super();
		
		
	}

}
