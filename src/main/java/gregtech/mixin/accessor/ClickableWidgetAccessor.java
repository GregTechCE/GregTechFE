package gregtech.mixin.accessor;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClickableWidget.class)
public interface ClickableWidgetAccessor {

    @Accessor
    void setHeight(int height);

    @Accessor
    void setY(int y);
}
