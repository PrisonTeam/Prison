package tech.mcprison.prison.spigot.integrations;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.badbones69.crazyenchantments.api.CrazyEnchantments;
import me.badbones69.crazyenchantments.api.enums.CEnchantments;
import me.badbones69.crazyenchantments.api.events.EnchantmentUseEvent;
import me.badbones69.crazyenchantments.api.objects.CEnchantment;
import tech.mcprison.prison.output.Output;

public class IntegrationCrazyEnchantmentsPickaxes
//	implements Listener
{
	private static IntegrationCrazyEnchantmentsPickaxes instance = null;
	
	private CrazyEnchantments ce = null;
	
	private IntegrationCrazyEnchantmentsPickaxes() {
		super();
		
		try {
			Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("CrazyEnchantments");
			
			if ( bukkitPlugin != null ) {
				
//				Bukkit.getPluginManager().registerEvents( this, bukkitPlugin);
				ce = CrazyEnchantments.getInstance();
			}
		}
		catch ( Exception e ) {
			Output.get().logWarn( "Unable to Minepacks integration.", e );
		}
	}
	
	public static IntegrationCrazyEnchantmentsPickaxes getInstance() {
		if ( instance == null ) {
			synchronized( IntegrationCrazyEnchantmentsPickaxes.class ) {
				if ( instance == null ) {
					instance = new IntegrationCrazyEnchantmentsPickaxes();
				}
			}
		}
		return instance;
	}
	
	public boolean isEnabled() {
		return ce != null;
	}
	
	public int getPickaxeEnchantmentExperienceBonus( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
        List<CEnchantment> enchantments = ce.getEnchantmentsOnItem(item);
        boolean isOre = isOre(block.getType());

        if (CEnchantments.EXPERIENCE.isActivated() && !hasSilkTouch(item) && isOre && 
        		(enchantments.contains(CEnchantments.EXPERIENCE.getEnchantment()) && 
        				!(enchantments.contains(CEnchantments.BLAST.getEnchantment()) || 
        						enchantments.contains(CEnchantments.TELEPATHY.getEnchantment())))) {
            int power = ce.getLevel(item, CEnchantments.EXPERIENCE);
            if (CEnchantments.EXPERIENCE.chanceSuccessful(item)) {
                EnchantmentUseEvent event = new EnchantmentUseEvent(player, CEnchantments.EXPERIENCE, item);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    bonusXp = (power + 2);
                }
            }
        }
        
        return bonusXp;
	}
	
//	@EventHandler
//	public void onBlockBreak( BlockBreakEvent e ) {
//		
//		if (e.isCancelled() || ce.isIgnoredEvent(e)) return;
//		
//		Block block = e.getBlock();
//		Player player = e.getPlayer();
//		ItemStack item = Methods.getItemInHand(player);
//		List<CEnchantment> enchantments = ce.getEnchantmentsOnItem(item);
//		boolean isOre = isOre(block.getType());
//		
//		if (CEnchantments.EXPERIENCE.isActivated() && !hasSilkTouch(item) && isOre && (enchantments.contains(CEnchantments.EXPERIENCE.getEnchantment()) && !(enchantments.contains(CEnchantments.BLAST.getEnchantment()) || enchantments.contains(CEnchantments.TELEPATHY.getEnchantment())))) {
//			int power = ce.getLevel(item, CEnchantments.EXPERIENCE);
//			if (CEnchantments.EXPERIENCE.chanceSuccessful(item)) {
//				EnchantmentUseEvent event = new EnchantmentUseEvent(player, CEnchantments.EXPERIENCE, item);
//				Bukkit.getPluginManager().callEvent(event);
//				if (!event.isCancelled()) {
//					e.setExpToDrop(e.getExpToDrop() + (power + 2));
//				}
//			}
//		}
//	}
	
    private boolean hasSilkTouch(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
    }
    
    private boolean isOre(Material material) {
        if (material == ce.getMaterial("NETHER_QUARTZ_ORE", "QUARTZ_ORE")) {
            return true;
        }
        switch (material) {
            case COAL_ORE:
            case IRON_ORE:
            case GOLD_ORE:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case LAPIS_ORE:
            case REDSTONE_ORE:
                return true;
            default:
                return false;
        }
    }
    
}
