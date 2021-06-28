package gregtech.api.gui.igredient;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.function.Consumer;

public class FluidIngredientTarget implements IGhostIngredientTarget.Target {

    private final Rectangle rectangle;
    private final Consumer<FluidVolume> consumer;

    public FluidIngredientTarget(Rectangle rectangle, Consumer<FluidVolume> consumer) {
        this.rectangle = rectangle;
        this.consumer = consumer;
    }

    @Override
    public Rectangle getArea() {
        return rectangle;
    }

    @Override
    public boolean accept(Object ingredient) {
        FluidVolume extractedFluid = retrieveFluidFromIngredient(ingredient);
        if (!extractedFluid.isEmpty()) {
            this.consumer.accept(extractedFluid);
            return true;
        }
        return false;
    }

    public static FluidVolume retrieveFluidFromIngredient(Object ingredient) {
        if (ingredient instanceof ItemStack itemStack) {
            FluidExtractable extractable = FluidAttributes.EXTRACTABLE.get(itemStack);
            return extractable.attemptAnyExtraction(FluidAmount.A_MILLION, Simulation.SIMULATE);
        }
        if (ingredient instanceof FluidVolume fluidVolume) {
            return fluidVolume;
        }
        return FluidVolumeUtil.EMPTY;
    }
}
