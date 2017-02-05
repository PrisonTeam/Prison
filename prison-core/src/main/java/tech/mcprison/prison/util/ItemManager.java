package tech.mcprison.prison.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import tech.mcprison.prison.Output;
import tech.mcprison.prison.Prison;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

/**
 * This class takes care of the items.csv containing 8000+ different name combinations for blocks.
 *
 * @author Dylan M. Perks
 * @since API 1.1
 */
public class ItemManager {
    private Multimap<BlockType, String> items;

    public ItemManager() {
        try {
            File file = new File(Prison.get().getDataFolder(), "/items.csv");
            items = ArrayListMultimap.create();

            if (!file.exists()) {
                InputStream inputStream = getClass().getResourceAsStream("/items.csv");
                Files.copy(inputStream, Paths.get(file.getPath()));
            }
            BufferedReader in = new BufferedReader(new FileReader(file));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                try {
                    if (!inputLine.startsWith("#")) {
                        String[] array = inputLine.split(",");
                        String itemName = array[0];
                        int id = Integer.parseInt(array[1]);
                        short data = Short.parseShort(array[2]);
                        items.put(BlockType.getBlockWithData(id, data), itemName.toLowerCase());
                    }
                } catch (Exception e) {
                    Output.get()
                            .logError("Error while reading items.csv -- it's probably invalid", e);
                }
            }
        } catch (Exception e) {
            Output.get().logError("Error while reading items.csv", e);
        }
    }

    public Map<BlockType, Collection<String>> getItems() {
        return items.asMap();
    }

}
