/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.prison.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.prison.Prison;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Camouflage100
 */
public class Patrons {
    public static Patrons instance;
    private List<String> patrons;

    public Patrons() {
        instance = this;
        patrons = new ArrayList<>();
        getPatrons();
    }

    private String readPage(Reader rd) {
        StringBuilder sb = new StringBuilder();
        int cp;
        try {
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void getPatrons() {
        try {
            // Needs a user agent since this is HTTPS
            URL website = new URL("https://mc-prison.tech/api/patrons.php");
            HttpURLConnection con = (HttpURLConnection) (website.openConnection());
            System.setProperty("http.agent", "");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");

            try (InputStream is = con.getInputStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

                Type type = new TypeToken<List<String>>() {
                }.getType();

                patrons = new Gson().fromJson(readPage(rd), type);
            }
        } catch (Exception e) {
            Prison.getInstance().getPlatform().log("&cError connecting to %s: %s", "https://mc-prison.tech/api/patrons.php", e.getMessage());
        }
    }

    public static List<String> get() {
        return instance.patrons;
    }
}
