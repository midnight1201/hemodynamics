package net.midnight.hemodynamics.content;


import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public class BloodFluidType extends FluidType {

    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private Vector3f fogColor;
    private Supplier<Float> fogDistance;

    public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
        return (properties, still, flow) -> {
            BloodFluidType type = new BloodFluidType(properties, still, flow);
            type.fogColor = new Color(fogColor, false).asVectorF();
            type.fogDistance = fogDistance;
            return type;
        };
    }
    private BloodFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
    }
    @Override
    @SuppressWarnings("removal")
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }
            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }
            @Override
            public int getTintColor(FluidStack stack) {
                return 0xffffffff;
            }
            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0x00ffffff;
            }
            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor == null ? fluidFogColor : fogColor;
            }
            @Override
            public void modifyFogRender(Camera camera, FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                float modifier = fogDistance.get();
                float baseWaterFog = 96.0f;
                if (modifier != 1f) {
                    RenderSystem.setShaderFogShape(FogShape.CYLINDER);
                    RenderSystem.setShaderFogStart(-8);
                    RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
                }
            }
        });
    }
}