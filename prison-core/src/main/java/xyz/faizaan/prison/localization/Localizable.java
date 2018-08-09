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

package xyz.faizaan.prison.localization;

import com.google.common.collect.Lists;
import xyz.faizaan.prison.Prison;
import xyz.faizaan.prison.internal.CommandSender;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.internal.World;
import xyz.faizaan.prison.output.LogLevel;
import xyz.faizaan.prison.output.Output;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * Represents an object which has the potential to be localized in one of
 * multiple languages and returned as a string.
 * <p>
 * <p>In the event that a {@link Localizable} cannot be localized with the given
 * parameters or in its parent {@link LocaleManager}'s default locale, it will
 * output its internal key instead.</p>
 * Adapted for Prison.
 *
 * @author Max Roncace
 * @author Faizaan A. Datoo
 * @version 1.1.1
 * @since 1.0
 */
public class Localizable {

    private final LocaleManager parent;
    private final String key;

    private String[] replacements = new String[0];
    private Localizable[] locReplacements = new Localizable[0];
    private String prefix = "";
    private String suffix = "";

    Localizable(LocaleManager parent, String key) {
        this.parent = parent;
        this.key = key;
    }

    /**
     * Gets the parent {@link LocaleManager} for this {@link Localizable}.
     *
     * @return The parent {@link LocaleManager} for this {@link Localizable}.
     * @since 1.0
     */
    public LocaleManager getParent() {
        return parent;
    }

    /**
     * Gets the key associated with this {@link Localizable}'s message.
     *
     * @return The key associated with this {@link Localizable}'s message
     * @since 1.0
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the replacements for placeholder sequences in this
     * {@link Localizable}.
     * <p>
     * <p>Placeholder sequences are defined as a percent symbol (%) followed by
     * a number greater than or equal to 1. The first element of the replacement
     * string array will replace any placeholder sequences matching {@code %1},
     * the second, sequences matching {@code %2}, and so on.</p>
     * <p>
     * <p><strong>Note:</strong> Mutating the contents of the array passed as a
     * parameter after calling this method will not impact this
     * {@link Localizable}.</p>
     *
     * @param replacements The replacement strings to set for this {@link Localizable}
     * @return This {@link Localizable} object, for chaining)
     * @since 1.0
     */
    public Localizable withReplacements(String... replacements) {
        this.replacements = Arrays.copyOf(replacements, replacements.length);
        this.locReplacements = null;
        return this;
    }

    /**
     * Sets the replacements for placeholder sequences in this
     * {@link Localizable} as {@link Localizable} objects. These objects will be
     * localized appropriately when the parent {@link Localizable} (the object
     * this method is invoked upon) is localized.
     * <p>
     * <p>Placeholder sequences are defined as a percent symbol (%) followed by
     * a number greater than or equal to 1. The first element of the replacement
     * string array will replace any placeholder sequences matching {@code %1},
     * the second, sequences matching {@code %2}, and so on.</p>
     * <p>
     * <p><strong>Note:</strong> Mutating the contents of the array passed as a
     * parameter after calling this method will not impact this
     * {@link Localizable}.</p>
     *
     * @param replacements The replacement sequences to set for this {@link Localizable}
     * @return This {@link Localizable} object, for chaining
     * @since 1.1
     */
    public Localizable withReplacements(Localizable... replacements) {
        this.locReplacements = Arrays.copyOf(replacements, replacements.length);
        this.replacements = null;
        return this;
    }

    /**
     * Sets the prefix to prepend to this {@link Localizable} when it is
     * localized.
     *
     * @param prefix The prefix to prepend to this {@link Localizable} when it is localized.
     * @return This {@link Localizable} object, for chaining
     * @since 1.0
     */
    public Localizable withPrefix(String prefix) {
        this.prefix = fromNullableString(prefix);
        return this;
    }

    /**
     * Sets the suffix to append to this {@link Localizable} when it is
     * localized.
     *
     * @param suffix The suffix to append to this {@link Localizable} when it is localized.
     * @return This {@link Localizable} object, for chaining
     * @since 1.0
     */
    public Localizable withSuffix(String suffix) {
        this.suffix = fromNullableString(suffix);
        return this;
    }

    /**
     * Localizes this {@link Localizable} in the given locale.
     * <p>
     * <p>It is unnecessary to include alternate dialects of a locale as
     * fallbacks (e.g. {@code en_GB} as a fallback for {@code en_US}), as they
     * are included by default by the library.</p>
     *
     * @param locale    The locale to localize this {@link Localizable} in
     * @param fallbacks Locales to fall back upon if this {@link Localizable} is not available in the
     *                  player's locale (the parent {@link LocaleManager}'s default locale will be used if all
     *                  fallbacks are exhausted, and if this is unavailable, the value of {@link Localizable#getKey()}
     *                  will be used instead)
     * @return A string representing the localized message, or this {@link Localizable}'s internal key
     * if no localizations are available
     * @since 1.0
     */
    public String localizeIn(String locale, String... fallbacks) {
        return localizeIn(locale, false, fallbacks);
    }

    private String localizeIn(String locale, boolean recursive, String... fallbacks) {
        if (getParent().configs.containsKey(locale)) { // check if the locale is defined
            Properties props = getParent().configs.get(locale);
            if (props.containsKey(getKey())) { // check if the message is defined in the locale
                String message = (String) props.get(getKey()); // yay, it worked
                if (replacements != null) {
                    for (int i = 0; i < replacements.length; i++) { // replace placeholder sequences
                        message = message
                            .replaceAll("%" + (i + 1), Matcher.quoteReplacement(replacements[i]));
                    }
                } else if (locReplacements != null) {
                    for (int i = 0;
                         i < locReplacements.length; i++) { // replace placeholder sequences
                        String strRepl = locReplacements[i].localizeIn(locale, false);
                        message =
                            message.replaceAll("%" + (i + 1), Matcher.quoteReplacement(strRepl));
                    }
                }
                return prefix + message + suffix;
            }
        }
        if (!recursive) { // only inject alternatives the method is not called recursively and the first choice fails
            List<String> fbList = Lists.newArrayList(fallbacks);
            for (int i = 0; i < fbList.size(); i++) {
                String fb = fbList.get(i);
                if (LocaleManager.ALTERNATIVES.containsKey(fb)) {
                    for (String alt : LocaleManager.ALTERNATIVES.get(fb)) {
                        if (!fbList.contains(
                            alt)) { // check if the alternate dialect is already in the list
                            fbList.add(i + 1,
                                alt); // inject alternate dialects after the current fallback entry
                            ++i; // increment the counter past the new entry
                        }
                    }
                }
            }
            if (LocaleManager.ALTERNATIVES.containsKey(locale)) {
                for (String alt : LocaleManager.ALTERNATIVES.get(locale)) {
                    if (!fbList
                        .contains(alt)) { // check if the alternate dialect is already in the list
                        fbList.add(0, alt); // inject alternate dialects at the start of the list
                    }
                }
            }
            fallbacks = new String[fbList.size()];
            fbList.toArray(fallbacks);
        }
        if (fallbacks.length > 0) { // still some fallbacks to use
            String[] newFallbacks =
                new String[fallbacks.length - 1]; // reconstruct the fallback array
            System.arraycopy(fallbacks, 1, newFallbacks, 0,
                newFallbacks.length); // drop the first element
            return localizeIn(fallbacks[0], true, newFallbacks); // try the next fallback
        } else if (!locale.equals(getParent().getDefaultLocale())) {
            return localizeIn(getParent().getDefaultLocale(), true); // try the default locale
        } else {
            return prefix + getKey() + suffix; // last resort if no locale is available
        }
    }

    /**
     * Localizes this {@link Localizable} in the owning {@link LocaleManager}'s
     * default locale.
     *
     * @return The appropriate localization for this {@link Localizable}.
     * @since 1.0
     */
    public String localize() {
        return localizeIn(getParent().getDefaultLocale());
    }

    /**
     * Localizes this {@link Localizable} for the given {@link CommandSender}.
     * If the {@link CommandSender} is also a {@link Player}, the message will
     * be localized in their respective locale. Otherwise, it will be localized
     * in the parent {@link LocaleManager}'s default locale.
     * <p>
     * <p>It is unnecessary to include alternate dialects of a locale as
     * fallbacks (e.g. {@code en_GB} as a fallback for {@code en_US}), as they
     * are included by default by the library.</p>
     *
     * @param sender The {@link CommandSender} to localize this {@link Localizable} for
     * @return A string representing the localized message, or this {@link Localizable}'s internal key
     * if no localizations are available
     * @since 1.0
     */
    public String localizeFor(CommandSender sender) {
        return sender instanceof Player ?
            localizeIn(getParent().getLocale((Player) sender)) :
            localize();
    }

    /**
     * Sends this {@link Localizable} to the given {@link CommandSender}. If the
     * {@link CommandSender} is also a {@link Player}, the message will be
     * localized in their respective locale. Otherwise, it will be localized in
     * the parent {@link LocaleManager}'s default locale.
     * <p>
     * <p>It is unnecessary to include alternate dialects of a locale as
     * fallbacks (e.g. {@code en_GB} as a fallback for {@code en_US}), as they
     * are included by default by the library.</p>
     *
     * @param sender The {@link CommandSender} to send this {@link Localizable} to
     * @param level  The message level to set this to.
     * @since 1.0
     */
    public void sendTo(CommandSender sender, LogLevel level) {
        switch (level) {
            case WARNING:
                Output.get().sendWarn(sender, localizeFor(sender));
                break;
            case ERROR:
                Output.get().sendError(sender, localizeFor(sender));
                break;
            case INFO:
            default:
                Output.get().sendInfo(sender, localizeFor(sender));
                break;
        }
    }

    /**
     * Sends this {@link Localizable} to the given {@link CommandSender}. If the
     * {@link CommandSender} is also a {@link Player}, the message will be
     * localized in their respective locale. Otherwise, it will be localized in
     * the parent {@link LocaleManager}'s default locale.
     * <p>
     * <p>It is unnecessary to include alternate dialects of a locale as
     * fallbacks (e.g. {@code en_GB} as a fallback for {@code en_US}), as they
     * are included by default by the library.</p>
     *
     * @param sender The {@link CommandSender} to send this {@link Localizable} to
     * @since 1.0
     */
    public void sendTo(CommandSender sender) {
        sendTo(sender, LogLevel.INFO);
    }

    /**
     * Broadcasts this {@link Localizable} to all players on the server,
     * applying the respective locale for each player.
     *
     * @since 1.0
     */
    public void broadcast() {
        for (Player player : Prison.get().getPlatform().getOnlinePlayers()) {
            sendTo(player);
        }
        Output.get().logInfo(localize());
    }

    /**
     * Broadcasts this {@link Localizable} to all players in the given
     * {@link World}s.
     *
     * @param worlds The {@link World}s to broadcast to
     * @since 1.0
     */
    public void broadcast(World... worlds) {
        for (World w : worlds) {
            for (Player player : w.getPlayers()) {
                sendTo(player);
            }
        }
        Output.get().logInfo(localize());
    }

    /**
     * Returns the parameter if it is not {@code null}; otherwise returns an
     * empty {@link String}.
     *
     * @param nullable The {@link String} to process
     * @return The parameter if it is not {@code null}; otherwise an empty {@link String}
     */
    private String fromNullableString(String nullable) {
        return nullable != null ? nullable : "";
    }

}
