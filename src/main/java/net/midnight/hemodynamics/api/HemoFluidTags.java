package net.midnight.hemodynamics.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public final class HemoFluidTags {
    private HemoFluidTags() {}

    public static final TagKey<Fluid> BLOOD = TagKey.create(
            Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", "blood"));
}