package tech.mcprison.prison.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ExampleJavaDoubleVsBigDecimal {
	
	public static void main( String[] args ) {
		
		ExampleJavaDoubleVsBigDecimal app = new ExampleJavaDoubleVsBigDecimal();
		
		ArrayList<String> out = app.runSample();
		
		String header = String.format( 
				"%s %45s  %3s  %55s %3s  %35s %s",
				"Int Digits   ", 
				" double  ",
				"Row",
				" BigDecimal  ",
				"Row",
				"Delta - loss of double precision ",
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
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0.000000" );
//		DecimalFormat iFmt = new DecimalFormat( "#,##0.00000" );
		
		for ( int i = 1; i < 35; i++ ) {
			sb.insert( 0, "1" );
			
			double dub = Double.parseDouble( sb.toString() );
			BigDecimal bigD = new BigDecimal( sb.toString() );
			
			BigDecimal delta = bigD.subtract( BigDecimal.valueOf(dub) );
			
			String rpt = String.format( 
					"%3d %55s  %3d %55s  %3d %35s  %3d",
					i, 
					dFmt.format( dub ), 
					i, 
					dFmt.format( bigD ), 
					i, 
					dFmt.format( delta ),
					i					);
			
			out.add( rpt );
			
		}
		
		return out;
	}
}
