package dev.rosewood.rosepotions.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosepotions.manager.PotionManager;
import dev.rosewood.rosepotions.potion.PotionEffect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class GiveCommand extends RoseCommand {

    public GiveCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Player target, PotionEffect effect, int duration, TimeUnit unit) {
        final PotionManager manager = this.rosePlugin.getManager(PotionManager.class);

        final ItemStack potion = manager.createPotion(effect, duration, unit).clone();
        target.getInventory().addItem(potion);
        context.getSender().sendMessage("Gave " + target.getName() + " a " + effect.getName() + " potion.");
    }

    @Override
    protected String getDefaultName() {
        return "give";
    }

    @Override
    public String getDescriptionKey() {
        return "command-give-description";
    }

    @Override
    public String getRequiredPermission() {
        return "rosepotions.give";
    }

}
