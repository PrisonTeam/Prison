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

import java.util.List;

/**
 * A wrapper for any object that is being stored. Contains
 * the string representation of the object in the form of a GSON-serialized string.
 * This also contains convenience methods for easy class-casting.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public class Data {

  private Object obj;

  public Data(Object obj) {
    this.obj = obj;
  }

  public Object get() {
    return obj;
  }

  public void set(Object obj) {
    this.obj = obj;
  }

  public void empty() {
    set(null);
  }

  public <T> T as(Class<T> type) {
    return type.cast(obj);
  }

  public int asInt() {
    return as(Integer.class);
  }

  public float asFloat() {
    return as(Float.class);
  }

  public boolean asBoolean() {
    return as(Boolean.class);
  }

  public double asDouble() {
    return as(Double.class);
  }

  public byte asByte() {
    return as(Byte.class);
  }

  public String asString() {
    return as(String.class);
  }

  public String toJson() {
    return GsonSingleton.getInstance().getGson().toJson(obj);
  }

  public <T> List<T> asList(Class<T> listType) {
    return (List<T>) get();
  }

}
