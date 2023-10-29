package dev.rosewood.rosepotions.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;

import java.util.List;

public class PotionCommandWrapper extends RoseCommandWrapper {

    public PotionCommandWrapper(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public String getDefaultName() {
        return "potions";
    }

    @Override
    public List<String> getDefaultAliases() {
        return List.of("rosepotions", "rp");
    }

    @Override
    public List<String> getCommandPackages() {
        return List.of("dev.rosewood.rosepotions.command.command");
    }

    @Override
    public boolean includeBaseCommand() {
        return true;
    }

    @Override
    public boolean includeHelpCommand() {
        return true;
    }

    @Override
    public boolean includeReloadCommand() {
        return true;
    }

}
