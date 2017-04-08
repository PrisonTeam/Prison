package tech.mcprison.prison.convert;

/**
 * A conversion agent contains a single method - convert() - which
 * can access any file to convert as necessary.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface ConversionAgent {

    /**
     * Run the conversion.
     *
     * @return The {@link ConversionResult}.
     */
    ConversionResult convert();

    /**
     * Returns a name which describes this conversion, used for internal and UI purposes.
     *
     * @return The (preferably short) conversion name.
     */
    String getName();

}
