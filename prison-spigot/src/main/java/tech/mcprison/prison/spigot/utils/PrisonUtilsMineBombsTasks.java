package tech.mcprison.prison.spigot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.ParticleDisplay;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombEffectsData;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.bombs.MineBombEffectsData.EffectState;
import tech.mcprison.prison.bombs.MineBombs.ExplosionOrientation;
import tech.mcprison.prison.bombs.MineBombs.ExplosionShape;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.ExplosiveBlockBreakEvent;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;
import tech.mcprison.prison.util.Location;

public class PrisonUtilsMineBombsTasks
	extends PrisonUtils
{
	public static final Map<String, Integer> playerCooldowns = new TreeMap<>();
	

	public PrisonUtilsMineBombsTasks() {
		super();
		
		initialize();
	}
	
	@Override
	protected Boolean initialize()
	{

		return null;
	}

	
	protected static void setFortune( SpigotItemStack itemInHand, int fortuneLevel )
	{

		if ( itemInHand != null && itemInHand.getBukkitStack() != null && itemInHand.getBukkitStack().hasItemMeta() )
		{

			itemInHand.getBukkitStack().addUnsafeEnchantment( Enchantment.LOOT_BONUS_BLOCKS, fortuneLevel );

			// ItemMeta meta = itemInHand.getBukkitStack().getItemMeta();
			// meta.addEnchant( Enchantment.LOOT_BONUS_BLOCKS, fortuneLevel,
			// true );
			// itemInHand.getBukkitStack().setItemMeta( meta );

		}

	}

	// public static boolean addPlayerCooldown( String playerUUID ) {
	// return addPlayerCooldown( playerUUID, MINE_BOMBS_COOLDOWN_TICKS );
	// }

	/**
	 * <p>
	 * If a cooldown is not setup for the player, this will try to add one and
	 * will return a value of true to indicate the a cooldown was set. If a
	 * cooldown already exists, then this will return a value of false.
	 * </p>
	 * 
	 * @param playerUUID
	 * @param ticks
	 * @return
	 */
	public static boolean addPlayerCooldown( String playerUUID, int ticks )
	{
		boolean results = false;

		if ( !playerCooldowns.containsKey( playerUUID ) || playerCooldowns.get( playerUUID ) <= 0 )
		{

			playerCooldowns.put( playerUUID, ticks );

			new BukkitRunnable()
			{

				@Override
				public void run()
				{

					int ticksRemaining = playerCooldowns.get( playerUUID ) - 10;

					if ( ticksRemaining <= 0 )
					{
						playerCooldowns.remove( playerUUID );
						this.cancel();
					}
					else
					{
						playerCooldowns.put( playerUUID, ticksRemaining );
					}

				}
			}.runTaskTimer( SpigotPrison.getInstance(), 10, 10 );

			results = true;
		}

		return results;
	}

	public static int checkPlayerCooldown( String playerUUID )
	{
		int results = 0;

		if ( playerCooldowns.containsKey( playerUUID ) )
		{
			results = playerCooldowns.get( playerUUID );
		}

		return results;
	}

	
	public static void submitBombEffects( MineBombData bomb, EffectState effectState, 
			Location location, Item droppedBomb ) {
		
		long lastTick = -1;
		
		// If running MC 1.9.0 or higher, then can use the glowing feature.  Ignore for 1.8.x.
		boolean is18 = new BluesSpigetSemVerComparator().compareMCVersionTo( "1.9.0" ) < 0 ;
		
		SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
		org.bukkit.Location bLocation = sBlock.getWrapper().getLocation();
		
		for ( MineBombEffectsData effect : bomb.getSoundEffects() )
		{
			if ( effect.getEffectState() == effectState ) {
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						try {
							
							XSound xSound = XSound.matchXSound( effect.getEffectName() ).orElse( null );
							
							if ( xSound != null ) {
								xSound.play( bLocation, effect.getVolumne(), effect.getPitch() );
							}
						}
						catch ( IllegalArgumentException |
								NullPointerException e ) {
							Output.get().logWarn( 
									String.format( 
											"Invalid Sound Effect value in bomb %s: %s  Error: [%s]",
											bomb.getName(),
											effect.toString(), e.getMessage()) );
						}
					}
				}.runTaskLater( SpigotPrison.getInstance(), effect.getOffsetTicks() );
			}
			
			if ( effect.getEffectState() == EffectState.finished && effect.getOffsetTicks() > lastTick ) {
				lastTick = effect.getOffsetTicks();
			}
		}
		
		for ( MineBombEffectsData effect : bomb.getVisualEffects() )
		{
			if ( effect.getEffectState() == effectState ) {
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						
						try {
							
							if ( is18 ) {
								
								Effect eff18 = Effect.valueOf( effect.getEffectName() );
								
								if ( eff18 != null ) {
									bLocation.getWorld().playEffect( bLocation, eff18, 1.0f );
								}
							}
							else {
								Particle particle = Particle.valueOf( effect.getEffectName() );
								if ( particle != null ) {
									
									ParticleDisplay.display( bLocation, particle );
								}
							}
						}
						catch ( IllegalArgumentException |
								NullPointerException e ) {
							Output.get().logWarn( 
									String.format( 
											"Invalid Visual Effect value in bomb %s: %s  Error: [%s]",
											bomb.getName(),
											effect.toString(), e.getMessage()) );
						}
						
					}
				}.runTaskLater( SpigotPrison.getInstance(), effect.getOffsetTicks() );

			}
			
			if ( effect.getEffectState() == EffectState.finished && effect.getOffsetTicks() > lastTick ) {
				lastTick = effect.getOffsetTicks();
			}

		}
		
		// After the last sound and visual is played, then remove the dropped bomb:
		if ( effectState == EffectState.finished && lastTick >= 0 ) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					droppedBomb.remove();
				}
			}.runTaskLater( SpigotPrison.getInstance(), lastTick + 1 );
		}
	}
	
	public static boolean setoffBombDelayed( SpigotPlayer sPlayer, MineBombData bomb, Item droppedBomb, 
						SpigotBlock targetBlock ) {
		boolean results = false;
		
		final Location location = ( targetBlock.getLocation() == null ? 
										sPlayer.getLocation() : targetBlock.getLocation() );
		
		
		submitBombEffects( bomb, EffectState.placed, location, droppedBomb );
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				// Remove the item that the player threw:
//				droppedBomb.remove();
				
				submitBombEffects( bomb, EffectState.explode, location, droppedBomb );
				
				MineBombs mBombs = MineBombs.getInstance();
				
				List<org.bukkit.block.Block> blocks = calculatBlocksForExplosion( bomb, location, mBombs );
				
				
//				SpigotBlock targetBlock = (SpigotBlock) world.getBlockAt( location );
				
				
				ExplosiveBlockBreakEvent explodeEvent = new ExplosiveBlockBreakEvent( 
						targetBlock.getWrapper(), sPlayer.getWrapper(), blocks );
				explodeEvent.setTriggeredBy( bomb.getName() );
				explodeEvent.setMineBomb( bomb );
				
				// Set the toolInHand that needs to be used for the bomb:
				// Default to a diamond pickaxe if not specified.
				// The bomb must define a tool, otherwise auto features will
				// use the mine bombs that the player is holding, or if it was their
				// last one, it would be just AIR:
				String toolInHandName = bomb.getToolInHandName();
				if ( toolInHandName == null || toolInHandName.trim().isEmpty() ) {
						XMaterial xMat = XMaterial.DIAMOND_PICKAXE;
						toolInHandName = xMat.name();
				}
				
				XMaterial xMatTool = XMaterial.matchXMaterial( toolInHandName )
											.orElse( XMaterial.DIAMOND_PICKAXE );
				SpigotItemStack toolInHand = new SpigotItemStack( xMatTool.parseItem() );
				
				setFortune( toolInHand, bomb.getToolInHandFortuneLevel() );
				
				
				explodeEvent.setToolInHand( toolInHand );
				
				
				// Mine bombs should not calculate durability:
				explodeEvent.setCalculateDurability( false );
				
				
				// Normally the explosion will ONLY work if the center target block was non-AIR.
				// This setting allows the explosion to be processed even if it is air.
				explodeEvent.setForceIfAirBlock( true );
				
				Bukkit.getServer().getPluginManager().callEvent( explodeEvent );

				// We want the event to be cancelled since that means prison processed it:
				if ( explodeEvent.isCancelled() ) {
					
					// Run all of the EffectState.finished effects:
					submitBombEffects( bomb, EffectState.finished, location, droppedBomb );
					
					if ( Output.get().isDebug() ) {
						Output.get().logDebug( "Mine Bomb's ExplosiveBlockBreakEvent has been canceled. " +
								"It may have been processed successfully." );
					}
				}
				else {
					// If it wasn't canceled, then it may not have been handled
					
					droppedBomb.remove();
					
					if ( Output.get().isDebug() ) {
						Output.get().logDebug( "Mine Bomb's ExplosiveBlockBreakEvent has NOT been canceled." );
					}
				}

				
			}

			private List<org.bukkit.block.Block> calculatBlocksForExplosion( MineBombData bomb, Location location,
					MineBombs mBombs )
			{
				List<org.bukkit.block.Block> blocks = new ArrayList<>();
				
				// Calculate all the locations that are included in the explosion:
				List<Location> blockLocations = null;

				
				ExplosionShape shape = ExplosionShape.fromString( bomb.getExplosionShape() );
				
				switch ( shape )
				{
					case cube:
						{
							blockLocations = mBombs.calculateCube( location, 
									bomb.getRadius() );
							break;
						}
					
					case ring_x:
					{
						blockLocations = mBombs.calculateSphere( location, 
								bomb.getRadius(), true, bomb.getRadiusInner(),
								ExplosionOrientation.x_axis );
						break;
					}
					case ring_y:
					{
						blockLocations = mBombs.calculateSphere( location, 
								bomb.getRadius(), true, bomb.getRadiusInner(),
								ExplosionOrientation.y_axis );
						break;
					}
					case ring_z:
					{
						blockLocations = mBombs.calculateSphere( location, 
								bomb.getRadius(), true, bomb.getRadiusInner(),
								ExplosionOrientation.z_axis );
						break;
					}

					
					case disk_x:
						{
							blockLocations = mBombs.calculateSphere( location, 
									bomb.getRadius(), false, 0,
									ExplosionOrientation.x_axis );
							break;
						}
					case disk_y:
						{
							blockLocations = mBombs.calculateSphere( location, 
									bomb.getRadius(), false, 0,
									ExplosionOrientation.y_axis );
							break;
						}
					case disk_z:
						{
							blockLocations = mBombs.calculateSphere( location, 
									bomb.getRadius(), false, 0,
									ExplosionOrientation.z_axis );
							break;
						}
						
						
					
					case sphereHollow:
					{
						blockLocations = mBombs.calculateSphere( location, 
								bomb.getRadius(), true, bomb.getRadiusInner() );
						break;
					}
					
					case sphere:
					default:
					{
						blockLocations = mBombs.calculateSphere( location, 
								bomb.getRadius(), false );
						break;
					}
				}

				
				SpigotWorld world = (SpigotWorld) location.getWorld();

				
				// Honor the percent chance for including the block:
				double removalChance = bomb.getRemovalChance();
				Random random = new Random();
				
				
				// Convert to spigot blocks:
				for ( Location bLocation : blockLocations ) {
					double chance = random.nextDouble() * 100.0d;
					
					if ( chance <= removalChance ) {
						
						SpigotBlock sBlock = (SpigotBlock) world.getBlockAt( bLocation );
						if ( !sBlock.isEmpty() ) {
							
							blocks.add( sBlock.getWrapper() );
						}
					}
				}
				return blocks;
			}
		}.runTaskLater( SpigotPrison.getInstance(), bomb.getFuseDelayTicks() );
		
		//.runTaskTimer( SpigotPrison.getInstance(), 10, 10);
		
		return results;
	}

}
