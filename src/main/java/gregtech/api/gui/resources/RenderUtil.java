package gregtech.api.gui.resources;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class RenderUtil {

    public static void drawFluidForGui(MatrixStack matrices, FluidVolume contents, FluidAmount tankCapacity, int startX, int startY, int width, int height) {
        double tankFullPercent = 1.0;
        if (!tankCapacity.isZero()) {
            tankFullPercent = contents.amount().div(tankCapacity).asInexactDouble();
        }
        int actualHeight = (int) Math.round(height * tankFullPercent);
        contents.renderGuiRect(startX, startY, startX + width, startY + actualHeight);
    }
}
