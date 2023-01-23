package tech.mcprison.prison.spigot.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.ParticleDisplay;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombEffectsData;
import tech.mcprison.prison.bombs.MineBombEffectsData.EffectState;
import tech.mcprison.prison.bombs.MineBombs;
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
import tech.mcprison.prison.util.Text;

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

	
	protected void setFortune( SpigotItemStack itemInHand, int fortuneLevel ) {

		if ( itemInHand != null && itemInHand.getBukkitStack() != null ) {

			itemInHand.getBukkitStack().addUnsafeEnchantment( Enchantment.LOOT_BONUS_BLOCKS, fortuneLevel );

			// ItemMeta meta = itemInHand.getBukkitStack().getItemMeta();
			// meta.addEnchant( Enchantment.LOOT_BONUS_BLOCKS, fortuneLevel,
			// true );
			// itemInHand.getBukkitStack().setItemMeta( meta );
		}
	}
	
	protected void setUnbreaking( SpigotItemStack itemInHand, int durabilityLevel ) {
		
		if ( itemInHand != null && itemInHand.getBukkitStack() != null ) {
			
			itemInHand.getBukkitStack().addUnsafeEnchantment( Enchantment.DURABILITY, durabilityLevel );
		}
	}
	
	protected void setDigSpeed( SpigotItemStack itemInHand, int digSpeedLevel ) {
		
		if ( itemInHand != null && itemInHand.getBukkitStack() != null ) {
			
			itemInHand.getBukkitStack().addUnsafeEnchantment( Enchantment.DIG_SPEED, digSpeedLevel );
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
	public boolean addPlayerCooldown( String playerUUID, int ticks )
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
					
					Integer cooldownTicks = playerCooldowns.get( playerUUID );

					int ticksRemaining = cooldownTicks == null ? 0 : cooldownTicks - 10;

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

	
	public void submitBombEffects( MineBombData bomb, EffectState effectState, Location location ) {
		
		
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
			
		}
		
	}
	
	public boolean setoffBombDelayed( SpigotPlayer sPlayer, MineBombData bomb, // Item droppedBomb, 
						SpigotBlock targetBlock ) {
		boolean results = false;
		
		final Location location = ( targetBlock.getLocation() == null ? 
										sPlayer.getLocation() : targetBlock.getLocation() );
		
		
		submitBombEffects( bomb, EffectState.placed, location );
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				// Remove the item that the player threw:
//				droppedBomb.remove();
				
				submitBombEffects( bomb, EffectState.explode, location );
				
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
				setUnbreaking( toolInHand, bomb.getToolInHandDurabilityLevel() );
				setDigSpeed( toolInHand, bomb.getToolInHandDigSpeedLevel() );
				
				
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
					submitBombEffects( bomb, EffectState.finished, location );
					
					if ( Output.get().isDebug() ) {
						Output.get().logDebug( "Mine Bomb's ExplosiveBlockBreakEvent has been canceled. " +
								"It may have been processed successfully." );
					}
				}
				else {
					// If it wasn't canceled, then it may not have been handled
					
					//droppedBomb.remove();
					
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
	
	public PlacedMineBombItemTask submitPlacedMineBombItemTask( MineBombData bomb, SpigotBlock sBlock, SpigotItemStack item ) {
		
		PlacedMineBombItemTask placedTask = new PlacedMineBombItemTask( bomb, sBlock, item );
		
		BukkitTask task = placedTask.runTaskTimer( 
				SpigotPrison.getInstance(), 1, 1 );
		
		placedTask.setBukkitTask( task );
		
		return placedTask;
	}
	
	public class PlacedMineBombItemTask 
		extends BukkitRunnable
	{
		
		private MineBombData bomb;
		private SpigotBlock sBlock;
		private SpigotItemStack item;
		
		private double eulerAngleX = 1.0;
		private double eulerAngleY = 0;
		private double eulerAngleZ = 0;
		
		private double twoPI;
		
		private ArmorStand armorStand;
		private boolean isDyanmicTag = false;
		private String tagName;

//		long ageTicks = 0L;
		long terminateOnZeroTicks = 0L;
				
		private BukkitTask bukkitTask;
		
		private DecimalFormat dFmt;
		
		public PlacedMineBombItemTask( MineBombData bomb, 
									SpigotBlock sBombBlock, SpigotItemStack item ) {
			super();
			
			this.bomb = bomb;
			this.sBlock = sBombBlock;
			this.item = item;
			
			
			this.twoPI = Math.PI * 2;
			
//			this.ageTicks = 0;
			this.terminateOnZeroTicks = getTaskLifeSpan();

			this.isDyanmicTag = bomb.getNameTag().contains( "{countdown}" );
			this.tagName = "";
			
			this.dFmt = Prison.get().getDecimalFormat( "0.0" );
			
			initializeArmorStand();
		}
		
		
		/**
		 * <p>This will calculate how long the placed item needs to 
		 * exist before removal, and this task will remove itself.
		 * While this item is placed, this task will run every 2 
		 * ticks and will spin the item in 3d space.
		 * </p>
		 * 
		 * <p>Removal is based upon the fuseDelayTicks which will take it to
		 * the explosion, then scanning the final effects to find how long
		 * the last one will be submitted for.  Then add 15 ticks.
		 * </p>
		 * 
		 * <p>At this time, not 100% sure if this item or armor stand
		 * will be used to "place" the effects.  Probably not.  If it's not
		 * needed, then this can be removed when the explosions start.
		 * </p>
		 * 
		 * @return
		 */
		private long getTaskLifeSpan()
		{
			int removeInTicks = bomb.getFuseDelayTicks() + bomb.getItemRemovalDelayTicks();
			return removeInTicks;
		}


		private void initializeArmorStand() {
			
			Location location = sBlock.getLocation();
			location.setY( location.getY() + 2.5 );
			
			SpigotWorld sWorld = (SpigotWorld) location.getWorld();

			
			EulerAngle arm = new EulerAngle( eulerAngleX, eulerAngleY, eulerAngleZ );
			
			armorStand = sWorld.getWrapper().spawn( 
					sWorld.getBukkitLocation( location ), 
						ArmorStand.class);
			
//			int removeInTicks = (int) getTaskLifeSpan();
			
			//armorStand.addAttachment( SpigotPrison.getInstance(), removeInTicks );
			
			if ( bomb.getNameTag() != null && !bomb.getNameTag().trim().isEmpty() ) {

				String tagName = bomb.getNameTag();
				if ( tagName.contains( "{name}" ) ) {
					tagName = tagName.replace( "{name}", bomb.getName() );
				}
				this.tagName = Text.translateAmpColorCodes( tagName );
				
				//updateArmorStandCustomName();
				armorStand.setCustomName( this.tagName );
				armorStand.setCustomNameVisible(true);
			}
			else {
				
				armorStand.setCustomNameVisible(false);
			}
			armorStand.setVisible(false);
			armorStand.setRemoveWhenFarAway(false);
			armorStand.setItemInHand( sWorld.getBukkitItemStack( item ) );
			armorStand.setRightArmPose(arm);
			
			
			if ( new BluesSpigetSemVerComparator().compareMCVersionTo( "1.9.0" ) >= 0 ) {
				
				armorStand.setGlowing( bomb.isGlowing() );
				
				// setGravity is invalid for spigot 1.8.8:
				armorStand.setGravity( bomb.isGravity() );
			}
		}

		private void updateArmorStandCustomName()
		{
			if ( isDyanmicTag ) {
			
				double countdown = (terminateOnZeroTicks / 20.0d);
				String tagName = this.tagName.replace( "{countdown}", dFmt.format( countdown) );
				
				armorStand.setCustomName( tagName );
			}
		}


		@Override
		public void run()
		{
			
			double speed = 0.35;
			
			eulerAngleX += speed;
			eulerAngleY += speed / 3;
			eulerAngleZ += speed / 5;
			
			
			EulerAngle arm = new EulerAngle( eulerAngleX, eulerAngleY, eulerAngleZ );
			
			armorStand.setRightArmPose(arm);
			
			
			if ( eulerAngleX > twoPI ) {
				eulerAngleX -= twoPI;
			}
			if ( eulerAngleY > twoPI ) {
				eulerAngleY -= twoPI;
			}
			if ( eulerAngleZ > twoPI ) {
				eulerAngleZ -= twoPI;
			}
			

			// Track the time that this has lived:
			if ( --terminateOnZeroTicks == 0 || !armorStand.isValid() ) {
				
				armorStand.remove();
				
				this.cancel();
			}
			
			updateArmorStandCustomName();
			
		}

		public BukkitTask getBukkitTask() {
			return bukkitTask;
		}
		public void setBukkitTask( BukkitTask bukkitTask ) {
			this.bukkitTask = bukkitTask;
		}
		
	}
	
	

}
