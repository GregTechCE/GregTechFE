package gregtech.mixin;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClickableWidget.class)
public interface ClickableWidgetMixin {

    @Accessor
    void setHeight(int height);

    @Accessor
    void setY(int y);
}
