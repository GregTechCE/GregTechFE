package gregtech.api.gui.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;

import java.util.Stack;

public class ScissorStack {

    private static final Stack<int[]> scissorFrameStack = new Stack<>();

    public static void useScissor(int x, int y, int width, int height, Runnable codeBlock) {
        pushScissorFrame(x, y, width, height);
        try {
            codeBlock.run();
        } finally {
            popScissorFrame();
        }
    }

    private static int[] peekFirstScissorOrFullScreen() {
        int[] currentTopFrame = scissorFrameStack.isEmpty() ? null : scissorFrameStack.peek();

        if(currentTopFrame == null) {
            MinecraftClient client = MinecraftClient.getInstance();

            int displayWidth = client.getWindow().getWidth();
            int displayHeight = client.getWindow().getHeight();

            return new int[] {0, 0, displayWidth, displayHeight};
        }

        return currentTopFrame;
    }

    public static void pushScissorFrame(int x, int y, int width, int height) {
        int[] parentScissor = peekFirstScissorOrFullScreen();
        int parentX = parentScissor[0];
        int parentY = parentScissor[1];
        int parentWidth = parentScissor[2];
        int parentHeight = parentScissor[3];

        boolean pushedFrame = false;
        if(x <= parentX + parentWidth && y <= parentY + parentHeight) {
            int newX = Math.max(x, parentX);
            int newY = Math.max(y, parentY);
            int newWidth = width - (newX - x);
            int newHeight = height - (newY - y);
            if(newWidth > 0 && newHeight > 0) {
                int maxWidth = parentWidth - (x - parentX);
                int maxHeight = parentHeight - (y - parentY);
                newWidth = Math.min(maxWidth, newWidth);
                newHeight = Math.min(maxHeight, newHeight);
                applyScissor(newX, newY, newWidth, newHeight);

                //finally, push applied scissor on top of scissor stack
                scissorFrameStack.push(new int[] {newX, newY, newWidth, newHeight});
                pushedFrame = true;
            }
        }

        if(!pushedFrame) {
            scissorFrameStack.push(new int[] {parentX, parentY, parentWidth, parentHeight});
        }
    }

    public static void popScissorFrame() {
        scissorFrameStack.pop();

        int[] parentScissor = peekFirstScissorOrFullScreen();
        int parentX = parentScissor[0];
        int parentY = parentScissor[1];
        int parentWidth = parentScissor[2];
        int parentHeight = parentScissor[3];
        applyScissor(parentX, parentY, parentWidth, parentHeight);

        if (scissorFrameStack.isEmpty()) {
            RenderSystem.disableScissor();
        }
    }

    //applies scissor with gui-space coordinates and sizes
    private static void applyScissor(int x, int y, int w, int h) {
        //translate upper-left to bottom-left
        MinecraftClient client = MinecraftClient.getInstance();

        double s = client.getWindow().getScaleFactor();
        int translatedY = client.getWindow().getScaledHeight() - y - h;
        RenderSystem.enableScissor((int) (x * s), (int) (translatedY * s), (int) (w * s), (int) (h * s));
    }

}
