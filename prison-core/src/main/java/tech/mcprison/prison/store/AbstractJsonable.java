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

package tech.mcprison.prison.store;

import com.google.common.reflect.TypeToken;
import tech.mcprison.prison.Prison;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A basic implementation of {@link Jsonable} for general beans.
 *
 * @author Faizaan A. Datoo
 * @since API 30
 */
public abstract class AbstractJsonable<T extends AbstractJsonable<T>> implements Jsonable<T> {

    @Override public T fromJson(String json) {
        return Prison.get().getGson().fromJson(json, TypeToken.of(getClass()).getType());
    }

    @Override public T fromFile(File file) throws IOException {
        String json = new String(Files.readAllBytes(file.toPath()));
        return fromJson(json);
    }

    @Override public String toJson() {
        return Prison.get().getGson().toJson(this, getClass());
    }

    @Override public void toFile(File file) throws IOException {
        Files.write(file.toPath(), toJson().getBytes());
    }

}
