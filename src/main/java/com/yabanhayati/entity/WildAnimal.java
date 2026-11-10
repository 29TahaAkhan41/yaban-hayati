package com.yabanhayati.entity;

import com.yabanhayati.YabanHayati;
import com.yabanhayati.def.AnimalDef;
import com.yabanhayati.def.AnimalDef.Temper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Set;

/** Tum kara hayvanlari icin ortak sinif; davranis AnimalDef tablosundan gelir. */
public class WildAnimal extends Animal {
    private AnimalDef def;
    private Set<String> foods;

    public WildAnimal(EntityType<? extends WildAnimal> type, Level level) {
        super(type, level);
    }

    public AnimalDef def() {
        if (def == null) def = YabanHayati.defOf(getType());
        return def;
    }

    @Override
    protected void registerGoals() {
        AnimalDef d = def();
        boolean fights = d.attacks();
        goalSelector.addGoal(0, new FloatGoal(this));
        if (fights) goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.3, true));
        else goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        if (d.prey)
            goalSelector.addGoal(2, new AvoidEntityGoal<>(this, WildAnimal.class, 8f, 1.2, 1.5,
                    e -> e instanceof WildAnimal w && w.isHunter() && !w.isBaby()));
        goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        goalSelector.addGoal(4, new TemptGoal(this, 1.15, this::isFood, false));
        goalSelector.addGoal(5, new FollowParentGoal(this, 1.15));
        if (d.flying) goalSelector.addGoal(6, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        else goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6f));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        if (fights) {
            targetSelector.addGoal(1, new HurtByTargetGoal(this));
            if (d.temper == Temper.AGGRESSIVE)
                targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
            if (isHunter())
                targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Animal.class, 120, true, false, this::isPrey));
        }
    }

    public boolean isHunter() {
        Temper t = def().temper;
        return t == Temper.PREDATOR || t == Temper.AGGRESSIVE;
    }

    private boolean isPrey(LivingEntity e) {
        if (isBaby()) return false;
        if (e instanceof WildAnimal w) return w.def().prey && w.getType() != getType();
        return e instanceof Chicken || e instanceof Rabbit || e instanceof Sheep;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        if (foods == null) foods = new java.util.HashSet<>(java.util.Arrays.asList(def().foods));
        return foods.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return (AgeableMob) getType().create(level);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && def().venom && target instanceof LivingEntity living)
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0), this);
        return hit;
    }

    // ------------------------------------------------------------------ sesler (vanilla seslerini kullanir)
    @Nullable
    static SoundEvent vanillaSound(@Nullable String entity, String kind) {
        if (entity == null) return null;
        return BuiltInRegistries.SOUND_EVENT.getOptional(ResourceLocation.withDefaultNamespace("entity." + entity + "." + kind)).orElse(null);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return vanillaSound(def().sound, "ambient");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        SoundEvent s = vanillaSound(def().sound, "hurt");
        return s != null ? s : super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        SoundEvent s = vanillaSound(def().sound, "death");
        return s != null ? s : super.getDeathSound();
    }
}
