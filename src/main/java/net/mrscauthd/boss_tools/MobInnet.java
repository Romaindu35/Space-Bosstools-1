
package net.mrscauthd.boss_tools;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.mrscauthd.boss_tools.itemgroup.SpaceBosstoolsSpawnEggsItemGroup;
import net.mrscauthd.boss_tools.entity.AlienEntity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.CreatureEntity;

import java.lang.reflect.Method;

@BossToolsModElements.ModElement.Tag
public class MobInnet extends BossToolsModElements.ModElement {
    public static final DeferredRegister<EntityType<?>> ENTITYS = DeferredRegister.create(ForgeRegistries.ENTITIES, "boss_tools");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "boss_tools");
    public static RegistryObject<EntityType<?>> ALIEN = ENTITYS.register("alien", () -> EntityType.Builder
            .create(AlienEntity::new, EntityClassification.CREATURE).size(0.75f, 2.5f).build(new ResourceLocation("boss_tools", "alien").toString()));
    public static final RegistryObject<ModSpawnEggs> ALIEN_SPAWN_EGG = ITEMS.register("alien_spawn_egg",
            () -> new ModSpawnEggs(ALIEN, -13382401, -11650781, new Item.Properties().group(SpaceBosstoolsSpawnEggsItemGroup.tab)));
    /**
     * Do not remove this constructor
     */
    public MobInnet(BossToolsModElements instance) {
        super(instance, 673);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // RenderingRegistry.registerEntityRenderingHandler(STEntitys.ROCKET.get(),
        // ((IRenderFactory) RocketRenderer::new));
        // RenderingRegistry.registerEntityRenderingHandler(ALIEN.get(),
        // ((IRenderFactory) AlienRenderer::new));
    }

    @Override
    public void initElements() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        bus.addGenericListener(Structure.class, this::onRegisterStructures);
        ENTITYS.register(bus);
        ITEMS.register(bus);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        STStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
        modEventBus.addListener(this::setup2);
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
    }
    public void setup2(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            STStructures.setupStructures();
            STConfiguredStructures.registerConfiguredStructures();
        });
    }

    public void biomeModification(final BiomeLoadingEvent event) {
        event.getGeneration().getStructures().add(() -> STConfiguredStructures.CONFIGURED_RUN_DOWN_HOUSE);
    }
    private static Method GETCODEC_METHOD;
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                // StructureTutorialMain.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }
        }
    }

    @Override
    public void init(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put((EntityType<? extends CreatureEntity>) ALIEN.get(), AlienEntity.setCustomAttributes().create());
        });
    }
    public void onRegisterStructures(final RegistryEvent.Register<Structure<?>> event) {
        ModConfiguredStructure.registerConfiguredStructures();
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        StructurePool.init();
    }

    @Override
    public void serverLoad(FMLServerStartingEvent event) {
    }
}
