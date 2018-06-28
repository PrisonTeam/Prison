package me.faizaand.prison.economy;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventPriority;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.internal.GamePlayer;

public class EconomyListener {

    public EconomyListener() {
        listenForNewPlayer();
    }

    private void listenForNewPlayer() {
        Prison.get().getEventManager().subscribe(EventType.PlayerJoinEvent, objects -> {
            GamePlayer player = (GamePlayer) objects[0];

            if (!PrisonEconomy.getInstance().getEconomyManager().getAccount(player.getUUID()).isPresent()) {
                // we want to create the account here
                PrisonEconomy.getInstance().getEconomyManager().createAccount(player.getUUID());
            }

            return new Object[]{};
        }, EventPriority.NORMAL);
    }

}
