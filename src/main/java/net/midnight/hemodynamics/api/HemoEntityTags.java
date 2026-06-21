package net.midnight.hemodynamics.api;

import net.midnight.hemodynamics.Hemodynamics;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class HemoEntityTags {
    public static final TagKey<EntityType<?>> BLOOD_TRACE  = tag("blood/trace");   // 10%
    public static final TagKey<EntityType<?>> BLOOD_LOW    = tag("blood/low");     // 25%
    public static final TagKey<EntityType<?>> BLOOD_MEDIUM = tag("blood/medium");  // 50%
    public static final TagKey<EntityType<?>> BLOOD_FULL   = tag("blood/full");    // 100%

    private static TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Hemodynamics.asResource(name));
    }
}