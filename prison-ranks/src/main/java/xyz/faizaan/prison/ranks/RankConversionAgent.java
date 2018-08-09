/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.ranks;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import xyz.faizaan.prison.Prison;
import xyz.faizaan.prison.convert.ConversionAgent;
import xyz.faizaan.prison.convert.ConversionResult;
import xyz.faizaan.prison.error.Error;
import xyz.faizaan.prison.output.Output;
import xyz.faizaan.prison.ranks.data.Rank;
import xyz.faizaan.prison.ranks.data.RankLadder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class RankConversionAgent implements ConversionAgent {

    @Override public ConversionResult convert() {
        File oldFolder = new File(Prison.get().getPlatform().getPluginDirectory().getParent(), "Prison.old");
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

                    JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                    String name = obj.getAsJsonPrimitive("name").getAsString();
                    double price = obj.getAsJsonPrimitive("price").getAsDouble();
                    String prefix = obj.getAsJsonPrimitive("prefix").getAsString();
                    if (!prefix.contains("[")) {
                        prefix = "&3[" + prefix;
                    }
                    if (!prefix.contains("]")) {
                        prefix = prefix + "&3]";
                    }

                    if(PrisonRanks.getInstance().getRankManager().getRank(name).isPresent()) {
                        break; // Already added
                    }

                    Optional<RankLadder> rankLadderOptional =
                        PrisonRanks.getInstance().getLadderManager().getLadder("default");
                    if (!rankLadderOptional.isPresent()) {
                        break; // Idek how this is possible.
                    }

                    Optional<Rank> ourRank =
                        PrisonRanks.getInstance().getRankManager().createRank(name, prefix, price);
                    if (!ourRank.isPresent()) {
                        Output.get().logWarn(String.format("Could not convert rank '%s'", name));
                        break; // It failed
                    }

                    try {
                        PrisonRanks.getInstance().getRankManager().saveRank(ourRank.get());
                    } catch (IOException e) {
                        String nonNullName = name == null ? "null" : name;
                        PrisonRanks.getInstance().getErrorManager().throwError(
                            new Error("while converting ranks")
                                .appendStackTrace("while saving rank " + nonNullName, e));
                        break; // Skip this...
                    }

                    rankLadderOptional.get().addRank(ourRank.get());
                    try {
                        PrisonRanks.getInstance().getLadderManager()
                            .saveLadder(rankLadderOptional.get());
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
