package me.faizaand.prison.signs;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventPriority;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.block.Block;
import me.faizaand.prison.internal.block.GameSign;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.util.BlockType;

import java.util.*;

public class DisplaySignManager {

    private static DisplaySignManager ourInstance = new DisplaySignManager();

    public static DisplaySignManager getInstance() {
        return ourInstance;
    }

    private List<DisplaySignAdapter> adapters;

    private DisplaySignManager() {
        this.adapters = new ArrayList<>();
        listenForSignPlace();
        listenForSignBreak();
        scheduleRefresh();
    }

    /**
     * Note: This applies both to sign placement and updating!
     */
    private void listenForSignPlace() {
        Prison.get().getEventManager().subscribe(EventType.SignChangeEvent, objects -> {
            String[] lines = (String[]) objects[0];
            GamePlayer player = (GamePlayer) objects[1];
            Block block = (Block) objects[2];

            if (weDontCareAboutEvent(block, player)) {
                return new Object[]{}; // validation failed, end now
            }

            if (!lines[0].equalsIgnoreCase("[prison]")) {
                return new Object[]{}; // we don't care about this one either
            }

            String identifier = lines[1];
            String[] params = new String[]{lines[2], lines[3]};

            Optional<DisplaySignAdapter> optional = adapters.stream().filter(a -> a.getIdentifier().equalsIgnoreCase(identifier)).findFirst();
            if (!optional.isPresent()) {
                // todo fix this
                player.sendMessage("You dun' fudged up.");
                return new Object[]{};
            }

            DisplaySignAdapter displaySignAdapter = optional.get();
            displaySignAdapter.addSign(new DisplaySign(block.getLocation(), identifier, params));

            return new Object[]{};
        }, EventPriority.NORMAL);
    }

    private void listenForSignBreak() {
        Prison.get().getEventManager().subscribe(EventType.BlockBreakEvent, objects -> {
            Block block = (Block) objects[0];
            GamePlayer player = (GamePlayer) objects[1];

            if (weDontCareAboutEvent(block, player)) {
                return new Object[]{}; // validation failed, end now
            }

            GameSign sign = ((GameSign) block.getState());
            String identifier = sign.getLines().get(0);
            Optional<DisplaySignAdapter> adapterOptional = getAdapter(identifier);
            adapterOptional.ifPresent(displaySignAdapter -> displaySignAdapter.removeSign(block.getLocation()));

            return new Object[]{};
        }, EventPriority.NORMAL);
    }

    /**
     * Ensure that this event is one that we're concerned about (i.e. player has permission to make display signs,
     * and the block is a sign).
     */
    private boolean weDontCareAboutEvent(Block block, GamePlayer player) {
        // We make sure that we're dealing with a sign
        if (block.getType() != BlockType.STANDING_SIGN_BLOCK && block.getType() != BlockType.WALL_MOUNTED_SIGN_BLOCK) {
            return true;
        }

        // Make sure
        if (!player.hasPermission("prison.signs")) {
            // todo localize
            Output.get().sendError(player, "You lack the necessary permissions to alter signs.");
            return true;
        }

        return false;
    }

    private void scheduleRefresh() {
        Map<String, Long> timeSinceLast = new HashMap<>(); // adapter identifier, duration since last refresh
        Prison.get().getPlatform().getScheduler().runTaskTimer(() -> {
            for (DisplaySignAdapter adapter : adapters) {
                long timeElapsed = timeSinceLast.getOrDefault(adapter.getIdentifier(), 0L);
                if (timeElapsed > adapter.getRefreshRate()) {
                    // time to refresh!
                    adapter.refreshSigns();
                    timeSinceLast.put(adapter.getIdentifier(), 0L);
                } else {
                    timeElapsed++;
                    timeSinceLast.put(adapter.getIdentifier(), timeElapsed);
                }
            }
        }, 20L, 20L);
    }

    public void registerAdapter(DisplaySignAdapter adapter) {
        if (getAdapter(adapter.getIdentifier()).isPresent())
            throw new IllegalArgumentException("an adapter for identifier " + adapter.getIdentifier() + " has already been registered.");

        this.adapters.add(adapter);
    }

    public Optional<DisplaySignAdapter> getAdapter(String identifier) {
        return adapters.stream().filter(displaySignAdapter -> displaySignAdapter.getIdentifier().equalsIgnoreCase(identifier)).findFirst();
    }

}
