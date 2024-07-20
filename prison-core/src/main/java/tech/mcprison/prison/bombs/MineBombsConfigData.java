package tech.mcprison.prison.bombs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import tech.mcprison.prison.file.FileIOData;

public class MineBombsConfigData
	implements FileIOData,
				Comparable<MineBombsConfigData>
{
	/**
	 * <p>If the format of this class, or any other variables and their
	 * classes change that would effect the structure of the save file,
	 * then increment this variable by 1.  This will force the saved
	 * files on the servers to be updated to the latest format of the
	 * data.
	 * </p>
	 */
	public static final int MINE_BOMB_DATA_FORMAT_VERSION = 3;
//	public static final int MINE_BOMB_DATA_FORMAT_VERSION = 4;
	
	private int dataFormatVersion = 0;
	
	private Map<String, MineBombData> bombs;
	
	public MineBombsConfigData() {
		super();
		
		this.dataFormatVersion = 0;
		
		this.bombs = new TreeMap<>();
	}

	public int getDataFormatVersion() {
		return dataFormatVersion;
	}
	public void setDataFormatVersion( int dataFormatVersion ) {
		this.dataFormatVersion = dataFormatVersion;
	}

	public boolean validateMineBombEffects() {
		boolean results = false;
		
		Set<String> keys = getBombs().keySet();
		for ( String key : keys ) {
			MineBombData bomb = getBombs().get( key );
			
			TreeSet<MineBombEffectsData> sounds = new TreeSet<>( bomb.getSoundEffects() );
			bomb.getSoundEffects().clear();
			for ( MineBombEffectsData sound : sounds ) {
				if ( bomb.addSoundEffects( sound ) && !results ) {
					results = true;
				}
			}
			
			TreeSet<MineBombEffectsData> visuals = new TreeSet<>( bomb.getVisualEffects() );
			bomb.getVisualEffects().clear();
			for ( MineBombEffectsData visual : visuals ) {
				if ( bomb.addVisualEffects( visual ) && !results ) {
					results = true;
				}
			}
			
			
//			// Check all lore, and if it contains `&#` then process the hex color codes:
//			for ( int i = 0; i < bomb.getLore().size(); i++ ) {
//				String lore = bomb.getLore().get( i );
//				
//				if ( lore != null && lore.contains( "&#") ) {
//					String hex = Text.translateAmpColorCodesAltHex2Code( lore );
//					bomb.getLore().set( i, hex );
//				}
//			}
//			
//			// Convert hex colors for bomb names and tags:.
//			if ( bomb.getName().contains( "&#" ) ) {
//				String hex = Text.translateAmpColorCodesAltHex2Code( bomb.getName() );
//				bomb.setName( hex );
//			}
//			if ( bomb.getNameTag().contains( "&#" ) ) {
//				String hex = Text.translateAmpColorCodesAltHex2Code( bomb.getNameTag() );
//				bomb.setNameTag( hex );
//			}
			
			
			
		}
		
		return results;
	}
	
	public Map<String, MineBombData> getBombs() {
		return bombs;
	}
	public void setBombs( Map<String, MineBombData> bombs ) {
		this.bombs = bombs;
	}

	
	/**
	 * This compareTo function will check to see if the two 
	 * MineBombsConfigData objects have the same number of 
	 * bombs, and they that have the same bomb names.
	 * 
	 * If the bomb names match, then it compares each bomb.
	 * 
	 */
	@Override
	public int compareTo(MineBombsConfigData o) {
		int results = 0;
		
		if ( o == null ) {
			results = -1;
		}
		
		if ( results == 0 ) {
			results = Integer.compare( getDataFormatVersion(), o.getDataFormatVersion() );
			
			if ( results == 0 ) {
				results = Integer.compare( getBombs().size(), o.getBombs().size() );
				
				if ( results == 0 ) {
					List<String> keys = new ArrayList<>( getBombs().keySet() );
					List<String> keysO = new ArrayList<>( o.getBombs().keySet() );
					
					for ( int i = 0; i < keys.size(); i++ ) {
						
						String key = keys.get(i);
						String keyO = keysO.get(i);
						
						results = key.compareTo(keyO);
						
						if ( results == 0 ) {
							MineBombData mBomb = getBombs().get(key);
							MineBombData mBombO = o.getBombs().get(keyO);
							
							mBomb.compareTo( mBombO );
						}
						
						if ( results != 0 ) {
							break;
						}
					}
					
				}
				
			}
		}
		
		return results;
	}
}
