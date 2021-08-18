package tech.mcprison.prison.ranks.commands;

/**
 * Ladder perms that are not being used.  May be added later, or just removed.
 *
 */
public abstract class LadderCommandsPerms
	extends LadderCommandsMessages
{

    
	public LadderCommandsPerms( String cmdGroup )
	{
		super( cmdGroup );
	}


// NOT USED:
//  @Command(identifier = "ranks ladder perms list", description = "Lists ladder permissions", 
//  							onlyPlayers = false, permissions = "ranks.set")
//  public void ladderPermsList(CommandSender sender, 
//  				@Arg(name = "ladderName", def = "default", 
//  						description = "Ladder name to list the permissions.") String ladderName
//  			){
//	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//	  
//      RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
//      
//      if ( ladder == null ) {
//    	  ladderDoesNotExistsMsg( sender, ladderName );
//          return;
//      }
//
//      if ( ladder.getPermissions() == null ||ladder.getPermissions().size() == 0 && 
//    		  ladder.getPermissionGroups() == null && ladder.getPermissionGroups().size() == 0 ) {
//      	
//    	  PrisonRanks.getInstance().getRanksMessages()
//				.getLocalizable( "ranks_LadderCommands__ladder_has_no_perms" )
//				.withReplacements( 
//						ladder.getName() )
//				.sendTo( sender );
//          return;
//      }
//
//      
//
//      ChatDisplay display = new ChatDisplay("Ladder Permissions and Groups for " + ladder.getName());
//      display.addText("&8Click the 'Remove' tag to remove it.");
//      display.addText("  &3Placeholders: &7{rank}&3 - Rank Name");
//      display.addText("  &3All Ladder perms will be applied automatically to all ladder ranks.");
//      
//      BulletedListComponent.BulletedListBuilder builder =
//      									new BulletedListComponent.BulletedListBuilder();
//
//      
//      int rowNumber = 1;
//      
//      if ( ladder.getPermissions().size() > 0 ) {
//      	builder.add( "&7Permissions:" );
//      }
//      for (String perm : ladder.getPermissions() ) {
//      	
//      	RowComponent row = new RowComponent();
//      	
//      	row.addTextComponent( "  &3Row: &d%d  ", rowNumber++ );
//      	
//      	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", perm ) )
//      			.command( "/ranks ladder perms edit " + ladder.getName() + " " + rowNumber + " " )
//      			.tooltip("Permission - Click to Edit");
//      	row.addFancy( msgPermission );
//      	
//      	
//      	FancyMessage msgRemove = new FancyMessage( String.format( "  &cRemove " ) )
//      			.command( "/ranks ladder perms remove " + ladder.getName() + " " + rowNumber + " " )
//      			.tooltip("Remove Permission - Click to Delete");
//      	row.addFancy( msgRemove );
//      	
//          builder.add( row );
//      }
//
//      if ( ladder.getPermissionGroups().size() > 0 ) {
//      	builder.add( "&7Permission Groups:" );
//      }
//      for (String permGroup : ladder.getPermissionGroups() ) {
//      	
//      	RowComponent row = new RowComponent();
//      	
//      	row.addTextComponent( "  &3Row: &d%d  ", rowNumber++ );
//      	
//      	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", permGroup ) )
//      			.command( "/ranks ladder perms edit " + ladder.getName() + " " + rowNumber + " " )
//      			.tooltip("Permission Group - Click to Edit");
//      	row.addFancy( msgPermission );
//      	
//      	
//      	FancyMessage msgRemove = new FancyMessage( String.format( "  &cRemove " ) )
//      			.command( "/ranks ladder perms remove " + ladder.getName() + " " + rowNumber + " " )
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
//          			.suggest("/ranks ladder perms addPerm " + ladder.getName() + " [perm] /")
//              .tooltip("&7Add a new Permission.")));
//      display.addComponent(new FancyMessageComponent(
//      		new FancyMessage("&7[&a+&7] Add Permission Group")
//      				.suggest("/ranks ladder perms addPermGroup " + ladder.getName() + " [permGroup] /")
//      		.tooltip("&7Add a new Permission Group.")));
//
//      display.send(sender);
//
//  }
  

//NOT USED:
//  @Command(identifier = "ranks ladder perms addPerm", 
//		  		description = "Add a ladder permission. Valid placeholder: {rank}.", 
//		  onlyPlayers = false, permissions = "ranks.set")
//  public void ladderPermsAddPerm(CommandSender sender, 
//		  @Arg(name = "ladderName", def = "default", 
//						description = "Ladder name to add the permission to.") String ladderName,
//		  @Arg(name = "permission", description = "Permission") String permission
//		  ){
//	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//	  
//      RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
//      
//      if ( ladder == null ) {
//          Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
//          return;
//      }
//
//      if ( permission == null || permission.trim().isEmpty() ) {
//      	
//          Output.get().sendInfo(sender, "&3The &7permission &3parameter is required." );
//          return;
//      }
//      
//      
//      if ( ladder.hasPermission( permission ) ) {
//    	  
//    	  Output.get().sendInfo(sender, "&3The permission &7%s &3already exists.", permission );
//    	  return;
//      }
//      
//      ladder.getPermissions().add( permission );
//      
//      boolean saved = PrisonRanks.getInstance().getLadderManager().save( ladder );
//      
//      if ( saved ) {
//    	  
//    	  Output.get().sendInfo(sender, "&3The permission &7%s &3was successfully added " +
//    			  "to the ladder &7%s&3.", permission, ladder.getName() );
//      }
//      else {
//    	  
//    	  Output.get().sendInfo(sender, "&cFailure: &3The permission &7%s &3was unable to " +
//    			  "be saved to the ladder &7%s&3. See the console for additional informatio.", 
//    			  permission, ladder.getName() );
//      }
//      
//      ladderPermsList( sender, ladder.getName() );
//  }
  
//NOT USED:
//  @Command(identifier = "ranks ladder perms addGroup", 
//		  		description = "Add a ladder permission group. Valid placeholder: {rank}.", 
//		  onlyPlayers = false, permissions = "ranks.set")
//  public void ladderPermsAddGroup(CommandSender sender, 
//		  @Arg(name = "ladderName", def = "default", 
//						description = "Ladder name to add the permission group to.") String ladderName,
//		  @Arg(name = "permissionGroup", description = "Permission Group") String permissionGroup
//		  ){
//	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//	  
//	  RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
//	  
//      if ( ladder == null ) {
//          Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
//          return;
//      }
//
//      if ( permissionGroup == null || permissionGroup.trim().isEmpty() ) {
//      	
//          Output.get().sendInfo(sender, "&3The &7permissionGroup &3parameter is required." );
//          return;
//      }
//      
//      
//      if ( ladder.hasPermissionGroup( permissionGroup ) ) {
//    	  
//    	  Output.get().sendInfo(sender, "&3The permission group &7%s &3already exists.", 
//    			  			permissionGroup );
//    	  return;
//      }
//      
//      ladder.getPermissionGroups().add( permissionGroup );
//      
//      boolean saved = PrisonRanks.getInstance().getLadderManager().save( ladder );
//      
//      if ( saved ) {
//    	  
//    	  Output.get().sendInfo(sender, "&3The permission group &7%s &3was successfully added " +
//    	  			"to the ladder &7%s&3.", permissionGroup, ladder.getName() );
//      }
//      else {
//    	  
//    	  Output.get().sendInfo(sender, "&cFailure: &3The permission group &7%s &3was unable to " +
//    			  	"be saved to the ladder &7%s&3. See the console for additional information.", 
//    			  	permissionGroup, ladder.getName() );
//      }
//      
//      ladderPermsList( sender, ladder.getName() );
//  }
  
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
  
  
//NOT USED:
//  @Command(identifier = "ranks ladder perms remove", description = "Lists ladder permissions", 
//		  onlyPlayers = false, permissions = "ranks.set")
//  public void ladderPermsRemove(CommandSender sender, 
//		  @Arg(name = "ladderName", def = "default", 
//						description = "Ladder name to list the permissions.") String ladderName,
//			@Arg(name = "row") Integer row
//		  ){
//	  sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
//	  
//      RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
//      
//      if ( ladder == null ) {
//          Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
//          return;
//      }
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
//      if ( row <= ladder.getPermissions().size() ) {
//    	  removedPerm = ladder.getPermissions().remove( row - 1 );
//    	  dirty = true;
//      }
//      else {
//    	  // Remove from row the size of permissions so the row will align to the permissionGroups.
//    	  row -= ladder.getPermissions().size();
//    	  
//    	  if ( row <= ladder.getPermissionGroups().size() ) {
//    		  
//    		  removedPerm = ladder.getPermissions().remove( row - 1 );
//    		  dirty = true;
//    		  permGroup = true;
//    	  }
//      }
//
//      if ( dirty ) {
//    	  boolean saved = PrisonRanks.getInstance().getLadderManager().save( ladder );
//          
//          if ( saved ) {
//        	  
//        	  Output.get().sendInfo(sender, "&3The permission%s &7%s &3was successfully removed " +
//        	  			"to the ladder &7%s&3.",
//        	  			( permGroup ? " group" : "" ),
//        	  			removedPerm, ladder.getName() );
//          }
//          else {
//        	  
//        	  Output.get().sendInfo(sender, "&cFailure: &3The permission%s &7%s &3was unable to " +
//        			  	"be saved to the ladder &7%s&3. See the console for additional information.", 
//        			  	( permGroup ? " group" : "" ),
//        	  			removedPerm, ladder.getName() );
//          }
//      }
//      else {
//    	  Output.get().sendInfo(sender, "&3The permission on row &7%s &3was unable to be removed " +
//    	  		"from the &7%s &3ladder. " +
//  	  			"Is that a valid row number?",
//  	  			Integer.toString( row ), ladder.getName() );
//      }
//  }
    
}
