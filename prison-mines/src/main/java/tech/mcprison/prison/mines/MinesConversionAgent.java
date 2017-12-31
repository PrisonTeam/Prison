package tech.mcprison.prison.mines;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.convert.ConversionAgent;
import tech.mcprison.prison.convert.ConversionResult;
import tech.mcprison.prison.error.Error;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class MinesConversionAgent implements ConversionAgent {

    @Override public ConversionResult convert() {
        File oldFolder = new File(PrisonAPI.getPluginDirectory().getParent(), "Prison.old");
        File minesFolder = new File(oldFolder, "mines");

        File alreadyConverted = new File(minesFolder, ".converted");
        if (alreadyConverted.exists()) {
            return ConversionResult.failure(getName(),
                "Already converted. Delete the '/plugins/Prison.old/mines' folder.");
        }

        String[] jsonFiles = minesFolder.list((dir, name) -> name.endsWith(".json"));

        try {

            // -----------
            // JSON
            // -----------

            if (jsonFiles != null) {
                for (String jsonFile : jsonFiles) {
                    File jsonFileObj = new File(minesFolder, jsonFile);
                    JsonParser parser = new JsonParser();
                    String json = new String(Files.readAllBytes(jsonFileObj.toPath()));
                    JsonObject obj = (JsonObject) parser.parse(json);

                    String name = obj.getAsJsonPrimitive("name").getAsString();
                    String world = obj.getAsJsonPrimitive("world").getAsString();
                    double minX = obj.getAsJsonPrimitive("minX").getAsInt();
                    double minY = obj.getAsJsonPrimitive("minY").getAsInt();
                    double minZ = obj.getAsJsonPrimitive("minZ").getAsInt();
                    double maxX = obj.getAsJsonPrimitive("maxX").getAsInt();
                    double maxY = obj.getAsJsonPrimitive("maxY").getAsInt();
                    double maxZ = obj.getAsJsonPrimitive("maxZ").getAsInt();

                    Optional<tech.mcprison.prison.internal.World> prisonWorld =
                        Prison.get().getPlatform().getWorld(world);
                    if (!prisonWorld.isPresent()) {
                        Output.get().logWarn(String.format(
                            "Can't convert mine %s because its world %s doesn't exist anymore.",
                            name, world));
                        break; // Skip it, its world didn't exist.
                    }

                    Bounds bounds = new Bounds(new Location(prisonWorld.get(), minX, minY, minZ),
                        new Location(prisonWorld.get(), maxX, maxY, maxZ));

                    HashMap<BlockType, Integer> blocks = new HashMap<>();
                    for (Map.Entry<String, JsonElement> blockEntry : obj.getAsJsonObject("blocks")
                        .entrySet()) {
                        String[] blockParts = blockEntry.getKey().split(":");
                        BlockType type = BlockType.getBlock(Integer.parseInt(blockParts[0]),
                            Short.parseShort(blockParts[1]));

                        // Prison 2 stores chances in values < 1, whereas Prison 3 does it < 100
                        int chance = (int) ((blockEntry.getValue().getAsDouble()) * 100);

                        blocks.put(type, chance);
                    }

                    Mine ourMine = new Mine();
                    ourMine.setName(name);
                    ourMine.setBounds(bounds);
                    ourMine.setBlocks(blocks);

                    if (PrisonMines.get().getMines().contains(ourMine)) {
                        break;
                    }

                    PrisonMines.get().getMines().add(ourMine);
                }

                PrisonMines.get().getMines().save();
                alreadyConverted.createNewFile();
                return new ConversionResult(getName(), ConversionResult.Status.Success,
                    "Converted " + jsonFiles.length + " mines.");
            } else {
                alreadyConverted.createNewFile();
                return new ConversionResult(getName(), ConversionResult.Status.Success,
                    "Converted 0 mines.");
            }
        } catch (IOException e) {
            PrisonMines.get().getErrorManager().throwError(
                new Error("Encountered an error while converting mines.")
                    .appendStackTrace("while loading mines", e));
            return new ConversionResult(getName(), ConversionResult.Status.Failure,
                "IOException, check console for details");
        }
    }

    @Override public String getName() {
        return "Mines";
    }
}
