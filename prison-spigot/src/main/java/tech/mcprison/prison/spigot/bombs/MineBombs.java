package tech.mcprison.prison.spigot.bombs;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.util.Location;

public class MineBombs
{
	
	public enum ExplosionShape {
		sphere,
		sphereHollow
		;
		
	}

	public List<Location> calculateSphere( Location loc, int radius, boolean hollow ) {
		List<Location> results = new ArrayList<>();
		
		if ( loc != null && radius > 0 ) {
			int cenX = loc.getBlockX();
			int cenY = loc.getBlockY();
			int cenZ = loc.getBlockZ();
			
			double radiusSqr = radius * radius;
			double radiusHSqr = (radius - 1) * (radius - 1);
			
			for ( int x = cenX - radius ; x <= cenX + radius ; x++ ) {
				double xSqr = (cenX - x) * (cenX - x);
				for ( int y = cenY - radius ; y <= cenY + radius ; y++ ) {
					double ySqr = (cenY - y) * (cenY - y);
					for ( int z = cenZ - radius ; z <= cenZ + radius ; z++ ) {
						double zSqr = (cenZ - z) * (cenZ - z);
						
						double distSqr = xSqr + ySqr + zSqr;
						
						if ( distSqr <= radiusSqr &&
								(!hollow || 
								 hollow && distSqr >= radiusHSqr )) {
							
							Location l = new Location( loc.getWorld(), x, y, z );
							results.add( l );
						}
					}
				}
			}
		}
		return results;
	}
}
