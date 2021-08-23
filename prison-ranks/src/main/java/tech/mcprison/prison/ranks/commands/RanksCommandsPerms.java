package tech.mcprison.prison.ranks.commands;

/**
 * NOTE: Rank permissions is not functional because of some major limitations of bukkit and
 * Vault.  
 * 
 * Therefore they are placed here to keep them out of the main RankCommands class until they can 
 * be enabled, or deleted.
 *
 */
public class RanksCommandsPerms
	extends RanksCommandsMessages 
{

	public RanksCommandsPerms( String cmdGroup ) {
		super( cmdGroup );
	}
	

//  @Command(identifier = "ranks perms list", description = "Lists rank permissions", 
//  							onlyPlayers = false, permissions = "ranks.set")
//  public void rankPermsList(CommandSender sender, 
//  				@Arg(name = "rankName") String rankName
//  			){
//  	sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//	  
//      Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//      if ( rank == null ) {
//      	rankDoesNotExistMsg( sender, rankName );
//          return;
//      }
//      
//      
//      if ( rank.getPermissions() == null ||rank.getPermissions().size() == 0 && 
//      		rank.getPermissionGroups() == null && rank.getPermissionGroups().size() == 0 ) {
//      	
//          Output.get().sendInfo(sender, "The Rank '%s' contains no permissions or " +
//          		"permission groups.", rank.getName());
//          return;
//      }
//
//      RankLadder ladder = rank.getLadder();
//
//      ChatDisplay display = new ChatDisplay("Rank Permissions and Groups for " + rank.getName());
//      display.addText("&8Click a Permission to remove it.");
//      BulletedListComponent.BulletedListBuilder builder =
//      									new BulletedListComponent.BulletedListBuilder();
//
//      listLadderPerms( ladder, builder );
//      
//      int rowNumber = 1;
//      
//      if ( rank.getPermissions().size() > 0 ) {
//      	builder.add( "&7Permissions:" );
//      }
//      for (String perm : rank.getPermissions() ) {
//      	
//      	RowComponent row = new RowComponent();
//      	
//      	row.addTextComponent( "  &3Row: &d%d  ", rowNumber++ );
//      	
//      	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", perm ) )
//      			.command( "/ranks perms edit " + rank.getName() + " " + rowNumber + " " )
//      			.tooltip("Permission - Click to Edit");
//      	row.addFancy( msgPermission );
//      	
//      	
//      	FancyMessage msgRemove = new FancyMessage( String.format( "  &cRemove " ) )
//      			.command( "/ranks perms remove " + rank.getName() + " " + rowNumber + " " )
//      			.tooltip("Remove Permission - Click to Delete");
//      	row.addFancy( msgRemove );
//      	
//          builder.add( row );
//      }
//
//      if ( rank.getPermissionGroups().size() > 0 ) {
//      	builder.add( "&7Permission Groups:" );
//      }
//      for (String permGroup : rank.getPermissionGroups() ) {
//      	
//      	RowComponent row = new RowComponent();
//      	
//      	row.addTextComponent( "  &3Row: &d%d  ", rowNumber++ );
//      	
//      	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", permGroup ) )
//      			.command( "/ranks perms edit " + rank.getName() + " " + rowNumber + " " )
//      			.tooltip("Permission Group - Click to Edit");
//      	row.addFancy( msgPermission );
//      	
//      	
//      	FancyMessage msgRemove = new FancyMessage( String.format( "  &cRemove " ) )
//      			.command( "/ranks perms remove " + rank.getName() + " " + rowNumber + " " )
//      			.tooltip("Remove Permission Group - Click to Delete");
//      	row.addFancy( msgRemove );
//      	
//          builder.add( row );
//      }
//
//      
//      display.addComponent(builder.build());
//      display.addComponent(new FancyMessageComponent(
//          new FancyMessage("&7[&a+&7] Add Permission")
//          			.suggest("/ranks perms addPerm " + rank.getName() + " [perm] /")
//              .tooltip("&7Add a new Permission.")));
//      display.addComponent(new FancyMessageComponent(
//      		new FancyMessage("&7[&a+&7] Add Permission Group")
//      				.suggest("/ranks perms addPermGroup " + rank.getName() + " [permGroup] /")
//      		.tooltip("&7Add a new Permission Group.")));
//
//      display.send(sender);
//
//  }
  
//  private void listLadderPerms( RankLadder ladder, 
//  					BulletedListComponent.BulletedListBuilder builder ) {
//  	
//      if ( ladder.getPermissions().size() > 0 ) {
//        	builder.add( "&3Ladder &7%s &3Permissions:", ladder.getName() );
//      }
//      for (String perm : ladder.getPermissions() ) {
//        	
//        	RowComponent row = new RowComponent();
//        	
//        	row.addTextComponent( "    " );
//        	
//        	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", perm ) )
//        			.command( "/ranks ladder perms list " + ladder.getName() )
//        			.tooltip("Ladder Permission - Click to List Ladder");
//        	row.addFancy( msgPermission );
//        	
//          builder.add( row );
//      }
//
//      if ( ladder.getPermissionGroups().size() > 0 ) {
//        	builder.add( "&3Ladder &7%s &3Permission Groups:", ladder.getName() );
//      }
//      for (String permGroup : ladder.getPermissionGroups() ) {
//        	
//        	RowComponent row = new RowComponent();
//        	
//        	row.addTextComponent( "    " );
//        	
//        	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", permGroup ) )
//        			.command( "/ranks ladder perms list " + ladder.getName() )
//        			.tooltip("Ladder Permission Group - Click to List Ladder");
//        	row.addFancy( msgPermission );
//        	
//        	builder.add( row );
//      }
//
//  }
  

//  @Command(identifier = "ranks perms addPerm", 
//		  		description = "Add a ladder permission. Valid placeholder: {rank}.", 
//		  onlyPlayers = false, permissions = "ranks.set")
//  public void rankPermsAddPerm(CommandSender sender, 
//		  @Arg(name = "rankName", 
//						description = "Rank name to add the permission to.") String rankName,
//		  @Arg(name = "permission", description = "Permission") String permission
//		  ){
//  	
//  	sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//	  
//      Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//      if ( rank == null ) {
//      	rankDoesNotExistMsg( sender, rankName );
//          return;
//      }
//      
//
//      if ( permission == null || permission.trim().isEmpty() ) {
//      	
//          Output.get().sendInfo(sender, "&3The &7permission &3parameter is required." );
//          return;
//      }
//      
//      
//      if ( rank.hasPermission( permission ) ) {
//    	  
//    	  Output.get().sendInfo(sender, "&3The permission &7%s &3already exists.", permission );
//    	  return;
//      }
//      
//      rank.getPermissions().add( permission );
//      
//      
//      PrisonRanks.getInstance().getRankManager().saveRank(rank);
//
//      Output.get().sendInfo(sender, "&3The permission &7%s &3was successfully added " +
//      		"to the rank &7%s&3.", permission, rank.getName() );
//      
//      rankPermsList( sender, rank.getName() );
//  }
  
  
//  @Command(identifier = "ranks perms addGroup", 
//  		description = "Add a ladder permission. Valid placeholder: {rank}.", 
//  		onlyPlayers = false, permissions = "ranks.set")
//  public void rankPermsAddPermGroup(CommandSender sender, 
//  		@Arg(name = "rankName", 
//  					description = "Rank name to add the permission to.") String rankName,
//  		@Arg(name = "permissionGroup", description = "Permission Group") String permissionGroup
//  	){
//  	
//  	sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//  	
//  	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//  	if ( rank == null ) {
//  		rankDoesNotExistMsg( sender, rankName );
//  		return;
//  	}
//  	
//  	
//  	if ( permissionGroup == null || permissionGroup.trim().isEmpty() ) {
//  		
//  		Output.get().sendInfo(sender, "&3The &7permission group &3parameter is required." );
//  		return;
//  	}
//  	
//  	
//  	if ( rank.hasPermissionGroup( permissionGroup ) ) {
//  		
//  		Output.get().sendInfo(sender, "&3The permission Group &7%s &3already exists.", permissionGroup );
//  		return;
//  	}
//  	
//  	rank.getPermissionGroups().add( permissionGroup );
//  	
//  	
//  	PrisonRanks.getInstance().getRankManager().saveRank(rank);
//  	
//  	Output.get().sendInfo(sender, "&3The permission group &7%s &3was successfully added " +
//  			"to the rank &7%s&3.", permissionGroup, rank.getName() );
//  	
//  	rankPermsList( sender, rank.getName() );
//  }
  


//  @Command(identifier = "ranks perms remove", description = "Remove rank permissions", 
//		  onlyPlayers = false, permissions = "ranks.set")
//  public void rankPermsRemove(CommandSender sender, 
//  		@Arg(name = "rankName", def = "default", 
//						description = "Rank name to list the permissions.") String rankName,
//			@Arg(name = "row") Integer row
//		  ){
//  	sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//	  
//     	
//  	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//  	if ( rank == null ) {
//  		rankDoesNotExistMsg( sender, rankName );
//  		return;
//  	}
//  	
//	  
//      boolean dirty = false;
//      String removedPerm = "";
//      boolean permGroup = false;
//      
//      if ( row == null || row <= 0 ) {
//      	sender.sendMessage( 
//      			String.format("&7Please provide a valid row number greater than zero. " +
//      					"Was row=[&b%d&7]",
//      					(row == null ? "null" : row) ));
//      	return;        	
//      }
//      
//      if ( row <= rank.getPermissions().size() ) {
//    	  removedPerm = rank.getPermissions().remove( row - 1 );
//    	  dirty = true;
//      }
//      else {
//    	  // Remove from row the size of permissions so the row will align to the permissionGroups.
//    	  row -= rank.getPermissions().size();
//    	  
//    	  if ( row <= rank.getPermissionGroups().size() ) {
//    		  
//    		  removedPerm = rank.getPermissions().remove( row - 1 );
//    		  dirty = true;
//    		  permGroup = true;
//    	  }
//      }
//
//      if ( dirty ) {
//      	PrisonRanks.getInstance().getRankManager().saveRank(rank);
//      	
//      	Output.get().sendInfo(sender, "&3The permission%s &7%s &3was successfully removed " +
//      			"to the rank &7%s&3.",
//      			( permGroup ? " group" : "" ),
//      			removedPerm, rank.getName() );
//         
//      }
//      else {
//    	  Output.get().sendInfo(sender, "&3The permission on row &7%s &3was unable to be removed " +
//    	  		"from the &7%s &3rank. " +
//  	  			"Is that a valid row number?",
//  	  			Integer.toString( row ), rank.getName() );
//      }
//  }
  
	// @Command(identifier = "ranks playerInventory", permissions = "mines.set",
	// description = "For listing what's in a player's inventory by dumping it
	// to console.",
	// onlyPlayers = false )
//	public void ranksPlayerInventoryCommand( CommandSender sender,
//			@Arg( name = "player", def = "", description = "Player name" ) String playerName )
//	{
//
//		Player player = getPlayer( sender, playerName );
//
//		if ( player == null )
//		{
//			sender.sendMessage( "&3You must be a player in the game to run this command, and/or the player must be online." );
//			return;
//		}
//
//		// Player player = getPlayer( sender );
//		//
//		// if (player == null || !player.isOnline()) {
//		// sender.sendMessage( "&3You must be a player in the game to run this
//		// command." );
//		// return;
//		// }
//
//		player.printDebugInventoryInformationToConsole();
//	}


}
