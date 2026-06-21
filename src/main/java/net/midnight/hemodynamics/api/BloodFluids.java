package net.midnight.hemodynamics.api;

import net.midnight.hemodynamics.HemoFluids;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public final class BloodFluids {
    private BloodFluids() {}

    /** The still/source blood fluid (id: hemodynamics:blood). */
    public static Fluid source() { return HemoFluids.BLOOD.get().getSource(); }

    /** The flowing blood fluid (id: hemodynamics:flowing_blood). */
    public static Fluid flowing() { return HemoFluids.BLOOD.get(); }

    /** The c:blood common fluid tag. */
    public static final TagKey<Fluid> TAG = HemoFluidTags.BLOOD;

    /** Convenience: a FluidStack of blood. */
    public static FluidStack of(int amountMb) {
        return new FluidStack(source(), amountMb);
    }
}