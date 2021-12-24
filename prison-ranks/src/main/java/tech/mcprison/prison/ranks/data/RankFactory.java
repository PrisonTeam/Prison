package tech.mcprison.prison.ranks.data;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.ConversionUtil;

public class RankFactory
	extends RankMessages
{

	@SuppressWarnings( "unchecked" )
	public Rank createRank( Document document )
	{
		Rank rank = null;

		try
		{
			// Object pos = document.get("position");
			// this.position = RankUtil.doubleToInt( pos == null ? 0.0d : pos );

			int id = ConversionUtil.doubleToInt( document.get( "id" ) );
			String name = (String) document.get( "name" );
			String tag = (String) document.get( "tag" );
			double cost = (double) document.get( "cost" );

			rank = new Rank( id, name, tag, cost );

			String currency = (String) document.get( "currency" );
			rank.setCurrency( (currency == null || "null".equalsIgnoreCase( currency ) ? null : currency) );

			rank.getRankUpCommands().clear();
			Object cmds = document.get( "commands" );
			if ( cmds != null )
			{

				List<String> commands = (List<String>) cmds;
				for ( String cmd : commands )
				{
					if ( cmd != null )
					{
						rank.getRankUpCommands().add( cmd );
					}
				}

				// This was allowing nulls to be added to the live commands...
				// this.rankUpCommands = (List<String>) cmds;
			}

			rank.getMines().clear();
			rank.getMineStrings().clear();
			Object minesObj = document.get( "mines" );
			if ( minesObj != null )
			{
				List<String> mineStrings = (List<String>) minesObj;
				rank.setMineStrings( mineStrings );
			}

			// getPermissions().clear();
			// Object perms = document.get( "permissions" );
			// if ( perms != null ) {
			// List<String> permissions = (List<String>) perms;
			// for ( String permission : permissions ) {
			// getPermissions().add( permission );
			// }
			// }
			//
			//
			// getPermissionGroups().clear();
			// Object permsGroups = document.get( "permissionGroups" );
			// if ( perms != null ) {
			// List<String> permissionGroups = (List<String>) permsGroups;
			// for ( String permissionGroup : permissionGroups ) {
			// getPermissionGroups().add( permissionGroup );
			// }
			// }

			
//			// Hook up the ladder if it has not been setup yet:
//			if ( rank.getLadder() == null ) {
//				
//				RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( rank );
//				
//				rank.setLadder( ladder );
//			}
		}
		catch ( Exception e )
		{
			String id = rank == null ? "(null rank)" : Integer.toString( rank.getId() );
			String rankName = rank.getName() == null ? "null" : rank.getName();
			String message = rankFailureLoadingRanksMsg( id,
					rankName, e.getMessage() );

			Output.get().logError( message );
		}

		return rank;
	}
	
    public Document toDocument( Rank rank ) {
        Document ret = new Document();
//        ret.put("position", this.position );
        ret.put("id", rank.getId());
        ret.put("name", rank.getName() );
        ret.put("tag", (rank.getTag() == null ? "none" : rank.getTag()) );
        ret.put("cost", rank.getCost() );
        ret.put("currency", rank.getCurrency() );
        
        List<String> cmds = new ArrayList<>();
        for ( String cmd : rank.getRankUpCommands() ) {
        	// Filters out possible nulls:
			if ( cmd != null ) {
				cmds.add( cmd );
			}
		}
        ret.put("commands", cmds);
        
        List<String> mineStrings = new ArrayList<>();
        if ( rank.getMines() != null ) {
        	for ( ModuleElement mine : rank.getMines() ) {
        		 String mineString = mine.getModuleElementType() + "," + mine.getName() + "," + 
        				 mine.getId() + "," + (mine.getTag() == null ? "none" : mine.getTag());
        		 mineStrings.add( mineString );
			}
        }
        ret.put("mines", mineStrings);
        
//        ret.put( "permissions", getPermissions() );
//        ret.put( "permissionGroups", getPermissionGroups() );
        
        return ret;
    }
    


}
