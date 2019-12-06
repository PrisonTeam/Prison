package tech.mcprison.prison.output;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.internal.CommandSender;

/**
 * A button component, which creates buttons that run commands.
 */
public class ButtonComponent extends DisplayComponent {

    protected FancyMessage button;

    /**
     * Create a button component. Buttons hold the format [symbol] text.
     *
     * @param text The text that tells the user what the button does (short and simple).
     * @param symbol The single-character symbol shown in the button's brackets, to denote it's a
     * button.
     * @param style The style of the button. See {@link ButtonComponent.Style}.
     */
    public ButtonComponent(String text, char symbol, Style style) {
        button = new FancyMessage("&7[" + style.color + symbol + "&7] " + text);
    }

    /**
     * Builder method. Makes this button open a URL on click. Automatically sets the tooltip to "Go
     * to URL" in order to provide transparency.
     *
     * @param url the <b>valid</b> URL.
     * @return an instance of this component for chaining.
     */
    public ButtonComponent openUrl(String url) {
        button.link(url);
        button.tooltip("&7Go to &b" + url); // force them to declare where we're taking them
        return this;
    }

    /**
     * Builder method. Makes this button suggest a command on click.
     *
     * @param command the command to suggest.
     * @param tooltip the tooltip to attach to the button.
     * @return an instance of this component for chaining.
     */
    public ButtonComponent suggestCommand(String command, String tooltip) {
        button.suggest(command);
        button.tooltip("&7" + tooltip);
        return this;
    }

    /**
     * Builder method. Makes this button run a command on click.
     *
     * @param command the command to run.
     * @param tooltip the tooltip to attach to the button.
     * @return an instance of this component for chaining.
     */
    public ButtonComponent runCommand(String command, String tooltip) {
        button.command(command);
        button.tooltip("&7" + tooltip);
        return this;
    }

    @Override
    public String text() {
        return button.toOldMessageFormat();
    }

    @Override
    public void send(CommandSender sender) {
        button.send(sender);
    }
    
    public FancyMessage getFancyMessage() {
    	return button;
    }

    /**
     * Denotes the style of the button.
     */
    public enum Style {

        /**
         * The button performs a positive action i.e. creates something.
         */
        POSITIVE("&a"),

        /**
         * The button performs a negative action i.e. deletes something.
         */
        NEGATIVE("&c"),

        /**
         * The button performs a neutral action i.e. shows information.
         */
        NEUTRAL("&6");

        private String color;

        Style(String color) {
            this.color = color;
        }

    }

}
