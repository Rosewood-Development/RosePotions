package dev.rosewood.rosepotions.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import dev.rosewood.rosepotions.manager.PotionManager;
import dev.rosewood.rosepotions.potion.PotionEffect;

import java.util.List;

public class EffectArgumentHandler extends RoseCommandArgumentHandler<PotionEffect> {

    public EffectArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, PotionEffect.class);
    }

    @Override
    protected PotionEffect handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) throws HandledArgumentException {
        String input = argumentParser.next();

        PotionEffect effect = this.rosePlugin.getManager(PotionManager.class).getLoaded().stream()
                .filter(potionEffect -> potionEffect.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);

        if (effect == null)
            throw new HandledArgumentException("argument-handler-effect", StringPlaceholders.of("input", input));

        return effect;
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();

        return this.rosePlugin.getManager(PotionManager.class).getLoaded().stream().map(PotionEffect::getName).toList();
    }

}
