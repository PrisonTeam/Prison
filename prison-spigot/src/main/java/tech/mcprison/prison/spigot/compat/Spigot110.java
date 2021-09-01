package tech.mcprison.prison.spigot.compat;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Important note: Spigot 1.10 support is represented in Spigot110 and is
 * identical to Spigot19 except for two spigot 1.10 functions: sendTitle()
 * and sendActionBar(). Therefore, all that needs to be done is to have 
 * the class Spigot110Player extend Spigot19 and then Override 
 * those two functons.
 *
 */
public class Spigot110
	extends Spigot110Player
	implements Compatibility {

	@Override
	public void setItemInMainHand(Player p, ItemStack itemStack){
		p.getInventory().setItemInMainHand(itemStack);
	}

}
