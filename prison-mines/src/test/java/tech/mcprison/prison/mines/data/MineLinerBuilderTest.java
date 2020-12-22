package tech.mcprison.prison.mines.data;

import static org.junit.Assert.*;

import org.junit.Test;

import tech.mcprison.prison.mines.features.MineLinerBuilder;
import tech.mcprison.prison.util.Bounds.Edges;

public class MineLinerBuilderTest
		extends
		MineLinerBuilder
{

	@Test
	public void apply2Dto3DPattern()
	{
		String[][] pattern2d = {
				{ "a", "b" },
				{ "c", "d" }
		};
		
		getPattern3d().clear();
		
		apply2Dto3DPattern( Edges.bottom, pattern2d );
		
		assertEquals( 2, getPattern3d().size() );
	}

}
