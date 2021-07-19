package gregtech.api.block.machine.module.api;

import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.render.model.state.ModelState;

public interface ModelStateAwareModule {

    void setupModelState(ModelState.Builder<?> builder);

    default void refreshModelState() {
        MachineModule machineModule = (MachineModule) this;
        machineModule.getMachine().refreshModelStateAndRedraw();
    }
}
