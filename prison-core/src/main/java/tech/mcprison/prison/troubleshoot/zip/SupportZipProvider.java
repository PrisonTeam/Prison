package tech.mcprison.prison.troubleshoot.zip;

public interface SupportZipProvider {
    String getName();
    SupportZipEntry[] generateSupportZipEntries();
}
