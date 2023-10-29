package dev.rosewood.rosepotions;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosepotions.listener.PotionListeners;
import dev.rosewood.rosepotions.manager.CommandManager;
import dev.rosewood.rosepotions.manager.ConfigurationManager;
import dev.rosewood.rosepotions.manager.LocaleManager;
import dev.rosewood.rosepotions.manager.PotionManager;
import org.bukkit.Bukkit;

import java.util.List;

public class RosePotions extends RosePlugin {

    private static RosePotions instance;

    public static RosePotions getInstance() {
        return instance;
    }

    public RosePotions() {
        super(-1, -1, ConfigurationManager.class, null, LocaleManager.class, CommandManager.class);

        instance = this;
    }

    @Override
    public void enable() {
        Bukkit.getPluginManager().registerEvents(new PotionListeners(this), this);
    }

    @Override
    public void disable() {

    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(PotionManager.class);
    }

}
