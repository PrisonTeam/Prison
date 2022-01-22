package tech.mcprison.prison.cache;

import java.util.Comparator;

public class TopNTokensComparator
	implements Comparator<TopNStatsData>
{

	@Override
	public int compare( TopNStatsData o1, TopNStatsData o2 ) {
		return Long.compare( o1.getCurrentTokens(), o2.getCurrentTokens() );
	}
}
