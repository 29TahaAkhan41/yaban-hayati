package com.yabanhayati.def;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bir AnimalDef'ten kutu tabanli model uretir (parcalar + doku yerlesimi + carpisma kutusu).
 * Koordinatlar Minecraft model uzayindadir: Y asagi dogru artar, zemin y=24, on taraf -Z.
 */
public final class ModelShape {
    public enum Role { BODY, NECK, HEAD, LEG, SNOUT, EAR, HORN, TAIL, TUFT, MANE, TRUNK, BEAK, WING, FIN, BIRD_LEG, EYE_STALK }

    public static final class Part {
        public final String name, parent;
        public final Role role;
        public float px, py, pz, rx, ry, rz;
        public float ox, oy, oz;
        public int w, h, d;
        public int u, v;
        /** Desen surekliligi icin yaklasik mutlak konum. */
        public float ax, ay, az;

        Part(String name, String parent, Role role) { this.name = name; this.parent = parent; this.role = role; }
        public Part rot(double x, double y, double z) { rx = (float) x; ry = (float) y; rz = (float) z; return this; }
    }

    public final AnimalDef def;
    public final List<Part> parts = new ArrayList<>();
    private final Map<String, Part> byName = new LinkedHashMap<>();
    public int texW, texH;
    /** Blok cinsinden carpisma kutusu. */
    public float width, height;

    private static final Map<String, ModelShape> CACHE = new LinkedHashMap<>();

    public static synchronized ModelShape of(AnimalDef def) {
        return CACHE.computeIfAbsent(def.id, k -> new ModelShape(def));
    }

    public Part part(String name) { return byName.get(name); }

    private ModelShape(AnimalDef d) {
        this.def = d;
        float wPx, hPx;
        switch (d.shape) {
            case QUAD -> { float[] s = quad(d); wPx = s[0]; hPx = s[1]; }
            case BIRD -> { float[] s = bird(d); wPx = s[0]; hPx = s[1]; }
            case FISH -> { float[] s = fish(d); wPx = s[0]; hPx = s[1]; }
            case SNAKE -> { float[] s = snake(d); wPx = s[0]; hPx = s[1]; }
            default -> { float[] s = crab(d); wPx = s[0]; hPx = s[1]; }
        }
        width = Math.max(0.25f, Math.round(wPx * d.scale / 16f * 20f) / 20f);
        height = Math.max(0.2f, Math.round(hPx * d.scale / 16f * 20f) / 20f);
        for (Part p : parts) {
            float x = p.ox, y = p.oy, z = p.oz;
            Part q = p;
            while (q != null) { x += q.px; y += q.py; z += q.pz; q = q.parent == null ? null : byName.get(q.parent); }
            p.ax = x; p.ay = y; p.az = z;
        }
        pack();
    }

    private Part add(String name, String parent, Role role, double px, double py, double pz,
                     double ox, double oy, double oz, int w, int h, int dd) {
        Part p = new Part(name, parent, role);
        p.px = (float) px; p.py = (float) py; p.pz = (float) pz;
        p.ox = (float) ox; p.oy = (float) oy; p.oz = (float) oz;
        p.w = Math.max(1, w); p.h = Math.max(1, h); p.d = Math.max(1, dd);
        parts.add(p);
        byName.put(name, p);
        return p;
    }

    // ------------------------------------------------------------------ dort ayakli
    private float[] quad(AnimalDef d) {
        int bW = d.bodyW, bH = d.bodyH, bL = d.bodyL;
        float legTop = 24 - d.legLen, bodyTop = legTop - bH;
        add("body", null, Role.BODY, 0, bodyTop, 0, -bW / 2f, 0, -bL / 2f, bW, bH, bL);
        if (d.humpH > 0)
            add("hump", "body", Role.BODY, 0, 0, 0, -(bW - 2) / 2f, -d.humpH, -bL / 2f, bW - 2, d.humpH, Math.max(2, bL / 2));

        float lx = bW / 2f - d.legW / 2f, lz = bL / 2f - d.legW / 2f;
        String[] ln = {"leg_fp", "leg_fn", "leg_bp", "leg_bn"};
        float[][] lp = {{lx, -lz}, {-lx, -lz}, {lx, lz}, {-lx, lz}};
        for (int i = 0; i < 4; i++)
            add(ln[i], null, Role.LEG, lp[i][0], legTop, lp[i][1], -d.legW / 2f, 0, -d.legW / 2f, d.legW, d.legLen, d.legW);

        int hW = d.headW, hH = d.headH, hL = d.headL;
        float hx0 = -hW / 2f, hy0, hz0;
        Part head;
        float headTopAbove; // govde ustunden ne kadar yukari
        if (d.neckLen > 0) {
            int nW = d.neckW;
            add("neck", null, Role.NECK, 0, bodyTop + nW * 0.5f, -bL / 2f + nW / 2f,
                    -nW / 2f, -d.neckLen, -nW / 2f, nW, d.neckLen, nW).rot(d.neckPitch, 0, 0);
            hy0 = -hH + 1; hz0 = -(hL - nW / 2f);
            head = add("head", "neck", Role.HEAD, 0, -d.neckLen, 0, hx0, hy0, hz0, hW, hH, hL).rot(-d.neckPitch, 0, 0);
            headTopAbove = (float) (d.neckLen * Math.cos(d.neckPitch)) + hH - 1 - nW * 0.5f;
        } else {
            hy0 = -hH / 2f; hz0 = -hL + 1;
            float py = bodyTop + bH * 0.35f - d.headRaise;
            head = add("head", null, Role.HEAD, 0, py, -bL / 2f, hx0, hy0, hz0, hW, hH, hL);
            headTopAbove = Math.max(0, bodyTop - (py + hy0));
        }
        if (d.hasMane)
            add("mane", "head", Role.MANE, 0, 0, 0, hx0 - 2, hy0 - 2, hz0 + hL - 3, hW + 4, hH + 4, 5);
        if (d.snoutL > 0) {
            add("snout", "head", Role.SNOUT, 0, 0, 0, -d.snoutW / 2f, hy0 + hH - d.snoutH, hz0 - d.snoutL, d.snoutW, d.snoutH, d.snoutL);
        }
        float frontZ = hz0 - d.snoutL;
        if (d.trunkLen > 0) {
            int tw = Math.max(2, hW / 3);
            add("trunk", "head", Role.TRUNK, 0, hy0 + hH - 4, hz0, -tw / 2f, 0, -tw, tw, d.trunkLen, tw);
        }
        if (d.tuskLen > 0) {
            float tx = (d.snoutL > 0 ? d.snoutW / 2f : hW / 4f + 1);
            add("tusk_p", "head", Role.HORN, tx, hy0 + hH - 1.5, frontZ + 1, -0.5, 0, -0.5, 1, d.tuskLen, 1).rot(-0.55, 0, -0.1);
            add("tusk_n", "head", Role.HORN, -tx, hy0 + hH - 1.5, frontZ + 1, -0.5, 0, -0.5, 1, d.tuskLen, 1).rot(-0.55, 0, 0.1);
        }
        float earZ = hz0 + hL - 2;
        switch (d.ears) {
            case SMALL -> {
                int s = Math.max(1, hW / 3);
                add("ear_p", "head", Role.EAR, hW / 2f - s / 2f, hy0, earZ, -s / 2f, -s, 0, s, s, 1);
                add("ear_n", "head", Role.EAR, -(hW / 2f - s / 2f), hy0, earZ, -s / 2f, -s, 0, s, s, 1);
            }
            case ROUND -> {
                int s = Math.max(2, hW / 2);
                add("ear_p", "head", Role.EAR, hW / 2f, hy0 + 1, earZ, -s / 2f, -s, 0, s, s, 1);
                add("ear_n", "head", Role.EAR, -hW / 2f, hy0 + 1, earZ, -s / 2f, -s, 0, s, s, 1);
            }
            case LONG -> {
                int s = Math.max(1, hW / 3);
                add("ear_p", "head", Role.EAR, hW / 2f - s / 2f, hy0, earZ, -s / 2f, -hH, 0, s, hH, 1).rot(0, 0, 0.3);
                add("ear_n", "head", Role.EAR, -(hW / 2f - s / 2f), hy0, earZ, -s / 2f, -hH, 0, s, hH, 1).rot(0, 0, -0.3);
            }
            case FLAP -> {
                add("ear_p", "head", Role.EAR, hW / 2f, hy0, hz0 + 2, 0, 0, 0, 1, hH + 2, hL).rot(0, 0.55, 0);
                add("ear_n", "head", Role.EAR, -hW / 2f, hy0, hz0 + 2, -1, 0, 0, 1, hH + 2, hL).rot(0, -0.55, 0);
            }
            default -> { }
        }
        float hornZ = hz0 + hL - 1.5f;
        switch (d.horns) {
            case HORNS -> {
                add("horn_p", "head", Role.HORN, hW / 2f - 1, hy0, hornZ, -0.5, -d.hornLen, -0.5, 1, d.hornLen, 1).rot(-0.3, 0, 0.5);
                add("horn_n", "head", Role.HORN, -(hW / 2f - 1), hy0, hornZ, -0.5, -d.hornLen, -0.5, 1, d.hornLen, 1).rot(-0.3, 0, -0.5);
            }
            case ANTLERS -> {
                int pl = Math.max(2, d.hornLen / 2);
                add("horn_p", "head", Role.HORN, hW / 2f - 1, hy0, hornZ, -0.5, -d.hornLen, -0.5, 1, d.hornLen, 1).rot(-0.25, 0, 0.55);
                add("horn_n", "head", Role.HORN, -(hW / 2f - 1), hy0, hornZ, -0.5, -d.hornLen, -0.5, 1, d.hornLen, 1).rot(-0.25, 0, -0.55);
                add("prong_p", "horn_p", Role.HORN, 0, -d.hornLen * 0.55, 0, -0.5, -pl, -0.5, 1, pl, 1).rot(0.9, 0, -0.5);
                add("prong_n", "horn_n", Role.HORN, 0, -d.hornLen * 0.55, 0, -0.5, -pl, -0.5, 1, pl, 1).rot(0.9, 0, 0.5);
                add("prong2_p", "horn_p", Role.HORN, 0, -d.hornLen + 0.5, 0, -0.5, -pl, -0.5, 1, pl, 1).rot(-0.2, 0, 0.9);
                add("prong2_n", "horn_n", Role.HORN, 0, -d.hornLen + 0.5, 0, -0.5, -pl, -0.5, 1, pl, 1).rot(-0.2, 0, -0.9);
            }
            case NOSE -> add("horn", "head", Role.HORN, 0, hy0 + hH - d.snoutH, frontZ + 2, -1, -d.hornLen, -1, 2, d.hornLen, 2).rot(0.3, 0, 0);
            default -> { }
        }
        float tailY = bodyTop + 1, tailZ = bL / 2f;
        switch (d.tail) {
            case STUB -> add("tail", null, Role.TAIL, 0, tailY, tailZ, -1, 0, -0.5, 2, 2, 2);
            case THIN -> add("tail", null, Role.TAIL, 0, tailY, tailZ, -0.5, 0, 0, 1, d.tailLen, 1).rot(d.tailUp ? 2.3 : 0.35, 0, 0);
            case TUFT -> {
                add("tail", null, Role.TAIL, 0, tailY, tailZ, -0.5, 0, 0, 1, d.tailLen, 1).rot(0.35, 0, 0);
                add("tail_tip", "tail", Role.TUFT, 0, d.tailLen, 0.5, -1, 0, -1, 2, 3, 2);
            }
            case BUSHY -> {
                int t = Math.max(2, Math.min(4, bW / 2));
                add("tail", null, Role.TAIL, 0, tailY + 1, tailZ - 0.5, -t / 2f, -t / 2f, 0, t, t, d.tailLen).rot(d.tailUp ? 1.25 : -0.45, 0, 0);
            }
            case THICK -> {
                int tw = Math.max(2, bW / 2), th = Math.max(1, bH / 2), l1 = Math.max(1, d.tailLen / 2);
                add("tail", null, Role.TAIL, 0, tailY - 1 + (bH - th) * 0.4, tailZ, -tw / 2f, 0, 0, tw, th, l1).rot(-0.1, 0, 0);
                int tw2 = Math.max(1, tw - 2), th2 = Math.max(1, th - 1);
                add("tail2", "tail", Role.TAIL, 0, (th - th2) / 2f, l1, -tw2 / 2f, 0, 0, tw2, th2, d.tailLen - l1).rot(-0.08, 0, 0);
            }
            default -> { }
        }
        return new float[]{Math.max(bW, hW) * 1.3f, d.legLen + bH + Math.max(d.humpH, headTopAbove)};
    }

    // ------------------------------------------------------------------ kus
    private float[] bird(AnimalDef d) {
        int bW = d.bodyW, bH = d.bodyH, bL = d.bodyL;
        float legTop = 24 - d.legLen, bodyTop = legTop - bH;
        add("body", null, Role.BODY, 0, bodyTop, 0, -bW / 2f, 0, -bL / 2f, bW, bH, bL);
        int lw = d.legW > 2 ? 2 : (d.legLen >= 8 && d.scale > 1.1f ? 2 : 1);
        float lx = Math.max(1f, bW / 4f);
        for (int s = -1; s <= 1; s += 2) {
            String n = s > 0 ? "leg_fp" : "leg_fn";
            add(n, null, Role.BIRD_LEG, s * lx, legTop, 0, -lw / 2f, 0, -lw / 2f, lw, d.legLen, lw);
            add(n + "_foot", n, Role.BIRD_LEG, 0, 0, 0, -lw / 2f - 0.5, d.legLen - 1, -lw / 2f - 2, lw + 1, 1, lw + 2);
        }
        int hW = d.headW, hH = d.headH, hL = d.headL;
        float above;
        if (d.neckLen > 0) {
            int nW = Math.max(1, Math.min(d.neckW, hW - 1));
            add("neck", null, Role.NECK, 0, bodyTop + 1, -bL / 2f + nW / 2f + 0.5, -nW / 2f, -d.neckLen, -nW / 2f, nW, d.neckLen, nW).rot(0.12, 0, 0);
            add("head", "neck", Role.HEAD, 0, -d.neckLen, 0, -hW / 2f, -hH + 1, -hL / 2f - 0.5, hW, hH, hL).rot(-0.12, 0, 0);
            above = d.neckLen + hH - 2;
        } else {
            add("head", null, Role.HEAD, 0, bodyTop + 1, -bL / 2f + hL / 2f - 1, -hW / 2f, -hH, -hL / 2f, hW, hH, hL);
            above = hH - 1;
        }
        Part head = byName.get("head");
        add("beak", "head", Role.BEAK, 0, 0, 0, -d.beakW / 2f, head.oy + hH * 0.45f, head.oz - d.beakL, d.beakW, d.beakH, d.beakL);
        int wingH = Math.max(2, bH - 2);
        add("wing_p", null, Role.WING, bW / 2f, bodyTop + 1, -bL / 2f + 1, 0, 0, 0, 1, wingH, d.wingLen);
        add("wing_n", null, Role.WING, -bW / 2f, bodyTop + 1, -bL / 2f + 1, -1, 0, 0, 1, wingH, d.wingLen);
        if (d.tailLen > 0) {
            int tw = Math.max(1, bW - 2);
            add("tail", null, Role.TUFT, 0, bodyTop + 1, bL / 2f - 0.5, -tw / 2f, 0, 0, tw, d.tailLen > 8 ? 2 : 1, d.tailLen)
                    .rot(d.tailUp ? 1.0 : (d.tailLen > 8 ? -0.25 : 0.15), 0, 0);
        }
        return new float[]{bW * 1.3f, d.legLen + bH + above};
    }

    // ------------------------------------------------------------------ balik
    private float[] fish(AnimalDef d) {
        int bW = d.bodyW, bH = d.bodyH, bL = d.bodyL;
        float cy = 24 - bH / 2f;
        add("body", null, Role.BODY, 0, cy, 0, -bW / 2f, -bH / 2f, -bL / 2f, bW, bH, bL);
        int nW = Math.max(1, bW - 2), nH = Math.max(1, bH - 2), nL = Math.max(2, bL / 5);
        if (d.flat) { nL = 2; nW = Math.max(2, bW / 2); nH = bH; }
        // "kafa": gozler burada
        add("head", "body", Role.HEAD, 0, 0, -bL / 2f, -nW / 2f, -nH / 2f + (d.flat ? 0 : 0.5f), -nL, nW, nH, nL);
        if (d.noseLen > 0)
            add("nose", "head", Role.HORN, 0, 0, -nL, -0.5, 0, -d.noseLen, 1, 1, d.noseLen);
        if (d.flat) {
            add("tail", "body", Role.TAIL, 0, 0, bL / 2f, -0.5, -0.5, 0, 1, 1, d.tailLen);
        } else {
            int tW = Math.max(1, bW - 2), tH = Math.max(1, bH - 2);
            add("tail", "body", Role.TAIL, 0, 0, bL / 2f, -tW / 2f, -tH / 2f, 0, tW, tH, d.tailLen);
            int fl = Math.max(2, d.tailFinH / 2);
            if (d.fluke) add("tail_fin", "tail", Role.FIN, 0, 0, d.tailLen - 1, -d.tailFinH / 2f, -0.5, 0, d.tailFinH, 1, fl);
            else add("tail_fin", "tail", Role.FIN, 0, 0, d.tailLen - 1, -0.5, -d.tailFinH / 2f, 0, 1, d.tailFinH, fl);
        }
        if (d.dorsalH > 0)
            add("dorsal", "body", Role.FIN, 0, -bH / 2f + 0.5, -1, -0.5, -d.dorsalH, 0, 1, d.dorsalH, Math.max(2, d.dorsalH - 1)).rot(-0.45, 0, 0);
        float fy = d.flat ? -0.5f : bH * 0.2f, fz = d.flat ? -d.finDepth / 2f : -bL / 4f;
        add("fin_p", "body", Role.FIN, bW / 2f, fy, fz, 0, 0, 0, d.finLen, 1, d.finDepth).rot(0, 0, d.flat ? 0.05 : 0.5);
        add("fin_n", "body", Role.FIN, -bW / 2f, fy, fz, -d.finLen, 0, 0, d.finLen, 1, d.finDepth).rot(0, 0, d.flat ? -0.05 : -0.5);
        float w = Math.max(bW, Math.min(bL * 0.45f, bW * 2f));
        if (d.flat) w = bW + d.finLen;
        return new float[]{w, Math.max(bH, 3)};
    }

    // ------------------------------------------------------------------ yilan
    private float[] snake(AnimalDef d) {
        int w = d.bodyW, h = d.bodyH, l = d.bodyL;
        float top = 24 - h;
        float z = -(d.segments * l) / 2f;
        add("head", null, Role.HEAD, 0, top, z, -(w + 1) / 2f, -0.5, -l, w + 1, h, l);
        for (int i = 0; i < d.segments; i++) {
            boolean last = i >= d.segments - 2;
            int sw = last ? Math.max(1, w - 1) : w, sh = last ? Math.max(1, h - 1) : h;
            add("seg_" + i, null, i == d.segments - 1 ? Role.TAIL : Role.BODY, 0, 24 - sh, z + i * l, -sw / 2f, 0, 0, sw, sh, l);
        }
        return new float[]{Math.max(6, w * 3), h + 1};
    }

    // ------------------------------------------------------------------ yengec
    private float[] crab(AnimalDef d) {
        int bW = d.bodyW, bH = d.bodyH, bL = d.bodyL;
        float top = 24 - d.legLen * 0.5f - bH;
        add("body", null, Role.BODY, 0, top, 0, -bW / 2f, 0, -bL / 2f, bW, bH, bL);
        for (int s = -1; s <= 1; s += 2) {
            String sn = s > 0 ? "p" : "n";
            for (int i = 0; i < 3; i++) {
                float z = -bL / 2f + 1 + i * (bL - 2) / 2f;
                add("cleg_" + sn + i, null, Role.LEG, s * bW / 2f, top + bH - 1, z, s > 0 ? 0 : -d.legLen, -0.5, -0.5, d.legLen, 1, 1)
                        .rot(0, s * (i - 1) * -0.35, s * 0.55);
            }
            add("claw_" + sn, null, Role.TUFT, s * (bW / 2f), top + bH - 2, -bL / 2f, -1.5, -1, -4, 3, 2, 4).rot(0, s * -0.5, 0);
            add("eye_" + sn, "body", Role.EYE_STALK, s * (bW / 4f), 0, -bL / 2f + 0.5, -0.5, -2, -0.5, 1, 2, 1);
        }
        return new float[]{bW + d.legLen, d.legLen * 0.5f + bH + 1};
    }

    // ------------------------------------------------------------------ doku yerlesimi
    private void pack() {
        int[][] sizes = {{32, 32}, {64, 32}, {64, 64}, {128, 64}, {128, 128}, {256, 128}, {256, 256}, {512, 256}, {512, 512}};
        List<Part> order = new ArrayList<>(parts);
        order.sort(Comparator.comparingInt((Part p) -> -(p.d + p.h)).thenComparingInt(p -> -(2 * p.d + 2 * p.w)));
        for (int[] s : sizes) {
            if (tryPack(order, s[0], s[1])) { texW = s[0]; texH = s[1]; return; }
        }
        throw new IllegalStateException("Doku sigmadi: " + def.id);
    }

    private boolean tryPack(List<Part> order, int W, int H) {
        int x = 0, y = 0, rowH = 0;
        for (Part p : order) {
            int w = 2 * p.d + 2 * p.w, h = p.d + p.h;
            if (w > W) return false;
            if (x + w > W) { x = 0; y += rowH; rowH = 0; }
            if (y + h > H) return false;
            p.u = x; p.v = y;
            x += w;
            rowH = Math.max(rowH, h);
        }
        return true;
    }
}
