package net.midnight.hemodynamics.mixin;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import net.midnight.hemodynamics.content.BloodHarvest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrushingWheelControllerBlockEntity.class)
public abstract class CrushingWheelControllerBlockEntityMixin extends BlockEntity {

    public CrushingWheelControllerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Shadow public Entity processingEntity;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
                    shift = At.Shift.AFTER))
    private void vampengines$harvestBlood(CallbackInfo ci) {
        if (processingEntity instanceof LivingEntity living && !living.isAlive()) {
            BloodHarvest.depositBlood(this.getLevel(), this.getBlockPos(), living);
        }
    }
}