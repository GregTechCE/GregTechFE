package gregtech.api.module.util;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

public class SlotAlignmentHelper {

    private static final Map<Integer, SlotAlignment> SLOT_PLACEMENTS = new HashMap<>();

    public static class SlotAlignment {
        private final int horizontalSlots;
        private final int verticalSlots;

        public SlotAlignment(int horizontalSlots, int verticalSlots) {
            this.horizontalSlots = horizontalSlots;
            this.verticalSlots = verticalSlots;
        }

        public int getHorizontalSlots() {
            return horizontalSlots;
        }

        public int getVerticalSlots() {
            return verticalSlots;
        }

        public int getTotalSlots() {
            return horizontalSlots * verticalSlots;
        }
    }

    public static class FluidSlotAlignment {
        private final SlotAlignment slotAlignment;
        private final boolean useSeparatePage;

        public FluidSlotAlignment(SlotAlignment slotAlignment, boolean useSeparatePage) {
            this.slotAlignment = slotAlignment;
            this.useSeparatePage = useSeparatePage;
        }

        public SlotAlignment getSlotAlignment() {
            return slotAlignment;
        }

        public boolean isUseSeparatePage() {
            return useSeparatePage;
        }
    }

    public static void register(int slots, SlotAlignment alignment) {
        Preconditions.checkNotNull(alignment, "alignment");
        SLOT_PLACEMENTS.put(slots, alignment);
    }

    public static SlotAlignment getAlignmentForSlots(int slots) {
        return Preconditions.checkNotNull(SLOT_PLACEMENTS.get(slots), "Alignment rules not defined for slot count %d", slots);
    }

    public static FluidSlotAlignment getAlignmentForFluidSlots(SlotAlignment inventorySlots, int fluidSlots) {
        //we align the fluid slots in the same way as inventory ones if we got no inventory slots
        if (inventorySlots.getTotalSlots() == 0) {
            return new FluidSlotAlignment(getAlignmentForSlots(fluidSlots), false);
        }
        //for 4 or less fluid slots, we arrange them behind the item slots in one single row
        if (fluidSlots <= 4) {
            return new FluidSlotAlignment(new SlotAlignment(fluidSlots, 1), false);
        }
        //otherwise we use the separate page for the fluid slots
        return new FluidSlotAlignment(getAlignmentForSlots(fluidSlots), true);
    }

    static {
        register(1, new SlotAlignment(1, 1));
        register(2, new SlotAlignment(2, 1));
        register(3, new SlotAlignment(3, 1));

        register(4, new SlotAlignment(2, 2));
        register(5, new SlotAlignment(5, 1));
        register(6, new SlotAlignment(3, 2));
        register(8, new SlotAlignment(4, 2));

        register(9, new SlotAlignment(3, 3));
        register(10, new SlotAlignment(5, 2));
        register(12, new SlotAlignment(4, 3));

        register(15, new SlotAlignment(5, 3));
        register(16, new SlotAlignment(4, 4));
    }
}
