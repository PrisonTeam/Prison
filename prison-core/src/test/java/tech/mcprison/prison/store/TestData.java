/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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

package tech.mcprison.prison.store;

import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class TestData {

    public String playerName;
    public Location location;
    public String nickname;

    public TestData(String playerName, Location location, String nickname) {
        this.playerName = playerName;
        this.location = location;
        this.nickname = nickname;
    }

    public TestData() {
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestData)) {
            return false;
        }

        TestData testData = (TestData) o;

        if (playerName != null ?
            !playerName.equals(testData.playerName) :
            testData.playerName != null) {
            return false;
        }
        if (location != null ? !location.equals(testData.location) : testData.location != null) {
            return false;
        }
        return nickname != null ? nickname.equals(testData.nickname) : testData.nickname == null;
    }

    @Override public int hashCode() {
        int result = playerName != null ? playerName.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "TestData{" + "playerName='" + playerName + '\'' + ", location=" + location
            + ", nickname='" + nickname + '\'' + '}';
    }
}
