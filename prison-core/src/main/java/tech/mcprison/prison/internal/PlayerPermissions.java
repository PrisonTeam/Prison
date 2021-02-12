package tech.mcprison.prison.internal;

import java.util.List;

public interface PlayerPermissions {


    /**
     * Returns whether the player is a server operator or not.
     *
     * @return true if the player is an operator, false otherwise.
     */
    boolean isOp();
    
    
    public void recalculatePermissions();
    
    /**
     * Returns true if the command sender has access to the permission specified.
     *
     * @param perm The permission to check.
     */
    boolean hasPermission(String perm);
    
    
    public List<String> getPermissions();

    
    public List<String> getPermissions( String prefix );
    
    
    public double getSellAllMultiplier();
    

}
