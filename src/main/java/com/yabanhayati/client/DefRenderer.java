package com.yabanhayati.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yabanhayati.YabanHayati;
import com.yabanhayati.def.AnimalDef;
import com.yabanhayati.def.ModelShape;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class DefRenderer<T extends Mob> extends MobRenderer<T, DefModel<T>> {
    private final AnimalDef def;
    private final ResourceLocation texture;

    public DefRenderer(EntityRendererProvider.Context ctx, AnimalDef def) {
        super(ctx, new DefModel<>(ctx.bakeLayer(layer(def)), def), ModelShape.of(def).width * 0.6f);
        this.def = def;
        this.texture = YabanHayati.id("textures/entity/" + def.id + ".png");
    }

    public static ModelLayerLocation layer(AnimalDef def) {
        return new ModelLayerLocation(YabanHayati.id(def.id), "main");
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTick) {
        float s = def.scale * (entity.isBaby() ? 0.5f : 1f);
        poseStack.scale(s, s, s);
    }
}
