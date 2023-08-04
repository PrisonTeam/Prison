package tech.mcprison.prison.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ExampleJavaDoubleVsBigDecimal {
	
	public static void main( String[] args ) {
		
		ExampleJavaDoubleVsBigDecimal app = new ExampleJavaDoubleVsBigDecimal();
		
		ArrayList<String> out = app.runSample();
		
		String header = String.format( 
				"%s %30s  %35s  %16s  %s",
				"Int Digits   ", 
				" double  ",
				" BigDecimal  ",
				"Delta  ",
				"Row" );
		
		System.out.println( header );

		for (String line : out) {
			System.out.println( line );
		}
	}

	
	private ArrayList<String> runSample() {
		ArrayList<String> out = new ArrayList<>();
		
		StringBuilder sb = new StringBuilder();
		sb.append( ".111111" );
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0.00000" );
		
		for ( int i = 1; i < 25; i++ ) {
			sb.insert( 0, "1" );
			
			double dub = Double.parseDouble( sb.toString() );
			BigDecimal bigD = new BigDecimal( sb.toString() );
			
			BigDecimal delta = bigD.subtract( BigDecimal.valueOf(dub) );
			
			String rpt = String.format( 
					"%2d  %40s  %35s  %16s  %2d",
					i, 
					dFmt.format( dub ), 
					bigD.toString(), delta.toString(),
					i
					);
			
			out.add( rpt );
			
		}
		
		return out;
	}
}
