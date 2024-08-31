package lych.worldmodifiers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Dynamic;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.mixin.ConfirmScreenAccessor;
import lych.worldmodifiers.util.mixin.ICreateWorldScreenMixin;
import lych.worldmodifiers.util.mixin.IWorldCreationUiStateMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Path;

@Mixin(WorldSelectionList.WorldListEntry.class)
public class WorldListEntryMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private SelectWorldScreen screen;

    @WrapOperation(method = "recreateWorld",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;createFromExisting(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/world/level/LevelSettings;Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;Ljava/nio/file/Path;)Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;"))
    private CreateWorldScreen worldModifier$handleAdditionalDataWhenRecreatingWorld(Minecraft minecraft, Screen lastScreen, LevelSettings levelSettings, WorldCreationContext settings, Path tempDataPackDir, Operation<CreateWorldScreen> original, @Local LevelStorageSource.LevelStorageAccess access) {
        CreateWorldScreen screen = original.call(minecraft, lastScreen, levelSettings, settings, tempDataPackDir);
        try {
            worldModifiers$readAdditionalData(access.getDataTag(), (ICreateWorldScreenMixin) screen);
        } catch (Exception e) {
            WorldModifiersMod.LOGGER.warn("Unable to read additional data when recreating world", e);
        }
        return screen;
    }

    @WrapOperation(method = "recreateWorld",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", ordinal = 0))
    private void worldModifier$handleAdditionalDataWhenRecreatingCustomizedWorld(Minecraft instance,
                                                                                 Screen old,
                                                                                 Operation<Void> original,
                                                                                 @Local LevelStorageSource.LevelStorageAccess access,
                                                                                 @Local LevelSettings settings,
                                                                                 @Local WorldCreationContext context,
                                                                                 @Local Path path) {

        original.call(instance, old);
        Dynamic<?> dataTag;
        try {
            dataTag = access.getDataTag();
        } catch (Exception e) {
            WorldModifiersMod.LOGGER.warn("Unable to read additional data when recreating old customized world", e);
            return;
        }
        if (!(minecraft.screen instanceof ConfirmScreen confirmScreen)) {
            WorldModifiersMod.LOGGER.warn("Unable to handle mod data when recreating old customized world");
            return;
        }
        final Dynamic<?> finalDataTag = dataTag;
        ((ConfirmScreenAccessor) confirmScreen).setCallback(ok -> {
            if (ok) {
                CreateWorldScreen createWorldScreen = CreateWorldScreen.createFromExisting(minecraft, screen, settings, context, path);

                worldModifiers$readAdditionalData(finalDataTag, (ICreateWorldScreenMixin) createWorldScreen);
                minecraft.setScreen(createWorldScreen);
            } else {
                minecraft.setScreen(screen);
            }
        });
    }

    @Unique
    private static void worldModifiers$readAdditionalData(Dynamic<?> tag, ICreateWorldScreenMixin screen) {
        ((IWorldCreationUiStateMixin) screen.worldModifiers$getUiState())
                .worldModifiers$setExtremeDifficulty(tag.get(DifficultyHelper.EXTREME_DIFFICULTY_TAG).asBoolean(false));
    }
}
