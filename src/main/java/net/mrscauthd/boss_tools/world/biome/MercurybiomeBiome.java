
package net.mrscauthd.boss_tools.world.biome;

import net.mrscauthd.boss_tools.block.MrrcuryCobblestoneBlock;
import net.mrscauthd.boss_tools.block.MercurystoneBlock;
import net.mrscauthd.boss_tools.BossToolsModElements;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;

import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.Biome;

@BossToolsModElements.ModElement.Tag
public class MercurybiomeBiome extends BossToolsModElements.ModElement {
	public static Biome biome;
	public MercurybiomeBiome(BossToolsModElements instance) {
		super(instance, 298);
		FMLJavaModLoadingContext.get().getModEventBus().register(new BiomeRegisterHandler());
	}
	private static class BiomeRegisterHandler {
		@SubscribeEvent
		public void registerBiomes(RegistryEvent.Register<Biome> event) {
			if (biome == null) {
				BiomeAmbience effects = new BiomeAmbience.Builder().setFogColor(-16777216).setWaterColor(4159204).setWaterFogColor(329011)
						.withSkyColor(-16777216).withFoliageColor(-16724992).withGrassColor(-16724992).build();
				BiomeGenerationSettings.Builder biomeGenerationSettings = new BiomeGenerationSettings.Builder()
						.withSurfaceBuilder(SurfaceBuilder.DEFAULT.func_242929_a(new SurfaceBuilderConfig(MercurystoneBlock.block.getDefaultState(),
								MrrcuryCobblestoneBlock.block.getDefaultState(), MrrcuryCobblestoneBlock.block.getDefaultState())));
				DefaultBiomeFeatures.withCavesAndCanyons(biomeGenerationSettings);
				DefaultBiomeFeatures.withFrozenTopLayer(biomeGenerationSettings);
				MobSpawnInfo.Builder mobSpawnInfo = new MobSpawnInfo.Builder().isValidSpawnBiomeForPlayer();
				biome = new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.2f).scale(0.3f).temperature(1.6f)
						.downfall(0f).setEffects(effects).withMobSpawnSettings(mobSpawnInfo.copy())
						.withGenerationSettings(biomeGenerationSettings.build()).build();
				event.getRegistry().register(biome.setRegistryName("boss_tools:mercurybiome"));
			}
		}
	}
	@Override
	public void init(FMLCommonSetupEvent event) {
	}
}
