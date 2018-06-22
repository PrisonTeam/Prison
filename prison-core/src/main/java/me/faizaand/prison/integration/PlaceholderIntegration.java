package me.faizaand.prison.integration;

import me.faizaand.prison.internal.GamePlayer;

import java.util.function.Function;

/**
 * An integration into a placeholder plugin.
 */
public interface PlaceholderIntegration extends Integration {

    void registerPlaceholder(String placeholder, Function<GamePlayer, String> action);

    @Override
    default IntegrationType getType() {
        return IntegrationType.PLACEHOLDER;
    }

}
