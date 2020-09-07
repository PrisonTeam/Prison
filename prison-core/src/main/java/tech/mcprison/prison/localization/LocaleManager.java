/*
 * New BSD License (BSD-new)
 *
 * Copyright (c) 2015-2016 Max Roncace
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     - Neither the name of the copyright holder nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package tech.mcprison.prison.localization;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.modules.PluginEntity;

/**
 * Provides localization support for a particular {@link tech.mcprison.prison.modules.Module}. <p>
 * <p>Locales are loaded as <code>.properties</code> files from the {@code /lang} directory of the
 * archive of the plugin owning this {@link LocaleManager}.</p> <p> Adapted for Prison.
 *
 * @author Max Roncacé
 * @author Faizaan A. Datoo
 * @version 1.0.0
 * @since 1.0
 */
public class LocaleManager {

    static final HashMap<String, List<String>> ALTERNATIVES = new HashMap<>();
    static final Logger LOGGER = Logger.getLogger("Rosetta");
    private static final String DEFAULT_LOCALE = "en_US";
    private static final String LOCALE_FOLDER = "lang";

    static {
        // English dialects
        ALTERNATIVES.put("en_AU", Arrays.asList("en_GB", "en_CA", "en_US"));
        ALTERNATIVES.put("en_CA", Arrays.asList("en_GB", "en_AU", "en_US"));
        ALTERNATIVES.put("en_GB", Arrays.asList("en_GB", "en_AU", "en_US"));
        ALTERNATIVES.put("en_PT", Arrays.asList("en_US", "en_CA", "en_GB", "en_AU")); // idk
        ALTERNATIVES.put("en_US", Arrays.asList("en_CA", "en_GB", "en_AU"));

        // Spanish dialects (not sure how accurate this is, mostly guesswork)
        ALTERNATIVES.put("es_AR", Arrays.asList("es_UY", "es_VE", "es_MX", "es_ES"));
        ALTERNATIVES.put("es_ES", Arrays.asList("es_MX", "es_AR", "es_UY", "es_VE"));
        ALTERNATIVES.put("es_MX", Arrays.asList("es_ES", "es_AR", "es_UY", "es_VE"));
        ALTERNATIVES.put("es_UY", Arrays.asList("es_AR", "es_VE", "es_MX", "es_ES"));
        ALTERNATIVES.put("es_VE", Arrays.asList("es_AR", "es_UY", "es_MX", "es_ES"));

        // French dialects
        ALTERNATIVES.put("fr_CA", Arrays.asList("fr_FR"));
        ALTERNATIVES.put("fr_FR", Arrays.asList("fr_CA"));

        // Norwegian dialects (not sure how accurate this is)
        ALTERNATIVES.put("nb_NO", Arrays.asList("no_NO", "nn_NO"));
        ALTERNATIVES.put("nn_NO", Arrays.asList("no_NO", "nb_NO"));
        ALTERNATIVES.put("no_NO", Arrays.asList("nb_NO", "nn_NO"));

        // Portugese dialects
        ALTERNATIVES.put("pt_BR", Arrays.asList("pt_PT"));
        ALTERNATIVES.put("pt_PT", Arrays.asList("pt_BR"));
    }

    private final PluginEntity module;
    HashMap<String, Properties> configs = new HashMap<>();
    private String defaultLocale = DEFAULT_LOCALE;
    private String internalPath;

    /**
     * Constructs a new {@link LocaleManager} owned by the given {@link PluginEntity}.
     *
     * @param module The module owning the new {@link LocaleManager}.
     * @since 1.0
     */
    public LocaleManager(PluginEntity module, String internalPath) {
        this.module = module;
        this.internalPath = internalPath;
        
        // Load the shipped locales first first from the prison jar file:
        loadShippedLocales();
        
        // Then any custom locales will overried and replace the internal locales:
        loadCustomLocales(); // custom locales will override
    }

    public LocaleManager(PluginEntity module) {
        this(module, LOCALE_FOLDER);
    }

    private void loadCustomLocales() {
        File dataFolder = getOwningPlugin().getDataFolder();
        
    	dataFolder = fixPrisonCoreLanguagePath( dataFolder );
    	        
        if (dataFolder.isDirectory()) {
            File localeFolder = new File(dataFolder, LOCALE_FOLDER);
            if (localeFolder.exists()) {
                if (localeFolder.isDirectory()) {
                    File[] contents = localeFolder.listFiles();
                    if (contents != null) {
                        for (File locale : contents) {
                            if (!locale.isDirectory()) {
                                if (locale.getName().endsWith(".properties")) {
                                    try {
                                        loadLocale(locale.getName().replace(".properties", ""),
                                            new FileInputStream(locale), false);
                                    } catch (IOException ex) {
                                        LOGGER.warning(
                                            "Failed to load custom locale \"" + locale.getName()
                                                + "\" for plugin " + getOwningPlugin() + " (" + ex
                                                .getClass().getName() + ")");
                                    }
                                }
                            } else {
                                LOGGER.warning("Found subfolder \"" + locale.getName()
                                    + "\" within locale folder \"" + LOCALE_FOLDER
                                    + "\" in data folder for plugin " + getOwningPlugin()
                                    + " - not loading");
                            }
                        }
                    }
                } else {
                    LOGGER.warning(
                        "Locale folder \"" + LOCALE_FOLDER + "\" in data folder for plugin "
                            + getOwningPlugin()
                            + " is not a directory - not loading custom locales");
                }
            }
            else {
            	// The local custom lang directory doesn't exist so create it:
            	localeFolder.mkdirs();
            	
            	// Now copy all of the default language files that are in the prison jar to this new directory.
            	// This will make it a lot easier for admins to modify the language files.
            	extractShippedLocales( localeFolder );
            	
            }
        }
    }

    /**
     * <p>The Prison core is not truly a Prison module, so the targetPath that is passed to this function
     *    is not correctly setup to place the core/lang directory in the correct place.  It will try
     *    to put it in /plugins/Prison/lang which is wrong.  This function detects this error and 
     *    will regenerate the targetPath to be /plugins/Prison/module_conf/core/lang.
     * </p>
     * 
     * <p>Note: There is a unit test that is ran by the gradle build process that hits this 
     * function.  At the time, the value of PrisonAPI.getModuleManager() is null due to the unit test,
     * therefore the following function is using static functions to ensure there is not a 
     * null pointer exception thrown.  The unit test does not do anything with the content of 
     * these directories, but it still hits these functions, so allowing it to proceed, even if the
     * paths are incorrect, will have no impact on anything.
     * </p>
     * 
     * @param targetPath
     * @return The corrected targetPath for the core plugin
     */
	private File fixPrisonCoreLanguagePath( File targetPath ) {
		if ( !targetPath.getAbsolutePath().startsWith( 
				ModuleManager.getModuleRootDefault().getAbsolutePath() ) ) {
			targetPath = Module.setupDataFolder( Prison.PSEDUO_MODLE_NAME );
    	}
		return targetPath;
	}

    private void extractShippedLocales( File targetPath ) {
    	
    	targetPath = fixPrisonCoreLanguagePath( targetPath );
    	
        CodeSource cs = getOwningPlugin().getClass().getProtectionDomain().getCodeSource();
        if (cs != null) {
            try {
                URL jar = cs.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry entry;
                while ((entry = zip.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    
                    if (entryName.startsWith(internalPath) && entryName.endsWith(".properties")) {
                    	
                    	String[] arr = entryName.split("/");
                    	String localeName = arr[arr.length - 1];

                    	File newLocal = new File( targetPath, localeName );
                    	
                    	BufferedInputStream inStream = new BufferedInputStream( zip );
                    	try (
                    			OutputStream outStream = Files.newOutputStream( newLocal.toPath(), 
                    					StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE );
                    			) {
                    		copyStreams( inStream, outStream) ;
                    	}
                    }
                    
                }
            } catch (IOException ex) {
                throw new RuntimeException(
                    "Failed to initialize LocaleManager for plugin " + getOwningPlugin()
                        + " - Prison cannot continue to load.", ex);
            }
        } else {
            throw new RuntimeException("LocalManager.extractShippedLocales(): " +
            		"Failed to load code source for plugin " + getOwningPlugin()
                + " - Prison cannot continue to load.");
        }
    	
    }
    
    private void copyStreams(InputStream inStream, OutputStream outStream) 
    		throws IOException {
	    byte[] buf = new byte[8192];
	    int length;
	    while ((length = inStream.read(buf)) > 0) {
	        outStream.write(buf, 0, length);
	    }
	}
    
    
    private void loadShippedLocales() {
        CodeSource cs = getOwningPlugin().getClass().getProtectionDomain().getCodeSource();
        if (cs != null) {
            try {
                URL jar = cs.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry entry;
                while ((entry = zip.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    if (entryName.startsWith(internalPath) && entryName
                        .endsWith(".properties")) {
                        String[] arr = entryName.split("/");
                        String localeName = arr[arr.length - 1].replace(".properties", "");
                        loadLocale(localeName, zip, true);
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(
                    "Failed to initialize LocaleManager for plugin " + getOwningPlugin()
                        + " - Rosetta cannot continue!", ex);
            }
        } else {
            throw new RuntimeException("Failed to load code source for plugin " + getOwningPlugin()
                + " - Rosetta cannot continue!");
        }
    }

    private void loadLocale(String name, InputStream is, boolean printStackTrace) {
    	
        try {
            Properties temp = new Properties();
            temp.load(is);
            Properties config;
            if (configs.containsKey(name)) {
                config = configs.get(name);
                for (Map.Entry<Object, Object> e : temp.entrySet()) {
                    config.put(e.getKey(), e.getValue());
                }
            } else {
                config = temp;
            }
            configs.put(name, config);
        } catch (IOException ex) {
            if (printStackTrace) {
                ex.printStackTrace();
            }
            LOGGER.warning("Failed to load locale " + name + " for plugin " + getOwningPlugin()
                + " - skipping");
        }
    }

    /**
     * Gets the plugin owning this {@link LocaleManager}.
     *
     * @return The plugin owning this {@link LocaleManager}
     * @since 1.0
     */
    public PluginEntity getOwningPlugin() {
        return module;
    }

    /**
     * Gets the default locale of this {@link LocaleManager}.
     *
     * @return A string representing the default locale. This should follow the {@code ISO 639-1}
     * and {@code ISO 3166-1} standards, respectively (e.g. {@code en_US}) and defaults to {@code
     * en_US}.
     * @since 1.0
     */
    public String getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Sets the default locale of this {@link LocaleManager}.
     *
     * @param locale A string representing the default locale. This should follow the {@code ISO
     * 639-1} and {@code ISO 3166-1} standards, respectively (e.g. {@code en_US} or {@code enUS})
     * and defaults to {@code en_US}.
     * @since 1.0
     */
    public void setDefaultLocale(String locale) {
        this.defaultLocale = locale;
    }

    /**
     * Gets the {@link Localizable} associated with the given key.
     *
     * @param key The key of the message to retrieve
     * @return The retrieved message as a {@link Localizable}
     * @since 1.0
     */
    public Localizable getLocalizable(String key) {
        return new Localizable(this, key);
    }

    String getLocale(Player player) {
        return player.getLocale().orElse(getDefaultLocale());
    }

}
