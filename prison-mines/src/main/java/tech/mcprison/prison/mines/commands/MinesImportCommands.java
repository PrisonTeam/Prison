package tech.mcprison.prison.mines.commands;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.Mine.MineType;
import tech.mcprison.prison.mines.data.MineData.MineNotificationMode;
import tech.mcprison.prison.mines.features.MineLinerBuilder;
import tech.mcprison.prison.mines.features.MineLinerBuilder.LinerPatterns;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Bounds.Edges;

public class MinesImportCommands
	extends MinesBlockCommands {

	
	public MinesImportCommands( String cmdGroup ) {
		super( cmdGroup );
	}
	
	

    
    public void importJetsPrisonMines( CommandSender sender,
    			String options ) {

    	boolean save = false;
    	boolean addLiner = false;
    	
    	String path = "JetsPrisonMines//mines//";

    	String worldForced = "";
				
//		if ( options.contains( "testImport" ) ) {
//			testImport = true;
//			list = true;
//			options = options.replace( "testImport", "" ).trim();
//		}
//		
//		if ( options.contains( "list" ) ) {
//			testImport = false;
//			list = true;
//			options = options.replace( "list", "" ).trim();
//		}
		
		if ( options.contains( "save" ) ) {
			save = true;
			options = options.replace( "save", "" ).trim();
		}
		
		if ( options.contains( "addLiner" ) ) {
			addLiner = true;
			options = options.replace( "addLiner", "" ).trim();
		}
		
		String pathStr = extractParameter("path=", options);
		if ( pathStr != null ) {
			options = options.replace( pathStr, "" );
			pathStr = pathStr.replace( "path=", "" ).trim();
			
			path = pathStr;
		}
		
		String worldStr = extractParameter("world=", options);
		if ( worldStr != null ) {
			options = options.replace( worldStr, "" );
			worldStr = worldStr.replace( "world=", "" ).trim();
			
			worldForced = worldStr;
		}

		File dirPlugins = Prison.get().getPlatform().getPluginDirectory().getParentFile();
		File dirMines = new File( dirPlugins, path );
		
		if ( !dirMines.exists() || !dirMines.isDirectory() ) {
			sender.sendMessage( 
					String.format( 
							"Error: The path for JetsPrisonMines' mines does not exist: '%s'",
							dirMines.getAbsolutePath() ) );
				
			return;
		}
		
		DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
		List<File> files = Arrays.asList( dirMines.listFiles() );
		
		PrisonMines pMines = PrisonMines.getInstance();
		
		sender.sendMessage( 
				String.format( 
						"Prison import from JetsPrisonMines' mines: '%s'",
						dirMines.getAbsolutePath() ) );
		sender.sendMessage( 
				String.format( 
						"Mines to import: '%d'",
						files.size() ) );
		
		int alreadyExists = 0;
		int failed = 0;
		int saved = 0;
		int savable = 0;
		
		for ( File f : files ) {

			if ( f.exists() && f.isFile() ) {
				
				Mine mine = convertJetsPrisonMines( f, worldForced );
				
				String status = "";
				if ( mine == null ) {
					failed++;
					status = "(failed)";
				}
				else if ( pMines.getMine( mine.getName() ) != null ) {
					alreadyExists++;
					status = "(alreadyExists)";
				}
				else if ( save ) {
					pMines.getMineManager().add(mine);
					pMines.getMineManager().saveMine( mine );
					
					if ( addLiner ) {
						Prison.get().getPlatform().autoCreateMineLinerAssignment( 
								mine, true, true );
					}
					
					saved++;
					status = "(saved)";
				}
				else {
					savable++;
					status = "(savable)";
				}
				
				sender.sendMessage( 
						String.format( 
								"  %-18s  %7s bytes   %s",
								f.getName(), iFmt.format(f.length()),
								status ) );
			}
		}
		
		if ( addLiner ) {
			List<Mine> mines = new ArrayList<>();
	    	
			mines.addAll( pMines.getMines() );
	    	
	    	for ( Mine mine : mines )
			{
	    		if ( !mine.isVirtual() ) {
	    			
	    			for (Edges edge : Edges.values() ) {
						
	    				LinerPatterns linerPattern = LinerPatterns.fromString( 
	    							mine.getLinerData().getEdge(edge) );
	    				
	    				boolean force = mine.getLinerData().getForce(edge);
	    				boolean useTracer = false;
	    				
	    				new MineLinerBuilder( mine, edge, linerPattern, force, useTracer );
					}
	    			
	    		}
			}
		}

		sender.sendMessage( 
				String.format( 
						"Impored Mines:  Saved: %d  AlreadyExists: %d  Saveable: %d  Failed: %d", 
						saved, alreadyExists, savable, failed ));
    }




	private Mine convertJetsPrisonMines(File f, String worldForced ) {
		Mine mine = null;
		
		
		if ( !f.isFile() || !f.canRead() || f.length() == 0 ) {
			return mine;
		}
		

		Map<String,Object> yaml = null;
		
		try {
			yaml = Prison.get().getPlatform().loadYaml( f );
			
			String mineName = (String) yaml.get( "mine_name" );
			
			
			List<String> blocks = getYamlListString( yaml, "blocks" );
			
			String spawnWorld = worldForced != null && worldForced.length() > 0 ?
					worldForced :
						getYamlString( yaml, "teleport_location.world" );
			double spawnX = getYamlDouble( yaml, "teleport_location.x" );
			double spawnY = getYamlDouble( yaml, "teleport_location.y" );
			double spawnZ = getYamlDouble( yaml, "teleport_location.z" );
			double spawnPitch = getYamlDouble( yaml, "teleport_location.pitch" );
			double spawnYaw = getYamlDouble( yaml, "teleport_location.yaw" );
			
			String locWorld =  worldForced != null && worldForced.length() > 0 ?
					worldForced :
						getYamlString( yaml, "region.world" );
			double xMin = getYamlInteger( yaml, "region.xmin" );
			double yMin = getYamlInteger( yaml, "region.ymin" );
			double zMin = getYamlInteger( yaml, "region.zmin" );
			double xMax = getYamlInteger( yaml, "region.xmax" );
			double yMax = getYamlInteger( yaml, "region.ymax" );
			double zMax = getYamlInteger( yaml, "region.zmax" );
			

			boolean rUseTimer = getYamlBoolean( yaml, "reset.use_timer" );
			int rTimer = getYamlInteger( yaml, "reset.timer" );
			boolean rUsePercentage = getYamlBoolean( yaml, "reset.use_percentage" );
			double rPercentage = getYamlDouble( yaml, "reset.percentage" );
			
			boolean rUseMessages = getYamlBoolean( yaml, "reset.use_messages" );
			int rBlocksMined = getYamlInteger( yaml, "reset.blocks_mined" );
			
			
			if ( mineName == null || mineName.trim().length() == 0 ||
					spawnWorld == null || spawnWorld.trim().length() == 0 ||
					locWorld == null || locWorld.trim().length() == 0 ) {
				return mine;
			}
				

			World sWorld = Prison.get().getPlatform().getWorld( spawnWorld ).orElse(null);
			Location spawn = new Location( sWorld, spawnX, spawnY, spawnZ, 
							(float) spawnPitch, (float) spawnYaw );

					
			World lWorld = Prison.get().getPlatform().getWorld( locWorld ).orElse(null);
			Location locMin = new Location( lWorld, xMin, yMin, zMin );
			Location locMax = new Location( lWorld, xMax, yMax, zMax );
			Selection mSel = new Selection( locMin, locMax);

			
			boolean logInfo = false;
			mine = new Mine( mineName, mSel, MineType.primary, logInfo );
			
			String tag = "&5[&d+" + mineName + "&5]";
			mine.setTag( tag );
			
			mine.setHasSpawn( false );
			
			if ( spawn != null ) {
				mine.setSpawn( spawn );
				mine.setHasSpawn( true );
			}
			
			if ( rUseTimer ) {
				// reset time in seconds:
				int time = rTimer * 60;
				mine.setResetTime(time);
			}
			else {
				// value of -1 disables resets by time:
				mine.setResetTime( -1 );
			}
			
			if ( rUsePercentage ) {
				mine.setResetThresholdPercent( rPercentage );
			}
			
			if ( rUseMessages ) {
				mine.setNotificationMode( MineNotificationMode.within );
			}
			else {
				mine.setNotificationMode( MineNotificationMode.disabled );
			}
			
			mine.setBlockBreakCount(rBlocksMined);
			
			PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
			 
			for (String blockDetails : blocks) {
				
				String[] bd = blockDetails.split( ":" );
				
				String blockName = bd.length > 0 ? bd[0] : null;
				String chanceStr = bd.length > 1 ? bd[1] : "1.0";
				
				PrisonBlock prisonBlock = prisonBlockTypes.getBlockTypesByName( blockName );
				
				if ( prisonBlock != null ) {
					
					double chance = 1.0;
					
					try {
						chance = Double.parseDouble(chanceStr);
					} 
					catch (NumberFormatException e) {
					}
					
					prisonBlock.setChance( chance );
					
					mine.addPrisonBlock( prisonBlock );
				}
			}
			
			
			// Check if one of the blocks is effected by gravity, and if so, set that indicator.
			mine.checkGravityAffectedBlocks();
		} 
		catch (Exception e) {

			String message = String.format( "Could not %s: %s [%s]",
					(yaml == null ? "parse mine file" : 
						mine == null ? "generate mine" :
							"fully configure mine"),
					f.getName(), 
					e.getMessage());
			Output.get().logInfo( message );
		}
		
		return mine;
	}
	
	private String getYamlString( Map<String,Object> yaml, String key ) {
		String results = null;
		
		try {
			results = (String) yaml.get( key );
		} 
		catch (Exception e) {
		}
		
		return results;
	}
	private double getYamlDouble( Map<String,Object> yaml, String key ) {
		double results = 0;
		
		try {
			results = (Double) yaml.get( key );
		} 
		catch (Exception e) {
		}
		
		return results;
	}
	private int getYamlInteger( Map<String,Object> yaml, String key ) {
		int results = 0;
		
		try {
			results = (Integer) yaml.get( key );
		} 
		catch (Exception e) {
		}
		
		return results;
	}
	private boolean getYamlBoolean( Map<String,Object> yaml, String key ) {
		boolean results = false;
		
		try {
			results = (Boolean) yaml.get( key );
		} 
		catch (Exception e) {
		}
		
		return results;
	}
	@SuppressWarnings("unchecked")
	private List<String> getYamlListString( Map<String,Object> yaml, String key ) {
		List<String> results = new ArrayList<>();
		
		try {
			results = new ArrayList<>( (List<String>) yaml.get(key) );
		} 
		catch (Exception e) {
		}
		
		return results;
	}

}
	