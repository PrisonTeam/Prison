package tech.mcprison.prison.output;

import tech.mcprison.prison.Prison;

public class OutputMessages
{
	protected String coreOutputPrefixTemplateMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__prefix_template" )
				.withReplacements( "%s" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputPrefixTemplatePrisonMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__prefix_template_prison" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputPrefixTemplateInfoMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__prefix_template_info" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputPrefixTemplateWarningMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__prefix_template_warning" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputPrefixTemplateErrorMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__prefix_template_error" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputPrefixTemplateDebugMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__prefix_template_debug" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputColorCodeInfoMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__color_code_info" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputColorCodeWarningMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__color_code_warning" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputColorCodeErrorMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__color_code_error" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputColorCodeDebugMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__color_code_debug" )
				.setFailSilently()
				.localize();
	}
	
	protected String coreOutputErrorStartupFailureMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__error_startup_failure" )
				.localize();
	}
	
	protected String coreOutputErrorIncorrectNumberOfParametersMsg( 
					String levelName, String errorMessage, 
					String originalMessage, String arguments ) {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_output__error_incorrect_number_of_parameters" )
				.withReplacements( levelName, errorMessage, 
							originalMessage, arguments )
				.localize();
	}
	
}
