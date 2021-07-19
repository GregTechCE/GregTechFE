package gregtech.api.block.machine;

import gregtech.api.render.model.state.ModelState;
import net.minecraft.block.Block;

public class MachineBlockEntityAttachment {

    private final ModelState<Block> modelState;

    public MachineBlockEntityAttachment(ModelState<Block> modelState) {
        this.modelState = modelState;
    }

    public ModelState<Block> getModelState() {
        return this.modelState;
    }
}
