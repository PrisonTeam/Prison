package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;

public class PrisonSortableResults {
	private List<Mine> include = new ArrayList<>();
	private List<Mine> exclude = new ArrayList<>();
	
	private MineSortOrder sortOrder;
	
	public PrisonSortableResults( MineSortOrder sortOrder ) {
		this.sortOrder = sortOrder;
	}
	
	/**
	 * <p>This function, based upon the provided MineSortOrder, returns
	 * the correct list of mines either included, or excluded.
	 * </p>
	 * 
	 * @return
	 */
	public List<Mine> getSortedList() {
		return sortOrder.isExcluded() ? 
				getExclude() :
				getInclude();
	}
	public List<Mine> getSortedSuppressedList() {
		return !sortOrder.isExcluded() ? 
				getExclude() :
					getInclude();
	}
	
	
	public String getSuppressedListSortTypes() {
		StringBuilder sb = new StringBuilder();
		
		for ( MineSortOrder so : MineSortOrder.values() ) {
			if ( so != MineSortOrder.invalid && getSortOrder().isExcluded() != so.isExcluded() ) {
				if ( sb.length() > 0 ) {
					sb.append( " " );
				}
				sb.append( so.name() );
			}
		}
		
		return sb.toString();
	}
	
	
	public List<Mine> getInclude() {
		return include;
	}
	public void setInclude( List<Mine> include ) {
		this.include = include;
	}
	
	public List<Mine> getExclude() {
		return exclude;
	}
	public void setExclude( List<Mine> exclude ) {
		this.exclude = exclude;
	}

	public MineSortOrder getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder( MineSortOrder sortOrder ) {
		this.sortOrder = sortOrder;
	}
}
