package tech.mcprison.prison.sellall;

public class PrisonSellallUtilsMessages
{
	// This does nothing. Used as an example until real messages can be added.
	protected String prisonSellallTest01Msg() {
		
		return PrisonSellall.getInstance().getSellallMessages()
    			.getLocalizable( "sellall_test__sample_01" )
    			.localize();
	}
	
	// This does nothing. Used as an example until real messages can be added.
	protected String prisonSellallTest02Msg( String parameter ) {
		
		return PrisonSellall.getInstance().getSellallMessages()
				.getLocalizable( "sellall_test__sample_02" )
				.withReplacements( parameter )
				.localize();
	}
	
	protected String prisonSellallTest03Msg() {
		
		String msg01 = prisonSellallTest01Msg();
		String msg02 = prisonSellallTest02Msg( "sample02" );
		
		return PrisonSellall.getInstance().getSellallMessages()
				.getLocalizable( "sellall_test__sample_03" )
				.withReplacements( 
						msg01,
						msg02 )
				.localize();
		
	}
}
