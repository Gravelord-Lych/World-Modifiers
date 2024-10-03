package lych.worldmodifiers.api.event;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;

/**
 * A class used to post custom events added by World Modifiers Mod.
 */
public final class WorldModifierEventHooks {
    private WorldModifierEventHooks() {}

    public static void onApplyingModifiersToPlayers(Player player) {
        NeoForge.EVENT_BUS.post(new ApplyingModifiersToEntitiesEvent.ToPlayer(player));
    }

    public static void onApplyingModifiersToMobs(Mob mob) {
        NeoForge.EVENT_BUS.post(new ApplyingModifiersToEntitiesEvent.ToMob(mob));
    }
}
