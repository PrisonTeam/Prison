package tech.mcprison.prison.output;

import tech.mcprison.prison.internal.CommandSender;

/**
 * <p>This adds a hyperLink data element to the ChatDisplay.  
 * A hyperLink data element consist of 2 or 3 words, and no actual linkage. The hyperlinks
 * will be auto generated when the support document is generated.
 * </p>
 * 
 * <p>This information applies to this data type, which is also part of the constructor
 * with parameters.
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
 * 
 * 
 * @author Blue
 *
 */
public class SupportHyperLinkComponent
	extends DisplayComponent {

    protected String text;

    public SupportHyperLinkComponent(String text, Object... args) {
        this.text = Output.stringFormat(text, args);
    }

    @Override public String text() {
        return text;
    }

    @Override public void send(CommandSender sender) {
        
    	// Do not send this message to the player... it is only for support documents:
    	//sender.sendMessage(text());
    }
}
