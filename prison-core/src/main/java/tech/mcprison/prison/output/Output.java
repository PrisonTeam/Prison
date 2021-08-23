/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.output;

import java.util.Arrays;
import java.util.HashSet;
import java.util.MissingFormatArgumentException;
import java.util.Set;
import java.util.TreeSet;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.CommandSender;

/**
 * Standardized output to the console and to players.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Output 
		extends OutputMessages {

    private static Output instance;
    
    private String prefixTemplate;
    
    private String prefixTemplatePrison;
    
    private String prefixTemplateInfo;
    private String prefixTemplateWarning;
    private String prefixTemplateError;
    private String prefixTemplateDebug;
    
    private String colorCodeInfo;
    private String colorCodeWarning;
    private String colorCodeError;
    private String colorCodeDebug;
    

    private boolean debug = false;
    private Set<DebugTarget> activeDebugTargets;

    public enum DebugTarget {
    	all,
    	on,
    	off,
    	blockBreak,
//    	blockBreakListeners,
    	blockBreakDurability, 
    	blockBreakFortune,
    	blockBreakXpCalcs,
    	
    	rankup,
    	support
    	;
    	
    	public static DebugTarget fromString( String target ) {
    		DebugTarget results = null;
    		
    		for ( DebugTarget t : values() ) {
				if ( t.name().equalsIgnoreCase( target ) ) {
					results = t;
				}
			}
    		
    		return results;
    	}
    	
    	public static TreeSet<DebugTarget> fromMultiString( String targets ) {
    		TreeSet<DebugTarget> results = new TreeSet<>();
    		
    		if ( targets != null && !targets.trim().isEmpty() ) {
    			
    			for ( String trgt : targets.split( " " ) ) {
    				DebugTarget t = fromString( trgt );
    				if ( t != null ) {
    					results.add( t );
    				}
    			}
    		}
    		
    		return results;
    	}
    }
    
    

    private Output() {
    	instance = this;

    	this.activeDebugTargets = new HashSet<>();
        
    	this.prefixTemplate = coreOutputPrefixTemplateMsg();

    	this.prefixTemplatePrison = gen( coreOutputPrefixTemplatePrisonMsg() );

    	this.prefixTemplateInfo = gen( coreOutputPrefixTemplateInfoMsg() );
    	this.prefixTemplateWarning = gen( coreOutputPrefixTemplateWarningMsg() );
    	this.prefixTemplateError = gen( coreOutputPrefixTemplateErrorMsg() );
    	this.prefixTemplateDebug = gen( coreOutputPrefixTemplateDebugMsg() );
        
    	this.colorCodeInfo = coreOutputColorCodeInfoMsg();
        this.colorCodeWarning = coreOutputColorCodeWarningMsg();
        this.colorCodeError = coreOutputColorCodeErrorMsg();
        this.colorCodeDebug = coreOutputColorCodeDebugMsg();
        
    }


    public static Output get() {
        if (instance == null) {
            new Output();
        }
        return instance;
    }

    
    /**
     * Need standardization on this.
     * 
     * @param level
     * @return
     */
    private String getLogPrefix( LogLevel level) {
        String prefix = null;
        
        switch ( level )
		{
			case INFO:
				prefix = prefixTemplateInfo;
				break;
			case WARNING:
				prefix = prefixTemplateWarning;
				break;
			case ERROR:
				prefix = prefixTemplateError;
				break;
			case DEBUG:
				prefix = prefixTemplateDebug;
				break;
				
			case PLAIN:
			default:
				prefix = "";
				break;
		}
        return getLogColorCode(level) + prefix;
    }
    
    private String getLogColorCode( LogLevel level) {
    	String colorCode = null;
    	
    	switch ( level )
    	{
    		case INFO:
    			colorCode = colorCodeInfo;
    			break;
    		case WARNING:
    			colorCode = colorCodeWarning;
    			break;
    		case ERROR:
    			colorCode = colorCodeError;
    			break;
    		case DEBUG:
    			colorCode = colorCodeDebug;
    			break;
    			
    		case PLAIN:
    		default:
    			colorCode = "";
    			break;
    	}
    	return colorCode;
    }
    
    public String format(String message, LogLevel level, Object... args) {
        return getLogPrefix(level) + String.format(message, args);
    }

    /**
     * Log a message with a specified {@link LogLevel}
     */
    public void log(String message, LogLevel level, Object... args) {
    	if ( Prison.get() == null || Prison.get().getPlatform() == null ) {
    		String errorMessage = coreOutputErrorStartupFailureMsg();
    		if ( errorMessage == null || errorMessage.trim().isEmpty() ) {
    			// NOTE: The following must remain as is.  This is a fallback for if there
    			// are major failures in prison.  At least it can prefix the messages so they
    			// can be identified along with the reasons.
    			errorMessage = "Prison: (Sending to System.err due to Output.log Logger failure):";
    		}
    		System.err.println( errorMessage + " " + message );
    	} else {
    		try {
				Prison.get().getPlatform().log(
						prefixTemplatePrison + " " + 
						getLogColorCode(level) +
						String.format(message, args));
			}
			catch ( MissingFormatArgumentException e )
			{
				StringBuilder sb = new StringBuilder();
				
				for ( Object arg : args ) {
					sb.append( "[" ).append( arg ).append( "] " );
				}
				
				String errorMessage = coreOutputErrorIncorrectNumberOfParametersMsg(
						level.name(), e.getMessage(), message, sb.toString() );
				
				Prison.get().getPlatform().logCore(
						prefixTemplatePrison + " " + 
						getLogColorCode(LogLevel.ERROR) +
						errorMessage );
			}
    	}
    }

    /**
     * Log an informational message to the console.
     *
     * @param message The informational message. May include color codes, but the default is white.
     */
    public void logInfo(String message, Object... args) {
        log(message, LogLevel.INFO, args);
    }

    /**
     * Log a warning to the console.
     *
     * @param message   The message describing the warning. May include color codes, but the default is
     *                  orange.
     * @param throwable The exceptions thrown, if any.
     */
    public void logWarn(String message, Throwable... throwable) {
        log(message, LogLevel.WARNING);

        if (throwable.length > 0) {
            Arrays.stream(throwable).forEach(Throwable::printStackTrace);
        }
    }

    /**
     * Log an error to the console.
     *
     * @param message   The message describing the error. May include color codes, but the default is
     *                  red.
     * @param throwable The exceptions thrown, if any.
     */
    public void logError(String message, Throwable... throwable) {
        log(message, LogLevel.ERROR);

        if (throwable.length > 0) {
            Arrays.stream(throwable).forEach(Throwable::printStackTrace);
        }
    }
    
    public void logDebug(String message, Object... args) {
    	if ( isDebug() ) {
    		log(message, LogLevel.DEBUG, args);
    	}
    }
    
    public void logDebug( DebugTarget debugTarget, String message, Object... args) {
    	
    	if ( isDebug( debugTarget ) ) {
    		
    		log(message, LogLevel.DEBUG, args);
//    		logDebug(message, args );
    	}
    	
//    	// The following is not yet enabled since the user interfaces are not in place to manage the set:
//    	if ( isDebug() && debugType != null && getActiveDebugTypes().contains( debugType ) ) {
//    		log(message, LogLevel.DEBUG, args);
//    	}
    }
    
    public String getDebugTargetsString() {
    	StringBuilder sb = new StringBuilder();
    	
    	for ( DebugTarget target : DebugTarget.values() ) {
			if ( sb.length() > 0 ) {
				sb.append( ", " );
			}
    		sb.append( target.name() );
		}
    	
    	return sb.toString();
    }
    
    public void applyDebugTargets( String targets ) {
    	
    	TreeSet<DebugTarget> trgts = DebugTarget.fromMultiString( targets );
    	
    	if ( trgts.size() > 0 ) {
    		applyDebugTargets( trgts );
    	}
    	else {
    		// No targets were set, so just toggle the debugger:
    		Output.get().setDebug( !Output.get().isDebug() );

    		// Clear all existing targets:
    		getActiveDebugTargets().clear();
    	}
    }
    
    public void applyDebugTargets( TreeSet<DebugTarget> targets ) {
    	
    	boolean onTarget = targets.contains( DebugTarget.on );
    	
    	// offTarget cannot be set if onTarget is on:
    	boolean offTarget = !onTarget && targets.contains( DebugTarget.off );
    	
    	targets.remove( DebugTarget.on );
    	targets.remove( DebugTarget.off );

    	for ( DebugTarget target : targets ) {
			
    		if ( onTarget ) {
    			getActiveDebugTargets().add( target );
    		}
    		else if ( offTarget ) {
    			getActiveDebugTargets().remove( target );
    		}
    		else {
    			// Toggle the settings:
    			boolean hasIt = getActiveDebugTargets().contains( target );
    			if ( hasIt ) {
    				getActiveDebugTargets().remove( target );
    			}
    			else {
    				getActiveDebugTargets().add( target );
    			}
    		}
    		
		}

    	// No global changes here:
    	// Output.get().setDebug( !Output.get().isDebug() );
    }
    
    public boolean isDebug( DebugTarget debugTarget ) {
    	return isDebug() || getActiveDebugTargets().contains( debugTarget );
    }
    public boolean isDebug() {
		return debug;
	}
	public void setDebug( boolean debug ) {
		this.debug = debug;
	}


	public Set<DebugTarget> getActiveDebugTargets() {
		return activeDebugTargets;
	}
	public void setActiveDebugTargets( Set<DebugTarget> activeDebugTargets ) {
		this.activeDebugTargets = activeDebugTargets;
	}

	/**
     * Send a message to a {@link CommandSender}
     */
    public void sendMessage(CommandSender sender, String message, LogLevel level, Object... args) {
        sender.sendMessage(getLogPrefix(level) + String.format(message, args));
    }
    
    public void send(CommandSender sender, String message, Object... args) {
    	sendMessage(sender, message, LogLevel.PLAIN, args);
    }

    /**
     * Send information to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is grey.
     */
    public void sendInfo(CommandSender sender, String message, Object... args) {
    	sendMessage(sender, message, LogLevel.INFO, args);
    }

    /**
     * Send a warning to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is grey.
     */
    public void sendWarn(CommandSender sender, String message, Object... args) {
    	sendMessage(sender, message, LogLevel.WARNING, args);
    }

    /**
     * Send an error to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is grey.
     */
    public void sendError(CommandSender sender, String message, Object... args) {
    	sendMessage(sender, message, LogLevel.ERROR, args);
    }

    // Private methods

    private String gen(String name) {
        return String.format(prefixTemplate, name);
    }

}
