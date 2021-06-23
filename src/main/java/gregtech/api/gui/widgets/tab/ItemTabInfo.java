package gregtech.api.gui.widgets.tab;

import com.google.common.collect.Lists;
import gregtech.api.gui.GuiUtils;
import gregtech.api.gui.resources.TextureArea;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Optional;

public class ItemTabInfo implements ITabInfo {

    private final String nameLocale;
    private final ItemStack iconStack;

    public ItemTabInfo(String nameLocale, ItemStack iconStack) {
        this.nameLocale = nameLocale;
        this.iconStack = iconStack;
    }

    @Override
    public void renderTab(MatrixStack matrices, TextureArea tabTexture, int posX, int posY, int xSize, int ySize, boolean isSelected) {
        tabTexture.draw(matrices, posX, posY, xSize, ySize);
        GuiUtils.drawItemStack(iconStack, posX + xSize / 2 - 8, posY + ySize / 2 - 8, null);
    }

    @Override
    public void renderHoverText(MatrixStack matrices, int posX, int posY, int xSize, int ySize, int guiWidth, int guiHeight, boolean isSelected, int mouseX, int mouseY) {
        Text text = new TranslatableText(nameLocale);
        GuiUtils.renderTooltip(matrices, Lists.newArrayList(text), Optional.empty(), mouseX, mouseY);
    }
}
