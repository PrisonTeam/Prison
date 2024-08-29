/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.bombs.MineBombs.AnimationArmorStandItemLocation;
import tech.mcprison.prison.internal.ArmorStand;
import tech.mcprison.prison.internal.Entity;
import tech.mcprison.prison.internal.EntityType;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.PrisonStatsElapsedTimeNanos;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotBlockGetAtLocation;
import tech.mcprison.prison.spigot.block.SpigotBlockSetAsynchronously;
import tech.mcprison.prison.spigot.block.SpigotBlockSetSynchronously;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.entity.SpigotArmorStand;
import tech.mcprison.prison.spigot.game.entity.SpigotEntity;
import tech.mcprison.prison.spigot.game.entity.SpigotEntityType;
import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotWorld 
		implements World {

    private org.bukkit.World bukkitWorld;
    
    private SpigotBlockSetAsynchronously setBlockAsync;
    private SpigotBlockSetSynchronously setBlockSync;
    private SpigotBlockGetAtLocation getBlockAtLocation;

    public SpigotWorld(org.bukkit.World bukkitWorld) {
        this.bukkitWorld = bukkitWorld;
    }

    @Override public String getName() {
        return bukkitWorld.getName();
    }

    @Override public List<Player> getPlayers() {
        return Bukkit.getServer().getOnlinePlayers().stream()
            .filter(player -> 
            		player.getWorld().getName().equals(
            					bukkitWorld.getName()))
            .map((Function<org.bukkit.entity.Player, SpigotPlayer>) SpigotPlayer::new)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Entity> getEntities() {
    	List<Entity> results = new ArrayList<>();
    	
    	for (org.bukkit.entity.Entity bukkitEnitity : getWrapper().getEntities() ) {
			
    		results.add( new SpigotEntity( bukkitEnitity) );
		}
    	
    	return results;
    }
    
    /**
     * Filters the returned Entities by the selected EntityType.
     * 
     * @param eType
     * @return
     */
    public List<Entity> getEntities( EntityType eType ) {
    	List<Entity> results = new ArrayList<>();
    	
    	SpigotEntityType seType = eType == null ? null : (SpigotEntityType) eType;
    	
    	for (org.bukkit.entity.Entity bukkitEnitity : getWrapper().getEntities() ) {
    		
    		if ( seType == null ||
					seType.getxEType() == XEntityType.of(bukkitEnitity) ) {
				
				results.add( new SpigotEntity( bukkitEnitity ));
			}
    	}
    	
    	return results;
    }

    /**
     * <p>This should be the ONLY usage in the whole Prison plugin that gets the 
     * bukkit block from the world and converts it to a SpigotBlock..
     * </p>
     * 
     * <p>This gets the actual block from the world, but it only reads, and does not 
     * update.  I cannot say this is safe to run asynchronously, but so far I have
     * not see any related problems when it is.
     * 
     */
    @Override 
    public Block getBlockAt( Location location, boolean containsCustomBlocks ) {
    	
    	if ( getBlockAtLocation == null ) {
    		getBlockAtLocation = new SpigotBlockGetAtLocation();
    	}
    	
    	return getBlockAtLocation.getBlockAt(location, containsCustomBlocks, this);
    }
    
    public Block getBlockAt( Location location ) {
    	return getBlockAt( location, false );
    }
 
    
//    public SpigotBlock getSpigotBlockAt(Location location) {
//    	return new SpigotBlock(
//    			bukkitWorld.getBlockAt(SpigotUtil.prisonLocationToBukkit(location)));
//    }
    
    public org.bukkit.Location getBukkitLocation(Location location) {
    	return SpigotUtil.prisonLocationToBukkit(location);
    }
    
    public org.bukkit.inventory.ItemStack getBukkitItemStack( ItemStack itemStack ) {
    	
    	SpigotItemStack sItemStack = (SpigotItemStack) itemStack;
    	
    	return sItemStack.getBukkitStack();
    }
    
    @Override
    public void setBlock( PrisonBlock block, int x, int y, int z ) {
    	
    	Location loc = new Location( this, x, y, z );
    	org.bukkit.block.Block bukkitBlock = 
    			bukkitWorld.getBlockAt(SpigotUtil.prisonLocationToBukkit(loc));
    	
    	SpigotCompatibility.getInstance().updateSpigotBlock( block, bukkitBlock );
    }
    
    
    /**
     * <p>This function should be called from an async task, and it will
     * drop down in to the synchronous thread to first get the block 
     * from the world, then it will change to the specified PrisonBlock type.
     * </p>
     * 
     * @param prisonBlock
     * @param location
     */
	public void setBlockAsync( PrisonBlock prisonBlock, Location location ) {
		
		if ( setBlockAsync == null ) {
			setBlockAsync = new SpigotBlockSetAsynchronously();
		}
		setBlockAsync.setBlockAsync(prisonBlock, location);
		
	}
	
	
	/**
	 * <p>This list of blocks to be updated, should be ran from an asynchronous thread.
	 * This function will take it's code block and run it in bukkit's synchronous 
	 * thread so the block updates will be thread safe.
	 * </p>
	 * 
	 * <p>The MineTargetPrisonBlock List should be a fairly short list of blocks that
	 * will be updated in one synchronous slice.
	 * </p>
	 * 
	 */
	@Override
	public void setBlocksSynchronously( List<MineTargetPrisonBlock> tBlocks, MineResetType resetType, 
			PrisonStatsElapsedTimeNanos nanos ) {
		
		if ( setBlockSync == null ) {
			setBlockSync = new SpigotBlockSetSynchronously();
		}
		setBlockSync.setBlocksSynchronously(tBlocks, resetType, nanos, this );
		
	}

	
//	public String getBlockSignature( Location location ) {
//		String results = null;
//		
//		if ( getWrapper() != null ) {
//			
//			SpigotBlock block = (SpigotBlock) getBlockAt( location );
//			
//			 StringBuilder sb = new StringBuilder();
//			 sb.append( block.getWrapper().getType().name() )
//			 	.append( ":" )
////			 	.append( location.toWorldCoordinates() )
////			 	.append( "::" )
//			 	.append( block.getWrapper().getBlockData() );
//			 
//			 results = sb.toString();
//		}
//		
//		return results;
//	}
	
//	public void getTestBlock() {
//		
//		PrisonNBTUtil nbtUtil = new PrisonNBTUtil();
//		
//		NBTItem nbtItemStack = nbtUtil.getNBT( bstack );
//		
//		nbtItemStack.
//	}
	
//	public void setBlockFromString( String blockString, Location location ) {
//		
//		String[] parts = blockString.split("::");
//		String blockNameFormal = parts[0];
//		String worldCoordinates = parts[1];
//		String blockData = parts[2];
//		
//		Location targetLocation = location;
//		if ( targetLocation == null ) {
//			targetLocation = Location.decodeWorldCoordinates(worldCoordinates);
//		}
//		
//		SpigotBlock block = (SpigotBlock) getBlockAt( targetLocation );
//		
//		Prison.get().getPlatform().getPrisonBlock(blockNameFormal);
////		block.setType(Material.getMaterial(parts[0]));
//		
//		BlockData targetBlockData = 
//				SpigotPrison.getInstance().getServer().createBlockData( blockData );
//		
//		block.getWrapper().setBlockData( targetBlockData );
//		
//	}
	
	
    public org.bukkit.World getWrapper() {
        return bukkitWorld;
    }

	@Override
	public Entity spawnEntity( Location location, EntityType entityType) {
		
		return new SpigotEntity( spawnBukkitEntity( location, entityType ) );
	}
	
	public org.bukkit.entity.Entity spawnBukkitEntity( Location location, EntityType entityType) {
		
		SpigotLocation sLocation =
				( location instanceof SpigotLocation ? 
						(SpigotLocation) location : 
							new SpigotLocation( location ));
		
		SpigotEntityType sEtityType = SpigotEntityType.getSpigotEntityType( entityType );
		
		org.bukkit.entity.Entity bEntity = 
				((SpigotWorld) sLocation.getWorld()).getWrapper().spawnEntity( 
						sLocation.getBukkitLocation(), sEtityType.getbEntityType() );
		
		return bEntity;
	}

	@Override
	public ArmorStand spawnArmorStand(Location location) {
		
		org.bukkit.entity.ArmorStand armorStand = spawnBukkitArmorStand( location );
				
		SpigotArmorStand sArmorStand = new SpigotArmorStand( armorStand );
		
		return sArmorStand;
	}
	
	public org.bukkit.entity.ArmorStand spawnBukkitArmorStand( Location location ) {
		
		int maxHight = location.getWorld().getMaxHeight();
		
		Location spawnPoint = new Location( location );
		spawnPoint.setY(maxHight);
		
//		Entity e = spawnEntity( spawnPoint, SpigotEntityType.ENTITY_TYPE_ARMOR_STAND );
//		SpigotEntity sEntity = (SpigotEntity) e;
		
		org.bukkit.entity.Entity bEntity = spawnBukkitEntity( spawnPoint, SpigotEntityType.ENTITY_TYPE_ARMOR_STAND );
		
		org.bukkit.entity.ArmorStand armorStand = (org.bukkit.entity.ArmorStand) bEntity;
		armorStand.setVisible(false);
		
		armorStand.teleport( new SpigotLocation( location ).getBukkitLocation() );
		
//		SpigotArmorStand sArmorStand = new SpigotArmorStand( armorStand );
		
//		sArmorStand.teleport(location);
		
//		testArmorStandPlacement( spawnPoint );
		
		return armorStand;
	}
	
	
	
	@Override
	public ArmorStand spawnArmorStand( Location location, String itemType, 
						AnimationArmorStandItemLocation asLocation ) {
		
		
		// NOTE: Once spawned, the armor stand is not being teleported back to the
		//       intended location.  It was being spawned at a different location
		//       because it was "flashing" as visible.
//		int maxHight = location.getWorld().getMaxHeight();
		
//		Location spawnPoint = new Location( location );
//		spawnPoint.setY(maxHight);

		
		
		org.bukkit.inventory.ItemStack bItemStack = null;
		
//		Location spawnPoint = new Location( location );
//		spawnPoint.setX( spawnPoint.getX() + 2 );
//		spawnPoint.setZ( spawnPoint.getZ() + 2 );
		
		
		org.bukkit.entity.Entity bEntity = spawnBukkitEntity( location, SpigotEntityType.ENTITY_TYPE_ARMOR_STAND );
		org.bukkit.entity.ArmorStand as = (org.bukkit.entity.ArmorStand) bEntity;
		
		as.setVisible( false );
		as.setBasePlate( false );
		as.setCanPickupItems( false );
//		as.setInvulnerable( true );
//		as.setGravity( false );
		
		
//		if ( customName == null ) {
//			as.setCustomNameVisible( false );
//		}
//		else {
//			as.setCustomNameVisible( true );
//			as.setCustomName( customName );
//		}
	
		
		if ( itemType == null || itemType.trim().length() == 0 ) {
			as.setArms( false );
			
		}
		else {
			
			XMaterial xMat = XMaterial.matchXMaterial( itemType ).orElse( null );
			bItemStack = xMat == null ? null : xMat.parseItem();
			
			if ( bItemStack == null ) {
				
				bItemStack = XMaterial.COBBLESTONE.parseItem();
			}
			
			
			if ( asLocation == AnimationArmorStandItemLocation.hand ) {
				
				as.setItemInHand(bItemStack);
				as.setArms( true );
				
//				as.setItem( EquipmentSlot.HAND, bItemStack);
			}
			else {
				as.setHelmet(bItemStack);
				
			}

			
		}
		
		as.getHelmet();
		as.setHelmet(bItemStack);
		
//		String msg = "SpigotWorld.spawnArmorStand: itemInHand: " +
//				( as.getItemInHand() == null ? "null" : as.getItemInHand().toString()) +  
//				"  hasArmms: " + as.hasArms() +
//				"  itemStack( " + 
//						(bItemStack == null ? "null" : 
//							bItemStack.toString() + " " + bItemStack.getAmount()) + ") " +
//						" name: " + (customName == null ? "noCustomName" : customName);
//		
//		Output.get().logInfo( msg );
		
//		as.teleport( SpigotLocation.getBukkitLocation(location) );
		
		
		// wrap in a SpigotArmorStand:
		SpigotArmorStand sas = new SpigotArmorStand( as );
		//sas.teleport( location );
		
		return sas;
	}
	
	/**
	 * This creates a new instance of a SpigotWorld object based upon the world's name.
	 * This uses the 'org.bukkit.World.getWorld( worldName );' to return a bukkit world, 
	 * then wrap it in a SpigotWorld object.
	 * 
	 * @param worldName
	 * @return
	 */
	public static SpigotWorld getWorld(String worldName) {
		
		org.bukkit.World world = Bukkit.getWorld( worldName );
		
		return new SpigotWorld( world );
	}


    public int getMaxHeight() {
    	return getWrapper().getMaxHeight();
    }

}
