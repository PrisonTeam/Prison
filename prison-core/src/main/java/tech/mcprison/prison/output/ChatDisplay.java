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

package tech.mcprison.prison.output;

import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.util.Text;

import java.util.LinkedList;

/**
 * A chat display is a utility for creating uniform and good-looking
 * in-chat menus and data displays.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ChatDisplay {

  /*
   * Fields & Constants
   */

    private String title;
    private LinkedList<DisplayComponent> displayComponents;
    
    private boolean showTitle = true;

  /*
   * Constructor
   */

    public ChatDisplay(String title) {
        this.title = Text.titleize(title);
        this.displayComponents = new LinkedList<>();
    }

  /*
   * Methods
   */

    public ChatDisplay addComponent(DisplayComponent component) {
        component.setDisplay(this);
        displayComponents.add(component);
        return this;
    }

    public ChatDisplay addText(String text, Object... args) {
        addComponent(new TextComponent(text, args));
        return this;
    }

    public ChatDisplay addEmptyLine() {
        addComponent(new TextComponent(""));
        return this;
    }

    public void send(CommandSender sender) {
    	if ( isShowTitle() ) {
    		sender.sendMessage(title);
    	}
    	
        for (DisplayComponent component : displayComponents) {
            component.send(sender);
        }
    }
    
    public void toLog(LogLevel logLevel) {
    	if ( isShowTitle() ) {
    		Output.get().log( title, logLevel );
    	}
    	
        for (DisplayComponent component : displayComponents) {
        	Output.get().log( component.text(), logLevel );
        }
    }
    
    public void sendtoOutputLogInfo() {
    	if ( isShowTitle() ) {
    		Output.get().logInfo( title );
    	}
    	
        for (DisplayComponent component : displayComponents) {
        	Output.get().logInfo( component.text() );
        }
    }

    public StringBuilder toStringBuilder() {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( isShowTitle() ) {
    		sb.append( title ).append( "\n" );
    	}
    	
        for (DisplayComponent component : displayComponents) {
        	sb.append( component.text() ).append( "\n" );
        }
        
        return sb;
    }
    
    public StringBuilder toStringBuilderEscaped() {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( isShowTitle() ) {
    		sb.append( title ).append( "\\n" );
    	}
    	
    	for (DisplayComponent component : displayComponents) {
    		sb.append( component.text() ).append( "\\n" );
    	}
    	
    	return sb;
    }

	public void addChatDisplay( ChatDisplay cDisp )
	{

		addComponent(new TextComponent(cDisp.getTitle()));
		
    	for (DisplayComponent component : cDisp.getDisplayComponents() ) {
    		addComponent( component );
    	}
	}

	protected String getTitle() {
		return title;
	}

	protected LinkedList<DisplayComponent> getDisplayComponents() {
		return displayComponents;
	}

	public boolean isShowTitle() {
		return showTitle;
	}
	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	/**
	 * <p>This adds a hyperLink data element to the ChatDisplay.  
	 * A hyperLink data element consist of 2 or 3 words, and no actual linkage. The hyperlinks
	 * will be auto generated when the support document is generated.
	 * </p>
	 * 
	 * <p>The proper format is for the first word to identify the major grouping, such as 
	 * `Mine`, `Rank`, `Ladder`, or `Commands`. 
	 * </p>
	 * 
	 * <p>The second word must be the name of the mine, rank, or ladder.  For mines, ranks, and
	 * ladders, the second word can also be a type of groupings of such mines, rnaks, and ladders,
	 * such as `TOC`, `List`, etc...
	 * For commands, the second word must be the name of the command's config file.
	 * </p>
	 * 
	 * <p>The third word is used to qualify the type of item, for example for config files, the 
	 * third world would always be `File` since it carries a special format where the contents 
	 * do not have the color mappings rendered.  Also, for some major groupings, like `Ladder List`,
	 * it also needs a qualifying ladder name as the third word since there are multiple 
	 * ladders, each is a group of ranks, but there is also just a list of
	 * all ladders, as found in `Ladder List`.  For example, `Ladder List default`, or 
	 * `Ladder List donors`, with also `Ladder List`.
	 * </p>
	 * 
	 * <p>This structure is used to auto generate html tag IDs and also their related links
	 * to other content.
	 * </p> 
	 * 
	 * <p>For example, let's assume we have one entry for `Rank B`.  This will crate an ID for that
	 * section of something like `<span id="rank_b"></span>`  Then it will also create hyperlinks
	 * to `(TOC)` `(Rank List)` `(Rank B File)`.  These hyperlinks will then generat it's own ID, with 
	 * hyperlinks to the other related content.  This will allow fast and simple way to 
	 * jump to the various components of data that is related to this concerpt of Rank B.
	 * </p>
	 * 
	 * <p>The data that is passed to this function should just be plain names.  But the actual 
	 * content is wrapped with double pipes.  So if the data `Rank B` is passed to this function,
	 * it would be stored as `||Rank B||`.  This notation is used to identify that it is a 
	 * support hyperlink, so all supporting hyperlinks can be generated from this data.
	 * </p>
	 * 
	 * <p>This data will never be shown to players in game, or shown within the console.
	 * This is only included in the Prison Support documents to better utilize the
	 * support teams time and talents.
	 * </p>
	 * 
	 * @param linkData
	 */
	public void addSupportHyperLinkData( String linkData, Object... args ) {
		String codedLinkData = "||" + linkData + "||";
		SupportHyperLinkComponent shlc = new SupportHyperLinkComponent( codedLinkData, args );
		addComponent( shlc );
	}
    
}
