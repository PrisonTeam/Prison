package tech.mcprison.prison.spigot.gui.guiutility;

import tech.mcprison.prison.spigot.SpigotPrison;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AnonymousGCA (GABRYCA)
 * */
public class ButtonLore extends SpigotGUIComponents{

    private List<String> descriptionAction = new ArrayList<>();
    private List<String> description = new ArrayList<>();
    private String colorIDBracket = "&8";
    private String colorIDAction = "&8";
    private String colorIDDescription = "&3";
    private String bracketDef = "--------------------";

    /**
     * Initialize a button lore with basic info (can also be empty).
     *
     * The final format, if all requirements are met, is as follow:
     *
     * &8descriptionAction
     *
     * &8----------------------------
     * &3description
     * &8----------------------------
     *
     * @param descriptionAction - List
     * @param description - List
     * */
    public ButtonLore(List<String> descriptionAction, List<String> description){
        if (!descriptionAction.isEmpty()) {
            for (String dAction : descriptionAction) {
                this.descriptionAction.add(SpigotPrison.format(colorIDAction + dAction));
            }
        }

        if (!description.isEmpty()) {
            for (String descriptionLore : description) {
                this.description.add(SpigotPrison.format(colorIDDescription + descriptionLore));
            }
        }
    }

    public ButtonLore(){}

    /**
     * Initialize a button lore with basic info (can also be empty).
     *
     * The final format, if all requirements are met, is as follow:
     *
     * &8descriptionAction
     *
     * &8----------------------------
     * &3description
     * &8----------------------------
     *
     * @param descriptionAction - String.
     * @param description - String.
     * */
    public ButtonLore(String descriptionAction, String description){
        if (descriptionAction != null) {
            this.descriptionAction.add(SpigotPrison.format(colorIDAction + descriptionAction));
        }

        if (description != null) {
            this.description.add(SpigotPrison.format(colorIDDescription + description));
        }
    }

    /**
     *  Set description action.
     *
     * @param descriptionAction - List.
     * */
    public void setLoreAction(List<String> descriptionAction){
        this.descriptionAction.clear();
        for (String dAction : descriptionAction) {
            this.descriptionAction.add(SpigotPrison.format(colorIDAction + dAction));
        }
    }

    /**
     *  Set description action.
     *
     * @param descriptionAction - String.
     * */
    public void setLoreAction(String descriptionAction){
        this.descriptionAction.clear();
        this.descriptionAction.add(SpigotPrison.format(colorIDAction + descriptionAction));
    }

    /**
     * Add line to action lore.
     *
     * @param line - String.
     * */
    public void addLineLoreAction(String line){
        this.descriptionAction.add(SpigotPrison.format(colorIDAction + line));
    }

    /**
     * Set description.
     *
     * @param description - List.
     * */
    public void setLoreDescription(List<String> description){
        this.description.clear();
        for (String descriptionLore : description) {
            this.description.add(SpigotPrison.format(colorIDDescription + descriptionLore));
        }
    }

    /**
     * Set description.
     *
     * @param description - String.
     * */
    public void setLoreDescription(String description){
        this.description.clear();
        this.description.add(SpigotPrison.format(colorIDDescription + description));
    }

    /**
     * Add line to description lore.
     *
     * @param line - String.
     * */
    public void addLineLoreDescription(String line){
        this.description.add(SpigotPrison.format(colorIDDescription + line));
    }

    /**
     * Set color ID of the bracket.
     *
     * Default value: &8 (or defined by config).
     *
     * Please add color IDs using "&".
     *
     * @param colorID - String.
     * */
    public void setColorIDBracket(String colorID){
        this.colorIDBracket = colorID;
    }

    /**
     * Set color ID of the action description.
     *
     * Default value: &8 (or defined by config).
     *
     * Please add color IDs using "&".
     *
     * @param colorID - String.
     * */
    public void setColorIDAction(String colorID){
        this.colorIDAction = colorID;
    }

    /**
     * Set color ID of the description.
     *
     * Default value: &3 (or defined by config).
     *
     * Please add color IDs using "&".
     *
     * @param colorID - String.
     * */
    public void setColorIDDescription(String colorID){
        this.colorIDDescription = colorID;
    }

    /**
     * Add bracket, this may in the feature become customizable trough a config.
     *
     * Usually the bracket looks like this:
     *
     * &8----------------------------
     *
     * And is formatted like this (editable).
     *
     * colorID + brackedDefDivider
     *
     * @return bracket - String.
     * */
    private String addBracket(){
        return colorIDBracket + bracketDef;
    }

    /**
     * Get button lore description.
     * */
    public List<String> getLoreDescription(){
        return description;
    }

    /**
     * Get button lore action.
     * */
    public List<String> getLoreAction(){
        return descriptionAction;
    }

    /**
     * This will return the lore with the global format:
     *
     * &8descriptionAction
     *
     * &8----------------------------
     * description
     * &8----------------------------
     *
     *
     * For example:
     *
     * &8Click to open
     *
     * &8----------------------------
     * &3This will open a GUI.
     * &8----------------------------
     *
     * @return lore - List
     * */
    public List<String> getLore(){

        List<String> lore = new ArrayList<>();

        if (!descriptionAction.isEmpty()) {
            lore.addAll(descriptionAction);
        }

        if (!description.isEmpty()) {
            lore.add("");
            lore.add(SpigotPrison.format(addBracket()));
            lore.addAll(description);
            lore.add(SpigotPrison.format(addBracket()));
        }
        return lore;
    }
}
