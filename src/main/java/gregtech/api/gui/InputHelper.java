package gregtech.api.gui;

import gregtech.mixin.MouseMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class InputHelper {

    public static void playButtonClickSound() {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    //TODO not exactly clean and might cause problems
    public static int getActiveMouseButton() {
        MinecraftClient client = MinecraftClient.getInstance();
        return ((MouseMixin) client.mouse).getActiveButton();
    }

    public static boolean isShiftDown() {
        long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();
        return InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isCtrlDown() {
        long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();
        if (MinecraftClient.IS_SYSTEM_MAC) {
            return InputUtil.isKeyPressed(windowHandle, 343) || InputUtil.isKeyPressed(windowHandle, 347);
        } else {
            return InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_LEFT_CONTROL) ||
                    InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_RIGHT_CONTROL);
        }
    }

    public static boolean isAltDown() {
        long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();
        return InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_LEFT_ALT) ||
                InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_RIGHT_ALT);
    }
}
