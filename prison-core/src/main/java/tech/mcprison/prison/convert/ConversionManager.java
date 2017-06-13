package tech.mcprison.prison.convert;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton which runs all registered conversion agents.
 *
 * @author Faizaan A. Datoo
 */
public class ConversionManager {

    private static ConversionManager instance;
    private List<ConversionAgent> agents;

    public ConversionManager() {
        this.agents = new ArrayList<>();
    }

    public static ConversionManager getInstance() {
        if (instance == null) {
            instance = new ConversionManager();
        }
        return instance;
    }

    public void registerConversionAgent(ConversionAgent agent) {
        agents.add(agent);
    }

    public List<ConversionResult> runConversion() {
        List<ConversionResult> results = new ArrayList<>();
        for (ConversionAgent agent : agents) {
            ConversionResult result = agent.convert();
            results.add(result);
        }
        return results;
    }


}
