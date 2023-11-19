package tech.mcprison.prison.spigot.compat;

public class Spigot_1_18 
	extends Spigot_1_14
	implements Compatibility
{

    
    @Override
    public int getMinY() {
    	return -64;
    }
    
    @Override
    public int getMaxY() {
    	return 320;
    }
	
}
