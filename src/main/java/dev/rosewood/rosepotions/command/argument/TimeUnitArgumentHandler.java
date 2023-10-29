package dev.rosewood.rosepotions.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import dev.rosewood.rosepotions.util.PotionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeUnitArgumentHandler extends RoseCommandArgumentHandler<TimeUnit> {

    public TimeUnitArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, TimeUnit.class);
    }

    @Override
    protected TimeUnit handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) throws HandledArgumentException {
        String input = argumentParser.next();

        TimeUnit timeUnit = PotionUtils.getEnum(TimeUnit.class, input.toUpperCase());
        if (timeUnit == null)
            throw new HandledArgumentException("argument-handler-timeunit", StringPlaceholders.of("input", input.toUpperCase()));

        return timeUnit;
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();

        return Arrays.stream(TimeUnit.values()).map(timeUnit -> timeUnit.name().toLowerCase()).toList();
    }

}
