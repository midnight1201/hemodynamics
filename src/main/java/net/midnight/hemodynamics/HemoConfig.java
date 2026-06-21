package net.midnight.hemodynamics;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class HemoConfig {

    public static final ModConfigSpec SERVER_SPEC;
    public static final ModConfigSpec.IntValue MB_PER_HEALTH;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("blood_harvest");
        MB_PER_HEALTH = builder
                .comment("Amount of blood (mB) produced per point of max health")
                .defineInRange("mbPerHealth", 50, 0, 100);
        builder.pop();

        SERVER_SPEC = builder.build();
    }

    private HemoConfig() {}
}