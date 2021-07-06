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
    public static final String PRISON_PLACEHOLDER_RANKNAME_SUFFIX = "_rankname";
    
    public static final String PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR = ":";
    public static final String PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR = 
								    		PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR + 
								    		PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR;
    
    private PlaceholderProgressBarConfig progressBarConfig;
    
    public enum PlaceholderFlags {
    	
    	PLAYER,
    	LADDERS,
    	RANKS,
    	RANKPLAYERS,

    	MINES,
    	MINEPLAYERS,
    	
    	STATSMINES( true ),
    	
    	
    	SUPRESS,
    	ALIAS
    	;
    	
    	private final boolean sequence;
    	
    	private PlaceholderFlags() {
    		this.sequence = false;
    	}
    	private PlaceholderFlags( boolean hasSequence ) {
    		this.sequence = hasSequence;
    	}
    	
    	/**
    	 * <p>This identifies if a placeholder type contains a sequence as
    	 * part of its placeholder name.  For example a sequence would be 
    	 * identified as '_nnn_' where 'n' represents a positive number and 
    	 * can be 1 digit in length or more.  Three 'n's are used to represent
    	 * this numeric sequence, but does not require it to be three digits
    	 * in length.  The number may also be left-padded with zeros; as long
    	 * as it parses successfully with Integer.parse().
    	 * </p>
    	 * 
    	 * @return
    	 */
    	public boolean hasSequence() {
    		return sequence;
    	}
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
		
		no_match__(PlaceholderFlags.SUPRESS),
		
		// Rank aliases:
		prison_r(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rn(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rt(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rlp(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		
		prison_rc(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rcf(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rcp(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rcb(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		
		prison_rcr(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rcrf(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rcrp(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rcrb(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		
		prison_rr(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_rrt(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),


		
		
		prison_rank(prison_r, PlaceholderFlags.PLAYER),
		prison_rank_number(prison_rn, PlaceholderFlags.PLAYER),
		prison_rank_tag(prison_rt, PlaceholderFlags.PLAYER),
		prison_rank_ladder_position(prison_rlp, PlaceholderFlags.PLAYER),
		
		prison_rankup_cost(prison_rc, PlaceholderFlags.PLAYER),
		prison_rankup_cost_formatted(prison_rcf, PlaceholderFlags.PLAYER),
		prison_rankup_cost_percent(prison_rcp, PlaceholderFlags.PLAYER),
		prison_rankup_cost_bar(prison_rcb, PlaceholderFlags.PLAYER),
		
		prison_rankup_cost_remaining(prison_rcr, PlaceholderFlags.PLAYER),
		prison_rankup_cost_remaining_formatted(prison_rcrf, PlaceholderFlags.PLAYER),
		prison_rankup_cost_remaining_percent(prison_rcrp, PlaceholderFlags.PLAYER),
		prison_rankup_cost_remaining_bar(prison_rcrb, PlaceholderFlags.PLAYER),
		
		prison_rankup_rank(prison_rr, PlaceholderFlags.PLAYER),
		prison_rankup_rank_tag(prison_rrt, PlaceholderFlags.PLAYER),

		
		
		// Ladder aliases:
		prison_r_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rn_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rt_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rlp_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		
		prison_rc_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rcf_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rcp_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rcb_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		
		prison_rcr_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rcrf_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rcrp_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rcrb_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		
		prison_rr_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_rrt_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		
		
		prison_rank_laddername(prison_r_laddername, PlaceholderFlags.LADDERS),
		prison_rank_number_laddername(prison_rn_laddername, PlaceholderFlags.LADDERS),
		prison_rank_tag_laddername(prison_rt_laddername, PlaceholderFlags.LADDERS),
		prison_rank_ladder_position_laddername(prison_rlp_laddername, PlaceholderFlags.LADDERS),
		
		prison_rankup_cost_laddername(prison_rc_laddername, PlaceholderFlags.LADDERS),
		prison_rankup_cost_formatted_laddername(prison_rcf_laddername, PlaceholderFlags.LADDERS),
		prison_rankup_cost_percent_laddername(prison_rcp_laddername, PlaceholderFlags.LADDERS),
		prison_rankup_cost_bar_laddername(prison_rcb_laddername, PlaceholderFlags.LADDERS),
		
		prison_rankup_cost_remaining_laddername(prison_rcr_laddername, PlaceholderFlags.LADDERS),
		prison_rankup_cost_remaining_formatted_laddername(prison_rcrf_laddername, PlaceholderFlags.LADDERS),
		prison_rankup_cost_remaining_percent_laddername(prison_rcrp_laddername, PlaceholderFlags.LADDERS),
		prison_rankup_cost_remaining_bar_laddername(prison_rcrb_laddername, PlaceholderFlags.LADDERS),
		
		prison_rankup_rank_laddername(prison_rr_laddername, PlaceholderFlags.LADDERS),
		prison_rankup_rank_tag_laddername(prison_rrt_laddername, PlaceholderFlags.LADDERS),
	
		
		
		// player balances.  Both with and without ladders.
		prison_pb(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_player_balance(prison_pb, PlaceholderFlags.PLAYER),
		
		prison_pb_laddername(PlaceholderFlags.LADDERS, PlaceholderFlags.ALIAS),
		prison_player_balance_laddername(prison_pb_laddername, PlaceholderFlags.LADDERS),

	
		
		prison_psm(PlaceholderFlags.PLAYER, PlaceholderFlags.ALIAS),
		prison_player_sellall_multiplier(prison_psm, PlaceholderFlags.PLAYER),
		
		
		
		// Mine aliases:
		prison_mn_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mt_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mi_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mif_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mtl_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mtlb_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mtlf_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_ms_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mr_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mrb_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mp_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mpc_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mbm_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mrc_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),

		prison_mb01_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb02_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb03_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb04_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb05_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb06_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb07_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb08_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb09_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),
		prison_mb10_minename(PlaceholderFlags.MINES, PlaceholderFlags.ALIAS),

		
		// reset_interval, reset_timeleft, blocks_size, blocks_remaining, blocks_percent
		// player_count
		// NOTE: Remove PrisonPlaceHolderFlags.SUPRESS when ready to be used:
		prison_mines_name_minename(prison_mn_minename, PlaceholderFlags.MINES),
		prison_mines_tag_minename(prison_mt_minename, PlaceholderFlags.MINES),
		prison_mines_interval_minename(prison_mi_minename, PlaceholderFlags.MINES),
		prison_mines_interval_formatted_minename(prison_mif_minename, PlaceholderFlags.MINES),
		prison_mines_timeleft_minename(prison_mtl_minename, PlaceholderFlags.MINES),
		prison_mines_timeleft_bar_minename(prison_mtlb_minename, PlaceholderFlags.MINES),
		prison_mines_timeleft_formatted_minename(prison_mtlf_minename, PlaceholderFlags.MINES),
		prison_mines_size_minename(prison_ms_minename, PlaceholderFlags.MINES),
		prison_mines_remaining_minename(prison_mr_minename, PlaceholderFlags.MINES),
		prison_mines_remaining_bar_minename(prison_mrb_minename, PlaceholderFlags.MINES),
		prison_mines_percent_minename(prison_mp_minename, PlaceholderFlags.MINES),
		prison_mines_player_count_minename(prison_mpc_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_mined_minename(prison_mbm_minename, PlaceholderFlags.MINES),
		prison_mines_reset_count_minename(prison_mrc_minename, PlaceholderFlags.MINES),

		prison_mines_blocks_01_minename(prison_mb01_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_02_minename(prison_mb02_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_03_minename(prison_mb03_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_04_minename(prison_mb04_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_05_minename(prison_mb05_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_06_minename(prison_mb06_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_07_minename(prison_mb07_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_08_minename(prison_mb08_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_09_minename(prison_mb09_minename, PlaceholderFlags.MINES),
		prison_mines_blocks_10_minename(prison_mb10_minename, PlaceholderFlags.MINES),

		
		
		// PlayerMine aliases:
		prison_mn_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mt_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mi_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mif_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mtl_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mtlb_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mtlf_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_ms_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mr_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mrb_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mp_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mpc_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mbm_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),
		prison_mrc_pm(PlaceholderFlags.MINEPLAYERS, PlaceholderFlags.ALIAS),


		
		prison_mines_name_playermines(prison_mn_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_tag_playermines(prison_mt_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_interval_playermines(prison_mi_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_interval_formatted_playermines(prison_mif_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_timeleft_playermines(prison_mtl_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_timeleft_bar_playermines(prison_mtlb_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_timeleft_formatted_playermines(prison_mtlf_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_size_playermines(prison_ms_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_remaining_playermines(prison_mr_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_remaining_bar_playermines(prison_mrb_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_percent_playermines(prison_mp_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_player_count_playermines(prison_mpc_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_blocks_mined_playermines(prison_mbm_pm, PlaceholderFlags.MINEPLAYERS),
		prison_mines_reset_count_playermines(prison_mrc_pm, PlaceholderFlags.MINEPLAYERS),

		
	
		
		prison_r_n_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_t_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_l_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_lp_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_c_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_cf_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_cu_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_id_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_pc_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		prison_r_lm_rankname(PlaceholderFlags.RANKS, PlaceholderFlags.ALIAS),
		
		

		
		
		prison_rank__name_rankname(prison_r_n_rankname, PlaceholderFlags.RANKS),
		prison_rank__tag_rankname(prison_r_t_rankname, PlaceholderFlags.RANKS),
		prison_rank__ladder_rankname(prison_r_l_rankname, PlaceholderFlags.RANKS),
		prison_rank__ladder_position_rankname(prison_r_lp_rankname, PlaceholderFlags.RANKS),
		prison_rank__cost_rankname(prison_r_c_rankname, PlaceholderFlags.RANKS),
		prison_rank__cost_formatted_rankname(prison_r_cf_rankname, PlaceholderFlags.RANKS),
		prison_rank__currency_rankname(prison_r_cu_rankname, PlaceholderFlags.RANKS),
		prison_rank__id_rankname(prison_r_id_rankname, PlaceholderFlags.RANKS),
		prison_rank__player_count_rankname(prison_r_pc_rankname, PlaceholderFlags.RANKS),
		prison_rank__linked_mines_rankname(prison_r_lm_rankname, PlaceholderFlags.RANKS),
		
		
		
		

		
		//prison_r_plp_rankname(PlaceHolderFlags.RANKPLAYERS, PlaceHolderFlags.ALIAS),
		prison_r_pcst_rankname(PlaceholderFlags.RANKPLAYERS, PlaceholderFlags.ALIAS),
		prison_r_pcf_rankname(PlaceholderFlags.RANKPLAYERS, PlaceholderFlags.ALIAS),
		prison_r_pcr_rankname(PlaceholderFlags.RANKPLAYERS, PlaceholderFlags.ALIAS),
		prison_r_pcrf_rankname(PlaceholderFlags.RANKPLAYERS, PlaceholderFlags.ALIAS),
		prison_r_pcp_rankname(PlaceholderFlags.RANKPLAYERS, PlaceholderFlags.ALIAS),
		prison_r_pcb_rankname(PlaceholderFlags.RANKPLAYERS, PlaceholderFlags.ALIAS),
		
		
		//prison_rank__player_ladder_position_rankname(prison_r_plp_rankname, PlaceHolderFlags.RANKPLAYERS),
		prison_rank__player_cost_rankname(prison_r_pcst_rankname, PlaceholderFlags.RANKPLAYERS),
		prison_rank__player_cost_formatted_rankname(prison_r_pcf_rankname, PlaceholderFlags.RANKPLAYERS),
		prison_rank__player_cost_remaining_rankname(prison_r_pcf_rankname, PlaceholderFlags.RANKPLAYERS),
		prison_rank__player_cost_remaining_formatted_rankname(prison_r_pcf_rankname, PlaceholderFlags.RANKPLAYERS),
		prison_rank__player_cost_percent_rankname(prison_r_pcp_rankname, PlaceholderFlags.RANKPLAYERS),
		prison_rank__player_cost_bar_rankname(prison_r_pcb_rankname, PlaceholderFlags.RANKPLAYERS),
		
		
		
		
		prison_tmbn_nnn_minename(PlaceholderFlags.STATSMINES, PlaceholderFlags.ALIAS),
		prison_tmbc_nnn_minename(PlaceholderFlags.STATSMINES, PlaceholderFlags.ALIAS),
		prison_tmbpl_nnn_minename(PlaceholderFlags.STATSMINES, PlaceholderFlags.ALIAS),
		prison_tmbr_nnn_minename(PlaceholderFlags.STATSMINES, PlaceholderFlags.ALIAS),
		prison_tmbrb_nnn_minename(PlaceholderFlags.STATSMINES, PlaceholderFlags.ALIAS),
		prison_tmbt_nnn_minename(PlaceholderFlags.STATSMINES, PlaceholderFlags.ALIAS),
		
		
		prison_top_mine_block_name_nnn_minename(prison_tmbn_nnn_minename, PlaceholderFlags.STATSMINES),
		prison_top_mine_block_chance_nnn_minename(prison_tmbc_nnn_minename, PlaceholderFlags.STATSMINES),
		prison_top_mine_block_placed_nnn_minename(prison_tmbpl_nnn_minename, PlaceholderFlags.STATSMINES),
		prison_top_mine_block_remaing_nnn_minename(prison_tmbr_nnn_minename, PlaceholderFlags.STATSMINES),
		prison_top_mine_block_remaing_bar_nnn_minename(prison_tmbrb_nnn_minename, PlaceholderFlags.STATSMINES),
		prison_top_mine_block_total_nnn_minename(prison_tmbt_nnn_minename, PlaceholderFlags.STATSMINES),
		
//		prison_top_mine_balance_nnn,
		
		
		;
		
		
		private final PrisonPlaceHolders alias;
		private final List<PlaceholderFlags> flags;
		private PrisonPlaceHolders() {
			this.flags = new ArrayList<>();
			this.alias = null;
		}
		private PrisonPlaceHolders(PlaceholderFlags... flags) {
			this.alias = null;
			this.flags = getFlags(flags);
		}
		private PrisonPlaceHolders(PrisonPlaceHolders alias, PlaceholderFlags... flags) {
			this.alias = alias;
			this.flags = getFlags(flags);
		}
		
		private List<PlaceholderFlags> getFlags( PlaceholderFlags[] flags ) {
			List<PlaceholderFlags> flagz = new ArrayList<>();
			if ( flags != null ) {
				for ( PlaceholderFlags flag : flags ) {
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
			return flags.contains( PlaceholderFlags.ALIAS );
		}
		public boolean isSuppressed() {
			return flags.contains( PlaceholderFlags.SUPRESS );
		}
		public boolean hasFlag( PlaceholderFlags flag ) {
			return flags.contains( flag );
		}
		public List<PlaceholderFlags> getFlags() {
			return flags;
		}
		
		/**
		 * <p>Some placeholders have a "sequence" of numbers as part of the
		 * placeholder name.  This function, hasSequence(), is a quick way to
		 * identify if the placeholder contains a sequence so the special processing
		 * that is required for sequences can be performed.
		 * </p>
		 * 
		 * @return
		 */
		public boolean hasSequence() {
			boolean results = false;
			
			for ( PlaceholderFlags placeholderFlags : flags ) {
				if ( placeholderFlags.hasSequence() ) {
					results = true;
					break;
				}
			}
			
			return results;
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
		
		public static List<PrisonPlaceHolders> getTypes(PlaceholderFlags flag) {
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
							List<PrisonPlaceHolders> list, PlaceholderFlags flag) {
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
					(hasAlias() ? " &b" + getAlias().name()  : "") +
					(isSuppressed() ? " &4*&a ": " ");
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
