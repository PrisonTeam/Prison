package tech.mcprison.prison.wip.internal.heads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class MinecraftHeadsCache {

	private static MinecraftHeadsCache instance;
	
	private Date headDataDownloadDate;
	
	private transient TreeSet<String> tags;
	
	private List<MinecraftHeadData> headData;

	private transient TreeMap<MinecraftHeadsCategory, List<MinecraftHeadData>> headDataByCategory;

	private transient TreeMap<String, MinecraftHeadData> headDataByUuid;
	
	private transient TreeMap<String, List<MinecraftHeadData>> headDataByTags;
	
	public enum MinecraftHeadsCategory {
		alphabet, 
		animals, 
		blocks, 
		decoration, 
		fooddrink("food-drinks"), 
		humans, 
		humanoid, 
		miscellaneous, 
		monsters, 
		plants;
		
		private final String value;
		private MinecraftHeadsCategory() {
			this.value = this.name();
		}
		private MinecraftHeadsCategory( String value ) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	private MinecraftHeadsCache() {
		super();
		
		this.headDataDownloadDate = null;
		
		this.tags = new TreeSet<>();
		
		this.headData = new ArrayList<>();
		
		this.headDataByUuid = new TreeMap<>();
		
		this.headDataByTags = new TreeMap<>();
	}
	
	public static MinecraftHeadsCache getInstance() {
		if ( instance == null ) {
				
			synchronized ( MinecraftHeadsCache.class ) {
				if ( instance == null ) {
					instance = new MinecraftHeadsCache();
				}
			}
		}
		
		return instance;
	}
	
	
	/**
	 * <p>After the headData is loaded from the file system, this function will build
	 * the associated indexes to allow the heads to be accessed in various ways.
	 * </p>
	 * 
	 */
	public void parseHeadData( boolean purgeUnused ) {
		List<MinecraftHeadData> purge = new ArrayList<>();
		
		for (MinecraftHeadData head : this.headData ) {
			
			if ( purgeUnused && !head.isUsed() ) {
				purge.add( head );
			}
			
			else {
				
				// Build the category index:
				if ( !getHeadDataByCategory().containsKey( head.getCategory() ) ) {
					getHeadDataByCategory().put( head.getCategory(), new ArrayList<>() );
				}
				getHeadDataByCategory().get( head.getCategory() ).add( head );
				
				// build uuid index:
				if ( !getHeadDataByUuid().containsKey( head.getUuid() ) ) {
					getHeadDataByUuid().put( head.getUuid(), head );
				}
				
				// Extract tags:
				if ( head.getTags() != null ) {
					List<String> tagz = Arrays.asList( 
											head.getTags().toLowerCase().split(",") );
					// Extract "tags" in the name field that are within parenthesis:
					getTagFromName( head.getName(), tagz );
					
					for ( String tag : tagz ) {
						
						// Add to tag collection:
						if ( !getTags().contains( tag ) ) {
							getTags().add( tag );
						}
						
						if ( !getHeadDataByTags().containsKey( tag ) ) {
							getHeadDataByTags().put( tag, new ArrayList<>() );
						}
						getHeadDataByTags().get( tag ).add( head );

					}
					
				}
			}
			
		}
		
		
		if ( purgeUnused && purge.size() > 0 ) {
			getHeadData().removeAll( purge );
		}
	}

	private void getTagFromName(String name, List<String> tagz) {
		
		int b = name.indexOf('(');
		int e = name.indexOf(')');
		
		if ( b != -1 && e != -1 && b < e ) {
			String tag = name.substring( b + 1, e ).toLowerCase();
			tagz.add( tag );
		}
	}

	public Date getHeadDataDownloadDate() {
		return headDataDownloadDate;
	}
	public void setHeadDataDownloadDate(Date headDataDownloadDate) {
		this.headDataDownloadDate = headDataDownloadDate;
	}

	public TreeSet<String> getTags() {
		return tags;
	}

	public void setTags(TreeSet<String> tags) {
		this.tags = tags;
	}

	public List<MinecraftHeadData> getHeadData() {
		return headData;
	}

	public void setHeadData(List<MinecraftHeadData> headData) {
		this.headData = headData;
	}

	public TreeMap<MinecraftHeadsCategory, List<MinecraftHeadData>> getHeadDataByCategory() {
		return headDataByCategory;
	}

	public void setHeadDataByCategory(TreeMap<MinecraftHeadsCategory, List<MinecraftHeadData>> headDataByCategory) {
		this.headDataByCategory = headDataByCategory;
	}

	public TreeMap<String, MinecraftHeadData> getHeadDataByUuid() {
		return headDataByUuid;
	}

	public void setHeadDataByUuid(TreeMap<String, MinecraftHeadData> headDataByUuid) {
		this.headDataByUuid = headDataByUuid;
	}

	public TreeMap<String, List<MinecraftHeadData>> getHeadDataByTags() {
		return headDataByTags;
	}

	public void setHeadDataByTags(TreeMap<String, List<MinecraftHeadData>> headDataByTags) {
		this.headDataByTags = headDataByTags;
	}
}
