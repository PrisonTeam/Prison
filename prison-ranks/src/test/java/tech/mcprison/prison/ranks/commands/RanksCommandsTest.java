package tech.mcprison.prison.ranks.commands;

import static org.junit.Assert.*;

import org.junit.Test;

public class RanksCommandsTest
		extends RanksCommands
{

	@Test
	public final void testPadRankName() {
		
		assertEquals("A A&8......", padRankName("A", "A", 3, 5, true) );
		assertEquals("A [A]&8....", padRankName("A", "[A]", 3, 5, true) );
		assertEquals("A &7[&3A&7]&8....", padRankName("A", "&7[&3A&7]", 3, 5, true) );
		assertEquals("Aa &7[&3Aa&7]&8..", padRankName("Aa", "&7[&3Aa&7]", 3, 5, true) );
		assertEquals("Aaa &7[&3Aaa&7]&8", padRankName("Aaa", "&7[&3Aaa&7]", 3, 5, true) );
		
		assertEquals("A&8....", padRankName("A", "A", 3, 5, false) );
		assertEquals("[A]&8..", padRankName("A", "[A]", 3, 5, false) );
		assertEquals("&7[&3A&7]&8..", padRankName("A", "&7[&3A&7]", 3, 5, false) );
		assertEquals("&7[&3Aa&7]&8.", padRankName("Aa", "&7[&3Aa&7]", 3, 5, false) );
		assertEquals("&7[&3Aaa&7]&8", padRankName("Aaa", "&7[&3Aaa&7]", 3, 5, false) );

	}

}
