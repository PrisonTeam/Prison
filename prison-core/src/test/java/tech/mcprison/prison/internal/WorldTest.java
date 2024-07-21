package tech.mcprison.prison.internal;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Location;

public class WorldTest implements World {


	@Override
	public String getName() {
		return "Test world - does not exist";
	}

	@Override
	public List<Player> getPlayers() {
		return new ArrayList<>();
	}

	@Override
	public List<Entity> getEntities() {
		return new ArrayList<>();
	}

	@Override
	public Block getBlockAt(Location location) {
		return PrisonBlock.AIR;
	}

	@Override
	public Block getBlockAt(Location location, boolean containsCustomBlocks) {
		return PrisonBlock.AIR;
	}

	@Override
	public void setBlock(PrisonBlock block, int x, int y, int z) {
	}

	@Override
	public void setBlockAsync(PrisonBlock prisonBlock, Location location) {
	}

	@Override
	public void setBlocksSynchronously(List<MineTargetPrisonBlock> tBlocks, MineResetType resetType,
			PrisonStatsElapsedTimeNanos nanos) {
	}

	@Override
	public Entity spawnEntity(Location loc, EntityType entityType) {
		return null;
	}

	@Override
	public ArmorStand spawnArmorStand(Location location) {
		return null;
	}

	@Override
	public ArmorStand spawnArmorStand(Location location, String itemType, String customName, String nbtKey,
			String nbtValue) {
		return null;
	}

	@Override
	public int getMaxHeight() {
		return 255;
	}

}
