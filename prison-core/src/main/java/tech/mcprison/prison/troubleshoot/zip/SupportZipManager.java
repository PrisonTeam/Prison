package tech.mcprison.prison.troubleshoot.zip;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.HashMap;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.error.Error;
import tech.mcprison.prison.modules.Module;

public class SupportZipManager {
    HashMap<String,SupportZipProvider> providers;
    public SupportZipManager(){
        providers = new HashMap<>();
        try {
            register(new FileProvider(Prison.get().getModuleManager().getModuleRoot()));
        } catch (NotDirectoryException e) {
            Error error = new Error("Couldn't add module_conf to the SupportZipManager as, for some reason, it's a directory?!");
            error.appendStackTrace("",e);
            Prison.get().getErrorManager().throwError(error);
        }
        try {
            register(new FileProvider(new File(Prison.get().getDataFolder(),"errors")));
        } catch (NotDirectoryException e) {
            Error error = new Error("Couldn't add errors to the SupportZipManager - not a directory");
            error.appendStackTrace("",e);
            Prison.get().getErrorManager().throwError(error);
        }
        register(new ServerInfoProvider());
    }
    public void tryRegister(Module module){
        if (module instanceof SupportZipProvider){
            if (!providers.containsKey(module.getName().toLowerCase())) {
                register((SupportZipProvider) module);
            }
        }
    }
    public void tryUnregister(Module module){
        if (module instanceof SupportZipProvider){
            if (providers.containsValue(module)){
                unregister((SupportZipProvider) module);
            }
        }
    }
    public void register(SupportZipProvider provider){
        providers.put(provider.getName().toLowerCase(),provider);
    }
    public void unregister(SupportZipProvider provider){
        providers.remove(provider.getName().toLowerCase());
    }
}
