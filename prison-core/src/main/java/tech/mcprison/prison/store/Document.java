package tech.mcprison.prison.store;

import java.util.HashMap;

import tech.mcprison.prison.file.FileIOData;

/**
 * Represents a single document in the storage system.
 * Documents are part of collections and are meant to represent single objects of data.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public class Document 
	extends HashMap<String, Object> 
	implements FileIOData
{
	private static final long serialVersionUID = 1L;
}
