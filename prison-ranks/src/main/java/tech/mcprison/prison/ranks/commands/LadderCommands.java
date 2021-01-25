package tech.mcprison.prison.ranks.commands;

import java.io.IOException;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.RowComponent;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

/**
 * @author Faizaan A. Datoo
 */
public class LadderCommands
				extends BaseCommands {
	
	public LadderCommands() {
		super( "LadderCommands" );
	}

    @Command(identifier = "ranks ladder create", description = "Creates a new rank ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderAdd(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if ( ladder != null ) {
            Output.get()
                .sendError(sender, "A ladder with the name '%s' already exists.", ladderName);
            return;
        }

        RankLadder rankLadder = PrisonRanks.getInstance().getLadderManager().createLadder(ladderName);

        if ( rankLadder == null ) {
            Output.get().sendError(sender,
                "An error occurred while creating your ladder. &8Check the console for details.");
            return;
        }

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(rankLadder);
            
            Output.get().sendInfo(sender, "The ladder '%s' has been created.", ladderName);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while creating your ladder. &8Check the console for details.");
            Output.get().logError("Could not save ladder.", e);
        }
    }

    @Command(identifier = "ranks ladder delete", description = "Deletes a rank ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemove(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if ( ladder == null ) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }
        
        if (ladder.getName().equalsIgnoreCase( "default" )) {
        	Output.get().sendError(sender, "You cannot delete the default ladder. It's needed." );
        	return;
        }

        if (ladder.getName().equalsIgnoreCase( "prestiges" )) {
        	Output.get().sendError(sender, "You cannot delete the prestiges ladder. It's needed." );
        	return;
        }
        
        if ( PrisonRanks.getInstance().getLadderManager().removeLadder(ladder) ) {
            Output.get().sendInfo(sender, "The ladder '%s' has been deleted.", ladderName);

        } else {
            Output.get().sendError(sender,
                "An error occurred while removing your ladder. &8Check the console for details.");
        }
    }

    @Command(identifier = "ranks ladder list", description = "Lists all rank ladders.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderList(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("Ladders");
        BulletedListComponent.BulletedListBuilder list =
            new BulletedListComponent.BulletedListBuilder();
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()) {
            list.add(ladder.getName());
        }
        display.addComponent(list.build());

        display.send(sender);
    }

    @Command(identifier = "ranks ladder listranks", description = "Lists the ranks within a ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderInfo(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if ( ladder == null ) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        ChatDisplay display = new ChatDisplay(ladder.getName());
        display.addText("&7This ladder contains the following ranks:");

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        
        boolean first = true;
        for (Rank rank : ladder.getRanks()) {
//        	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankPos.getRankId());
        	if ( rank == null ) {
        		continue;
        	}
        	
//            Optional<Rank> rankOptional =
//                PrisonRanks.getInstance().getRankManager().getRankOptional(rankPos.getRankId());
//            if(!rankOptional.isPresent()) {
//                continue; // Skip it
//            }
            
            boolean defaultRank = ("default".equalsIgnoreCase( ladderName ) && first);

            builder.add("&3#%d &8- &3%s %s", rank.getPosition(),
                rank.getName(), 
                (defaultRank ? "&b(&9Default Rank&b) &7-" : "")
            	);
            first = false;
        }

        builder.add( "&3See &f/ranks list &b[ladderName] &3for more details on ranks." );
        
        display.addComponent(builder.build());
        
        display.send(sender);
    }
    
    @Command(identifier = "ranks ladder moveRank", description = "Moves a rank to a new " +
    		"ladder position or a new ladder.", 
			onlyPlayers = false, permissions = "ranks.ladder")
		public void ladderMoveRank(CommandSender sender, 
				@Arg(name = "ladderName") String ladderName,
				@Arg(name = "rankName") String rankName,
				@Arg(name = "position", def = "0", verifiers = "min[0]", 
				description = "Position where you want the rank to be moved to. " +
						"0 is the first position in the ladder.") int position) {
    	sender.sendMessage( "Attempting to remove the specified rank from it's original ladder, " +
    			"then it will be added back to the target ladder at the spcified location. The rank " +
    			"will not be lost." );
    	ladderRemoveRank( sender, ladderName, rankName );
    	ladderAddRank(sender, ladderName, rankName, position );
    }

    @Command(identifier = "ranks ladder addrank", description = "Adds a rank to a ladder, or move a rank.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderAddRank(CommandSender sender, 
    		@Arg(name = "ladderName") String ladderName,
	        @Arg(name = "rankName") String rankName,
	        @Arg(name = "position", def = "0", verifiers = "min[0]",
	        description = "Position where you want the rank to be added. " +
	        		"0 is the first position in the ladder.") int position) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        
        if ( ladder == null ) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRankOptional(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }

        if (ladder.containsRank(rank.getId())) {
            Output.get()
                .sendError(sender, "The ladder '%s' already contains the rank '%s'.", ladderName,
                    rankName);
            return;
        }

        if (position > 0) {
            ladder.addRank(position, rank);
        } else {
            ladder.addRank(rank);
        }

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladder);
            
            Output.get().sendInfo(sender, "Added rank '%s' to ladder '%s' in position %s.", 
            		rank.getName(), ladder.getName(), Integer.toString( position ));
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while adding a rank to your ladder. &8Check the console for details.");
            Output.get().logError("Error while saving ladder.", e);
        }
    }

    @Command(identifier = "ranks ladder delrank", description = "Removes a rank from a ladder.", 
    											onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemoveRank(CommandSender sender, @Arg(name = "ladderName") String ladderName,
        @Arg(name = "rankName") String rankName) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        
        if ( ladder == null ) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRankOptional(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }

        ladder.removeRank(ladder.getPositionOfRank(rank));

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladder);

            Output.get().sendInfo(sender, "Removed rank '%s' from ladder '%s'.", rank.getName(),
            		ladder.getName());
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while removing a rank from your ladder. &8Check the console for details.");
            Output.get().logError("Error while saving ladder.", e);
        }

    }


  @Command(identifier = "ranks ladder perms list", description = "Lists ladder permissions", 
  							onlyPlayers = false, permissions = "ranks.set")
  public void ladderPermsList(CommandSender sender, 
  				@Arg(name = "ladderName", def = "default", 
  						description = "Ladder name to list the permissions.") String ladderName
  			){
	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
	  
      RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
      
      if ( ladder == null ) {
          Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
          return;
      }

      if ( ladder.getPermissions() == null ||ladder.getPermissions().size() == 0 && 
    		  ladder.getPermissionGroups() == null && ladder.getPermissionGroups().size() == 0 ) {
      	
          Output.get().sendInfo(sender, "&3The ladder '&7%s&3' contains no permissions or " +
          		"permission groups.", ladder.getName() );
          return;
      }

      

      ChatDisplay display = new ChatDisplay("Ladder Permissions and Groups for " + ladder.getName());
      display.addText("&8Click the 'Remove' tag to remove it.");
      display.addText("  &3Placeholders: &7{rank}&3 - Rank Name");
      display.addText("  &3All Ladder perms will be applied automatically to all ladder ranks.");
      
      BulletedListComponent.BulletedListBuilder builder =
      									new BulletedListComponent.BulletedListBuilder();

      
      int rowNumber = 1;
      
      if ( ladder.getPermissions().size() > 0 ) {
      	builder.add( "&7Permissions:" );
      }
      for (String perm : ladder.getPermissions() ) {
      	
      	RowComponent row = new RowComponent();
      	
      	row.addTextComponent( "  &3Row: &d%d  ", rowNumber++ );
      	
      	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", perm ) )
      			.command( "/ranks ladder perms edit " + ladder.getName() + " " + rowNumber + " " )
      			.tooltip("Permission - Click to Edit");
      	row.addFancy( msgPermission );
      	
      	
      	FancyMessage msgRemove = new FancyMessage( String.format( "  &cRemove " ) )
      			.command( "/ranks ladder perms remove " + ladder.getName() + " " + rowNumber + " " )
      			.tooltip("Remove Permission - Click to Delete");
      	row.addFancy( msgRemove );
      	
          builder.add( row );
      }

      if ( ladder.getPermissionGroups().size() > 0 ) {
      	builder.add( "&7Permission Groups:" );
      }
      for (String permGroup : ladder.getPermissionGroups() ) {
      	
      	RowComponent row = new RowComponent();
      	
      	row.addTextComponent( "  &3Row: &d%d  ", rowNumber++ );
      	
      	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", permGroup ) )
      			.command( "/ranks ladder perms edit " + ladder.getName() + " " + rowNumber + " " )
      			.tooltip("Permission Group - Click to Edit");
      	row.addFancy( msgPermission );
      	
      	
      	FancyMessage msgRemove = new FancyMessage( String.format( "  &cRemove " ) )
      			.command( "/ranks ladder perms remove " + ladder.getName() + " " + rowNumber + " " )
      			.tooltip("Remove Permission Group - Click to Delete");
      	row.addFancy( msgRemove );
      	
          builder.add( row );
      }

      
      display.addComponent(builder.build());
      display.addComponent(new FancyMessageComponent(
          new FancyMessage("&7[&a+&7] Add Permission")
          			.suggest("/ranks ladder perms addPerm " + ladder.getName() + " [perm] /")
              .tooltip("&7Add a new Permission.")));
      display.addComponent(new FancyMessageComponent(
      		new FancyMessage("&7[&a+&7] Add Permission Group")
      				.suggest("/ranks ladder perms addPermGroup " + ladder.getName() + " [permGroup] /")
      		.tooltip("&7Add a new Permission Group.")));

      display.send(sender);

  }
  

  @Command(identifier = "ranks ladder perms addPerm", 
		  		description = "Add a ladder permission. Valid placeholder: {rank}.", 
		  onlyPlayers = false, permissions = "ranks.set")
  public void ladderPermsAddPerm(CommandSender sender, 
		  @Arg(name = "ladderName", def = "default", 
						description = "Ladder name to add the permission to.") String ladderName,
		  @Arg(name = "permission", description = "Permission") String permission
		  ){
	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
	  
      RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
      
      if ( ladder == null ) {
          Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
          return;
      }

      if ( permission == null || permission.trim().isEmpty() ) {
      	
          Output.get().sendInfo(sender, "&3The &7permission &3parameter is required." );
          return;
      }
      
      
      if ( ladder.hasPermission( permission ) ) {
    	  
    	  Output.get().sendInfo(sender, "&3The permission &7%s &3already exists.", permission );
    	  return;
      }
      
      ladder.getPermissions().add( permission );
      
      boolean saved = PrisonRanks.getInstance().getLadderManager().save( ladder );
      
      if ( saved ) {
    	  
    	  Output.get().sendInfo(sender, "&3The permission &7%s &3was successfully added " +
    			  "to the ladder &7%s&3.", permission, ladder.getName() );
      }
      else {
    	  
    	  Output.get().sendInfo(sender, "&cFailure: &3The permission &7%s &3was unable to " +
    			  "be saved to the ladder &7%s&3. See the console for additional informatio.", 
    			  permission, ladder.getName() );
      }
      
      ladderPermsList( sender, ladder.getName() );
  }
  
  
  @Command(identifier = "ranks ladder perms addGroup", 
		  		description = "Add a ladder permission group. Valid placeholder: {rank}.", 
		  onlyPlayers = false, permissions = "ranks.set")
  public void ladderPermsAddGroup(CommandSender sender, 
		  @Arg(name = "ladderName", def = "default", 
						description = "Ladder name to add the permission group to.") String ladderName,
		  @Arg(name = "permissionGroup", description = "Permission Group") String permissionGroup
		  ){
	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
	  
	  RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
	  
      if ( ladder == null ) {
          Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
          return;
      }

      if ( permissionGroup == null || permissionGroup.trim().isEmpty() ) {
      	
          Output.get().sendInfo(sender, "&3The &7permissionGroup &3parameter is required." );
          return;
      }
      
      
      if ( ladder.hasPermissionGroup( permissionGroup ) ) {
    	  
    	  Output.get().sendInfo(sender, "&3The permission group &7%s &3already exists.", 
    			  			permissionGroup );
    	  return;
      }
      
      ladder.getPermissionGroups().add( permissionGroup );
      
      boolean saved = PrisonRanks.getInstance().getLadderManager().save( ladder );
      
      if ( saved ) {
    	  
    	  Output.get().sendInfo(sender, "&3The permission group &7%s &3was successfully added " +
    	  			"to the ladder &7%s&3.", permissionGroup, ladder.getName() );
      }
      else {
    	  
    	  Output.get().sendInfo(sender, "&cFailure: &3The permission group &7%s &3was unable to " +
    			  	"be saved to the ladder &7%s&3. See the console for additional information.", 
    			  	permissionGroup, ladder.getName() );
      }
      
      ladderPermsList( sender, ladder.getName() );
  }
  
//  Since we are strictly dealing with a single value for perms, editing makes no sense; 
//  just delete the 'bad' perm and re-add it.
//  @Command(identifier = "ranks ladder perms edit", description = "Lists ladder permissions", 
//  							onlyPlayers = false, permissions = "ranks.set")
//  public void ladderPermsEdit(CommandSender sender, 
//		  @Arg(name = "ladderName", def = "default", 
//		  				description = "Ladder name to list the permissions.") String ladderName,
//			@Arg(name = "row") Integer row
//  			){
//	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//	  
//      Optional<RankLadder> ladderOptional =
//              PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
//      if (!ladderOptional.isPresent()) {
//          Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
//          return;
//      }
//
//      RankLadder ladder = ladderOptional.get();
//
//  }
  
  @Command(identifier = "ranks ladder perms remove", description = "Lists ladder permissions", 
		  onlyPlayers = false, permissions = "ranks.set")
  public void ladderPermsRemove(CommandSender sender, 
		  @Arg(name = "ladderName", def = "default", 
						description = "Ladder name to list the permissions.") String ladderName,
			@Arg(name = "row") Integer row
		  ){
	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
	  
      RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
      
      if ( ladder == null ) {
          Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
          return;
      }

      boolean dirty = false;
      String removedPerm = "";
      boolean permGroup = false;
      
      if ( row == null || row <= 0 ) {
      	sender.sendMessage( 
      			String.format("&7Please provide a valid row number greater than zero. " +
      					"Was row=[&b%d&7]",
      					(row == null ? "null" : row) ));
      	return;        	
      }
      
      if ( row <= ladder.getPermissions().size() ) {
    	  removedPerm = ladder.getPermissions().remove( row - 1 );
    	  dirty = true;
      }
      else {
    	  // Remove from row the size of permissions so the row will align to the permissionGroups.
    	  row -= ladder.getPermissions().size();
    	  
    	  if ( row <= ladder.getPermissionGroups().size() ) {
    		  
    		  removedPerm = ladder.getPermissions().remove( row - 1 );
    		  dirty = true;
    		  permGroup = true;
    	  }
      }

      if ( dirty ) {
    	  boolean saved = PrisonRanks.getInstance().getLadderManager().save( ladder );
          
          if ( saved ) {
        	  
        	  Output.get().sendInfo(sender, "&3The permission%s &7%s &3was successfully removed " +
        	  			"to the ladder &7%s&3.",
        	  			( permGroup ? " group" : "" ),
        	  			removedPerm, ladder.getName() );
          }
          else {
        	  
        	  Output.get().sendInfo(sender, "&cFailure: &3The permission%s &7%s &3was unable to " +
        			  	"be saved to the ladder &7%s&3. See the console for additional information.", 
        			  	( permGroup ? " group" : "" ),
        	  			removedPerm, ladder.getName() );
          }
      }
      else {
    	  Output.get().sendInfo(sender, "&3The permission on row &7%s &3was unable to be removed " +
    	  		"from the &7%s &3ladder. " +
  	  			"Is that a valid row number?",
  	  			Integer.toString( row ), ladder.getName() );
      }
  }
    
  
  
}
