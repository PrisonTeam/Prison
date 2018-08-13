/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.localization;

import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.modules.Module;
import xyz.faizaan.prison.modules.PluginEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Provides localization support for a particular {@link Module}. <p>
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
        loadShippedLocales();
        loadCustomLocales(); // custom locales will override
    }

    public LocaleManager(PluginEntity module) {
        this(module, LOCALE_FOLDER);
    }

    private void loadCustomLocales() {
        File dataFolder = getOwningPlugin().getDataFolder();
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
