package tech.mcprison.prison.mines.data;

import tech.mcprison.prison.mines.data.MineLinerBuilder.LinerPatterns;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Bounds.Edges;

public class MineMover
{

	public MineMover() {
		
	}
	
	public void moveMine( Mine mine, Edges edge, int amount ) {
		
		mine.clearMine( false );
		
		new MineLinerBuilder( mine, Edges.top, LinerPatterns.repair );
		new MineLinerBuilder( mine, Edges.bottom, LinerPatterns.repair );
		new MineLinerBuilder( mine, Edges.walls, LinerPatterns.repair );

		while ( amount-- > 0 ) {
    		
    		Bounds newBounds = new Bounds( mine.getBounds(), edge, 1 );
    		Bounds newerBounds = new Bounds( newBounds, edge.oppositeEdge(), -1 );
    		mine.setBounds( newerBounds );
    	}

		// Finally trace the mine:
		//mine.clearMine( true );
	}
}