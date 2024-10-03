package lych.worldmodifiers.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityEvent;

/**
 * ApplyingModifiersToEntitiesEvent is fired when modifiers are being applied to an {@link Entity}.<br>
 * <br>
 * All children of this event are fired on the {@link NeoForge#EVENT_BUS}.
 */
public abstract class ApplyingModifiersToEntitiesEvent<T extends Entity> extends EntityEvent {
    private final T entity;

    protected ApplyingModifiersToEntitiesEvent(T entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public T getEntity() {
        return entity;
    }

    /**
     * ToPlayer is fired when modifiers are being applied to a {@link Player}<br>
     * <br>
     * This event is fired via the {@link WorldModifierEventHooks#onApplyingModifiersToPlayers(Player)}.<br>
     * <br>
     * This event is not {@link net.neoforged.bus.api.ICancellableEvent}.<br>
     * <br>
     * This event does not have a result.<br>
     * <br>
     * This event is fired on the {@link NeoForge#EVENT_BUS}.
     */
    public static class ToPlayer extends ApplyingModifiersToEntitiesEvent<Player> {
        public ToPlayer(Player player) {
            super(player);
        }
    }

    /**
     * ToMob is fired when modifiers are being applied to a {@link Mob}<br>
     * <br>
     * This event is fired via the {@link WorldModifierEventHooks#onApplyingModifiersToMobs(Mob)}.<br>
     * <br>
     * This event is not {@link net.neoforged.bus.api.ICancellableEvent}.<br>
     * <br>
     * This event does not have a result.<br>
     * <br>
     * This event is fired on the {@link NeoForge#EVENT_BUS}.
     */
    public static class ToMob extends ApplyingModifiersToEntitiesEvent<Mob> {
        public ToMob(Mob mob) {
            super(mob);
        }
    }
}
