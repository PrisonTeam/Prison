package me.faizaand.prison.gui.signs;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventPriority;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.block.Block;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.store.Collection;
import me.faizaand.prison.store.Document;
import me.faizaand.prison.util.BlockType;
import me.faizaand.prison.util.GameLocation;

import java.util.*;

public class DisplaySignManager {

    private static DisplaySignManager ourInstance = new DisplaySignManager();

    public static DisplaySignManager getInstance() {
        return ourInstance;
    }

    private List<DisplaySignAdapter> adapters;

    private Collection signsColl;

    private DisplaySignManager() {
        this.adapters = new ArrayList<>();
        listenForSignPlace();
        listenForSignBreak();
        scheduleRefresh();

        // we want to do this after all the other plugins boot up so that we can have
        // all our signs loaded. just in case any third party modules are installed.
        Prison.get().getPlatform().getScheduler().runTaskLater(this::load, 0L);
    }

    private void load() {
        Optional<Collection> collOpt = Prison.get().getMetaDatabase().getCollection("signs");
        if (!collOpt.isPresent()) {
            Prison.get().getMetaDatabase().createCollection("signs");
            collOpt = Prison.get().getMetaDatabase().getCollection("signs");
        }
        this.signsColl = collOpt.get();

        // we can store/load them by location block coordinates
        List<Document> signDocs = signsColl.getAll();

        for (Document signDoc : signDocs) {
            String location = (String) signDoc.get("location");
            String identifier = ((String) signDoc.get("identifier"));
            String[] params = ((ArrayList<String>) signDoc.get("params")).toArray(new String[]{});

            // parse the location
            // [0] - world, [1,2,3] - x,y,z
            String[] coords = location.split(",");
            GameLocation loc = new GameLocation(
                    Prison.get().getPlatform().getWorldManager().getWorld(coords[0]).orElse(null),
                    Double.parseDouble(coords[1]),
                    Double.parseDouble(coords[2]),
                    Double.parseDouble(coords[3]));

            if (!getAdapter(identifier).isPresent()) {
                Output.get().logWarn("A sign at " + loc.toString() + " has an identifier with no adapter. Skipping...");
                continue;
            }

            DisplaySign sign = new DisplaySign(loc, identifier, params);
            getAdapter(identifier).get().addSign(sign);
        }
    }

    public void save() {
        for (DisplaySignAdapter adapter : adapters) {
            for (DisplaySign displaySign : adapter.getSigns()) {
                saveSign(displaySign);
            }
        }
    }

    private void saveSign(DisplaySign displaySign) {
        Document signDoc = new Document();

        GameLocation location = displaySign.getLocation();
        signDoc.put("location", location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ());
        signDoc.put("identifier", displaySign.getIdentifier());
        signDoc.put("params", displaySign.getParams());

        this.signsColl.insert(getSignName(location), signDoc);
    }

    private String getSignName(GameLocation location) {
        String format = "%s_%d%d%d";
        return String.format(format, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
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
            DisplaySign sign = new DisplaySign(block.getLocation(), identifier, params);
            displaySignAdapter.addSign(sign);
            saveSign(sign);

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

            DisplaySign sign = getSignAt(block.getLocation());
            if (sign == null) {
                return new Object[]{};
            }

            this.signsColl.remove(getSignName(block.getLocation()));

            Optional<DisplaySignAdapter> adapterOptional = getAdapter(sign.getIdentifier());
            adapterOptional.ifPresent(displaySignAdapter -> displaySignAdapter.removeSign(block.getLocation()));

            return new Object[]{};
        }, EventPriority.NORMAL);
    }

    private DisplaySign getSignAt(GameLocation loc) {
        for (DisplaySignAdapter adapter : adapters) {
            for (DisplaySign sign : adapter.getSigns()) {
                if (sign.getLocation().equals(loc)) return sign;
            }
        }

        return null;
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
                if (timeElapsed >= adapter.getRefreshRate()) {
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
