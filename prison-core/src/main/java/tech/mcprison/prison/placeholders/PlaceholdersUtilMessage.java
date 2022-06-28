package tech.mcprison.prison.placeholders;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;

public class PlaceholdersUtilMessage {
	protected static List<String> coreOutputTextTimeUnitsShortArray() {
		List<String> timeUnits = new ArrayList<>();
		
		String unitsStr = Prison.get().getLocaleManager()
				.getLocalizable( "core_text__time_units_short" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
		String[] units = unitsStr.split(",");
		
		for (String unit : units) {
			timeUnits.add( unit.trim() );
		}
		
		return timeUnits;
	}

}
