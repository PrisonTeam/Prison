package tech.mcprison.prison.ranks;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.convert.ConversionAgent;
import tech.mcprison.prison.convert.ConversionResult;
import tech.mcprison.prison.error.Error;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class RankConversionAgent implements ConversionAgent {

    @Override public ConversionResult convert() {
        File oldFolder = new File(PrisonAPI.getPluginDirectory().getParent(), "Prison.old");
        File ranksFolder = new File(oldFolder, "ranks");

        File alreadyConverted = new File(ranksFolder, ".converted");
        if (alreadyConverted.exists()) {
            return ConversionResult.failure(getName(),
                "Already converted. Delete the '/plugins/Prison.old/ranks' folder.");
        }

        String[] ranksJson = ranksFolder.list((dir, name) -> name.endsWith(".json"));

        try {

            if (ranksJson != null) {
                for (String rankJson : ranksJson) {
                    File rankFile = new File(ranksFolder, rankJson);
                    String json = new String(Files.readAllBytes(rankFile.toPath()));

                    JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                    String name = obj.getAsJsonPrimitive("name").getAsString();
                    double price = obj.getAsJsonPrimitive("price").getAsDouble();
                    String prefix = obj.getAsJsonPrimitive("prefix").getAsString();
                    if (!prefix.contains("[")) {
                        prefix = "&3[" + prefix;
                    }
                    if (!prefix.contains("]")) {
                        prefix = prefix + "&3]";
                    }

                    if(PrisonRanks.getInstance().getRankManager().getRank(name) != null) {
                        break; // Already added
                    }

                    RankLadder rankLadder =
                    				PrisonRanks.getInstance().getLadderManager().getLadder("default");
                    if ( rankLadder == null ) {
                        break; // Idek how this is possible.
                    }

                    Optional<Rank> ourRank =
                        PrisonRanks.getInstance().getRankManager().createRank(name, prefix, price);
                    if (!ourRank.isPresent()) {
                        Output.get().logWarn(String.format("Could not convert rank '%s'", name));
                        break; // It failed
                    }

//                    try {
                        PrisonRanks.getInstance().getRankManager().saveRank(ourRank.get());
//                    } catch (IOException e) {
//                        String nonNullName = name == null ? "null" : name;
//                        PrisonRanks.getInstance().getErrorManager().throwError(
//                            new Error("while converting ranks")
//                                .appendStackTrace("while saving rank " + nonNullName, e));
//                        break; // Skip this...
//                    }

                    rankLadder.addRank(ourRank.get());
                    try {
                        PrisonRanks.getInstance().getLadderManager().saveLadder( rankLadder );
                    } catch (IOException e) {
                        PrisonRanks.getInstance().getErrorManager().throwError(
                            new Error("while converting ranks")
                                .appendStackTrace("while saving default ladder", e));
                        break; // Skip this...
                    }

                }

                Output.get().logInfo("Notice: While we converted your ranks data, Prison 3 no longer ties itself to the permissions plugin."
                    + "That means that if you want users to change permissions groups when they rank up, you'll have to use rank-up commands.");
                Output.get().logInfo("For more information, see this article:&b https://github.com/MC-Prison/Prison/wiki/Ranks-Guidebook#rank-up-commands");

                alreadyConverted.createNewFile();
                return new ConversionResult(getName(), ConversionResult.Status.Success,
                    "Converted " + ranksJson.length + " ranks.");
            } else {
                alreadyConverted.createNewFile();
                return new ConversionResult(getName(), ConversionResult.Status.Success,
                    "Converted 0 ranks.");
            }
        } catch (IOException e) {
            PrisonRanks.getInstance().getErrorManager().throwError(
                new Error("while converting ranks").appendStackTrace("while loading ranks", e));
            return ConversionResult.failure(getName(), "IOException, check console for details.");
        }

    }

    @Override public String getName() {
        return "Ranks";
    }

}
