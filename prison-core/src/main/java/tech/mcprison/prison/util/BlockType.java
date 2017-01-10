/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
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

/**
 * All of the blocks in the game.
 *
 * @author Faizaan A. Datoo
 * @author Camouflage100
 * @since API 30
 */
public enum BlockType {

    // This was auto-generated from WorldEdit's blocks.json
    AIR(0, "minecraft:air", 0), STONE(1, "minecraft:stone", 0), GRANITE(1, "minecraft:stone",
        1), POLISHED_GRANITE(1, "minecraft:stone", 2), DIORITE(1, "minecraft:stone",
        3), POLISHED_DIORITE(1, "minecraft:stone", 4), ANDESITE(1, "minecraft:stone",
        5), POLISHED_ANDESITE(1, "minecraft:stone", 6), GRASS(2, "minecraft:grass", 0), DIRT(3,
        "minecraft:dirt", 0), COARSE_DIRT(3, "minecraft:dirt", 1), PODZOL(3, "minecraft:dirt",
        2), COBBLESTONE(4, "minecraft:cobblestone", 0), OAK_WOOD_PLANK(5, "minecraft:planks",
        0), SPRUCE_WOOD_PLANK(5, "minecraft:planks", 1), BIRCH_WOOD_PLANK(5, "minecraft:planks",
        2), JUNGLE_WOOD_PLANK(5, "minecraft:planks", 3), ACACIA_WOOD_PLANK(5, "minecraft:planks",
        4), DARK_OAK_WOOD_PLANK(5, "minecraft:planks", 5), OAK_SAPLING(6, "minecraft:sapling",
        0), SPRUCE_SAPLING(6, "minecraft:sapling", 1), BIRCH_SAPLING(6, "minecraft:sapling",
        2), JUNGLE_SAPLING(6, "minecraft:sapling", 3), ACACIA_SAPLING(6, "minecraft:sapling",
        4), DARK_OAK_SAPLING(6, "minecraft:sapling", 5), BEDROCK(7, "minecraft:bedrock",
        0), FLOWING_WATER(8, "minecraft:flowing_water", 0), STILL_WATER(9, "minecraft:water",
        0), FLOWING_LAVA(10, "minecraft:flowing_lava", 0), STILL_LAVA(11, "minecraft:lava",
        0), SAND(12, "minecraft:sand", 0), RED_SAND(12, "minecraft:sand", 1), GRAVEL(13,
        "minecraft:gravel", 0), GOLD_ORE(14, "minecraft:gold_ore", 0), IRON_ORE(15,
        "minecraft:iron_ore", 0), COAL_ORE(16, "minecraft:coal_ore", 0), OAK_WOOD(17,
        "minecraft:log", 0), SPRUCE_WOOD(17, "minecraft:log", 1), BIRCH_WOOD(17, "minecraft:log",
        2), JUNGLE_WOOD(17, "minecraft:log", 3), OAK_LEAVES(18, "minecraft:leaves",
        0), SPRUCE_LEAVES(18, "minecraft:leaves", 1), BIRCH_LEAVES(18, "minecraft:leaves",
        2), UNGL_LEAVES(18, "minecraft:leaves", 3), SPONGE(19, "minecraft:sponge", 0), WET_SPONGE(
        19, "minecraft:sponge", 1), GLASS(20, "minecraft:glass", 0), LAPIS_LAZULI_ORE(21,
        "minecraft:lapis_ore", 0), LAPIS_LAZULI_BLOCK(22, "minecraft:lapis_block", 0), DISPENSER(23,
        "minecraft:dispenser", 0), SANDSTONE(24, "minecraft:sandstone", 0), CHISELED_SANDSTONE(24,
        "minecraft:sandstone", 1), SMOOTH_SANDSTONE(24, "minecraft:sandstone", 2), NOTE_BLOCK(25,
        "minecraft:noteblock", 0), BED(26, "minecraft:bed", 0), POWERED_RAIL(27,
        "minecraft:golden_rail", 0), DETECTOR_RAIL(28, "minecraft:detector_rail", 0), STICKY_PISTON(
        29, "minecraft:sticky_piston", 0), COBWEB(30, "minecraft:web", 0), DEAD_SHRUB(31,
        "minecraft:tallgrass", 0), TALL_GRASS(31, "minecraft:tallgrass", 1), FERN(31,
        "minecraft:tallgrass", 2), DEAD_BUSH(32, "minecraft:deadbush", 0), PISTON(33,
        "minecraft:piston", 0), PISTON_HEAD(34, "minecraft:piston_head", 0), WHITE_WOOL(35,
        "minecraft:wool", 0), ORANGE_WOOL(35, "minecraft:wool", 1), MAGENTA_WOOL(35,
        "minecraft:wool", 2), LIGHT_BLUE_WOOL(35, "minecraft:wool", 3), YELLOW_WOOL(35,
        "minecraft:wool", 4), LIME_WOOL(35, "minecraft:wool", 5), PINK_WOOL(35, "minecraft:wool",
        6), GRAY_WOOL(35, "minecraft:wool", 7), LIGHT_GRAY_WOOL(35, "minecraft:wool", 8), CYAN_WOOL(
        35, "minecraft:wool", 9), PURPLE_WOOL(35, "minecraft:wool", 10), BLUE_WOOL(35,
        "minecraft:wool", 11), BROWN_WOOL(35, "minecraft:wool", 12), GREEN_WOOL(35,
        "minecraft:wool", 13), RED_WOOL(35, "minecraft:wool", 14), BLACK_WOOL(35, "minecraft:wool",
        15), DANDELION(37, "minecraft:yellow_flower", 0), POPPY(38, "minecraft:red_flower",
        0), BLUE_ORCHID(38, "minecraft:red_flower", 1), ALLIUM(38, "minecraft:red_flower",
        2), AZURE_BLUET(38, "minecraft:red_flower", 3), RED_TULIP(38, "minecraft:red_flower",
        4), ORANGE_TULIP(38, "minecraft:red_flower", 5), WHITE_TULIP(38, "minecraft:red_flower",
        6), PINK_TULIP(38, "minecraft:red_flower", 7), OXEYE_DAISY(38, "minecraft:red_flower",
        8), BROWN_MUSHROOM(39, "minecraft:brown_mushroom", 0), RED_MUSHROOM(40,
        "minecraft:red_mushroom", 0), GOLD_BLOCK(41, "minecraft:gold_block", 0), IRON_BLOCK(42,
        "minecraft:iron_block", 0), DOUBLE_STONE_SLAB(43, "minecraft:double_stone_slab",
        0), DOUBLE_SANDSTONE_SLAB(43, "minecraft:double_stone_slab", 1), DOUBLE_WOODEN_SLAB(43,
        "minecraft:double_stone_slab", 2), DOUBLE_COBBLESTONE_SLAB(43,
        "minecraft:double_stone_slab", 3), DOUBLE_BRICK_SLAB(43, "minecraft:double_stone_slab",
        4), DOUBLE_STONE_BRICK_SLAB(43, "minecraft:double_stone_slab", 5), DOUBLE_NETHER_BRICK_SLAB(
        43, "minecraft:double_stone_slab", 6), DOUBLE_QUARTZ_SLAB(43, "minecraft:double_stone_slab",
        7), STONE_SLAB(44, "minecraft:stone_slab", 0), SANDSTONE_SLAB(44, "minecraft:stone_slab",
        1), WOODEN_SLAB(44, "minecraft:stone_slab", 2), COBBLESTONE_SLAB(44, "minecraft:stone_slab",
        3), BRICK_SLAB(44, "minecraft:stone_slab", 4), STONE_BRICK_SLAB(44, "minecraft:stone_slab",
        5), NETHER_BRICK_SLAB(44, "minecraft:stone_slab", 6), QUARTZ_SLAB(44,
        "minecraft:stone_slab", 7), BRICKS(45, "minecraft:brick_block", 0), TNT(46, "minecraft:tnt",
        0), BOOKSHELF(47, "minecraft:bookshelf", 0), MOSS_STONE(48, "minecraft:mossy_cobblestone",
        0), OBSIDIAN(49, "minecraft:obsidian", 0), TORCH(50, "minecraft:torch", 0), FIRE(51,
        "minecraft:fire", 0), MONSTER_SPAWNER(52, "minecraft:mob_spawner", 0), OAK_WOOD_STAIRS(53,
        "minecraft:oak_stairs", 0), CHEST(54, "minecraft:chest", 0), REDSTONE_WIRE(55,
        "minecraft:redstone_wire", 0), DIAMOND_ORE(56, "minecraft:diamond_ore", 0), DIAMOND_BLOCK(
        57, "minecraft:diamond_block", 0), CRAFTING_TABLE(58, "minecraft:crafting_table",
        0), WHEAT_CROPS(59, "minecraft:wheat", 0), FARMLAND(60, "minecraft:farmland", 0), FURNACE(
        61, "minecraft:furnace", 0), BURNING_FURNACE(62, "minecraft:lit_furnace",
        0), STANDING_SIGN_BLOCK(63, "minecraft:standing_sign", 0), OAK_DOOR_BLOCK(64,
        "minecraft:wooden_door", 0), LADDER(65, "minecraft:ladder", 0), RAIL(66, "minecraft:rail",
        0), COBBLESTONE_STAIRS(67, "minecraft:stone_stairs", 0), WALL_MOUNTED_SIGN_BLOCK(68,
        "minecraft:wall_sign", 0), LEVER(69, "minecraft:lever", 0), STONE_PRESSURE_PLATE(70,
        "minecraft:stone_pressure_plate", 0), IRON_DOOR_BLOCK(71, "minecraft:iron_door",
        0), WOODEN_PRESSURE_PLATE(72, "minecraft:wooden_pressure_plate", 0), REDSTONE_ORE(73,
        "minecraft:redstone_ore", 0), GLOWING_REDSTONE_ORE(74, "minecraft:lit_redstone_ore",
        0), REDSTONE_TORCH_OFF(75, "minecraft:unlit_redstone_torch", 0), REDSTONE_TORCH_ON(76,
        "minecraft:redstone_torch", 0), STONE_BUTTON(77, "minecraft:stone_button", 0), SNOW(78,
        "minecraft:snow_layer", 0), ICE(79, "minecraft:ice", 0), SNOW_BLOCK(80, "minecraft:snow",
        0), CACTUS(81, "minecraft:cactus", 0), CLAY(82, "minecraft:clay", 0), SUGAR_CANES(83,
        "minecraft:reeds", 0), JUKEBOX(84, "minecraft:jukebox", 0), OAK_FENCE(85, "minecraft:fence",
        0), PUMPKIN(86, "minecraft:pumpkin", 0), NETHERRACK(87, "minecraft:netherrack",
        0), SOUL_SAND(88, "minecraft:soul_sand", 0), GLOWSTONE(89, "minecraft:glowstone",
        0), NETHER_PORTAL(90, "minecraft:portal", 0), JACK_OLANTERN(91, "minecraft:lit_pumpkin",
        0), CAKE_BLOCK(92, "minecraft:cake", 0), REDSTONE_REPEATER_BLOCK_OFF(93,
        "minecraft:unpowered_repeater", 0), REDSTONE_REPEATER_BLOCK_ON(94,
        "minecraft:powered_repeater", 0), WHITE_STAINED_GLASS(95, "minecraft:stained_glass",
        0), ORANGE_STAINED_GLASS(95, "minecraft:stained_glass", 1), MAGENTA_STAINED_GLASS(95,
        "minecraft:stained_glass", 2), LIGHT_BLUE_STAINED_GLASS(95, "minecraft:stained_glass",
        3), YELLOW_STAINED_GLASS(95, "minecraft:stained_glass", 4), LIME_STAINED_GLASS(95,
        "minecraft:stained_glass", 5), PINK_STAINED_GLASS(95, "minecraft:stained_glass",
        6), GRAY_STAINED_GLASS(95, "minecraft:stained_glass", 7), LIGHT_GRAY_STAINED_GLASS(95,
        "minecraft:stained_glass", 8), CYAN_STAINED_GLASS(95, "minecraft:stained_glass",
        9), PURPLE_STAINED_GLASS(95, "minecraft:stained_glass", 10), BLUE_STAINED_GLASS(95,
        "minecraft:stained_glass", 11), BROWN_STAINED_GLASS(95, "minecraft:stained_glass",
        12), GREEN_STAINED_GLASS(95, "minecraft:stained_glass", 13), RED_STAINED_GLASS(95,
        "minecraft:stained_glass", 14), BLACK_STAINED_GLASS(95, "minecraft:stained_glass",
        15), WOODEN_TRAPDOOR(96, "minecraft:trapdoor", 0), STONE_MONSTER_EGG(97,
        "minecraft:monster_egg", 0), COBBLESTONE_MONSTER_EGG(97, "minecraft:monster_egg",
        1), STONE_BRICK_MONSTER_EGG(97, "minecraft:monster_egg", 2), MOSSY_STONE_BRICK_MONSTER_EGG(
        97, "minecraft:monster_egg", 3), CRACKED_STONE_BRICK_MONSTER_EGG(97,
        "minecraft:monster_egg", 4), CHISELED_STONE_BRICK_MONSTER_EGG(97, "minecraft:monster_egg",
        5), STONE_BRICKS(98, "minecraft:stonebrick", 0), MOSSY_STONE_BRICKS(98,
        "minecraft:stonebrick", 1), CRACKED_STONE_BRICKS(98, "minecraft:stonebrick",
        2), CHISELED_STONE_BRICKS(98, "minecraft:stonebrick", 3), BROWN_MUSHROOM_BLOCK(99,
        "minecraft:brown_mushroom_block", 0), RED_MUSHROOM_BLOCK(100,
        "minecraft:red_mushroom_block", 0), IRON_BARS(101, "minecraft:iron_bars", 0), GLASS_PANE(
        102, "minecraft:glass_pane", 0), MELON_BLOCK(103, "minecraft:melon_block", 0), PUMPKIN_STEM(
        104, "minecraft:pumpkin_stem", 0), MELON_STEM(105, "minecraft:melon_stem", 0), VINES(106,
        "minecraft:vine", 0), OAK_FENCE_GATE(107, "minecraft:fence_gate", 0), BRICK_STAIRS(108,
        "minecraft:brick_stairs", 0), STONE_BRICK_STAIRS(109, "minecraft:stone_brick_stairs",
        0), MYCELIUM(110, "minecraft:mycelium", 0), LILY_PAD(111, "minecraft:waterlily",
        0), NETHER_BRICK(112, "minecraft:nether_brick", 0), NETHER_BRICK_FENCE(113,
        "minecraft:nether_brick_fence", 0), NETHER_BRICK_STAIRS(114,
        "minecraft:nether_brick_stairs", 0), NETHER_WART(115, "minecraft:nether_wart",
        0), ENCHANTMENT_TABLE(116, "minecraft:enchanting_table", 0), BREWING_STAND(117,
        "minecraft:brewing_stand", 0), CAULDRON(118, "minecraft:cauldron", 0), END_PORTAL(119,
        "minecraft:end_portal", 0), END_PORTAL_FRAME(120, "minecraft:end_portal_frame",
        0), END_STONE(121, "minecraft:end_stone", 0), DRAGON_EGG(122, "minecraft:dragon_egg",
        0), REDSTONE_LAMP_INACTIVE(123, "minecraft:redstone_lamp", 0), REDSTONE_LAMP_ACTIVE(124,
        "minecraft:lit_redstone_lamp", 0), DOUBLE_OAK_WOOD_SLAB(125, "minecraft:double_wooden_slab",
        0), DOUBLE_SPRUCE_WOOD_SLAB(125, "minecraft:double_wooden_slab", 1), DOUBLE_BIRCH_WOOD_SLAB(
        125, "minecraft:double_wooden_slab", 2), DOUBLE_JUNGLE_WOOD_SLAB(125,
        "minecraft:double_wooden_slab", 3), DOUBLE_ACACIA_WOOD_SLAB(125,
        "minecraft:double_wooden_slab", 4), DOUBLE_DARK_OAK_WOOD_SLAB(125,
        "minecraft:double_wooden_slab", 5), OAK_WOOD_SLAB(126, "minecraft:wooden_slab",
        0), SPRUCE_WOOD_SLAB(126, "minecraft:wooden_slab", 1), BIRCH_WOOD_SLAB(126,
        "minecraft:wooden_slab", 2), JUNGLE_WOOD_SLAB(126, "minecraft:wooden_slab",
        3), ACACIA_WOOD_SLAB(126, "minecraft:wooden_slab", 4), DARK_OAK_WOOD_SLAB(126,
        "minecraft:wooden_slab", 5), COCOA(127, "minecraft:cocoa", 0), SANDSTONE_STAIRS(128,
        "minecraft:sandstone_stairs", 0), EMERALD_ORE(129, "minecraft:emerald_ore", 0), ENDER_CHEST(
        130, "minecraft:ender_chest", 0), TRIPWIRE_HOOK(131, "minecraft:tripwire_hook",
        0), TRIPWIRE(132, "minecraft:tripwire_hook", 0), EMERALD_BLOCK(133,
        "minecraft:emerald_block", 0), SPRUCE_WOOD_STAIRS(134, "minecraft:spruce_stairs",
        0), BIRCH_WOOD_STAIRS(135, "minecraft:birch_stairs", 0), JUNGLE_WOOD_STAIRS(136,
        "minecraft:jungle_stairs", 0), COMMAND_BLOCK(137, "minecraft:command_block", 0), BEACON(138,
        "minecraft:beacon", 0), COBBLESTONE_WALL(139, "minecraft:cobblestone_wall",
        0), MOSSY_COBBLESTONE_WALL(139, "minecraft:cobblestone_wall", 1), FLOWER_POT(140,
        "minecraft:flower_pot", 0), CARROTS(141, "minecraft:carrots", 0), POTATOES(142,
        "minecraft:potatoes", 0), WOODEN_BUTTON(143, "minecraft:wooden_button", 0), MOB_HEAD(144,
        "minecraft:skull", 0), ANVIL(145, "minecraft:anvil", 0), TRAPPED_CHEST(146,
        "minecraft:trapped_chest", 0), WEIGHTED_PRESSURE_PLATE_LIGHT(147,
        "minecraft:light_weighted_pressure_plate", 0), WEIGHTED_PRESSURE_PLATE_HEAVY(148,
        "minecraft:heavy_weighted_pressure_plate", 0), REDSTONE_COMPARATOR_INACTIVE(149,
        "minecraft:unpowered_comparator", 0), REDSTONE_COMPARATOR_ACTIVE(150,
        "minecraft:powered_comparator", 0), DAYLIGHT_SENSOR(151, "minecraft:daylight_detector",
        0), REDSTONE_BLOCK(152, "minecraft:redstone_block", 0), NETHER_QUARTZ_ORE(153,
        "minecraft:quartz_ore", 0), HOPPER(154, "minecraft:hopper", 0), QUARTZ_BLOCK(155,
        "minecraft:quartz_block", 0), CHISELED_QUARTZ_BLOCK(155, "minecraft:quartz_block",
        1), PILLAR_QUARTZ_BLOCK(155, "minecraft:quartz_block", 2), QUARTZ_STAIRS(156,
        "minecraft:quartz_stairs", 0), ACTIVATOR_RAIL(157, "minecraft:activator_rail", 0), DROPPER(
        158, "minecraft:dropper", 0), WHITE_STAINED_CLAY(159, "minecraft:stained_hardened_clay",
        0), ORANGE_STAINED_CLAY(159, "minecraft:stained_hardened_clay", 1), MAGENTA_STAINED_CLAY(
        159, "minecraft:stained_hardened_clay", 2), LIGHT_BLUE_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 3), YELLOW_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 4), LIME_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 5), PINK_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 6), GRAY_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 7), LIGHT_GRAY_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 8), CYAN_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 9), PURPLE_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 10), BLUE_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 11), BROWN_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 12), GREEN_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 13), RED_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 14), BLACK_STAINED_CLAY(159,
        "minecraft:stained_hardened_clay", 15), WHITE_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 0), ORANGE_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 1), MAGENTA_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 2), LIGHT_BLUE_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 3), YELLOW_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 4), LIME_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 5), PINK_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 6), GRAY_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 7), LIGHT_GRAY_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 8), CYAN_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 9), PURPLE_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 10), BLUE_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 11), BROWN_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 12), GREEN_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 13), RED_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 14), BLACK_STAINED_GLASS_PANE(160,
        "minecraft:stained_glass_pane", 15), ACACIA_LEAVES(161, "minecraft:leaves2",
        0), DARK_OAK_LEAVES(161, "minecraft:leaves2", 1), ACACIA_WOOD(162, "minecraft:log2",
        0), DARK_OAK_WOOD(162, "minecraft:log2", 1), ACACIA_WOOD_STAIRS(163,
        "minecraft:acacia_stairs", 0), DARK_OAK_WOOD_STAIRS(164, "minecraft:dark_oak_stairs",
        0), SLIME_BLOCK(165, "minecraft:slime", 0), BARRIER(166, "minecraft:barrier",
        0), IRON_TRAPDOOR(167, "minecraft:iron_trapdoor", 0), PRISMARINE(168,
        "minecraft:prismarine", 0), PRISMARINE_BRICKS(168, "minecraft:prismarine",
        1), DARK_PRISMARINE(168, "minecraft:prismarine", 2), SEA_LANTERN(169,
        "minecraft:sea_lantern", 0), HAY_BALE(170, "minecraft:hay_block", 0), WHITE_CARPET(171,
        "minecraft:carpet", 0), ORANGE_CARPET(171, "minecraft:carpet", 1), MAGENTA_CARPET(171,
        "minecraft:carpet", 2), LIGHT_BLUE_CARPET(171, "minecraft:carpet", 3), YELLOW_CARPET(171,
        "minecraft:carpet", 4), LIME_CARPET(171, "minecraft:carpet", 5), PINK_CARPET(171,
        "minecraft:carpet", 6), GRAY_CARPET(171, "minecraft:carpet", 7), LIGHT_GRAY_CARPET(171,
        "minecraft:carpet", 8), CYAN_CARPET(171, "minecraft:carpet", 9), PURPLE_CARPET(171,
        "minecraft:carpet", 10), BLUE_CARPET(171, "minecraft:carpet", 11), BROWN_CARPET(171,
        "minecraft:carpet", 12), GREEN_CARPET(171, "minecraft:carpet", 13), RED_CARPET(171,
        "minecraft:carpet", 14), BLACK_CARPET(171, "minecraft:carpet", 15), HARDENED_CLAY(172,
        "minecraft:hardened_clay", 0), BLOCK_OF_COAL(173, "minecraft:coal_block", 0), PACKED_ICE(
        174, "minecraft:packed_ice", 0), SUNFLOWER(175, "minecraft:double_plant", 0), LILAC(175,
        "minecraft:double_plant", 1), DOUBLE_TALLGRASS(175, "minecraft:double_plant",
        2), LARGE_FERN(175, "minecraft:double_plant", 3), ROSE_BUSH(175, "minecraft:double_plant",
        4), PEONY(175, "minecraft:double_plant", 5), FREE_STANDING_BANNER(176,
        "minecraft:standing_banner", 0), WALL_MOUNTED_BANNER(177, "minecraft:wall_banner",
        0), INVERTED_DAYLIGHT_SENSOR(178, "minecraft:daylight_detector_inverted", 0), RED_SANDSTONE(
        179, "minecraft:red_sandstone", 0), CHISELED_RED_SANDSTONE(179, "minecraft:red_sandstone",
        1), SMOOTH_RED_SANDSTONE(179, "minecraft:red_sandstone", 2), RED_SANDSTONE_STAIRS(180,
        "minecraft:red_sandstone_stairs", 0), DOUBLE_RED_SANDSTONE_SLAB(181,
        "minecraft:double_stone_slab2", 0), RED_SANDSTONE_SLAB(182, "minecraft:stone_slab2",
        0), SPRUCE_FENCE_GATE(183, "minecraft:spruce_fence_gate", 0), BIRCH_FENCE_GATE(184,
        "minecraft:birch_fence_gate", 0), JUNGLE_FENCE_GATE(185, "minecraft:jungle_fence_gate",
        0), DARK_OAK_FENCE_GATE(186, "minecraft:dark_oak_fence_gate", 0), ACACIA_FENCE_GATE(187,
        "minecraft:acacia_fence_gate", 0), SPRUCE_FENCE(188, "minecraft:spruce_fence",
        0), BIRCH_FENCE(189, "minecraft:birch_fence", 0), JUNGLE_FENCE(190,
        "minecraft:jungle_fence", 0), DARK_OAK_FENCE(191, "minecraft:dark_oak_fence",
        0), ACACIA_FENCE(192, "minecraft:acacia_fence", 0), SPRUCE_DOOR_BLOCK(193,
        "minecraft:spruce_door", 0), BIRCH_DOOR_BLOCK(194, "minecraft:birch_door",
        0), JUNGLE_DOOR_BLOCK(195, "minecraft:jungle_door", 0), ACACIA_DOOR_BLOCK(196,
        "minecraft:acacia_door", 0), DARK_OAK_DOOR_BLOCK(197, "minecraft:dark_oak_door",
        0), END_ROD(198, "minecraft:end_rod", 0), CHORUS_PLANT(199, "minecraft:chorus_plant",
        0), CHORUS_FLOWER(200, "minecraft:chorus_flower", 0), PURPUR_BLOCK(201,
        "minecraft:purpur_block", 0), PURPUR_PILLAR(202, "minecraft:purpur_pillar",
        0), PURPUR_STAIRS(203, "minecraft:purpur_stairs", 0), PURPUR_DOUBLE_SLAB(204,
        "minecraft:purpur_double_slab", 0), PURPUR_SLAB(205, "minecraft:purpur_slab",
        0), END_STONE_BRICKS(206, "minecraft:end_bricks", 0), BEETROOT_BLOCK(207,
        "minecraft:beetroots", 0), GRASS_PATH(208, "minecraft:grass_path", 0), END_GATEWAY(209,
        "minecraft:end_gateway", 0), REPEATING_COMMAND_BLOCK(210,
        "minecraft:repeating_command_block", 0), CHAIN_COMMAND_BLOCK(211,
        "minecraft:chain_command_block", 0), FROSTED_ICE(212, "minecraft:frosted_ice",
        0), STRUCTURE_BLOCK(255, "minecraft:structure_block", 0), IRON_SHOVEL(256,
        "minecraft:iron_shovel", 0), IRON_PICKAXE(257, "minecraft:iron_pickaxe", 0), IRON_AXE(258,
        "minecraft:iron_axe", 0), FLINT_AND_STEEL(259, "minecraft:flint_and_steel", 0), APPLE(260,
        "minecraft:apple", 0), BOW(261, "minecraft:bow", 0), ARROW(262, "minecraft:arrow", 0), COAL(
        263, "minecraft:coal", 0), CHARCOAL(263, "minecraft:coal", 1), DIAMOND(264,
        "minecraft:diamond", 0), IRON_INGOT(265, "minecraft:iron_ingot", 0), GOLD_INGOT(266,
        "minecraft:gold_ingot", 0), IRON_SWORD(267, "minecraft:iron_sword", 0), WOODEN_SWORD(268,
        "minecraft:wooden_sword", 0), WOODEN_SHOVEL(269, "minecraft:wooden_shovel",
        0), WOODEN_PICKAXE(270, "minecraft:wooden_pickaxe", 0), WOODEN_AXE(271,
        "minecraft:wooden_axe", 0), STONE_SWORD(272, "minecraft:stone_sword", 0), STONE_SHOVEL(273,
        "minecraft:stone_shovel", 0), STONE_PICKAXE(274, "minecraft:stone_pickaxe", 0), STONE_AXE(
        275, "minecraft:stone_axe", 0), DIAMOND_SWORD(276, "minecraft:diamond_sword",
        0), DIAMOND_SHOVEL(277, "minecraft:diamond_shovel", 0), DIAMOND_PICKAXE(278,
        "minecraft:diamond_pickaxe", 0), DIAMOND_AXE(279, "minecraft:diamond_axe", 0), STICK(280,
        "minecraft:stick", 0), BOWL(281, "minecraft:bowl", 0), MUSHROOM_STEW(282,
        "minecraft:mushroom_stew", 0), GOLDEN_SWORD(283, "minecraft:golden_sword",
        0), GOLDEN_SHOVEL(284, "minecraft:golden_shovel", 0), GOLDEN_PICKAXE(285,
        "minecraft:golden_pickaxe", 0), GOLDEN_AXE(286, "minecraft:golden_axe", 0), STRING(287,
        "minecraft:string", 0), FEATHER(288, "minecraft:feather", 0), GUNPOWDER(289,
        "minecraft:gunpowder", 0), WOODEN_HOE(290, "minecraft:wooden_hoe", 0), STONE_HOE(291,
        "minecraft:stone_hoe", 0), IRON_HOE(292, "minecraft:iron_hoe", 0), DIAMOND_HOE(293,
        "minecraft:diamond_hoe", 0), GOLDEN_HOE(294, "minecraft:golden_hoe", 0), WHEAT_SEEDS(295,
        "minecraft:wheat_seeds", 0), WHEAT(296, "minecraft:wheat", 0), BREAD(297, "minecraft:bread",
        0), LEATHER_HELMET(298, "minecraft:leather_helmet", 0), LEATHER_TUNIC(299,
        "minecraft:leather_chestplate", 0), LEATHER_PANTS(300, "minecraft:leather_leggings",
        0), LEATHER_BOOTS(301, "minecraft:leather_boots", 0), CHAINMAIL_HELMET(302,
        "minecraft:chainmail_helmet", 0), CHAINMAIL_CHESTPLATE(303,
        "minecraft:chainmail_chestplate", 0), CHAINMAIL_LEGGINGS(304,
        "minecraft:chainmail_leggings", 0), CHAINMAIL_BOOTS(305, "minecraft:chainmail_boots",
        0), IRON_HELMET(306, "minecraft:iron_helmet", 0), IRON_CHESTPLATE(307,
        "minecraft:iron_chestplate", 0), IRON_LEGGINGS(308, "minecraft:iron_leggings",
        0), IRON_BOOTS(309, "minecraft:iron_boots", 0), DIAMOND_HELMET(310,
        "minecraft:diamond_helmet", 0), DIAMOND_CHESTPLATE(311, "minecraft:diamond_chestplate",
        0), DIAMOND_LEGGINGS(312, "minecraft:diamond_leggings", 0), DIAMOND_BOOTS(313,
        "minecraft:diamond_boots", 0), GOLDEN_HELMET(314, "minecraft:golden_helmet",
        0), GOLDEN_CHESTPLATE(315, "minecraft:golden_chestplate", 0), GOLDEN_LEGGINGS(316,
        "minecraft:golden_leggings", 0), GOLDEN_BOOTS(317, "minecraft:golden_boots", 0), FLINT(318,
        "minecraft:flint", 0), RAW_PORKCHOP(319, "minecraft:porkchop", 0), COOKED_PORKCHOP(320,
        "minecraft:cooked_porkchop", 0), PAINTING(321, "minecraft:painting", 0), GOLDEN_APPLE(322,
        "minecraft:golden_apple", 0), ENCHANTED_GOLDEN_APPLE(322, "minecraft:golden_apple",
        1), SIGN(323, "minecraft:sign", 0), OAK_DOOR(324, "minecraft:wooden_door", 0), BUCKET(325,
        "minecraft:bucket", 0), WATER_BUCKET(326, "minecraft:water_bucket", 0), LAVA_BUCKET(327,
        "minecraft:lava_bucket", 0), MINECART(328, "minecraft:minecart", 0), SADDLE(329,
        "minecraft:saddle", 0), IRON_DOOR(330, "minecraft:iron_door", 0), REDSTONE(331,
        "minecraft:redstone", 0), SNOWBALL(332, "minecraft:snowball", 0), OAK_BOAT(333,
        "minecraft:boat", 0), LEATHER(334, "minecraft:leather", 0), MILK_BUCKET(335,
        "minecraft:milk_bucket", 0), BRICK(336, "minecraft:brick", 0), CLAY_BALL(337,
        "minecraft:clay_ball", 0), SUGAR_CANES_ITEM(338, "minecraft:reeds", 0), PAPER(339,
        "minecraft:paper", 0), BOOK(340, "minecraft:book", 0), SLIMEBALL(341,
        "minecraft:slime_ball", 0), MINECART_WITH_CHEST(342, "minecraft:chest_minecart",
        0), MINECART_WITH_FURNACE(343, "minecraft:furnace_minecart", 0), EGG(344, "minecraft:egg",
        0), COMPASS(345, "minecraft:compass", 0), FISHING_ROD(346, "minecraft:fishing_rod",
        0), CLOCK(347, "minecraft:clock", 0), GLOWSTONE_DUST(348, "minecraft:glowstone_dust",
        0), RAW_FISH(349, "minecraft:fish", 0), RAW_SALMON(349, "minecraft:fish", 1), CLOWNFISH(349,
        "minecraft:fish", 2), PUFFERFISH(349, "minecraft:fish", 3), COOKED_FISH(350,
        "minecraft:cooked_fish", 0), COOKED_SALMON(350, "minecraft:cooked_fish", 1), INK_SACK(351,
        "minecraft:dye", 0), ROSE_RED(351, "minecraft:dye", 1), CACTUS_GREEN(351, "minecraft:dye",
        2), COCO_BEANS(351, "minecraft:dye", 3), LAPIS_LAZULI(351, "minecraft:dye", 4), PURPLE_DYE(
        351, "minecraft:dye", 5), CYAN_DYE(351, "minecraft:dye", 6), LIGHT_GRAY_DYE(351,
        "minecraft:dye", 7), GRAY_DYE(351, "minecraft:dye", 8), PINK_DYE(351, "minecraft:dye",
        9), LIME_DYE(351, "minecraft:dye", 10), DANDELION_YELLOW(351, "minecraft:dye",
        11), LIGHT_BLUE_DYE(351, "minecraft:dye", 12), MAGENTA_DYE(351, "minecraft:dye",
        13), ORANGE_DYE(351, "minecraft:dye", 14), BONE_MEAL(351, "minecraft:dye", 15), BONE(352,
        "minecraft:bone", 0), SUGAR(353, "minecraft:sugar", 0), CAKE(354, "minecraft:cake",
        0), BED_ITEM(355, "minecraft:bed", 0), REDSTONE_REPEATER(356, "minecraft:repeater",
        0), COOKIE(357, "minecraft:cookie", 0), MAP(358, "minecraft:filled_map", 0), SHEARS(359,
        "minecraft:shears", 0), MELON(360, "minecraft:melon", 0), PUMPKIN_SEEDS(361,
        "minecraft:pumpkin_seeds", 0), MELON_SEEDS(362, "minecraft:melon_seeds", 0), RAW_BEEF(363,
        "minecraft:beef", 0), STEAK(364, "minecraft:cooked_beef", 0), RAW_CHICKEN(365,
        "minecraft:chicken", 0), COOKED_CHICKEN(366, "minecraft:cooked_chicken", 0), ROTTEN_FLESH(
        367, "minecraft:rotten_flesh", 0), ENDER_PEARL(368, "minecraft:ender_pearl", 0), BLAZE_ROD(
        369, "minecraft:blaze_rod", 0), GHAST_TEAR(370, "minecraft:ghast_tear", 0), GOLD_NUGGET(371,
        "minecraft:gold_nugget", 0), NETHER_WART_ITEM(372, "minecraft:nether_wart", 0), POTION(373,
        "minecraft:potion", 0), GLASS_BOTTLE(374, "minecraft:glass_bottle", 0), SPIDER_EYE(375,
        "minecraft:spider_eye", 0), FERMENTED_SPIDER_EYE(376, "minecraft:fermented_spider_eye",
        0), BLAZE_POWDER(377, "minecraft:blaze_powder", 0), MAGMA_CREAM(378,
        "minecraft:magma_cream", 0), BREWING_STAND_ITEM(379, "minecraft:brewing_stand",
        0), CAULDRON_ITEM(380, "minecraft:cauldron", 0), EYE_OF_ENDER(381, "minecraft:ender_eye",
        0), GLISTERING_MELON(382, "minecraft:speckled_melon", 0), SPAWN_CREEPER(383,
        "minecraft:spawn_egg", 50), SPAWN_SKELETON(383, "minecraft:spawn_egg", 51), SPAWN_SPIDER(
        383, "minecraft:spawn_egg", 52), SPAWN_ZOMBIE(383, "minecraft:spawn_egg", 54), SPAWN_SLIME(
        383, "minecraft:spawn_egg", 55), SPAWN_GHAST(383, "minecraft:spawn_egg", 56), SPAWN_PIGMAN(
        383, "minecraft:spawn_egg", 57), SPAWN_ENDERMAN(383, "minecraft:spawn_egg",
        58), SPAWN_CAVE_SPIDER(383, "minecraft:spawn_egg", 59), SPAWN_SILVERFISH(383,
        "minecraft:spawn_egg", 60), SPAWN_BLAZE(383, "minecraft:spawn_egg", 61), SPAWN_MAGMA_CUBE(
        383, "minecraft:spawn_egg", 62), SPAWN_BAT(383, "minecraft:spawn_egg", 65), SPAWN_WITCH(383,
        "minecraft:spawn_egg", 66), SPAWN_ENDERMITE(383, "minecraft:spawn_egg", 67), SPAWN_GUARDIAN(
        383, "minecraft:spawn_egg", 68), SPAWN_SHULKER(383, "minecraft:spawn_egg", 69), SPAWN_PIG(
        383, "minecraft:spawn_egg", 90), SPAWN_SHEEP(383, "minecraft:spawn_egg", 91), SPAWN_COW(383,
        "minecraft:spawn_egg", 92), SPAWN_CHICKEN(383, "minecraft:spawn_egg", 93), SPAWN_SQUID(383,
        "minecraft:spawn_egg", 94), SPAWN_WOLF(383, "minecraft:spawn_egg", 95), SPAWN_MOOSHROOM(383,
        "minecraft:spawn_egg", 96), SPAWN_OCELOT(383, "minecraft:spawn_egg", 98), SPAWN_HORSE(383,
        "minecraft:spawn_egg", 100), SPAWN_RABBIT(383, "minecraft:spawn_egg", 101), SPAWN_VILLAGER(
        383, "minecraft:spawn_egg", 120), BOTTLE_O_ENCHANTING(384, "minecraft:experience_bottle",
        0), FIRE_CHARGE(385, "minecraft:fire_charge", 0), BOOK_AND_QUILL(386,
        "minecraft:writable_book", 0), WRITTEN_BOOK(387, "minecraft:written_book", 0), EMERALD(388,
        "minecraft:emerald", 0), ITEM_FRAME(389, "minecraft:item_frame", 0), FLOWER_POT_ITEM(390,
        "minecraft:flower_pot", 0), CARROT(391, "minecraft:carrot", 0), POTATO(392,
        "minecraft:potato", 0), BAKED_POTATO(393, "minecraft:baked_potato", 0), POISONOUS_POTATO(
        394, "minecraft:poisonous_potato", 0), EMPTY_MAP(395, "minecraft:map", 0), GOLDEN_CARROT(
        396, "minecraft:golden_carrot", 0), MOB_HEAD_SKELETON(397, "minecraft:skull",
        0), MOB_HEAD_WITHER_SKELETON(397, "minecraft:skull", 1), MOB_HEAD_ZOMBIE(397,
        "minecraft:skull", 2), MOB_HEAD_HUMAN(397, "minecraft:skull", 3), MOB_HEAD_CREEPER(397,
        "minecraft:skull", 4), MOB_HEAD_DRAGON(397, "minecraft:skull", 5), CARROT_ON_A_STICK(398,
        "minecraft:carrot_on_a_stick", 0), NETHER_STAR(399, "minecraft:nether_star",
        0), PUMPKIN_PIE(400, "minecraft:pumpkin_pie", 0), FIREWORK_ROCKET(401,
        "minecraft:fireworks", 0), FIREWORK_STAR(402, "minecraft:firework_charge",
        0), ENCHANTED_BOOK(403, "minecraft:enchanted_book", 0), REDSTONE_COMPARATOR(404,
        "minecraft:comparator", 0), NETHER_BRICK_ITEM(405, "minecraft:netherbrick",
        0), NETHER_QUARTZ(406, "minecraft:quartz", 0), MINECART_WITH_TNT(407,
        "minecraft:tnt_minecart", 0), MINECART_WITH_HOPPER(408, "minecraft:hopper_minecart",
        0), PRISMARINE_SHARD(409, "minecraft:prismarine_shard", 0), PRISMARINE_CRYSTALS(410,
        "minecraft:prismarine_crystals", 0), RAW_RABBIT(411, "minecraft:rabbit", 0), COOKED_RABBIT(
        412, "minecraft:cooked_rabbit", 0), RABBIT_STEW(413, "minecraft:rabbit_stew",
        0), RABBITS_FOOT(414, "minecraft:rabbit_foot", 0), RABBIT_HIDE(415, "minecraft:rabbit_hide",
        0), ARMOR_STAND(416, "minecraft:armor_stand", 0), IRON_HORSE_ARMOR(417,
        "minecraft:iron_horse_armor", 0), GOLDEN_HORSE_ARMOR(418, "minecraft:golden_horse_armor",
        0), DIAMOND_HORSE_ARMOR(419, "minecraft:diamond_horse_armor", 0), LEAD(420,
        "minecraft:lead", 0), NAME_TAG(421, "minecraft:name_tag", 0), MINECART_WITH_COMMAND_BLOCK(
        422, "minecraft:command_block_minecart", 0), RAW_MUTTON(423, "minecraft:mutton",
        0), COOKED_MUTTON(424, "minecraft:cooked_mutton", 0), BANNER(425, "minecraft:banner",
        0), SPRUCE_DOOR(427, "minecraft:spruce_door", 0), BIRCH_DOOR(428, "minecraft:birch_door",
        0), JUNGLE_DOOR(429, "minecraft:jungle_door", 0), ACACIA_DOOR(430, "minecraft:acacia_door",
        0), DARK_OAK_DOOR(431, "minecraft:dark_oak_door", 0), CHORUS_FRUIT(432,
        "minecraft:chorus_fruit", 0), POPPED_CHORUS_FRUIT(433, "minecraft:popped_chorus_fruit",
        0), BEETROOT(434, "minecraft:beetroot", 0), BEETROOT_SEEDS(435, "minecraft:beetroot_seeds",
        0), BEETROOT_SOUP(436, "minecraft:beetroot_soup", 0), DRAGONS_BREATH(437,
        "minecraft:dragon_breath", 0), SPLASH_POTION(438, "minecraft:splash_potion",
        0), SPECTRAL_ARROW(439, "minecraft:spectral_arrow", 0), TIPPED_ARROW(440,
        "minecraft:tipped_arrow", 0), LINGERING_POTION(441, "minecraft:lingering_potion",
        0), SHIELD(442, "minecraft:shield", 0), ELYTRA(443, "minecraft:elytra", 0), SPRUCE_BOAT(444,
        "minecraft:spruce_boat", 0), BIRCH_BOAT(445, "minecraft:birch_boat", 0), JUNGLE_BOAT(446,
        "minecraft:jungle_boat", 0), ACACIA_BOAT(447, "minecraft:acacia_boat", 0), DARK_OAK_BOAT(
        448, "minecraft:dark_oak_boat", 0), DISC_13(2256, "minecraft:record_13", 0), CAT_DISC(2257,
        "minecraft:record_cat", 0), BLOCKS_DISC(2258, "minecraft:record_blocks", 0), CHIRP_DISC(
        2259, "minecraft:record_chirp", 0), FAR_DISC(2260, "minecraft:record_far", 0), MALL_DISC(
        2261, "minecraft:record_mall", 0), MELLOHI_DISC(2262, "minecraft:record_mellohi",
        0), STAL_DISC(2263, "minecraft:record_stal", 0), STRAD_DISC(2264, "minecraft:record_strad",
        0), WARD_DISC(2265, "minecraft:record_ward", 0), DISC_11(2266, "minecraft:record_11",
        0), WAIT_DISC(2267, "minecraft:record_wait", 0);

    private int legacyId;
    private String id;
    private short data;

    BlockType(int legacyId, String id) {
        this.legacyId = legacyId;
        this.id = id;
        this.data = 0;
    }

    BlockType(int legacyId, String id, int data) {
        this.legacyId = legacyId;
        this.id = id;
        this.data = (short) data;
    }

    public static BlockType getBlock(int legacyId) {
        for (BlockType block : values()) {
            if (block.getLegacyId() == legacyId) {
                return block;
            }
        }
        return null;
    }

    public static BlockType getBlock(String id) {
        for (BlockType block : values()) {
            if (block.getId().equalsIgnoreCase(id)) {
                return block;
            }
        }
        return null;
    }

    public static BlockType getBlockByName(String name) {
        for (BlockType block : values()) {
            if (block.name().equalsIgnoreCase(name)) {
                return block;
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

    @Override public String toString() {
        return id + ":" + data;
    }

}
