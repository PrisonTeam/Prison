package tech.mcprison.prison.placeholders;

import java.util.List;

public interface ManagerPlaceholders {
	
    public List<PlaceHolderKey> getTranslatedPlaceHolderKeys();

	public void reloadPlaceholders();
}
