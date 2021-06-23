package gregtech.api.gui.igredient;

import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.function.Consumer;

public class ItemIngredientTarget implements IGhostIngredientTarget.Target {

    private final Rectangle rectangle;
    private final Consumer<ItemStack> consumer;

    public ItemIngredientTarget(Rectangle rectangle, Consumer<ItemStack> consumer) {
        this.rectangle = rectangle;
        this.consumer = consumer;
    }

    @Override
    public Rectangle getArea() {
        return rectangle;
    }

    @Override
    public boolean accept(Object ingredient) {
        if (ingredient instanceof ItemStack itemStack) {
            this.consumer.accept(itemStack);
            return true;
        }
        return false;
    }
}
