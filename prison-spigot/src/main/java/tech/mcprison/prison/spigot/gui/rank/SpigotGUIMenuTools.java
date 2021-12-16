package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;

public class SpigotGUIMenuTools
{
	
	public static final String GUI_MENU_TOOLS_PAGE = "GUIPage";
	public static final String GUI_MENU_TOOLS_COMMAND = "Command: ";
	
	private static SpigotGUIMenuTools instance;
	
	private XMaterial menuStateOn1;
	private XMaterial menuStateOn2;
	
	private XMaterial menuStateOff1;
	private XMaterial menuStateOff2;
	
//	private String loreCommand;
	
	private SpigotGUIMenuTools() {
		super();
		
		
		this.menuStateOn1 = XMaterial.LIME_STAINED_GLASS_PANE;
		this.menuStateOn2 = XMaterial.GREEN_STAINED_GLASS_PANE;
		
		this.menuStateOff1 = XMaterial.PINK_STAINED_GLASS_PANE;
		this.menuStateOff2 = XMaterial.RED_STAINED_GLASS_PANE;
		
//		this.menuStateOff1 = XMaterial.WHITE_STAINED_GLASS_PANE;
//		this.menuStateOff2 = XMaterial.BLACK_STAINED_GLASS_PANE;
		
	}

	public static SpigotGUIMenuTools getInstance() {
		if ( instance == null ) {
			synchronized ( SpigotGUIMenuTools.class )
			{
				if ( instance == null ) {

					instance = new SpigotGUIMenuTools();
					instance.internalInitalize();
				}
			}
		}
		return instance;
	}
	
	

	public class GUIMenuPageData {
		
		private int totalArraySize;
		
		private int pageSize = 45;
		private int page;
		
		private int dimension;
		
		private int posStart;
		private int posEnd;
		
		private int pagePrior = -1;
		private int pageNext = -1;

		private int pageLast = 1;
		
		private String commandToRun;
		
		public GUIMenuPageData( int totalArraySize, int page, String commandToRun ) {
			super();
			
			this.pageSize = 45;
			this.totalArraySize = totalArraySize;
			this.page = page;
			
	        
	        // Create the list of ranks for the selected page and setup the page details:
	        if ( (page - 1) * pageSize > totalArraySize || page < 1 ) {
	        	// too high of a page number was selected, so use page 0:
	        	page = 1;
	        }
	        
	        
	        pageLast = (totalArraySize / pageSize) +
	        				(totalArraySize % pageSize == 0 ? 0 : 1);
	        

	        posStart = (page - 1) * pageSize;
	        posEnd = posStart + pageSize;
//	        posEnd = posStart + pageSize - 1;
	        
	        if ( posEnd > totalArraySize ) {
	        	posEnd = totalArraySize;
	        }
	        pagePrior = page <= 1 ? -1 : page - 1;
	        pageNext = posEnd == totalArraySize ? -1 : page + 1;
	        
	        
	        // Create the inventory and set up the owner, dimensions or number of slots, and title
	        this.dimension = (int) (Math.ceil(( posEnd - posStart ) / 9d) * 9) + 9;
	        
	        if ( dimension > 54 ) {
	        	dimension = 54;
	        }
	        
	        this.commandToRun = commandToRun;
			
		}
		
		public int getTotalArraySize() {
			return totalArraySize;
		}
		public void setTotalArraySize( int totalArraySize ) {
			this.totalArraySize = totalArraySize;
		}

		public int getPageSize() {
			return pageSize;
		}
		public void setPageSize( int pageSize ) {
			this.pageSize = pageSize;
		}

		public int getPage() {
			return page;
		}
		public void setPage( int page ) {
			this.page = page;
		}

		public int getDimension() {
			return dimension;
		}
		public void setDimension( int dimension ) {
			this.dimension = dimension;
		}

		public int getPosStart() {
			return posStart;
		}
		public void setPosStart( int posStart ) {
			this.posStart = posStart;
		}

		public int getPosEnd() {
			return posEnd;
		}
		public void setPosEnd( int posEnd ) {
			this.posEnd = posEnd;
		}

		public int getPagePrior() {
			return pagePrior;
		}
		public void setPagePrior( int pagePrior ) {
			this.pagePrior = pagePrior;
		}

		public int getPageNext() {
			return pageNext;
		}
		public void setPageNext( int pageNext ) {
			this.pageNext = pageNext;
		}

		public int getPageLast() {
			return pageLast;
		}
		public void setPageLast( int pageLast ) {
			this.pageLast = pageLast;
		}

		public String getCommandToRun() {
			return commandToRun;
		}
		public void setCommandToRun( String commandToRun ) {
			this.commandToRun = commandToRun;
		}
	}
	
	/**
	 * <p>This sets up version specific settings.
	 * </p>
	 * 
	 * <p>Glass panes are not working 
	 */
	private void internalInitalize() {
		
		
//		this.loreCommand = SpigotPrison.getInstance().getMessagesConfig()
//									.getString(MessagesConfig.StringID.spigot_gui_lore_command);
		
		
		
//		String bukkitVersion =  new BluesSpigetSemVerComparator().getBukkitVersion();
//		if ( bukkitVersion != null ) {
//			
//			BluesSemanticVersionData svData = new BluesSemanticVersionData( bukkitVersion );
//			
//			// If Spigot version is < 1.9.0... ie 1.8...
//			if ( svData.compareTo( new BluesSemanticVersionData( "1.9.0" ) ) < 0 ) {
//				
////				// glass panes do not work well in 1.8 so setup alternatives:
////				
////				this.menuStateOff = XMaterial.PINK_STAINED_GLASS;
////				this.menuStateOn = XMaterial.GREEN_STAINED_GLASS;
//
//			}
//		}

		
	}
	
	
	
	public GUIMenuPageData createGUIPageObject( int totalArraySize, int page, String commandToRun ) {
		return new GUIMenuPageData( totalArraySize, page, commandToRun );
	}
	
	
//	private ButtonLore createButtonLore( boolean enabled, String message, GUIMenuPageData pageData, int page ) {
//		
//		ButtonLore buttonLore = new ButtonLore();
//		buttonLore.addLineLoreDescription( message );
//		
//		if ( enabled ) {
//			buttonLore.addLineLoreDescription( 
//					GUI_MENU_TOOLS_COMMAND + pageData.getCommandToRun() + " " + 1 );
//		}
//		return buttonLore;
//	}
//	
//	private Button createButton( ) {
//		
//		return null;
//	}
	
	public Button createButtonPageOne( GUIMenuPageData pageData ) {
		
		boolean hasPage = pageData.getPage() > 1;
		
		String message = !hasPage ?
				"Already on page 1" : "Page 1";
		
//		ButtonLore buttonLore = createButtonLore( hasPage, message, pageData, 1 );
		
		ButtonLore buttonLore = new ButtonLore();
		buttonLore.addLineLoreDescription( message );
		
		if ( hasPage ) {
			buttonLore.addLineLoreDescription( 
					GUI_MENU_TOOLS_COMMAND + pageData.getCommandToRun() + " " + 1 );
		}
		
		int pos = pageData.getDimension() - 9;
		
		XMaterial xMat = !hasPage ?
				menuStateOff1 : menuStateOn1;
		
		int pageNumber = 1;
		
		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, 
				(hasPage ? GUI_MENU_TOOLS_PAGE : message) );
		
		return guiButton;
	}
	
	public Button createButtonPagePrior( GUIMenuPageData pageData ) {
		
		boolean hasPage = pageData.getPagePrior() > 0;

		String message = !hasPage ?
				"No prior page" : "Page " + pageData.getPagePrior();
		
		ButtonLore buttonLore = new ButtonLore();
		buttonLore.addLineLoreDescription( message );

		if ( hasPage ) {
			buttonLore.addLineLoreDescription( 
					GUI_MENU_TOOLS_COMMAND + pageData.getCommandToRun() + " " + pageData.getPagePrior() );
		}
		
		int pos = pageData.getDimension() - 8;
		
		XMaterial xMat = !hasPage ?
				menuStateOff2 : menuStateOn2;
		
		int pageNumber = !hasPage ?
				1 : pageData.getPagePrior();

		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, 
				(hasPage ? GUI_MENU_TOOLS_PAGE : message) );
		
		return guiButton;
	}
	
	public Button createButtonPageNext( GUIMenuPageData pageData ) {
		
		boolean hasPage = pageData.getPageNext() > 0;
		
		String message = !hasPage ?
				"No next page" : "Page " + pageData.getPageNext();
		
		ButtonLore buttonLore = new ButtonLore();
		buttonLore.addLineLoreDescription( message );
		
		if ( hasPage ) {
			buttonLore.addLineLoreDescription( 
					GUI_MENU_TOOLS_COMMAND + pageData.getCommandToRun() + " " + pageData.getPageNext() );
		}
		
		int pos = pageData.getDimension() - 2;
		
		XMaterial xMat = !hasPage ?
				menuStateOff2 : menuStateOn2;
		
		int pageNumber = !hasPage ?
				1 : pageData.getPageNext();

		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, 
				(hasPage ? GUI_MENU_TOOLS_PAGE : message) );
		
		//guiButton.
		
		return guiButton;
	}
	
	public Button createButtonPageLast( GUIMenuPageData pageData ) {
		
		boolean hasPage = pageData.getPage() < pageData.getPageLast();
		
		String message = !hasPage ?
				"Already on last page" : "Page " + pageData.getPageNext();
		
		ButtonLore buttonLore = new ButtonLore();
		buttonLore.addLineLoreDescription( message );
		
		if ( hasPage ) {
			buttonLore.addLineLoreDescription( 
					GUI_MENU_TOOLS_COMMAND + pageData.getCommandToRun() + " " + pageData.getPageLast() );
		}
		
		int pos = pageData.getDimension() - 1;
		
		XMaterial xMat = !hasPage ?
				menuStateOff1 : menuStateOn1;
		
		int pageNumber = !hasPage ?
				1 : pageData.getPageLast();
		
		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, 
				(hasPage ? GUI_MENU_TOOLS_PAGE : message) );
		
		//guiButton.
		
		return guiButton;
	}
	
	
	public void addMenuPageButtons( PrisonGUI gui, GUIMenuPageData pageData ) {
		
		gui.addButton( createButtonPageOne( pageData ) );
		gui.addButton( createButtonPagePrior( pageData ) );

		gui.addButton( createButtonPageNext( pageData ) );
		gui.addButton( createButtonPageLast( pageData ) );
	}


	
	
	
}


