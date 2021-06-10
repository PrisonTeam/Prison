package tech.mcprison.prison.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;

/**
 * <p>This class will take a list of jars and validate that a
 * class file that they contain is valid and the Java 
 * version it was compiled with.
 * </p>
 * 
 * <p>Based upon the following Java class signature:
 * </p>
 * https://en.wikipedia.org/wiki/Java_class_file
 * 
 *
 */
public class PrisonJarReporter {
	
	private List<JarFileData> jars;
	
	public enum JavaVersion {
		JDK_1_1("2d"),
		J2SE_1_2("2e"),
		J2SE_1_3("2f"),
		J2SE_1_4("30"),
		J2SE_5_0("31"),
		JavaSE_6("32"),
		JavaSE_7("33"),
		JavaSE_8("34"),
		JavaSE_9("35"),
		JavaSE_10("36"),
		JavaSE_11("37"),
		JavaSE_12("38"),
		JavaSE_13("39"),
		JavaSE_14("3a"),
		JavaSE_15("3b"),
		JavaSE_16("3c"),
		JavaSE_17("3d"),
		JavaSE_18("3e")
		;
		
		private final String hex;
		private JavaVersion( String hex ) {
			this.hex = hex;
		}
		public String getHex() {
			return hex;
		}
		
		public static JavaVersion fromString( String hexString ) {
			JavaVersion results = null;
			
			if ( hexString != null && hexString.length() >= 2 ) {
				
				for ( JavaVersion jv : values() ) {
					if ( hexString.endsWith( jv.getHex() )) {
						results = jv;
						break;
					}
				}
			}
			
			return results;
		}
	}
	
	public PrisonJarReporter() {
		super();
		
		this.jars = new ArrayList<>();
	}
	
	public class JarFileData {

		private String pluginName;
		private String jarName;
		private String classHex;
		private boolean validJar = false;
		private JavaVersion javaVersion;
		
		public JarFileData( String jarName, String pluginName, String classHex, 
								boolean validJar, JavaVersion javaVersion ) {
			super();
			
			this.pluginName = pluginName;
			this.jarName = jarName;
			this.classHex = classHex;
			this.validJar = validJar;
			this.javaVersion = javaVersion;
		}

		public String toString() {
			return String.format( "%s  Jar: %s  %s  JavaVersion: %s  hex: %s", 
					getPluginName(),
					getJarName(), (isValidJar() ? "" : "(invalid)"),
					(getJavaVersion() == null ? "(none)" : getJavaVersion().name()), 
					getClassHex() );
		}
		
		public String getJarName() {
			return jarName;
		}
		public void setJarName( String jarName ) {
			this.jarName = jarName;
		}

		public String getPluginName() {
			return pluginName;
		}
		public void setPluginName( String pluginName ) {
			this.pluginName = pluginName;
		}

		public String getClassHex() {
			return classHex;
		}
		public void setClassHex( String classHex ) {
			this.classHex = classHex;
		}

		public boolean isValidJar() {
			return validJar;
		}
		public void setValidJar( boolean validJar ) {
			this.validJar = validJar;
		}

		public JavaVersion getJavaVersion() {
			return javaVersion;
		}
		public void setJavaVersion( JavaVersion javaVersion ) {
			this.javaVersion = javaVersion;
		}
		
	}

	protected class JarFileFilter 
		implements FilenameFilter {

		@Override
		public boolean accept( File dir, String name )
		{
			return name == null ? false : name.toLowerCase().endsWith( ".jar" );
		}
		
	}
	
	public void dumpJarDetails() {
		for ( JarFileData jfData : jars )
		{
			Output.get().logInfo( jfData.toString() );
		}
	}
	
	public void scanForJars() {
		File pluginDir = Prison.get().getDataFolder().getParentFile();
		
		File[] jars = pluginDir.listFiles( new JarFileFilter() );
		
		for ( File jar : jars ) {
			String name = jar.getName();
			
			try
			{
				ZipFile zipFile = new ZipFile( jar );
				
				String classHex = getClassHexFromJar( zipFile );
				
				boolean isValidJar = isValidJar( classHex );
				
				JavaVersion javaVersion = JavaVersion.fromString( classHex );
				
				String pluginName = getPluginNameFromJar( zipFile );
				
				JarFileData jfData = new JarFileData( name, pluginName, classHex, isValidJar, javaVersion );
				
				getJars().add( jfData );
				
//				Output.get().logInfo( jfData.toString() );
			}
			catch ( ZipException e )
			{
				e.printStackTrace();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
			
			
		}
		
	}


	private String getPluginNameFromJar( ZipFile zipFile )
	{
		String results = null;
		
		try {
			
			ZipEntry pluginYmlZipEntry = getPluginYamlZipEntry( zipFile );
			
			results = getJarNameFromZipEntry( zipFile, pluginYmlZipEntry );
			
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return results;
	}


	private String getClassHexFromJar( ZipFile zipFile ) {
		String results = null;
		
		try {
			
			ZipEntry classZipEntry = getClassZipEntry( zipFile );
			
			results = getHexFromZipEntry( zipFile, classZipEntry );
			
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	private ZipEntry getClassZipEntry( ZipFile zipFile ) {
		ZipEntry results = null;
	
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry ze = entries.nextElement();
			
			if ( ze.getName().toLowerCase().endsWith( ".class" ) ) {
				results = ze;
				break;
			}
		}

		return results;
	}
	private ZipEntry getPluginYamlZipEntry( ZipFile zipFile ) {
		ZipEntry results = null;
		
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry ze = entries.nextElement();
			
			if ( ze.getName().toLowerCase().equalsIgnoreCase( "plugin.yml" ) ) {
				results = ze;
				break;
			}
		}
		
		return results;
	}

	private String getHexFromZipEntry( ZipFile zipFile, ZipEntry classZipEntry ) {
		String results = null;
		
		if ( classZipEntry != null ) {
			
			byte[] buffer = new byte[8];
			
			try (
				BufferedInputStream bis = new BufferedInputStream( zipFile.getInputStream( classZipEntry ) );	
				) {
				
				int length = bis.read( buffer );
				
				if ( length > 0 ) {
					
					StringBuilder sb = new StringBuilder();
					
					for( int i = 0; i < buffer.length; ) {
						
						ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);
					    // by choosing big endian, high order bytes must be put
					    // to the buffer before low order bytes
					    byteBuffer.order(ByteOrder.BIG_ENDIAN);
					    // since ints are 4 bytes (32 bit), you need to put all 4, so put 0
					    // for the high order bytes
					    byteBuffer.put((byte)0x00);
					    byteBuffer.put((byte)0x00);
					    byteBuffer.put(buffer[i++]);
					    byteBuffer.put(buffer[i++]);
					    byteBuffer.flip();
					    int intVal = byteBuffer.getInt();
						
					    String hexLong = "0000" + Integer.toHexString( intVal );
						String hex = hexLong.substring( hexLong.length() - 4 );
						
						sb.append( hex ); //.append( " " );
					}
					
					
//					for (byte b : buffer) {
//						
//						String hexLong = Integer.toHexString( (int) b );
//						String hex = hexLong.substring( hexLong.length() - 2 );
//						
//						sb.append( hex ).append( " " );
////						sb.append(String.format("%02X ", _byte));
//						
//					}
					results = sb.toString();
				}
			}
			catch ( FileNotFoundException e ) {
				e.printStackTrace();
			}
			catch ( IOException e ) {
				e.printStackTrace();
			}
			
		}
		
		return results;
	}
		


	private String getJarNameFromZipEntry( ZipFile zipFile, ZipEntry pluginYmlZipEntry )
	{
		String results = null;
		
		if ( pluginYmlZipEntry != null ) {
			
			try (
					BufferedReader br = new BufferedReader( new InputStreamReader( 
								zipFile.getInputStream( pluginYmlZipEntry ) ));	
				) {
				
				String line = br.readLine();
				
				while ( line != null ) {
					
					if ( line.trim().startsWith( "name:" )) {
						results = line.replaceAll( "name:|\"", "" ).trim();
						break;
					}
					
					line = br.readLine();
				}

			}
			catch ( FileNotFoundException e ) {
				e.printStackTrace();
			}
			catch ( IOException e ) {
				e.printStackTrace();
			}
			
		}
		
		return results;
	}

	
	private boolean isValidJar( String jarHex ) {
		return jarHex != null && jarHex.startsWith( "cafebabe" );
	}

	public List<JarFileData> getJars() {
		return jars;
	}
	public void setJars( List<JarFileData> jars ) {
		this.jars = jars;
	}
	
	
}
