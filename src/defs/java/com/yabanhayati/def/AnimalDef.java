package com.yabanhayati.def;

import java.util.ArrayList;
import java.util.List;

/**
 * Bir hayvanin tum tanimi: sekli, renkleri, davranisi ve nerede dogdugu.
 * Bu sinif Minecraft'a bagimli degildir; hem mod hem doku ureticisi kullanir.
 * Olculer "model pikseli" cinsindendir (16 piksel = 1 blok).
 */
public final class AnimalDef {
    public enum Shape { QUAD, BIRD, FISH, SNAKE, CRAB }
    public enum Temper { PASSIVE, NEUTRAL, PREDATOR, AGGRESSIVE }
    public enum Pattern { NONE, STRIPES, SPOTS, PATCHES, BACK }
    public enum Ears { NONE, SMALL, ROUND, LONG, FLAP }
    public enum Horns { NONE, ANTLERS, HORNS, NOSE, TUSKS }
    public enum Tail { NONE, STUB, THIN, TUFT, BUSHY, THICK }

    public record Drop(String item, int min, int max) {}

    public final String id, en, tr;
    public final Shape shape;

    // --- genel ---
    public float scale = 1f;
    public float health = 10, speed = 0.25f, attack = 0;
    public Temper temper = Temper.PASSIVE;
    public boolean prey, flying, venom;
    public String[] foods = {};
    public String[] biomes = {};
    public int weight = 8, minGroup = 1, maxGroup = 3;
    public final List<Drop> drops = new ArrayList<>();
    public String sound;

    // --- renkler ---
    public int base = 0x888888, belly = -1, accent = 0x222222, detail = 0xD8D0C0, mane = -1, headColor = -1, legColor = -1;
    public Pattern pattern = Pattern.NONE;
    public boolean pBody = true, pHead = false, pLegs = true, pTail = true;
    public boolean frontBelly;

    // --- govde (tum sekiller) ---
    public int bodyW = 6, bodyH = 6, bodyL = 10;
    public int legLen = 6, legW = 2;
    public int neckLen = 0, neckW = 3;
    public float neckPitch = 0.4f;
    public int headW = 5, headH = 5, headL = 5;
    public float headRaise = 0;
    public int snoutW = 0, snoutH = 0, snoutL = 0;
    public Ears ears = Ears.NONE;
    public Horns horns = Horns.NONE;
    public int hornLen = 4;
    public int tuskLen = 0;
    public Tail tail = Tail.NONE;
    public int tailLen = 0;
    public boolean tailUp;
    public boolean hasMane;
    public int trunkLen = 0;
    public int humpH = 0;

    // --- kus ---
    public int beakW = 1, beakH = 1, beakL = 2;
    public int wingLen = 5;

    // --- balik ---
    public int tailFinH = 6, dorsalH = 0, finLen = 3, finDepth = 3, noseLen = 0;
    public boolean fluke, flat;

    // --- yilan / yengec ---
    public int segments = 6;

    public AnimalDef(Shape shape, String id, String en, String tr) {
        this.shape = shape;
        this.id = id;
        this.en = en;
        this.tr = tr;
    }

    public AnimalDef body(int w, int h, int l) { bodyW = w; bodyH = h; bodyL = l; return this; }
    public AnimalDef legs(int len, int w) { legLen = len; legW = w; return this; }
    public AnimalDef neck(int len, int w, double pitch) { neckLen = len; neckW = w; neckPitch = (float) pitch; return this; }
    public AnimalDef head(int w, int h, int l) { headW = w; headH = h; headL = l; return this; }
    public AnimalDef headRaise(double r) { headRaise = (float) r; return this; }
    public AnimalDef snout(int w, int h, int l) { snoutW = w; snoutH = h; snoutL = l; return this; }
    public AnimalDef ears(Ears e) { ears = e; return this; }
    public AnimalDef horns(Horns h, int len) { horns = h; hornLen = len; return this; }
    public AnimalDef tusks(int len) { tuskLen = len; return this; }
    public AnimalDef tail(Tail t, int len) { tail = t; tailLen = len; return this; }
    public AnimalDef tailUp() { tailUp = true; return this; }
    public AnimalDef withMane(int color) { hasMane = true; mane = color; return this; }
    public AnimalDef trunk(int len) { trunkLen = len; return this; }
    public AnimalDef hump(int h) { humpH = h; return this; }
    public AnimalDef beak(int w, int h, int l) { beakW = w; beakH = h; beakL = l; return this; }
    public AnimalDef wings(int len) { wingLen = len; return this; }
    public AnimalDef fly() { flying = true; return this; }
    public AnimalDef fishTail(int len, int finH) { tailLen = len; tailFinH = finH; return this; }
    public AnimalDef dorsal(int h) { dorsalH = h; return this; }
    public AnimalDef fins(int len, int depth) { finLen = len; finDepth = depth; return this; }
    public AnimalDef nose(int len) { noseLen = len; return this; }
    public AnimalDef fluke() { fluke = true; return this; }
    public AnimalDef flat() { flat = true; return this; }
    public AnimalDef segments(int n) { segments = n; return this; }
    public AnimalDef scale(double s) { scale = (float) s; return this; }

    public AnimalDef colors(int base, int belly) { this.base = base; this.belly = belly; return this; }
    public AnimalDef color(int base) { this.base = base; return this; }
    public AnimalDef pattern(Pattern p, int accent) { pattern = p; this.accent = accent; return this; }
    /** Desenin uygulanacagi bolgeler: govde, kafa, bacak, kuyruk. */
    public AnimalDef patternOn(boolean body, boolean head, boolean legs, boolean tail) { pBody = body; pHead = head; pLegs = legs; pTail = tail; return this; }
    public AnimalDef detail(int c) { detail = c; return this; }
    public AnimalDef maneColor(int c) { mane = c; return this; }
    public AnimalDef headColor(int c) { headColor = c; return this; }
    public AnimalDef legColor(int c) { legColor = c; return this; }
    public AnimalDef frontBelly() { frontBelly = true; return this; }

    public AnimalDef stats(double health, double speed, double attack) { this.health = (float) health; this.speed = (float) speed; this.attack = (float) attack; return this; }
    public AnimalDef temper(Temper t) { temper = t; return this; }
    public AnimalDef prey() { prey = true; return this; }
    public AnimalDef venom() { venom = true; return this; }
    public AnimalDef food(String... items) { foods = items; return this; }
    public AnimalDef biomes(String[]... groups) {
        List<String> all = new ArrayList<>();
        for (String[] g : groups) all.addAll(List.of(g));
        biomes = all.toArray(String[]::new);
        return this;
    }
    public AnimalDef spawn(int weight, int min, int max) { this.weight = weight; minGroup = min; maxGroup = max; return this; }
    public AnimalDef drop(String item, int min, int max) { drops.add(new Drop(item, min, max)); return this; }
    public AnimalDef sound(String vanillaEntity) { sound = vanillaEntity; return this; }

    public boolean isAquatic() { return shape == Shape.FISH; }
    public boolean attacks() { return attack > 0 && temper != Temper.PASSIVE; }
    public int bellyColor() { return belly < 0 ? base : belly; }
    public int maneColorOr() { return mane < 0 ? darken(base, 0.7f) : mane; }
    public int headColorOr() { return headColor < 0 ? base : headColor; }
    public int legColorOr() { return legColor < 0 ? base : legColor; }

    public static int darken(int rgb, float f) {
        int r = Math.min(255, Math.round(((rgb >> 16) & 255) * f));
        int g = Math.min(255, Math.round(((rgb >> 8) & 255) * f));
        int b = Math.min(255, Math.round((rgb & 255) * f));
        return (r << 16) | (g << 8) | b;
    }
}
