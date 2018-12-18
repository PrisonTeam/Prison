/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.error;

import org.apache.commons.lang3.StringUtils;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.PluginEntity;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A manager for creating more comprehensive and helpful error messages for users.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ErrorManager {

    private static final String HEADER = Text.titleize("Begin Error");
    private static final String FOOTER = Text.titleize("End Error");
    private PluginEntity owner;
    private File errorDir;

    public ErrorManager(PluginEntity owner) {
        this.owner = owner;
        this.errorDir = new File(owner.getDataFolder(), "errors");
        if (!this.errorDir.exists()) {
            this.errorDir.mkdir();
        }
    }

    public void throwError(Error err) {
        List<String> lines = new ArrayList<>();
        addWithLineSep("\n\n" + HEADER, lines);

        addWithLineSep("An error has occurred within " + owner.getName() + ".", lines);
        addWithLineSep("Description: " + err.getDescription(), lines);

        int stackTraceNumber = 0;
        for (ErrorStackTrace stackTrace : err.getStackTraces()) {
            stackTraceNumber++;
            addWithLineSep("==> Stack Trace #" + stackTraceNumber, lines);
            addWithLineSep(stackTrace.toString(), lines);
            addWithLineSep("", lines);
        }

        addWithLineSep(FOOTER, lines);

        Output.get().logError("An error has occurred.");

        Prison.get().getPlatform().log(StringUtils.join(lines, ""));

        Output.get().logInfo("Attempting to write error file...");
        createFile(lines);

        Output.get().logInfo(
            "Please report this error to the developer, at http://github.com/MC-Prison/Prison/issues.\n\n");
    }

    private void createFile(List<String> lines) {
        try {
            String name = "error_" + getDateTime() + ".txt";
            File dumpFile = new File(errorDir, name);
            if (!dumpFile.exists()) {
                dumpFile.createNewFile();
            }

            Files.write(dumpFile.toPath(), lines);
            Output.get().logInfo("Created error file dump Prison/errors/" + name + ".");

        } catch (IOException e) {
            e.printStackTrace();
            Output.get()
                .logInfo("Could not write error file. Copy-paste the above error block instead.");
        }
    }

    private String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        df.setTimeZone(TimeZone.getDefault());
        return df.format(new Date());
    }

    private void addWithLineSep(String line, List<String> lines) {
        lines.add(line + System.lineSeparator());
    }

}
