/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tech.mcprison.prison.TestWorld;

/**
 * @author Faizaan A. Datoo
 */
public class BoundsTest {

    @Test 
    public void equals() throws Exception {
        Bounds bounds =
            new Bounds(new Location(null, 0.0, 0.0, 0.0), new Location(null, 10.0, 10.0, 10.0));
        Bounds otherBounds =
            new Bounds(new Location(null, 0.0, 0.0, 0.0), new Location(null, 10.0, 10.0, 10.0));

        // Since world is null, this cannot be true:
        // Bounds should never exist without a world, otherwise it is virtual.
        assertFalse(bounds.equals(otherBounds));

        // NOTE: How would this ever be true???
//        assertTrue(bounds.equals(otherBounds));
        
        TestWorld world1 = new TestWorld("test1");
        TestWorld world2 = new TestWorld("test2");
        
        bounds.setWorld( world1 );
        otherBounds.setWorld( world2 );
        
        assertFalse(bounds.equals(otherBounds));

        otherBounds.setWorld( world1 );
        
        assertTrue(bounds.equals(otherBounds));
        
        otherBounds.getMax().setX( 10.1 );
        
        assertTrue(bounds.equals(otherBounds));
    }
    
    @Test 
    public void equalsInSameBlock() throws Exception {
    	
    	TestWorld world1 = new TestWorld("test1");
    	
    	Bounds bounds =
    			new Bounds(new Location(world1, 0.0, 0.0, 0.0), new Location(world1, 10.0, 10.0, 10.0));
    	Bounds otherBounds =
    			new Bounds(new Location(world1, 0.0, 0.0, 0.0), new Location(world1, 10.0, 10.0, 10.0));

    	
    	// Check to ensure these are identical before changing anything:
    	assertTrue(bounds.equals(otherBounds));

    	
    	// Confirm that it will fail if outside of existing block:
    	otherBounds.getMax().setX( 9.0 );
    	assertFalse(bounds.equals(otherBounds));
    	

    	// Now try a somewhere in the original block, using fractions:
    	otherBounds.getMax().setX( 10.001 );
    	assertTrue(bounds.equals(otherBounds));
    	
    	// And a few other variations:
    	otherBounds.getMax().setX( 10.5 );
    	assertTrue(bounds.equals(otherBounds));
    	
    	otherBounds.getMax().setX( 10.999999 );
    	assertTrue(bounds.equals(otherBounds));

    	otherBounds.getMax().setX( 10.000001 );
    	otherBounds.getMax().setY( 10.999999 );
    	otherBounds.getMax().setY( 10.5 );
    	assertTrue(bounds.equals(otherBounds));
    	
    	
    	assertTrue(bounds.equals(otherBounds));
    	
    	
    }
    
//    @Test 
//    public void fail() throws Exception {
//    	assertTrue( true );
//    }

    @Test 
    public void within() throws Exception {
    	TestWorld world1 = new TestWorld("test1");
//    	TestWorld world2 = new TestWorld("test2");
    	Location loc1 = new Location(world1, 0.0, 4.0, 0.0);
    	Location loc2 = new Location(world1, 10.0, 10.0, 10.0);
    	Location loc3 = new Location(world1, 5.0, 5.0, 5.0);
    	
        Bounds bounds = new Bounds(loc1, loc2);
        Location toCheck = loc3;
        assertTrue("Failed bounds check with null worlds", bounds.within(toCheck));

        toCheck.setY(11.0);
        assertFalse("Y should be out of bounds", bounds.within(toCheck));
        toCheck.setY(1.0);
        assertFalse("Y should be out of bounds", bounds.within(toCheck));
        
        toCheck.setY(10.0);
        assertTrue("Y should be within bounds", bounds.within(toCheck));
        toCheck.setY(6.0);
        assertTrue("Y should be within bounds", bounds.within(toCheck));
        
        // Feet of player is at the bottom layer of the mine:
        toCheck.setY( 4.0 );
        assertTrue("Y should be within bounds", bounds.within(toCheck));
        
        // Feet of player is one layer below of the mine and should be marked as 
        // within the mine since the player's head is within the mine:
        toCheck.setY( 3.0 );
        assertTrue("Y should be within bounds", bounds.withinIncludeTopBottomOfMine(toCheck));
        assertFalse("Y should NOT be within bounds", bounds.within(toCheck));
              
        // Feet of player is one layer below of the mine and should be marked as 
        // within the mine since the player's head is within the mine:
        toCheck.setY( 2.0 );
        assertFalse("Y should be out of bounds", bounds.within(toCheck));
        
        
        // TODO disable until junit is functional
//        loc1.setWorld(world1);
//        loc2.setWorld(world1);
//        assertFalse("Failed bounds check with check world being null", bounds.within(toCheck));
//        
//        loc3.setWorld(world1);
//        assertTrue("Failed bounds check all worlds should be identical", bounds.within(toCheck));
//        
//        loc3.setWorld(world2);
//        assertFalse("Failed bounds check with check world not matching the other worlds", bounds.within(toCheck));
        
    }

}
