
package net.mrscauthd.boss_tools.world.structure;


import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.mrscauthd.boss_tools.BossToolsModElements;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MeteorStructure extends Structure<NoFeatureConfig> {
	//private static Feature<NoFeatureConfig> feature = null;
	//private static ConfiguredFeature<?, ?> configuredFeature = null;
	public MeteorStructure(Codec<NoFeatureConfig> codec) {
		super(codec);
		//MinecraftForge.EVENT_BUS.register(this);
		//System.out.println("its works!");
	}

	/**
	 * This is how the worldgen code knows what to call when it
	 * is time to create the pieces of the structure for generation.
	 */
	@Override
	public  IStartFactory<NoFeatureConfig> getStartFactory() {
		return MeteorStructure.Start::new;
	}


	/**
	 * Generation stage for when to generate the structure. there are 10 stages you can pick from!
	 * This surface structure stage places the structure before plants and ores are generated.
	 */
	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}


	/**
	 * || ONLY WORKS IN FORGE 34.1.12+ ||
	 *
	 * This method allows us to have mobs that spawn naturally over time in our structure.
	 * No other mobs will spawn in the structure of the same entity classification.
	 * The reason you want to match the classifications is so that your structure's mob
	 * will contribute to that classification's cap. Otherwise, it may cause a runaway
	 * spawning of the mob that will never stop.
	 *
	 * NOTE: getDefaultSpawnList is for monsters only and getDefaultCreatureSpawnList is
	 *       for creatures only. If you want to add entities of another classification,
	 *       use the StructureSpawnListGatherEvent to add water_creatures, water_ambient,
	 *       ambient, or misc mobs. Use that event to add/remove mobs from structures
	 *       that are not your own.
	 */
	private static final List<MobSpawnInfo.Spawners> STRUCTURE_MONSTERS = ImmutableList.of(
			new MobSpawnInfo.Spawners(EntityType.ILLUSIONER, 100, 4, 9),
			new MobSpawnInfo.Spawners(EntityType.VINDICATOR, 100, 4, 9)
	);
	@Override
	public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
		return STRUCTURE_MONSTERS;
	}

	private static final List<MobSpawnInfo.Spawners> STRUCTURE_CREATURES = ImmutableList.of(
			new MobSpawnInfo.Spawners(EntityType.SHEEP, 30, 10, 15),
			new MobSpawnInfo.Spawners(EntityType.RABBIT, 100, 1, 2)
	);
	@Override
	public List<MobSpawnInfo.Spawners> getDefaultCreatureSpawnList() {
		return STRUCTURE_CREATURES;
	}



	/**
	 * Handles calling up the structure's pieces class and height that structure will spawn at.
	 */
	public static class Start extends StructureStart<NoFeatureConfig>  {
		public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
			super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
		}

		@Override
		public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {

			// Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
			int x = (chunkX << 4) + 7;
			int z = (chunkZ << 4) + 7;
			BlockPos blockpos = new BlockPos(x, -9, z);

			// All a structure has to do is call this method to turn it into a jigsaw based structure!

			JigsawManager.func_242837_a(
					dynamicRegistryManager,
					new VillageConfig(() -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY)
							// The path to the starting Template Pool JSON file to read.
							//
							// Note, this is "structure_tutorial:run_down_house/start_pool" which means
							// the game will automatically look into the following path for the template pool:
							// "resources/data/structure_tutorial/worldgen/template_pool/run_down_house/start_pool.json"
							// This is why your pool files must be in "data/<modid>/worldgen/template_pool/<the path to the pool here>"
							// because the game automatically will check in worldgen/template_pool for the pools.
							.getOrDefault(new ResourceLocation("boss_tools", "run_mateor_dungeon/side_meteor_4")),

							// How many pieces outward from center can a recursive jigsaw structure spawn.
							// Our structure is only 1 block out and isn't recursive so any value of 1 or more doesn't change anything.
							// However, I recommend you keep this a high value so people can use datapacks to add additional pieces to your structure easily.
							// But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
							25),
					AbstractVillagePiece::new,
					chunkGenerator,
					templateManagerIn,
					blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
					this.components, // The list that will be populated with the jigsaw pieces after this method.
					this.rand,
					true, // Allow intersecting jigsaw pieces. If false, villages cannot generate houses. I recommend to keep this to true.
					true); // Place at heightmap (top land). Set this to false for structure to be place at blockpos's Y value instead


			// **THE FOLLOWING TWO LINES ARE OPTIONAL**
			//
			// Right here, you can do interesting stuff with the pieces in this.components such as offset the
			// center piece by 50 blocks up for no reason, remove repeats of a piece or add a new piece so
			// only 1 of that piece exists, etc. But you do not have access to the piece's blocks as this list
			// holds just the piece's size and positions. Blocks will be placed later in JigsawManager.
			//
			// In this case, we do `piece.offset` to raise pieces up by 1 block so that the house is not right on
			// the surface of water or sunken into land a bit.
			//
			// Then we extend the bounding box down by 1 by doing `piece.getBoundingBox().minY` which will cause the
			// land formed around the structure to be lowered and not cover the doorstep. You can raise the bounding
			// box to force the structure to be buried as well. This bounding box stuff with land is only for structures
			// that you added to Structure.field_236384_t_ field handles adding land around the base of structures.
			//
			// By lifting the house up by 1 and lowering the bounding box, the land at bottom of house will now be
			// flush with the surrounding terrain without blocking off the doorstep.
			this.components.forEach(piece -> piece.offset(0, 1, 0));
			this.components.forEach(piece -> piece.getBoundingBox().minY -= 1);


			// Sets the bounds of the structure once you are finished.
			this.recalculateStructureSize();

			// I use to debug and quickly find out if the structure is spawning or not and where it is.
			// This is returning the coordinates of the center starting piece.
			//StructureTutorialMain.LOGGER.log(Level.DEBUG, "Rundown House at " +

			// I use to debug and quickly find out if the structure is spawning or not and where it is.
			// This is returning the coordinates of the center starting piece.
		}
	}
}
