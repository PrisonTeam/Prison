/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.util;

/**
 * <p>All of the old blocks in the game. This list is obsolete, and was used in
 * v3.2.0 and earlier.  It because obsolete with the new block model which was 
 * introduced when incorporating XSeries' XMaterial.  
 * </p>
 * 
 * <p>The only reasonto keep this enumeration is for conversion processes 
 * to convert to the new block model.  This enum "should" be deleted, but 
 * for the sake of conversioin use only makes ths valuable.  Where it excels, 
 * is that if an old block name cannot be auto mapped to the XMaterial, then
 * this contains the mapping hints to ensure it works correctly.
 * </p>
 * 
 * <p>The new field altNames contains a list of String values that will help
 * XMaterial map these types to valid Material types.  These are spigot version
 * Dependent, so some blocks may have different altNames for different spigot 
 * versions.
 * </p>
 * 
 * <p>XMaterial support for spigot 1.8.8:
 * 		MOSS_STONE = mossy_cobblestone,  LAPIS_LAZULI_ORE = "lapis_ore",  
 * 		LAPIS_LAZULI_BLOCK = "lapis_block",  PILLAR_QUARTZ_BLOCK = "quartz_pillar"
 *  </p>
 *
 * @author Faizaan A. Datoo
 * @author Camouflage100
 * @since API 1.0
 * @deprecated since v3.2.6
 */
@Deprecated
public enum ObsoleteBlockType {

	// NOTE: This obsolete source has been purged.  See the history in git.
	

}
