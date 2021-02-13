/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.util;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>All of the blocks in the game.
 * </p>
 * 
 * <p>The new field altNames contains a list of String values that will help
 * XMaterial map these types to valid Material types.  These are spigot version
 * Dependent, so some blocks may have different altNames for different spigot 
 * versions.
 * </p>
 * 
 * <p>XMaterial support for spigot 1.8.8:
 * 		MOSS_STONE = mossy_cobblestone,  LAPIS_LAZULI_ORE = "lapis_ore",  
 * 		LAPIS_LAZULI_BLOCK = "lapis_block",  PILLAR_QUARTZ_BLOCK = "quartz_pillar"
 *  </p>
 *
 * @author Faizaan A. Datoo
 * @author Camouflage100
 * @since API 1.0
 */
public enum BlockType {

	/**
	 * Identifying a block as a MaterialType.BLOCK will allow the 
	 * block search to only show entries that will be placable within a mine.
	 * 
	 * Cannot dynamically check for blocks at run time since each major version
	 * has slightly different sets and the names of this enum do not match the names
	 * of the Material.
	 */
	
	IGNORE( -1, "prison:ignore", -1, MaterialType.BLOCK ),
	NULL_BLOCK( -2, "prison:null_block", -1, MaterialType.INVALID ),
	
	
    // This was auto-generated from WorldEdit's blocks.json
    // @formatter:off
	
	
	// NOTE: Double slabs are ones that players cannot naturally place, and they are
	//       similar to the main block type.  It appears like they have been replaced,
	//       but not sure with what.  I suspect no one will use them anyway, and if they
	//       do, then mapping them to their counter part block.  There are a few doubles
	//       that have been mapped to "smooth" in the later versions, so they are used when 
	//       possible.
	
	
	DOUBLE_STONE_SLAB( 43, "minecraft:double_stone_slab", 0, MaterialType.BLOCK, "smooth_stone" ),
	DOUBLE_SANDSTONE_SLAB( 43, "minecraft:double_stone_slab", 1, MaterialType.BLOCK, "smooth_sandstone_slab" ),
	DOUBLE_WOODEN_SLAB( 43, "minecraft:double_stone_slab", 2, MaterialType.BLOCK, "OAK_PLANKS" ),
	DOUBLE_COBBLESTONE_SLAB( 43, "minecraft:double_stone_slab", 3, MaterialType.BLOCK, "COBBLESTONE" ),
	DOUBLE_BRICK_SLAB( 43, "minecraft:double_stone_slab", 4, MaterialType.BLOCK, "BRICKS" ),
	DOUBLE_STONE_BRICK_SLAB( 43, "minecraft:double_stone_slab", 5, MaterialType.BLOCK, "STONE_BRICKS" ),
	DOUBLE_NETHER_BRICK_SLAB( 43, "minecraft:double_stone_slab", 6, MaterialType.BLOCK, "NETHER_BRICKS" ),
	DOUBLE_QUARTZ_SLAB( 43, "minecraft:double_stone_slab", 7, MaterialType.BLOCK, "SMOOTH_QUARTZ" ),

	DOUBLE_OAK_WOOD_SLAB( 125, "minecraft:double_wooden_slab", 0, MaterialType.BLOCK, "OAK_PLANKS" ),
	DOUBLE_SPRUCE_WOOD_SLAB( 125, "minecraft:double_wooden_slab", 1, MaterialType.BLOCK, "SPRUCE_PLANKS" ),
	DOUBLE_BIRCH_WOOD_SLAB( 125, "minecraft:double_wooden_slab", 2, MaterialType.BLOCK, "BIRCH_PLANKS" ),
	DOUBLE_JUNGLE_WOOD_SLAB( 125, "minecraft:double_wooden_slab", 3, MaterialType.BLOCK, "JUNGLE_PLANKS" ),
	DOUBLE_ACACIA_WOOD_SLAB( 125, "minecraft:double_wooden_slab", 4, MaterialType.BLOCK, "ACACIA_PLANKS" ),
	DOUBLE_DARK_OAK_WOOD_SLAB( 125, "minecraft:double_wooden_slab", 5, MaterialType.BLOCK, "DARK_OAK_PLANKS" ),

	DOUBLE_RED_SANDSTONE_SLAB( 181, "minecraft:double_stone_slab2", 0, MaterialType.BLOCK, "RED_SANDSTONE" ),
	PURPUR_DOUBLE_SLAB( 204, "minecraft:purpur_double_slab", 0, MaterialType.BLOCK, "PURPUR_BLOCK" ),

	
	
	AIR( 0, "minecraft:air", 0, MaterialType.BLOCK ),
	STONE( 1, "minecraft:stone", 0, MaterialType.BLOCK ),
	GRANITE( 1, "minecraft:stone", 1, MaterialType.BLOCK ),
	POLISHED_GRANITE( 1, "minecraft:stone", 2, MaterialType.BLOCK ),
	DIORITE( 1, "minecraft:stone", 3, MaterialType.BLOCK ),
	POLISHED_DIORITE( 1, "minecraft:stone", 4, MaterialType.BLOCK ),
	ANDESITE( 1, "minecraft:stone", 5, MaterialType.BLOCK ),
	POLISHED_ANDESITE( 1, "minecraft:stone", 6, MaterialType.BLOCK ),
	
	GRASS( 2, "minecraft:grass", 0, MaterialType.BLOCK ),
	GRASS_BLOCK( 2, "minecraft:grass_block", 0, MaterialType.BLOCK ),
	
	DIRT( 3, "minecraft:dirt", 0, MaterialType.BLOCK ),
	COARSE_DIRT( 3, "minecraft:dirt", 1, MaterialType.BLOCK ),
	PODZOL( 3, "minecraft:dirt", 2, MaterialType.BLOCK ),
	COBBLESTONE( 4, "minecraft:cobblestone", 0, MaterialType.BLOCK ),
	
	OAK_WOOD_PLANK( 5, "minecraft:planks", 0, MaterialType.BLOCK, "OAK_PLANKS" ),
	SPRUCE_WOOD_PLANK( 5, "minecraft:planks", 1, MaterialType.BLOCK, "SPRUCE_PLANKS" ),
	BIRCH_WOOD_PLANK( 5, "minecraft:planks", 2, MaterialType.BLOCK, "BIRCH_PLANKS" ),
	JUNGLE_WOOD_PLANK( 5, "minecraft:planks", 3, MaterialType.BLOCK, "JUNGLE_PLANKS" ),
	ACACIA_WOOD_PLANK( 5, "minecraft:planks", 4, MaterialType.BLOCK, "ACACIA_PLANKS" ),
	DARK_OAK_WOOD_PLANK( 5, "minecraft:planks", 5, MaterialType.BLOCK, "DARK_OAK_PLANKS" ),
	
	OAK_SAPLING( 6, "minecraft:sapling", 0, MaterialType.BLOCK ),
	SPRUCE_SAPLING( 6, "minecraft:sapling", 1, MaterialType.BLOCK ),
	BIRCH_SAPLING( 6, "minecraft:sapling", 2, MaterialType.BLOCK ),
	JUNGLE_SAPLING( 6, "minecraft:sapling", 3, MaterialType.BLOCK ),
	ACACIA_SAPLING( 6, "minecraft:sapling", 4, MaterialType.BLOCK ),
	DARK_OAK_SAPLING( 6, "minecraft:sapling", 5, MaterialType.BLOCK ),
	BEDROCK( 7, "minecraft:bedrock", 0, MaterialType.BLOCK ),
	
	FLOWING_WATER( 8, "minecraft:flowing_water", 0, MaterialType.BLOCK, "WATER" ),
	
	STILL_WATER( 9, "minecraft:water", 0, MaterialType.BLOCK, "STATIONARY_WATER"),
	
	STATIONARY_WATER_01( 9, "minecraft:water", 1, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_02( 9, "minecraft:water", 2, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_03( 9, "minecraft:water", 3, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_04( 9, "minecraft:water", 4, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_05( 9, "minecraft:water", 5, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_06( 9, "minecraft:water", 6, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_07( 9, "minecraft:water", 7, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_08( 9, "minecraft:water", 8, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_09( 9, "minecraft:water", 9, MaterialType.BLOCK, "STATIONARY_WATER"),
	STATIONARY_WATER_10( 9, "minecraft:water", 10, MaterialType.BLOCK, "STATIONARY_WATER"),
	
	
	FLOWING_LAVA( 10, "minecraft:flowing_lava", 0, MaterialType.BLOCK, "LAVA" ),
	STILL_LAVA( 11, "minecraft:lava", 0, MaterialType.BLOCK ),
	
	SAND( 12, "minecraft:sand", 0, MaterialType.BLOCK ),
	RED_SAND( 12, "minecraft:sand", 1, MaterialType.BLOCK ),
	GRAVEL( 13, "minecraft:gravel", 0, MaterialType.BLOCK ),
	GOLD_ORE( 14, "minecraft:gold_ore", 0, MaterialType.BLOCK ),
	IRON_ORE( 15, "minecraft:iron_ore", 0, MaterialType.BLOCK ),
	COAL_ORE( 16, "minecraft:coal_ore", 0, MaterialType.BLOCK ),

	OAK_WOOD( 17, "minecraft:log", 0, MaterialType.BLOCK ),
	SPRUCE_WOOD( 17, "minecraft:log", 1, MaterialType.BLOCK ),
	BIRCH_WOOD( 17, "minecraft:log", 2, MaterialType.BLOCK ),
	JUNGLE_WOOD( 17, "minecraft:log", 3, MaterialType.BLOCK, "jungle_planks" ),
	
	OAK_LEAVES( 18, "minecraft:leaves", 0, MaterialType.BLOCK ),
	SPRUCE_LEAVES( 18, "minecraft:leaves", 1, MaterialType.BLOCK ),
	BIRCH_LEAVES( 18, "minecraft:leaves", 2, MaterialType.BLOCK ),
	JUNGLE_LEAVES( 18, "minecraft:leaves", 3, MaterialType.BLOCK ),

	SPONGE( 19, "minecraft:sponge", 0, MaterialType.BLOCK ),
	WET_SPONGE( 19, "minecraft:sponge", 1, MaterialType.BLOCK ),
	GLASS( 20, "minecraft:glass", 0, MaterialType.BLOCK ),
	
	
	LAPIS_ORE( 21, "minecraft:lapis_ore", 0, MaterialType.BLOCK, "LAPIS_LAZULI_ORE" ),
	LAPIS_LAZULI_ORE( 21, "minecraft:lapis_ore", 0, MaterialType.BLOCK ), // obsolete...
	
	LAPIS_BLOCK( 22, "minecraft:lapis_block", 0, MaterialType.BLOCK, "LAPIS_LAZULI_BLOCK" ),
	LAPIS_LAZULI_BLOCK( 22, "minecraft:lapis_block", 0, MaterialType.BLOCK ), // obsolete...
	
	DISPENSER( 23, "minecraft:dispenser", 0, MaterialType.BLOCK ),
	SANDSTONE( 24, "minecraft:sandstone", 0, MaterialType.BLOCK ),
	CHISELED_SANDSTONE( 24, "minecraft:sandstone", 1, MaterialType.BLOCK ),
	SMOOTH_SANDSTONE( 24, "minecraft:sandstone", 2, MaterialType.BLOCK ),
	NOTE_BLOCK( 25, "minecraft:noteblock", 0, MaterialType.BLOCK ),
	BED( 26, "minecraft:bed", 0 ),
	POWERED_RAIL( 27, "minecraft:golden_rail", 0, MaterialType.BLOCK ),
	DETECTOR_RAIL( 28, "minecraft:detector_rail", 0, MaterialType.BLOCK ),
	STICKY_PISTON( 29, "minecraft:sticky_piston", 0, MaterialType.BLOCK ),
	COBWEB( 30, "minecraft:web", 0, MaterialType.BLOCK ),
	DEAD_SHRUB( 31, "minecraft:tallgrass", 0, MaterialType.BLOCK, "DEAD_BUSH" ),
	TALL_GRASS( 31, "minecraft:tallgrass", 1, MaterialType.BLOCK ),
	FERN( 31, "minecraft:tallgrass", 2, MaterialType.BLOCK ),
	DEAD_BUSH( 32, "minecraft:deadbush", 0, MaterialType.BLOCK, "DEAD_BUSH" ),
	PISTON( 33, "minecraft:piston", 0, MaterialType.BLOCK ),
	PISTON_HEAD( 34, "minecraft:piston_head", 0, MaterialType.BLOCK ),
	WHITE_WOOL( 35, "minecraft:wool", 0, MaterialType.BLOCK ),
	ORANGE_WOOL( 35, "minecraft:wool", 1, MaterialType.BLOCK ),
	MAGENTA_WOOL( 35, "minecraft:wool", 2, MaterialType.BLOCK ),
	LIGHT_BLUE_WOOL( 35, "minecraft:wool", 3, MaterialType.BLOCK ),
	YELLOW_WOOL( 35, "minecraft:wool", 4, MaterialType.BLOCK ),
	LIME_WOOL( 35, "minecraft:wool", 5, MaterialType.BLOCK ),
	PINK_WOOL( 35, "minecraft:wool", 6, MaterialType.BLOCK ),
	GRAY_WOOL( 35, "minecraft:wool", 7, MaterialType.BLOCK ),
	LIGHT_GRAY_WOOL( 35, "minecraft:wool", 8, MaterialType.BLOCK ),
	CYAN_WOOL( 35, "minecraft:wool", 9, MaterialType.BLOCK ),
	PURPLE_WOOL( 35, "minecraft:wool", 10, MaterialType.BLOCK ),
	BLUE_WOOL( 35, "minecraft:wool", 11, MaterialType.BLOCK ),
	BROWN_WOOL( 35, "minecraft:wool", 12, MaterialType.BLOCK ),
	GREEN_WOOL( 35, "minecraft:wool", 13, MaterialType.BLOCK ),
	RED_WOOL( 35, "minecraft:wool", 14, MaterialType.BLOCK ),
	BLACK_WOOL( 35, "minecraft:wool", 15, MaterialType.BLOCK ),
	
	DANDELION( 37, "minecraft:yellow_flower", 0, MaterialType.BLOCK ),
	POPPY( 38, "minecraft:red_flower", 0, MaterialType.BLOCK, "RED_ROSE" ),
	BLUE_ORCHID( 38, "minecraft:red_flower", 1, MaterialType.BLOCK ),
	ALLIUM( 38, "minecraft:red_flower", 2, MaterialType.BLOCK ),
	AZURE_BLUET( 38, "minecraft:red_flower", 3, MaterialType.BLOCK, "AZURE_BLUET" ),
	RED_TULIP( 38, "minecraft:red_flower", 4, MaterialType.BLOCK ),
	ORANGE_TULIP( 38, "minecraft:red_flower", 5, MaterialType.BLOCK ),
	WHITE_TULIP( 38, "minecraft:red_flower", 6, MaterialType.BLOCK ),
	PINK_TULIP( 38, "minecraft:red_flower", 7, MaterialType.BLOCK ),
	OXEYE_DAISY( 38, "minecraft:red_flower", 8, MaterialType.BLOCK ),
	BROWN_MUSHROOM( 39, "minecraft:brown_mushroom", 0, MaterialType.BLOCK ),
	RED_MUSHROOM( 40, "minecraft:red_mushroom", 0, MaterialType.BLOCK ),
	GOLD_BLOCK( 41, "minecraft:gold_block", 0, MaterialType.BLOCK ),
	IRON_BLOCK( 42, "minecraft:iron_block", 0, MaterialType.BLOCK ),

	
	STONE_SLAB( 44, "minecraft:stone_slab", 0, MaterialType.BLOCK ),
	SANDSTONE_SLAB( 44, "minecraft:stone_slab", 1, MaterialType.BLOCK ),
	WOODEN_SLAB( 44, "minecraft:stone_slab", 2, MaterialType.BLOCK ),
	COBBLESTONE_SLAB( 44, "minecraft:stone_slab", 3, MaterialType.BLOCK ),
	BRICK_SLAB( 44, "minecraft:stone_slab", 4, MaterialType.BLOCK, "STONE_BRICK_SLAB" ),
	STONE_BRICK_SLAB( 44, "minecraft:stone_slab", 5, MaterialType.BLOCK ),
	NETHER_BRICK_SLAB( 44, "minecraft:stone_slab", 6, MaterialType.BLOCK ),
	QUARTZ_SLAB( 44, "minecraft:stone_slab", 7, MaterialType.BLOCK ),
	BRICKS( 45, "minecraft:brick_block", 0, MaterialType.BLOCK ),
	TNT( 46, "minecraft:tnt", 0, MaterialType.BLOCK ),
	BOOKSHELF( 47, "minecraft:bookshelf", 0, MaterialType.BLOCK ),
	
	MOSSY_COBBLESTONE( 48, "minecraft:mossy_cobblestone", 0, MaterialType.BLOCK, "MOSSY_COBBLESTONE" ),
	MOSS_STONE( 48, "minecraft:mossy_cobblestone", 0, MaterialType.BLOCK, "MOSSY_COBBLESTONE", "MOSS_STONE" ),
	
	OBSIDIAN( 49, "minecraft:obsidian", 0, MaterialType.BLOCK ),
	TORCH( 50, "minecraft:torch", 0, MaterialType.BLOCK ),
	FIRE( 51, "minecraft:fire", 0, MaterialType.BLOCK ),
	MONSTER_SPAWNER( 52, "minecraft:mob_spawner", 0, MaterialType.BLOCK ),
	OAK_WOOD_STAIRS( 53, "minecraft:oak_stairs", 0, MaterialType.BLOCK ),
	CHEST( 54, "minecraft:chest", 0, MaterialType.BLOCK ),
	REDSTONE_WIRE( 55, "minecraft:redstone_wire", 0, MaterialType.BLOCK ),
	DIAMOND_ORE( 56, "minecraft:diamond_ore", 0, MaterialType.BLOCK ),
	DIAMOND_BLOCK( 57, "minecraft:diamond_block", 0, MaterialType.BLOCK ),
	CRAFTING_TABLE( 58, "minecraft:crafting_table", 0, MaterialType.BLOCK ),
	WHEAT_CROPS( 59, "minecraft:wheat", 0, MaterialType.BLOCK ),
	FARMLAND( 60, "minecraft:farmland", 0, MaterialType.BLOCK ),
	FURNACE( 61, "minecraft:furnace", 0, MaterialType.BLOCK ),
	BURNING_FURNACE( 62, "minecraft:lit_furnace", 0, MaterialType.BLOCK ),
	STANDING_SIGN_BLOCK( 63, "minecraft:standing_sign", 0, MaterialType.BLOCK, "OAK_SIGN" ),
	OAK_DOOR_BLOCK( 64, "minecraft:wooden_door", 0, MaterialType.BLOCK ),
	LADDER( 65, "minecraft:ladder", 0, MaterialType.BLOCK ),
	RAIL( 66, "minecraft:rail", 0, MaterialType.BLOCK ),
	COBBLESTONE_STAIRS( 67, "minecraft:stone_stairs", 0, MaterialType.BLOCK ),
	WALL_MOUNTED_SIGN_BLOCK( 68, "minecraft:wall_sign", 0 ),
	LEVER( 69, "minecraft:lever", 0, MaterialType.BLOCK ),
	STONE_PRESSURE_PLATE( 70, "minecraft:stone_pressure_plate", 0, MaterialType.BLOCK ),
	IRON_DOOR_BLOCK( 71, "minecraft:iron_door", 0, MaterialType.BLOCK ),
	
	WOODEN_PRESSURE_PLATE( 72, "minecraft:wooden_pressure_plate", 0, MaterialType.BLOCK, 
										"OAK_PRESSURE_PLATE", "WOOD_PLATE" ),
	REDSTONE_ORE( 73, "minecraft:redstone_ore", 0, MaterialType.BLOCK ),

	GLOWING_REDSTONE_ORE( 74, "minecraft:lit_redstone_ore", 0, MaterialType.BLOCK ),
	REDSTONE_TORCH_OFF( 75, "minecraft:unlit_redstone_torch", 0, MaterialType.BLOCK ),
	REDSTONE_TORCH_ON( 76, "minecraft:redstone_torch", 0, MaterialType.BLOCK ),
	STONE_BUTTON( 77, "minecraft:stone_button", 0, MaterialType.BLOCK ),

	SNOW( 78, "minecraft:snow_layer", 0, MaterialType.BLOCK ),
	ICE( 79, "minecraft:ice", 0, MaterialType.BLOCK ),
	SNOW_BLOCK( 80, "minecraft:snow", 0, MaterialType.BLOCK ),

	CACTUS( 81, "minecraft:cactus", 0, MaterialType.BLOCK ),

	CLAY( 82, "minecraft:clay", 0, MaterialType.BLOCK, "HARD_CLAY" ),
	SUGAR_CANES( 83, "minecraft:reeds", 0, MaterialType.BLOCK, "SUGAR_CANE", "SUGAR_CANE_BLOCK" ),
	JUKEBOX( 84, "minecraft:jukebox", 0, MaterialType.BLOCK ),
	OAK_FENCE( 85, "minecraft:fence", 0, MaterialType.BLOCK ),
	PUMPKIN( 86, "minecraft:pumpkin", 0, MaterialType.BLOCK ),
	NETHERRACK( 87, "minecraft:netherrack", 0, MaterialType.BLOCK ),

	SOUL_SAND( 88, "minecraft:soul_sand", 0, MaterialType.BLOCK ),
	GLOWSTONE( 89, "minecraft:glowstone", 0, MaterialType.BLOCK ),
	NETHER_PORTAL( 90, "minecraft:portal", 0, MaterialType.BLOCK ),
	
	JACK_OLANTERN( 91, "minecraft:lit_pumpkin", 0, MaterialType.BLOCK, "jack_o_lantern" ),
	
	CAKE_BLOCK( 92, "minecraft:cake", 0, MaterialType.BLOCK ),
	
	REDSTONE_REPEATER_BLOCK_OFF( 93, "minecraft:unpowered_repeater", 0, MaterialType.BLOCK, "REPEATER" ),
	REDSTONE_REPEATER_BLOCK_ON( 94, "minecraft:powered_repeater", 0, MaterialType.BLOCK, "REPEATER" ),
	
	WHITE_STAINED_GLASS( 95, "minecraft:stained_glass", 0, MaterialType.BLOCK ),
	ORANGE_STAINED_GLASS( 95, "minecraft:stained_glass", 1, MaterialType.BLOCK ),
	MAGENTA_STAINED_GLASS( 95, "minecraft:stained_glass", 2, MaterialType.BLOCK ),
	LIGHT_BLUE_STAINED_GLASS( 95, "minecraft:stained_glass", 3, MaterialType.BLOCK ),
	YELLOW_STAINED_GLASS( 95, "minecraft:stained_glass", 4, MaterialType.BLOCK ),
	LIME_STAINED_GLASS( 95, "minecraft:stained_glass", 5, MaterialType.BLOCK ),
	PINK_STAINED_GLASS( 95, "minecraft:stained_glass", 6, MaterialType.BLOCK ),
	GRAY_STAINED_GLASS( 95, "minecraft:stained_glass", 7, MaterialType.BLOCK ),
	LIGHT_GRAY_STAINED_GLASS( 95, "minecraft:stained_glass", 8, MaterialType.BLOCK ),
	CYAN_STAINED_GLASS( 95, "minecraft:stained_glass", 9, MaterialType.BLOCK ),
	PURPLE_STAINED_GLASS( 95, "minecraft:stained_glass", 10, MaterialType.BLOCK ),
	BLUE_STAINED_GLASS( 95, "minecraft:stained_glass", 11, MaterialType.BLOCK ),
	BROWN_STAINED_GLASS( 95, "minecraft:stained_glass", 12, MaterialType.BLOCK ),
	GREEN_STAINED_GLASS( 95, "minecraft:stained_glass", 13, MaterialType.BLOCK ),
	RED_STAINED_GLASS( 95, "minecraft:stained_glass", 14, MaterialType.BLOCK ),
	BLACK_STAINED_GLASS( 95, "minecraft:stained_glass", 15, MaterialType.BLOCK ),
	WOODEN_TRAPDOOR( 96, "minecraft:trapdoor", 0, MaterialType.BLOCK, "oak_trapdoor" ),
	
	STONE_MONSTER_EGG( 97, "minecraft:monster_egg", 0, MaterialType.BLOCK, "INFESTED_STONE" ),
	COBBLESTONE_MONSTER_EGG( 97, "minecraft:monster_egg", 1, MaterialType.BLOCK, "INFESTED_COBBLESTONE" ),
	STONE_BRICK_MONSTER_EGG( 97, "minecraft:monster_egg", 2, MaterialType.BLOCK, "INFESTED_STONE_BRICKS" ),
	MOSSY_STONE_BRICK_MONSTER_EGG( 97, "minecraft:monster_egg", 3, MaterialType.BLOCK, "INFESTED_MOSSY_STONE_BRICKS" ),
	CRACKED_STONE_BRICK_MONSTER_EGG( 97, "minecraft:monster_egg", 4, MaterialType.BLOCK, "INFESTED_CRACKED_STONE_BRICKS" ),
	CHISELED_STONE_BRICK_MONSTER_EGG( 97, "minecraft:monster_egg", 5, MaterialType.BLOCK, "INFESTED_CHISELED_STONE_BRICKS" ),
	
	STONE_BRICKS( 98, "minecraft:stonebrick", 0, MaterialType.BLOCK, "STONE_BRICKS" ),
	MOSSY_STONE_BRICKS( 98, "minecraft:stonebrick", 1, MaterialType.BLOCK, "MOSSY_STONE_BRICKS" ),
	CRACKED_STONE_BRICKS( 98, "minecraft:stonebrick", 2, MaterialType.BLOCK, "CRACKED_STONE_BRICKS" ),
	CHISELED_STONE_BRICKS( 98, "minecraft:stonebrick", 3, MaterialType.BLOCK, "CHISELED_STONE_BRICKS" ),
	
	BROWN_MUSHROOM_BLOCK( 99, "minecraft:brown_mushroom_block", 0, MaterialType.BLOCK ),
	HUGE_MUSHROOM_1( 99, "minecraft:brown_mushroom_block", 14, MaterialType.BLOCK ),
	RED_MUSHROOM_BLOCK( 100, "minecraft:red_mushroom_block", 0, MaterialType.BLOCK ),
	HUGE_MUSHROOM_2( 100, "minecraft:red_mushroom_block", 14, MaterialType.BLOCK ),
	
	IRON_BARS( 101, "minecraft:iron_bars", 0, MaterialType.BLOCK ),
	GLASS_PANE( 102, "minecraft:glass_pane", 0, MaterialType.BLOCK ),
	MELON_BLOCK( 103, "minecraft:melon_block", 0, MaterialType.BLOCK ),
	PUMPKIN_STEM( 104, "minecraft:pumpkin_stem", 0, MaterialType.BLOCK ),
	MELON_STEM( 105, "minecraft:melon_stem", 0, MaterialType.BLOCK ),
	VINES( 106, "minecraft:vine", 0, MaterialType.BLOCK ),
	OAK_FENCE_GATE( 107, "minecraft:fence_gate", 0, MaterialType.BLOCK ),
	BRICK_STAIRS( 108, "minecraft:brick_stairs", 0, MaterialType.BLOCK ),
	STONE_BRICK_STAIRS( 109, "minecraft:stone_brick_stairs", 0, MaterialType.BLOCK ),
	MYCELIUM( 110, "minecraft:mycelium", 0, MaterialType.BLOCK ),
	LILY_PAD( 111, "minecraft:waterlily", 0, MaterialType.ITEM ),
	
	NETHER_BRICK( 112, "minecraft:nether_brick", 0, MaterialType.ITEM ),
	NETHER_BRICK_FENCE( 113, "minecraft:nether_brick_fence", 0, MaterialType.BLOCK ),
	NETHER_BRICK_STAIRS( 114, "minecraft:nether_brick_stairs", 0, MaterialType.BLOCK ),
	NETHER_WART( 115, "minecraft:nether_wart", 0, MaterialType.ITEM ),
	ENCHANTMENT_TABLE( 116, "minecraft:enchanting_table", 0, MaterialType.BLOCK ),
	BREWING_STAND( 117, "minecraft:brewing_stand", 0, MaterialType.BLOCK ),
	CAULDRON( 118, "minecraft:cauldron", 0, MaterialType.BLOCK),
	END_PORTAL( 119, "minecraft:end_portal", 0 ),
	END_PORTAL_FRAME( 120, "minecraft:end_portal_frame", 0, MaterialType.BLOCK ),
	END_STONE( 121, "minecraft:end_stone", 0, MaterialType.BLOCK ),
	DRAGON_EGG( 122, "minecraft:dragon_egg", 0, MaterialType.ITEM ),
	
	REDSTONE_LAMP_INACTIVE( 123, "minecraft:redstone_lamp", 0, MaterialType.BLOCK, "REDSTONE_LAMP_OFF" ),
	REDSTONE_LAMP_ACTIVE( 124, "minecraft:lit_redstone_lamp", 0, MaterialType.BLOCK, "REDSTONE_LAMP", "REDSTONE_LAMP_ON" ),
	

	OAK_WOOD_SLAB( 126, "minecraft:wooden_slab", 0, MaterialType.BLOCK ),
	SPRUCE_WOOD_SLAB( 126, "minecraft:wooden_slab", 1, MaterialType.BLOCK ),
	BIRCH_WOOD_SLAB( 126, "minecraft:wooden_slab", 2, MaterialType.BLOCK ),
	JUNGLE_WOOD_SLAB( 126, "minecraft:wooden_slab", 3, MaterialType.BLOCK ),
	ACACIA_WOOD_SLAB( 126, "minecraft:wooden_slab", 4, MaterialType.BLOCK ),
	DARK_OAK_WOOD_SLAB( 126, "minecraft:wooden_slab", 5, MaterialType.BLOCK ),
	COCOA( 127, "minecraft:cocoa", 0, MaterialType.BLOCK ),
	SANDSTONE_STAIRS( 128, "minecraft:sandstone_stairs", 0, MaterialType.BLOCK ),
	EMERALD_ORE( 129, "minecraft:emerald_ore", 0, MaterialType.BLOCK ),
	ENDER_CHEST( 130, "minecraft:ender_chest", 0, MaterialType.BLOCK ),
	TRIPWIRE_HOOK( 131, "minecraft:tripwire_hook", 0, MaterialType.BLOCK ),
	TRIPWIRE( 132, "minecraft:tripwire_hook", 0, MaterialType.BLOCK ),
	EMERALD_BLOCK( 133, "minecraft:emerald_block", 0, MaterialType.BLOCK ),
	SPRUCE_WOOD_STAIRS( 134, "minecraft:spruce_stairs", 0, MaterialType.BLOCK ),
	BIRCH_WOOD_STAIRS( 135, "minecraft:birch_stairs", 0, MaterialType.BLOCK ),
	JUNGLE_WOOD_STAIRS( 136, "minecraft:jungle_stairs", 0, MaterialType.BLOCK ),
	COMMAND_BLOCK( 137, "minecraft:command_block", 0 ),
	BEACON( 138, "minecraft:beacon", 0, MaterialType.BLOCK ),
	COBBLESTONE_WALL( 139, "minecraft:cobblestone_wall", 0, MaterialType.BLOCK ),
	MOSSY_COBBLESTONE_WALL( 139, "minecraft:cobblestone_wall", 1, MaterialType.BLOCK ),
	FLOWER_POT( 140, "minecraft:flower_pot", 0, MaterialType.BLOCK ),
	CARROTS( 141, "minecraft:carrots", 0, MaterialType.BLOCK ),
	POTATOES( 142, "minecraft:potatoes", 0, MaterialType.BLOCK ),
	WOODEN_BUTTON( 143, "minecraft:wooden_button", 0, MaterialType.BLOCK, "OAK_BUTTON", "wood_button" ),
	MOB_HEAD( 144, "minecraft:skull", 0, MaterialType.BLOCK ),
	ANVIL( 145, "minecraft:anvil", 0, MaterialType.BLOCK ),
	TRAPPED_CHEST( 146, "minecraft:trapped_chest", 0, MaterialType.BLOCK ),
	WEIGHTED_PRESSURE_PLATE_LIGHT( 147, "minecraft:light_weighted_pressure_plate", 0, MaterialType.BLOCK ),
	WEIGHTED_PRESSURE_PLATE_HEAVY( 148, "minecraft:heavy_weighted_pressure_plate", 0, MaterialType.BLOCK ),
	
	REDSTONE_COMPARATOR_INACTIVE( 149, "minecraft:unpowered_comparator", 0, MaterialType.BLOCK, "COMPARATOR" ),
	REDSTONE_COMPARATOR_ACTIVE( 150, "minecraft:powered_comparator", 0, MaterialType.BLOCK, "COMPARATOR" ),
	
	DAYLIGHT_SENSOR( 151, "minecraft:daylight_detector", 0, MaterialType.BLOCK ),
	REDSTONE_BLOCK( 152, "minecraft:redstone_block", 0, MaterialType.BLOCK ),
	NETHER_QUARTZ_ORE( 153, "minecraft:quartz_ore", 0, MaterialType.BLOCK ),
	HOPPER( 154, "minecraft:hopper", 0, MaterialType.BLOCK ),
	QUARTZ_BLOCK( 155, "minecraft:quartz_block", 0, MaterialType.BLOCK ),
	CHISELED_QUARTZ_BLOCK( 155, "minecraft:quartz_block", 1, MaterialType.BLOCK ),
	
	PILLAR_QUARTZ_BLOCK( 155, "minecraft:quartz_block", 2, MaterialType.BLOCK, "QUARTZ_PILLAR" ),
	
	QUARTZ_STAIRS( 156, "minecraft:quartz_stairs", 0, MaterialType.BLOCK ),
	ACTIVATOR_RAIL( 157, "minecraft:activator_rail", 0, MaterialType.BLOCK ),
	DROPPER( 158, "minecraft:dropper", 0, MaterialType.BLOCK ),
	
	WHITE_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 0, MaterialType.BLOCK, "WHITE_TERRACOTTA" ),
	ORANGE_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 1, MaterialType.BLOCK, "ORANGE_TERRACOTTA" ),
	MAGENTA_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 2, MaterialType.BLOCK, "MAGENTA_TERRACOTTA" ),
	LIGHT_BLUE_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 3, MaterialType.BLOCK, "LIGHT_BLUE_TERRACOTTA" ),
	YELLOW_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 4, MaterialType.BLOCK, "YELLOW_TERRACOTTA" ),
	LIME_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 5, MaterialType.BLOCK, "LIME_TERRACOTTA" ),
	PINK_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 6, MaterialType.BLOCK, "PINK_TERRACOTTA" ),
	GRAY_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 7, MaterialType.BLOCK, "GRAY_TERRACOTTA" ),
	LIGHT_GRAY_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 8, MaterialType.BLOCK, "LIGHT_GRAY_TERRACOTTA" ),
	CYAN_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 9, MaterialType.BLOCK, "CYAN_TERRACOTTA" ),
	PURPLE_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 10, MaterialType.BLOCK, "PURPLE_TERRACOTTA" ),
	BLUE_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 11, MaterialType.BLOCK, "BLUE_TERRACOTTA" ),
	BROWN_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 12, MaterialType.BLOCK, "BROWN_TERRACOTTA" ),
	GREEN_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 13, MaterialType.BLOCK, "GREEN_TERRACOTTA" ),
	RED_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 14, MaterialType.BLOCK, "RED_TERRACOTTA" ),
	BLACK_STAINED_CLAY( 159, "minecraft:stained_hardened_clay", 15, MaterialType.BLOCK, "BLACK_TERRACOTTA" ),
	
	WHITE_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 0, MaterialType.BLOCK ),
	ORANGE_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 1, MaterialType.BLOCK ),
	MAGENTA_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 2, MaterialType.BLOCK ),
	LIGHT_BLUE_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 3, MaterialType.BLOCK ),
	YELLOW_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 4, MaterialType.BLOCK ),
	LIME_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 5, MaterialType.BLOCK ),
	PINK_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 6, MaterialType.BLOCK ),
	GRAY_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 7, MaterialType.BLOCK ),
	LIGHT_GRAY_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 8, MaterialType.BLOCK ),
	CYAN_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 9, MaterialType.BLOCK ),
	PURPLE_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 10, MaterialType.BLOCK ),
	BLUE_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 11, MaterialType.BLOCK ),
	BROWN_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 12, MaterialType.BLOCK ),
	GREEN_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 13, MaterialType.BLOCK ),
	RED_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 14, MaterialType.BLOCK ),
	BLACK_STAINED_GLASS_PANE( 160, "minecraft:stained_glass_pane", 15, MaterialType.BLOCK ),
	
	ACACIA_LEAVES( 161, "minecraft:leaves2", 0, MaterialType.BLOCK, "ACACIA_LEAVES" ),
	DARK_OAK_LEAVES( 161, "minecraft:leaves2", 1, MaterialType.BLOCK ),
	ACACIA_WOOD( 162, "minecraft:log2", 0, MaterialType.BLOCK ),
	DARK_OAK_WOOD( 162, "minecraft:log2", 1, MaterialType.BLOCK ),
	ACACIA_WOOD_STAIRS( 163, "minecraft:acacia_stairs", 0, MaterialType.BLOCK ),
	DARK_OAK_WOOD_STAIRS( 164, "minecraft:dark_oak_stairs", 0, MaterialType.BLOCK ),
	SLIME_BLOCK( 165, "minecraft:slime", 0, MaterialType.BLOCK ),
	BARRIER( 166, "minecraft:barrier", 0, MaterialType.BLOCK ),
	IRON_TRAPDOOR( 167, "minecraft:iron_trapdoor", 0, MaterialType.BLOCK ),
	PRISMARINE( 168, "minecraft:prismarine", 0, MaterialType.BLOCK ),
	PRISMARINE_BRICKS( 168, "minecraft:prismarine", 1, MaterialType.BLOCK ),
	DARK_PRISMARINE( 168, "minecraft:prismarine", 2, MaterialType.BLOCK ),
	SEA_LANTERN( 169, "minecraft:sea_lantern", 0, MaterialType.BLOCK ),
	HAY_BALE( 170, "minecraft:hay_block", 0, MaterialType.BLOCK ),
	WHITE_CARPET( 171, "minecraft:carpet", 0, MaterialType.BLOCK ),
	ORANGE_CARPET( 171, "minecraft:carpet", 1, MaterialType.BLOCK ),
	MAGENTA_CARPET( 171, "minecraft:carpet", 2, MaterialType.BLOCK ),
	LIGHT_BLUE_CARPET( 171, "minecraft:carpet", 3, MaterialType.BLOCK ),
	YELLOW_CARPET( 171, "minecraft:carpet", 4, MaterialType.BLOCK ),
	LIME_CARPET( 171, "minecraft:carpet", 5, MaterialType.BLOCK ),
	PINK_CARPET( 171, "minecraft:carpet", 6, MaterialType.BLOCK ),
	GRAY_CARPET( 171, "minecraft:carpet", 7, MaterialType.BLOCK ),
	LIGHT_GRAY_CARPET( 171, "minecraft:carpet", 8, MaterialType.BLOCK ),
	CYAN_CARPET( 171, "minecraft:carpet", 9, MaterialType.BLOCK ),
	PURPLE_CARPET( 171, "minecraft:carpet", 10, MaterialType.BLOCK ),
	BLUE_CARPET( 171, "minecraft:carpet", 11, MaterialType.BLOCK ),
	BROWN_CARPET( 171, "minecraft:carpet", 12, MaterialType.BLOCK ),
	GREEN_CARPET( 171, "minecraft:carpet", 13, MaterialType.BLOCK ),
	RED_CARPET( 171, "minecraft:carpet", 14, MaterialType.BLOCK ),
	BLACK_CARPET( 171, "minecraft:carpet", 15, MaterialType.BLOCK ),
	HARDENED_CLAY( 172, "minecraft:hardened_clay", 0, MaterialType.BLOCK, "TERRACOTTA" ),

	COAL_BLOCK( 173, "minecraft:coal_block", 0, MaterialType.BLOCK, "BLOCK_OF_COAL" ),
	BLOCK_OF_COAL( 173, "minecraft:coal_block", 0, MaterialType.BLOCK ), // obsolete...
	
	PACKED_ICE( 174, "minecraft:packed_ice", 0, MaterialType.BLOCK ),
	SUNFLOWER( 175, "minecraft:double_plant", 0, MaterialType.BLOCK ),
	LILAC( 175, "minecraft:double_plant", 1, MaterialType.BLOCK ),
	DOUBLE_TALLGRASS( 175, "minecraft:double_plant", 2, MaterialType.BLOCK ),
	LARGE_FERN( 175, "minecraft:double_plant", 3, MaterialType.BLOCK ),
	ROSE_BUSH( 175, "minecraft:double_plant", 4, MaterialType.BLOCK ),
	PEONY( 175, "minecraft:double_plant", 5, MaterialType.BLOCK ),
	FREE_STANDING_BANNER( 176, "minecraft:standing_banner", 0 ),
	WALL_MOUNTED_BANNER( 177, "minecraft:wall_banner", 0 ),
	INVERTED_DAYLIGHT_SENSOR( 178, "minecraft:daylight_detector_inverted", 0, MaterialType.BLOCK ),
	RED_SANDSTONE( 179, "minecraft:red_sandstone", 0, MaterialType.BLOCK ),
	CHISELED_RED_SANDSTONE( 179, "minecraft:red_sandstone", 1, MaterialType.BLOCK ),
	SMOOTH_RED_SANDSTONE( 179, "minecraft:red_sandstone", 2, MaterialType.BLOCK ),
	RED_SANDSTONE_STAIRS( 180, "minecraft:red_sandstone_stairs", 0, MaterialType.BLOCK ),
	
	RED_SANDSTONE_SLAB( 182, "minecraft:stone_slab2", 0, MaterialType.BLOCK ),
	
	SPRUCE_FENCE_GATE( 183, "minecraft:spruce_fence_gate", 0, MaterialType.BLOCK ),
	BIRCH_FENCE_GATE( 184, "minecraft:birch_fence_gate", 0, MaterialType.BLOCK ),
	JUNGLE_FENCE_GATE( 185, "minecraft:jungle_fence_gate", 0, MaterialType.BLOCK ),
	DARK_OAK_FENCE_GATE( 186, "minecraft:dark_oak_fence_gate", 0, MaterialType.BLOCK ),
	ACACIA_FENCE_GATE( 187, "minecraft:acacia_fence_gate", 0, MaterialType.BLOCK ),
	SPRUCE_FENCE( 188, "minecraft:spruce_fence", 0, MaterialType.BLOCK ),
	BIRCH_FENCE( 189, "minecraft:birch_fence", 0, MaterialType.BLOCK ),
	JUNGLE_FENCE( 190, "minecraft:jungle_fence", 0, MaterialType.BLOCK ),
	DARK_OAK_FENCE( 191, "minecraft:dark_oak_fence", 0, MaterialType.BLOCK ),
	ACACIA_FENCE( 192, "minecraft:acacia_fence", 0, MaterialType.BLOCK ),
	SPRUCE_DOOR_BLOCK( 193, "minecraft:spruce_door", 0, MaterialType.BLOCK ),
	BIRCH_DOOR_BLOCK( 194, "minecraft:birch_door", 0, MaterialType.BLOCK ),
	JUNGLE_DOOR_BLOCK( 195, "minecraft:jungle_door", 0, MaterialType.BLOCK ),
	ACACIA_DOOR_BLOCK( 196, "minecraft:acacia_door", 0, MaterialType.BLOCK ),
	DARK_OAK_DOOR_BLOCK( 197, "minecraft:dark_oak_door", 0, MaterialType.BLOCK ),
	END_ROD( 198, "minecraft:end_rod", 0, MaterialType.BLOCK ),
	CHORUS_PLANT( 199, "minecraft:chorus_plant", 0, MaterialType.BLOCK ),
	CHORUS_FLOWER( 200, "minecraft:chorus_flower", 0, MaterialType.BLOCK ),
	PURPUR_BLOCK( 201, "minecraft:purpur_block", 0, MaterialType.BLOCK ),
	PURPUR_PILLAR( 202, "minecraft:purpur_pillar", 0, MaterialType.BLOCK ),
	PURPUR_STAIRS( 203, "minecraft:purpur_stairs", 0, MaterialType.BLOCK ),
	
	PURPUR_SLAB( 205, "minecraft:purpur_slab", 0, MaterialType.BLOCK ),
	END_STONE_BRICKS( 206, "minecraft:end_bricks", 0, MaterialType.BLOCK ),
	BEETROOT_BLOCK( 207, "minecraft:beetroots", 0, MaterialType.BLOCK ),
	GRASS_PATH( 208, "minecraft:grass_path", 0, MaterialType.BLOCK ),
	END_GATEWAY( 209, "minecraft:end_gateway", 0, MaterialType.BLOCK ),
	REPEATING_COMMAND_BLOCK( 210, "minecraft:repeating_command_block", 0 ),
	CHAIN_COMMAND_BLOCK( 211, "minecraft:chain_command_block", 0 ),
	FROSTED_ICE( 212, "minecraft:frosted_ice", 0, MaterialType.BLOCK ),
	STRUCTURE_BLOCK( 255, "minecraft:structure_block", 0, MaterialType.BLOCK ),
	IRON_SHOVEL( 256, "minecraft:iron_shovel", 0 ),
	IRON_PICKAXE( 257, "minecraft:iron_pickaxe", 0 ),
	IRON_AXE( 258, "minecraft:iron_axe", 0 ),
	FLINT_AND_STEEL( 259, "minecraft:flint_and_steel", 0 ),
	APPLE( 260, "minecraft:apple", 0 ),
	BOW( 261, "minecraft:bow", 0 ),
	ARROW( 262, "minecraft:arrow", 0 ),
	COAL( 263, "minecraft:coal", 0 ),
	CHARCOAL( 263, "minecraft:coal", 1 ),
	DIAMOND( 264, "minecraft:diamond", 0 ),
	IRON_INGOT( 265, "minecraft:iron_ingot", 0 ),
	GOLD_INGOT( 266, "minecraft:gold_ingot", 0 ),
	IRON_SWORD( 267, "minecraft:iron_sword", 0 ),
	WOODEN_SWORD( 268, "minecraft:wooden_sword", 0 ),
	WOODEN_SHOVEL( 269, "minecraft:wooden_shovel", 0 ),
	WOODEN_PICKAXE( 270, "minecraft:wooden_pickaxe", 0 ),
	WOODEN_AXE( 271, "minecraft:wooden_axe", 0 ),
	STONE_SWORD( 272, "minecraft:stone_sword", 0 ),
	STONE_SHOVEL( 273, "minecraft:stone_shovel", 0 ),
	STONE_PICKAXE( 274, "minecraft:stone_pickaxe", 0 ),
	STONE_AXE( 275, "minecraft:stone_axe", 0 ),
	DIAMOND_SWORD( 276, "minecraft:diamond_sword", 0 ),
	DIAMOND_SHOVEL( 277, "minecraft:diamond_shovel", 0 ),
	DIAMOND_PICKAXE( 278, "minecraft:diamond_pickaxe", 0 ),
	DIAMOND_AXE( 279, "minecraft:diamond_axe", 0 ),
	STICK( 280, "minecraft:stick", 0 ),
	BOWL( 281, "minecraft:bowl", 0 ),
	MUSHROOM_STEW( 282, "minecraft:mushroom_stew", 0 ),
	GOLDEN_SWORD( 283, "minecraft:golden_sword", 0 ),
	GOLDEN_SHOVEL( 284, "minecraft:golden_shovel", 0 ),
	GOLDEN_PICKAXE( 285, "minecraft:golden_pickaxe", 0 ),
	GOLDEN_AXE( 286, "minecraft:golden_axe", 0 ),
	STRING( 287, "minecraft:string", 0 ),
	FEATHER( 288, "minecraft:feather", 0 ),
	GUNPOWDER( 289, "minecraft:gunpowder", 0 ),
	WOODEN_HOE( 290, "minecraft:wooden_hoe", 0 ),
	STONE_HOE( 291, "minecraft:stone_hoe", 0 ),
	IRON_HOE( 292, "minecraft:iron_hoe", 0 ),
	DIAMOND_HOE( 293, "minecraft:diamond_hoe", 0 ),
	GOLDEN_HOE( 294, "minecraft:golden_hoe", 0 ),
	WHEAT_SEEDS( 295, "minecraft:wheat_seeds", 0 ),
	WHEAT( 296, "minecraft:wheat", 0 ),
	BREAD( 297, "minecraft:bread", 0 ),
	LEATHER_HELMET( 298, "minecraft:leather_helmet", 0 ),
	LEATHER_TUNIC( 299, "minecraft:leather_chestplate", 0 ),
	LEATHER_PANTS( 300, "minecraft:leather_leggings", 0 ),
	LEATHER_BOOTS( 301, "minecraft:leather_boots", 0 ),
	CHAINMAIL_HELMET( 302, "minecraft:chainmail_helmet", 0 ),
	CHAINMAIL_CHESTPLATE( 303, "minecraft:chainmail_chestplate", 0 ),
	CHAINMAIL_LEGGINGS( 304, "minecraft:chainmail_leggings", 0 ),
	CHAINMAIL_BOOTS( 305, "minecraft:chainmail_boots", 0 ),
	IRON_HELMET( 306, "minecraft:iron_helmet", 0 ),
	IRON_CHESTPLATE( 307, "minecraft:iron_chestplate", 0 ),
	IRON_LEGGINGS( 308, "minecraft:iron_leggings", 0 ),
	IRON_BOOTS( 309, "minecraft:iron_boots", 0 ),
	DIAMOND_HELMET( 310, "minecraft:diamond_helmet", 0 ),
	DIAMOND_CHESTPLATE( 311, "minecraft:diamond_chestplate", 0 ),
	DIAMOND_LEGGINGS( 312, "minecraft:diamond_leggings", 0 ),
	DIAMOND_BOOTS( 313, "minecraft:diamond_boots", 0 ),
	GOLDEN_HELMET( 314, "minecraft:golden_helmet", 0 ),
	GOLDEN_CHESTPLATE( 315, "minecraft:golden_chestplate", 0 ),
	GOLDEN_LEGGINGS( 316, "minecraft:golden_leggings", 0 ),
	GOLDEN_BOOTS( 317, "minecraft:golden_boots", 0 ),
	FLINT( 318, "minecraft:flint", 0 ),
	RAW_PORKCHOP( 319, "minecraft:porkchop", 0 ),
	COOKED_PORKCHOP( 320, "minecraft:cooked_porkchop", 0 ),
	PAINTING( 321, "minecraft:painting", 0 ),
	GOLDEN_APPLE( 322, "minecraft:golden_apple", 0 ),
	ENCHANTED_GOLDEN_APPLE( 322, "minecraft:golden_apple", 1 ),
	SIGN( 323, "minecraft:sign", 0, MaterialType.BLOCK ),
	OAK_DOOR( 324, "minecraft:wooden_door", 0, MaterialType.BLOCK ),
	BUCKET( 325, "minecraft:bucket", 0 ),
	WATER_BUCKET( 326, "minecraft:water_bucket", 0 ),
	LAVA_BUCKET( 327, "minecraft:lava_bucket", 0 ),
	MINECART( 328, "minecraft:minecart", 0 ),
	SADDLE( 329, "minecraft:saddle", 0 ),
	IRON_DOOR( 330, "minecraft:iron_door", 0, MaterialType.BLOCK ),
	REDSTONE( 331, "minecraft:redstone", 0, MaterialType.ITEM ),
	SNOWBALL( 332, "minecraft:snowball", 0 ),
	OAK_BOAT( 333, "minecraft:boat", 0 ),
	LEATHER( 334, "minecraft:leather", 0 ),
	MILK_BUCKET( 335, "minecraft:milk_bucket", 0 ),
	BRICK( 336, "minecraft:brick", 0, MaterialType.BLOCK ),
	CLAY_BALL( 337, "minecraft:clay_ball", 0 ),
	SUGAR_CANES_ITEM( 338, "minecraft:reeds", 0, MaterialType.BLOCK, "SUGAR_CANE" ),
	PAPER( 339, "minecraft:paper", 0 ),
	BOOK( 340, "minecraft:book", 0 ),
	SLIMEBALL( 341, "minecraft:slime_ball", 0 ),
	MINECART_WITH_CHEST( 342, "minecraft:chest_minecart", 0 ),
	MINECART_WITH_FURNACE( 343, "minecraft:furnace_minecart", 0 ),
	EGG( 344, "minecraft:egg", 0 ),
	COMPASS( 345, "minecraft:compass", 0 ),
	FISHING_ROD( 346, "minecraft:fishing_rod", 0 ),
	CLOCK( 347, "minecraft:clock", 0 ),
	GLOWSTONE_DUST( 348, "minecraft:glowstone_dust", 0 ),
	RAW_FISH( 349, "minecraft:fish", 0 ),
	RAW_SALMON( 349, "minecraft:fish", 1 ),
	CLOWNFISH( 349, "minecraft:fish", 2 ),
	PUFFERFISH( 349, "minecraft:fish", 3 ),
	COOKED_FISH( 350, "minecraft:cooked_fish", 0 ),
	COOKED_SALMON( 350, "minecraft:cooked_fish", 1 ),
	
	INK_SACK( 351, "minecraft:dye", 0 ),
	ROSE_RED( 351, "minecraft:dye", 1 ),
	CACTUS_GREEN( 351, "minecraft:dye", 2 ),
	COCO_BEANS( 351, "minecraft:dye", 3 ),
	
	// NOTE: May actually be minecraft:ink_sack which is what XMaterial uses?
	LAPIS_LAZULI( 351, "minecraft:dye", 4 ),
	
	PURPLE_DYE( 351, "minecraft:dye", 5 ),
	CYAN_DYE( 351, "minecraft:dye", 6 ),
	LIGHT_GRAY_DYE( 351, "minecraft:dye", 7 ),
	GRAY_DYE( 351, "minecraft:dye", 8 ),
	PINK_DYE( 351, "minecraft:dye", 9 ),
	LIME_DYE( 351, "minecraft:dye", 10 ),
	DANDELION_YELLOW( 351, "minecraft:dye", 11 ),
	LIGHT_BLUE_DYE( 351, "minecraft:dye", 12 ),
	MAGENTA_DYE( 351, "minecraft:dye", 13 ),
	ORANGE_DYE( 351, "minecraft:dye", 14 ),
	BONE_MEAL( 351, "minecraft:dye", 15 ),
	
	
	BONE( 352, "minecraft:bone", 0 ),
	SUGAR( 353, "minecraft:sugar", 0 ),
	CAKE( 354, "minecraft:cake", 0 ),
	BED_ITEM( 355, "minecraft:bed", 0 ),
	REDSTONE_REPEATER( 356, "minecraft:repeater", 0, MaterialType.BLOCK ),
	COOKIE( 357, "minecraft:cookie", 0 ),
	MAP( 358, "minecraft:filled_map", 0 ),
	SHEARS( 359, "minecraft:shears", 0 ),
	MELON( 360, "minecraft:melon", 0, MaterialType.BLOCK ),
	PUMPKIN_SEEDS( 361, "minecraft:pumpkin_seeds", 0 ),
	MELON_SEEDS( 362, "minecraft:melon_seeds", 0 ),
	RAW_BEEF( 363, "minecraft:beef", 0 ),
	STEAK( 364, "minecraft:cooked_beef", 0 ),
	RAW_CHICKEN( 365, "minecraft:chicken", 0 ),
	COOKED_CHICKEN( 366, "minecraft:cooked_chicken", 0 ),
	ROTTEN_FLESH( 367, "minecraft:rotten_flesh", 0 ),
	ENDER_PEARL( 368, "minecraft:ender_pearl", 0 ),
	BLAZE_ROD( 369, "minecraft:blaze_rod", 0 ),
	GHAST_TEAR( 370, "minecraft:ghast_tear", 0 ),
	GOLD_NUGGET( 371, "minecraft:gold_nugget", 0 ),
	NETHER_WART_ITEM( 372, "minecraft:nether_wart", 0 ),
	POTION( 373, "minecraft:potion", 0 ),
	GLASS_BOTTLE( 374, "minecraft:glass_bottle", 0 ),
	SPIDER_EYE( 375, "minecraft:spider_eye", 0 ),
	FERMENTED_SPIDER_EYE( 376, "minecraft:fermented_spider_eye", 0 ),
	BLAZE_POWDER( 377, "minecraft:blaze_powder", 0 ),
	MAGMA_CREAM( 378, "minecraft:magma_cream", 0 ),
	BREWING_STAND_ITEM( 379, "minecraft:brewing_stand", 0, MaterialType.BLOCK ),
	CAULDRON_ITEM( 380, "minecraft:cauldron", 0, MaterialType.BLOCK ),
	EYE_OF_ENDER( 381, "minecraft:ender_eye", 0 ),
	GLISTERING_MELON( 382, "minecraft:speckled_melon", 0 ),
	SPAWN_CREEPER( 383, "minecraft:spawn_egg", 50 ),
	SPAWN_SKELETON( 383, "minecraft:spawn_egg", 51 ),
	SPAWN_SPIDER( 383, "minecraft:spawn_egg", 52 ),
	SPAWN_ZOMBIE( 383, "minecraft:spawn_egg", 54 ),
	SPAWN_SLIME( 383, "minecraft:spawn_egg", 55 ),
	SPAWN_GHAST( 383, "minecraft:spawn_egg", 56 ),
	SPAWN_PIGMAN( 383, "minecraft:spawn_egg", 57 ),
	SPAWN_ENDERMAN( 383, "minecraft:spawn_egg", 58 ),
	SPAWN_CAVE_SPIDER( 383, "minecraft:spawn_egg", 59 ),
	SPAWN_SILVERFISH( 383, "minecraft:spawn_egg", 60 ),
	SPAWN_BLAZE( 383, "minecraft:spawn_egg", 61 ),
	SPAWN_MAGMA_CUBE( 383, "minecraft:spawn_egg", 62 ),
	SPAWN_BAT( 383, "minecraft:spawn_egg", 65 ),
	SPAWN_WITCH( 383, "minecraft:spawn_egg", 66 ),
	SPAWN_ENDERMITE( 383, "minecraft:spawn_egg", 67 ),
	SPAWN_GUARDIAN( 383, "minecraft:spawn_egg", 68 ),
	SPAWN_SHULKER( 383, "minecraft:spawn_egg", 69 ),
	SPAWN_PIG( 383, "minecraft:spawn_egg", 90 ),
	SPAWN_SHEEP( 383, "minecraft:spawn_egg", 91 ),
	SPAWN_COW( 383, "minecraft:spawn_egg", 92 ),
	SPAWN_CHICKEN( 383, "minecraft:spawn_egg", 93 ),
	SPAWN_SQUID( 383, "minecraft:spawn_egg", 94 ),
	SPAWN_WOLF( 383, "minecraft:spawn_egg", 95 ),
	SPAWN_MOOSHROOM( 383, "minecraft:spawn_egg", 96 ),
	SPAWN_OCELOT( 383, "minecraft:spawn_egg", 98 ),
	SPAWN_HORSE( 383, "minecraft:spawn_egg", 100 ),
	SPAWN_RABBIT( 383, "minecraft:spawn_egg", 101 ),
	SPAWN_VILLAGER( 383, "minecraft:spawn_egg", 120 ),
	BOTTLE_O_ENCHANTING( 384, "minecraft:experience_bottle", 0 ),
	FIRE_CHARGE( 385, "minecraft:fire_charge", 0 ),
	BOOK_AND_QUILL( 386, "minecraft:writable_book", 0 ),
	WRITTEN_BOOK( 387, "minecraft:written_book", 0 ),
	EMERALD( 388, "minecraft:emerald", 0 ),
	ITEM_FRAME( 389, "minecraft:item_frame", 0, MaterialType.BLOCK ),
	FLOWER_POT_ITEM( 390, "minecraft:flower_pot", 0, MaterialType.BLOCK ),
	CARROT( 391, "minecraft:carrot", 0, MaterialType.BLOCK ),
	POTATO( 392, "minecraft:potato", 0, MaterialType.BLOCK ),
	BAKED_POTATO( 393, "minecraft:baked_potato", 0 ),
	POISONOUS_POTATO( 394, "minecraft:poisonous_potato", 0 ),
	EMPTY_MAP( 395, "minecraft:map", 0 ),
	GOLDEN_CARROT( 396, "minecraft:golden_carrot", 0 ),
	MOB_HEAD_SKELETON( 397, "minecraft:skull", 0 ),
	MOB_HEAD_WITHER_SKELETON( 397, "minecraft:skull", 1 ),
	MOB_HEAD_ZOMBIE( 397, "minecraft:skull", 2 ),
	MOB_HEAD_HUMAN( 397, "minecraft:skull", 3 ),
	MOB_HEAD_CREEPER( 397, "minecraft:skull", 4 ),
	MOB_HEAD_DRAGON( 397, "minecraft:skull", 5 ),
	CARROT_ON_A_STICK( 398, "minecraft:carrot_on_a_stick", 0 ),
	NETHER_STAR( 399, "minecraft:nether_star", 0 ),
	PUMPKIN_PIE( 400, "minecraft:pumpkin_pie", 0 ),
	FIREWORK_ROCKET( 401, "minecraft:fireworks", 0 ),
	FIREWORK_STAR( 402, "minecraft:firework_charge", 0 ),
	ENCHANTED_BOOK( 403, "minecraft:enchanted_book", 0 ),
	REDSTONE_COMPARATOR( 404, "minecraft:comparator", 0, MaterialType.BLOCK ),
	NETHER_BRICK_ITEM( 405, "minecraft:netherbrick", 0, MaterialType.ITEM ),
	NETHER_QUARTZ( 406, "minecraft:quartz", 0 ),
	MINECART_WITH_TNT( 407, "minecraft:tnt_minecart", 0 ),
	MINECART_WITH_HOPPER( 408, "minecraft:hopper_minecart", 0 ),
	PRISMARINE_SHARD( 409, "minecraft:prismarine_shard", 0 ),
	PRISMARINE_CRYSTALS( 410, "minecraft:prismarine_crystals", 0 ),
	RAW_RABBIT( 411, "minecraft:rabbit", 0 ),
	COOKED_RABBIT( 412, "minecraft:cooked_rabbit", 0 ),
	RABBIT_STEW( 413, "minecraft:rabbit_stew", 0 ),
	RABBITS_FOOT( 414, "minecraft:rabbit_foot", 0 ),
	RABBIT_HIDE( 415, "minecraft:rabbit_hide", 0 ),
	ARMOR_STAND( 416, "minecraft:armor_stand", 0, MaterialType.BLOCK ),
	IRON_HORSE_ARMOR( 417, "minecraft:iron_horse_armor", 0 ),
	GOLDEN_HORSE_ARMOR( 418, "minecraft:golden_horse_armor", 0 ),
	DIAMOND_HORSE_ARMOR( 419, "minecraft:diamond_horse_armor", 0 ),
	LEAD( 420, "minecraft:lead", 0 ),
	NAME_TAG( 421, "minecraft:name_tag", 0 ),
	MINECART_WITH_COMMAND_BLOCK( 422, "minecraft:command_block_minecart", 0 ),
	RAW_MUTTON( 423, "minecraft:mutton", 0 ),
	COOKED_MUTTON( 424, "minecraft:cooked_mutton", 0 ),
	BANNER( 425, "minecraft:banner", 0, MaterialType.BLOCK ),
	SPRUCE_DOOR( 427, "minecraft:spruce_door", 0, MaterialType.BLOCK ),
	BIRCH_DOOR( 428, "minecraft:birch_door", 0, MaterialType.BLOCK ),
	JUNGLE_DOOR( 429, "minecraft:jungle_door", 0, MaterialType.BLOCK ),
	ACACIA_DOOR( 430, "minecraft:acacia_door", 0, MaterialType.BLOCK ),
	DARK_OAK_DOOR( 431, "minecraft:dark_oak_door", 0, MaterialType.BLOCK ),
	CHORUS_FRUIT( 432, "minecraft:chorus_fruit", 0, MaterialType.BLOCK ),
	POPPED_CHORUS_FRUIT( 433, "minecraft:popped_chorus_fruit", 0 ),
	BEETROOT( 434, "minecraft:beetroot", 0, MaterialType.BLOCK ),
	BEETROOT_SEEDS( 435, "minecraft:beetroot_seeds", 0 ),
	BEETROOT_SOUP( 436, "minecraft:beetroot_soup", 0 ),
	DRAGONS_BREATH( 437, "minecraft:dragon_breath", 0, MaterialType.BLOCK ),
	SPLASH_POTION( 438, "minecraft:splash_potion", 0 ),
	SPECTRAL_ARROW( 439, "minecraft:spectral_arrow", 0 ),
	TIPPED_ARROW( 440, "minecraft:tipped_arrow", 0 ),
	LINGERING_POTION( 441, "minecraft:lingering_potion", 0 ),
	SHIELD( 442, "minecraft:shield", 0 ),
	ELYTRA( 443, "minecraft:elytra", 0 ),
	SPRUCE_BOAT( 444, "minecraft:spruce_boat", 0 ),
	BIRCH_BOAT( 445, "minecraft:birch_boat", 0 ),
	JUNGLE_BOAT( 446, "minecraft:jungle_boat", 0 ),
	ACACIA_BOAT( 447, "minecraft:acacia_boat", 0 ),
	DARK_OAK_BOAT( 448, "minecraft:dark_oak_boat", 0 ),
	DISC_13( 2256, "minecraft:record_13", 0 ),
	CAT_DISC( 2257, "minecraft:record_cat", 0 ),
	BLOCKS_DISC( 2258, "minecraft:record_blocks", 0 ),
	CHIRP_DISC( 2259, "minecraft:record_chirp", 0 ),
	FAR_DISC( 2260, "minecraft:record_far", 0 ),
	MALL_DISC( 2261, "minecraft:record_mall", 0 ),
	MELLOHI_DISC( 2262, "minecraft:record_mellohi", 0 ),
	STAL_DISC( 2263, "minecraft:record_stal", 0 ),
	STRAD_DISC( 2264, "minecraft:record_strad", 0 ),
	WARD_DISC( 2265, "minecraft:record_ward", 0 ),
	DISC_11( 2266, "minecraft:record_11", 0 ),
	WAIT_DISC( 2267, "minecraft:record_wait", 0 ),
	
	
	// Minecraft v1.10.x blocks:
	
	STRUCTURE_VOID( "minecraft:structure_void", MaterialType.BLOCK, MaterialVersion.v1_10 ),
	MAGMA_BLOCK( "minecraft:magma_block", MaterialType.BLOCK, MaterialVersion.v1_10 ),
	BONE_BLOCK( "minecraft:bone_block", MaterialType.BLOCK, MaterialVersion.v1_10 ),

	
	// Minecraft v1.11.x blocks:
	
	SHULKER_BOX( "minecraft:shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),

	WHITE_SHULKER_BOX( "minecraft:white_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	ORANGE_SHULKER_BOX( "minecraft:orange_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	MAGENTA_SHULKER_BOX( "minecraft:magenta_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	LIGHT_BLUE_SHULKER_BOX( "minecraft:light_blue_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	YELLOW_SHULKER_BOX( "minecraft:yellow_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),

	LIME_SHULKER_BOX( "minecraft:lime_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	PINK_SHULKER_BOX( "minecraft:pink_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	GRAY_SHULKER_BOX( "minecraft:gray_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	LIGHT_GRAY_SHULKER_BOX( "minecraft:light_gray_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	CYAN_SHULKER_BOX( "minecraft:cyan_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	
	PURPLE_SHULKER_BOX( "minecraft:purple_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	BLUE_SHULKER_BOX( "minecraft:blue_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	BROWN_SHULKER_BOX( "minecraft:brown_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	GREEN_SHULKER_BOX( "minecraft:green_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	RED_SHULKER_BOX( "minecraft:red_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),
	BLACK_SHULKER_BOX( "minecraft:black_shulker_box", MaterialType.BLOCK, MaterialVersion.v1_11 ),

	

	// Minecraft v1.12.x blocks:
	
	WHITE_GLAZED_TERRACOTTA( "minecraft:white_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	ORANGE_GLAZED_TERRACOTTA( "minecraft:orange_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	MAGENTA_GLAZED_TERRACOTTA( "minecraft:magenta_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	LIGHT_BLUE_GLAZED_TERRACOTTA( "minecraft:light_blue_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	YELLOW_GLAZED_TERRACOTTA( "minecraft:yellow_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),

	LIME_GLAZED_TERRACOTTA( "minecraft:lime_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	PINK_GLAZED_TERRACOTTA( "minecraft:pink_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	GRAY_GLAZED_TERRACOTTA( "minecraft:gray_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	LIGHT_GRAY_GLAZED_TERRACOTTA( "minecraft:light_gray_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	CYAN_GLAZED_TERRACOTTA( "minecraft:cyan_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	
	PURPLE_GLAZED_TERRACOTTA( "minecraft:purple_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BLUE_GLAZED_TERRACOTTA( "minecraft:blue_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BROWN_GLAZED_TERRACOTTA( "minecraft:brown_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	GREEN_GLAZED_TERRACOTTA( "minecraft:green_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	RED_GLAZED_TERRACOTTA( "minecraft:red_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BLACK_GLAZED_TERRACOTTA( "minecraft:black_glazed_terracotta", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	
	
	WHITE_CONCRETE( "minecraft:white_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	ORANGE_CONCRETE( "minecraft:orange_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	MAGENTA_CONCRETE( "minecraft:magenta_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	LIGHT_BLUE_CONCRETE( "minecraft:light_blue_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	YELLOW_CONCRETE( "minecraft:yellow_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	
	LIME_CONCRETE( "minecraft:lime_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	PINK_CONCRETE( "minecraft:pink_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	GRAY_CONCRETE( "minecraft:gray_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	LIGHT_GRAY_CONCRETE( "minecraft:light_gray_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	CYAN_CONCRETE( "minecraft:cyan_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	
	PURPLE_CONCRETE( "minecraft:purple_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BLUE_CONCRETE( "minecraft:blue_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BROWN_CONCRETE( "minecraft:brown_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	GREEN_CONCRETE( "minecraft:green_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	RED_CONCRETE( "minecraft:red_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BLACK_CONCRETE( "minecraft:black_concrete", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	
	
	WHITE_CONCRETE_POWDER( "minecraft:white_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	ORANGE_CONCRETE_POWDER( "minecraft:orange_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	MAGENTA_CONCRETE_POWDER( "minecraft:magenta_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	LIGHT_BLUE_CONCRETE_POWDER( "minecraft:light_blue_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	YELLOW_CONCRETE_POWDER( "minecraft:yellow_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	
	LIME_CONCRETE_POWDER( "minecraft:lime_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	PINK_CONCRETE_POWDER( "minecraft:pink_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	GRAY_CONCRETE_POWDER( "minecraft:gray_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	LIGHT_GRAY_CONCRETE_POWDER( "minecraft:light_gray_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	CYAN_CONCRETE_POWDER( "minecraft:cyan_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	
	PURPLE_CONCRETE_POWDER( "minecraft:purple_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BLUE_CONCRETE_POWDER( "minecraft:blue_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BROWN_CONCRETE_POWDER( "minecraft:brown_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	GREEN_CONCRETE_POWDER( "minecraft:green_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	RED_CONCRETE_POWDER( "minecraft:red_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	BLACK_CONCRETE_POWDER( "minecraft:black_concrete_powder", MaterialType.BLOCK, MaterialVersion.v1_12 ),
	
	
	
	
	// Minecraft v1.13.x blocks:

	CAVE_AIR( "minecraft:cave_air", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	VOID_AIR( "minecraft:void_air", MaterialType.BLOCK, MaterialVersion.v1_13 ),

	BLUE_ICE( "minecraft:blue_ice", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BUBBLE_COLUMN( "minecraft:bubble_column", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	
	TUBE_CORAL( "minecraft:tube_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BRAIN_CORAL( "minecraft:brain_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BUBBLE_CORAL( "minecraft:bubble_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	FIRE_CORAL( "minecraft:fire_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	HORN_CORAL( "minecraft:horn_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),

	DEAD_TUBE_CORAL( "minecraft:dead_tube_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_BRAIN_CORAL( "minecraft:dead_brain_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_BUBBLE_CORAL( "minecraft:dead_bubble_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_FIRE_CORAL( "minecraft:dead_fire_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_HORN_CORAL( "minecraft:dead_horn_coral", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	
	
	TUBE_CORAL_BLOCK( "minecraft:tube_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BRAIN_CORAL_BLOCK( "minecraft:brain_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BUBBLE_CORAL_BLOCK( "minecraft:bubble_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	FIRE_CORAL_BLOCK( "minecraft:fire_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	HORN_CORAL_BLOCK( "minecraft:horn_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	
	DEAD_TUBE_CORAL_BLOCK( "minecraft:dead_tube_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_BRAIN_CORAL_BLOCK( "minecraft:dead_brain_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_BUBBLE_CORAL_BLOCK( "minecraft:dead_bubble_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_FIRE_CORAL_BLOCK( "minecraft:dead_fire_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_HORN_CORAL_BLOCK( "minecraft:dead_horn_coral_block", MaterialType.BLOCK, MaterialVersion.v1_13 ),

	
	TUBE_CORAL_FAN( "minecraft:tube_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BRAIN_CORAL_FAN( "minecraft:brain_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BUBBLE_CORAL_FAN( "minecraft:bubble_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	FIRE_CORAL_FAN( "minecraft:fire_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	HORN_CORAL_FAN( "minecraft:horn_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),

	DEAD_TUBE_CORAL_FAN( "minecraft:dead_tube_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_BRAIN_CORAL_FAN( "minecraft:dead_brain_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_BUBBLE_CORAL_FAN( "minecraft:dead_bubble_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_FIRE_CORAL_FAN( "minecraft:dead_fire_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_HORN_CORAL_FAN( "minecraft:dead_horn_coral_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	

	TUBE_CORAL_WALL_FAN( "minecraft:tube_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BRAIN_CORAL_WALL_FAN( "minecraft:brain_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BUBBLE_CORAL_WALL_FAN( "minecraft:bubble_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	FIRE_CORAL_WALL_FAN( "minecraft:fire_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	HORN_CORAL_WALL_FAN( "minecraft:horn_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	
	DEAD_TUBE_CORAL_WALL_FAN( "minecraft:dead_tube_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_BRAIN_CORAL_WALL_FAN( "minecraft:dead_brain_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_BUBBLE_CORAL_WALL_FAN( "minecraft:dead_bubble_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_FIRE_CORAL_WALL_FAN( "minecraft:dead_fire_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DEAD_HORN_CORAL_WALL_FAN( "minecraft:dead_horn_coral_wall_fan", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	
	
	
	
	ACACIA_LOG( "minecraft:acacia_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	BIRCH_LOG( "minecraft:birch_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	DARK_OAK_LOG( "minecraft:dark_oak_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	JUNGLE_LOG( "minecraft:jungle_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	OAK_LOG( "minecraft:oak_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	SPRUCE_LOG( "minecraft:spruce_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	
	
	STRIPPED_ACACIA_LOG( "minecraft:stripped_acacia_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_BIRCH_LOG( "minecraft:stripped_birch_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_DARK_OAK_LOG( "minecraft:stripped_dark_oak_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_JUNGLE_LOG( "minecraft:stripped_jungle_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_OAK_LOG( "minecraft:stripped_oak_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_SPRUCE_LOG( "minecraft:stripped_spruce_log", MaterialType.BLOCK, MaterialVersion.v1_13 ),

	STRIPPED_ACACIA_WOOD( "minecraft:stripped_acacia_wood", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_BIRCH_WOOD( "minecraft:stripped_birch_wood", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_DARK_OAK_WOOD( "minecraft:stripped_dark_oak_wood", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_JUNGLE_WOOD( "minecraft:stripped_jungle_wood", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_OAK_WOOD( "minecraft:stripped_oak_wood", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	STRIPPED_SPRUCE_WOOD( "minecraft:stripped_spruce_wood", MaterialType.BLOCK, MaterialVersion.v1_13 ),
	
	
	
	// Minecraft v1.14.x blocks:
	BAMBOO( "minecraft:bamboo", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	BAMBOO_SAPLING( "minecraft:bamboo_sapling", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	
	BARREL( "minecraft:barrel", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	BELL( "minecraft:bell", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	BLAST_FURNACE( "minecraft:blast_furnace", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	
	CAMPFIRE( "minecraft:campfire", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	CARTOGRAPHY_TABLE( "minecraft:cartography_table", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	COMPOSTER( "minecraft:composter", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	FLETCHING_TABLE( "minecraft:fletching_table", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	//FLOWERS( "minecraft:bee_nest", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	GRINDSTONE( "minecraft:grindstone", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	JIGSAW( "minecraft:jigsaw", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	LANTERN( "minecraft:lantern", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	LECTERN( "minecraft:lectern", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	
	LOOM( "minecraft:loom", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	// Already exists: NOTE_BLOCK( "minecraft:note_block", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	SCAFFOLDING( "minecraft:scaffolding", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	//SIGNS( "minecraft:bee_nest", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	//SLABS( "minecraft:bee_nest", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	SMITHING_TABLE( "minecraft:smithing_table", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	SMOKER( "minecraft:smoker", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	//STAIRS( "minecraft:bee_nest", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	STONECUTTER( "minecraft:stonecutter", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	SWEET_BERRY_BUSH( "minecraft:sweet_berry_bush", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	//WALLS( "minecraft:bee_nest", MaterialType.BLOCK, MaterialVersion.v1_14 ),
	
	
	
	
	// Minecraft v1.15.x blocks:
	BEE_NEST( "minecraft:bee_nest", MaterialType.BLOCK, MaterialVersion.v1_15 ),
	BEEHIVE( "minecraft:beehive", MaterialType.BLOCK, MaterialVersion.v1_15 ),
	HONEY_BLOCK( "minecraft:honey_block", MaterialType.BLOCK, MaterialVersion.v1_15 ),
	HONEYCOMB_BLOCK( "minecraft:honeycomb_block", MaterialType.BLOCK, MaterialVersion.v1_15 ),
	
	
	
	// Minecraft v1.16.x blocks:
	ANCIENT_DEBRIS( "minecraft:ancient_debris", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	CRYING_OBSIDIAN( "minecraft:crying_obsidian", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	NETHER_GOLD_ORE( "minecraft:nether_gold_ore", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	
	
	BASALT( "minecraft:basal", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	POLISHED_BASALT( "minecraft:polished_basalt", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	NETHERITE_BLOCK( "minecraft:netherite_block", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	
	
	BLACKSTONE( "minecraft:base_stone_blackstone", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	POLISHED_BLACKSTONE( "minecraft:polished_blackstone", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	CHISELED_POLISHED_BLACKSTONE( "minecraft:chiseled_polished_blackstone", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	
	
	NETHER_BRICKS( "minecraft:nether_bricks", MaterialType.BLOCK, MaterialVersion.v1_8 ),
	RED_NETHER_BRICKS( "minecraft:red_nether_bricks", MaterialType.BLOCK, MaterialVersion.v1_10 ),
	CRACKED_NETHER_BRICKS( "minecraft:cracked_nether_bricks", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	CHISELED_NETHER_BRICKS( "minecraft:chiseled_nether_bricks", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	
	
	CRIMSON_PLANKS( "minecraft:crimson_planks", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	WARPED_PLANKS( "minecraft:warped_planks", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	STRIPPED_CRIMSON_HYPHAE( "minecraft:stripped_crimson_hyphae", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	STRIPPED_WARPED_HYPHAE( "minecraft:stripped_warped_hyphae", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	NETHER_WART_BLOCK( "minecraft:nether_wart_block", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	WARPED_WART_BLOCK( "minecraft:warped_wart_block", MaterialType.BLOCK, MaterialVersion.v1_16 ),

	
	LODESTONE( "minecraft:lodestone", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	QUARTZ_BRICKS( "minecraft:quartz_bricks", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	RESPAWN_ANCHOR( "minecraft:respawn_anchor", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	
	
	SHROOMLIGHT( "minecraft:shroomlight", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	SOUL_CAMPFIRE( "minecraft:soul_campfire", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	
	
	SOUL_LANTERN( "minecraft:soul_lantern", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	SOUL_TORCH( "minecraft:soul_torch", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	SOUL_SOIL( "minecraft:soul_soil", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	TARGET( "minecraft:target", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	
	
	TWISTING_VINES( "minecraft:twisting_vines", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	WEEPING_VINES( "minecraft:weeping_vines", MaterialType.BLOCK, MaterialVersion.v1_16 ),
	
	
	

	
	;
    // @formatter:on

    private final int legacyId;
    private final String id;
    private final short data;
    private final MaterialType materialType;
    private final MaterialVersion materialVersion;
    
    private final List<String> altNames;

    BlockType(int legacyId, String id, int data, MaterialType materialType) {
    	this.legacyId = legacyId;
    	this.id = (id != null ? id : "minecraft:" + this.name().toLowerCase());
    	this.data = (short) data;
    	this.materialType = materialType;
    	this.materialVersion = MaterialVersion.v1_8;
    	
    	this.altNames = new ArrayList<>();
    }
    
    
    BlockType(int legacyId, String id, int data, MaterialType materialType, String... altNames) {
    	this( legacyId, id, data, materialType );
    	
    	for ( String altName : altNames ) {
			this.altNames.add( altName );
		}
    }
    
    
    
    BlockType(String id, MaterialType materialType, MaterialVersion materialVersion ) {
    	this.legacyId = -1;
    	this.id = (id != null ? id : "minecraft:" + this.name().toLowerCase());
    	this.data = 0;
    	this.materialType = materialType;
    	this.materialVersion = materialVersion;

    	this.altNames = new ArrayList<>();
    }
    
    BlockType(MaterialType materialType) {
    	this(0, null, 0, materialType);
    }

    BlockType(int legacyId, String id) {
    	this(legacyId, id, 0, MaterialType.NOT_SET);
    }

    BlockType(int legacyId, String id, int data) {
    	this(legacyId, id, data, MaterialType.NOT_SET);
    }

    /**
     * <p>This function is for legacy versions of spigot that
     * uses the data value.  This function will returns a 
     * string value of a material name that
     * XMaterial will be able to use to look up the correct 
     * bukkit material type.
     * </p>
     * 
     * <p>The way it needs to be constructed, is by taking the id, 
     * dropping the "minecraft:" prefix, then if data is non-zero, 
     * add a colon and the value of data.
     * </p>
     * 
     * 
     * @return
     */
    public String getXMaterialNameLegacy() {
    	String xMatName = getId().replace( "minecraft:", "" ) +
    			( getData() > 0 ? ":" + getData() : "" );
    	return xMatName;
    }
    
    /**
     * <p>This function will return the lower case name of the BlockType.
     * This should match  
     * @return
     */
    public String getXMaterialName() {
    	return name().toLowerCase();
    }
    
    public List<String> getXMaterialAltNames() {
    	return getAltNames();
    }
    
    public static BlockType getBlock(int legacyId) {
        return getBlock(legacyId, (short) 0);
    }

    public static BlockType getBlock(int legacyId, short data) {
        for (BlockType block : values()) {
            if (block.getLegacyId() == legacyId) {
                if (block.getData() == data) {
                    return block;
                }
            }
        }
        return null;
    }
    
    /**
     * This is just an alias for getBlock() which checks for matches in 
     * many robust ways with numerous fall backs to ensure the best matching.
     * @param key
     * @return
     */
    public static BlockType fromString( String key ) {
    	return getBlock( key );
    }
    /**
     * <p>Must search first on block name since the block id has potential for duplicates which
     * will corrupt the block list for the mine. If at all possible, only search by the block name.
     * </p>
     * 
     * @param key Block name, id, or number.
     * @return
     */
    public static BlockType getBlock(String key) {
    	BlockType blockType = getBlockByName( key );
    	if ( blockType == null ) {
    		blockType = getBlockById( key );
    	}
    	if ( blockType == null ) {
    		blockType = getBlockByXMaterialName(key);
    	}

        return blockType;
    }

    private static BlockType getBlockById(String id) {
        for (BlockType block : values()) {
            if (block.getId().equalsIgnoreCase(id) || block.name().equalsIgnoreCase(id) ||
            		block.getId().equalsIgnoreCase( "minecraft:" + id )) {
                return block;
            }
        }
        boolean isInt = false;
        try {
            Integer.parseInt(id.replaceAll(":", ""));
            isInt = true;
        } catch (Exception e) {
            isInt = false;
        }
        if (isInt) {
            if (!id.contains(":")) {
                return getBlockWithData(Integer.parseInt(id), (short) 0);
            }
            return getBlockWithData(Integer.parseInt(id.split(":")[0]),
                Short.parseShort(id.split(":")[1]));
        }
//        Prison prison = Prison.get();
//        if ( prison != null && prison.getItemManager() != null ) {
//        	Set<Entry<BlockType, Collection<String>>> entrySet = prison.getItemManager().getItems().entrySet();
//        	for (Map.Entry<BlockType, Collection<String>> entry : entrySet) {
//        		if (entry.getValue().contains(id.toLowerCase())) {
//        			return entry.getKey();
//        		}
//        	}
//        	
//        	return getBlockByName(id);
//        }
        return null;
    }

    private static BlockType getBlockByName(String name) {
        for (BlockType block : values()) {
            if (block.name().equalsIgnoreCase(name)) {
                return block;
            }
        }
        return null;
    }
    
    private static BlockType getBlockByXMaterialName(String name) {
    	for (BlockType block : values()) {
    		if (block.getXMaterialAltNames().size() > 0 ) {
    			for ( String altName : block.getXMaterialAltNames() ) {
					
    				if ( altName.equalsIgnoreCase(name)) {
    					return block;
    				}
				}
    		}
    			
    	}
    	return null;
    }

    public static BlockType getBlockWithData(int id, short data) {
        for (BlockType block : values()) {
            if (block.getLegacyId() == id && block.getData() == data) {
                return block;
            }
        }
        return null;
    }

    public static boolean isDoor(BlockType block) {
        return block == ACACIA_DOOR_BLOCK || block == BIRCH_DOOR_BLOCK
            || block == DARK_OAK_DOOR_BLOCK || block == IRON_DOOR_BLOCK
            || block == JUNGLE_DOOR_BLOCK || block == OAK_DOOR_BLOCK || block == SPRUCE_DOOR_BLOCK;
    }

    public int getLegacyId() {
        return legacyId;
    }

    public String getId() {
        return id;
    }

    public short getData() {
        return data;
    }
    
    public boolean isBlock() {
    	return materialType == MaterialType.BLOCK;
    }
    
    public boolean isItem() {
    	return materialType == MaterialType.ITEM;
    }

    public MaterialType getMaterialType() {
		return materialType;
	}

	public MaterialVersion getMaterialVersion() {
		return materialVersion;
	}
	
	public List<String> getAltNames() {
		return altNames;
	}

	@Override public String toString() {
        return id + ":" + data;
    }

}
