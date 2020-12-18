package tech.mcprison.prison.mines.data;

import java.text.DecimalFormat;

public class MineBlockEvent {

	private double chance;
	private String permission;
	private String command;
	private String mode;
	
	public MineBlockEvent( double chance, String permission, String command, String mode ) {
		super();
		
		this.chance = chance;
		this.permission = permission;
		this.command = command;
		this.mode = mode;
	}

	
	public String toSaveString() {
		DecimalFormat dFmt = new DecimalFormat("0.00000");
		return dFmt.format( getChance() ) + "|" + 
				(getPermission() == null || getPermission().trim().length() == 0 ? 
						"none" : getPermission())  + "|" + 
				getCommand() + "|" + getMode();
	}
	
	public static MineBlockEvent fromSaveString( String chancePermCommand ) {
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
			
			if ( !"sync".equalsIgnoreCase( mode ) && !"inline".equalsIgnoreCase( mode ) ) {
				mode = "sync";
			}
//			boolean async = (asyncStr != null && 
//											"true".equalsIgnoreCase( asyncStr ) );
			
			if ( command != null && command.trim().length() > 0 ) {
				
				results = new MineBlockEvent( chance, permission, command, mode );
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

	public String getMode() {
		return mode;
	}
	public void setMode( String mode ) {
		this.mode = mode;
	}

	public boolean isInline() {
		return "inline".equalsIgnoreCase( getMode() );
	}
	public boolean isSync() {
		return "sync".equalsIgnoreCase( getMode() );
	}
	public boolean isAsync() {
		return "async".equalsIgnoreCase( getMode() );
	}
	
}
