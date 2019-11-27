package tech.mcprison.prison.spigot.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.PlaceholderIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class PlaceHolderAPIIntegration extends PlaceholderExpansion implements PlaceholderIntegration {

	private boolean pluginInstalled;

	public PlaceHolderAPIIntegration() {
		pluginInstalled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}

	@Override
	public void registerPlaceholder(String placeholder, Function<Player, String> action) {
		this.register();
	}

	@Override
	public String getProviderName() {
		return "PlaceHolderAPI";
	}

	@Override
	public boolean hasIntegrated() {
		return pluginInstalled;
	}

	@Override
	public String getAuthor(){
		return "PrisonTeam";
	}

	@Override
	public String getIdentifier(){
		return "Prison";
	}

	@Override
	public String getVersion(){
		return PrisonAPI.getPluginVersion();
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier){

		if(identifier.equals("rank")){
			return getNames(player.getUniqueId());
		}else if(identifier.equals("rankup_cost")){
			return getNextCost(player.getUniqueId());
		}else if(identifier.equals("rankup_rank")) {
			return getNextName(player.getUniqueId());
		}

		return null;
	}

	private String getNames(UUID uid) {

		Optional<RankPlayer> player =
				PrisonRanks.getInstance().getPlayerManager().getPlayer(uid);
		String prefix = "";

		if (player.isPresent() && !player.get().getRanks().isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (Map.Entry<RankLadder, Rank> entry : player.get().getRanks().entrySet()) {
				builder.append(entry.getValue().name);
				builder.append(", ");
			}
			prefix = builder.toString();

		}

		if(prefix.length() > 0) {
			return prefix.substring(0, prefix.length() - 2);
		}else{
			return "";
		}

	}

	private String getNextCost(UUID uid) {

		Optional<RankPlayer> player =
				PrisonRanks.getInstance().getPlayerManager().getPlayer(uid);
		String prefix = "";

		if (player.isPresent() && !player.get().getRanks().isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (Map.Entry<RankLadder, Rank> entry : player.get().getRanks().entrySet()) {
				if(entry.getKey().getNext(entry.getKey().getPositionOfRank(entry.getValue())).isPresent()) {
					builder.append(entry.getKey().getNext(entry.getKey().getPositionOfRank(entry.getValue())).get().cost);
					builder.append(", ");
				}
			}
			prefix = builder.toString();
		}

		if(prefix.length() > 0) {
			return prefix.substring(0, prefix.length() - 2);
		}else{
			return "";
		}

	}

	private String getNextName(UUID uid) {

		Optional<RankPlayer> player =
				PrisonRanks.getInstance().getPlayerManager().getPlayer(uid);
		String prefix = "";

		if (player.isPresent() && !player.get().getRanks().isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (Map.Entry<RankLadder, Rank> entry : player.get().getRanks().entrySet()) {
				if(entry.getKey().getNext(entry.getKey().getPositionOfRank(entry.getValue())).isPresent()) {
					builder.append(entry.getKey().getNext(entry.getKey().getPositionOfRank(entry.getValue())).get().name);
					builder.append(", ");
				}
			}
			prefix = builder.toString();
		}

		if(prefix.length() > 0) {
			return prefix.substring(0, prefix.length() - 2);
		}else{
			return "";
		}

	}

}