package tech.mcprison.prison.integration;

import java.util.List;

public interface ManagerPlaceholders {
	
    public List<PlaceHolderKey> getTranslatedPlaceHolderKeys();

	public void reloadPlaceholders();
}
