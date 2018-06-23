package me.faizaand.prison.signs;

import java.util.ArrayList;
import java.util.List;

public class DisplaySignManager {

    private static DisplaySignManager instance;

    public static DisplaySignManager getInstance() {
        if (instance == null) instance = new DisplaySignManager();
        return instance;
    }

    private List<DisplaySignFiller> fillers;

    public DisplaySignManager() {
        this.fillers = new ArrayList<>();
    }

    public List<DisplaySignFiller> getFillers() {
        return fillers;
    }

}
