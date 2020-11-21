package tech.mcprison.prison.troubleshoot.inbuilt;

/**
 * Inbuilt troubleshooter to scan the 'items.csv' file to ensure it's valid.
 *
 * @author Faizaan A. Datoo
 */
public class ItemTroubleshooter 
//	extends Troubleshooter 
	{

//    public ItemTroubleshooter() {
//        super("item_scan", "Run this if you have trouble with the items.csv file.");
//    }
//
//    @Override public TroubleshootResult invoke(CommandSender invoker) {
//
//        // Let's do our own test of initializing the ItemManager.
//        try {
//            ItemManager ourManager = new ItemManager();
//            ourManager.getItems();
//        } catch (Exception e) {
//            // OK, so something's wrong
//            // Let's try deleting the file and telling the user to relaunch.
//
//            File itemsCsv = new File(PrisonAPI.getPluginDirectory(), "items.csv");
//            boolean deleted = itemsCsv.delete();
//            if (deleted) {
//                return new TroubleshootResult(TroubleshootResult.Result.USER_ACTION,
//                    "We've found a problem with your items.csv file. We deleted it so that a new and non-corrupted one is generated. Please restart your server for the changes to take effect.");
//            } else {
//                // We can only hot delete on *NIX systems.
//                return new TroubleshootResult(TroubleshootResult.Result.FAILURE,
//                    "We've found a problem with your items.csv file. We tried deleting it, but it could not be successfully deleted. Please stop your server, delete '/plugins/Prison/items.csv', and start your server again.");
//            }
//        }
//
//        // Nothing is wrong.
//        return new TroubleshootResult(TroubleshootResult.Result.SUCCESS,
//            "No problems were found with your item manager or items.csv file.");
//    }
}
