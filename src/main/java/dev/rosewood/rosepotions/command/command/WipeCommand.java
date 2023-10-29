package dev.rosewood.rosepotions.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosepotions.manager.PotionManager;
import org.bukkit.entity.Player;

public class WipeCommand extends RoseCommand {

    public WipeCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        final Player player = (Player) context.getSender();
        final PotionManager manager = this.rosePlugin.getManager(PotionManager.class);

        manager.wipeEffects(player);
        player.sendMessage("Wiped " + player.getName() + "'s potion effects.");
    }

    @Override
    protected String getDefaultName() {
        return "wipe";
    }

    @Override
    public String getDescriptionKey() {
        return "command-wipe-description";
    }

    @Override
    public String getRequiredPermission() {
        return "rosepotions.wipe";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
