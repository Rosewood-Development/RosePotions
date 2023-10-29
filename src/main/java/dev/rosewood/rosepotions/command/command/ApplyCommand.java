package dev.rosewood.rosepotions.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosepotions.potion.PotionEffect;
import org.bukkit.entity.Player;

public class ApplyCommand extends RoseCommand {

    public ApplyCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, PotionEffect effect) {
        final Player player = (Player) context.getSender();

        if (effect.has(player)) {
            effect.remove(player);
            player.sendMessage("Removed effect " + effect.getName() + " from " + player.getName() + ".");
            return;
        }

        effect.apply(player);
        player.sendMessage("Applied effect " + effect.getName() + " to " + player.getName() + " for 30 seconds.");
    }

    @Override
    protected String getDefaultName() {
        return "apply";
    }

    @Override
    public String getDescriptionKey() {
        return "command-apply-description";
    }

    @Override
    public String getRequiredPermission() {
        return "rosepotions.apply";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
