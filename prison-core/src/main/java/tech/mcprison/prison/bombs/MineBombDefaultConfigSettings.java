package tech.mcprison.prison.bombs;

import tech.mcprison.prison.bombs.MineBombEffectsData.EffectState;
import tech.mcprison.prison.bombs.MineBombs.ExplosionShape;
import tech.mcprison.prison.output.Output;

public class MineBombDefaultConfigSettings {

	
	@SuppressWarnings( "unused" )
	public void setupDefaultMineBombData(MineBombs mineBombs)
	{
		if ( mineBombs.getConfigData().getBombs().size() == 0 ) {

//			XMaterial.WOODEN_PICKAXE;
//			XMaterial.STONE_PICKAXE;
//			XMaterial.IRON_PICKAXE;
//			XMaterial.GOLDEN_PICKAXE;
//			XMaterial.DIAMOND_PICKAXE;
//			XMaterial.NETHERITE_PICKAXE;

			MineBombEffectsData mbeSound01 = new MineBombEffectsData( "ENTITY_CREEPER_PRIMED", EffectState.placed, 0 );
			MineBombEffectsData mbeSound02 = new MineBombEffectsData( "CAT_HISS", EffectState.placed, 0 );
			
			MineBombEffectsData mbeSound03 = new MineBombEffectsData( "ENTITY_GENERIC_EXPLODE", EffectState.explode, 0 );
			MineBombEffectsData mbeSound04 = new MineBombEffectsData( "ENTITY_DRAGON_FIREBALL_EXPLODE", EffectState.explode, 0 );
			
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode01 = new MineBombEffectsData( "FIREWORKS_SPARK", EffectState.placed, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode02 = new MineBombEffectsData( "BUBBLE_COLUMN_UP", EffectState.placed, 0 );
			MineBombEffectsData mbeExplode03 = new MineBombEffectsData( "ENCHANTMENT_TABLE", EffectState.placed, 0 );
			
//			MineBombEffectsData mbeExplode05 = new MineBombEffectsData( "END_ROD", EffectState.placed, 0 );
			MineBombEffectsData mbeExplode04 = new MineBombEffectsData( "FLAME", EffectState.placed, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode08 = new MineBombEffectsData( "DRAGON_BREATH", EffectState.placed, 0 );

			MineBombEffectsData mbeExplode06a = new MineBombEffectsData( "SMOKE", EffectState.placed, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode06 = new MineBombEffectsData( "SMOKE_NORMAL", EffectState.placed, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode07 = new MineBombEffectsData( "SMOKE_LARGE", EffectState.placed, 0 );
			
			MineBombEffectsData mbeExplode10 = new MineBombEffectsData( "EXPLOSION_NORMAL", EffectState.explode, 0 );
			MineBombEffectsData mbeExplode11 = new MineBombEffectsData( "EXPLOSION_LARGE", EffectState.explode, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode12 = new MineBombEffectsData( "EXPLOSION_HUGE", EffectState.explode, 0 );
			
			
			{
				MineBombData mbd = new MineBombData( 
						"SmallBomb", "brewing_stand", ExplosionShape.sphere.name(), 2, "&dSmall &6Mine &eBomb &3(lore line 1)" );
				
				mbd.setNameTag( "&6&kABC&r&c-= &7{name}&c =-&6&kCBA" );
				mbd.setItemName( MineBombData.MINE_BOMB_DEFAULT_ITEM_NAME );
				
				
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 0 );
				mbd.setDescription("A small mine bomb made with some chemicals and a brewing stand.");
				
				mbd.getLore().add( "&4Lore line 2" );
				mbd.getLore().add( "&aLore line &73" );

				mbd.addSoundEffects( mbeSound01.clone() );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.addSoundEffects( mbeSound03.clone() );
				
				mbd.addVisualEffects( mbeExplode04.clone() );
				mbd.addVisualEffects( mbeExplode03.clone().setOffsetTicks( 30 ) );
				
				mbd.addVisualEffects( mbeExplode06a.clone() );
				mbd.addVisualEffects( mbeExplode10.clone() );
				mbd.addVisualEffects( mbeExplode06.clone() );
				
				mbd.setCooldownTicks( 10 );

				mineBombs.getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
				
			}
			
			{
				MineBombData mbd = new MineBombData( 
						"MediumBomb", "firework_rocket", ExplosionShape.sphere.name(), 5, "Medium Mine Bomb" );
				mbd.setDescription("A medium mine bomb made from leftover fireworks, " +
						"but supercharged with a strange green glowing liquid.");
				
				mbd.setNameTag( "&6&k1 23 456&r&a-=- &7{name}&a -=-&6&k654 32 1" );
				mbd.setItemName( MineBombData.MINE_BOMB_DEFAULT_ITEM_NAME );

				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 3 );
				
				mbd.addSoundEffects( mbeSound01.clone() );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.addSoundEffects( mbeSound03.clone() );
				
				mbd.addVisualEffects( mbeExplode04.clone() );
				mbd.addVisualEffects( mbeExplode03.clone().setOffsetTicks( 30 ) );
				
				mbd.addVisualEffects( mbeExplode10.clone() );
				mbd.addVisualEffects( mbeExplode06.clone() );

				mbd.setCooldownTicks( 60 );
				
				mineBombs.getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
				
			}

			{
				MineBombData mbd = new MineBombData( 
						"LargeBomb", "tnt", ExplosionShape.sphereHollow.name(), 12, "Large Mine Bomb" );
				
				mbd.setNameTag( "&a-=- &7{name}&a -=--" );
				mbd.setItemName( MineBombData.MINE_BOMB_DEFAULT_ITEM_NAME );

				mbd.setRadiusInner( 3 );
				mbd.setDescription("A large mine bomb made from TNT with some strange parts " +
						"that maybe be described as alien technology.");
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 3 );
				
				mbd.addSoundEffects( mbeSound01.clone() );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.addSoundEffects( mbeSound03.clone().setVolumne( 2.0f ) );
				
				mbd.addVisualEffects( mbeExplode04.clone() );
				mbd.addVisualEffects( mbeExplode03.clone().setOffsetTicks( 30 ) );
				
				mbd.addVisualEffects( mbeExplode10.clone() );
				mbd.addVisualEffects( mbeExplode06.clone() );
				mbd.addVisualEffects( mbeExplode06a.clone() );

				mbd.setCooldownTicks( 60 );
				
				mineBombs.getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
			}
			
			{
				MineBombData mbd = new MineBombData( 
						"OofBomb", "tnt_minecart", ExplosionShape.sphereHollow.name(), 21, "Oof Mine Bomb" );
				mbd.setRadiusInner( 3 );
				
				mbd.setNameTag( "&c&k1&6&k23&e&k456&r&a-=- &4{countdown} &5-=- &7{name} &5-=- &4{countdown} &a-=-&e&k654&6&k32&c&k1" );
				mbd.setItemName( MineBombData.MINE_BOMB_DEFAULT_ITEM_NAME );
				
				mbd.setDescription("An oof-ably large mine bomb made with a minecart heaping with TNT.  " +
						"Unlike the large mine bomb, this one obviously is built with alien technology.");
				
				mbd.setToolInHandName( "GOLDEN_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 13 );
				
				mbd.addSoundEffects( mbeSound01.clone() );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 10 ).setVolumne( 0.25f ).setPitch( 0.25f ) );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 20 ).setVolumne( 0.5f ).setPitch( 0.5f ) );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 30 ).setVolumne( 1.0f ).setPitch( 0.75f ) );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 40 ).setVolumne( 2.0f ).setPitch( 1.5f ) );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 50 ).setVolumne( 5.0f ).setPitch( 2.5f ) );
				
				mbd.addSoundEffects( mbeSound03.clone().setOffsetTicks( 0 ).setVolumne( 3.0f ) );
				mbd.addSoundEffects( mbeSound04.clone().setOffsetTicks( 5 ).setVolumne( 1.5f ) );
				mbd.addSoundEffects( mbeSound03.clone().setOffsetTicks( 10 ).setVolumne( 2.5f ) );
				mbd.addSoundEffects( mbeSound04.clone().setOffsetTicks( 15 ).setVolumne( 1.0f ) );
				mbd.addSoundEffects( mbeSound03.clone().setOffsetTicks( 20 ).setVolumne( 2.0f ) );
				mbd.addSoundEffects( mbeSound04.clone().setOffsetTicks( 25 ).setVolumne( 0.75f ) );
				mbd.addSoundEffects( mbeSound03.clone().setOffsetTicks( 30 ).setVolumne( 1.5f ) );
				mbd.addSoundEffects( mbeSound04.clone().setOffsetTicks( 35 ).setVolumne( 0.55f ) );
				mbd.addSoundEffects( mbeSound03.clone().setOffsetTicks( 40 ).setVolumne( 1.0f ) );
				mbd.addSoundEffects( mbeSound04.clone().setOffsetTicks( 45 ).setVolumne( 0.25f ) );
				mbd.addSoundEffects( mbeSound03.clone().setOffsetTicks( 50 ).setVolumne( 0.5f ) );
				mbd.addSoundEffects( mbeSound04.clone().setOffsetTicks( 55 ).setVolumne( 0.15f ) );
				
				
				mbd.addVisualEffects( mbeExplode06.clone() );
				mbd.addVisualEffects( mbeExplode06a.clone() );
				mbd.addVisualEffects( mbeExplode03.clone() );
				mbd.addVisualEffects( mbeExplode12.clone() );
				mbd.addVisualEffects( mbeExplode12.clone().setOffsetTicks( 30 ) );
				mbd.addVisualEffects( mbeExplode12.clone().setOffsetTicks( 60 ) );
				mbd.addVisualEffects( mbeExplode07.clone().setOffsetTicks( 60 ) );
				mbd.addVisualEffects( mbeExplode08.clone().setOffsetTicks( 90 ) );
				
				mbd.addVisualEffects( mbeExplode10.clone() );
				mbd.addVisualEffects( mbeExplode06.clone().setOffsetTicks( 20 ) );
				
				mbd.setAutosell( true );
				mbd.setGlowing( true );
				mbd.setAutosell( true );
				
				mbd.setCooldownTicks( 60 );
				mbd.setFuseDelayTicks( 13 * 20 ); // 13 seconds

				mineBombs.getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
			}
			
			{
				MineBombData mbd = new MineBombData( 
						"WimpyBomb", "GUNPOWDER", ExplosionShape.sphere.name(), 5, 
							"A Wimpy Mine Bomb" );
//				mbd.setLoreBombItemId( "&7A &2Wimpy &cBomb &9...&02A3F" );
				
				mbd.setNameTag( "&7A &2Wimpy &cBomb" );
				mbd.setItemName( MineBombData.MINE_BOMB_DEFAULT_ITEM_NAME );
				
				mbd.setRadiusInner( 2 );
				mbd.setDescription("A whimpy bomb made with gunpowder and packs the punch of a " +
						"dull wooden pickaxe. For some reason, it only has a 40% chance of removing " +
						"a block.");
				
				mbd.getLore().add( "" );
				mbd.getLore().add( "A whimpy bomb made with gunpowder and packs the punch " );
				mbd.getLore().add( "of a dull wooden pickaxe. For some reason, it only " );
				mbd.getLore().add( "has a 40% chance of removing a block." );
				mbd.getLore().add( "" );
				mbd.getLore().add( "Not labeled for retail sale." );
				
				mbd.setToolInHandName( "WOODEN_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 0 );
				mbd.setRemovalChance( 40.0d );
				
				mbd.getAllowedMines().add( "a" );
				mbd.getAllowedMines().add( "b" );
				mbd.getAllowedMines().add( "c" );
				
				mbd.getPreventedMines().add( "d" );
				mbd.getPreventedMines().add( "e" );
				
				mbd.addSoundEffects( mbeSound01.clone() );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.addSoundEffects( mbeSound03.clone() );
				
				mbd.addVisualEffects( mbeExplode01.clone() );
				mbd.addVisualEffects( mbeExplode02.clone().setOffsetTicks( 30 ) );
				mbd.addVisualEffects( mbeExplode03.clone().setOffsetTicks( 10 ) );
				mbd.addVisualEffects( mbeExplode04.clone() );
				
				mbd.addVisualEffects( mbeExplode10.clone() );
				mbd.addVisualEffects( mbeExplode06.clone() );
				mbd.addVisualEffects( mbeExplode06a.clone() );
				mbd.addVisualEffects( mbeExplode11.clone().setOffsetTicks( 05 ) );
				
				mbd.setCooldownTicks( 3 * 20 ); // 3 seconds
				mbd.setFuseDelayTicks( 2 * 20 ); // 2 seconds

				mbd.setGlowing( true );
				mbd.setGravity( false );
				
				mbd.setCooldownTicks( 5 );

				mineBombs.getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
			}
			
			
			{
				MineBombData mbd = new MineBombData( 
						"CubeBomb", "SLIME_BLOCK", ExplosionShape.cube.name(), 2, 
						"A Cubic Bomb" );
				mbd.setDescription("The most anti-round bomb you will ever be able to find. " +
						"It's totally cubic.");
				
				mbd.setNameTag( "&a-=- &7{name}&a -=--" );
				mbd.setItemName( MineBombData.MINE_BOMB_DEFAULT_ITEM_NAME );
				
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 7 );
				mbd.setRemovalChance( 100.0d );
				
				mbd.addSoundEffects( mbeSound01.clone() );
				mbd.addSoundEffects( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.addSoundEffects( mbeSound03.clone() );
				
				mbd.addVisualEffects( mbeExplode04.clone() );
				mbd.addVisualEffects( mbeExplode02.clone().setOffsetTicks( 30 ) );
				
				mbd.setGlowing( true );
				
				mbd.setCooldownTicks( 60 );

				mineBombs.getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
			}
			
			
			mineBombs.saveConfigJson();
			
			Output.get().logInfo( "Mine bombs: setup default values." );
		}
		else {
			Output.get().logInfo( "Could not generate a mine bombs save file since at least one " +
						"mine bomb already exists." );
		}
		
	}

}
