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
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.CodeSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.modules.PluginEntity;
import tech.mcprison.prison.output.Output;

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
    private HashMap<String, Properties> configs = new HashMap<>();
    private String defaultLocale = DEFAULT_LOCALE;
    private String internalPath;
    
    private File localFolder;
    
    private static final List<LocaleManager> registeredInstances = new ArrayList<>();

    /**
     * Constructs a new {@link LocaleManager} owned by the given {@link PluginEntity}.
     *
     * @param module The module owning the new {@link LocaleManager}.
     * @since 1.0
     */
    public LocaleManager(PluginEntity module, String internalPath) {
        this.module = module;
        this.internalPath = internalPath;
        
        this.localFolder = getLocalDataFolder();
        
        getRegisteredInstances().add( this );
        
        reload();
    }

    public LocaleManager(PluginEntity module) {
        this(module, LOCALE_FOLDER);
    }
    
    
    public void reload() {
    	
    	// Reset configs:
    	configs.clear();
    	
    	
        // Always get the config's default-language settings to ensure we are always
        // accessing the correct files.
        setDefaultLocale( Prison.get().getPlatform().getConfigString( "default-language", "en_US" ));
        
        
        // Check to see if there are any updates that need to be applied to the 
        // the properties files that are local.
        // The properties file in the jar must have indicate it should be
        // updated and the local must not be denied to be updatable.
        refreshLocalLocales();
        
        
        // Load the shipped locales first first from the prison jar file:
        loadShippedLocales();
        
        
        // Then any custom locales will override and replace the internal locales:
        loadCustomLocales(); // custom locales will override

    }
    
    
    
    public static List<LocaleManager> getRegisteredInstances()
	{
		return registeredInstances;
	}

	@Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append( "LocalManager: " ).append( module.getName() )
    		.append( "  Internal Path: " ).append( internalPath )
    		.append( "  Local Folder:" ).append( getLocalFolder().getAbsolutePath() );
    	
    	return sb.toString();
    }
    
    public File getLocalDataFolder() {
    	// Setup the local folders:
    	File dataFolder = fixPrisonCoreLanguagePath( getOwningPlugin().getDataFolder() );
    	File localeDirectory = new File(dataFolder, LOCALE_FOLDER);
    	
    	// if the folder does not exist, try to create it:
    	if ( !localeDirectory.exists() ) {
    		localeDirectory.mkdirs();
    		
        	// Now copy all of the default language files that are in the prison jar to this new directory.
        	// This will make it a lot easier for admins to modify the language files.
        	extractShippedLocales( localeDirectory );

    	}
    	if ( !localeDirectory.isDirectory() ) {
            Output.get().logWarn( 
                "The Locale Folder is not a directory. [" +
            		localeDirectory.getAbsolutePath() + "]  Plugin: " +
                    getOwningPlugin() +
                    " Not able to load custom locales");
        }
    	
    	return localeDirectory;
    }

	protected class PropertyFileFilter 
		implements FilenameFilter {
		
		public PropertyFileFilter() {
			super();
		}

		@Override
		public boolean accept( File dir, String name )
		{
			boolean results = name != null && 
							name.toLowerCase().endsWith( ".properties" );
			return results;
		}
	}
	
	
    private void refreshLocalLocales() {
    	
    	// Setup the local folders:
    	File localeFolder = getLocalDataFolder();
    	
    	// Get a Map of all properties files in the selected directory:
    	TreeMap<String, LocalManagerPropertyFileData> localFilesByName = new TreeMap<>();
    	File[] localFiles = localeFolder.listFiles( new PropertyFileFilter() );
    	
    	for ( File file : localFiles ) {
    		LocalManagerPropertyFileData pfd = new LocalManagerPropertyFileData( file );
    		localFilesByName.put( pfd.getLocalPropertiesName(), pfd );
    		
    		checkLocalPropertiesStatus( pfd );
		}
    	
    	
    	// Prison's module properties files in the Prison jar that need to be scanned:
        CodeSource cs = getOwningPlugin().getClass().getProtectionDomain().getCodeSource();
        if (cs != null) {
        	
            try {
                URL jar = cs.getLocation();
                
                
                ZipEntry entry;
                try (
                	ZipInputStream zip = new ZipInputStream(jar.openStream());
                	) {
                	
                	while ( (entry = zip.getNextEntry()) != null) {
                		String entryName = entry.getName();
                		
                		if (entryName.startsWith(internalPath) && entryName.endsWith(".properties")) {
                			
                			String[] arr = entryName.split("/");
                			String jarResourceName = arr[arr.length - 1];
                			
                			LocalManagerPropertyFileData pfd = localFilesByName.get( jarResourceName );
                			
                			BufferedInputStream inStream = new BufferedInputStream( zip );
                			
                			if ( pfd != null ) {
                				// Need to check to see if there the local needs to be updated:
                				
                				// Read the whole properties file since it may need to be used more than once
                				String propertiesData = readInputStream( inStream );
                				
                				
                				
                				checkJarPropertiesStatus( pfd, propertiesData, jarResourceName );
                				
                				
                				if ( pfd.replaceLocalWithJar() ) {
                					// Replace the local with what's in the jar:
                					
                					// Archive the existing file:
                					archiveOldPropertiesFile( pfd.getLocalPropFile() );
                					
                					File newFile = new File( localeFolder, jarResourceName );
                					
                					
                					
                					Files.copy( new ByteArrayInputStream( 
                							propertiesData.getBytes( StandardCharsets.UTF_8 ) ), newFile.toPath() );
                					
                					Output.get().logInfo( "### LocalManager refreshLocalLocales(): Replace Local " +
                							"Jar. " + pfd.toString() + "  Replaced.");
                					
                				}
                				else {
                					// Do not replace:
//                						Output.get().logInfo( "### LocalManager refreshLocalLocales(): Keep existing " +
//                								"local jar. " + pfd.toString() );
                				}
                				
                			}
                			else {
                				// The current Zip Entry does not exist in the local directory.
                				// Need to extract it.
                				
                				File newFile = new File( localeFolder, jarResourceName );
                				
                				Files.copy( inStream, newFile.toPath() );
                				
                				Output.get().logInfo( "### LocalManager refreshLocalLocales(): Local did not exist. " +
                						"Jar copied to local. " );
                			}
                		}
                		else {
                			
                		}
                		// Need to close the entry to position to the next entry:
                		zip.closeEntry();
                		
                	}
                	
                }
                
            }
            catch ( Exception e ) {
            	e.printStackTrace();
            }
        }

    	
    }
    

    /**
     * NOTE: Since the inStream is from the ZipInputStream, it cannot be closed 
     *       through a try-with-resources since it will prevent reading of the next
     *       ZipEntry.
     *       
     * @param inStream
     * @return
     */
    private String readInputStream( BufferedInputStream inStream )
	{
		StringBuilder sb = new StringBuilder();
		
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader( inStream, StandardCharsets.UTF_8) );	
			
			String line = br.readLine();
			
			while ( line != null ) {
				sb.append( line ).append( "\n" );
				
				line = br.readLine();
			}
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	private void archiveOldPropertiesFile( File localPropFile ) {
		
    	String name = localPropFile.getName();
    	
    	String newName = "_archived_"  + name + "_" + getDateTime() + ".txt";
    	
    	File targetName = new File( localPropFile.getParentFile(), newName );
    	
    	localPropFile.renameTo( targetName );
	}
    
    private String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        df.setTimeZone(TimeZone.getDefault());
        return df.format(new Date());
    }

	private void checkLocalPropertiesStatus( LocalManagerPropertyFileData pfd )
	{
		Properties prop = new Properties();
		
		try (
				FileReader fr = new FileReader( pfd.getLocalPropFile() );
//				FileReader fr = new FileReader( pfd.getLocalPropFile(), StandardCharsets.UTF_8 ); // Java 11
				) {
			prop.load( fr );
			
			boolean hasVersion = prop.containsKey( "messages__version" );
			String version = prop.getProperty( "messages__version" );
			
			boolean hasAutoRefresh = prop.containsKey( "messages__auto_refresh" );
			String autoRefresh = prop.getProperty( "messages__auto_refresh" );
			
			if ( hasVersion && version != null && !version.trim().isEmpty() ) {
				pfd.setLocalVersion( version );
			}
			pfd.setLocalHasAutoReplace( hasAutoRefresh );
			pfd.setLocalAutoReplace( autoRefresh != null && "true".equalsIgnoreCase( autoRefresh ) );
			
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		
	}
    
    
    private void checkJarPropertiesStatus( LocalManagerPropertyFileData pfd, String propertiesData, 
    					String jarPath )
    {
    	Properties prop = new Properties();
    	
    	try {
    		
    		prop.load( new StringReader( propertiesData ) );
    		
    		boolean hasVersion = prop.containsKey( "messages__version" );
    		String version = prop.getProperty( "messages__version" );
    		
    		boolean hasAutoRefresh = prop.containsKey( "messages__auto_refresh" );
    		String autoRefresh = prop.getProperty( "messages__auto_refresh" );
    		
    		if ( hasVersion && version != null && !version.trim().isEmpty() ) {
    			pfd.setJarVersion( version );
    		}
    		pfd.setJarHasAutoReplace( hasAutoRefresh );
    		pfd.setJarAutoReplace( autoRefresh != null && "true".equalsIgnoreCase( autoRefresh ) );
    	}
    	catch ( IOException e ) {
    		Output.get().logWarn( "Unable to open and read a property file within the jar file. " +
    				"[" + jarPath + "]", e );
    	}
    	
    }

	private void loadCustomLocales() {
		
		// Setup the local folders:
    	File localeFolder = getLocalDataFolder();
		
    	File[] contents = localeFolder.listFiles( new PropertyFileFilter() );
    	
    	if (contents != null) {
    		for (File locale : contents) {
    			if (!locale.isDirectory()) {
    				try (
    					InputStream is = new FileInputStream(locale);
    					) {
    					
    					loadLocale(locale.getName().replace(".properties", ""), is, false);
    				} 
    				catch (IOException ex) {
    					Output.get().logWarn(
    							"Failed to load custom locale " + locale.getName() +
    							" for plugin " + getOwningPlugin() + " (" + 
    							ex.getMessage() + ")");
    				}
    			} 
    			else {
    				Output.get().logWarn("Found subfolder " + locale.getName() +
			    				" within locale folder " + LOCALE_FOLDER +
			    				" in data folder for plugin " + getOwningPlugin() +
			    				" - not loading");
    			}
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

	/**
	 * <p>This actually may never be used again.  The new functions above will
	 * extract jars when they don't exist on the local, and it will also update
	 * them when they are out of date too.
	 * </p>
	 * 
	 * @param targetPath
	 */
    private void extractShippedLocales( File targetPath ) {
    	
    	targetPath = fixPrisonCoreLanguagePath( targetPath );
    	
        CodeSource cs = getOwningPlugin().getClass().getProtectionDomain().getCodeSource();
        if (cs != null) {
            try {
                URL jar = cs.getLocation();

                ZipInputStream zip = new ZipInputStream(jar.openStream());
                
                ZipEntry entry;
                while ( zip != null && (entry = zip.getNextEntry()) != null) {
                	String entryName = entry.getName();
                	
                	if (entryName.startsWith(internalPath) && entryName.endsWith(".properties")) {
                		
                		String[] arr = entryName.split("/");
                		String localeName = arr[arr.length - 1];
                		
                		File newLocal = new File( targetPath, localeName );
                		
                		BufferedInputStream inStream = new BufferedInputStream( zip );

        				Files.copy( inStream, newLocal.toPath() );
                	}
                	else {
                		// Need to close the entry to position to the next entry:
                		zip.closeEntry();
                	}
                	
                	
                }
            } 
            catch (Exception ex) {
                throw new RuntimeException(
                    "Failed to initialize LocaleManager for plugin " + getOwningPlugin()
                        + " - Prison cannot continue to load.", ex);
            }
        } 
        else {
            throw new RuntimeException("LocalManager.extractShippedLocales(): " +
            		"Failed to load code source for plugin " + getOwningPlugin()
                + " - Prison cannot continue to load.");
        }
    	
    }
    
//    private void copyStreams(InputStream inStream, OutputStream outStream) 
//    		throws IOException {
//	    byte[] buf = new byte[8192];
//	    int length;
//	    while ((length = inStream.read(buf)) > 0) {
//	        outStream.write(buf, 0, length);
//	    }
//	}
    
    
    private void loadShippedLocales() {
        CodeSource cs = getOwningPlugin().getClass().getProtectionDomain().getCodeSource();
        if (cs != null) {
            try {
                URL jar = cs.getLocation();
                
                ZipEntry entry;

                try ( 
                		ZipInputStream zip = new ZipInputStream(jar.openStream());
                	) {
                	
                	while ((entry = zip.getNextEntry()) != null) {
                		String entryName = entry.getName();
                		if (entryName.startsWith(internalPath) && entryName
                				.endsWith(".properties")) {
                			String[] arr = entryName.split("/");
                			String localeName = arr[arr.length - 1].replace(".properties", "");
                			loadLocale(localeName, zip, true);
                		}
                	}
                }
            } 
            catch (IOException ex) {
                throw new RuntimeException(
                    "Failed to initialize LocaleManager for plugin " + getOwningPlugin()
                        + " - Rosetta cannot continue!", ex);
            }
        } 
        else {
            throw new RuntimeException("Failed to load code source for plugin " + getOwningPlugin()
                + " - Rosetta cannot continue!");
        }
    }

    private void loadLocale(String name, InputStream is, boolean printStackTrace) {
    	
        try {

        	
        	Properties temp = new Properties();
//            temp.load(is);

        	// The InputStream is part of a zipEntry so it cannot be closed, or it will close the zip stream
            BufferedReader br = new BufferedReader( new InputStreamReader( is, Charset.forName("UTF-8") ));
            String line = br.readLine();
            
            while ( line != null ) {
            	if ( !line.startsWith( "#" ) && line.contains( "=" ) ) {
            		
            		String[] keyValue = line.split( "\\=" );
            		String value = keyValue[1]; // StringEscapeUtils.escapeJava( keyValue[1] );
            		temp.put( keyValue[0], value );
            	}
            	
            	line = br.readLine();
            }
            
            
            Properties config;
            if (configs.containsKey(name)) {
                config = configs.get(name);
                
                for (Map.Entry<Object, Object> e : temp.entrySet()) {
                    config.put(e.getKey(), e.getValue());
                }
            } else {
                config = temp;
                configs.put(name, config);
            }
        } 
        catch (IOException ex) {
            if (printStackTrace) {
                ex.printStackTrace();
            }
            Output.get().logWarn("Failed to load locale " + name + " for plugin " + 
            			getOwningPlugin() + " - skipping");
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
    	if ( defaultLocale == null ) {
    		defaultLocale = Prison.get().getPlatform().getConfigString( "default-language", "en_US" );
    		if ( defaultLocale == null ) {
    			defaultLocale = "en_US";
    		}
    	}
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

	public String getInternalPath() {
		return internalPath;
	}
	public void setInternalPath( String internalPath ) {
		this.internalPath = internalPath;
	}

	public PluginEntity getModule() {
		return module;
	}
	
	public File getLocalFolder() {
		return localFolder;
	}
	public void setLocalFolder( File localFolder ) {
		this.localFolder = localFolder;
	}

	public HashMap<String, Properties> getConfigs() {
		return configs;
	}
	public void setConfigs( HashMap<String, Properties> configs ) {
		this.configs = configs;
	}

}
