package cc.ddev.feather.api.events.player;

import cc.ddev.feather.api.enums.ATMOpenType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

public class ATMOpenEvent implements CancellableEvent, Event {
    private final Player player;
    private Player openedFor;
    private final ATMOpenType openType;
    private boolean cancelled = false;

    public ATMOpenEvent(Player player, Player openedFor, ATMOpenType openType) {
        this.player = player;
        this.openType = openType;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Player getOpenedFor() {
        return this.openedFor;
    }

    public ATMOpenType getOpenType() {
        return this.openType;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}