package lych.worldmodifiers.data;

import lych.worldmodifiers.WorldModifiers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = WorldModifiers.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModDataGenerators {
    private ModDataGenerators() {}

    @SubscribeEvent
    public static void run(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(event.includeServer(),new ModDatapackBuiltinEntriesProvider(output, lookupProvider));

        gen.addProvider(event.includeClient(), new ModLanguageProvider.EnUs(output));
        gen.addProvider(event.includeClient(), new ModLanguageProvider.ZhCn(output));
    }
}
