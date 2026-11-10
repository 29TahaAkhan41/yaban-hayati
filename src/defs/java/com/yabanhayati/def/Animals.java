package com.yabanhayati.def;

import static com.yabanhayati.def.AnimalDef.Ears.*;
import static com.yabanhayati.def.AnimalDef.Horns.*;
import static com.yabanhayati.def.AnimalDef.Pattern.*;
import static com.yabanhayati.def.AnimalDef.Shape.*;
import static com.yabanhayati.def.AnimalDef.Tail.*;
import static com.yabanhayati.def.AnimalDef.Temper.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TUM HAYVANLARIN LISTESI.
 * Yeni hayvan eklemek icin buraya bir satir ekleyip `./gradlew genAssets build` calistirmak yeterli.
 */
public final class Animals {
    public static final List<AnimalDef> ALL = new ArrayList<>();
    public static final Map<String, AnimalDef> BY_ID = new LinkedHashMap<>();

    // --- biyom gruplari ("#" ile baslayanlar etiket, digerleri tek biyom) ---
    static final String[] PLAINS = {"minecraft:plains", "minecraft:sunflower_plains", "minecraft:meadow"};
    static final String[] FOREST = {"#minecraft:is_forest"};
    static final String[] TAIGA = {"#minecraft:is_taiga"};
    static final String[] SAVANNA = {"#minecraft:is_savanna"};
    static final String[] JUNGLE = {"#minecraft:is_jungle"};
    static final String[] DESERT = {"minecraft:desert"};
    static final String[] BADLANDS = {"#minecraft:is_badlands"};
    static final String[] SNOWY = {"minecraft:snowy_plains", "minecraft:ice_spikes", "minecraft:snowy_taiga", "minecraft:grove", "minecraft:snowy_slopes"};
    static final String[] ICE_COAST = {"minecraft:snowy_beach", "minecraft:frozen_ocean", "minecraft:deep_frozen_ocean", "minecraft:ice_spikes"};
    static final String[] SWAMP = {"minecraft:swamp", "minecraft:mangrove_swamp"};
    static final String[] RIVER = {"minecraft:river"};
    static final String[] BEACH = {"minecraft:beach", "minecraft:stony_shore"};
    static final String[] MOUNTAIN = {"#minecraft:is_mountain"};
    static final String[] OCEAN = {"minecraft:ocean", "minecraft:deep_ocean", "minecraft:lukewarm_ocean", "minecraft:deep_lukewarm_ocean", "minecraft:warm_ocean"};
    static final String[] DEEP_OCEAN = {"minecraft:deep_ocean", "minecraft:deep_lukewarm_ocean", "minecraft:deep_cold_ocean"};
    static final String[] COLD_OCEAN = {"minecraft:cold_ocean", "minecraft:deep_cold_ocean", "minecraft:frozen_ocean", "minecraft:deep_frozen_ocean"};
    static final String[] WARM_OCEAN = {"minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:deep_lukewarm_ocean"};

    // --- yiyecek gruplari ---
    static final String[] MEAT = {"minecraft:beef", "minecraft:porkchop", "minecraft:mutton", "minecraft:chicken", "minecraft:rabbit"};
    static final String[] FISHES = {"minecraft:cod", "minecraft:salmon", "minecraft:tropical_fish"};
    static final String[] GRASS = {"minecraft:wheat", "minecraft:hay_block"};
    static final String[] FRUIT = {"minecraft:apple", "minecraft:melon_slice", "minecraft:sweet_berries", "minecraft:glow_berries"};
    static final String[] VEG = {"minecraft:carrot", "minecraft:potato", "minecraft:beetroot", "minecraft:apple"};
    static final String[] SEEDS = {"minecraft:wheat_seeds", "minecraft:pumpkin_seeds", "minecraft:melon_seeds", "minecraft:beetroot_seeds"};
    static final String[] LEAVES = {"minecraft:oak_leaves", "minecraft:jungle_leaves", "minecraft:acacia_leaves", "minecraft:birch_leaves"};
    static final String[] BAMBOO = {"minecraft:bamboo"};

    private static AnimalDef q(String id, String en, String tr) { return reg(new AnimalDef(QUAD, id, en, tr)); }
    private static AnimalDef b(String id, String en, String tr) { return reg(new AnimalDef(BIRD, id, en, tr).legs(2, 1).head(3, 3, 3)); }
    private static AnimalDef f(String id, String en, String tr) { return reg(new AnimalDef(FISH, id, en, tr)); }
    private static AnimalDef reg(AnimalDef d) { ALL.add(d); BY_ID.put(d.id, d); return d; }
    private static String[] mix(String[]... a) {
        List<String> l = new ArrayList<>();
        for (String[] s : a) l.addAll(List.of(s));
        return l.toArray(String[]::new);
    }

    static {
        // ============================== BUYUK KEDILER VE AVCILAR ==============================
        q("lion", "Lion", "Aslan").body(8, 8, 18).legs(8, 3).head(7, 7, 6).snout(4, 3, 2).ears(ROUND).tail(TUFT, 10)
                .withMane(0x7A4A1E).colors(0xD9A85B, 0xEBD3A0).scale(1.2).stats(30, 0.28, 7).temper(PREDATOR)
                .food(MEAT).biomes(SAVANNA).spawn(5, 2, 4).drop("minecraft:leather", 0, 2).drop("minecraft:bone", 0, 2);
        q("tiger", "Tiger", "Kaplan").body(8, 8, 19).legs(7, 3).head(7, 6, 6).snout(4, 3, 2).ears(ROUND).tail(THIN, 11)
                .colors(0xE08A2E, 0xF6EBD8).pattern(STRIPES, 0x2A1B10).patternOn(true, true, true, true).scale(1.2)
                .stats(32, 0.28, 8).temper(PREDATOR).food(MEAT).biomes(JUNGLE).spawn(4, 1, 2)
                .drop("minecraft:leather", 0, 2).drop("minecraft:bone", 0, 2);
        q("leopard", "Leopard", "Leopar").body(6, 6, 15).legs(6, 2).head(6, 5, 5).snout(4, 2, 2).ears(SMALL).tail(THIN, 11)
                .colors(0xE2B45A, 0xF3E3C0).pattern(SPOTS, 0x3A2A14).patternOn(true, true, true, true)
                .stats(22, 0.3, 6).temper(PREDATOR).food(MEAT).biomes(JUNGLE, SAVANNA).spawn(3, 1, 2).drop("minecraft:leather", 0, 2);
        q("black_panther", "Black Panther", "Kara Panter").body(6, 6, 15).legs(6, 2).head(6, 5, 5).snout(4, 2, 2).ears(SMALL).tail(THIN, 11)
                .colors(0x1C1C22, 0x26262E).detail(0xE8D040).stats(22, 0.3, 6).temper(PREDATOR).food(MEAT).biomes(JUNGLE).spawn(2, 1, 1)
                .drop("minecraft:leather", 0, 2);
        q("cheetah", "Cheetah", "Çita").body(5, 6, 15).legs(8, 2).head(5, 5, 5).snout(3, 2, 2).ears(SMALL).tail(THIN, 10)
                .colors(0xE6C36E, 0xF7ECD0).pattern(SPOTS, 0x33240F).stats(18, 0.42, 5).temper(PREDATOR)
                .food(MEAT).biomes(SAVANNA).spawn(3, 1, 2).drop("minecraft:leather", 0, 2);
        q("lynx", "Lynx", "Vaşak").body(5, 6, 11).legs(6, 2).head(6, 5, 5).snout(3, 2, 1).ears(LONG).tail(STUB, 2)
                .colors(0xB8946A, 0xE8DCC8).pattern(SPOTS, 0x6A5236).stats(14, 0.3, 3).temper(PREDATOR)
                .food(MEAT).biomes(TAIGA, SNOWY).spawn(3, 1, 2).drop("minecraft:leather", 0, 1);
        q("hyena", "Hyena", "Sırtlan").body(6, 7, 13).legs(7, 2).head(6, 6, 5).snout(4, 3, 3).ears(ROUND).tail(BUSHY, 5)
                .colors(0xA58B63, 0xC2AB86).pattern(SPOTS, 0x4A3A28).stats(18, 0.3, 5).temper(PREDATOR)
                .food(MEAT).biomes(SAVANNA).spawn(4, 2, 4).drop("minecraft:bone", 0, 2);
        q("brown_bear", "Brown Bear", "Boz Ayı").body(10, 10, 18).legs(7, 4).head(8, 7, 6).snout(4, 3, 3).ears(ROUND).tail(STUB, 2)
                .colors(0x6B4A2B, 0x7A5836).stats(40, 0.25, 8).temper(NEUTRAL).sound("polar_bear")
                .food(mix(FISHES, new String[]{"minecraft:sweet_berries", "minecraft:honeycomb"})).biomes(FOREST, TAIGA).spawn(3, 1, 2)
                .drop("minecraft:leather", 0, 2).drop("minecraft:salmon", 0, 2);
        q("crocodile", "Crocodile", "Timsah").body(9, 4, 20).legs(2, 3).head(7, 4, 7).snout(5, 3, 8).tail(THICK, 18)
                .colors(0x4A5D32, 0xC8C890).pattern(SPOTS, 0x34421F).scale(1.2).stats(35, 0.22, 8).temper(AGGRESSIVE)
                .food(MEAT).biomes(SWAMP).spawn(4, 1, 2).drop("minecraft:leather", 1, 3);
        q("komodo_dragon", "Komodo Dragon", "Komodo Ejderi").body(6, 3, 14).legs(2, 2).head(4, 3, 5).snout(3, 2, 4).tail(THICK, 14)
                .colors(0x6B6455, 0x8A8270).pattern(SPOTS, 0x4A453A).stats(20, 0.24, 5).temper(PREDATOR).venom()
                .food(MEAT).biomes(BADLANDS, DESERT).spawn(3, 1, 2).drop("minecraft:leather", 0, 2);

        // ============================== SAVAN ==============================
        q("elephant", "Elephant", "Fil").body(14, 14, 22).legs(12, 5).head(10, 10, 9).headRaise(4).ears(FLAP).trunk(15).tusks(7)
                .tail(TUFT, 8).colors(0x8E8E93, 0x9A9AA0).maneColor(0x55555A).detail(0xF2EAD8).scale(1.6)
                .stats(80, 0.2, 12).temper(NEUTRAL).food(mix(GRASS, FRUIT, new String[]{"minecraft:sugar_cane"}))
                .biomes(SAVANNA).spawn(2, 1, 3).drop("minecraft:leather", 2, 5);
        q("giraffe", "Giraffe", "Zürafa").body(8, 9, 16).legs(16, 3).neck(18, 4, 0.25).head(5, 5, 7).snout(3, 3, 3).ears(SMALL)
                .horns(HORNS, 2).tail(TUFT, 10).colors(0xF0D9A0, 0xF5E6BE).pattern(PATCHES, 0xB5722F).maneColor(0x6A4420)
                .detail(0x7A4E24).scale(1.4).stats(40, 0.27, 0).food(mix(LEAVES, GRASS)).biomes(SAVANNA).spawn(3, 2, 3)
                .drop("minecraft:leather", 1, 3);
        q("zebra", "Zebra", "Zebra").body(7, 8, 16).legs(10, 2).neck(6, 4, 0.55).head(5, 5, 9).ears(SMALL).tail(TUFT, 7)
                .colors(0xF2F2F2, 0xFFFFFF).pattern(STRIPES, 0x1E1E1E).patternOn(true, true, true, false).maneColor(0x1E1E1E)
                .stats(22, 0.34, 0).prey().sound("horse").food(mix(GRASS, new String[]{"minecraft:sugar", "minecraft:apple"}))
                .biomes(SAVANNA).spawn(8, 3, 6).drop("minecraft:leather", 0, 2);
        q("rhino", "Rhinoceros", "Gergedan").body(11, 10, 20).legs(7, 4).head(8, 8, 9).snout(6, 5, 4).horns(NOSE, 5).ears(SMALL)
                .tail(THIN, 6).colors(0x8A857C, 0x969188).detail(0xD8D0BC).scale(1.3).stats(60, 0.24, 10).temper(NEUTRAL)
                .food(GRASS).biomes(SAVANNA).spawn(2, 1, 2).drop("minecraft:leather", 1, 4);
        q("hippo", "Hippopotamus", "Su Aygırı").body(12, 10, 20).legs(5, 4).head(10, 8, 8).snout(10, 6, 5).ears(SMALL).tail(STUB, 2)
                .colors(0x8C7A80, 0xC9A0A0).scale(1.3).stats(55, 0.22, 9).temper(NEUTRAL)
                .food(mix(GRASS, new String[]{"minecraft:melon_slice"})).biomes(RIVER, SWAMP).spawn(2, 1, 3).drop("minecraft:leather", 1, 3);
        q("wildebeest", "Wildebeest", "Gnu").body(8, 8, 16).legs(9, 2).head(6, 7, 7).horns(HORNS, 4).withMane(0x1E1E22)
                .tail(TUFT, 8).colors(0x5C5A5E, 0x6A686C).headColor(0x2A2A2E).stats(24, 0.32, 4).temper(NEUTRAL).prey().sound("cow")
                .food(GRASS).biomes(SAVANNA).spawn(6, 3, 6).drop("minecraft:beef", 1, 3).drop("minecraft:leather", 0, 2);
        q("antelope", "Antelope", "Ceylan").body(5, 5, 12).legs(9, 2).neck(5, 3, 0.3).head(4, 4, 5).snout(3, 2, 2).ears(SMALL)
                .horns(HORNS, 5).tail(STUB, 2).colors(0xD2A064, 0xF5EEDD).detail(0x3A3028).stats(12, 0.4, 0).prey()
                .food(mix(GRASS, VEG)).biomes(SAVANNA, DESERT).spawn(8, 3, 5).drop("minecraft:mutton", 1, 2).drop("minecraft:leather", 0, 1);
        q("meerkat", "Meerkat", "Mirket").body(3, 6, 4).legs(2, 1).head(3, 3, 3).headRaise(4).snout(2, 1, 1).tail(THIN, 5)
                .colors(0xC8A878, 0xE6D4B0).stats(6, 0.3, 0).prey().food(SEEDS).biomes(DESERT, SAVANNA, BADLANDS).spawn(6, 2, 5);
        q("fennec_fox", "Fennec Fox", "Çöl Tilkisi").body(3, 3, 6).legs(3, 1).head(4, 3, 3).snout(2, 1, 2).ears(LONG).tail(BUSHY, 5)
                .colors(0xEBD5A8, 0xFAF0DC).stats(8, 0.34, 0).sound("fox").food(mix(FRUIT, new String[]{"minecraft:chicken"}))
                .biomes(DESERT).spawn(5, 1, 3);

        // ============================== ORMAN ==============================
        q("deer", "Deer", "Geyik").body(6, 6, 13).legs(9, 2).neck(5, 3, 0.4).head(4, 4, 6).snout(3, 2, 2).ears(LONG)
                .horns(ANTLERS, 6).tail(STUB, 2).colors(0xA9744A, 0xE8D9C0).detail(0xD8C8A8).stats(14, 0.34, 0).prey()
                .food(mix(GRASS, VEG)).biomes(FOREST, PLAINS).spawn(9, 2, 4).drop("minecraft:mutton", 1, 2).drop("minecraft:leather", 0, 2);
        q("moose", "Moose", "Sığın").body(9, 10, 18).legs(13, 3).neck(5, 5, 0.6).head(6, 6, 9).snout(5, 4, 3).ears(SMALL)
                .horns(ANTLERS, 7).hump(3).tail(STUB, 2).colors(0x4E3A2A, 0x5C4634).detail(0xC8B898).scale(1.3)
                .stats(40, 0.26, 8).temper(NEUTRAL).food(mix(GRASS, LEAVES)).biomes(TAIGA, new String[]{"minecraft:snowy_taiga"}).spawn(3, 1, 2)
                .drop("minecraft:beef", 1, 3).drop("minecraft:leather", 0, 2);
        q("boar", "Wild Boar", "Yaban Domuzu").body(7, 7, 13).legs(5, 2).head(6, 6, 6).snout(4, 3, 3).ears(SMALL).tusks(2)
                .tail(THIN, 3).colors(0x4F3B2A, 0x5C4634).pattern(BACK, 0x2E2218).detail(0xEDE4D0).stats(16, 0.27, 4).temper(NEUTRAL)
                .prey().sound("pig").food(VEG).biomes(FOREST, TAIGA).spawn(7, 2, 4).drop("minecraft:porkchop", 1, 3);
        q("raccoon", "Raccoon", "Rakun").body(5, 5, 9).legs(3, 2).head(5, 4, 4).snout(3, 2, 2).ears(SMALL).tail(BUSHY, 7)
                .colors(0x8A8A8A, 0xB0B0B0).pattern(STRIPES, 0x2A2A2A).patternOn(false, false, false, true)
                .stats(8, 0.28, 0).food(mix(FRUIT, FISHES)).biomes(FOREST).spawn(6, 1, 3);
        q("squirrel", "Squirrel", "Sincap").body(3, 3, 5).legs(2, 1).head(3, 3, 3).ears(SMALL).tail(BUSHY, 5).tailUp()
                .colors(0xB5651D, 0xF0E0C8).stats(5, 0.32, 0).prey().food(SEEDS).biomes(FOREST, TAIGA).spawn(9, 1, 3);
        q("hedgehog", "Hedgehog", "Kirpi").body(5, 4, 6).legs(1, 1).head(3, 3, 3).snout(1, 1, 2)
                .colors(0x6B5A45, 0xD8C8A8).pattern(SPOTS, 0x2E261C).headColor(0xD8C8A8).stats(6, 0.2, 0)
                .food(mix(FRUIT, SEEDS)).biomes(FOREST, PLAINS).spawn(6, 1, 2);
        q("badger", "Badger", "Porsuk").body(6, 5, 10).legs(3, 2).head(5, 4, 5).snout(3, 2, 2).ears(SMALL).tail(STUB, 2)
                .colors(0x4A4A4A, 0x2A2A2A).pattern(BACK, 0xF0F0F0).patternOn(false, true, false, false)
                .stats(12, 0.26, 3).temper(NEUTRAL).food(mix(VEG, FRUIT)).biomes(FOREST, PLAINS).spawn(5, 1, 2);
        q("skunk", "Skunk", "Kokarca").body(5, 5, 8).legs(2, 2).head(4, 4, 4).snout(2, 2, 2).ears(SMALL).tail(BUSHY, 7).tailUp()
                .colors(0x1C1C1C, 0x1C1C1C).pattern(BACK, 0xF4F4F4).patternOn(true, true, false, true)
                .stats(8, 0.25, 0).food(mix(FRUIT, SEEDS)).biomes(FOREST).spawn(4, 1, 2);
        q("mouse", "Mouse", "Fare").body(2, 2, 4).legs(1, 1).head(3, 2, 3).ears(ROUND).tail(THIN, 4)
                .colors(0x9A9A9A, 0xD8D8D8).maneColor(0xE0A0A0).stats(3, 0.3, 0).prey().food(SEEDS).biomes(PLAINS, FOREST).spawn(8, 1, 3);
        q("beaver", "Beaver", "Kunduz").body(6, 5, 9).legs(2, 2).head(5, 4, 4).snout(3, 2, 1).ears(SMALL).tail(THICK, 6)
                .colors(0x6A4628, 0x80583A).stats(10, 0.24, 0).food(new String[]{"minecraft:oak_log", "minecraft:birch_log", "minecraft:stick", "minecraft:apple"})
                .biomes(RIVER, SWAMP).spawn(5, 1, 3);
        q("otter", "Otter", "Su Samuru").body(4, 4, 11).legs(2, 2).head(4, 3, 4).snout(2, 2, 1).ears(SMALL).tail(THICK, 8)
                .colors(0x5E4632, 0xC8B090).stats(10, 0.3, 0).food(FISHES).biomes(RIVER).spawn(6, 1, 3);
        q("capybara", "Capybara", "Kapibara").body(7, 7, 12).legs(4, 2).head(6, 6, 7).ears(SMALL)
                .colors(0xA67B4B, 0xB88C5C).stats(14, 0.24, 0).prey().food(mix(GRASS, FRUIT)).biomes(SWAMP, RIVER, JUNGLE).spawn(5, 2, 4);

        // ============================== ORMAN (TROPIKAL) ==============================
        q("gorilla", "Gorilla", "Goril").body(10, 10, 12).legs(8, 4).head(8, 8, 7).headRaise(5).snout(5, 3, 1)
                .colors(0x2B2B2E, 0x3A3A3E).stats(40, 0.26, 9).temper(NEUTRAL)
                .food(mix(FRUIT, BAMBOO)).biomes(JUNGLE).spawn(3, 1, 3);
        q("monkey", "Monkey", "Maymun").body(4, 5, 8).legs(5, 2).head(5, 5, 5).headRaise(3).snout(3, 2, 1).ears(ROUND).tail(THIN, 9)
                .colors(0x8A5A2B, 0xE0C090).headColor(0x9A6A3B).stats(10, 0.32, 0)
                .food(mix(FRUIT, new String[]{"minecraft:cocoa_beans"})).biomes(JUNGLE).spawn(8, 2, 5);
        q("koala", "Koala", "Koala").body(5, 6, 6).legs(3, 2).head(6, 5, 4).headRaise(3).snout(2, 2, 1).ears(ROUND)
                .colors(0x9A9AA0, 0xE8E8E8).stats(8, 0.18, 0).food(LEAVES).biomes(new String[]{"minecraft:sparse_jungle", "minecraft:birch_forest"}).spawn(4, 1, 2);
        q("red_panda", "Red Panda", "Kızıl Panda").body(5, 5, 10).legs(3, 2).head(6, 5, 4).snout(3, 2, 1).ears(ROUND).tail(BUSHY, 9)
                .colors(0xB5462A, 0x2A1A14).pattern(STRIPES, 0xE8C8A0).patternOn(false, false, false, true).legColor(0x2A1A14)
                .stats(10, 0.26, 0).food(mix(BAMBOO, FRUIT)).biomes(new String[]{"minecraft:bamboo_jungle", "minecraft:cherry_grove"}).spawn(6, 1, 2);
        q("lizard", "Lizard", "Kertenkele").body(2, 1, 5).legs(1, 1).head(2, 2, 2).tail(THICK, 5)
                .colors(0x5FA83C, 0xC8E08C).stats(4, 0.3, 0).prey().food(SEEDS).biomes(JUNGLE, DESERT, BADLANDS).spawn(6, 1, 2);

        // ============================== SOGUK VE DAG ==============================
        q("reindeer", "Reindeer", "Ren Geyiği").body(7, 7, 14).legs(9, 2).neck(5, 4, 0.5).head(5, 5, 6).snout(3, 3, 2).ears(SMALL)
                .horns(ANTLERS, 7).tail(STUB, 2).colors(0x8C7B6B, 0xE6E0D6).detail(0xD8CCB8).stats(18, 0.3, 0).prey()
                .food(GRASS).biomes(SNOWY).spawn(7, 2, 5).drop("minecraft:mutton", 1, 2).drop("minecraft:leather", 0, 2);
        q("bison", "Bison", "Bizon").body(10, 11, 18).legs(7, 3).head(8, 8, 6).snout(5, 4, 2).horns(HORNS, 3).hump(4)
                .tail(TUFT, 7).colors(0x5A3F2A, 0x4A3322).headColor(0x3B2818).maneColor(0x2A1C10).scale(1.2)
                .stats(40, 0.24, 7).temper(NEUTRAL).sound("cow").food(GRASS).biomes(PLAINS).spawn(4, 2, 4)
                .drop("minecraft:beef", 1, 3).drop("minecraft:leather", 0, 2);
        q("yak", "Yak", "Yak").body(9, 10, 16).legs(6, 3).head(7, 7, 6).snout(5, 4, 2).horns(HORNS, 4).hump(2).tail(BUSHY, 6)
                .colors(0x2E2622, 0x3A302A).stats(30, 0.22, 5).temper(NEUTRAL).sound("cow").food(GRASS).biomes(MOUNTAIN).spawn(4, 2, 3)
                .drop("minecraft:beef", 1, 3).drop("minecraft:leather", 0, 2);
        q("seal", "Seal", "Fok").body(7, 6, 14).legs(1, 3).head(6, 5, 5).snout(4, 3, 2).tail(THICK, 5)
                .colors(0x7C8A96, 0xC8D2DC).pattern(SPOTS, 0x5E6A76).stats(16, 0.18, 0).food(FISHES).biomes(ICE_COAST, new String[]{"minecraft:stony_shore"})
                .spawn(6, 2, 4);
        q("walrus", "Walrus", "Mors").body(11, 9, 18).legs(1, 4).head(8, 7, 6).snout(6, 4, 2).tusks(5).tail(THICK, 5)
                .colors(0x8A6248, 0x9A7258).detail(0xF2EAD8).scale(1.2).stats(40, 0.15, 7).temper(NEUTRAL).food(FISHES)
                .biomes(ICE_COAST).spawn(3, 1, 3);

        // ============================== KUSLAR (YERDE) ==============================
        b("ostrich", "Ostrich", "Devekuşu").body(7, 7, 10).legs(10, 2).neck(10, 2, 0).beak(2, 1, 3).wings(7).fishTail(3, 0)
                .colors(0x2A2420, 0x2A2420).headColor(0xD8B8A8).detail(0xD8B8A8).maneColor(0xF0F0F0).scale(1.3)
                .stats(20, 0.4, 4).temper(NEUTRAL).food(SEEDS).biomes(SAVANNA).spawn(4, 2, 4)
                .drop("minecraft:feather", 1, 3).drop("minecraft:chicken", 1, 2);
        b("penguin", "Penguin", "Penguen").body(6, 9, 5).legs(1, 1).head(5, 4, 4).beak(2, 1, 2).wings(4).fishTail(1, 0)
                .colors(0x22262E, 0xF4F4F4).frontBelly().detail(0xF0A020).maneColor(0x22262E)
                .stats(8, 0.18, 0).food(FISHES).biomes(ICE_COAST, new String[]{"minecraft:snowy_plains"}).spawn(8, 3, 6)
                .drop("minecraft:feather", 0, 2);
        b("flamingo", "Flamingo", "Flamingo").body(5, 5, 7).legs(10, 1).neck(8, 2, 0).beak(2, 2, 3).wings(5).fishTail(2, 0)
                .colors(0xF28CA0, 0xF7A8B8).detail(0x2A2A2A).legColor(0xF0A0B0).maneColor(0xE86C88)
                .stats(8, 0.24, 0).food(FISHES).biomes(SWAMP, BEACH).spawn(5, 3, 6).drop("minecraft:feather", 0, 2);
        b("duck", "Duck", "Ördek").body(4, 4, 6).legs(2, 1).neck(2, 2, 0).beak(2, 1, 2).wings(4).fishTail(2, 0)
                .colors(0x7A5C3C, 0xC8B8A0).headColor(0x1E6B3A).detail(0xF0A020).stats(5, 0.24, 0).prey().sound("chicken")
                .food(SEEDS).biomes(RIVER, SWAMP, PLAINS).spawn(8, 2, 4).drop("minecraft:chicken", 1, 1).drop("minecraft:feather", 0, 2);
        b("goose", "Goose", "Kaz").body(5, 5, 8).legs(3, 1).neck(5, 2, 0).beak(2, 1, 2).wings(6).fishTail(2, 0)
                .colors(0xE8E8E8, 0xFFFFFF).detail(0xF08020).stats(8, 0.24, 2).temper(NEUTRAL).sound("chicken")
                .food(SEEDS).biomes(PLAINS, RIVER).spawn(5, 2, 4).drop("minecraft:chicken", 1, 1).drop("minecraft:feather", 0, 2);
        b("swan", "Swan", "Kuğu").body(6, 5, 9).legs(2, 1).neck(7, 2, 0).beak(2, 1, 2).wings(7).fishTail(2, 0)
                .colors(0xFAFAFA, 0xFFFFFF).detail(0xF07818).stats(10, 0.22, 2).temper(NEUTRAL)
                .food(SEEDS).biomes(RIVER).spawn(3, 1, 2).drop("minecraft:feather", 1, 3);
        b("turkey", "Turkey", "Hindi").body(7, 7, 8).legs(3, 1).neck(3, 2, 0).beak(1, 1, 2).wings(6).fishTail(5, 0).tailUp()
                .colors(0x4A3020, 0x5A3C28).headColor(0xC84030).detail(0xD8B060).maneColor(0x3A2418)
                .stats(8, 0.24, 0).prey().sound("chicken").food(SEEDS).biomes(FOREST, PLAINS).spawn(6, 2, 4)
                .drop("minecraft:chicken", 1, 2).drop("minecraft:feather", 0, 2);
        b("peacock", "Peacock", "Tavus Kuşu").body(5, 5, 8).legs(4, 1).neck(5, 2, 0).beak(1, 1, 2).wings(6).fishTail(14, 0)
                .colors(0x1450A0, 0x1A5CB8).detail(0xC8B890).maneColor(0x1E8C5A).pattern(SPOTS, 0x2848C8).patternOn(false, false, false, true)
                .stats(8, 0.24, 0).food(SEEDS).biomes(JUNGLE).spawn(4, 1, 2).drop("minecraft:feather", 1, 3);

        // ============================== KUSLAR (UCAN) ==============================
        b("eagle", "Eagle", "Kartal").body(5, 5, 8).legs(2, 1).head(4, 4, 4).beak(2, 2, 2).wings(10).fishTail(4, 0).fly()
                .colors(0x4A3220, 0x5A4030).headColor(0xF2F2F2).detail(0xF0C020).maneColor(0x3A2618)
                .stats(14, 0.28, 4).temper(NEUTRAL).food(mix(MEAT, FISHES)).biomes(MOUNTAIN, TAIGA).spawn(3, 1, 1)
                .drop("minecraft:feather", 1, 3);
        b("owl", "Owl", "Baykuş").body(5, 6, 5).legs(1, 1).head(6, 5, 5).beak(1, 1, 1).wings(5).fishTail(2, 0).fly()
                .colors(0x8A6A45, 0xD8C8A0).frontBelly().pattern(SPOTS, 0x5A4228).detail(0xE8C040).maneColor(0x6A4E30)
                .stats(8, 0.25, 0).food(MEAT).biomes(FOREST, TAIGA).spawn(4, 1, 1).drop("minecraft:feather", 0, 2);
        b("crow", "Crow", "Karga").body(3, 3, 5).legs(2, 1).beak(1, 1, 2).wings(5).fishTail(3, 0).fly()
                .colors(0x16161A, 0x1E1E24).detail(0x2A2A2A).maneColor(0x101014)
                .stats(5, 0.25, 0).food(SEEDS).biomes(PLAINS, FOREST).spawn(6, 1, 3).drop("minecraft:feather", 0, 1);
        b("pigeon", "Pigeon", "Güvercin").body(3, 3, 5).legs(1, 1).beak(1, 1, 1).wings(4).fishTail(2, 0).fly()
                .colors(0x8E96A0, 0xB0B8C0).headColor(0x5A6A78).detail(0xD06060).maneColor(0x6A727C)
                .stats(4, 0.25, 0).prey().food(SEEDS).biomes(PLAINS, FOREST).spawn(7, 2, 5).drop("minecraft:feather", 0, 1);
        b("seagull", "Seagull", "Martı").body(3, 3, 6).legs(2, 1).beak(1, 1, 2).wings(6).fishTail(2, 0).fly()
                .colors(0xF4F4F4, 0xFFFFFF).detail(0xF0B020).maneColor(0xA0A8B0)
                .stats(5, 0.25, 0).food(FISHES).biomes(BEACH).spawn(10, 2, 4).drop("minecraft:feather", 0, 1);
        b("toucan", "Toucan", "Tukan").body(4, 4, 6).legs(2, 1).head(4, 4, 4).beak(2, 3, 5).wings(5).fishTail(3, 0).fly()
                .colors(0x141414, 0xF8E040).frontBelly().detail(0xF08018).maneColor(0x141414)
                .stats(6, 0.25, 0).food(FRUIT).biomes(JUNGLE).spawn(6, 1, 2).drop("minecraft:feather", 0, 2);
        b("vulture", "Vulture", "Akbaba").body(5, 6, 7).legs(3, 1).neck(3, 2, 0).beak(2, 2, 2).wings(10).fishTail(3, 0).fly()
                .colors(0x3A2E28, 0x4A3C34).headColor(0xD09090).detail(0x5A5048).maneColor(0x2A201C)
                .stats(12, 0.25, 0).food(mix(MEAT, new String[]{"minecraft:rotten_flesh"})).biomes(DESERT, BADLANDS, SAVANNA).spawn(3, 1, 2)
                .drop("minecraft:feather", 1, 2);
        b("sparrow", "Sparrow", "Serçe").body(2, 2, 3).legs(1, 1).head(2, 2, 2).beak(1, 1, 1).wings(3).fishTail(2, 0).fly()
                .colors(0x9A6E48, 0xE0D0B8).frontBelly().detail(0x4A4038).maneColor(0x6A4A30)
                .stats(3, 0.25, 0).prey().food(SEEDS).biomes(PLAINS, FOREST).spawn(9, 2, 5);
        b("stork", "Stork", "Leylek").body(5, 5, 8).legs(9, 1).neck(7, 2, 0).beak(1, 1, 5).wings(8).fishTail(2, 0).fly()
                .colors(0xF4F4F4, 0xFFFFFF).detail(0xE03020).maneColor(0x1A1A1A)
                .stats(8, 0.25, 0).food(FISHES).biomes(SWAMP, PLAINS).spawn(3, 1, 2).drop("minecraft:feather", 0, 2);

        // ============================== DENIZ ==============================
        f("shark", "Shark", "Köpekbalığı").body(8, 8, 20).fishTail(10, 11).dorsal(6).fins(6, 4)
                .colors(0x6E7C8A, 0xE8ECF0).stats(30, 1.2, 8).temper(AGGRESSIVE).biomes(OCEAN).spawn(3, 1, 1)
                .drop("minecraft:cod", 1, 3);
        f("orca", "Orca", "Katil Balina").body(10, 10, 22).fishTail(10, 12).fluke().dorsal(8).fins(6, 5)
                .colors(0x16161A, 0xF4F4F4).scale(1.3).stats(50, 1.2, 9).temper(NEUTRAL).biomes(COLD_OCEAN).spawn(2, 1, 3);
        f("whale", "Blue Whale", "Mavi Balina").body(12, 11, 30).fishTail(14, 16).fluke().dorsal(2).fins(8, 5)
                .colors(0x3E5C7E, 0xA8BCCC).scale(2.5).stats(120, 0.8, 0).biomes(DEEP_OCEAN).spawn(1, 1, 1);
        f("swordfish", "Swordfish", "Kılıç Balığı").body(4, 6, 14).nose(10).fishTail(5, 8).dorsal(5).fins(3, 2)
                .colors(0x3A4E7A, 0xD8E0E8).stats(16, 1.5, 5).temper(NEUTRAL).biomes(OCEAN).spawn(3, 1, 2).drop("minecraft:cod", 1, 2);
        f("tuna", "Tuna", "Ton Balığı").body(4, 6, 12).fishTail(4, 7).dorsal(3).fins(3, 2)
                .colors(0x2E4A78, 0xD0D8E0).stats(10, 1.3, 0).prey().biomes(OCEAN, COLD_OCEAN).spawn(8, 3, 6).drop("minecraft:cod", 1, 2);
        f("piranha", "Piranha", "Pirana").body(2, 4, 5).fishTail(2, 4).dorsal(2).fins(1, 1)
                .colors(0x6A7A6A, 0xC83828).stats(5, 1.2, 2).temper(AGGRESSIVE).biomes(JUNGLE, new String[]{"minecraft:mangrove_swamp"}).spawn(6, 3, 6);
        f("catfish", "Catfish", "Yayın Balığı").body(5, 4, 11).fishTail(5, 5).dorsal(2).fins(3, 2)
                .colors(0x5A4E3A, 0xC8BCA0).stats(8, 0.9, 0).biomes(RIVER, SWAMP).spawn(6, 1, 3).drop("minecraft:cod", 1, 2);
        f("manta_ray", "Manta Ray", "Vatoz").body(10, 2, 10).flat().fishTail(10, 0).fins(8, 8)
                .colors(0x1E2A3A, 0xE8E8E8).stats(16, 0.9, 0).biomes(WARM_OCEAN).spawn(3, 1, 2);

        // ============================== DIGER ==============================
        reg(new AnimalDef(SNAKE, "snake", "Snake", "Yılan")).body(2, 2, 4).segments(6)
                .colors(0x4A7A2A, 0xC8D890).pattern(STRIPES, 0x2A4A18).stats(8, 0.22, 2).temper(NEUTRAL).venom()
                .food(new String[]{"minecraft:rabbit", "minecraft:chicken", "minecraft:egg"}).biomes(JUNGLE, SWAMP, DESERT).spawn(4, 1, 1);
        reg(new AnimalDef(CRAB, "crab", "Crab", "Yengeç")).body(6, 3, 5).legs(4, 1)
                .colors(0xD8442A, 0xF0C8A0).maneColor(0xE85A3A).stats(6, 0.22, 1).temper(NEUTRAL)
                .food(FISHES).biomes(BEACH).spawn(10, 1, 3);
    }

    private Animals() {}
}
