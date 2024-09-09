package tech.mcprison.prison.ranks.data;

import java.util.List;

import com.google.gson.internal.LinkedTreeMap;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.ConversionUtil;


/**
 * <p>Conversion of IDs: In general, IDs should not be used. The newer
 * format of working with ranks and ladders, is that only the rank's name
 * should be used to link ranks to ladders.  It's a one way path, where only 
 * the ladders are saved with a group of ranks. A saved rank has no idea what
 * ladder, if any, that it's tied to.
 * </p>
 * 
 * <p>The conversion process is simple and does not have to be concerned with 
 * reverting back to using IDs or adding an id when it has a value of -1.
 * The only thing that will change is that new ranks and ladders will be given 
 * an id of -1.  If upon saving an id is -1, then it will not be included in the
 * save file. If a rank or ladder already has an id, it will not be changed and
 * it will be continually saved with the rank and ladder.
 * </p>
 * 
 * <p>The only real "conversion" process is when ladders are loaded, and if a 
 * rank does not have a name, then it will be joined by the id, then when 
 * the ladder is fully loaded, it will be resaved, thus dropping the rank 
 * ids within the ladder's save file.
 * </p>
 * 
 * <p>Ranks are always loaded before ladders.
 * </p>
 * 
 */
public class RankLadderFactory
{
	
	public RankLadderFactory() {
		super();
		
	}
	
	/**
	 * <p>This function loads a ladder from a save file.
	 * </p>
	 * 
	 * <p>For conversion to eliminating IDs, if a rank or a ladder has an
	 * id, then keep it for now.  If a rank was saved in the ladder without a 
	 * name, then the ladder needs to be resaved, after loading all ranks
	 * so the ids won't be saved with the new format.  If a rank name is not
	 * loaded, then the ids will be used to connect the ranks to the ladder.
	 * Otherwise the name will be used.
	 * </p>
	 * 
	 * <p>No special processing needs to be used in the loading of the ladder, 
	 * except to track when a rank does not have a name to trigger a resave at
	 * the end of the loading process by setting 'isDirty' to true.
	 * </p>
	 * 
	 * 
	 * @param document
	 * @param rankManager
	 * @return
	 */
	@SuppressWarnings( "unchecked" )
	public RankLadder createRankLadder(Document document, RankManager rankManager) {
		RankLadder rankLadder = null;
	    	
	    	boolean isDirty = false;
	    	
	    	
	    	// If an "id" field is not found, doubleToInt will now return a -1.
	    	int id = ConversionUtil.doubleToInt(document.get("id"));
	    	String name = (String) document.get("name");
	    	
	    	rankLadder = new RankLadder( id, name );
	    	
//	    	RankManager rankManager = prisonRanks.getRankManager();
	    	
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
	        	
	        	if ( rank == null ) {
	        		
	        		// Force a resave to "fix" the problem?
	        		isDirty = true;
	        		
	        		Output.get().logInfo( "RankLadderFactory.createRankLadder: "
	        				+ "A loaded rank was null, and is skipping it.  Since it"
	        				+ "was null, there is no way to identify what it was. "
	        				+ "This notice is to inform you and to prevent Prison from "
	        				+ "failing to load.");
	        		
	        		continue;
	        	}
	        	
	        	// The only real field that is important here is rankId to tie the 
	        	// rank back to this ladder.  Name helps clarify the contents of the 
	        	// Ladder file. 
	        	int rRankId = ConversionUtil.doubleToInt((rank.get("rankId")));
	        	String rRankName = (String) rank.get( "rankName" );
	        	
	        	Rank rankPrison = null;
	        	
	        	if ( rRankId != -1 ) {
	        		// the file was saved with rankIds, so resave to remove them:
	        		isDirty = true;
	        	}
	        	
	        	if ( rRankName == null || rRankName.trim().length() == 0 ) {
	        		
	        		// NOTICE: Loading an older save file that has not been converted
	        		//         yet. No name was saved, so link with id and then set
	        		//         isDirty to true.  
	        		isDirty = true;
	        		
	        		rankPrison = rankManager.getRank( rRankId );
	        		
	        	}
	        	else {
	        		// Load rank by name:
	        		rankPrison = rankManager.getRank( rRankName );
	        		
	        	}
	        	
	        	
	        	if ( rankPrison != null && rankPrison.getLadder() != null ) {
	        		
	        		if ( rankPrison.getLadder().equals( rankLadder ) ) {
	        			// ignore: The selected rank is already on this ladder.
	        		}
	        		else {
	        			
	        			String msg = String.format(
	        					"&4Loading ladder: %s  Rank %s is already assigned to the %s ladder. "
	        					+ "You may need to manually move the rank if this is incorrect. ",
	        					rankLadder.getName(),
	        					rankPrison.getName(),
	        					rankPrison.getLadder().getName()
	        					);
	        			Output.get().logInfo( msg );
	        			
	        		}
	        		
//	        		RankMessages rMessages = new RankMessages();
//	        		rMessages.rankFailureLoadingDuplicateRankMsg( 
//	        				rankPrison.getName(), 
//	        				rankPrison.getLadder().getName(), 
//	        				rankLadder.getName() );

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
