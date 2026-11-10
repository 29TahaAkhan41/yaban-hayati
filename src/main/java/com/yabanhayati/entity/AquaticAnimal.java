package com.yabanhayati.entity;

import com.yabanhayati.YabanHayati;
import com.yabanhayati.def.AnimalDef;
import com.yabanhayati.def.AnimalDef.Temper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Tum su hayvanlari icin ortak sinif (kopekbaligi, balina, ton baligi...). */
public class AquaticAnimal extends WaterAnimal {
    private AnimalDef def;

    public AquaticAnimal(EntityType<? extends AquaticAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public AnimalDef def() {
        if (def == null) def = YabanHayati.defOf(getType());
        return def;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        AnimalDef d = def();
        goalSelector.addGoal(0, new TryFindWaterGoal(this));
        if (d.attacks()) goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, true));
        else goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        if (d.prey)
            goalSelector.addGoal(2, new AvoidEntityGoal<>(this, AquaticAnimal.class, 8f, 1.2, 1.5,
                    e -> e instanceof AquaticAnimal a && a.isHunter()));
        goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        if (d.attacks()) {
            targetSelector.addGoal(1, new HurtByTargetGoal(this));
            if (d.temper == Temper.AGGRESSIVE) {
                targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, LivingEntity::isInWater));
                targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, WaterAnimal.class, 200, true, false, this::isPrey));
            }
        }
    }

    public boolean isHunter() {
        Temper t = def().temper;
        return (t == Temper.PREDATOR || t == Temper.AGGRESSIVE) && def().attack >= 4;
    }

    private boolean isPrey(LivingEntity e) {
        if (def().attack < 4) return false;
        return e instanceof AbstractFish || (e instanceof AquaticAnimal a && a.def().prey);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (isEffectiveAi() && isInWater()) {
            moveRelative(getSpeed(), travelVector);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9));
            if (getTarget() == null) setDeltaMovement(getDeltaMovement().add(0, -0.005, 0));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public void aiStep() {
        // karada cirpinma
        if (!isInWater() && onGround() && verticalCollision) {
            setDeltaMovement(getDeltaMovement().add((random.nextFloat() * 2 - 1) * 0.05F, 0.3, (random.nextFloat() * 2 - 1) * 0.05F));
            setOnGround(false);
            hasImpulse = true;
        }
        super.aiStep();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.COD_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.COD_DEATH; }
}
