package tech.mcprison.prison.spigot.autofeatures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Text;

/**
 * <p>This class controls the data and the basic functions for auto features.
 * It loads the settings only once, and uses them on each event that is raised
 * in order to reduce overhead in reloading all of them on each block break
 * event.  Then, if an external process should happen to save changes to
 * these settings, then it will reload the settings so they are always
 * current.
 * </p>
 *
 *
 */
public class AutoManagerFeatures
		extends OnBlockBreakEventListener {

	public enum ItemLoreCounters {

		// NOTE: the String value must include a trailing space!

		itemLoreBlockBreakCount( ChatColor.LIGHT_PURPLE + "Prison Blocks Mined:" +
				ChatColor.GRAY + " "),

		itemLoreBlockExplodeCount( ChatColor.LIGHT_PURPLE + "Prison Blocks Exploded:" +
				ChatColor.GRAY + " ");


		private final String lore;
		ItemLoreCounters( String lore ) {
			this.lore = lore;
		}
		public String getLore() {
			return lore;
		}

	}

	public enum ItemLoreEnablers {
		Pickup,
		Smelt,
		Block
		;
	}

	private Random random = new Random();

	private AutoFeaturesFileConfig autoFeaturesConfig = null;


	public AutoManagerFeatures() {
		super();

		setup();
	}


	private void setup() {

		this.autoFeaturesConfig = new AutoFeaturesFileConfig();

	}

	public AutoFeaturesFileConfig getAutoFeaturesConfig() {
		return autoFeaturesConfig;
	}

	public boolean isBoolean( AutoFeatures feature ) {
		return autoFeaturesConfig.isFeatureBoolean( feature );
	}

	protected String getMessage( AutoFeatures feature ) {
		return autoFeaturesConfig.getFeatureMessage( feature );
	}

	protected List<String> getListString( AutoFeatures feature ) {
		List<String> results = null;
		if ( feature.isStringList() ) {
			results = autoFeaturesConfig.getFeatureStringList( feature );
		}
		else {
			results = new ArrayList<>();
		}
		return results;
	}

//	/**
//     * <p>This lazy loading of the FileConfiguration for the AutoFeatures will ensure
//     * the file is loaded from the file system only one time and only when it is first
//     * used.  This ensures that if it is never used, it is never loaded in to memory.
//     * </p>
//     *
//     * @return
//     */
//    private FileConfiguration getAutoFeaturesConfig() {
//    	if ( this.autoFeaturesConfig == null ) {
//    		AutoFeaturesFileConfig afc = new AutoFeaturesFileConfig();
//    		this.autoFeaturesConfig = afc.getConfig();
//
//    	}
//        return autoFeaturesConfig;
//    }


//    /**
//     * <p>This change in this function, to move it in to this class, allows the
//     * reloading of the parameters that controls all of the behaviors whenever
//     * the data is saved.
//     * </p>
//     *
//     * @return
//     */
//    public boolean saveAutoFeaturesConfig() {
//    	boolean success = false;
//    	FileConfiguration afConfig = getAutoFeaturesConfig();
//
//    	if ( afConfig != null ) {
//    		AutoFeaturesFileConfig afc = new AutoFeaturesFileConfig();
//    		success = afc.saveConf(afConfig);
//
//    		// reload the internal settings:
//    		setup();
//    	}
//    	return success;
//    }

	/**
	 * <p>If the fortune level is zero, then this function will always return a value of one.
	 * </p>
	 *
	 * <p>If it is non-zero, then this function will return a value of one, plus an
	 * equally distributed chance of returning an additional 0 to fortuneLevel
	 * bonuses of that material.
	 * </p>
	 *
	 * <p>This applies to "all" materials within the mine, including chests, shulkers,
	 * signs etc...
	 * </p>
	 *
	 * @param fortuneLevel
	 * @return
	 */
	public int getDropCount(int fortuneLevel) {
		int j =
				fortuneLevel == 0 ?
						1 :
						1 + getRandom().nextInt(fortuneLevel + 1);

		return j;
	}


	protected boolean hasSilkTouch (SpigotItemStack itemInHand){
		return itemInHand.getBukkitStack().getEnchantments().containsKey(Enchantment.SILK_TOUCH);
	}

	protected boolean hasFortune(SpigotItemStack itemInHand){
		return itemInHand.getBukkitStack().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS);
	}
	protected short getFortune(SpigotItemStack itemInHand){
		return (short) itemInHand.getBukkitStack().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
	}

	/*
	 * Drops are wrong with old 1.14.4 releases of spigot
	 * but got fixed in newer versions.
	 *
	 * For older versions, a good way to get the right drops would be to use BlockDropItemEvent.getItems(), but it's deprecated
	 * */
	protected int autoPickup( boolean autoPickup, Player player, SpigotItemStack itemInHand, SpigotBlock block ) {
		//, BlockBreakEvent e ) {
		int count = 0;
		if (autoPickup) {

			// The following is not the correct drops:
			Collection<SpigotItemStack> drops = SpigotUtil.getDrops(block, itemInHand);
//			Collection<ItemStack> drops = e.getBlock().getDrops(itemInHand);


			if (drops != null && drops.size() > 0 ) {

				// Need better drop calculation that is not using the getDrops function.
				short fortuneLevel = getFortune(itemInHand);


				// Adds in additional drop items:
				calculateDropAdditions( itemInHand, drops );

				if ( isBoolean( AutoFeatures.isCalculateSilkEnabled ) &&
						hasSilkTouch( itemInHand )) {

					calculateSilkTouch( itemInHand, drops );
				}

				// Add the item to the inventory
				for ( SpigotItemStack itemStack : drops ) {

					if ( isBoolean( AutoFeatures.isCalculateFortuneEnabled ) ) {
						// calculateFortune directly modifies the quantity on the blocks ItemStack:
						calculateFortune( itemStack, fortuneLevel );
					}

					count += itemStack.getAmount();
					dropExtra( SpigotUtil.addItemToPlayerInventory( player, itemStack ), player, block );
//					dropExtra( player.getInventory().addItem(itemStack), player, block );
				}

//				autoPickupCleanup( player, itemInHand, count );
			}
		}
		return count;
	}

	
	public int calculateNormalDrop( SpigotItemStack itemInHand, SpigotBlock block ) {
		int count = 0;
		

		// The following is not the correct drops:
		Collection<SpigotItemStack> drops = SpigotUtil.getDrops(block, itemInHand);
//		Collection<ItemStack> drops = e.getBlock().getDrops(itemInHand);


		if (drops != null && drops.size() > 0 ) {

			// Need better drop calculation that is not using the getDrops function.
			short fortuneLevel = getFortune(itemInHand);


			// Adds in additional drop items:
			calculateDropAdditions( itemInHand, drops );

			if ( isBoolean( AutoFeatures.isCalculateSilkEnabled ) &&
					hasSilkTouch( itemInHand )) {

				calculateSilkTouch( itemInHand, drops );
			}

			// Drop the items where the origional block was located:
			for ( SpigotItemStack itemStack : drops ) {

				if ( isBoolean( AutoFeatures.isCalculateFortuneEnabled ) ) {
					// calculateFortune directly modifies the quantity on the blocks ItemStack:
					calculateFortune( itemStack, fortuneLevel );
				}

				count += itemStack.getAmount();
				
				dropAtBlock( itemStack, block );
				
//				dropExtra( SpigotUtil.addItemToPlayerInventory( player, itemStack ), player, block );
//				dropExtra( player.getInventory().addItem(itemStack), player, block );
			}

			
			// Break the block and change it to air:
			block.setPrisonBlock( PrisonBlock.AIR );;
		}
		
		return count;
	}

	protected void autoPickupCleanup( SpigotBlock block, Player player, SpigotItemStack itemInHand, int count )
	{
		// Auto pickup has been successful. Now clean up.
		if ( count > 0 ) {

//			// Set the broken block to AIR and cancel the event
			if ( !block.isEmpty() ) {
				block.setPrisonBlock( PrisonBlock.AIR );
			}
			
//			e.setCancelled(true);
//			e.getBlock().setType(Material.AIR);
//
//			// Maybe needed to prevent drop side effects:
//			e.getBlock().getDrops().clear();

			// calculate durability impact: Include item durability resistance.
			if ( isBoolean( AutoFeatures.isCalculateDurabilityEnabled ) ) {

				// value of 0 = normal durability. Value 100 = never calculate durability.
				int durabilityResistance = 0;
				if ( isBoolean( AutoFeatures.loreDurabiltyResistance ) ) {
					durabilityResistance = getDurabilityResistance( itemInHand,
							getMessage( AutoFeatures.loreDurabiltyResistanceName ) );
				}

				calculateDurability( player, itemInHand, durabilityResistance );
			}
		}
	}

	protected void autoSmelt( boolean autoSmelt, String sourceStr, String destinationStr, Player p, SpigotBlock block  ) {

		if ( autoSmelt ) {
			XMaterial source = SpigotUtil.getXMaterial( sourceStr );
			XMaterial destination = SpigotUtil.getXMaterial( destinationStr );

			SpigotItemStack sourceStack = new SpigotItemStack( source.parseItem() );
			SpigotItemStack destStack = new SpigotItemStack( destination.parseItem() );

			if ( sourceStack != null && destStack != null &&
					SpigotUtil.playerInventoryContainsAtLeast( p, sourceStack, 1 ) ) {

				int count = itemCount(source, p);
				if ( count > 0 ) {
					sourceStack.setAmount( count );
					destStack.setAmount( count );

					SpigotUtil.playerInventoryRemoveItem( p, sourceStack );

					HashMap<Integer, SpigotItemStack> extras = SpigotUtil.addItemToPlayerInventory( p, destStack );
					dropExtra( extras, p, block );
				}
			}

		}
	}
	protected void autoBlock( boolean autoBlock, String sourceStr, String destinationStr,
							  Player p, SpigotBlock block  ) {
		autoBlock(autoBlock, sourceStr, destinationStr, 9, p, block );
	}

	protected void autoBlock( boolean autoBlock, String sourceStr, String destinationStr,
							  int targetCount, Player p, SpigotBlock block  ) {

		XMaterial source = SpigotUtil.getXMaterial( sourceStr );
		XMaterial destination = SpigotUtil.getXMaterial( destinationStr );

		if ( autoBlock ) {
			int count = itemCount(source, p);
			if ( count >= targetCount ) {
				int mult = count / targetCount;

				p.getInventory().removeItem(SpigotUtil.getItemStack(source, mult * targetCount));

				SpigotItemStack itemStack = SpigotUtil.getSpigotItemStack(destination, mult);
				HashMap<Integer, SpigotItemStack> extras = SpigotUtil.addItemToPlayerInventory( p, itemStack );
//				HashMap<Integer, ItemStack> extras = p.getInventory().addItem(SpigotUtil.getItemStack(destination, mult));
				dropExtra( extras, p, block);
			}
		}
	}

//	/**
//	 * <p>Lapis is really dyed ink sacks, so need to have special processing to ensure we process the
//	 * correct material, and not just any ink sack.
//	 * </p>
//	 *
//	 * <p><b>Warning:</b> this will not work with minecraft 1.15.x since magic numbers have been
//	 * 						eliminated.
//	 * </p>
//	 *
//	 * @param autoBlock
//	 * @param player
//	 */
//	protected void autoBlockLapis( boolean autoBlock, Player player, Block block  ) {
//		if ( autoBlock ) {
//			// ink_sack = 351:4
//
//			Material mat = Material.matchMaterial( "INK_SACK" );
//			short typeId = 4;
//
//			// try both methods to get lapis:
//
//			try {
//				convertLapisBlock( player, block, mat, typeId );
//			}
//			catch ( Exception e ) {
//				// Ignore exception.
//			}
//
//			mat = Material.matchMaterial( "lapis_lazuli" );
//			if ( mat != null ) {
//
//				try {
//					convertLapisBlock( player, block, mat );
//				}
//				catch ( Exception e ) {
//					// Ignore exception.
//				}
//			}
//
//		}
//	}


//	private void convertLapisBlock( Player player, Block block, Material mat, int typeId ) {
//		XMaterial xMat = SpigotUtil.getXMaterial( mat );
//		int count = itemCount(xMat, typeId, player);
//
//		if ( count >= 9 ) {
//			int mult = count / 9;
//
//			ItemStack removeLapisItemStack = XMaterial.LAPIS_LAZULI.parseItem();
//			removeLapisItemStack.setAmount( mult * 9 );
//
////			ItemStack removeLapisItemStack = new ItemStack( mat,  mult * 9, (short) typeId);
//			player.getInventory().removeItem(removeLapisItemStack);
//
//			dropExtra( player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, mult)), player, block );
//
//		}
//	}

//	private void convertLapisBlock( Player player, Block block, Material mat ) {
//		XMaterial xMat = SpigotUtil.getXMaterial( mat );
//		int count = itemCount(xMat, player);
//
//		if ( count >= 9 ) {
//			int mult = count / 9;
//
//			ItemStack removeLapisItemStack = XMaterial.LAPIS_LAZULI.parseItem();
//			removeLapisItemStack.setAmount( mult * 9 );
//
////			ItemStack removeLapisItemStack = new ItemStack( mat,  mult * 9 );
//			player.getInventory().removeItem(removeLapisItemStack);
//
//			dropExtra( player.getInventory().addItem(
//					new ItemStack(Material.LAPIS_BLOCK, mult)), player, block );
//		}
//	}

	private int itemCount(XMaterial source, Player player) {
		int count = 0;
		if ( source != null ) {
			ItemStack testStack = source.parseItem();

			PlayerInventory inv = player.getInventory();
			for (ItemStack is : inv.getContents() ) {
				if ( is != null && is.isSimilar( testStack ) ) {
					count += is.getAmount();
				}
			}
		}
		return count;
	}


//	private int itemCount(Material source, int typeId, Player player) {
//		int count = 0;
//		PlayerInventory inv = player.getInventory();
//
//		for (ItemStack is : inv.getContents()) {
//
//			if ( is != null && is.getType() != null && is.getType().compareTo( source ) == 0 ) {
//				count += is.getAmount();
//			}
//		}
//		return count;
//	}

	/**
	 * <p>If the player does not have any more inventory room for the current items, then
	 * it will drop the extra.
	 * </p>
	 * @param extra
	 * @param player
	 * @param block
	 */
	private void dropExtra( HashMap<Integer, SpigotItemStack> extra, Player player, SpigotBlock block ) {

		if ( extra != null && extra.size() > 0 ) {

			Configuration sellAllConfig = SpigotPrison.getInstance().getSellAllConfig();

			if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase("true")) {
				if (sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable").equalsIgnoreCase("true")){

					UUID playerUUID = player.getUniqueId();

					if (sellAllConfig.getString("Users." + playerUUID + ".isEnabled") != null){
						if (sellAllConfig.getString("Users." + playerUUID + ".isEnabled").equalsIgnoreCase("true")){
							if (sellAllConfig.getString("Options.Full_Inv_AutoSell_Notification").equalsIgnoreCase("true")) {
								Output.get().sendInfo(new SpigotPlayer(player), SpigotPrison.format(SpigotPrison.getInstance().getMessagesConfig().getString("Message.SellAllAutoSell")));
							}

							Bukkit.dispatchCommand(player, "sellall sell");
							return;
						}
					}
				} else {
					if (sellAllConfig.getString("Options.Full_Inv_AutoSell_Notification").equalsIgnoreCase("true")) {
						Output.get().sendInfo(new SpigotPlayer(player), SpigotPrison.format(SpigotPrison.getInstance().getMessagesConfig().getString("Message.SellAllAutoSell")));
					}

					Bukkit.dispatchCommand(player, "sellall sell");
					return;
				}
			}

			for ( SpigotItemStack itemStack : extra.values() ) {

				if ( isBoolean( AutoFeatures.dropItemsIfInventoryIsFull ) ) {

//					Location dropPoint = player.getLocation().add( player.getLocation().getDirection());
//
//					player.getWorld().dropItem( dropPoint, itemStack );

					SpigotUtil.dropPlayerItems( player, itemStack );
					notifyPlayerThatInventoryIsFull( player, block );
				} else {
					notifyPlayerThatInventoryIsFullLosingItems( player, block );
				}
			}
		}
	}
	
	private void dropAtBlock( SpigotItemStack itemStack, SpigotBlock block ) {
		
		SpigotUtil.dropItems( block, itemStack );
	}

	private void notifyPlayerThatInventoryIsFull( Player player, SpigotBlock block ) {
		notifyPlayerWithSound( player, block, AutoFeatures.inventoryIsFull );
	}

	@SuppressWarnings( "unused" )
	private void notifyPlayerThatInventoryIsFullDroppingItems( Player player, SpigotBlock block ) {
		notifyPlayerWithSound( player, block, AutoFeatures.inventoryIsFullDroppingItems );
	}

	private void notifyPlayerThatInventoryIsFullLosingItems( Player player, SpigotBlock block ) {
		notifyPlayerWithSound( player, block, AutoFeatures.inventoryIsFullLosingItems );

	}

	private void notifyPlayerWithSound( Player player, SpigotBlock block, AutoFeatures messageId ) {

		String message = autoFeaturesConfig.getFeatureMessage( messageId );

		// Play sound when full
		if (isBoolean(AutoFeatures.playSoundIfInventoryIsFull)) {
			Prison.get().getMinecraftVersion() ;

			// This hard coding the Sound enum causes failures in spigot 1.8.8 since it does not exist:
			Sound sound;
			try {
				sound = Sound.valueOf("ANVIL_USE"); // pre 1.9 sound
			} catch(IllegalArgumentException e) {
				sound = Sound.valueOf("BLOCK_ANVIL_PLACE"); // post 1.9 sound
			}

			player.playSound(player.getLocation(), sound, 10F, 1F);
		}

		// holographic display for showing full inventory does not work well.
//		if ( isBoolean( AutoFeatures.hologramIfInventoryIsFull ) ) {
//			displayMessageHologram( block, message , player);
//		}
//		else {
		actionBarVersion(player, message);
//		}
	}

	private void actionBarVersion(Player player, String message) {
		if (new BluesSpigetSemVerComparator().compareMCVersionTo("1.9.0") < 0) {
			displayActionBarMessage(player, message);
		}
		else {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(SpigotPrison.format(message)));
		}
	}

	/**
	 * This is not usable since it not only prevents the player from mining when it is
	 * displayed, but it cannot be seen since it is shown above the player's head.
	 *
	 * The player can continue to mine other materials even though one kind of resource
	 * be full.
	 *
	 * @param block
	 * @param message
	 * @param p
	 */
	@SuppressWarnings( "unused" )
	private void displayMessageHologram(Block block, String message, Player p){
		ArmorStand as = (ArmorStand) block.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setCustomNameVisible(true);
		as.setVisible(false);
		as.setCustomName(SpigotPrison.format(message));
		Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), as::remove, (7L * 20L));
	}

	private void displayActionBarMessage(Player player, String message) {
		SpigotPlayer prisonPlayer = new SpigotPlayer(player);
		Prison.get().getPlatform().showActionBar(prisonPlayer, message, 80);
	}


	/**
	 * <p>Using the ItemLoreEnables, check to see if the item in the hand has the
	 * specified Lore.  If so, then return 100.0 if just the lore, otherwise
	 * if it has a number to the right of the Lore, convert it to a double and
	 * return it.
	 * </p>
	 *
	 * <p>The only valid value to follow the lore, can only be a double number,
	 * or an integer.  For example 1.234, 50, 75.567, 99.0, or 100.0.
	 * Any value less than 0 will be zero, which is the same as no lore (disabled).
	 * Any value greater than 100 will be 100.0. No values following the lore will
	 * be treated as 100.0 percent.  Do not include percent sign or unit of measure.
	 * </p>
	 *
	 * @param loreEnabler
	 * @param player
	 * @return Percent chance of Lore enablement.
	 */
	protected double doesItemHaveAutoFeatureLore( ItemLoreEnablers loreEnabler, Player player ) {
		double results = 0.0;

		ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( player );
		if ( itemInHand != null && itemInHand.getType() != Material.AIR &&
				itemInHand.getItemMeta() != null ) { // (itemInHand.hasItemMeta()) { NOTE: hasItemMeta() always returns nulls
			
			ItemMeta meta = itemInHand.getItemMeta();
			
			if ( meta != null && meta.hasLore()) { 
				for (String lore : meta.getLore()) {
					if (lore.startsWith( loreEnabler.name())) {

						// Lore detected so set default to 100%:
						results = 100.0;
						
						String value = lore.replace( loreEnabler.name(), "" ).trim();

						if (value.length() > 0) {
							
							// Content has been found after the lore's name. If it is a number, then
							// use that to set the lore's percentage.  If it fails at parsing then use 100%.

							try {
								results = Double.parseDouble( value );
							}
							catch (NumberFormatException e) {

								// Error: Default to 100%
								// Do not generate log messages since there will be 1000's...
								results = 100.0;
							}

							// Clean up the parsed number.  Less than zero is zero (disabled).
							if ( results < 0.0 ) {
								results = 0.0;
							}

							// Cannot exceed 100%
							if ( results > 100.0 ) {
								results = 100.0;
							}
						}
					}
				}
			}
		}

		return results;
	}

	protected double getLoreValue( SpigotItemStack itemInHand, String loreValue ) {
		double results = 0.0;

		if ( itemInHand != null && !itemInHand.isAir() && loreValue != null && !loreValue.trim().isEmpty() ) {
			List<String> lores = itemInHand.getLore();
			
			// Clean the loreValue we need to compare everything to.  It must have all color codes removed:
			String loreValueCleaned = Text.stripColor( loreValue );
			
			for ( String lore : lores ) {
				
				// Remove the color codes so it can be cleanly compared with the loreValue:
				String loreCleaned = Text.stripColor( lore );

				if (loreCleaned.startsWith( loreValueCleaned )) {
					
					// Lore detected so set default to 100%:
					results = 100.0;
					
					String value = loreCleaned.replace( loreValueCleaned, "" ).trim();
					
					if (value.length() > 0) {
						
						// Content has been found after the lore's name. If it is a number, then
						// use that to set the lore's percentage.  If it fails at parsing then use 100%.
						
						try {
							results = Double.parseDouble( value );
						}
						catch (NumberFormatException e) {
							
							// Error: Default to 100%
							// Do not generate log messages since there will be 1000's...
							results = 100.0;
						}
						
						// Clean up the parsed number.  Less than zero is zero (disabled).
						if ( results < 0.0 ) {
							results = 0.0;
						}
						
						// Cannot exceed 100%
						if ( results > 100.0 ) {
							results = 100.0;
						}
					}
				}
				
			}
		}

		return results;
	}

	
	/**
	 * <p>If using autoPickupBlockNameList then must use XMaterial's name.
	 * If block type is CustomItems, then it must be prefixed with CustomIems:BlockName. Minecraft
	 * blocks can be prefixed with minecraft:BlockName but they don't have to be.
	 * </p>
	 * 
	 * @param block
	 * @param p
	 * @param itemInHand
	 * @return
	 */
	protected int autoFeaturePickup( SpigotBlock block, Player p, SpigotItemStack itemInHand ) {


//		Material brokenBlock = e.getBlock().getType();
//		String blockName = brokenBlock.toString().toLowerCase();
//		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( p );
		int count = 0;
		
		// Use this is a block name lisst based upon the following:  blockType:blockName if not minecraft, or blockName
		List<String> autoPickupBlockNameList =
				isBoolean( AutoFeatures.autoPickupBlockNameListEnabled ) ? 
						getListString( AutoFeatures.autoPickupBlockNameList ) : null;

		if (isBoolean(AutoFeatures.autoPickupAllBlocks)) {
			count += autoPickup( true, p, itemInHand, block );

		}
		
		else if ( isBoolean( AutoFeatures.autoPickupBlockNameListEnabled ) && autoPickupBlockNameList.size() > 0 && 
							autoPickupBlockNameList.contains( block.getPrisonBlock().getBlockName() ) ) {
			count += autoPickup( true, p, itemInHand, block );
		}
			
		else {

			switch (block.getPrisonBlock().getBlockNameSearch() ) {

				case "cobblestone":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupCobbleStone ), p, itemInHand, block);
					break;

				case "stone":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupStone ), p, itemInHand, block);
					break;

				case "gold_ore":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupGoldOre ), p, itemInHand, block);
					break;

				case "iron_ore":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupIronOre ), p, itemInHand, block);
					break;

				case "coal_ore":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupCoalOre ), p, itemInHand, block);
					break;

				case "diamond_ore":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupDiamondOre ), p, itemInHand, block);
					break;

				case "redstone_ore":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupRedStoneOre ), p, itemInHand, block);
					break;

				case "emerald_ore":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupEmeraldOre ), p, itemInHand, block);
					break;

				case "quartz_ore":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupQuartzOre ), p, itemInHand, block);
					break;

				case "lapis_ore":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupLapisOre ), p, itemInHand, block );
					break;

				case "snow_ball":
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupSnowBall ), p, itemInHand, block );
					break;

				case "glowstone_dust": // works 1.15.2
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ) ||
							isBoolean( AutoFeatures.autoPickupGlowstoneDust ), p, itemInHand, block );
					break;

				default:
					count += autoPickup(isBoolean( AutoFeatures.autoPickupAllBlocks ), p, itemInHand, block );
					break;
			}
		}

		// Calculate XP on block break if enabled:
		calculateXP( p, block, count );

//				Output.get().logInfo( "In mine: %s  blockName= [%s] %s  drops= %s  count= %s  dropNumber= %s ",
//						mine.getName(), blockName, Integer.toString( dropNumber ),
//						(e.getBlock().getDrops(itemInHand) != null ? e.getBlock().getDrops(itemInHand).size() : "-=null=-"),
//						Integer.toString( count ), Integer.toString( dropNumber )
//						);

		return count;
	}

	protected void autoFeatureSmelt( SpigotBlock block, Player p, SpigotItemStack itemInHand )
	{
//		Block block = e.getBlock();

		autoSmelt(isBoolean( AutoFeatures.autoSmeltAllBlocks ) || isBoolean( AutoFeatures.autoSmeltGoldOre ), "GOLD_ORE", "GOLD_INGOT", p, block);

		autoSmelt(isBoolean( AutoFeatures.autoSmeltAllBlocks ) || isBoolean( AutoFeatures.autoSmeltIronOre ), "IRON_ORE", "IRON_INGOT", p, block);
	}

	protected void autoFeatureBlock( SpigotBlock block, Player p, SpigotItemStack itemInHand ) {

//		Block block = e.getBlock();

		// Any autoBlock target could be enabled, and could have multiples of 9, so perform the
		// checks within each block type's function call.  So in one pass, could hit on more
		// than one of these for multiple times too.
		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockGoldBlock ), "GOLD_INGOT", "GOLD_BLOCK", p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockIronBlock ), "IRON_INGOT", "IRON_BLOCK", p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockCoalBlock ), "COAL", "COAL_BLOCK", p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockDiamondBlock ), "DIAMOND", "DIAMOND_BLOCK", p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockRedstoneBlock ), "REDSTONE","REDSTONE_BLOCK", p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockEmeraldBlock ), "EMERALD", "EMERALD_BLOCK", p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockQuartzBlock ), "QUARTZ", "QUARTZ_BLOCK", 4, p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockPrismarineBlock ), "PRISMARINE_SHARD", "PRISMARINE", 4, p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockSnowBlock ), "SNOW_BALL", "SNOW_BLOCK", 4, p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockGlowstone ), "GLOWSTONE_DUST", "GLOWSTONE", 4, p, block);

		autoBlock(isBoolean( AutoFeatures.autoBlockAllBlocks ) || isBoolean( AutoFeatures.autoBlockLapisBlock ), "LAPIS_LAZULI", "LAPIS_BLOCK", p, block);
	}

	protected void itemLoreCounter( SpigotItemStack itemInHand, String itemLore, int blocks) {

		if (itemInHand.getBukkitStack().hasItemMeta()) {

			List<String> lore = new ArrayList<>();
			itemLore = itemLore.trim() + " ";
			itemLore = Text.translateAmpColorCodes( itemLore.trim() + " ");
			ItemMeta meta = itemInHand.getBukkitStack().getItemMeta();

//			String prisonBlockBroken = itemLore.getLore();


			if (meta.hasLore()) {
				lore = meta.getLore();
				boolean found = false;

				for( int i = 0; i < lore.size(); i++ ) {
					if ( lore.get( i ).startsWith( itemLore ) ) {
						String val = lore.get( i ).replace( itemLore, "" ).trim();
						int count = blocks;

						try {
							count += Integer.parseInt(val);
						} catch (NumberFormatException e1) {
							Output.get().logError("AutoManager: tool counter failure. lore= [" + lore.get(i) + "] val= [" + val + "] error: " + e1.getMessage());								}

						lore.set(i, itemLore + count);
						found = true;

						break;
					}
				}

				if ( !found ) {
					lore.add(itemLore + 1);
				}

			} else {
				lore.add(itemLore + 1);
			}

			meta.setLore(lore);
			itemInHand.getBukkitStack().setItemMeta(meta);

			// incrementCounterInName( itemInHand, meta );

		}
	}

	/**
	 * <p>This function will search for the loreDurabiltyResistanceName within the
	 * item in the hand, if found it will return the number if it exists.  If not
	 * found, then it will return a value of zero, indicating that no special resistance
	 * exists, and that durability should be applied as normal.
	 * </p>
	 *
	 * <p>If there is no value after the lore name, then the default is 100 %.
	 * If a value follows the lore name, then it must be an integer.
	 * If it is less than 0, then 0. If it is greater than 100, then 100.
	 * </p>
	 *
	 * @param itemInHand
	 * @param itemLore
	 * @return
	 */
	protected int getDurabilityResistance(SpigotItemStack itemInHand, String itemLore) {
		int results = 0;

		if ( itemInHand.getBukkitStack().hasItemMeta() ) {

			List<String> lore = new ArrayList<>();

			itemLore = itemLore.trim() + " ";

			itemLore = Text.translateAmpColorCodes( itemLore.trim() + " ");

//			String prisonBlockBroken = itemLore.getLore();

			ItemMeta meta = itemInHand.getBukkitStack().getItemMeta();

			if (meta.hasLore()) {
				lore = meta.getLore();

				for (String s : lore) {
					if (s.startsWith(itemLore)) {

						// It has the durability resistance lore, so set the results to 100.
						// If a value is set, then it will be replaced.
						results = 100;

						String val = s.replace(itemLore, "").trim();

						try {
							results += Integer.parseInt(val);
						} catch (NumberFormatException e1) {
							Output.get().logError("AutoManager: tool durability failure. lore= [" + s + "] val= [" + val + "] error: " + e1.getMessage());
						}

						break;
					}
				}
			}

			if ( results > 100d ) {
				results = 100;
			}
			else if ( results < 0 ) {
				results = 0;
			}

		}

		return results;
	}


	protected void incrementCounterInName( ItemStack itemInHand, ItemMeta meta ) {
		String name = meta.getDisplayName();
		if (!meta.hasDisplayName() || name == null || name.trim().length() == 0) {
			name = itemInHand.getType().name().toLowerCase().replace("_", " ").trim();
			name += " [1]";
		} else {

			int j = name.lastIndexOf(']');
			if (j == -1) {
				name += " [1]";
			} else {
				if (j != -1) {
					int i = name.lastIndexOf('[', j);

					if (i != -1) {
						String numStr = name.substring(i + 1, j);

//									Output.get().logInfo( String.format( "AutoManager: name:  %s : %s ",
//											name, numStr) );

						try {
							int blocksMined = Integer.parseInt( numStr );
							name = name.substring( 0, i ).trim() + " [" + ++blocksMined + "]";
						} catch (NumberFormatException e1) {
							Output.get().logError("AutoManager: tool counter failure. tool name= [" + name + "] error: " + e1.getMessage());
						}
					}
				}
			}
		}
		if (name != null) {
			meta.setDisplayName( name );
			itemInHand.setItemMeta( meta );
		}
	}

	/**
	 * <p>This should calculate and apply the durability consumption on the tool.
	 * </p>
	 *
	 * <p>The damage is calculated as a value of one durability, but all damage can be
	 * skipped if the tool has a durability enchantment.  If it does, then there is a
	 * percent chance of 1 in (1 + durabilityLevel).  So if a tool has a durability level
	 * of 1, then there is a 50% chance. Level of 2, then a 66.6% chance. Level of 3 has
	 * a 75% chance. And a level of 9 has a 90% chance. There are no upper limits on
	 * durability enchantment levels.
	 * </p>
	 *
	 * <p>Some blocks may have a damage of greater than 1, but for now, this
	 * does not take that in to consideration. If hooking up in the future, just
	 * set the initial damage to the correct value based upon block type that was mined.
	 * </p>
	 *
	 * <p>The parameter durabilityResistance is optional, to disable use a value of ZERO.
	 * This is a percentage and is calculated first.  If random value is equal to the parameter
	 * or less, then it will skip the durability calculations for the current event.
	 * </p>
	 *
	 * <p>Based upon the following URL.  See Tool Durability.
	 * </p>
	 * https://minecraft.gamepedia.com/Item_durability
	 *
	 * @param player
	 * @param itemInHand - Should be the tool they used to mine or dig the block
	 * @param durabilityResistance - Chance to prevent durability wear being applied.
	 * 			Zero always disables this calculation and allows normal durability calculations
	 * 			to be performed. 100 always prevents wear.
	 */
	protected void calculateDurability(Player player, SpigotItemStack itemInHand, int durabilityResistance) {

		short damage = 1;  // Generally 1 unless instant break block then zero.

		if ( durabilityResistance >= 100 ) {
			damage = 0;
		} else if ( durabilityResistance > 0 ) {
			if ( getRandom().nextInt( 100 ) <= durabilityResistance ) {
				damage = 0;
			}
		}

		if (damage > 0 && itemInHand.getBukkitStack().containsEnchantment( Enchantment.DURABILITY)) {
			int durabilityLevel = itemInHand.getBukkitStack().getEnchantmentLevel( Enchantment.DURABILITY );

			// the chance of losing durability is 1 in (1+level)
			// So if the random int == 0, then take damage, otherwise none.
			if (getRandom().nextInt( durabilityLevel ) > 0) {
				damage = 0;
			}
		}

		if (damage > 0) {

			Compatibility compat = SpigotPrison.getInstance().getCompatibility();
			int maxDurability = compat.getDurabilityMax( itemInHand );
			int durability = compat.getDurability( itemInHand );
			int newDurability = durability + damage;

			if (newDurability > maxDurability) {
				// Item breaks! ;(
				compat.breakItemInMainHand( player );
			} else {
				compat.setDurability( itemInHand, newDurability );
			}
			player.updateInventory();
		}
	}

	private void calculateXP(Player player, SpigotBlock block, int count) {

		if (isBoolean(AutoFeatures.isCalculateXPEnabled) && block != null ) {

			String blockName = block.getPrisonBlock() == null ? null : block.getPrisonBlock().getBlockName();

			if ( blockName != null ) {

				int xp = 0;
				for ( int i = 0; i < count; i++ ) {
					xp += calculateXP( blockName );
				}

				if (xp > 0) {

					if ( isBoolean( AutoFeatures.givePlayerXPAsOrbDrops )) {

						Location dropPoint = player.getLocation().add( player.getLocation().getDirection());
						((ExperienceOrb) player.getWorld().spawn(dropPoint, ExperienceOrb.class)).setExperience(xp);

					}
					else {
						player.giveExp( xp );
					}
				}
			}
		}
	}
	/**
	 * <p>This calculate xp based upon the block that is broken.
	 * Fortune does not increase XP that a block drops.
	 * </p>
	 *
	 * <ul>
	 *   <li>Coal Ore: 0 - 2</li>
	 *   <li>Nether Gold Ore: 0 - 1</li>
	 *   <li>Diamond Ore, Emerald Ore: 3 - 7</li>
	 *   <li>Lapis Luzuli Ore, Nether Quartz Ore: 2 - 5</li>
	 *   <li>Redstone Ore: 1 - 5</li>
	 *   <li>Monster Spawner: 15 - 43</li>
	 * </ul>
	 *
	 * @param Block
	 * @return
	 */
	private int calculateXP( String blockName ) {
		int xp = 0;

		switch (blockName.toLowerCase()) {
			case "coal_ore":
				xp = getRandom().nextInt( 2 );
				break;

			case "nether_gold_ore":
				xp = getRandom().nextInt( 1 );
				break;

			case "diamond_ore":
			case "emerald_ore":
				xp = getRandom().nextInt( 4 ) + 3;
				break;

			case "lapis_ore":
			case "nether_quartz_ore":
				xp = getRandom().nextInt( 3 ) + 2;
				break;

			case "redstone_ore":
				xp = getRandom().nextInt( 4 ) + 1;
				break;

			case "spawn":
				xp = getRandom().nextInt( 28 ) + 15;
				break;

			default:
				break;
		}

		return xp;
	}

	/**
	 * <p>This function is based upon the following wiki page.
	 * </p>
	 * https://minecraft.gamepedia.com/Fortune
	 *
	 * <p>"<b>Ore:</b> For coal ore, diamond ore, emerald ore, lapis lazuli ore,
	 * nether gold ore,‌[upcoming: JE 1.16] and nether quartz ore,
	 * Fortune I gives a 33% chance to multiply drops by 2 (averaging 33% increase),
	 * Fortune II gives a chance to multiply drops by 2 or 3 (25% chance each,
	 * averaging 75% increase), and Fortune III gives a chance to multiply drops
	 * by 2, 3, or 4 (20% chance each, averaging 120% increase).
	 * </p>
	 * <p>"Generally speaking, Fortune gives a weight of 2 to a normal drop chance
	 * and adds a weight of 1 for each extra drop multiplier. The drop multipliers
	 * are the integers between 2 and Fortune Level + 1, inclusive.
	 * </p>
	 * <p>"The formula to calculate the average drops multiplier is
	 * 1/(Fortune Level+2) + (Fortune Level+1)/2, which means
	 * Fortune IV gives 2.67x drops on average, Fortune V gives
	 * 3.14x drops on average, etc. "
	 * </p>
	 *
	 * <p><b>Discrete random</b> Glowstone, melons, nether wart, redstone ore,
	 * sea lanterns, and sweet berries use a discrete uniform distribution,
	 * meaning each possible drop amount is equally likely to be dropped.
	 * Fortune increases the maximum number of drops by 1 per level. However,
	 * maximum drop limitations may apply: glowstone has a cap of 4 glowstone
	 * dust, sea lanterns have a cap of 5 prismarine crystals, and melons have
	 * a cap of 9 melon slices. If a drop higher than these maximums is rolled,
	 * it is rounded down to the cap.
	 * </p>
	 *
	 *
	 * @param blocks
	 * @param fortuneLevel
	 */
	protected void calculateFortune(SpigotItemStack blocks, int fortuneLevel) {

		if (fortuneLevel > 0) {

			int count = blocks.getAmount();
			int multiplier = 1;

			// Due to variations with gold and wood PickAxe need to use a dynamic
			// Material name selection which will fit for the version of MC that is
			// being ran.
			BlockType block = blocks.getMaterial();
			
				
			if ( 
					isBoolean( AutoFeatures.isCalculateFortuneOnAllBlocksEnabled ) ||
					
					block == BlockType.COAL_ORE ||
					block == BlockType.DIAMOND_ORE ||
					block == BlockType.EMERALD_ORE ||
					block == BlockType.IRON_ORE ||
					block == BlockType.LAPIS_LAZULI_ORE ||
					block == BlockType.LAPIS_ORE ||
					block == BlockType.GOLD_ORE ||
					block == BlockType.NETHER_GOLD_ORE ||
					block == BlockType.NETHER_QUARTZ_ORE ||
					
					block == BlockType.BLOCK_OF_COAL ||
					block == BlockType.COAL ||
					block == BlockType.COAL_BLOCK ||
					block == BlockType.DIAMOND ||
					block == BlockType.DIAMOND_BLOCK ||
					block == BlockType.EMERALD ||
					block == BlockType.EMERALD_BLOCK ||
					block == BlockType.GOLD_BLOCK ||
					block == BlockType.IRON_BLOCK ||
					block == BlockType.LAPIS_BLOCK ||
					block == BlockType.LAPIS_LAZULI_BLOCK ||
					block == BlockType.NETHER_WART_BLOCK ||
					block == BlockType.NETHERITE_BLOCK ||
					block == BlockType.PURPUR_BLOCK ||
					block == BlockType.QUARTZ_BLOCK ||
					block == BlockType.REDSTONE_BLOCK ||
					block == BlockType.SLIME_BLOCK ||
					block == BlockType.SNOW_BLOCK
					) {

				multiplier = calculateFortuneMultiplier( fortuneLevel, multiplier );

				// multiply the multiplier:
				count *= multiplier;
			} else if ( block == BlockType.GLOWSTONE ||
					block == BlockType.GLOWSTONE_DUST ||
					block == BlockType.REDSTONE ||
					block == BlockType.SEA_LANTERN ||
					block == BlockType.GLOWING_REDSTONE_ORE ||
					block == BlockType.REDSTONE_ORE ||
					block == BlockType.PRISMARINE ||

					block == BlockType.BEETROOT_SEEDS ||
					block == BlockType.CARROT ||
					block == BlockType.MELON ||
					block == BlockType.MELON_SEEDS ||
					block == BlockType.NETHER_WART ||
					block == BlockType.POTATO ||
					block == BlockType.GRASS ||
					block == BlockType.WHEAT ) {
				multiplier = getRandom().nextInt( fortuneLevel );

				// limits slightly greater than standard:
				if (block == BlockType.GLOWSTONE) {
					// standard: 4
					if (multiplier > 5) {
						multiplier = 5;
					}
				} else if (block == BlockType.SEA_LANTERN) {
					// standard: 5
					if (multiplier > 6) {
						multiplier = 6;
					}
				} else if (block == BlockType.MELON) {
					// standard: 9
					if (multiplier > 11) {
						multiplier = 11;
					}
				}

				// add the multiplier to the count:
				count += multiplier;
			}

			// The count has the final value so set it as the amount:
			blocks.setAmount( count );
		}



		// cannot use switches with dynamic Material types:
//		switch ( blocks.getType() ){
//
//			case COAL:
//			case DIAMOND:
//			case EMERALD:
//			case LAPIS_BLOCK:
//			case GOLD_BLOCK:
//			case QUARTZ_BLOCK:
//			case COAL_ORE:
//			case DIAMOND_ORE:
//			case EMERALD_ORE:
//			case LAPIS_ORE:
//			case GOLD_ORE:
//			case QUARTZ_ORE:
//
//				multiplier = calculateFortuneMultiplier( fortuneLevel, multiplier );
//
//				// multiply the multiplier:
//				count *= multiplier;
//				break;
//
//			case GLOWSTONE:
//			case GLOWSTONE_DUST:
//			case REDSTONE:
//			case SEA_LANTERN:
//			case GLOWING_REDSTONE_ORE:
//			case PRISMARINE:
//
//			case BEETROOT_SEEDS:
//			case CARROT:
//			case MELON:
//			case MELON_SEEDS:
//			case NETHER_WARTS:
//			case POTATO:
//			case GRASS:
//			case WHEAT:
//
//				multiplier = getRandom().nextInt( fortuneLevel );
//
//				switch ( blocks.getType() )
//				{
//					// limits slightly greater than standard:
//					case GLOWSTONE:
//						// standard: 4
//						if ( multiplier > 5 ) {
//							multiplier = 5;
//						}
//						break;
//					case SEA_LANTERN:
//						// standard: 5
//						if ( multiplier > 6 ) {
//							multiplier = 6;
//						}
//						break;
//					case MELON:
//						// standard: 9
//						if ( multiplier > 11 ) {
//							multiplier = 11;
//						}
//
//					default:
//						break;
//				}
//
//				// add the multiplier to the count:
//				count += multiplier;
//
//			default:
//				break;
//		}

//		// The count has the final value so set it as the amount:
//		blocks.setAmount( count );
	}


	/**
	 * <p>Fortune is calculated using the standard calculations used by vanilla
	 * minecraft when the tool's fortuneLevel is three or lower.
	 * </p>
	 * 
	 * <p>This function also supports non-standard higher fortune levels. Fortune 
	 * levels of 4 and 5 are fixed at lower levels.  Levels 6 and higher uses a
	 * formula with an initial threshold of 80%, but the threshold increases 
	 * as the fortuneLevel increases.  There is no upper limit on the calculations, 
	 * but anything greater than a fortune level of 200 has a 100% chance of 
	 * the calculated multiplier being applied.
	 * </p>
	 * 
	 * <p>Fortune Levels and the resulting multipliers are based upon random chance
	 * between 0 and 100:
	 * 
	 * </p>
	 * <ul>
	 *   <li>Fortune 1: <ul>
	 *   	<li><b>2</b> :: rnd <= 33</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 2: <ul>
	 *   	<li><b>2</b> :: rnd <= 25</li>
	 *   	<li><b>3</b> :: rnd <= 50</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 3: <ul>
	 *   	<li><b>2</b> :: rnd <= 20</li>
	 *   	<li><b>3</b> :: rnd <= 40</li>
	 *   	<li><b>4</b> :: rnd <= 60</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 4: <ul>
	 *   	<li><b>2</b> :: rnd <= 16</li>
	 *   	<li><b>3</b> :: rnd <= 32</li>
	 *   	<li><b>4</b> :: rnd <= 48</li>
	 *   	<li><b>5</b> :: rnd <= 64</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 5: <ul>
	 *   	<li><b>2</b> :: rnd <= 14</li>
	 *   	<li><b>3</b> :: rnd <= 28</li>
	 *   	<li><b>4</b> :: rnd <= 42</li>
	 *   	<li><b>5</b> :: rnd <= 56</li>
	 *   	<li><b>6</b> :: rnd <= 70</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 6 & Higher: <ul>
	 *     <li>Threshold is set to 80%</li>
	 *     <li>For every fortuneLevel of 10, threshold is increased by 1%</li>
	 *     <li>Threshold can only reach a max value of 100</li>
	 *     <li>If rnd is greater than threshold, then multiplier is only 1</li>
	 *     <li>chancePerUnit = threshold / fortuneLevel</li>
	 *     <li>multiplier = the floor value of chancePerUnit * rnd</li>
	 *     <li>If modified threshold hit 100, then chancePerUnit is always calculated.</li>
	 *     <li>No upper limit on fortuneLevel.</li>
	 *     </ul></li>
	 *   </li>
	 * </ul
	 * 
	 * @param fortuneLevel
	 * @param multiplier
	 * @return
	 */
	private int calculateFortuneMultiplier(int fortuneLevel, int multiplier) {
		int rnd = getRandom().nextInt( 100 );

		switch (fortuneLevel) {
			case 0:
				break;
			case 1:
				if (rnd <= 33) {
					multiplier = 2;
				}
				break;

			case 2:
				if (rnd <= 25) {
					multiplier = 2;
				}
				else if (rnd <= 50) {
					multiplier = 3;
				}
				break;

			case 3:
				if (rnd <= 20) {
					multiplier = 2;
				}
				else if (rnd <= 40) {
					multiplier = 3;
				}
				else if (rnd <= 60) {
					multiplier = 4;
				}
				break;


			case 4:
				if (rnd <= 16) {
					multiplier = 2;
				}
				else if (rnd <= 32) {
					multiplier = 3;
				}
				else if (rnd <= 48) {
					multiplier = 4;
				}
				else if (rnd <= 64) {
					multiplier = 5;
				}
				break;

			case 5:
				// values of 5 or higher
				if (rnd <= 14) {
					multiplier = 2;
				}
				else if (rnd <= 28) {
					multiplier = 3;
				}
				else if (rnd <= 42) {
					multiplier = 4;
				}
				else if (rnd <= 56) {
					multiplier = 5;
				}
				else if (rnd <= 70) {
					// Only 8% not 16% chance
					multiplier = 6;
				}
				break;
				
			default:
				
				// Fortune is over 5, so apply a special formula:
				
				// Take the fortune level and divide by 10 and then take the 
				// floor value of that and use it as the threshold modifier.
				
				int thresholdModifier = Math.floorDiv( fortuneLevel, 10 );
				
				// Max thresholdModifier can only be 100.
				if ( thresholdModifier > 100 ) {
					thresholdModifier = 100;
				}
				
				// Calculate the threshold to apply the multiplier, starting 
				// with 80%, then add the thresholdModifier so the higher the
				// fortune, then the greater the odds of engaging the the 
				// multiplier, where a fortune of 200 will guarantee the 
				// the multiplier will always be enabled.
				
				double threshold = 80.0d + thresholdModifier;
				
				// Use a random number that is a double:
				double rndD = getRandom().nextDouble() * 100d;
				
				if ( rndD <= threshold ) {
					// Passed the threshold, so calculate the multiplier.
					
					// The chancesPerUnit represents how to subdivide the 
					// threshold in to number of fortuneLevels.
					double chancesPerUnit = threshold / fortuneLevel;
					
					// Calculate how many units are in the rndD number:
					double units = rndD / chancesPerUnit;
					
					// The multiplier is the floor of units. Do not round up.
					multiplier = 1 + (int) Math.floor( units );
					
				}
				
		}
		return multiplier;
	}

	/**
	 * <p>This function has yet to be implemented, but it should implement behavior if
	 * silk touch is enabled for the tool.
	 * </p>
	 *
	 * @param itemInHand
	 * @param drops
	 */
	@SuppressWarnings( "unused" )
	private void calculateSilkTouch(SpigotItemStack itemInHand, Collection<SpigotItemStack> drops) {

		for (SpigotItemStack itemStack : drops) {

			// If stack is gravel, then there is a 10% chance of droping flint.

		}
	}

	/**
	 * <p>Because of the use of getDrops() function, not all of the correct drops are actually
	 * dropped.  This function tries to restore some of those special drops.
	 * </p>
	 *
	 * <p>Example of what this does, is to provide a random drop of flint when mining
	 * coal ore.
	 * </p>
	 *
	 * <p>"When a block of gravel is mined, there is a 10% chance for a single piece of flint
	 * to drop instead of the gravel block. When mined with a Fortune-enchanted tool, this chance
	 * increases to 14% at Fortune I, 25% at Fortune II, and 100% at Fortune III. Gravel mined
	 * using a tool with Silk Touch or gravel that fell on a non-solid block never produces flint."
	 * <a href="https://minecraft.gamepedia.com/Flint">wiki</a>
	 * </p>
	 *
	 * <p>For example gravel having random flint drops.
	 * </p>
	 *
	 * @param itemInHand
	 * @param drops
	 */
	private void calculateDropAdditions(SpigotItemStack itemInHand, Collection<SpigotItemStack> drops) {

		for (SpigotItemStack itemStack : drops) {

			// If gravel and has the 10% chance whereas rnd is zero, which is 1 out of 10.
			// But if has silk touch, then never drop flint.
			calculateDropAdditionsGravelFlint( itemInHand, itemStack, drops );
		}
	}


	/**
	 * <p>For gravel flint drops, this function adds special processing to increase the quantity
	 * of flint drops from the standard 1, to be influence by fortune enchants.  If fortune
	 * is >= 3, then add one to the quantity drop, plus a random chance to add floor(fortune / 5).
	 * So if fortune is 3, then the drop will always be 2.  If fortune is 5, then drop will
	 * be 2, plus a random chance of one additional drop.  If fortune is 20, then drop will be
	 * 2, plus an equal random chance to add 0 to 4 additional flints to the quantity of
	 * the flint drop. The other thing that is different from vanilla, is that if the player
	 * will get a flint drop, they will still get the normal gravel drop.
	 * </p>
	 *
	 * @param itemInHand
	 * @param itemStack
	 * @param drops
	 */
	private void calculateDropAdditionsGravelFlint(SpigotItemStack itemInHand, SpigotItemStack itemStack,
												   Collection<SpigotItemStack> drops ) {
		if (itemStack.getMaterial() == BlockType.GRAVEL && !hasSilkTouch(itemInHand)) {

			int quantity = 1;
			int threshold = 10;

			// If fortune is enabled on the tool, then increase drop oddds by:
			//  1 = 14%, 2 = 25%, 3+ = 100%
			int fortune = getFortune(itemInHand);
			switch (fortune) {
				case 0:
					// No additional threshold when fortune is zero:
					break;
				case 1:
					threshold = 14;
					break;
				case 2:
					threshold = 25;
					break;
				case 3:
				default:
					// if Fortune 3 or higher, default to 100% drop:
					threshold = 100;
					break;
			}

			// If zero, then 10% chance of 1 out of 10.
			if (getRandom().nextInt(100) <= threshold) {

				// If fortune is >= 3, then add one to the quantity drop, plus a
				// random chance to add floor(fortune / 5).
				if (fortune >= 3) {
					quantity += 1 + getRandom().nextInt(Math.floorDiv( fortune, 5));
				}

//				ItemStack flintStack = new ItemStack(Material.FLINT, quantity);
				SpigotItemStack flintStack = new SpigotItemStack( quantity, BlockType.FLINT);
				drops.add(flintStack);
			}
		}
	}

	public Random getRandom() {
		return random;
	}

}