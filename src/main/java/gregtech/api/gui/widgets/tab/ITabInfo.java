package gregtech.api.gui.widgets.tab;

import gregtech.api.gui.resources.TextureArea;
import net.minecraft.client.util.math.MatrixStack;

public interface ITabInfo {

    void renderTab(MatrixStack matrices, TextureArea tabTexture, int posX, int posY, int xSize, int ySize, boolean isSelected);

    void renderHoverText(MatrixStack matrices, int posX, int posY, int xSize, int ySize, int guiWidth, int guiHeight, boolean isSelected, int mouseX, int mouseY);

}
