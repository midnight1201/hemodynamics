package net.midnight.hemodynamics;

import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.midnight.hemodynamics.content.BloodFluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry.InteractionInformation;

import java.util.Objects;

public class HemoFluids {

    public static final FluidEntry<BaseFlowingFluid.Flowing> BLOOD =
            Hemodynamics.REGISTRATE.standardFluid("blood",
                            BloodFluidType.create(0x6E0A0A, () -> 1f / 32f))
                    .lang("Blood")
                    .properties(b -> b.viscosity(1500)
                            .density(1400))
                    .fluidProperties(p -> p.levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .source(BaseFlowingFluid.Source::new)
                    .block()
                    .properties(p -> p.mapColor(MapColor.COLOR_RED))
                    .build()
                    .bucket()
                    .tab(Objects.requireNonNull(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey()))
                    .onRegister(HemoFluids::registerFluidDispenseBehavior)
                    .tag(Tags.Items.BUCKETS)
                    .build()
                    .register();

    private static final DispenseItemBehavior DEFAULT = new DefaultDispenseItemBehavior();
    private static final DispenseItemBehavior DISPENSE_FLUID = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            DispensibleContainerItem container = (DispensibleContainerItem) stack.getItem();
            BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
            Level level = source.level();
            if (container.emptyContents(null, level, pos, null, stack)) {
                return new ItemStack(Items.BUCKET);
            }
            return DEFAULT.dispense(source, stack);
        }
    };

    public static void registerFluidInteractions() {
        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(),
                new InteractionInformation(
                        BLOOD.get().getFluidType(),
                        fluidState -> fluidState.isSource()
                                ? Blocks.OBSIDIAN.defaultBlockState()
                                : Blocks.NETHERRACK.defaultBlockState()
                ));
    }

    private static void registerFluidDispenseBehavior(BucketItem bucket) {
        DispenserBlock.registerBehavior(bucket, DISPENSE_FLUID);
    }

    public static void register() {
        Hemodynamics.LOGGER.info("Register Fluids...");
    }
}
