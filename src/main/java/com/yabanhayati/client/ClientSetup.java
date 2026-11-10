package com.yabanhayati.client;

import com.yabanhayati.YabanHayati;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = YabanHayati.MODID, value = Dist.CLIENT)
public final class ClientSetup {
    @SubscribeEvent
    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        YabanHayati.LAND.keySet().forEach(def -> event.registerLayerDefinition(DefRenderer.layer(def), () -> DefModel.createLayer(def)));
        YabanHayati.WATER.keySet().forEach(def -> event.registerLayerDefinition(DefRenderer.layer(def), () -> DefModel.createLayer(def)));
    }

    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        YabanHayati.LAND.forEach((def, type) -> event.registerEntityRenderer(type.get(), ctx -> new DefRenderer<>(ctx, def)));
        YabanHayati.WATER.forEach((def, type) -> event.registerEntityRenderer(type.get(), ctx -> new DefRenderer<>(ctx, def)));
    }

    private ClientSetup() {}
}
