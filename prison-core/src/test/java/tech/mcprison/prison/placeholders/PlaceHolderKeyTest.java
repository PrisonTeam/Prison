package tech.mcprison.prison.placeholders;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;

public class PlaceHolderKeyTest
		//extends PlaceHolderKey
{
	public PlaceHolderKeyTest() {
		super();
		
	}

	@Test
	public void test()
	{
		/**
		[14:04:20] [Async Chat Thread - #0/INFO]: &8| &3Prison &8| &7 &cFailure to generate log message due to incorrect number of parameters: [Format specifier '%1$s'] :: Original raw message [### ChatHandler - before [\Q%s\E]  after [\Q%s\E]] Arguments: [<{prison_rank_tag}{prison_rank}{prison_r}{prison_rank_default}{PRISON_RANK}{PRISON_RANK_TAG}[{prison_rc} {prison_rcp}]:%1$s>%2$s] [<{prison_rank_tag}{prison_rank}{prison_r}{prison_rank_default}{PRISON_RANK}{PRISON_RANK_TAG}[{prison_rc} {prison_rcp}]:%1$s>%2$s]
		[14:04:20] [Async Chat Thread - #0/INFO]: <{prison_rank_tag}{prison_rank}{prison_r}{prison_rank_default}{PRISON_RANK}{PRISON_RANK_TAG}[{prison_rc} {prison_rcp}]:RoyalBlueRanger>hey
		
		*/
		
		List<PlaceHolderKey> placeHolderKeys = new ArrayList<>();
		
		PlaceHolderKey key1 = new PlaceHolderKey( PrisonPlaceHolders.prison_rank.name(), PrisonPlaceHolders.prison_rank, true );
		PlaceHolderKey key2 = new PlaceHolderKey( PrisonPlaceHolders.prison_rank_tag.name(), PrisonPlaceHolders.prison_rank_tag, true );

		placeHolderKeys.add( key1 );
		placeHolderKeys.add( key2 );
		
		String testText1 = "<{prison_rank_tag}{prison_rank}>";
		String testText2 = "<{prison_rank_tag}{prison_r}>";
		String testText3 = "<{prison_rank_default}{prison_rank_tag}>";

		String testText4 = "<{prison_rank_tag}{prison_rank::some:annotation:goes:here}>";

		String testText5 = "<{prison_rank_tag}{Prison_Rank}>";
		String testText6 = "<{prison_rank_tag}{PRISON_RANK}>";

//		String testText7 = "<{prison_rank_tag}{prison_rank}{prison_r}{prison_rank_default}" +
//				"{PRISON_RANK}{PRISON_RANK_TAG}[{prison_rc} {prison_rcp}";

		
		PlaceholderResults results1 = key1.getIdentifier( testText1 );
		PlaceholderResults results2 = key1.getIdentifier( testText2 );
		PlaceholderResults results3 = key1.getIdentifier( testText3 );
		
		assertTrue( results1.hasResults() );
		assertEquals( "prison_rank", results1.getIdentifier() );
		
		assertFalse( results2.hasResults() );
		assertNull( results2.getIdentifier() );
		
		assertFalse( results3.hasResults() );
		assertNull( results3.getIdentifier() );
		
		
		
		PlaceholderResults results4 = key2.getIdentifier( testText3 );
		
		assertTrue( results4.hasResults() );
		assertEquals( "prison_rank_tag", results4.getIdentifier() );

		
		
		PlaceholderResults results5 = key1.getIdentifier( testText4 );
		
		assertTrue( results5.hasResults() );
		assertEquals( "prison_rank::some:annotation:goes:here", results5.getIdentifier() );
		
		
		PlaceholderResults results6 = key1.getIdentifier( testText5 );
		PlaceholderResults results7 = key1.getIdentifier( testText6 );

		assertTrue( results6.hasResults() );
		assertEquals( "Prison_Rank", results6.getIdentifier() );
		
		assertTrue( results7.hasResults() );
		assertEquals( "PRISON_RANK", results7.getIdentifier() );
		
		
		
		String testText10 = "<{prison_rANk_tag}{prison_rank_default}{PRISON_RANK}[{prison_rc} {prison_rcp}]";
		
		PlaceholderResults results10a = key1.getIdentifier( testText10 );
		PlaceholderResults results10b = key2.getIdentifier( testText10 );

		
		assertTrue( results10a.hasResults() );
		assertEquals( "PRISON_RANK", results10a.getIdentifier() );

		
		assertTrue( results10b.hasResults() );
		assertEquals( "prison_rANk_tag", results10b.getIdentifier() );
		
		
	}

}
