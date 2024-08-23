package tech.mcprison.prison.internal;

import java.util.List;

public interface PlayerPermissions {


    /**
     * Returns whether the player is a server operator or not.
     *
     * @return true if the player is an operator, false otherwise.
     */
    public boolean isOp();
    
    
    public void recalculatePermissions();
    
    /**
     * Returns true if the command sender has access to the permission specified.
     *
     * @param perm The permission to check.
     */
    public boolean hasPermission(String perm);
    
    
    public List<String> getPermissions();

    
    public List<String> getPermissions( String prefix );
    
    public List<String> getPermissions( String prefix, List<String> perms );
    
    
    public double getSellAllMultiplier();
    
    public double getSellAllMultiplierDebug();
    
    
    public List<String> getPermissionsIntegrations( boolean detailed );
    
}
