package com.yabanhayati.client;

import com.yabanhayati.def.AnimalDef;
import com.yabanhayati.def.ModelShape;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/** AnimalDef'ten uretilen modelin oyun ici hali ve animasyonlari. */
public class DefModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final AnimalDef def;
    private final Map<String, ModelPart> parts = new HashMap<>();

    public DefModel(ModelPart root, AnimalDef def) {
        this.root = root;
        this.def = def;
        for (ModelShape.Part p : ModelShape.of(def).parts) {
            ModelPart parent = p.parent == null ? root : parts.get(p.parent);
            parts.put(p.name, parent.getChild(p.name));
        }
    }

    public static LayerDefinition createLayer(AnimalDef def) {
        ModelShape shape = ModelShape.of(def);
        MeshDefinition mesh = new MeshDefinition();
        Map<String, PartDefinition> defs = new HashMap<>();
        for (ModelShape.Part p : shape.parts) {
            PartDefinition parent = p.parent == null ? mesh.getRoot() : defs.get(p.parent);
            defs.put(p.name, parent.addOrReplaceChild(p.name,
                    CubeListBuilder.create().texOffs(p.u, p.v).addBox(p.ox, p.oy, p.oz, p.w, p.h, p.d),
                    PartPose.offsetAndRotation(p.px, p.py, p.pz, p.rx, p.ry, p.rz)));
        }
        return LayerDefinition.create(mesh, shape.texW, shape.texH);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    private ModelPart p(String name) {
        return parts.get(name);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        float yaw = Mth.clamp(netHeadYaw, -60, 60) * Mth.DEG_TO_RAD, pitch = Mth.clamp(headPitch, -40, 40) * Mth.DEG_TO_RAD;
        switch (def.shape) {
            case QUAD -> {
                float f = 0.6662f * Mth.clamp(8f / Math.max(4, def.legLen), 0.6f, 1.4f);
                float amp = def.legLen <= 2 ? 0.8f : 1.2f;
                float a = Mth.cos(limbSwing * f) * amp * limbSwingAmount, b = Mth.cos(limbSwing * f + Mth.PI) * amp * limbSwingAmount;
                p("leg_fp").xRot += a; p("leg_bn").xRot += a;
                p("leg_fn").xRot += b; p("leg_bp").xRot += b;
                ModelPart neck = p("neck"), head = p("head");
                if (neck != null) { neck.yRot += yaw * 0.5f; head.yRot += yaw * 0.5f; head.xRot += pitch * 0.6f; }
                else { head.yRot += yaw; head.xRot += pitch; }
                ModelPart tail = p("tail");
                if (tail != null) {
                    switch (def.tail) {
                        case THIN, TUFT -> tail.zRot += Mth.sin(age * 0.09f) * 0.18f;
                        case BUSHY, THICK -> {
                            tail.yRot += Mth.sin(age * 0.08f + limbSwing * 0.4f) * (0.12f + 0.25f * limbSwingAmount);
                            ModelPart t2 = p("tail2");
                            if (t2 != null) t2.yRot += Mth.sin(age * 0.08f + limbSwing * 0.4f - 0.8f) * (0.15f + 0.3f * limbSwingAmount);
                        }
                        default -> tail.yRot += Mth.sin(age * 0.2f) * 0.1f;
                    }
                }
                ModelPart trunk = p("trunk");
                if (trunk != null) { trunk.xRot += Mth.sin(age * 0.07f) * 0.12f - 0.05f; trunk.zRot += Mth.cos(age * 0.05f) * 0.06f; }
                if (def.ears == AnimalDef.Ears.FLAP) {
                    float e = Mth.sin(age * 0.1f) * 0.12f;
                    p("ear_p").yRot += e; p("ear_n").yRot -= e;
                }
            }
            case BIRD -> {
                float a = Mth.cos(limbSwing * 0.9f) * 1.1f * limbSwingAmount;
                boolean airborne = def.flying && !entity.onGround();
                if (airborne) a = 0.6f;
                p("leg_fp").xRot += airborne ? 0.6f : a;
                p("leg_fn").xRot += airborne ? 0.6f : -a;
                ModelPart neck = p("neck"), head = p("head");
                if (neck != null) { neck.yRot += yaw * 0.5f; head.yRot += yaw * 0.5f; head.xRot += pitch * 0.6f; neck.xRot += Mth.cos(limbSwing * 0.9f) * 0.08f * limbSwingAmount; }
                else { head.yRot += yaw; head.xRot += pitch; }
                float wing = airborne ? Mth.HALF_PI + Mth.sin(age * 1.1f) * 0.75f
                        : (entity.isInWater() || limbSwingAmount > 0.9f ? 0.25f + Mth.sin(age * 0.8f) * 0.2f : 0);
                p("wing_p").zRot -= wing; p("wing_n").zRot += wing;
                ModelPart tail = p("tail");
                if (tail != null) tail.xRot += Mth.sin(age * 0.1f) * 0.04f;
            }
            case FISH -> {
                ModelPart body = p("body");
                body.xRot += pitch;
                float sw = Mth.sin(age * 0.35f) * (0.18f + 0.3f * Math.min(1, limbSwingAmount));
                ModelPart tail = p("tail"), fin = p("tail_fin");
                if (def.fluke) { tail.xRot += sw * 0.8f; if (fin != null) fin.xRot += sw; }
                else { tail.yRot += sw; if (fin != null) fin.yRot += sw * 0.8f; }
                float fl = Mth.sin(age * 0.15f) * (def.flat ? 0.3f : 0.12f);
                p("fin_p").zRot += fl; p("fin_n").zRot -= fl;
            }
            case SNAKE -> {
                float m = 0.25f + Math.min(1, limbSwingAmount);
                for (int i = 0; i < def.segments; i++) {
                    ModelPart s = p("seg_" + i);
                    float ph = limbSwing * 0.9f + age * 0.06f - i * 1.1f;
                    s.x += Mth.sin(ph) * 1.1f * m;
                    s.yRot += Mth.cos(ph) * 0.35f * m;
                }
                ModelPart head = p("head");
                head.x += Mth.sin(limbSwing * 0.9f + age * 0.06f + 1.1f) * 0.6f * m;
                head.yRot += yaw * 0.6f;
            }
            case CRAB -> {
                for (int i = 0; i < 3; i++) {
                    float w = Mth.sin(limbSwing * 1.6f + i * 2.1f) * 0.5f * limbSwingAmount;
                    p("cleg_p" + i).zRot += w; p("cleg_n" + i).zRot += w;
                }
                float c = Mth.sin(age * 0.12f) * 0.12f;
                p("claw_p").yRot += c; p("claw_n").yRot -= c;
            }
        }
    }
}
