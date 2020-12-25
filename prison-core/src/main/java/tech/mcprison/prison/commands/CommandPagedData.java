package tech.mcprison.prison.commands;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.output.ButtonComponent;
import tech.mcprison.prison.output.ButtonComponent.Style;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.RowComponent;

public class CommandPagedData {
	public static final int MAX_PAGE_SIZE = 10;

	private String pageCommand;
	private String pageCommandSuffix;
	
	private int curPage = 1;
	private boolean showAll = false;
	private boolean debug = false;
	
	private int pageSize = MAX_PAGE_SIZE;
	private int pages = 1;
	private int extraPages = 1;

	private int pageStart = 1;
	private int pageEnd = 1;

	public CommandPagedData( String pageCommand, int itemSize, int extraPages, String page) {
		this( pageCommand, itemSize, extraPages, page, MAX_PAGE_SIZE );
	}
	
	/**
	 * 
	 * @param command The command to run to show the same command, minus the page 
	 * 			number, but including the leading slash.
	 * @param itemSize The total number of items that need to be paged through
	 * @param extraPages Extra pages to be displayed with the the item pages. Example 
	 * 			would be the /prison info command with the mine details being on page 1.
	 * @param page The raw page String from the command interface. May contain invalid data.
	 */
	public CommandPagedData( String pageCommand, int itemSize, int extraPages, String page, int pageSize ) {
		super();
		
		this.pageCommand = pageCommand;
		
		if ( "all".equalsIgnoreCase( page ) ) {
			setShowAll( true );;
		} 
		else if ( "debug".equalsIgnoreCase( page ) ) {
			setShowAll( true );;
			setDebug( true );
		} 

		this.curPage = 1;
		this.pageSize = isShowAll() ? itemSize : pageSize;
		this.pages = isShowAll() ? 1 : (itemSize / getPageSize()) + 
										(itemSize % getPageSize() == 0 ? 0 : 1);
		this.extraPages = isShowAll() ? 0 : extraPages;
		
		
		try {
			curPage = Integer.parseInt(page);
		}
		catch ( NumberFormatException e ) {
			// Ignore: Not an integer, will use the default value.
		}

		curPage = ( isShowAll() || curPage < 1 ? 1 : 
					(curPage > (pages + getExtraPages()) ? (pages + getExtraPages()) : curPage ));
		
		// Just set to defaults for the pre-list pages:
		this.pageStart = 0;
		this.pageEnd = (itemSize < getPageSize() ? itemSize : getPageSize());
		
		if ( isShowAll() ) {
			this.pageEnd = itemSize;
			this.pages = 1;
		}
		else if ( (curPage - getExtraPages()) >= 1 ) {
			this.pageStart = (curPage - getExtraPages() - 1) * getPageSize();
			this.pageEnd = ((pageStart + getPageSize()) > itemSize ? itemSize : pageStart + getPageSize());
		}

//		Output.get().logInfo( "CommandPagedData: pageStart=" + pageStart + "  pageEnd=" + pageEnd);
	}
	
	public void generatePagedCommandFooter( ChatDisplay display ) {
		generatePagedCommandFooter( display, null );
	}
	
	public void generatePagedCommandFooter( ChatDisplay display, String message ) {
		// Need to construct a dynamic row of buttons. It may have no buttons, both, or
        // a combination of previous page or next page.  But it will always have a page
        // count between the two.
        RowComponent row = new RowComponent();
        if ( getCurPage() > 1 )
        {
        	row.addFancy( 
        			new ButtonComponent( "&e<-- Prev Page", '-', Style.NEGATIVE)
        			.runCommand(pageCommand + " " + (getCurPage() - 1) + 
        					(getPageCommandSuffix() == null ? "" : " " + getPageCommandSuffix()), 
        					"View the prior page of search results").getFancyMessage() );
        }
        row.addFancy( 
        		new FancyMessage(" &9< &3Page " + curPage + " of " + 
        						(getPages() + getExtraPages() ) + " &9> " +
        				(message == null || message.trim().length() == 0 ? "" : "  " + message ) + "&3"));
        
        if ( getCurPage() < (getPages() + getExtraPages()) )
        {
   			row.addFancy( 
        			new ButtonComponent( "&eNext Page -->", '+', Style.POSITIVE)
        			.runCommand(pageCommand + " " + (getCurPage() + 1) + 
        					(getPageCommandSuffix() == null ? "" : " " + getPageCommandSuffix()), 
        					"View the prior page of search results").getFancyMessage() );
        }
        display.addComponent( row );
	}



	public String getPageCommand() {
		return pageCommand;
	}
	public void setPageCommand( String pageCommand ) {
		this.pageCommand = pageCommand;
	}

	public String getPageCommandSuffix() {
		return pageCommandSuffix;
	}
	public void setPageCommandSuffix( String pageCommandSuffix ) {
		this.pageCommandSuffix = pageCommandSuffix;
	}

	public int getCurPage() {
		return curPage;
	}
	public void setCurPage( int curPage ) {
		this.curPage = curPage;
	}

	public boolean isShowAll() {
		return showAll;
	}
	public void setShowAll( boolean showAll ) {
		this.showAll = showAll;
	}

	public boolean isDebug() {
		return debug;
	}
	public void setDebug( boolean debug ) {
		this.debug = debug;
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize( int pageSize ) {
		this.pageSize = pageSize;
	}

	public int getPages() {
		return pages;
	}
	public void setPages( int pages ) {
		this.pages = pages;
	}

	public int getExtraPages() {
		return extraPages;
	}

	public void setExtraPages( int extraPages ) {
		this.extraPages = extraPages;
	}

	public int getPageStart() {
		return pageStart;
	}
	public void setPageStart( int pageStart ) {
		this.pageStart = pageStart;
	}

	public int getPageEnd() {
		return pageEnd;
	}
	public void setPageEnd( int pageEnd ) {
		this.pageEnd = pageEnd;
	}

	
	
}
