package tech.mcprison.prison.mines;

import tech.mcprison.prison.convert.ConversionAgent;
import tech.mcprison.prison.convert.ConversionResult;
import tech.mcprison.prison.output.Output;

/**
 * @author Faizaan A. Datoo
 */
public class MinesConversionAgent 
implements ConversionAgent 
{

    @Override
    public ConversionResult convert() {
    	
    	Output.get().logWarn( "&7This version of prison is unable to convert older versions " +
    			"to this release.  Please upgrade first to Prision v3.1.1, then " +
    			"Prison v3.2.1, then Prison v3.2.11. Once prison is upgraded to " +
    			"version v3.2.11 then it should be able to convert automatically to " +
    			"Prison v3.3.0. When upgrading to all of these versions, all that is needed " +
    			"is to just install the newer Prison jar file, and then start the server. " +
    			"The configs will be converted for you.  Then 'stop' the server and " +
    			"continue to the next version.  You may be able to skip v3.2.1, but it " +
    			"may be safest to run that version for the incremental adjustments. " +
    			"It also would not be a bad idea to may a copy of the prison plugin " +
    			"directory betwen version upgrades: plugins/Prison/. " );
    	
    	Output.get().logWarn( "&7NOTE: This version of prison cannot process the older block types that " +
    			"were used in the older versions of prison." );
    	
//        File oldFolder = new File(PrisonAPI.getPluginDirectory().getParent(), "Prison.old");
//        File minesFolder = new File(oldFolder, "mines");
//
//        File alreadyConverted = new File(minesFolder, ".converted");
//        if (alreadyConverted.exists()) {
//            return ConversionResult.failure(getName(),
//                "Already converted. Delete the '/plugins/Prison.old/mines' folder.");
//        }
//
//        String[] jsonFiles = minesFolder.list((dir, name) -> name.endsWith(".json"));
//
//        try {
//
//            // -----------
//            // JSON
//            // -----------
//
//            if (jsonFiles != null) {
//                for (String jsonFile : jsonFiles) {
//                    File jsonFileObj = new File(minesFolder, jsonFile);
//                    String json = new String(Files.readAllBytes(jsonFileObj.toPath()));
//                    JsonObject obj = (JsonObject) JsonParser.parseString(json);
//
//                    String name = obj.getAsJsonPrimitive("name").getAsString();
//                    String world = obj.getAsJsonPrimitive("world").getAsString();
//                    double minX = obj.getAsJsonPrimitive("minX").getAsInt();
//                    double minY = obj.getAsJsonPrimitive("minY").getAsInt();
//                    double minZ = obj.getAsJsonPrimitive("minZ").getAsInt();
//                    double maxX = obj.getAsJsonPrimitive("maxX").getAsInt();
//                    double maxY = obj.getAsJsonPrimitive("maxY").getAsInt();
//                    double maxZ = obj.getAsJsonPrimitive("maxZ").getAsInt();
//
//                    Optional<tech.mcprison.prison.internal.World> prisonWorld =
//                        Prison.get().getPlatform().getWorld(world);
//                    if (!prisonWorld.isPresent()) {
//                        Output.get().logWarn(String.format(
//                            "Can't convert mine %s because its world %s doesn't exist anymore.",
//                            name, world));
//                        break; // Skip it, its world didn't exist.
//                    }
//
//                    Bounds bounds = new Bounds(new Location(prisonWorld.get(), minX, minY, minZ),
//                        new Location(prisonWorld.get(), maxX, maxY, maxZ));
//
//                    HashMap<BlockType, Integer> blocks = new HashMap<>();
//                    for (Map.Entry<String, JsonElement> blockEntry : obj.getAsJsonObject("blocks")
//                        .entrySet()) {
//                        String[] blockParts = blockEntry.getKey().split(":");
//                        BlockType type = BlockType.getBlock(Integer.parseInt(blockParts[0]),
//                            Short.parseShort(blockParts[1]));
//
//                        // Prison 2 stores chances in values < 1, whereas Prison 3 does it < 100
//                        int chance = (int) ((blockEntry.getValue().getAsDouble()) * 100);
//
//                        blocks.put(type, chance);
//                    }
//
//                    Mine ourMine = new Mine();
//                    ourMine.setName(name);
//                    ourMine.setBounds(bounds);
//                    ourMine.setBlocks(blocks);
//
//                    if (PrisonMines.getInstance().getMines().contains(ourMine)) {
//                        break;
//                    }
//
//                    PrisonMines.getInstance().getMines().add(ourMine);
//                }
//
//                PrisonMines.getInstance().getMineManager().saveMines();
//                alreadyConverted.createNewFile();
//                return new ConversionResult(getName(), ConversionResult.Status.Success,
//                    "Converted " + jsonFiles.length + " mines.");
//            } else {
//                alreadyConverted.createNewFile();
//                return new ConversionResult(getName(), ConversionResult.Status.Success,
//                    "Converted 0 mines.");
//            }
//        } catch (IOException e) {
//            PrisonMines.getInstance().getErrorManager().throwError(
//                new Error("Encountered an error while converting mines.")
//                    .appendStackTrace("while loading mines", e));
//            return new ConversionResult(getName(), ConversionResult.Status.Failure,
//                "IOException, check console for details");
//        }
    	
    	 return ConversionResult.failure(getName(),
               "This version of prison cannot perform conversion upgrades. It skips too " +
               "many versions. See WARNING in console, and install the suggested older " +
               "versions of Prison to ensure all of the old data is updated correctly, " +
               "with no losses.");
    }

    @Override
    public String getName() {
        return "Mines";
    }
}
