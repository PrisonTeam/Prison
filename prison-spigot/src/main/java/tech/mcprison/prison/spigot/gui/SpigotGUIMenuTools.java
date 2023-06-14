package tech.mcprison.prison.spigot.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.nbt.PrisonNBTUtil;
import tech.mcprison.prison.util.Text;

public class SpigotGUIMenuTools
{
	
	public static final String GUI_MENU_TOOLS_PAGE = "GUIPage";
	public static final String GUI_MENU_TOOLS_COMMAND = "Command: ";
	public static final String GUI_MENU_TOOLS_COMMANDS_BACK = "CommandsBack: ";

	public static final String GUI_MENU_TOOLS_NBT_ENABLED = "guiNBT";
	public static final String GUI_MENU_TOOLS_NBT_COMMAND = "guiNBTCommand";

	public static final String GUI_MENU_TOOLS_NBT_RANK_NAME = "guiNBTRankName";
	public static final String GUI_MENU_TOOLS_NBT_MINE_NAME = "guiNBTMineName";
	
	
	private static SpigotGUIMenuTools instance;
	
	private XMaterial menuStateOn1;
	private XMaterial menuStateOn2;
	
	private XMaterial menuStateOff1;
	private XMaterial menuStateOff2;

	private XMaterial menuBackground;
	
	private XMaterial menuGoBack;

	private boolean useDisabledButtons;
	
	
//	private String loreCommand;
	
	private SpigotGUIMenuTools() {
		super();
		
		this.useDisabledButtons = false;
		
		this.menuStateOn1 = XMaterial.LIME_STAINED_GLASS_PANE;
		this.menuStateOn2 = XMaterial.GREEN_STAINED_GLASS_PANE;
		
		this.menuStateOff1 = XMaterial.PINK_STAINED_GLASS_PANE;
		this.menuStateOff2 = XMaterial.RED_STAINED_GLASS_PANE;
		
		this.menuBackground = XMaterial.BLACK_STAINED_GLASS_PANE;
		
		this.menuGoBack = XMaterial.BARRIER;
		
//		this.menuStateOff1 = XMaterial.BLACK_STAINED_GLASS_PANE;
//		this.menuStateOff2 = XMaterial.GRAY_STAINED_GLASS_PANE;
		
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
		
		private String commandGoBack;
		
		private List<Button> buttons;
		
		/**
		 * 
		 * @param totalArraySize
		 * @param page
		 * @param commandToRun Command to run for paging.  The page number will be appended 
		 * 						to this command.  This command is ran as-is.
		 * @param commandGoBack Command to run to return to a prior menu, or another app. 
		 * 						No page number is added, so if page number is important to return
		 * 						to the same "view" then it must be included with this command.
		 * 						This command is ran as-is.
		 */
		public GUIMenuPageData( int totalArraySize, int page, 
						String commandToRun, String commandGoBack ) {
			this( totalArraySize, page, commandToRun );
			
			if ( commandGoBack != null ) {
				this.commandGoBack = commandGoBack;
			}
		}
		
		public GUIMenuPageData( int totalArraySize, int currentPage, String commandToRun ) {
			super();
			
			this.pageSize = 45;
			this.totalArraySize = totalArraySize;
			
			this.buttons = new ArrayList<>(9);
			for ( int i = 0; i < 9; i++ ) {
				buttons.add( null );
			}
	        
	        // Create the list of ranks for the selected page and setup the page details:
	        if ( (currentPage - 1) * pageSize > totalArraySize || currentPage < 1 ) {
	        	// too high of a page number was selected, so use page 0:
	        	currentPage = 1;
	        }
	        
	        this.page = currentPage;
	        
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
		
		public GUIMenuPageData clone() {
			GUIMenuPageData results = new GUIMenuPageData( totalArraySize, page, 
					getCommandToRun(), getCommandGoBack() );
			
			return results;
		}
		
		public int getMenuPosition( int position ) {
			return getDimension() - 10 + position;
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

		public String getCommandGoBack() {
			return commandGoBack;
		}
		public void setCommandGoBack( String commandGoBack ) {
			this.commandGoBack = commandGoBack;
		}

		public Button setButtonNextAvailable( Button button ) {
			
			for ( int i = 0; i < 9 && i <= buttons.size(); i++ ) {
				if ( buttons.get( i ) ==  null ) {
					
					int position = getMenuPosition( i + 1 );
					
					button.setPosition( position );
					buttons.set( i, button );
					break;
				}
			}
			
			return button;
		}
		public Button setButton( int position, Button button ) {
			if ( position < 1 ) {
				position = 1;
			}
			else if ( position > 9 ) {
				position = 9;
			}
			this.buttons.set( position - 1, button );
			
			return button;
		}
		public List<Button> getButtons() {
			return buttons;
		}
		public void setButtons( List<Button> buttons ) {
			this.buttons = buttons;
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
	
	public GUIMenuPageData createGUIPageObject( int totalArraySize, int page, 
					String commandToRun, String commandGoBack ) {
		return new GUIMenuPageData( totalArraySize, page, commandToRun, commandGoBack );
	}
	
	
    
    public boolean processGUIPage( Player p, String title, InventoryClickEvent e ) {
    	boolean isPageAction = false;
    	
    	ItemStack currentItem = e.getCurrentItem();
    	
    	if ( currentItem != null ) {
    		
//    		PrisonNBTUtil nbtUtil = new PrisonNBTUtil();
//    		NBTItem nbtItem = nbtUtil == null ? null : nbtUtil.getNBT(currentItem);
    		
    		// process if NBTs are enabled:
    		if ( PrisonNBTUtil.getNBTBoolean(currentItem, GUI_MENU_TOOLS_NBT_ENABLED) ) {
    			String command = PrisonNBTUtil.getNBTString(currentItem, GUI_MENU_TOOLS_NBT_COMMAND);
    			
    			if ( command != null ) {
    				isPageAction = true;
    				Bukkit.dispatchCommand(p, command);
    			}
    			
    		}
    		
    		// Otherwise process the old way where the command is in the lore:
    		else if ( currentItem != null && currentItem.hasItemMeta() ) {
    			
    			ItemMeta meta = currentItem.getItemMeta();
    			
    			if ( meta.hasLore() ) {
    				
    				String command = null;
    				
    				List<String> lores = meta.getLore();
    				
    				boolean isMenuToolsPage = false;
    				
    				for ( String lore : lores ) {
    					
    					if ( lore.contains( SpigotGUIMenuTools.GUI_MENU_TOOLS_PAGE ) ) {
    						isMenuToolsPage = true;
    					}
    					if ( lore.contains( SpigotGUIMenuTools.GUI_MENU_TOOLS_COMMAND ) ) {
    						command = Text.stripColor( lore ).replace( SpigotGUIMenuTools.GUI_MENU_TOOLS_COMMAND, "" ).trim();
    					}
    					
    					if ( command != null && command.equalsIgnoreCase( "close" ) ) {
    						p.closeInventory();
    					}
    				}
    				
    				if ( isMenuToolsPage && command != null ) {
    					isPageAction = true;
    					Bukkit.dispatchCommand(p, command);
    				}
    			}
    			
    		}
    		
    		

    		
    	}
    	
    	
    	return isPageAction;
	}
	
    private void addButtonNBT( Button guiButton, GUIMenuPageData pageData, int page ) {
    	
    	if ( pageData != null ) {
    		
//       		PrisonNBTUtil nbtUtil = new PrisonNBTUtil();
//    		NBTItem nbtItem = nbtUtil == null ? null : nbtUtil.getNBT( guiButton.getButtonItem() );
    		
    		// process if NBTs are enabled:
//    		if ( nbtItem != null ) 
    		{
    			String command = pageData.getCommandToRun() + 
    								( page <= 0 ? "" : " " + page);
//    			String command = GUI_MENU_TOOLS_COMMAND + pageData.getCommandToRun() + 
//    					( page <= 0 ? "" : " " + page);
    			
    			PrisonNBTUtil.setNBTBoolean( guiButton.getButtonItem(), GUI_MENU_TOOLS_NBT_ENABLED, true);
    			PrisonNBTUtil.setNBTString( guiButton.getButtonItem(), GUI_MENU_TOOLS_NBT_COMMAND, command );
    			    			
    		}
    	}
    	
    }
    
//	private ButtonLore createButtonLore( boolean enableCmd, // String message, 
//					GUIMenuPageData pageData, int page ) {
//		
//		ButtonLore buttonLore = new ButtonLore();
//		
////		if ( message != null ) {
////			buttonLore.addLineLoreDescription( message );
////		}
//		
//		
//		if ( enableCmd && pageData != null ) {
//		
//			buttonLore.addLineLoreAction( "&0" +
//					GUI_MENU_TOOLS_PAGE );
//			
//			if ( pageData.getCommandToRun() != null ) {
//				buttonLore.addLineLoreAction( "&0" +
//						GUI_MENU_TOOLS_COMMAND + pageData.getCommandToRun() + 
//						( page <= 0 ? "" : " " + page) );
//			}
//			
////			if ( pageData.getCommandsBack() != null ) {
////				buttonLore.addLineLoreDescription( "&0" +
////						GUI_MENU_TOOLS_COMMANDS_BACK + pageData.getCommandsBack() );
////			}
//			
//		}
//		return buttonLore;
//	}
	
	
	public Button createButtonBack( GUIMenuPageData pageData, int position ) {
		
		if ( pageData.getCommandGoBack() == null ) {
			
			return createButtonMenuBackground( pageData, position );
		}
		
		GUIMenuPageData newPageData = pageData.clone();
		newPageData.setCommandToRun( pageData.getCommandGoBack() );
		newPageData.setCommandGoBack( null );
		
		String message = pageData.getCommandGoBack().equalsIgnoreCase( "close" ) ?
				"Close" : "Go Back";
		
//		ButtonLore buttonLore = createButtonLore( true, newPageData, 0 );
		ButtonLore buttonLore = null;
		
		int pos = pageData.getMenuPosition( position );

		XMaterial xMat = menuGoBack;
		
		int pageNumber = 1;
		
		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, message );
		
		addButtonNBT( guiButton, newPageData, 0 );
		
		return pageData.setButton( position, guiButton );
	}
	
	public Button createButtonPageOne( GUIMenuPageData pageData, int position ) {
		
		boolean active = !useDisabledButtons || pageData.getPage() > 1;
		
		int pageNumber = 1;

		String message = "Page 1 of " + pageData.getPageLast();
		
//		ButtonLore buttonLore = createButtonLore( active, pageData, pageNumber );
		ButtonLore buttonLore = null;
		
		int pos = pageData.getMenuPosition( position );
		
		XMaterial xMat = !active ?
				menuStateOff2 : menuStateOn2;
		
		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, message );
		
		addButtonNBT( guiButton, pageData, pageNumber );

		return pageData.setButton( position, guiButton );
	}
	
	public Button createButtonPagePrior( GUIMenuPageData pageData, int position ) {
		
		boolean active = !useDisabledButtons ||pageData.getPagePrior() > 0;
		
		int pageNumber = pageData.getPagePrior() < 1 ?
				1 : pageData.getPagePrior();

		String message = "Page " + pageNumber + " of " + pageData.getPageLast();
	
//		ButtonLore buttonLore = createButtonLore( active, pageData, pageNumber );
		ButtonLore buttonLore = null;

		int pos = pageData.getMenuPosition( position );
		
		XMaterial xMat = !active ?
				menuStateOff1 : menuStateOn1;

		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore,  message );
		
		addButtonNBT( guiButton, pageData, pageNumber );

		return pageData.setButton( position, guiButton );
	}
	
	public Button createButtonPageCurrent( GUIMenuPageData pageData, int position ) {
		
		int pageNumber = pageData.getPage();

		String message = "Page " + pageNumber + " of " + pageData.getPageLast();
		
//		ButtonLore buttonLore = createButtonLore( false, pageData, pageNumber );
		ButtonLore buttonLore = null;
		
		int pos = pageData.getMenuPosition( position );
		
		XMaterial xMat = XMaterial.COMPASS;
		
		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, message );

		addButtonNBT( guiButton, pageData, pageNumber );
		
		return pageData.setButton( position, guiButton );
	}
	
	public Button createButtonPageNext( GUIMenuPageData pageData, int position ) {
		
		boolean active = !useDisabledButtons ||pageData.getPageNext() > 0;
		
		int pageNumber = pageData.getPageNext() < 1 ?
				pageData.getPageLast() : pageData.getPageNext();
		
		String message = "Page " + pageNumber + " of " + pageData.getPageLast();
	
//		ButtonLore buttonLore = createButtonLore( active, pageData, pageNumber );
		ButtonLore buttonLore = null;

		int pos = pageData.getMenuPosition( position );
		
		XMaterial xMat = !active ?
				menuStateOff1 : menuStateOn1;
		
		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, message );

		addButtonNBT( guiButton, pageData, pageNumber );
		
		return pageData.setButton( position, guiButton );
	}
	
	public Button createButtonPageLast( GUIMenuPageData pageData, int position ) {
		
		boolean active = !useDisabledButtons ||pageData.getPage() < pageData.getPageLast();
		
		int pageNumber = pageData.getPageLast() < 1 ?
				pageData.getPageLast() : pageData.getPageLast();
		
		String message = "Page " + pageNumber + " of " + pageData.getPageLast();
		
		ButtonLore buttonLore = null;
//		ButtonLore buttonLore = createButtonLore( active, pageData, pageNumber );
		
		int pos = pageData.getMenuPosition( position );
		
		XMaterial xMat = !active ?
				menuStateOff2 : menuStateOn2;
		
		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, message );

		addButtonNBT( guiButton, pageData, pageNumber );
		
		return pageData.setButton( position, guiButton );
	}
	
	public Button createButtonMenuBackground( GUIMenuPageData pageData, int position ) {
		
		ButtonLore buttonLore = null;
//		ButtonLore buttonLore = createButtonLore( false, null, 1 );

		int pos = pageData.getMenuPosition( position );
		
		XMaterial xMat = menuBackground;
		
		int pageNumber = 1;
		
		Button guiButton = new Button( pos, xMat, pageNumber, buttonLore, "--" );
		
		return guiButton;

		// DO NOT save the backgrounds so that way other buttons can automatically be added to 
		// the tool bar:
		//return pageData.setButton( position, guiButton );;
	}
	
	
	public void addMenuPageButtonsStandard( PrisonGUI gui, GUIMenuPageData pageData ) {
		
		gui.addButton( createButtonMenuBackground( pageData, 1 ) );
		gui.addButton( createButtonMenuBackground( pageData, 2 ) );
		gui.addButton( createButtonMenuBackground( pageData, 3 ) );

		gui.addButton( createButtonPagePrior( pageData, 4 ) );
		gui.addButton( createButtonPageCurrent( pageData, 5 ) );
		gui.addButton( createButtonPageNext( pageData, 6 ) );
		
		gui.addButton( createButtonMenuBackground( pageData, 7 ) );
		gui.addButton( createButtonMenuBackground( pageData, 8 ) );
		gui.addButton( createButtonBack( pageData, 9 ) );

	}
	
	public void addMenuPageButtonsWideLayout( PrisonGUI gui, GUIMenuPageData pageData ) {
		
		gui.addButton( createButtonPageOne( pageData, 1 ) );
		gui.addButton( createButtonPagePrior( pageData, 2 ) );
		
		gui.addButton( createButtonMenuBackground( pageData, 3 ) );
//		gui.addButton( createButtonMenuBackground( pageData, 4 ) );
		
		gui.addButton( createButtonBack( pageData, 4 ) );
		gui.addButton( createButtonPageCurrent( pageData, 5 ) );
		
		gui.addButton( createButtonMenuBackground( pageData, 6 ) );
		gui.addButton( createButtonMenuBackground( pageData, 7 ) );
		
		gui.addButton( createButtonPageNext( pageData, 8 ) );
		gui.addButton( createButtonPageLast( pageData, 9 ) );
	}

	
	
	public void addMenuPageButtonsNoPaging( PrisonGUI gui, GUIMenuPageData pageData ) {
		
		gui.addButton( createButtonMenuBackground( pageData, 1 ) );
		gui.addButton( createButtonMenuBackground( pageData, 2 ) );
		gui.addButton( createButtonMenuBackground( pageData, 3 ) );
		gui.addButton( createButtonMenuBackground( pageData, 4 ) );

		gui.addButton( createButtonPageCurrent( pageData, 5 ) );
		
		gui.addButton( createButtonMenuBackground( pageData, 6 ) );
		gui.addButton( createButtonMenuBackground( pageData, 7 ) );
		gui.addButton( createButtonMenuBackground( pageData, 8 ) );
		gui.addButton( createButtonBack( pageData, 9 ) );

	}
}


