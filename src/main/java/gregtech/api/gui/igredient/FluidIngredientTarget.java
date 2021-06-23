package gregtech.api.gui.igredient;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import gregtech.api.util.GTUtility;

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
        FluidVolume extractedFluid = GTUtility.retrieveFluidFromIngredient(ingredient);
        if (!extractedFluid.isEmpty()) {
            this.consumer.accept(extractedFluid);
            return true;
        }
        return false;
    }
}
