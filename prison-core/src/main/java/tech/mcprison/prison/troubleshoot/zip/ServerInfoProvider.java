package tech.mcprison.prison.troubleshoot.zip;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.util.Text;

public class ServerInfoProvider implements SupportZipProvider {

    @Override
    public String getName() {
        return "context";
    }

    @Override
    public SupportZipEntry[] generateSupportZipEntries() {
        List<SupportZipEntry> list = getNotZipfiles();
        list.add(getServerInfo());
        return (SupportZipEntry[])list.toArray();
    }

    private List<SupportZipEntry> getNotZipfiles() {
        ArrayList<SupportZipEntry> es = new ArrayList<>();
        for (File file : Objects.requireNonNull(Prison.get().getDataFolder().listFiles(
            pathname -> !pathname.isDirectory() && !pathname.getName().endsWith(".zip") && !pathname.getName().equalsIgnoreCase("platform.txt")))) {
            SupportZipEntry entry = new SupportZipEntry();
            entry.setPath(file.toURI().relativize(Prison.get().getDataFolder().toURI()).getPath());
            try {
                entry.setData(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                continue;
            }
            es.add(entry);
        }
        return es;
    }

    private SupportZipEntry getServerInfo() {
        StringBuilder sb = new StringBuilder();
        String n = System.lineSeparator();
        sb.append(Text.titleize("Information") + n);
        sb.append("Platform Class: " + Prison.get().getPlatform().getClass().getTypeName() + n);
        sb.append("Plugin Version: " + Prison.get().getPlatform().getPluginVersion() + n);
        sb.append("API Level: " + Prison.API_LEVEL + n);
        sb.append(n);
        sb.append(Text.titleize("Capabilities")+n);
        for (Entry<Capability,Boolean> capability : Prison.get().getPlatform().getCapabilities().entrySet()){
            sb.append(capability.getKey().name().substring(0, 1) + capability.getKey().name().toLowerCase().substring(1)+": "+(capability.getValue() ? "Capable" : "Incapable")+n);
        }
        for (IntegrationType it : IntegrationType.values()) {
            sb.append(n);
            sb.append(Text.titleize(
                it.name().substring(0, 1) + it.name().toLowerCase().substring(1)
                    + " Integrations") + n);
            for (Integration i : Prison.get().getIntegrationManager().getAllForType(it)) {
                sb.append(
                    i.getProviderName() + ": " + (i.hasIntegrated() ? "Active" : "Inactive") + n);
            }
        }
        SupportZipEntry entry = new SupportZipEntry();
        entry.setPath("platform.txt");
        entry.setData(sb.toString().getBytes());
        return entry;
    }
}
