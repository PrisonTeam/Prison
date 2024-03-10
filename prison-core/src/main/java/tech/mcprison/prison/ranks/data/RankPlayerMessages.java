package tech.mcprison.prison.ranks.data;

import tech.mcprison.prison.Prison;

public class RankPlayerMessages {

	
	protected String coreOutputErrorIncorrectNumberOfParametersMsg ( 
			String levelName, String errorMessage, 
			String originalMessage, String arguments ) {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__error_incorrect_number_of_parameters" )
				.withReplacements( levelName, errorMessage, 
							originalMessage, arguments )
				.localize();
	}
	
	
	protected static String coreTopNLine1HeaderMsg() {
		String header = Prison.get().getLocaleManager()
				.getLocalizable( "core_ranks_topn__player_line_1_header_format" )
				.localize();
		header = header.replace( "[", "%" ).replace( "]", "s" );
		
		String[] values = coreTopNLine1HeaderValuesMsg();
		
		String results = String.format(
				header,
				(Object[]) values
				);
		return results;
	}
	
	private static String[] coreTopNLine1HeaderValuesMsg () {
		String[] results = null;
		String values = Prison.get().getLocaleManager()
				.getLocalizable( "core_ranks_topn__player_line_1_header_values" )
				.localize();
		results = values.split(", ");
		
		return results;
	}
	
	
	
	protected static String coreTopNLine2HeaderMsg() {
		String header = Prison.get().getLocaleManager()
				.getLocalizable( "core_ranks_topn__player_line_2_header_format" )
				.localize();
		header = header.replace( "[", "%" ).replace( "]", "s" );
		
		String[] values = coreTopNLine2HeaderValuesMsg();
		
		String results = String.format(
				header,
				(Object[]) values
				);
		return results;
	}
	
	private static String[] coreTopNLine2HeaderValuesMsg () {
		String[] results = null;
		String values = Prison.get().getLocaleManager()
				.getLocalizable( "core_ranks_topn__player_line_2_header_values" )
				.localize();
		results = values.split(", ");
		
		return results;
	}
	
	
	protected static String coreTopNLine1DetailMsg(
			String playerName,
			String rankPositionStr,
			String rankScoreStr, String sPenaltyStr,
			String prestigeRankName, String defaultRankName, 
			String prestRankTag, String defRankTag, 
			String prestRankTagNc, String defRankTagNc, 
			String balanceFmtStr, String balanceKmbtStr, String balanceMetricStr) {
		
		String detail = Prison.get().getLocaleManager()
				.getLocalizable( "core_ranks_topn__player_line_1_detail_format" )
				.localize();
		detail = detail.replace( "[", "%" ).replace( "]", "s" );
		
		String[] values = coreTopNLine1DetailValuesMsg(
				playerName,
				rankPositionStr,
				rankScoreStr,
				sPenaltyStr,
				prestigeRankName, 
				defaultRankName, 
				prestRankTag,
				defRankTag,
				prestRankTagNc,
				defRankTagNc,
				balanceFmtStr,
				balanceKmbtStr,
				balanceMetricStr
				);
		
		String results = String.format(
				detail,
				(Object[]) values
				);
		return results;
	}
	
	private static String[] coreTopNLine1DetailValuesMsg(
			String playerName,
			String rankPositionStr,
			String rankScoreStr, String sPenaltyStr,
			String prestigeRankName, String defaultRankName, 
			String prestRankTag, String defRankTag, 
			String prestRankTagNc, String defRankTagNc, 
			String balanceFmtStr, String balanceKmbtStr, String balanceMetricStr
			) {
		
		String[] results = null;
		
		String values = Prison.get().getLocaleManager()
				.getLocalizable( "core_ranks_topn__player_line_1_detail_values" )
				.localize();
		
		String[] tmp = values.split(",");
		results = new String[tmp.length];
		
		for ( int i = 0; i < tmp.length; i++ ) {
			String value = tmp[i];
			
			results[i] = value
					.replace( "{playerName}", playerName )
					.replace( "{rankPosition}", rankPositionStr )
					.replace( "{rankScore}", rankScoreStr )
					.replace( "{rankScorePenalty}", sPenaltyStr )
					
					.replace( "{prestigeRank}", prestigeRankName )
					.replace( "{defaultRank}", defaultRankName )
					.replace( "{prestigeDefaultRank}", prestigeRankName + defaultRankName )
					
					.replace( "{prestigeRankTag}", prestRankTag + "&r" )
					.replace( "{defaultRankTag}", defRankTag + "&r" )
					.replace( "{prestigeDefaultRankTag}", prestRankTag + defRankTag + "&r" )
					
					.replace( "{prestigeRankTagNoColor}", prestRankTagNc )
					.replace( "{defaultRankTagNoColor}", defRankTagNc )
					.replace( "{prestigeDefaultRankTagNoColor}", prestRankTagNc + defRankTagNc )
					
					.replace( "{balanceFmt}", balanceFmtStr )
					.replace( "{balanceKmbt}", balanceKmbtStr )
					.replace( "{balanceMetric}", balanceMetricStr )
					.trim();
			
		}
		
		return results;
	}
	
	protected static String coreTopNLine2DetailMsg(
			String playerName,
			String rankPositionStr,
			String rankScoreStr, String sPenaltyStr,
			String prestigeRankName, String defaultRankName, 
			String prestRankTag, String defRankTag, 
			String prestRankTagNc, String defRankTagNc, 
			String balanceFmtStr, String balanceKmbtStr, String balanceMetricStr) {
		
		String detail = Prison.get().getLocaleManager()
				.getLocalizable( "core_ranks_topn__player_line_2_detail_format" )
				.localize();
		detail = detail.replace( "[", "%" ).replace( "]", "s" );
		
		String[] values = coreTopNLine2DetailValuesMsg(
				playerName,
				rankPositionStr,
				rankScoreStr,
				sPenaltyStr,
				prestigeRankName, 
				defaultRankName, 
				prestRankTag,
				defRankTag,
				prestRankTagNc,
				defRankTagNc,
				balanceFmtStr,
				balanceKmbtStr,
				balanceMetricStr
				);
		
		String results = String.format(
				detail,
				(Object[]) values
				);
		return results;
	}
	
	private static String[] coreTopNLine2DetailValuesMsg(
			String playerName,
			String rankPositionStr,
			String rankScoreStr, String sPenaltyStr,
			String prestigeRankName, String defaultRankName, 
			String prestRankTag, String defRankTag, 
			String prestRankTagNc, String defRankTagNc, 
			String balanceFmtStr, String balanceKmbtStr, String balanceMetricStr
			) {
		
		String[] results = null;
		
		String values = Prison.get().getLocaleManager()
				.getLocalizable( "core_ranks_topn__player_line_2_detail_values" )
				.localize();
		
		String[] tmp = values.split(",");
		results = new String[tmp.length];
		
		for ( int i = 0; i < tmp.length; i++ ) {
			String value = tmp[i];
			
			
			results[i] = value
				.replace( "{playerName}", playerName )
				.replace( "{rankPosition}", rankPositionStr )
				.replace( "{rankScore}", rankScoreStr )
				.replace( "{rankScorePenalty}", sPenaltyStr )
				
				.replace( "{prestigeRank}", prestigeRankName )
				.replace( "{defaultRank}", defaultRankName )
				.replace( "{prestigeDefaultRank}", prestigeRankName + defaultRankName )
				
				.replace( "{prestigeRankTag}", prestRankTag + "&r" )
				.replace( "{defaultRankTag}", defRankTag + "&r" )
				.replace( "{prestigeDefaultRankTag}", prestRankTag + defRankTag + "&r" )
				
				.replace( "{prestigeRankTagNoColor}", prestRankTagNc )
				.replace( "{defaultRankTagNoColor}", defRankTagNc )
				.replace( "{prestigeDefaultRankTagNoColor}", prestRankTagNc + defRankTagNc )
				
				.replace( "{balanceFmt}", balanceFmtStr )
				.replace( "{balanceKmbt}", balanceKmbtStr )
				.replace( "{balanceMetric}", balanceMetricStr )
				.trim()
				;
			
		}
		
		return results;
	}
	
}
