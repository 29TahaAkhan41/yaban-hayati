package com.yabanhayati.gen;

import com.yabanhayati.def.AnimalDef;
import com.yabanhayati.def.AnimalDef.Shape;
import com.yabanhayati.def.AnimalDef.Temper;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Her hayvan icin ayri bir et ikonu (16x16) cizer.
 * Kesim sekli hayvanin turune gore secilir; deri kismi hayvanin kendi rengi ve deseniyle boyanir;
 * et rengi, yag damarlari ve yon de hayvana gore degisir.
 *
 * Maske harfleri: o=dis cizgi, m=et, h=parlak et, d=koyu et cizgisi, s=deri/kabuk (hayvanin deseni),
 * f=yag, b=kemik, e=goz.
 */
final class MeatIcons {
    enum Cut { STEAK, RIBS, CHOP, HAM, MORSEL, CUTLET, TAIL, BLUBBER, DRUMSTICK, WING, BREAST, WHOLE_BIRD, FILLET, FISH_STEAK, WHOLE_FISH, RAY_WING, CLAW, COIL }

    static final Map<Cut, String[]> MASKS = Map.ofEntries(
            Map.entry(Cut.STEAK, new String[]{
                    "................", "................", ".....oooooo.....", "...oossssssoo...", "..ossmmmmmmsso..", ".osmmmhhmmmmmso.",
                    ".osmmhmmmmmmmso.", ".ofmmmmmmbbmmfo.", ".ofmmmmmmbbmmfo.", ".ofmmmmmmmmmmfo.", "..ofmmmmmmmmfo..", "..offmmmmmmffo..",
                    "...ooffffffoo...", ".....oooooo.....", "................", "................"}),
            Map.entry(Cut.RIBS, new String[]{
                    "................", "...b..b..b..b...", "...b..b..b..b...", ".oobooboobooboo.", ".omdmmdmmdmmdso.", ".omdmmdmmdmmdso.",
                    ".ohdmmdmmdmmdso.", ".omdmmdmmdmmdso.", ".omdmmdmmdmmdso.", ".omdmmdmmdmmdso.", ".omdmmdmmdmmdso.", ".offfffffffffso.",
                    ".ooooooooooooo..", "................", "................", "................"}),
            Map.entry(Cut.CHOP, new String[]{
                    "................", ".......ooooo....", ".....oosssssoo..", "....osmmmmmmmso.", "....ommhhmmmmfo.", "....ommhmmmmmfo.",
                    "....ommmmmmmmfo.", "....ofmmmmmmfo..", "...obbfffffoo...", "..obboooooo.....", ".obbo...........", ".obo............",
                    "obbo............", "obo.............", ".o..............", "................"}),
            Map.entry(Cut.HAM, new String[]{
                    "................", "...oooooo.......", "..ossssssoo.....", ".osmmmmmmsso....", ".ommhhmmmmmso...", "osmmhmmmmmmmo...",
                    "osmmmmmmmmmmfo..", "osmmmmmmmmmmfo..", ".ommmmmmmmmmfo..", ".offmmmmmmmffo..", "..offfmmmfffobo.", "...oofffffoobbo.",
                    ".....oooooobbbbo", "............obbo", ".............oo.", "................"}),
            Map.entry(Cut.MORSEL, new String[]{
                    "................", "................", "................", "................", ".....oooooo.....", "....ossssmmo....",
                    "....osmmhmmo....", "....ommhmmmo....", "....ommmmmfo....", "....ommmmffo....", ".....oooooo.....", "................",
                    "................", "................", "................", "................"}),
            Map.entry(Cut.CUTLET, new String[]{
                    "................", "................", "................", "....ooooooo.....", "..oosssssssoo...", ".osmmmmmmmmmso..",
                    ".ommhhmmmmmmmo..", ".ommmmmmmdmmmfo.", "..ommmdmmmmmmfo.", "..offmmmmmmffo..", "...ooffffffoo...", ".....oooooo.....",
                    "................", "................", "................", "................"}),
            Map.entry(Cut.TAIL, new String[]{
                    "................", "..............o.", "............ooso", "..........oossmo", "........oossmmo.", "......oossmmmo..",
                    "....oossmmmmmo..", "...ossmmhmmmo...", "..osmmmhmmmmo...", ".osmmmmmmmmo....", ".osmmmmmffo.....", ".ommmmfffo......",
                    ".offfffoo.......", "..ooooo.........", "................", "................"}),
            Map.entry(Cut.BLUBBER, new String[]{
                    "................", "................", "..oooooooooooo..", ".osssssssssssso.", ".osssssssssssso.", ".offffffffffffo.",
                    ".offffffffffffo.", ".offffffffffffo.", ".ommmmmmmmmmmmo.", ".ommhhmmmmmmmmo.", ".ommmmmmmmmmmmo.", ".ommmmmmmmmmmmo.",
                    "..oooooooooooo..", "................", "................", "................"}),
            Map.entry(Cut.DRUMSTICK, new String[]{
                    "................", ".........oooo...", "........osssso..", ".......osmhmmso.", ".......ommmmmmo.", "......ommmmmmmo.",
                    "......ommmmmmo..", ".....offmmmmo...", "....obbfoooo....", "...obbo.........", ".oobbo..........", "obbbo...........",
                    "obbbbo..........", ".oobo...........", "................", "................"}),
            Map.entry(Cut.WING, new String[]{
                    "................", "................", "..oooo..........", ".ossssoo........", ".osmmmsso.......", ".ommhmmmso......",
                    "..ommmmmmso.....", "...ommmmmmo.oo..", "....ommmmmoomo..", ".....ommmmmmmo..", "......oommmmo...", "........ommo....",
                    ".........obo....", "..........o.....", "................", "................"}),
            Map.entry(Cut.BREAST, new String[]{
                    "................", "................", ".....ooooo......", "...oosssssoo....", "..ossmmmmmsso...", "..osmmhhmmmmso..",
                    ".osmmmhmmmmmmo..", ".ommmmmmmmmmmo..", ".ommmmmmmmmmfo..", "..ommmmmmmmfo...", "...ommmmmffo....", "....oommffo.....",
                    "......oooo......", "................", "................", "................"}),
            Map.entry(Cut.WHOLE_BIRD, new String[]{
                    "................", "................", "................", ".....oooooo.....", "...oossssssoo...", "..ossmmhhmmsso..",
                    "..osmmhmmmmmso..", ".oommmmmmmmmmoo.", "obommmmmmmmmmobo", "obboffmmmmffobbo", ".oooooffffooooo.", ".....oooooo.....",
                    "................", "................", "................", "................"}),
            Map.entry(Cut.FILLET, new String[]{
                    "................", "................", "............oo..", "..........oommo.", "........oommmso.", "......oommmmsso.",
                    "....oommhmmsso..", "...ommmhmmsso...", "..ommmmmmsso....", ".ommmmmmsso.....", ".ommmmssso......", ".osssssoo.......",
                    "..ooooo.........", "................", "................", "................"}),
            Map.entry(Cut.FISH_STEAK, new String[]{
                    "................", "................", "....oooooooo....", "..oossssssssoo..", ".osmmmmmmmmmmso.", ".osmmhmmmmmmmso.",
                    ".osmmmmmbmmmmso.", ".osmmmmbbbmmmso.", ".osmmmmmbmmmmso.", ".osmmmmmmmmmmso.", "..ossmmmffmsso..", "...oosssssoo....",
                    ".....ooooo......", "................", "................", "................"}),
            Map.entry(Cut.WHOLE_FISH, new String[]{
                    "................", "................", "................", "......oooo....o.", "....oossssoo.oso", "...ossssssssosso",
                    "..oseessssssssso", "..ossssmmmmssso.", "..ommmmmmmmmosso", "...ommmmmmmo.oso", "....oommmoo...o.", "......ooo.......",
                    "................", "................", "................", "................"}),
            Map.entry(Cut.RAY_WING, new String[]{
                    "................", "................", ".o..............", ".oso............", ".osso...........", ".osmsoo.........",
                    ".osmmssoo.......", ".osmmhmssoo.....", ".osmmmmmmssoo...", ".osmmmmmmmmssoo.", ".offfffffffffso.", ".oooooooooooooo.",
                    "................", "................", "................", "................"}),
            Map.entry(Cut.CLAW, new String[]{
                    "................", ".....oo...oo....", "....osso.osso...", "...ossso.ossso..", "...osssoosssso..", "...osssssssso...",
                    "....ossssssso...", "....osssssso....", ".....ossmmo.....", ".....osmmmo.....", "....osmmmmo.....", "....ommmmo......",
                    "....ommho.......", ".....ooo........", "................", "................"}),
            Map.entry(Cut.COIL, new String[]{
                    "................", "................", "....oooooooo....", "...ossssssssoo..", "..osmmmmmmmmsso.", "..osmooooooomso.",
                    "..osmo.....omso.", "..osmo.oooosmso.", "..osmoosssssmo..", "..osmmmmmmmmo...", "...ossssssso....", "....ooooooo.....",
                    "................", "................", "................", "................"}));

    /** Tur bazinda elle secilen kesimler; listede olmayanlar cut() icindeki kurallarla belirlenir. */
    static final Map<String, Cut> SPECIAL = Map.ofEntries(
            Map.entry("crocodile", Cut.TAIL), Map.entry("komodo_dragon", Cut.TAIL), Map.entry("lizard", Cut.TAIL),
            Map.entry("seal", Cut.BLUBBER), Map.entry("walrus", Cut.BLUBBER), Map.entry("whale", Cut.BLUBBER), Map.entry("orca", Cut.BLUBBER),
            Map.entry("shark", Cut.FISH_STEAK), Map.entry("tuna", Cut.FISH_STEAK), Map.entry("swordfish", Cut.FISH_STEAK),
            Map.entry("piranha", Cut.WHOLE_FISH), Map.entry("catfish", Cut.FILLET), Map.entry("manta_ray", Cut.RAY_WING),
            Map.entry("elephant", Cut.RIBS), Map.entry("rhino", Cut.RIBS), Map.entry("hippo", Cut.RIBS), Map.entry("bison", Cut.RIBS),
            Map.entry("yak", Cut.RIBS), Map.entry("moose", Cut.RIBS), Map.entry("giraffe", Cut.RIBS),
            Map.entry("deer", Cut.CHOP), Map.entry("reindeer", Cut.CHOP), Map.entry("antelope", Cut.CHOP), Map.entry("zebra", Cut.CHOP),
            Map.entry("wildebeest", Cut.CHOP),
            Map.entry("boar", Cut.HAM), Map.entry("brown_bear", Cut.HAM), Map.entry("gorilla", Cut.HAM), Map.entry("capybara", Cut.HAM),
            Map.entry("mouse", Cut.MORSEL), Map.entry("squirrel", Cut.MORSEL), Map.entry("hedgehog", Cut.MORSEL), Map.entry("meerkat", Cut.MORSEL),
            Map.entry("fennec_fox", Cut.MORSEL), Map.entry("skunk", Cut.MORSEL),
            Map.entry("raccoon", Cut.CUTLET), Map.entry("beaver", Cut.CUTLET), Map.entry("otter", Cut.CUTLET), Map.entry("badger", Cut.CUTLET),
            Map.entry("monkey", Cut.CUTLET), Map.entry("koala", Cut.CUTLET), Map.entry("red_panda", Cut.CUTLET),
            Map.entry("ostrich", Cut.DRUMSTICK), Map.entry("turkey", Cut.DRUMSTICK), Map.entry("peacock", Cut.DRUMSTICK), Map.entry("flamingo", Cut.DRUMSTICK),
            Map.entry("duck", Cut.BREAST), Map.entry("goose", Cut.BREAST), Map.entry("swan", Cut.BREAST), Map.entry("penguin", Cut.BREAST),
            Map.entry("sparrow", Cut.WHOLE_BIRD), Map.entry("pigeon", Cut.WHOLE_BIRD), Map.entry("crow", Cut.WHOLE_BIRD));

    static Cut cut(AnimalDef d) {
        Cut c = SPECIAL.get(d.id);
        if (c != null) return c;
        return switch (d.shape) {
            case CRAB -> Cut.CLAW;
            case SNAKE -> Cut.COIL;
            case FISH -> Cut.FILLET;
            case BIRD -> Cut.WING;
            default -> Cut.STEAK;
        };
    }

    /** Cig etin temel rengi: kirmizi et, beyaz et, balik... */
    static int rawColor(AnimalDef d, Cut cut) {
        switch (d.id) {
            case "tuna": return 0xB0303C;
            case "whale": return 0x8E2430;
            case "orca": return 0x7A2034;
            case "shark": return 0xE6D2C8;
            case "swordfish": return 0xF0C4A8;
            case "catfish": return 0xF2E0D0;
            case "piranha": return 0xE89C8C;
            case "manta_ray": return 0xE0B4B0;
            case "crocodile": return 0xEACFC2;
            case "komodo_dragon": return 0xDDC2B0;
            case "lizard": return 0xDCE2BC;
            case "snake": return 0xEAD8C6;
            case "crab": return 0xF6EEE4;
            case "duck": case "goose": case "ostrich": case "swan": return 0xB8444A;
            case "brown_bear": return 0x8E2A2A;
            case "seal": case "walrus": return 0x6E2030;
            default: break;
        }
        if (d.shape == Shape.BIRD) return 0xF0B4A4;
        if (cut == Cut.HAM || d.id.equals("hippo")) return 0xE07478;
        if (cut == Cut.MORSEL || cut == Cut.CUTLET) return 0xE08484;
        if (d.temper == Temper.PREDATOR || d.temper == Temper.AGGRESSIVE) return 0xA82C30;
        return 0xC83A3A;
    }

    static int cookedColor(int raw) {
        int luma = (((raw >> 16) & 255) * 3 + ((raw >> 8) & 255) * 6 + (raw & 255)) / 10;
        if (luma > 190) return 0xD9A85A;   // beyaz et -> altin sarisi
        if (luma > 140) return 0xB8702E;   // pembe et / kumes hayvani
        if (luma > 85) return 0x8A4826;    // kirmizi et
        return 0x5A2C1C;                    // cok koyu et
    }

    static BufferedImage icon(AnimalDef d, boolean cooked) {
        Cut cut = cut(d);
        String[] mask = MASKS.get(cut);
        int seed = d.id.hashCode();
        float j1 = AssetGen.hash(seed, 1, 1) - 0.5f, j2 = AssetGen.hash(seed, 2, 2) - 0.5f, j3 = AssetGen.hash(seed, 3, 3) - 0.5f;
        // Ayni kesimi paylasan hayvanlar sirayla farkli yon + damar deseni alir; boylece hicbiri ayni gorunmez
        int k = 0;
        for (AnimalDef other : com.yabanhayati.def.Animals.ALL) {
            if (other == d) break;
            if (cut(other) == cut) k++;
        }
        boolean flip = k % 2 == 1;
        int marbling = (k / 2) % 4; // 0: duz, 1: yag benekleri, 2: capraz lifler, 3: yatay yag seritleri
        int stripeDir = (k / 8) % 2 == 0 ? 1 : -1;

        int raw = AssetGen.mix(rawColor(d, cut), d.base, 0.22f);
        int meat = jitter(cooked ? AssetGen.mix(cookedColor(rawColor(d, cut)), d.base, 0.2f) : raw, j1, j2, j3, cooked ? 26 : 22);
        int fat = cooked ? 0xE2BE84 : 0xF6EBDD;
        int second = d.pattern != AnimalDef.Pattern.NONE ? d.accent
                : d.headColor >= 0 ? d.headColor : d.mane >= 0 ? d.mane : d.bellyColor();
        boolean shell = cut == Cut.CLAW;

        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 16; y++) {
            if (mask[y].length() != 16) throw new IllegalStateException(cut + " maskesinin " + y + ". satiri 16 karakter olmali: " + mask[y]);
            for (int x = 0; x < 16; x++) {
                char ch = mask[y].charAt(flip ? 15 - x : x);
                float n = 1f + (AssetGen.hash(x * 5 + seed, y * 9, cooked ? 1 : 0) - 0.5f) * 0.1f;
                int c;
                switch (ch) {
                    case 'o' -> c = AnimalDef.darken(meat, 0.48f);
                    case 'h' -> c = AnimalDef.darken(meat, 1.24f);
                    case 'd' -> c = AnimalDef.darken(meat, 0.72f);
                    case 'm' -> {
                        c = AnimalDef.darken(meat, n);
                        if (marbling == 1 && AssetGen.hash(x + seed, y, 7) < 0.22f) c = AssetGen.mix(c, fat, 0.75f);
                        if (marbling == 2 && Math.floorMod(x + stripeDir * y, 4) == 0) c = AnimalDef.darken(c, 0.62f);
                        if (marbling == 3 && y % 3 == 0) c = AssetGen.mix(c, fat, 0.6f);
                        // pismis ette izgara izleri
                        if (cooked && Math.floorMod(x - stripeDir * y, 5) == 0) c = AnimalDef.darken(c, 0.6f);
                    }
                    case 's' -> {
                        boolean acc = switch (d.pattern) {
                            case STRIPES -> Math.floorMod(x + stripeDir * (y / 2), 3) == 0;
                            case SPOTS -> AssetGen.hash(x * 3 + seed, y * 7, 9) < 0.32f;
                            case PATCHES -> x % 3 != 0 && y % 3 != 0;
                            case BACK -> (x + y) % 4 < 2;
                            default -> d.base != second && AssetGen.hash(x + seed, y * 3, 11) < 0.3f;
                        };
                        int coat = acc ? second : d.base;
                        if (shell) c = cooked ? AssetGen.mix(coat, 0xFF7038, 0.35f) : AnimalDef.darken(coat, 0.8f);
                        else c = cooked ? AssetGen.mix(AnimalDef.darken(coat, 0.78f), 0x6A3C1C, 0.22f) : coat;
                        c = AnimalDef.darken(c, n);
                    }
                    case 'f' -> c = AnimalDef.darken(fat, n);
                    case 'b' -> c = cooked ? 0xE0D4BC : 0xF2ECDC;
                    case 'e' -> c = cooked ? 0xF0F0F0 : 0x101010;
                    default -> { continue; }
                }
                img.setRGB(x, y, 0xFF000000 | c);
            }
        }
        return img;
    }

    private static int jitter(int rgb, float a, float b, float c, int amount) {
        int r = clamp(((rgb >> 16) & 255) + Math.round(a * amount)), g = clamp(((rgb >> 8) & 255) + Math.round(b * amount)),
                bl = clamp((rgb & 255) + Math.round(c * amount));
        return (r << 16) | (g << 8) | bl;
    }

    private static int clamp(int v) { return Math.max(0, Math.min(255, v)); }

    private MeatIcons() {}
}
