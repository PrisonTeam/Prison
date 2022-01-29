### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - Setting Prison with Auto Configure

This document provides information on how to get started quickly using Prison's `/ranks autoConfigure`.


[Prison Log File Examples - Starting Prison & auto configure](prison_docs_101_auto_configure_log_examples.md)


*Documented updated: 2022-01-29*

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">

# Overview


These are the values that are supplied to the PlaceholderAPI wiki.  They are stored in this document for backup copies, and for easy references.



<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">

The following are Prison's entries in the file **Plugins-using-PlaceholderAPI.md**:


- **[Prison](https://www.spigotmc.org/resources/1223/)**
  - [x] Supports placeholders.
  - [x] Provides own placeholders. [**[[Link|Placeholders#prison]]**]


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


The following are Prison's entries in the file **Placeholders.md**:


## **P**

- **[Prison](#prison)**

----

- ### **[Prison](https://www.spigotmc.org/resources/1223/)**
> NO DOWNLOAD COMMAND

Each placeholder has a shorter alias, which follows the primary placeholder below.

Prison uses PlaceholderAPI to support any plugin placeholders within the GUI Ranks Lore and the GUI Mine Lore.


**Prison Placeholder Attributes:**

Prison supports Placeholder Attributes which allows an infinite way to customize most placeholders, such as numeric formatting, hex colors, and reductions. Can customize any bar-graph for character codes, colors, and size.

Simple examples using placeholder attributes with the results following each example. Colors are not shown.

```
%prison_mines_size_mine5::nFormat:#,##0%
654,321
%prison_mines_size_mine5::nFormat:#,##0.00:1:kmg%
654.32 k
%prison_mines_size_mine5::nFormat:'#af33ff'#,##0.00:1:binary:hex%
638.99 KB
%prison_mines_timeleft_bar_mine5::bar:40:&2:O:&d:x:debug%
OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOxxxxxxxx

```


**PLAYER placeholders (110, 55 aliases):**

PLAYER placeholders are used directly with a player, such as with player chat prefixes and scoreboards.

Rank related placeholders apply to all ranks that a player may have, and may return more than one value.  Use the LADDER placeholders to control the order that is displayed. Rank based placeholder can return zero, one, or more rank related values depending upon how many ladders the player is on.

```
%prison_rank%
%prison_r%
%prison_rank_number%
%prison_rn%
%prison_rank_tag%
%prison_rt%
%prison_rank_ladder_position%
%prison_rlp%
%prison_rankup_cost%
%prison_rc%
%prison_rankup_cost_formatted%
%prison_rcf%
%prison_rankup_cost_percent%
%prison_rcp%
%prison_rankup_cost_bar%
%prison_rcb%
%prison_rankup_cost_remaining%
%prison_rcr%
%prison_rankup_cost_remaining_formatted%
%prison_rcrf%
%prison_rankup_cost_remaining_percent%
%prison_rcrp
%prison_rankup_cost_remaining_bar%
%prison_rcrb%
%prison_rankup_rank%
%prison_rr%
%prison_rankup_rank_tag%
%prison_rrt%
%prison_player_balance%
%prison_pb%
%prison_player_balance_formatted%
%prison_pbf%
%prison_player_balance_earnings_per_minute%
%prison_pb_epm%
%prison_player_balance_earnings_per_minute_formatted%
%prison_pb_epmf%
%prison_player_token_balance%
%prison_ptb%
%prison_player_token_balance_formatted%
%prison_ptbf%
%prison_player_token_balance_formatted_metric%
%prison_ptbfm%
%prison_player_token_balance_formatted_kmbt%
%prison_ptbfk%
%prison_player_token_balance_earnings_per_minute%
%prison_ptb_epm%
%prison_player_token_balance_earnings_per_minute_formatted%
%prison_ptb_epmf%
%prison_player_sellall_multiplier%
%prison_psm%
%prison_player_blocks_total%
%prison_pbt%
%prison_player_blocks_total_formatted%
%prison_pbtf%
%prison_player_tool_id%
%prison_ptid%
%prison_player_tool_name%
%prison_ptn%
%prison_player_tool_type%
%prison_ptt%
%prison_player_tool_material_type%
%prison_ptmt%
%prison_player_tool_data%
%prison_ptdata%
%prison_player_tool_lore%
%prison_ptlore%
%prison_player_tool_durability_used%
%prison_ptdu%
%prison_player_tool_durability_max%
%prison_ptdm%
%prison_player_tool_durability_remaining%
%prison_ptdr%
%prison_player_tool_durability_percent%
%prison_ptdp%
%prison_player_tool_durability_bar%
%prison_ptdb%
%prison_player_tool_enchantment_fortune%
%prison_ptef%
%prison_player_tool_enchantment_efficency%
%prison_ptee%
%prison_player_tool_enchantment_silktouch%
%prison_ptes%
%prison_player_tool_enchantment_unbreaking%
%prison_pteu%
%prison_player_tool_enchantment_mending%
%prison_ptem%
%prison_player_tool_enchantment_luck%
%prison_ptel%
%prison_player_health%
%prison_ph%
%prison_player_health_max%
%prison_phm%
%prison_player_air_max%
%prison_pam%
%prison_player_air_remaining%
%prison_par%
%prison_player_food_level%
%prison_pfl%
%prison_player_food_saturation%
%prison_pfs%
%prison_player_food_exhaustion%
%prison_pfe%
%prison_player_xp%
%prison_pxp%
%prison_player_xp_to_level%
%prison_pxptl%
%prison_player_level%
%prison_pl%
%prison_player_walk_speed%
%prison_pws%
```


**LADDERS placeholders (32, 16 aliases):**

Must be used directly with a player, and returns the information related to their active rank on the specified ladder.

Use the ladder name, all lowercase, in place of `<laddername>`, and it will return zero or one rank related values.

```
%prison_rank_<laddername>%
%prison_r_<laddername>%
%prison_rank_number_<laddername>%
%prison_rn_<laddername>%
%prison_rank_tag_<laddername>%
%prison_rt_<laddername>%
prison_rank_ladder_position_<laddername>%
%prison_rlp_<laddername>%
%prison_rankup_cost_<laddername>%
%prison_rc_<laddername>%
%prison_rankup_cost_formatted_<laddername>%
%prison_rcf_<laddername>%
%prison_rankup_cost_percent_<laddername>%
%prison_rcp_<laddername>%
%prison_rankup_cost_bar_<laddername>%
%prison_rcb_<laddername>%
%prison_rankup_cost_remaining_<laddername>%
%prison_rcr_<laddername>%
%prison_rankup_cost_remaining_formatted_<laddername>%
%prison_rcrf_<laddername>%
%prison_rankup_cost_remaining_percent_<laddername>%
%prison_rcrp_<laddername>%
%prison_rankup_cost_remaining_bar_<laddername>%
%prison_rcrb_<laddername>%
%prison_rankup_rank_<laddername>%
%prison_rr_<laddername>%
%prison_rankup_rank_tag_<laddername>%
%prison_rrt_<laddername>%
%prison_player_balance_<laddername>%
%prison_pb_<laddername>%
prison_player_balance_formatted_<laddername>%
%prison_pbf_<laddername>%
```


**RANKS placeholders (22, 11 aliases):**

RANKS placeholders deal directly with specific rank information.

Use the rank name in place of `<rankname>`, and may return zero or one value.

```
%prison_rank__name_<rankname>%
%prison_r_n_<rankname>%
%prison_rank__tag_<rankname>%
%prison_r_t_<rankname>%
%prison_rank__ladder_<rankname>%
%prison_r_l_<rankname>%
%prison_rank__ladder_position_<rankname>%
%prison_r_lp_<rankname>%
%prison_rank__cost_<rankname>%
%prison_r_c_<rankname>%
%prison_rank__cost_formatted_<rankname>%
%prison_r_cf_<rankname>%
%prison_rank__cost_multiplier_<rankname>%
%prison_r_cm_<rankname>%
%prison_rank__currency_<rankname>%
%prison_r_cu_<rankname>%
%prison_rank__id_<rankname>%
%prison_r_id_<rankname>%
%prison_rank__player_count_<rankname>%
%prison_r_pc_<rankname>%
%prison_rank__linked_mines_<rankname>%
%prison_r_lm_<rankname>%
```


**RANKPLAYERS placeholders (12, 6 aliases):**

RANKPLAYERS placeholders are used with players and shows what their adjusted costs are for the specified rank. These placeholders are ideal for showing a player how much each rank will cost using their personal rank cost multipliers.

Use the rank name in place of `<rankname>`, and may return zero or one value.

```
%prison_rank__player_cost_<rankname>%
%prison_r_pcst_<rankname>%
%prison_rank__player_cost_formatted_<rankname>%
%prison_r_pcf_<rankname>%
%prison_rank__player_cost_remaining_<rankname>%
%prison_r_pcf_<rankname>%
%prison_rank__player_cost_remaining_formatted_<rankname>%
%prison_r_pcf_<rankname>%
%prison_rank__player_cost_percent_<rankname>%
%prison_r_pcp_<rankname>%
%prison_rank__player_cost_bar_<rankname>%
%prison_r_pcb_<rankname>%
```

**MINES placeholders (34, 17 aliases):**

MINES placeholder provides stats for the given mine.

Use the mine name in place of `<minename>`, and may return zero or one value.

```
%prison_mines_name_<minename>%
%prison_mn_<minename>%
%prison_mines_tag_<minename>%
%prison_mt_<minename>%
%prison_mines_interval_<minename>%
%prison_mi_<minename>%
%prison_mines_interval_formatted_<minename>%
%prison_mif_<minename>%
%prison_mines_timeleft_<minename>%
%prison_mtl_<minename>%
%prison_mines_timeleft_bar_<minename>%
%prison_mtlb_<minename>%
%prison_mines_timeleft_formatted_<minename>%
%prison_mtlf_<minename>%
%prison_mines_size_<minename>%
%prison_ms_<minename>%
%prison_mines_remaining_<minename>%
%prison_mr_<minename>%
%prison_mines_remaining_bar_<minename>%
%prison_mrb_<minename>%
%prison_mines_percent_<minename>%
%prison_mp_<minename>%
%prison_mines_player_count_<minename>%
%prison_mpc_<minename>%
%prison_mines_blocks_mined_<minename>%
%prison_mbm_<minename>%
%prison_mines_reset_count_<minename>%
%prison_mrc_<minename>%
%prison_player_blocks_total_<minename>%
%prison_pbtm

%prison_top_mine_block_line_header_<minename>%
%prison_tmbl_header_<minename>%
%prison_top_mine_block_line_totals_<minename>%
%prison_tmbl_totals_<minename>%
```

Note: The placeholders `%prison_top_mine_block_line_header_<minename>%` and `%prison_top_mine_block_line_totals_<minename>%` are used with the STATSMINES placeholders and provide the headers and total details for the given mines.


**MINEPLAYERS placeholders (28, 14 aliases):**

MINEPLAYERS placeholders are tied to a player and dynamically shows the details of the mine the player is in, or blanks when not in a mine. These are ideal for use in scoreboards or chat prefixes.

Must be used with a player.

```
%prison_mines_name_playermines%
%prison_mn_pm%
%prison_mines_tag_playermines%
%prison_mt_pm%
%prison_mines_interval_playermines%
%prison_mi_pm%
%prison_mines_interval_formatted_playermines%
%prison_mif_pm%
%prison_mines_timeleft_playermines%
%prison_mtl_pm%
%prison_mines_timeleft_bar_playermines%
%prison_mtlb_pm%
%prison_mines_timeleft_formatted_playermines%
%prison_mtlf_pm%
%prison_mines_size_playermines%
%prison_ms_pm%
%prison_mines_remaining_playermines%
%prison_mr_pm%
%prison_mines_remaining_bar_playermines%
%prison_mrb_pm%
%prison_mines_percent_playermines%
%prison_mp_pm%
%prison_mines_player_count_playermines%
%prison_mpc_pm%
%prison_mines_blocks_mined_playermines%
%prison_mbm_pm%
%prison_mines_reset_count_playermines%
%prison_mrc_pm%
```


**STATSMINES placeholders (14, 7 aliases):**

The STATSMINES placeholders represents the blocks that in the specified mine. The value `nnn` should be replaced with a number starting with a `1`, or `001` and refers to one block. The "_line_ placeholder is composed of the other placeholders and can simplify the use of these placeholders. See the headers and footer totals within the MINES placeholders.

Use the mine name in place of `<minename>`, and may return zero or one value.  Invalid values for `_nnn_` will return blanks.

```
%prison_top_mine_block_line_nnn_<minename>%
%prison_tmbl_nnn_<minename>%
%prison_top_mine_block_name_nnn_<minename>%
%prison_tmbn_nnn_<minename>%
%prison_top_mine_block_chance_nnn_<minename>%
%prison_tmbc_nnn_<minename>%
%prison_top_mine_block_placed_nnn_<minename>%
%prison_tmbpl_nnn_<minename>%
%prison_top_mine_block_remaing_nnn_<minename>%
%prison_tmbr_nnn_<minename>%
%prison_top_mine_block_remaing_bar_nnn_<minename>%
%prison_tmbrb_nnn_<minename>%
%prison_top_mine_block_total_nnn_<minename>%
%prison_tmbt_nnn_<minename>%
```


**STATSRANKS placeholders (6, 3 aliases)**

The STATSRANKS placeholders represents the top-n players for a given rank. The value `nnn` should be replaced with a number starting with a `1`, or `001` and refers to a given player.

Use the mine name in place of `<rankname>`, and may return zero or one value.  Invalid values for `_nnn_` will return blanks.

```
%prison_top_rank_balance_name_nnn_<rankname>%
%prison_trbn_nnn_<rankname>%
%prison_top_rank_balance_score_nnn_<rankname>%
%prison_trbs_nnn_<rankname>%
%prison_top_rank_balance_balance_nnn_<rankname>%
%prison_trbb_nnn_<rankname>%
```

----


