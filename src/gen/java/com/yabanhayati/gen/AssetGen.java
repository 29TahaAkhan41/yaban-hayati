package com.yabanhayati.gen;

import com.yabanhayati.def.AnimalDef;
import com.yabanhayati.def.AnimalDef.Pattern;
import com.yabanhayati.def.Animals;
import com.yabanhayati.def.ModelShape;
import com.yabanhayati.def.ModelShape.Part;
import com.yabanhayati.def.ModelShape.Role;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Hayvan tablosundan tum kaynak dosyalarini uretir:
 * dokular, dil dosyalari, yumurta modelleri, ganimet tablolari, biyom dogma kurallari
 * ve gozle kontrol icin bir onizleme resmi.
 */
public final class AssetGen {
    static final String MOD = "yabanhayati";
    enum Face { TOP, BOTTOM, SIDE_A, FRONT, SIDE_B, BACK }

    public static void main(String[] args) throws IOException {
        Path out = Path.of(args[0]);
        Path preview = Path.of(args.length > 1 ? args[1] : "build/onizleme");
        Path assets = out.resolve("assets/" + MOD), data = out.resolve("data/" + MOD);
        deleteTree(out);
        StringJoiner en = new StringJoiner(",\n", "{\n", "\n}\n"), tr = new StringJoiner(",\n", "{\n", "\n}\n");
        en.add("  \"itemGroup." + MOD + "\": \"Wildlife\"");
        tr.add("  \"itemGroup." + MOD + "\": \"Yaban Hayatı\"");
        List<BufferedImage> thumbs = new ArrayList<>();
        StringJoiner meatTag = new StringJoiner(",\n", "{\n  \"replace\": false,\n  \"values\": [\n", "\n  ]\n}\n");
        for (AnimalDef d : Animals.ALL) {
            ModelShape s = ModelShape.of(d);
            BufferedImage tex = paint(s);
            write(assets.resolve("textures/entity/" + d.id + ".png"), tex);
            thumbs.add(render(s, tex));

            en.add("  \"entity." + MOD + "." + d.id + "\": \"" + d.en + "\"");
            tr.add("  \"entity." + MOD + "." + d.id + "\": \"" + d.tr + "\"");
            en.add("  \"item." + MOD + "." + d.id + "_spawn_egg\": \"" + d.en + " Spawn Egg\"");
            tr.add("  \"item." + MOD + "." + d.id + "_spawn_egg\": \"" + d.tr + " Çağırma Yumurtası\"");
            text(assets.resolve("models/item/" + d.id + "_spawn_egg.json"), "{\n  \"parent\": \"minecraft:item/template_spawn_egg\"\n}\n");

            // --- et: cig + pismis
            String raw = d.rawMeatId(), cooked = d.cookedMeatId();
            en.add("  \"item." + MOD + "." + raw + "\": \"Raw " + d.en + " Meat\"");
            tr.add("  \"item." + MOD + "." + raw + "\": \"Çiğ " + d.tr + " Eti\"");
            en.add("  \"item." + MOD + "." + cooked + "\": \"Cooked " + d.en + " Meat\"");
            tr.add("  \"item." + MOD + "." + cooked + "\": \"Pişmiş " + d.tr + " Eti\"");
            for (String item : new String[]{raw, cooked}) {
                write(assets.resolve("textures/item/" + item + ".png"), meatIcon(d, item.equals(cooked)));
                text(assets.resolve("models/item/" + item + ".json"),
                        "{\n  \"parent\": \"minecraft:item/generated\",\n  \"textures\": {\n    \"layer0\": \"" + MOD + ":item/" + item + "\"\n  }\n}\n");
            }
            text(data.resolve("recipe/" + cooked + ".json"), cooking("smelting", raw, cooked, 200));
            text(data.resolve("recipe/" + cooked + "_from_smoking.json"), cooking("smoking", raw, cooked, 100));
            text(data.resolve("recipe/" + cooked + "_from_campfire_cooking.json"), cooking("campfire_cooking", raw, cooked, 600));
            if (d.shape != AnimalDef.Shape.FISH) {
                meatTag.add("    \"" + MOD + ":" + raw + "\"");
                meatTag.add("    \"" + MOD + ":" + cooked + "\"");
            }
            text(data.resolve("loot_table/entities/" + d.id + ".json"), loot(d));
            for (int i = 0; i < d.biomes.length; i++)
                text(data.resolve("neoforge/biome_modifier/" + d.id + "_" + i + ".json"), biomeModifier(d, d.biomes[i]));
        }
        // kurtlar da bu etleri yiyebilsin
        text(out.resolve("data/minecraft/tags/item/meat.json"), meatTag.toString());
        text(assets.resolve("lang/en_us.json"), en.toString());
        text(assets.resolve("lang/tr_tr.json"), tr.toString());
        writeSheet(preview.resolve("hayvanlar.png"), thumbs);
        System.out.println(Animals.ALL.size() + " hayvan icin kaynaklar uretildi -> " + out);
    }

    // ------------------------------------------------------------------ JSON
    static String loot(AnimalDef d) {
        StringJoiner pools = new StringJoiner(",\n");
        int[] mc = d.meatCount();
        pools.add(pool(MOD + ":" + d.rawMeatId(), mc[0], mc[1], true));
        for (AnimalDef.Drop dr : d.drops) pools.add(pool(dr.item(), dr.min(), dr.max(), false));
        return "{\n  \"type\": \"minecraft:entity\",\n  \"pools\": [\n" + pools + "\n  ]\n}\n";
    }

    static String pool(String item, int min, int max, boolean smelt) {
        String fn = "            {\n              \"function\": \"minecraft:set_count\",\n              \"count\": { \"type\": \"minecraft:uniform\", \"min\": " + min + ", \"max\": " + max + " }\n            },\n";
        if (smelt) fn += "            {\n              \"function\": \"minecraft:furnace_smelt\",\n              \"conditions\": [\n                {\n                  \"condition\": \"minecraft:entity_properties\",\n"
                + "                  \"entity\": \"this\",\n                  \"predicate\": { \"flags\": { \"is_on_fire\": true } }\n                }\n              ]\n            },\n";
        fn += "            {\n              \"function\": \"minecraft:enchanted_count_increase\",\n              \"enchantment\": \"minecraft:looting\",\n              \"count\": { \"type\": \"minecraft:uniform\", \"min\": 0, \"max\": 1 }\n            }\n";
        return "    {\n      \"rolls\": 1,\n      \"entries\": [\n        {\n          \"type\": \"minecraft:item\",\n          \"name\": \"" + item + "\",\n          \"functions\": [\n" + fn + "          ]\n        }\n      ]\n    }";
    }

    static String cooking(String type, String raw, String cooked, int time) {
        return "{\n  \"type\": \"minecraft:" + type + "\",\n  \"category\": \"food\",\n  \"cookingtime\": " + time + ",\n  \"experience\": 0.35,\n"
                + "  \"ingredient\": { \"item\": \"" + MOD + ":" + raw + "\" },\n  \"result\": { \"id\": \"" + MOD + ":" + cooked + "\" }\n}\n";
    }

    // ------------------------------------------------------------------ et ikonlari (16x16)
    static final String[] STEAK = {
            "................", "................", ".....oooooo.....", "...ooffffffoo...", "..offmmmmmmffo..", ".ofmmmhhmmmmmfo.",
            ".ofmmhmmmmmmmfo.", ".ofmmmmmmbbmmfo.", ".ofmmmmmmbbmmfo.", ".ofmmmmmmmmmmfo.", "..ofmmmmmmmmfo..", "..offmmmmmmffo..",
            "...ooffffffoo...", ".....oooooo.....", "................", "................"};
    static final String[] DRUMSTICK = {
            "................", ".........oooo...", "........ommmmo..", ".......ommhmmmo.", ".......ommmmmmo.", "......ommmmmmmo.",
            "......ommmmmmo..", ".....offmmmmo...", "....obbfoooo....", "...obbo.........", ".oobbo..........", "obbbo...........",
            "obbbbo..........", ".oobo...........", "................", "................"};
    static final String[] FILLET = {
            "................", "................", "............oo..", "..........oommo.", "........oommmfo.", "......oommmmffo.",
            "....oommhmmffo..", "...ommmhmmffo...", "..ommmmmmffo....", ".ommmmmmffo.....", ".ommmmfffo......", ".offfffoo.......",
            "..ooooo.........", "................", "................", "................"};

    static BufferedImage meatIcon(AnimalDef d, boolean cooked) {
        String[] mask; int rawC, cookedC;
        switch (d.shape) {
            case BIRD -> { mask = DRUMSTICK; rawC = 0xF0B4A4; cookedC = 0xC8823C; }
            case FISH, CRAB -> { mask = FILLET; rawC = 0xEE9078; cookedC = 0xDCC8A0; }
            default -> { mask = STEAK; rawC = 0xC83A3A; cookedC = 0x8A4A28; }
        }
        // hayvanin kendi rengi ete hafifce, deri/yag kismina belirgin sekilde yansir
        int meat = cooked ? mix(cookedC, d.base, 0.12f) : mix(rawC, d.base, 0.2f);
        int skin = cooked ? mix(AnimalDef.darken(d.base, 0.6f), 0x5A3418, 0.35f) : mix(d.base, 0xFFFFFF, 0.3f);
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 16; y++) {
            if (mask[y].length() != 16) throw new IllegalStateException("maske satiri 16 olmali: " + mask[y]);
            for (int x = 0; x < 16; x++) {
                float n = 1f + (hash(x * 5 + d.id.hashCode(), y * 9, cooked ? 1 : 0) - 0.5f) * 0.12f;
                int c;
                switch (mask[y].charAt(x)) {
                    case 'o' -> c = AnimalDef.darken(meat, 0.5f);
                    case 'm' -> c = AnimalDef.darken(meat, n);
                    case 'h' -> c = AnimalDef.darken(meat, 1.22f);
                    case 'f' -> c = AnimalDef.darken(skin, n);
                    case 'b' -> c = cooked ? 0xE0D4BC : 0xF2ECDC;
                    default -> { continue; }
                }
                img.setRGB(x, y, 0xFF000000 | c);
            }
        }
        return img;
    }

    static String biomeModifier(AnimalDef d, String biome) {
        return "{\n  \"type\": \"neoforge:add_spawns\",\n  \"biomes\": \"" + biome + "\",\n  \"spawners\": {\n    \"type\": \"" + MOD + ":" + d.id
                + "\",\n    \"weight\": " + d.weight + ",\n    \"minCount\": " + d.minGroup + ",\n    \"maxCount\": " + d.maxGroup + "\n  }\n}\n";
    }

    // ------------------------------------------------------------------ doku boyama
    static BufferedImage paint(ModelShape s) {
        BufferedImage img = new BufferedImage(s.texW, s.texH, BufferedImage.TYPE_INT_ARGB);
        for (Part p : s.parts) {
            int W = 2 * p.d + 2 * p.w, H = p.d + p.h;
            for (int y = 0; y < H; y++)
                for (int x = 0; x < W; x++) {
                    int[] t = texel(p, x, y);
                    if (t == null) continue;
                    img.setRGB(p.u + x, p.v + y, 0xFF000000 | color(s.def, p, Face.values()[t[0]], t[1], t[2], t[3]));
                }
        }
        return img;
    }

    /** Doku pikseli -> {yuz, lx, ly, lz} (kutu icindeki yerel konum). */
    static int[] texel(Part p, int x, int y) {
        int w = p.w, h = p.h, d = p.d;
        if (y < d) {
            if (x >= d && x < d + w) return new int[]{Face.TOP.ordinal(), x - d, 0, d - 1 - y};
            if (x >= d + w && x < d + 2 * w) return new int[]{Face.BOTTOM.ordinal(), x - d - w, h - 1, d - 1 - y};
            return null;
        }
        int ly = y - d;
        if (x < d) return new int[]{Face.SIDE_A.ordinal(), 0, ly, d - 1 - x};
        if (x < d + w) return new int[]{Face.FRONT.ordinal(), x - d, ly, 0};
        if (x < 2 * d + w) return new int[]{Face.SIDE_B.ordinal(), w - 1, ly, x - d - w};
        return new int[]{Face.BACK.ordinal(), w - 1 - (x - 2 * d - w), ly, d - 1};
    }

    static int color(AnimalDef d, Part p, Face f, int lx, int ly, int lz) {
        int ax = Math.round(p.ax) + lx, ay = Math.round(p.ay) + ly, az = Math.round(p.az) + lz;
        boolean side = f != Face.TOP && f != Face.BOTTOM;
        int c;
        boolean patterned = false;
        switch (p.role) {
            case BODY, NECK -> {
                c = p.role == Role.NECK && d.shape == AnimalDef.Shape.BIRD ? d.headColorOr() : d.base;
                boolean bellyZone = f == Face.BOTTOM || (side && p.role == Role.BODY && p.h >= 3 && ly >= p.h - Math.max(1, p.h / 4))
                        || (d.frontBelly && f == Face.FRONT && ly > 0 && lx > 0 && lx < p.w - 1);
                if (d.frontBelly && d.shape == AnimalDef.Shape.BIRD && f == Face.BOTTOM) bellyZone = true;
                if (bellyZone && p.name.equals("body")) c = d.bellyColor();
                else patterned = d.pBody;
            }
            case HEAD -> { c = d.headColorOr(); patterned = d.pHead; }
            case LEG -> {
                c = d.legColorOr();
                patterned = d.pLegs && d.pattern != Pattern.BACK;
            }
            case SNOUT -> c = mix(d.headColorOr(), d.bellyColor(), 0.5f);
            case EAR -> c = AnimalDef.darken(d.headColorOr(), 0.82f);
            case HORN, BEAK, BIRD_LEG -> c = p.role == Role.BIRD_LEG && d.legColor >= 0 ? d.legColor : d.detail;
            case TAIL -> { c = d.base; patterned = d.pTail; }
            case TUFT, MANE, WING -> { c = d.maneColorOr(); patterned = d.pTail && p.role == Role.TUFT && d.shape == AnimalDef.Shape.BIRD; }
            case TRUNK -> c = AnimalDef.darken(d.base, 0.9f);
            case FIN -> c = AnimalDef.darken(d.base, 0.8f);
            case EYE_STALK -> c = ly == 0 ? 0x101010 : d.base;
            default -> c = d.base;
        }
        if (patterned && pattern(d, p, f, lx, ly, lz, ax, ay, az)) c = d.accent;

        // baliklarda karin alt yarida
        if (d.shape == AnimalDef.Shape.FISH && (p.role == Role.BODY || p.role == Role.HEAD || p.role == Role.TAIL)) {
            if (f == Face.BOTTOM || (side && ly >= Math.round(p.h * 0.6f))) c = d.bellyColor();
            else if (f == Face.TOP) c = d.base;
        }

        float shade = switch (f) { case TOP -> 1.07f; case BOTTOM -> 0.86f; case BACK -> 0.95f; default -> 1f; };
        if (p.role == Role.LEG && ly == p.h - 1 && p.h >= 3) shade *= 0.72f; // toynak / pati
        float n = (hash(ax * 7 + p.name.hashCode(), ay * 13 + f.ordinal(), az * 17) - 0.5f) * 0.09f;
        c = AnimalDef.darken(c, shade + n);

        // burun
        if (p.role == Role.SNOUT && f == Face.FRONT && ly == 0 && Math.abs(lx - (p.w - 1) / 2f) <= Math.max(0.5f, p.w / 4f)) c = 0x1E1614;
        // gozler
        if (p.role == Role.HEAD && isEye(d, p, f, lx, ly, lz)) c = d.id.equals("black_panther") ? d.detail : 0x0C0C0C;
        return c;
    }

    static boolean isEye(AnimalDef d, Part p, Face f, int lx, int ly, int lz) {
        int ey = Math.max(0, Math.round(p.h * 0.3f) - (p.h <= 3 ? 1 : 0));
        if (p.h <= 2) ey = 0;
        boolean tall = p.h >= 8;
        boolean rowOk = ly == ey || (tall && ly == ey + 1);
        if (!rowOk) return false;
        boolean sideEyes = d.shape != AnimalDef.Shape.QUAD || p.w <= 2;
        if (sideEyes) {
            int ez = d.shape == AnimalDef.Shape.FISH ? Math.min(1, p.d - 1) : Math.max(0, Math.min(1, p.d / 3));
            return (f == Face.SIDE_A || f == Face.SIDE_B) && lz == ez;
        }
        if (f == Face.FRONT) {
            int inset = p.w >= 7 ? 1 : 0;
            if (d.snoutL > 0 && d.snoutW >= p.w - 1) inset = 0;
            return lx == inset || lx == p.w - 1 - inset;
        }
        // genis burunlu / burunsuz hayvanlarda goz yanda da gorunsun
        return (f == Face.SIDE_A || f == Face.SIDE_B) && lz == 0 && (d.snoutW >= p.w - 1 || d.neckLen > 0);
    }

    static boolean pattern(AnimalDef d, Part p, Face f, int lx, int ly, int lz, int ax, int ay, int az) {
        switch (d.pattern) {
            case STRIPES -> {
                boolean alongY = p.role == Role.LEG || (p.role == Role.NECK);
                int t = alongY ? ay : az;
                int wob = (int) Math.floor(hash(alongY ? ax : ay, 3, t / 5) * 2);
                int period = p.role == Role.TAIL ? 4 : 4;
                int m = Math.floorMod(t + wob, period);
                if (f == Face.FRONT || f == Face.BACK) return !alongY ? hash(ax, ay, 5) < 0.3f : m == 0;
                return m == 0 || (m == 1 && hash(ax, ay, az) < 0.35f);
            }
            case SPOTS -> {
                return hash(ax * 3 + 1, ay * 5 + 2, az * 7 + 3) < 0.2f;
            }
            case PATCHES -> {
                int cs = 4;
                int gx = f == Face.SIDE_A || f == Face.SIDE_B ? 0 : Math.floorMod(ax, cs);
                int gy = f == Face.TOP || f == Face.BOTTOM ? 0 : Math.floorMod(ay, cs);
                int gz = f == Face.FRONT || f == Face.BACK ? 0 : Math.floorMod(az, cs);
                boolean gap = (f != Face.SIDE_A && f != Face.SIDE_B && gx == 0) || (f != Face.TOP && f != Face.BOTTOM && gy == 0)
                        || (f != Face.FRONT && f != Face.BACK && gz == 0);
                return !gap && hash(Math.floorDiv(ax, cs) + 11, Math.floorDiv(ay, cs) + 5, Math.floorDiv(az, cs) + 3) < 0.85f;
            }
            case BACK -> {
                float cx = Math.abs(lx - (p.w - 1) / 2f);
                if (p.role == Role.TAIL) return f == Face.TOP || (f != Face.BOTTOM && ly == 0);
                return f == Face.TOP && cx <= Math.max(0.5f, p.w / 5f)
                        || (p.role == Role.HEAD && f == Face.FRONT && cx <= 0.5f);
            }
            default -> { return false; }
        }
    }

    static float hash(int x, int y, int z) {
        int h = x * 374761393 + y * 668265263 + z * 1274126177;
        h = (h ^ (h >>> 13)) * 1274126177;
        h ^= h >>> 16;
        return (h & 0xFFFF) / 65536f;
    }

    static int mix(int a, int b, float t) {
        int r = Math.round(((a >> 16) & 255) * (1 - t) + ((b >> 16) & 255) * t);
        int g = Math.round(((a >> 8) & 255) * (1 - t) + ((b >> 8) & 255) * t);
        int bl = Math.round((a & 255) * (1 - t) + (b & 255) * t);
        return (r << 16) | (g << 8) | bl;
    }

    // ------------------------------------------------------------------ onizleme (basit yazilim cizici)
    static final int TH = 200;

    static BufferedImage render(ModelShape s, BufferedImage tex) {
        BufferedImage img = new BufferedImage(TH, TH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x9CC4E4)); g.fillRect(0, 0, TH, TH);
        g.setColor(new Color(0x6FA84E)); g.fillRect(0, TH - 34, TH, 34);
        float[] zbuf = new float[TH * TH];
        java.util.Arrays.fill(zbuf, Float.NEGATIVE_INFINITY);
        // sinirlar
        float ext = 8;
        for (Part p : s.parts) ext = Math.max(ext, Math.max(Math.abs(p.az) + p.d, Math.max(Math.abs(p.ax) + p.w, 24 - p.ay)));
        float zoom = (TH * 0.42f) / ext;
        double yaw = Math.toRadians(-35), pitch = Math.toRadians(18);
        for (Part p : s.parts) {
            List<Part> chain = new ArrayList<>();
            for (Part q = p; q != null; q = q.parent == null ? null : s.part(q.parent)) chain.add(0, q);
            int W = 2 * p.d + 2 * p.w, H = p.d + p.h;
            int sub = Math.max(2, (int) Math.ceil(zoom * 1.6f));
            for (int ty = 0; ty < H; ty++)
                for (int tx = 0; tx < W; tx++) {
                    int[] t = texel(p, tx, ty);
                    if (t == null) continue;
                    int rgb = tex.getRGB(p.u + tx, p.v + ty) & 0xFFFFFF;
                    Face f = Face.values()[t[0]];
                    for (int a = 0; a < sub; a++)
                        for (int b = 0; b < sub; b++) {
                            float fa = (a + 0.5f) / sub, fb = (b + 0.5f) / sub;
                            float lx = t[1], ly = t[2], lz = t[3];
                            switch (f) {
                                case TOP -> { lx += fa; lz += fb; }
                                case BOTTOM -> { lx += fa; lz += fb; ly += 1; }
                                case FRONT -> { lx += fa; ly += fb; }
                                case BACK -> { lx += fa; ly += fb; lz += 1; }
                                case SIDE_A -> { lz += fa; ly += fb; }
                                default -> { lz += fa; ly += fb; lx += 1; }
                            }
                            double[] v = {p.ox + lx, p.oy + ly, p.oz + lz};
                            for (int i = chain.size() - 1; i >= 0; i--) {
                                Part q = chain.get(i);
                                v = rotX(v, q.rx); v = rotY(v, q.ry); v = rotZ(v, q.rz);
                                v[0] += q.px; v[1] += q.py; v[2] += q.pz;
                            }
                            v[1] -= 24;
                            v = rotY(v, yaw); v = rotX(v, pitch);
                            int sx = Math.round(TH / 2f + (float) v[0] * zoom), sy = Math.round(TH - 30 + (float) v[1] * zoom);
                            if (sx < 0 || sy < 0 || sx >= TH || sy >= TH) continue;
                            float depth = (float) -v[2];
                            if (depth > zbuf[sy * TH + sx]) { zbuf[sy * TH + sx] = depth; img.setRGB(sx, sy, rgb); }
                        }
                }
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        g.drawString(s.def.tr + "  " + s.width + "x" + s.height, 4, TH - 6);
        g.dispose();
        return img;
    }

    static double[] rotX(double[] v, double a) { double c = Math.cos(a), s = Math.sin(a); return new double[]{v[0], v[1] * c - v[2] * s, v[1] * s + v[2] * c}; }
    static double[] rotY(double[] v, double a) { double c = Math.cos(a), s = Math.sin(a); return new double[]{v[0] * c + v[2] * s, v[1], -v[0] * s + v[2] * c}; }
    static double[] rotZ(double[] v, double a) { double c = Math.cos(a), s = Math.sin(a); return new double[]{v[0] * c - v[1] * s, v[0] * s + v[1] * c, v[2]}; }

    static void writeSheet(Path path, List<BufferedImage> thumbs) throws IOException {
        int cols = 6, perSheet = 24;
        for (int sheet = 0; sheet * perSheet < thumbs.size(); sheet++) {
            int n = Math.min(perSheet, thumbs.size() - sheet * perSheet);
            int rows = (n + cols - 1) / cols;
            BufferedImage img = new BufferedImage(cols * TH, rows * TH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            for (int i = 0; i < n; i++) g.drawImage(thumbs.get(sheet * perSheet + i), (i % cols) * TH, (i / cols) * TH, null);
            g.dispose();
            write(path.resolveSibling("hayvanlar_" + (sheet + 1) + ".png"), img);
        }
    }

    // ------------------------------------------------------------------ dosya yardimcilari
    static void write(Path p, BufferedImage img) throws IOException { Files.createDirectories(p.getParent()); ImageIO.write(img, "png", p.toFile()); }
    static void text(Path p, String s) throws IOException { Files.createDirectories(p.getParent()); Files.writeString(p, s, StandardCharsets.UTF_8); }
    static void deleteTree(Path p) throws IOException {
        if (!Files.exists(p)) return;
        try (var st = Files.walk(p)) {
            for (Path q : st.sorted(java.util.Comparator.reverseOrder()).toList()) if (!q.equals(p)) Files.delete(q);
        }
    }
}
