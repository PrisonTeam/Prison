package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.internal.ItemStack;

public class BlockConverterResults {
	
	private String sourceBlockName;
	private int sourceBlockQuantity;
	
	// Results:
	private boolean resultsSuccess;
	
	private List<ItemStack> resultsItemStack;
	private int resultsQuantityConsumed;
	
	public BlockConverterResults( String blockName, int blockQuanty ) {
		super();
		
		this.sourceBlockName = blockName;
		this.sourceBlockQuantity = blockQuanty;
		
		this.resultsSuccess = false;
		this.resultsItemStack = new ArrayList<>();
		this.resultsQuantityConsumed = -1;
	}

	public String getSourceBlockName() {
		return sourceBlockName;
	}
	public void setSourceBlockName(String sourceBlockName) {
		this.sourceBlockName = sourceBlockName;
	}

	public int getSourceBlockQuantity() {
		return sourceBlockQuantity;
	}
	public void setSourceBlockQuantity(int sourceBlockQuantity) {
		this.sourceBlockQuantity = sourceBlockQuantity;
	}

	public boolean isResultsSuccess() {
		return resultsSuccess;
	}
	public void setResultsSuccess(boolean resultsSuccess) {
		this.resultsSuccess = resultsSuccess;
	}

	public List<ItemStack> getResultsItemStack() {
		return resultsItemStack;
	}
	public void setResultsItemStack(List<ItemStack> resultsItemStack) {
		this.resultsItemStack = resultsItemStack;
	}

	public int getResultsQuantityConsumed() {
		return resultsQuantityConsumed;
	}
	public void setResultsQuantityConsumed(int resultsQuantityConsumed) {
		this.resultsQuantityConsumed = resultsQuantityConsumed;
	}

}
