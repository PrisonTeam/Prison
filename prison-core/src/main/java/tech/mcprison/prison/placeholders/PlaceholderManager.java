package tech.mcprison.prison.placeholders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;

public class PlaceholderManager {

    public static final String PRISON_PLACEHOLDER_PREFIX = "prison";
    public static final String PRISON_PLACEHOLDER_PREFIX_EXTENDED = PRISON_PLACEHOLDER_PREFIX + "_";
    
    public static final String PRISON_PLACEHOLDER_MINENAME_SUFFIX = "_minename";
    public static final String PRISON_PLACEHOLDER_LADDERNAME_SUFFIX = "_laddername";
    
    public static final String PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR = ":";
    public static final String PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR = 
								    		PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR + 
								    		PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR;
    
    private PlaceholderProgressBarConfig progressBarConfig;
    
    public enum PlaceHolderFlags {
    	
    	PLAYER,
    	LADDERS,
    	MINES,
    	PLAYERMINES,
    	
    	SUPRESS,
    	ALIAS
    	;
    }
    
    public enum PlaceholderAttributePrefixes {
    	nFormat,
    	bar,
    	text;
    	
    	public static PlaceholderAttributePrefixes fromString( String value ) {
    		PlaceholderAttributePrefixes pap = null;
    		
    		if ( value != null ) {
    			for ( PlaceholderAttributePrefixes attrPrefix : values() ) {
    				if ( attrPrefix.name().equalsIgnoreCase( value ) ) {
    					pap = attrPrefix;
    				}
    			}
    		}
    		
    		return pap;
    	}
    }
    
    public enum NumberTransformationUnitTypes {
    	none,
    	kmg,
    	kmbt,
    	binary;
    	
    	public static NumberTransformationUnitTypes fromString( String value ) {
    		NumberTransformationUnitTypes pap = none;
    		
    		if ( value != null ) {
    			for ( NumberTransformationUnitTypes nTrans : values() ) {
    				if ( nTrans.name().equalsIgnoreCase( value ) ) {
    					pap = nTrans;
    				}
    			}
    		}
    		
    		return pap;
    	}
    }
    
    /**
     * <p>The given place holders should have both the prison prefix and without,
     * with the without having the suppress value set.  The suppressable items 
     * will not always be displayed since it would be implied that the prefix
     * would have been provided.
     * </p>
     * 
     * <p>Update: The placeholders without the prison prefix have been eliminated
     * since the prefix is now prepended when it is missing prior to matching to a
     * valid placeholder enum.  This cuts the number of generated placeholders in half. 
     * This is significant since with the addition of the aliases there would be about 
     * 744 placeholders generated if the prison had 30 mines setup!  Now a 30 mine prison
     * would have about 372.
     * </p>
     *
     * <p>Note: In order to use these placeholders with something like holographic display
     * you need to also include the placeholderAPI,
     *  plugin holographic extension and protocolib.
     * </p>
     * 
     * <p>In order to get the holographics extension to work it is critical you read 
     * their spigot page since you have to specify a refresh speed.
     * </p>
     * <code>
     *   /hd addline temp2 Mine Size: {slowest}{prison_mines_blocks_size_temp2}
     *   or
     *   /hd addline temp2 Mine Size: {slowest}{prison_mines_blocks_size_temp2}
     *   /hd addline temp2 Mine Size: {slowest}%prison_mines_blocks_size_temp2%
     * </code>
     * 
     * https://dev.bukkit.org/projects/holographic-displays
     * https://www.spigotmc.org/resources/placeholderapi.6245/
     * https://www.spigotmc.org/resources/protocollib.1997/
     * https://www.spigotmc.org/resources/holographicextension.18461/
     */
	public enum PrisonPlaceHolders {
		
		no_match__(PlaceHolderFlags.SUPRESS),
		
		// Rank aliases:
		prison_r(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rn(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rt(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rc(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rcf(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rcp(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rcb(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rcr(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rcrf(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rr(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_rrt(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),

		
		prison_rank(prison_r, PlaceHolderFlags.PLAYER),
		prison_rank_number(prison_rn, PlaceHolderFlags.PLAYER),
		prison_rank_tag(prison_rt, PlaceHolderFlags.PLAYER),
		prison_rankup_cost(prison_rc, PlaceHolderFlags.PLAYER),
		prison_rankup_cost_formatted(prison_rcf, PlaceHolderFlags.PLAYER),
		prison_rankup_cost_percent(prison_rcp, PlaceHolderFlags.PLAYER),
		prison_rankup_cost_bar(prison_rcb, PlaceHolderFlags.PLAYER),
		prison_rankup_cost_remaining(prison_rcr, PlaceHolderFlags.PLAYER),
		prison_rankup_cost_remaining_formatted(prison_rcrf, PlaceHolderFlags.PLAYER),
		prison_rankup_rank(prison_rr, PlaceHolderFlags.PLAYER),
		prison_rankup_rank_tag(prison_rrt, PlaceHolderFlags.PLAYER),
		
		
		// Ladder aliases:
		prison_r_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rn_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rt_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rc_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rcf_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rcp_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rcb_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rcr_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rcrf_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rr_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_rrt_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		
		
		prison_rank_laddername(prison_r_laddername, PlaceHolderFlags.LADDERS),
		prison_rank_number_laddername(prison_rn_laddername, PlaceHolderFlags.LADDERS),
		prison_rank_tag_laddername(prison_rt_laddername, PlaceHolderFlags.LADDERS),
		prison_rankup_cost_laddername(prison_rc_laddername, PlaceHolderFlags.LADDERS),
		prison_rankup_cost_formatted_laddername(prison_rcf_laddername, PlaceHolderFlags.LADDERS),
		prison_rankup_cost_percent_laddername(prison_rcp_laddername, PlaceHolderFlags.LADDERS),
		prison_rankup_cost_bar_laddername(prison_rcb_laddername, PlaceHolderFlags.LADDERS),
		prison_rankup_cost_remaining_laddername(prison_rcr_laddername, PlaceHolderFlags.LADDERS),
		prison_rankup_cost_remaining_formatted_laddername(prison_rcrf_laddername, PlaceHolderFlags.LADDERS),
		prison_rankup_rank_laddername(prison_rr_laddername, PlaceHolderFlags.LADDERS),
		prison_rankup_rank_tag_laddername(prison_rrt_laddername, PlaceHolderFlags.LADDERS),
	
		
		
		// player balances.  Both with and without ladders.
		prison_pb(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_player_balance(prison_pb, PlaceHolderFlags.PLAYER),
		
		prison_pb_laddername(PlaceHolderFlags.LADDERS, PlaceHolderFlags.ALIAS),
		prison_player_balance_laddername(prison_pb_laddername, PlaceHolderFlags.LADDERS),

	
		
		prison_psm(PlaceHolderFlags.PLAYER, PlaceHolderFlags.ALIAS),
		prison_player_sellall_multiplier(prison_psm, PlaceHolderFlags.PLAYER),
		
		
		
		// Mine aliases:
		prison_mn_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mt_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mi_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mif_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mtl_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mtlb_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mtlf_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_ms_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mr_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mrb_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mp_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mpc_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mbm_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),
		prison_mrc_minename(PlaceHolderFlags.MINES, PlaceHolderFlags.ALIAS),

		
		// reset_interval, reset_timeleft, blocks_size, blocks_remaining, blocks_percent
		// player_count
		// NOTE: Remove PrisonPlaceHolderFlags.SUPRESS when ready to be used:
		prison_mines_name_minename(prison_mn_minename, PlaceHolderFlags.MINES),
		prison_mines_tag_minename(prison_mt_minename, PlaceHolderFlags.MINES),
		prison_mines_interval_minename(prison_mi_minename, PlaceHolderFlags.MINES),
		prison_mines_interval_formatted_minename(prison_mif_minename, PlaceHolderFlags.MINES),
		prison_mines_timeleft_minename(prison_mtl_minename, PlaceHolderFlags.MINES),
		prison_mines_timeleft_bar_minename(prison_mtlb_minename, PlaceHolderFlags.MINES),
		prison_mines_timeleft_formatted_minename(prison_mtlf_minename, PlaceHolderFlags.MINES),
		prison_mines_size_minename(prison_ms_minename, PlaceHolderFlags.MINES),
		prison_mines_remaining_minename(prison_mr_minename, PlaceHolderFlags.MINES),
		prison_mines_remaining_bar_minename(prison_mrb_minename, PlaceHolderFlags.MINES),
		prison_mines_percent_minename(prison_mp_minename, PlaceHolderFlags.MINES),
		prison_mines_player_count_minename(prison_mpc_minename, PlaceHolderFlags.MINES),
		prison_mines_blocks_mined_minename(prison_mbm_minename, PlaceHolderFlags.MINES),
		prison_mines_reset_count_minename(prison_mrc_minename, PlaceHolderFlags.MINES),

		
		
		// PlayerMine aliases:
		prison_mn_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mt_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mi_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mif_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mtl_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mtlb_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mtlf_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_ms_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mr_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mrb_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mp_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mpc_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mbm_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),
		prison_mrc_pm(PlaceHolderFlags.PLAYERMINES, PlaceHolderFlags.ALIAS),

		
		prison_mines_name_playermines(prison_mn_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_tag_playermines(prison_mt_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_interval_playermines(prison_mi_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_interval_formatted_playermines(prison_mif_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_timeleft_playermines(prison_mtl_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_timeleft_bar_playermines(prison_mtlb_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_timeleft_formatted_playermines(prison_mtlf_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_size_playermines(prison_ms_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_remaining_playermines(prison_mr_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_remaining_bar_playermines(prison_mrb_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_percent_playermines(prison_mp_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_player_count_playermines(prison_mpc_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_blocks_mined_playermines(prison_mbm_pm, PlaceHolderFlags.PLAYERMINES),
		prison_mines_reset_count_playermines(prison_mrc_pm, PlaceHolderFlags.PLAYERMINES),

		
	
		;
		
		
		private final PrisonPlaceHolders alias;
		private final List<PlaceHolderFlags> flags;
		private PrisonPlaceHolders() {
			this.flags = new ArrayList<>();
			this.alias = null;
		}
		private PrisonPlaceHolders(PlaceHolderFlags... flags) {
			this.alias = null;
			this.flags = getFlags(flags);
		}
		private PrisonPlaceHolders(PrisonPlaceHolders alias, PlaceHolderFlags... flags) {
			this.alias = alias;
			this.flags = getFlags(flags);
		}
		
		private List<PlaceHolderFlags> getFlags( PlaceHolderFlags[] flags ) {
			List<PlaceHolderFlags> flagz = new ArrayList<>();
			if ( flags != null ) {
				for ( PlaceHolderFlags flag : flags ) {
					flagz.add( flag );
				}
			}
			return flagz;
		}
		
		public PrisonPlaceHolders getAlias()
		{
			return alias;
		}
		
		public boolean hasAlias() {
			return alias != null;
		}
		public boolean isAlias() {
			return flags.contains( PlaceHolderFlags.ALIAS );
		}
		public boolean isSuppressed() {
			return flags.contains( PlaceHolderFlags.SUPRESS );
		}
		public boolean hasFlag( PlaceHolderFlags flag ) {
			return flags.contains( flag );
		}
		public List<PlaceHolderFlags> getFlags() {
			return flags;
		}

		public static PrisonPlaceHolders fromString( String placeHolder ) {
			PrisonPlaceHolders result = no_match__;
			
			if ( placeHolder != null && placeHolder.trim().length() > 0 ) {
				placeHolder = placeHolder.trim();
				
				// This allows us to get rid of suppressed placeholders that were used for 
				// internal matching when placeholder APIs strip off the prefix:
				if ( !placeHolder.toLowerCase().startsWith( PRISON_PLACEHOLDER_PREFIX ) ) {
					placeHolder = PRISON_PLACEHOLDER_PREFIX + "_" + placeHolder;
				}
				
				for ( PrisonPlaceHolders ph : values() ) {
					if ( ph.name().equalsIgnoreCase( placeHolder ) ) {
						result = ph;
						break;
					}
				}
			}
			
			return result;
		}
		
		public static List<PrisonPlaceHolders> getTypes(PlaceHolderFlags flag) {
			List<PrisonPlaceHolders> results = new ArrayList<>();
			
			if ( flag != null ) {
				for ( PrisonPlaceHolders ph : values() ) {
					if ( ph.getFlags().contains( flag )) {
						results.add( ph );
					}
				}
			}
			
			return results;
		}
		
		public static List<PrisonPlaceHolders> excludeTypes(
							List<PrisonPlaceHolders> list, PlaceHolderFlags flag) {
			List<PrisonPlaceHolders> results = new ArrayList<>();
			
			if ( flag != null ) {
				for ( PrisonPlaceHolders ph : list ) {
					if ( !ph.getFlags().contains( flag )) {
						results.add( ph );
					}
				}
			}
			
			return results;
		}
		
		public String getChatText() {
			return "&a" + name() + 
					(hasAlias() ? "&7(&b" + getAlias().name() + "&7)" : "") +
					(isSuppressed() ? "&4*&a ": " ");
		}
		
		public static String getAllChatTexts() {
			return getAllChatTexts(false);
		}
		
		public static String getAllChatTextsOmitSuppressable() {
			return getAllChatTexts(true);
		}
		
		public static List<String> getAllChatList( boolean omitSuppressable) {
			List<String> results = new ArrayList<>();
			
			boolean hasDeprecated = false;
			
			for ( PrisonPlaceHolders ph : values() )
			{
				if ( !omitSuppressable || omitSuppressable && !ph.isSuppressed() && !ph.isAlias() ) {
					if ( !hasDeprecated && ph.isSuppressed() ) {
						hasDeprecated = true;
					}
					
					results.add( ph.getChatText() );
				}
			}
			
			if ( hasDeprecated ) {
				results.add( " &2(&4*&2=&4suppressed&2)" );
			}
			
			return results;
		}

		private static String getAllChatTexts( boolean omitSuppressable) {
			StringBuilder sb = new StringBuilder();
			
			List<String> placeholders = getAllChatList(omitSuppressable);
			
			for ( String placeholder : placeholders ) {
				sb.append( placeholder );
			}
			
			return sb.toString();
		}
		
	}
	
	
	/**
	 * <p>This will extract attributes from dynamic placeholders and will return. 
	 * </p>
	 * 
	 * <p>Planning on using : as separators.  :: for identifying each attribute, and then
	 * within each attribute : will separate the individual fields and values.  
	 * For example it a number format attribute could look like this: 
	 * </p>
	 * 
	 * <pre>::nFormat:0.00{unit}</pre> for no spaces.  
	 * <pre>::nFormat:#,##0.0+{unit}</pre> for spaces since + will be converted to spaces.
	 * 
	 * @param placeholder
	 * @return
	 */
	public PlaceholderAttribute extractPlaceholderExtractAttribute( String placeholder ) {
		PlaceholderAttribute attribute = null;
		
		if ( placeholder != null ) {
			String[] attributes = placeholder.split( PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR );
			
			// attributes[0] will be the placeholder, so ignore:
			if ( attributes != null && attributes.length > 1 ) {
				for ( int i = 1; i < attributes.length ; i++ ) {
					String rawAttribute = attributes[i];
					
					if ( rawAttribute != null ) {
						attribute = attributeFactory( rawAttribute );
						break;
					}
				}
			}
		}
		
		return attribute;
	}
	

	private PlaceholderAttribute attributeFactory( String rawAttribute ) {
		PlaceholderAttribute attribute = null;
		
		if ( rawAttribute != null && !rawAttribute.isEmpty() ) {
			ArrayList<String> parts = new ArrayList<>();
			parts.addAll( Arrays.asList( rawAttribute.split( PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR )) );
			
			if ( parts.size() > 1 ) {
				PlaceholderAttributePrefixes pap = PlaceholderAttributePrefixes.fromString( parts.get( 0 ) );
				
				switch ( pap )
				{
					case nFormat:
						attribute = new PlaceholderAttributeNumberFormat( parts, rawAttribute );
						break;

					case bar:
						attribute = new PlaceholderAttributeBar( parts, getProgressBarConfig(), rawAttribute );
						break;
						
					case text:
						attribute = new PlaceholderAttributeText( parts, rawAttribute );
						break;
						
					default:
						break;
				}
				
			}
			
		}
		
		return attribute;
	}


	public String extractPlaceholderString( String identifier ) {
		String results = null;
		
		int idx = identifier == null ? -1 :
					identifier.indexOf( PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR );
					
		if ( idx >= 0 ) {
			results = identifier.substring( 0, idx );
		}
		else {
			results = identifier;
		}
		
		return results;
	}
	
	public void reloadPlaceholderBarConfig() {
		setProgressBarConfig( loadPlaceholderBarConfig() );
	}
	
	public PlaceholderProgressBarConfig loadPlaceholderBarConfig() {
		PlaceholderProgressBarConfig config = null;
		
		String barSegmentsStr = Prison.get().getPlatform().getConfigString( 
							"placeholder.bar-segments" );
		String barPositiveColor = Prison.get().getPlatform().getConfigString( 
							"placeholder.bar-positive-color" );
		String barPositiveSegment = Prison.get().getPlatform().getConfigString( 
							"placeholder.bar-positive-segment" );
		String barNegativeColor = Prison.get().getPlatform().getConfigString( 
							"placeholder.bar-negative-color" );
		String barNegativeSegment = Prison.get().getPlatform().getConfigString( 
							"placeholder.bar-negative-segment" );
				
		
		// All 5 must not be null:
		if ( barSegmentsStr != null && barPositiveColor != null && barPositiveSegment != null &&
				barNegativeColor != null && barNegativeSegment != null ) {
			
			int barSegments = 20;
			
			try {
				barSegments = Integer.parseInt( barSegmentsStr );
			}
			catch ( NumberFormatException e ) {
				Output.get().logWarn( 
						"IntegrationManager.loadPlaceholderBarConfigs(): Failure to convert the" +
						"/plugins/Prison/config.yml  prison-placeholder-configs.progress-bar.bar-segments " +
						"to a valid integer. Defaulting to a value of 20 " +
						"[" + barSegmentsStr + "] " + e.getMessage() );
			
			}
			
			config = new PlaceholderProgressBarConfig( barSegments, 
							barPositiveColor, barPositiveSegment,
							barNegativeColor, barNegativeSegment );
		}
		
		if ( config == null ) {
			// go with default values because the config.yml is not up to date with
			// the default values
			
			config = new PlaceholderProgressBarConfig( 
					20, "&2", "#", "&4", "=" 
//					20, "&2", "▊", "&4", "▒" 
					);
			
			Output.get().logInfo( "The /plugins/Prison/config.yml does not contain the " +
					"default values for the Placeholder Progress Bar." );
			Output.get().logInfo( "Default values are " +
					"being used. To customize the bar, rename the config.yml and it will be " +
					"regenerated and then edit to restore prior values.");
			
		}

		return config;
	}
	
    public PlaceholderProgressBarConfig getProgressBarConfig() {
    	if ( progressBarConfig == null ) {
    		progressBarConfig = loadPlaceholderBarConfig();
    	}
		return progressBarConfig;
	}
	public void setProgressBarConfig( PlaceholderProgressBarConfig progressBarConfig ) {
		this.progressBarConfig = progressBarConfig;
	}

	/**
	 * <p>This function uses the settings within the config.yml to construct a progress
	 * bar.  It takes two numeric values and constructs it upon those parameters.
	 * The parameter <pre>value</pre> is the value that changes, and is the value that 
	 * sets where the bar changes.  The parameter <pre>valueTotal</pre> is the max value
	 * of where the <pre>value</pre> is increasing to.
	 * </p>
	 * 
	 * <p>The lowest range is always zero and <pre>value</pre> will be set to zero if 
	 * it is negative.   If <pre>value</pre> is greater than <pre>valueTotal</pre>
	 * then it will be set to that value.  The valid range for this function is only 0 percent 
	 * to 100 percent.
	 * </p>
	 * 
	 * <p>If the progress bar is moving in the wrong direction, then set the parameter
	 * <pre>reverse</pre> to true and then the <pre>value</pre> will be inverted by subtracting
	 * its value from <pre>valueTotal</pre>.
	 * </p>
	 * 
	 * @param value A value that is changing. Will be set to zero if negative. Will be 
	 * 				set to valueTotal if greater than that amount. 
	 * @param valueTotal The target value that is non-changing.
	 * @param reverse Changes the growth direction of the progress bar.
	 * @param attribute 
	 * @return
	 */
	public String getProgressBar( double value, double valueTotal, boolean reverse, 
															PlaceholderAttribute attribute ) {
		StringBuilder sb = new StringBuilder();
		
		// value cannot be greater than valueTotal:
		if ( value > valueTotal ) {
			value = valueTotal;
		}
		else if ( value < 0 ) {
			value = 0;
		}
		
		// If reverse, then the new value is subtracted from valueTotal:
		if ( reverse ) {
			value = valueTotal - value;
		}
		
    	double percent = value / valueTotal * 100.0;
    	
    	PlaceholderAttributeBar barAttribute = attribute == null || 
    							!(attribute instanceof PlaceholderAttributeBar) ? null : 
    								(PlaceholderAttributeBar) attribute;
    	
//    	Output.get().logInfo( "### @@@ ### getProgressBar: barAttribute: " + 
//    				( barAttribute != null ? "true" : "false"));
    	
    	PlaceholderProgressBarConfig barConfig = 
    								barAttribute != null ? barAttribute.getBarConfig() :
    									getProgressBarConfig();

		String lastColorCode = null;
		for ( int i = 0; i < barConfig.getSegments(); i++ ) {
			double pct = i / ((double)barConfig.getSegments()) * 100.0;
			
			if ( pct < percent ) {
				if ( lastColorCode == null || 
						!barConfig.getPositiveColor().equalsIgnoreCase( lastColorCode )) { 
					sb.append( barConfig.getPositiveColor() );
					lastColorCode = barConfig.getPositiveColor();
				}
				sb.append( barConfig.getPositiveSegment() );
			}
			else {
				if ( lastColorCode == null || 
						!barConfig.getNegativeColor().equalsIgnoreCase( lastColorCode )) { 
					sb.append( barConfig.getNegativeColor() );
					lastColorCode = barConfig.getNegativeColor();
				}
				sb.append( barConfig.getNegativeSegment() );
				
			}
		}

    	
    	return sb.toString();
	}


	
	
}
