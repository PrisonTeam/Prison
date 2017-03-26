package tech.mcprison.prison.internal.block;

import java.util.List;

/**
 * Represents a sign.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface Sign extends BlockState {

    List<String> getLines();

    void setLines(List<String> lines);

    void setLine(int line, String text);

}
