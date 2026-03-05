package tech.mcprison.prison.wip.discord;

import static org.junit.Assert.*;

import org.junit.Test;

import tech.mcprison.prison.discord.PrisonSupportFileLinkage;
import tech.mcprison.prison.discord.PrisonSupportFileLinkage.PrimaryLinkages;

public class PrisonSupportFileLinkageTest extends PrisonSupportFileLinkage {

	@Test
	public void test() {

		PrisonSupportFileLinkage psfl = new PrisonSupportFileLinkage();
		
		String test1 = "||Ladder default||";
		String test2 = "Some regular text";
		String test3 = "||Ladder cats||";
		String test4 = "||Invalid Primary||";
		
		
		// This should be ignored since it's not a linkage:
		psfl.addLinkage(test2);
		
		assertEquals( 0, psfl.getPrimaries().size() );
		
		
		
		// This should be rejected because the primary is not a valid one:
		psfl.addLinkage(test4);
		
		assertEquals( 0, psfl.getPrimaries().size() );

		
		
		// This should result in the first one being added:
		psfl.addLinkage(test1);
		
		assertEquals( 1, psfl.getPrimaries().size() );
		assertEquals( 1, psfl.getPrimaries().get( PrimaryLinkages.Ladder ).size() );
		
		
		// 
		psfl.addLinkage(test3);
		
		assertEquals( 1, psfl.getPrimaries().size() );
		assertEquals( 2, psfl.getPrimaries().get( PrimaryLinkages.Ladder ).size() );
		
		
	}

}
