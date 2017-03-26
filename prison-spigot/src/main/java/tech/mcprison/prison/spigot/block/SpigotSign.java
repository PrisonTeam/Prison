package tech.mcprison.prison.spigot.block;

import com.google.common.collect.ImmutableList;
import tech.mcprison.prison.internal.block.Sign;

import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotSign extends SpigotBlockState implements Sign {

    public SpigotSign(SpigotBlock block) {
        super(block);
    }

    @Override public List<String> getLines() {
        org.bukkit.block.Sign bSign = (org.bukkit.block.Sign) block.getWrapper().getState();
        return ImmutableList.copyOf(bSign.getLines());
    }

    @Override public void setLines(List<String> lines) {
        for (int i = 0; i < Math.max(lines.size(), 4); i++) {
            setLine(i, lines.get(i));
        }
    }

    @Override public void setLine(int line, String text) {
        org.bukkit.block.Sign bSign = (org.bukkit.block.Sign) block.getWrapper().getState();
        bSign.setLine(line, text);
        bSign.update();
    }

}
