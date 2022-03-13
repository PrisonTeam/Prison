package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * <p>This is a manual way of dealing with player inventory directly.  It is
 * added here to keep it out of the auto features.
 * The use of this is through the prison utils functions blocking and smelting.
 * </p>
 *
 * <p>These should not be used with normal block break event handling since 
 * these manipulate the player's inventory for each process,
 * instead of dealing with item stacks during the processing, then adding
 * the drops, and the product of the smelting and blocking.
 * </p> 
 */
public class OnBlockBreakPlayerManualCore
	extends AutoManagerFeatures
{

	
	public void playerSmelt( SpigotPlayer player, StringBuilder debugInfo ) {
		
		List<XMaterial> smelts = new ArrayList<>();
		
		smelts.add( XMaterial.COBBLESTONE );
		smelts.add( XMaterial.GOLD_ORE );
		smelts.add( XMaterial.NETHER_GOLD_ORE );
		smelts.add( XMaterial.DEEPSLATE_GOLD_ORE );
		smelts.add( XMaterial.RAW_GOLD );
		
		smelts.add( XMaterial.IRON_ORE );
		smelts.add( XMaterial.DEEPSLATE_IRON_ORE );
		smelts.add( XMaterial.RAW_IRON );
		
		smelts.add( XMaterial.COAL_ORE );
		smelts.add( XMaterial.DEEPSLATE_COAL_ORE );
		
		smelts.add( XMaterial.DIAMOND_ORE );
		smelts.add( XMaterial.DEEPSLATE_DIAMOND_ORE );
		
		smelts.add( XMaterial.EMERALD_ORE );
		smelts.add( XMaterial.DEEPSLATE_EMERALD_ORE );
		
		smelts.add( XMaterial.LAPIS_ORE );
		smelts.add( XMaterial.DEEPSLATE_LAPIS_ORE );
		
		smelts.add( XMaterial.REDSTONE_ORE );
		smelts.add( XMaterial.DEEPSLATE_REDSTONE_ORE );
		
		smelts.add( XMaterial.NETHER_QUARTZ_ORE );
		smelts.add( XMaterial.ANCIENT_DEBRIS );
		
		smelts.add( XMaterial.COPPER_ORE );
		smelts.add( XMaterial.DEEPSLATE_COPPER_ORE );
		smelts.add( XMaterial.RAW_COPPER );
		
		
		for ( XMaterial xMat : smelts ) {
			autoFeatureSmelt( player.getWrapper(), xMat, debugInfo );
		}

	}
	
	public void playerBlock( SpigotPlayer player, StringBuilder debugInfo ) {
		
		List<XMaterial> blocks = new ArrayList<>();
		
		blocks.add( XMaterial.GOLD_INGOT );
		blocks.add( XMaterial.IRON_INGOT );
		blocks.add( XMaterial.COAL );
		blocks.add( XMaterial.DIAMOND );
		blocks.add( XMaterial.REDSTONE );
		blocks.add( XMaterial.EMERALD );
		blocks.add( XMaterial.QUARTZ );
		blocks.add( XMaterial.PRISMARINE_SHARD );
		blocks.add( XMaterial.SNOW_BLOCK );
		blocks.add( XMaterial.GLOWSTONE_DUST );
		blocks.add( XMaterial.LAPIS_LAZULI );
		
		
		for ( XMaterial xMat : blocks ) {
			autoFeatureBlock( player.getWrapper(), xMat, debugInfo );
		}
		
	}

	
	protected XMaterial autoFeatureSmelt( Player p, XMaterial source, StringBuilder debugInfo )
	{
		XMaterial results = source;
		
		boolean isAll = isBoolean( AutoFeatures.smeltAllBlocks );
		
//		XMaterial source = SpigotUtil.getXMaterial( block.getPrisonBlock() );
		if ( source != null ) {
			
			switch ( source )
			{
				case COBBLESTONE:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltCobblestone ), source, XMaterial.STONE, p, debugInfo );
					results = XMaterial.STONE;
					break;
					
				case GOLD_ORE:
				case NETHER_GOLD_ORE:
				case DEEPSLATE_GOLD_ORE:
				case RAW_GOLD:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltGoldOre ), source, XMaterial.GOLD_INGOT, p, debugInfo );
					results = XMaterial.GOLD_INGOT;
					break;
					
				case IRON_ORE:
				case DEEPSLATE_IRON_ORE:
				case RAW_IRON:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltIronOre ), source, XMaterial.IRON_INGOT, p, debugInfo );
					results = XMaterial.IRON_INGOT;
					break;
					
				case COAL_ORE:
				case DEEPSLATE_COAL_ORE:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltCoalOre ), source, XMaterial.COAL, p, debugInfo );
					results = XMaterial.COAL;
					break;
					
				case DIAMOND_ORE:
				case DEEPSLATE_DIAMOND_ORE:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltDiamondlOre ), source, XMaterial.DIAMOND, p, debugInfo );
					results = XMaterial.DIAMOND;
					break;
					
				case EMERALD_ORE:
				case DEEPSLATE_EMERALD_ORE:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltEmeraldOre ), source, XMaterial.EMERALD, p, debugInfo );
					results = XMaterial.EMERALD;
					break;
					
				case LAPIS_ORE:
				case DEEPSLATE_LAPIS_ORE:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltLapisOre ), source, XMaterial.LAPIS_LAZULI, p, debugInfo );
					results = XMaterial.LAPIS_LAZULI;
					break;
					
				case REDSTONE_ORE:
				case DEEPSLATE_REDSTONE_ORE:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltRedstoneOre ), source, XMaterial.REDSTONE, p, debugInfo );
					results = XMaterial.REDSTONE;
					break;
					
				case NETHER_QUARTZ_ORE:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltNetherQuartzOre ), source, XMaterial.QUARTZ, p, debugInfo );
					results = XMaterial.QUARTZ;
					break;
					
				case ANCIENT_DEBRIS:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltAncientDebris ), source, XMaterial.NETHERITE_SCRAP, p, debugInfo );
					results = XMaterial.NETHERITE_SCRAP;
					break;

				// v1.17 !!
				case COPPER_ORE:
				case DEEPSLATE_COPPER_ORE:
				case RAW_COPPER:
					autoSmelt( isAll || isBoolean( AutoFeatures.smeltCopperOre ), source, XMaterial.COPPER_INGOT, p, debugInfo );
					results = XMaterial.COPPER_INGOT;
					break;
					
				default:
					break;
			}
		}
		
		
		return results;
	}

	protected void autoFeatureBlock( Player p, XMaterial source, StringBuilder debugInfo ) {

		boolean isAll = isBoolean( AutoFeatures.smeltAllBlocks );

		if ( source != null ) {
			
			// Any autoBlock target could be enabled, and could have multiples of 9, so perform the
			// checks within each block type's function call.  So in one pass, could hit on more
			// than one of these for multiple times too.
			switch ( source )
			{
				case GOLD_INGOT:
					autoBlock( isAll || isBoolean( AutoFeatures.blockGoldBlock ), source, XMaterial.GOLD_BLOCK, p, debugInfo );
					
					break;
					
				case IRON_INGOT:
					autoBlock( isAll || isBoolean( AutoFeatures.blockIronBlock ), source, XMaterial.IRON_BLOCK, p, debugInfo );
					
					break;

				case COAL:
					autoBlock( isAll || isBoolean( AutoFeatures.blockCoalBlock ), source, XMaterial.COAL_BLOCK, p, debugInfo );
					
					break;
					
				case DIAMOND:
					autoBlock( isAll || isBoolean( AutoFeatures.blockDiamondBlock ), source, XMaterial.DIAMOND_BLOCK, p, debugInfo );
					
					break;
					
				case REDSTONE:
					autoBlock( isAll || isBoolean( AutoFeatures.blockRedstoneBlock ), source,XMaterial.REDSTONE_BLOCK, p, debugInfo );
					
					break;
					
				case EMERALD:
					autoBlock( isAll || isBoolean( AutoFeatures.blockEmeraldBlock ), source, XMaterial.EMERALD_BLOCK, p, debugInfo );
					
					break;
					
				case QUARTZ:
					autoBlock( isAll || isBoolean( AutoFeatures.blockQuartzBlock ), source, XMaterial.QUARTZ_BLOCK, 4, p, debugInfo );
					
					break;
					
				case PRISMARINE_SHARD:
					autoBlock( isAll || isBoolean( AutoFeatures.blockPrismarineBlock ), source, XMaterial.PRISMARINE, 4, p, debugInfo );
					
					break;
					
				case SNOWBALL:
					autoBlock( isAll || isBoolean( AutoFeatures.blockSnowBlock ), source, XMaterial.SNOW_BLOCK, 4, p, debugInfo );
					
					break;
					
				case GLOWSTONE_DUST:
					autoBlock( isAll || isBoolean( AutoFeatures.blockGlowstone ), source, XMaterial.GLOWSTONE, 4, p, debugInfo );
					
					break;
					
				case LAPIS_LAZULI:
					autoBlock( isAll || isBoolean( AutoFeatures.blockLapisBlock ), source, XMaterial.LAPIS_BLOCK, p, debugInfo );
					
					break;
					
				case COPPER_INGOT:
					autoBlock( isAll || isBoolean( AutoFeatures.blockCopperBlock ), source, XMaterial.COPPER_BLOCK, p, debugInfo );
					
					break;
					
				default:
					break;
			}
		}

	}



	protected void autoSmelt( boolean autoSmelt, XMaterial source, XMaterial target, Player p, StringBuilder debugInfo ) {

		if ( autoSmelt && source != null && target != null ) {
			
			HashMap<Integer, SpigotItemStack> overflow = SpigotUtil.itemStackReplaceItems( p, source, target, 1 );
			dropExtra( overflow, p, debugInfo );

		}
	}
	
	
	protected void autoBlock( boolean autoBlock, XMaterial source, XMaterial target, Player p, StringBuilder debugInfo  ) {
		autoBlock(autoBlock, source, target, 9, p, debugInfo );
	}

	
	protected void autoBlock( boolean autoBlock, XMaterial source, XMaterial target, int ratio, Player p, StringBuilder debugInfo  ) {

		if ( autoBlock && source != null && target != null ) {
			HashMap<Integer, SpigotItemStack> overflow = SpigotUtil.itemStackReplaceItems( p, source, target, ratio );
			dropExtra( overflow, p, debugInfo );
			
		}
	}
	
	
	@Override
	protected int checkBonusXp( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		return bonusXp;
	}
}
