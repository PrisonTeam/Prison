package tech.mcprison.prison.ranks.data;

import java.util.List;

import com.google.gson.internal.LinkedTreeMap;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.ConversionUtil;

public class RankLadderFactory
{
	
	@SuppressWarnings( "unchecked" )
	public RankLadder createRankLadder(Document document, PrisonRanks prisonRanks) {
		RankLadder rankLadder = null;
	    	
	    	boolean isDirty = false;
	    	
	    	int id = ConversionUtil.doubleToInt(document.get("id"));
	    	String name = (String) document.get("name");
	    	
	    	rankLadder = new RankLadder( id, name );
	    	
	    	RankManager rankManager = prisonRanks.getRankManager();
	    	
	    	if ( rankManager == null ) {

	    		RankMessages rMessages = new RankMessages();
	    		rMessages.rankFailureLoadingRankManagerMsg( name, id );
	    		
	    		return null;
	    	}
	        
	        List<LinkedTreeMap<String, Object>> ranksLocal =
	                (List<LinkedTreeMap<String, Object>>) document.get("ranks");

			rankLadder.getRankUpCommands().clear();
			Object cmds = document.get("commands");
			if ( cmds != null ) {

				List<String> commands = (List<String>) cmds;
				for ( String cmd : commands ) {
					if ( cmd != null ) {
						rankLadder.getRankUpCommands().add( cmd );
					}
				}
				
				// This was allowing nulls to be added to the live commands... 
//				this.rankUpCommands = (List<String>) cmds;
			}

	        
//	        rankLadder.ranks = new ArrayList<>(); // already initialized
	        for (LinkedTreeMap<String, Object> rank : ranksLocal) {
	        	
	        	
	        	// The only real field that is important here is rankId to tie the 
	        	// rank back to this ladder.  Name helps clarify the contents of the 
	        	// Ladder file. 
	        	int rRankId = ConversionUtil.doubleToInt((rank.get("rankId")));
	        	String rRankName = (String) rank.get( "rankName" );
	        	
	        	Rank rankPrison = rankManager.getRank( rRankId );
	        	
	        	if ( rankPrison != null && rankPrison.getLadder() != null ) {
	        		
	        		RankMessages rMessages = new RankMessages();
	        		rMessages.rankFailureLoadingDuplicateRankMsg( 
	        				rankPrison.getName(), rankPrison.getLadder().getName(), 
	        				rankLadder.getName() );

	        		isDirty = true;
	        	}
	        	else if ( rankPrison != null) {

	        		rankLadder.addRank( rankPrison );

//	        		Output.get().logInfo( "RankLadder load : " + getName() + 
//	        				"  rank= " + rankPrison.getName() + " " + rankPrison.getId() + 
//	        				 );
	        		
//	        		// if null look it up from loaded ranks:
//	        		if ( rRankName == null  ) {
//	        			rRankName = rankPrison.getName();
//	        			dirty = true;
//	        		}
	        	}
	        	else {
	        		// Rank not found. Try to create it? The name maybe wrong.
	        		String rankName = rRankName != null && !rRankName.trim().isEmpty() ?
	        					rRankName : "Rank " + rRankId;
	        		
	        		// NOTE: The following is valid use of getCost():
	        		double cost = rankLadder.getRanks().size() == 0 ? 0 : 
	        			rankLadder.getRanks().get( rankLadder.getRanks().size() - 1 ).getCost() * 3;
	        		Rank newRank = new Rank( rRankId, rankName, null, cost );
	        		
	        		rankLadder.addRank( newRank );
	        		
//	        		String message = String.format( 
//	        				"Loading RankLadder Error: A rank for %s was not found so it was " +
//	        				"fabricated: %s  id=%d  tag=%s  cost=%d", getName(), newRank.getName(), newRank.getId(),
//	        				newRank.getTag(), newRank.getCost() );
//	        		Output.get().logError( message );
	        	}
	        	
	        }
	        
//	        this.maxPrestige = RankUtil.doubleToInt(document.get("maxPrestige"));
	        
	        
	        Double rankCostMultiplier = (Double) document.get( "rankCostMultiplierPerRank" );
	        rankLadder.setRankCostMultiplierPerRank( rankCostMultiplier == null ? 0 : rankCostMultiplier );
	        
			Boolean applyRankCostMultiplierToLadder = (Boolean) document.get( "applyRankCostMultiplierToLadder" );
			if ( applyRankCostMultiplierToLadder != null ) {
				
				rankLadder.setApplyRankCostMultiplierToLadder( applyRankCostMultiplierToLadder );
			}
			else {
				rankLadder.setApplyRankCostMultiplierToLadder( true );
				isDirty = true;
			}
			
	        
	        
//			getPermissions().clear();
//			Object perms = document.get( "permissions" );
//			if ( perms != null ) {
//				List<String> permissions = (List<String>) perms;
//				for ( String permission : permissions ) {
//					getPermissions().add( permission );
//				}
//			}
//	        
//			
//			getPermissionGroups().clear();
//			Object permsGroups = document.get( "permissionGroups" );
//			if ( perms != null ) {
//				List<String> permissionGroups = (List<String>) permsGroups;
//				for ( String permissionGroup : permissionGroups ) {
//					getPermissionGroups().add( permissionGroup );
//				}
//			}
			
			if ( isDirty ) {
				PrisonRanks.getInstance().getLadderManager().save( rankLadder );
			}

			return rankLadder;
	    }


}
