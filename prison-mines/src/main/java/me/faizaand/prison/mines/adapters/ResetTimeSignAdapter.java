package me.faizaand.prison.mines.adapters;

import me.faizaand.prison.internal.block.GameSign;
import me.faizaand.prison.mines.PrisonMines;
import me.faizaand.prison.mines.data.Mine;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.signs.DisplaySign;
import me.faizaand.prison.signs.DisplaySignAdapter;
import me.faizaand.prison.util.Text;

import java.util.Optional;

public class ResetTimeSignAdapter extends DisplaySignAdapter {

    public ResetTimeSignAdapter() {
        super("mine reset time", 1);
    }

    @Override
    public void refreshSigns() {
        for (DisplaySign sign : getSigns()) {
            if (sign.getParams().length == 0) {
                Output.get().logError("sign @ " + sign.getLocation().toBlockCoordinates() + " needs to specify a mine name on its third line.");
                continue; //skip this one
            }
            String mineName = sign.getParams()[0];
            Optional<Mine> mineOpt = PrisonMines.getInstance().getMineManager().getMine(mineName);
            if (!mineOpt.isPresent()) {
                Output.get().logError("sign @ " + sign.getLocation().toBlockCoordinates() + " has specified a mine that doesn't exist!");
                continue; //skip this one
            }

            // todo mines should be able to have individual reset times
            int resetCount = PrisonMines.getInstance().getMineManager().resetCount;
            GameSign state = (GameSign) sign.getLocation().getBlockAt().getState();

            state.setLine(0, Text.translateAmpColorCodes("&3Mine: &a" + mineOpt.get().getName()));
            state.setLine(1, "Resetting in");

            // todo either keep numbers on same line or just cut it off at the comma or and

            String timeUntil = Text.getTimeUntilString(resetCount * 1000);
            if (timeUntil.length() > 15) {
                state.setLine(2, Text.translateAmpColorCodes("&c" + timeUntil.substring(0, 15)));
                state.setLine(3, Text.translateAmpColorCodes("&c" + timeUntil.substring(15)));
            } else {
                state.setLine(2, Text.translateAmpColorCodes("&c" + timeUntil));
            }

        }
    }
}
