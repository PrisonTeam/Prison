package tech.mcprison.prison.troubleshoot.zip;

import java.util.HashMap;
import tech.mcprison.prison.modules.Module;

public class SupportZipManager {
    HashMap<String,SupportZipProvider> providers;
    public SupportZipManager(){
        providers = new HashMap<>();
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
