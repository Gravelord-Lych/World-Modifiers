package lych.worldmodifiers.attachment;

import lych.worldmodifiers.WorldModifiersMod;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WorldModifiersMod.MODID);
    public static final Supplier<AttachmentType<PlayerModifiersData>> PLAYER_MODIFIERS_DATA = ATTACHMENT_TYPES.register("player_modifiers_data", () ->
            AttachmentType.<CompoundTag, PlayerModifiersData>serializable(PlayerModifiersDataImpl::new).copyOnDeath().build());

    private ModAttachments() {}
}
