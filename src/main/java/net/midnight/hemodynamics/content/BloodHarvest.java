package net.midnight.hemodynamics.content;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.midnight.hemodynamics.HemoConfig;
import net.midnight.hemodynamics.api.HemoEntityTags;
import net.midnight.hemodynamics.HemoFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class BloodHarvest {

    private static final int MAX_DEPTH = 3;

    private static int getBloodAmount(LivingEntity entity) {
        float fraction = bloodFraction(entity);
        if (fraction <= 0f) return 0;
        return Math.round(entity.getMaxHealth() * HemoConfig.MB_PER_HEALTH.get() * fraction);
    }

    private static float bloodFraction(LivingEntity entity) {
        var holder = entity.getType().builtInRegistryHolder();
        if (holder.is(HemoEntityTags.BLOOD_TRACE))   return 0.10f; // bogged, husk
        if (holder.is(EntityTypeTags.SKELETONS))            return 0f;
        if (holder.is(HemoEntityTags.BLOOD_MEDIUM))  return 0.50f; // arthropods
        if (holder.is(HemoEntityTags.BLOOD_LOW))     return 0.25f; // undead
        if (holder.is(HemoEntityTags.BLOOD_FULL))   return 1.00f;
        if (entity instanceof Animal
                || entity instanceof AbstractVillager
                || entity instanceof WaterAnimal
                || entity instanceof AmbientCreature) return 1.00f;
        return 0f;
    }

    public static void depositBlood(Level level, BlockPos controllerPos, LivingEntity entity) {
        if (level == null || level.isClientSide) return;
        int amount = getBloodAmount(entity);
        if (amount <= 0) return;
        FluidStack blood = new FluidStack(HemoFluids.BLOOD.get().getSource(), amount);
        for (int i = 1; i <= MAX_DEPTH; i++) {
            BlockPos pos = controllerPos.below(i);
            BlockEntity be = level.getBlockEntity(pos);

            if (!(be instanceof ItemDrainBlockEntity) && !(be instanceof BasinBlockEntity))
                continue;
            // Item Drain
            SmartFluidTankBehaviour tank = BlockEntityBehaviour.get(level, pos, SmartFluidTankBehaviour.TYPE);
            if (tank != null && tank.getPrimaryHandler().fill(blood, FluidAction.EXECUTE) > 0)
                return;
            // Basin
            IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, Direction.UP);
            if (handler == null)
                handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
            if (handler != null && handler.fill(blood, FluidAction.EXECUTE) > 0)
                return;
        }
    }
}