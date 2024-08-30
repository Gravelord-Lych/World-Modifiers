package lych.worldmodifiers.util;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.Supplier;

public final class ExtraRaiderTypes {
    public static final EnumProxy<Raid.RaiderType> VINDICATOR89_PROXY = createVanillaRaider89Proxy(EntityType.VINDICATOR, 1, 6);
    public static final EnumProxy<Raid.RaiderType> EVOKER89_PROXY = createVanillaRaider89Proxy(EntityType.EVOKER, 3, 0);
    public static final EnumProxy<Raid.RaiderType> PILLAGER89_PROXY = createVanillaRaider89Proxy(EntityType.PILLAGER, 3, 6);
    public static final EnumProxy<Raid.RaiderType> WITCH89_PROXY = createVanillaRaider89Proxy(EntityType.WITCH, 4, 2);
    public static final EnumProxy<Raid.RaiderType> RAVAGER89_PROXY = createVanillaRaider89Proxy(EntityType.RAVAGER, 1, 3);

    private ExtraRaiderTypes() {}

    private static EnumProxy<Raid.RaiderType> createVanillaRaider89Proxy(EntityType<? extends Raider> raiderType, int baseCount8, int baseCount9) {
        return new EnumProxy<>(Raid.RaiderType.class,
                (Supplier<EntityType<? extends Raider>>) () -> raiderType,
                new int[]{0, 0, 0, 0, 0, 0, 0, 0, baseCount8, baseCount9});
    }
}
