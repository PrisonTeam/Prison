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

package io.github.prison.util;

/**
 * All of the blocks in the game.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public enum Block {

    // This was auto-generated from WorldEdit's blocks.json

    AIR(0, "minecraft:air"),
    STONE(1, "minecraft:stone"),
    GRASS_BLOCK(2, "minecraft:grass"),
    DIRT(3, "minecraft:dirt"),
    COBBLESTONE(4, "minecraft:cobblestone"),
    WOODEN_PLANKS(5, "minecraft:planks"),
    OAK_SAPLING(6, "minecraft:sapling"),
    BEDROCK(7, "minecraft:bedrock"),
    FLOWING_WATER(8, "minecraft:flowing_water"),
    WATER(9, "minecraft:water"),
    FLOWING_LAVA(10, "minecraft:flowing_lava"),
    LAVA(11, "minecraft:lava"),
    SAND(12, "minecraft:sand"),
    GRAVEL(13, "minecraft:gravel"),
    GOLD_ORE(14, "minecraft:gold_ore"),
    IRON_ORE(15, "minecraft:iron_ore"),
    COAL_ORE(16, "minecraft:coal_ore"),
    LOG(17, "minecraft:log"),
    LEAVES(18, "minecraft:leaves"),
    SPONGE(19, "minecraft:sponge"),
    GLASS(20, "minecraft:glass"),
    LAPIS_LAZULI_ORE(21, "minecraft:lapis_ore"),
    LAPIS_LAZULI_BLOCK(22, "minecraft:lapis_block"),
    DISPENSER(23, "minecraft:dispenser"),
    SANDSTONE(24, "minecraft:sandstone"),
    NOTE_BLOCK(25, "minecraft:noteblock"),
    BED(26, "minecraft:bed"),
    POWERED_RAIL(27, "minecraft:golden_rail"),
    DETECTOR_RAIL(28, "minecraft:detector_rail"),
    STICKY_PISTON(29, "minecraft:sticky_piston"),
    COBWEB(30, "minecraft:web"),
    GRASS(31, "minecraft:tallgrass"),
    DEAD_BUSH(32, "minecraft:deadbush"),
    PISTON(33, "minecraft:piston"),
    PISTON_HEAD(34, "minecraft:piston_head"),
    WOOL(35, "minecraft:wool"),
    PISTON_EXTENSION(36, "minecraft:piston_extension"),
    YELLOW_FLOWER(37, "minecraft:yellow_flower"),
    RED_FLOWER(38, "minecraft:red_flower"),
    BROWN_MUSHROOM(39, "minecraft:brown_mushroom"),
    RED_MUSHROOM(40, "minecraft:red_mushroom"),
    BLOCK_OF_GOLD(41, "minecraft:gold_block"),
    BLOCK_OF_IRON(42, "minecraft:iron_block"),
    DOUBLE_STONE_SLAB(43, "minecraft:double_stone_slab"),
    STONE_SLAB(44, "minecraft:stone_slab"),
    BRICKS(45, "minecraft:brick_block"),
    TNT(46, "minecraft:tnt"),
    BOOKSHELF(47, "minecraft:bookshelf"),
    MOSS_STONE(48, "minecraft:mossy_cobblestone"),
    OBSIDIAN(49, "minecraft:obsidian"),
    TORCH(50, "minecraft:torch"),
    FIRE(51, "minecraft:fire"),
    MONSTER_SPAWNER(52, "minecraft:mob_spawner"),
    OAK_WOOD_STAIRS(53, "minecraft:oak_stairs"),
    CHEST(54, "minecraft:chest"),
    REDSTONE_DUST(55, "minecraft:redstone_wire"),
    DIAMOND_ORE(56, "minecraft:diamond_ore"),
    BLOCK_OF_DIAMOND(57, "minecraft:diamond_block"),
    CRAFTING_TABLE(58, "minecraft:crafting_table"),
    CROPS(59, "minecraft:wheat"),
    FARMLAND(60, "minecraft:farmland"),
    FURNACE(61, "minecraft:furnace"),
    LIT_FURNACE(62, "minecraft:lit_furnace"),
    STANDING_SIGN(63, "minecraft:standing_sign"),
    OAK_DOOR(64, "minecraft:wooden_door"),
    LADDER(65, "minecraft:ladder"),
    RAIL(66, "minecraft:rail"),
    COBBLESTONE_STAIRS(67, "minecraft:stone_stairs"),
    WALL_SIGN(68, "minecraft:wall_sign"),
    LEVER(69, "minecraft:lever"),
    STONE_PRESSURE_PLATE(70, "minecraft:stone_pressure_plate"),
    IRON_DOOR(71, "minecraft:iron_door"),
    WOODEN_PRESSURE_PLATE(72, "minecraft:wooden_pressure_plate"),
    REDSTONE_ORE(73, "minecraft:redstone_ore"),
    LIT_REDSTONE_ORE(74, "minecraft:lit_redstone_ore"),
    REDSTONE_TORCH_OFF(75, "minecraft:unlit_redstone_torch"),
    REDSTONE_TORCH(76, "minecraft:redstone_torch"),
    STONE_BUTTON(77, "minecraft:stone_button"),
    SNOW_LAYER(78, "minecraft:snow_layer"),
    ICE(79, "minecraft:ice"),
    SNOW(80, "minecraft:snow"),
    CACTUS(81, "minecraft:cactus"),
    CLAY(82, "minecraft:clay"),
    SUGAR_CANE(83, "minecraft:reeds"),
    JUKEBOX(84, "minecraft:jukebox"),
    OAK_FENCE(85, "minecraft:fence"),
    PUMPKIN(86, "minecraft:pumpkin"),
    NETHERRACK(87, "minecraft:netherrack"),
    SOUL_SAND(88, "minecraft:soul_sand"),
    GLOWSTONE(89, "minecraft:glowstone"),
    PORTAL(90, "minecraft:portal"),
    JACK_O_LANTERN(91, "minecraft:lit_pumpkin"),
    CAKE(92, "minecraft:cake"),
    REDSTONE_REPEATER_OFF(93, "minecraft:unpowered_repeater"),
    REDSTONE_REPEATER(94, "minecraft:powered_repeater"),
    STAINED_GLASS(95, "minecraft:stained_glass"),
    WOODEN_TRAPDOOR(96, "minecraft:trapdoor"),
    STONE_MONSTER_EGG(97, "minecraft:monster_egg"),
    STONE_BRICKS(98, "minecraft:stonebrick"),
    BROWN_MUSHROOM_BLOCK(99, "minecraft:brown_mushroom_block"),
    RED_MUSHROOM_BLOCK(100, "minecraft:red_mushroom_block"),
    IRON_BARS(101, "minecraft:iron_bars"),
    GLASS_PANE(102, "minecraft:glass_pane"),
    MELON(103, "minecraft:melon_block"),
    PUMPKIN_STEM(104, "minecraft:pumpkin_stem"),
    MELON_STEM(105, "minecraft:melon_stem"),
    VINES(106, "minecraft:vine"),
    OAK_FENCE_GATE(107, "minecraft:fence_gate"),
    BRICK_STAIRS(108, "minecraft:brick_stairs"),
    STONE_BRICK_STAIRS(109, "minecraft:stone_brick_stairs"),
    MYCELIUM(110, "minecraft:mycelium"),
    LILY_PAD(111, "minecraft:waterlily"),
    NETHER_BRICK(112, "minecraft:nether_brick"),
    NETHER_BRICK_FENCE(113, "minecraft:nether_brick_fence"),
    NETHER_BRICK_STAIRS(114, "minecraft:nether_brick_stairs"),
    NETHER_WART(115, "minecraft:nether_wart"),
    ENCHANTMENT_TABLE(116, "minecraft:enchanting_table"),
    BREWING_STAND(117, "minecraft:brewing_stand"),
    CAULDRON(118, "minecraft:cauldron"),
    END_PORTAL(119, "minecraft:end_portal"),
    END_PORTAL_FRAME(120, "minecraft:end_portal_frame"),
    END_STONE(121, "minecraft:end_stone"),
    DRAGON_EGG(122, "minecraft:dragon_egg"),
    REDSTONE_LAMP(123, "minecraft:redstone_lamp"),
    REDSTONE_LAMP_ON(124, "minecraft:lit_redstone_lamp"),
    DOUBLE_WOOD_SLAB(125, "minecraft:double_wooden_slab"),
    WOOD_SLAB(126, "minecraft:wooden_slab"),
    COCOA(127, "minecraft:cocoa"),
    SANDSTONE_STAIRS(128, "minecraft:sandstone_stairs"),
    EMERALD_ORE(129, "minecraft:emerald_ore"),
    ENDER_CHEST(130, "minecraft:ender_chest"),
    TRIPWIRE_HOOK(131, "minecraft:tripwire_hook"),
    TRIPWIRE(132, "minecraft:tripwire"),
    BLOCK_OF_EMERALD(133, "minecraft:emerald_block"),
    SPRUCE_WOOD_STAIRS(134, "minecraft:spruce_stairs"),
    BIRCH_WOOD_STAIRS(135, "minecraft:birch_stairs"),
    JUNGLE_WOOD_STAIRS(136, "minecraft:jungle_stairs"),
    COMMAND_BLOCK(137, "minecraft:command_block"),
    BEACON(138, "minecraft:beacon"),
    COBBLESTONE_WALL(139, "minecraft:cobblestone_wall"),
    FLOWER_POT(140, "minecraft:flower_pot"),
    CARROTS(141, "minecraft:carrots"),
    POTATOES(142, "minecraft:potatoes"),
    WOODEN_BUTTON(143, "minecraft:wooden_button"),
    SKULL(144, "minecraft:skull"),
    ANVIL(145, "minecraft:anvil"),
    TRAPPED_CHEST(146, "minecraft:trapped_chest"),
    WEIGHTED_PRESSURE_PLATE_LIGHT(147, "minecraft:light_weighted_pressure_plate"),
    WEIGHTED_PRESSURE_PLATE_HEAVY(148, "minecraft:heavy_weighted_pressure_plate"),
    REDSTONE_COMPARATOR_OFF(149, "minecraft:unpowered_comparator"),
    REDSTONE_COMPARATOR(150, "minecraft:powered_comparator"),
    DAYLIGHT_SENSOR(151, "minecraft:daylight_detector"),
    REDSTONE_BLOCK(152, "minecraft:redstone_block"),
    NETHER_QUARTZ_ORE(153, "minecraft:quartz_ore"),
    HOPPER(154, "minecraft:hopper"),
    BLOCK_OF_QUARTZ(155, "minecraft:quartz_block"),
    QUARTZ_STAIRS(156, "minecraft:quartz_stairs"),
    ACTIVATOR_RAIL(157, "minecraft:activator_rail"),
    DROPPER(158, "minecraft:dropper"),
    STAINED_CLAY(159, "minecraft:stained_hardened_clay"),
    STAINED_GLASS_PANE(160, "minecraft:stained_glass_pane"),
    LEAVES_2(161, "minecraft:leaves2"),
    WOOD(162, "minecraft:log2"),
    ACACIA_WOOD_STAIRS(163, "minecraft:acacia_stairs"),
    DARK_OAK_WOOD_STAIRS(164, "minecraft:dark_oak_stairs"),
    SLIME_BLOCK(165, "minecraft:slime"),
    BARRIER(166, "minecraft:barrier"),
    IRON_TRAPDOOR(167, "minecraft:iron_trapdoor"),
    PRISMARINE(168, "minecraft:prismarine"),
    SEA_LANTERN(169, "minecraft:sea_lantern"),
    HAY_BALE(170, "minecraft:hay_block"),
    CARPET(171, "minecraft:carpet"),
    HARDENED_CLAY(172, "minecraft:hardened_clay"),
    BLOCK_OF_COAL(173, "minecraft:coal_block"),
    PACKED_ICE(174, "minecraft:packed_ice"),
    PLANT(175, "minecraft:double_plant"),
    STANDING_BANNER(176, "minecraft:standing_banner"),
    WALL_BANNER(177, "minecraft:wall_banner"),
    INVERTED_DAYLIGHT_SENSOR(178, "minecraft:daylight_detector_inverted"),
    RED_SANDSTONE(179, "minecraft:red_sandstone"),
    RED_SANDSTONE_STAIRS(180, "minecraft:red_sandstone_stairs"),
    DOUBLE_RED_SANDSTONE_SLAB(181, "minecraft:double_stone_slab2"),
    RED_SANDSTONE_SLAB(182, "minecraft:stone_slab2"),
    SPRUCE_FENCE_GATE(183, "minecraft:spruce_fence_gate"),
    BIRCH_FENCE_GATE(184, "minecraft:birch_fence_gate"),
    JUNGLE_FENCE_GATE(185, "minecraft:jungle_fence_gate"),
    DARK_OAK_FENCE_GATE(186, "minecraft:dark_oak_fence_gate"),
    ACACIA_FENCE_GATE(187, "minecraft:acacia_fence_gate"),
    SPRUCE_FENCE(188, "minecraft:spruce_fence"),
    BIRCH_FENCE(189, "minecraft:birch_fence"),
    JUNGLE_FENCE(190, "minecraft:jungle_fence"),
    DARK_OAK_FENCE(191, "minecraft:dark_oak_fence"),
    ACACIA_FENCE(192, "minecraft:acacia_fence"),
    SPRUCE_DOOR(193, "minecraft:spruce_door"),
    BIRCH_DOOR(194, "minecraft:birch_door"),
    JUNGLE_DOOR(195, "minecraft:jungle_door"),
    ACACIA_DOOR(196, "minecraft:acacia_door"),
    DARK_OAK_DOOR(197, "minecraft:dark_oak_door"),
    END_ROD(198, "minecraft:end_rod"),
    CHORUS_PLANT(199, "minecraft:chorus_plant"),
    CHORUS_FLOWER(200, "minecraft:chorus_flower"),
    PURPUR_BLOCK(201, "minecraft:purpur_block"),
    PURPUR_PILLAR(202, "minecraft:purpur_pillar"),
    PURPUR_STAIRS(203, "minecraft:purpur_stairs"),
    DOUBLE_PURPUR_SLAB(204, "minecraft:purpur_double_slab"),
    PURPUR_SLAB(205, "minecraft:purpur_slab"),
    END_STONE_BRICKS(206, "minecraft:end_bricks"),
    BEETROOTS(207, "minecraft:beetroots"),
    GRASS_PATH(208, "minecraft:grass_path"),
    END_GATEWAY(209, "minecraft:end_gateway"),
    REPEATING_COMMAND_BLOCK(210, "minecraft:repeating_command_block"),
    CHAIN_COMMAND_BLOCK(211, "minecraft:chain_command_block"),
    FROSTED_ICE(212, "minecraft:frosted_ice"),
    STRUCTURE_BLOCK(255, "minecraft:structure_block");

    private int legacyId;
    private String id;
    private short data;

    Block(int legacyId, String id) {
        this.legacyId = legacyId;
        this.id = id;
        this.data = 0;
    }

    Block(int legacyId, String id, short data) {
        this.legacyId = legacyId;
        this.id = id;
        this.data = data;
    }

    public static Block getBlock(int legacyId) {
        for(Block block : values()) if(block.getLegacyId() == legacyId) return block;
        return null;
    }

    public static Block getBlock(String id) {
        for(Block block : values()) if(block.getId().equalsIgnoreCase(id)) return block;
        return null;
    }

    public static Block getBlockByName(String name) {
        for(Block block : values()) if(block.name().equalsIgnoreCase(name)) return block;
        return null;
    }

    public static Block getBlockWithData(int id, short data) {
        for(Block block : values()) if(block.getLegacyId() == id && block.getData() == data) return block;
        return null;
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

    @Override
    public String toString() {
        return id + ":" + data;
    }

}
