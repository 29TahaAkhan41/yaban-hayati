package com.yabanhayati;

import com.yabanhayati.def.AnimalDef;
import com.yabanhayati.def.Animals;
import com.yabanhayati.def.ModelShape;
import com.yabanhayati.entity.AquaticAnimal;
import com.yabanhayati.entity.FlyingBird;
import com.yabanhayati.entity.WildAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod(YabanHayati.MODID)
public final class YabanHayati {
    public static final String MODID = "yabanhayati";

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    /** Kara hayvanlari ve kuslar. */
    public static final Map<AnimalDef, DeferredHolder<EntityType<?>, EntityType<WildAnimal>>> LAND = new LinkedHashMap<>();
    /** Su hayvanlari. */
    public static final Map<AnimalDef, DeferredHolder<EntityType<?>, EntityType<AquaticAnimal>>> WATER = new LinkedHashMap<>();
    public static final List<DeferredItem<Item>> EGGS = new ArrayList<>();

    static {
        for (AnimalDef def : Animals.ALL) {
            ModelShape shape = ModelShape.of(def);
            DeferredHolder<EntityType<?>, ? extends EntityType<? extends Mob>> holder;
            if (def.isAquatic()) {
                boolean small = def.bodyL * def.scale < 16;
                var h = ENTITIES.register(def.id, () -> EntityType.Builder.<AquaticAnimal>of(AquaticAnimal::new,
                                small ? MobCategory.WATER_AMBIENT : MobCategory.WATER_CREATURE)
                        .sized(shape.width, shape.height).clientTrackingRange(10).build(def.id));
                WATER.put(def, h);
                holder = h;
            } else {
                EntityType.EntityFactory<WildAnimal> factory = def.flying ? FlyingBird::new : WildAnimal::new;
                var h = ENTITIES.register(def.id, () -> EntityType.Builder.of(factory, MobCategory.CREATURE)
                        .sized(shape.width, shape.height).clientTrackingRange(10).build(def.id));
                LAND.put(def, h);
                holder = h;
            }
            int bg = def.base, fg = def.pattern != AnimalDef.Pattern.NONE ? def.accent
                    : (def.bellyColor() != def.base ? def.bellyColor() : AnimalDef.darken(def.base, 0.6f));
            EGGS.add(ITEMS.register(def.id + "_spawn_egg", () -> new DeferredSpawnEggItem(holder, bg, fg, new Item.Properties())));
        }
        TABS.register("main", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + MODID))
                .icon(() -> new ItemStack(EGGS.get(0).get()))
                .displayItems((params, out) -> EGGS.forEach(e -> out.accept(e.get())))
                .build());
    }

    public YabanHayati(IEventBus modBus) {
        ENTITIES.register(modBus);
        ITEMS.register(modBus);
        TABS.register(modBus);
        modBus.addListener(YabanHayati::attributes);
        modBus.addListener(YabanHayati::spawnPlacements);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static AnimalDef defOf(EntityType<?> type) {
        return Animals.BY_ID.get(BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath());
    }

    private static AttributeSupplier attributesFor(AnimalDef def) {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, def.health)
                .add(Attributes.MOVEMENT_SPEED, def.speed)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.ATTACK_DAMAGE, Math.max(1, def.attack))
                .add(Attributes.FOLLOW_RANGE, 24)
                .build();
    }

    private static void attributes(EntityAttributeCreationEvent event) {
        LAND.forEach((def, type) -> event.put(type.get(), attributesFor(def)));
        WATER.forEach((def, type) -> event.put(type.get(), attributesFor(def)));
    }

    private static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        LAND.forEach((def, type) -> event.register(type.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                YabanHayati::canSpawnOnLand, RegisterSpawnPlacementsEvent.Operation.REPLACE));
        WATER.forEach((def, type) -> event.register(type.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE));
    }

    /** Vanilla hayvanlarin aksine cim sarti yok: col, kar, kumsal gibi her zeminde dogabilirler. */
    private static boolean canSpawnOnLand(EntityType<WildAnimal> type, LevelAccessor level, MobSpawnType reason, BlockPos pos,
                                          net.minecraft.util.RandomSource random) {
        BlockPos below = pos.below();
        BlockState ground = level.getBlockState(below);
        return ground.isFaceSturdy(level, below, net.minecraft.core.Direction.UP)
                && !ground.is(BlockTags.LEAVES)
                && level.getRawBrightness(pos, 0) > 8;
    }
}
