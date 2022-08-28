package tech.mcprison.prison.util;

import tech.mcprison.prison.Prison;

public class TextMessage
{

//  core_text__prefix=&3
//	core_text__just_now=just now
//	core_text__ago=ago
//	core_text__from_now=from now
//	core_text__and=and
//	core_text__time_units_singular=year,month,week,day,hour,minute,second
//	core_text__time_units_plural=years,months,weeks,days,hours,minutes,seconds

	
	protected static String coreOutputTextJustPrefixMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_text__prefix" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
			
	protected static String coreOutputTextJustNowMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_text__just_now" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
	
	protected static String coreOutputTextAgoMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_text__ago" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
	
	protected static String coreOutputTextFromNowMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_text__from_now" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
	
	protected static String coreOutputTextAndMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_text__and" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
	
	protected static String coreOutputUnitPrefixSpacer() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_text__time_units_prefix_spacer" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
	
	protected static String coreOutputTextTimeUnitsSingularMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_text__time_units_singular" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
	
	protected static String coreOutputTextTimeUnitsPluralMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_text__time_units_plural" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
	
}
