package tech.mcprison.prison.cache;

import java.util.Comparator;

public class TopNBlocksComparator
	implements Comparator<TopNStatsData>
{

	@Override
	public int compare( TopNStatsData o1, TopNStatsData o2 ) {
		return Long.compare( o1.getTotalBlocks(), o2.getTotalBlocks() );
	}

}
