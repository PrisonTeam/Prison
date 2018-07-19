package tech.mcprison.prison.troubleshoot.zip;

import java.io.IOException;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.error.Error;
import tech.mcprison.prison.output.Output;

public class SupportZipCommands {
    @Command(identifier = "prison zip",onlyPlayers = false,permissions = "prison.admin",description = "Generates a support zip that can be used by the Prison Team to aid our support process.")
    public void createSupportZip(CommandSender sender){
        String out;
        try {
            out = SupportZip.create();
        } catch (IOException e) {
            Error error = new Error("Something went wrong when preparing a Support ZIP");
            error.appendStackTrace("",e);
            Prison.get().getErrorManager().throwError(error);
            Output.get().sendError(sender,"Something went wrong while preparing a Support ZIP. Check the log for details.");
            return;
        }
        Output.get().sendInfo(sender,"Successfully saved a Support ZIP to &b"+out+"&7. Give this file to us when asking for support. Thanks in advance for helping us help you :)");
    }
}
