/*
 * New BSD License (BSD-new)
 *
 * Copyright (c) 2015-2016 Max Roncace
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     - Neither the name of the copyright holder nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package tech.mcprison.prison.localization;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

import com.google.common.collect.Lists;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;

/**
 * Represents an object which has the potential to be localized in one of
 * multiple configs and returned as a string.
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
    	Output.get().sendMessage(sender, localizeFor(sender), level);
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
     */
    public void sendTo(CommandSender sender) {
        sendTo(sender, LogLevel.PLAIN);
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

    // Use LogLevel instead since they are the same:
//    public enum Level {
//        PLAIN, INFO, WARN, ERROR
//    }

}
