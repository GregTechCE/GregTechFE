package gregtech.api.block.machine.module;

import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.render.model.state.ModelStateManager;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class ConfiguredModule<C extends MachineModuleConfig> {

    private final MachineModuleType<C, ?> moduleType;
    private final C config;

    private ConfiguredModule(MachineModuleType<C, ?> moduleType, C config) {
        this.moduleType = moduleType;
        this.config = config;
    }

    public MachineModuleType<C, ?> getModuleType() {
        return moduleType;
    }

    public C getConfig() {
        return config;
    }

    public static <C extends MachineModuleConfig> ConfiguredModule<C> of(MachineModuleType<C, ?> type, C config) {
        return new ConfiguredModule<>(type, config);
    }

    public void appendTooltip(ItemStack stack, List<Text> tooltip, TooltipContext options) {
        this.moduleType.appendTooltip(stack, tooltip, options, this.config);
    }

    public void appendModelProperties(ModelStateManager.Builder<?> stateManager) {
        this.moduleType.appendModelProperties(stateManager, this.config);
    }

    public boolean needsTicking(MachineTickType tickType) {
        return this.moduleType.needsTicking(tickType, this.config);
    }

    public MachineModule<C> createModule(MachineBlockEntity machine) {
        return this.moduleType.createModule(machine, this.config);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfiguredModule<?> that = (ConfiguredModule<?>) o;
        return moduleType.equals(that.moduleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleType);
    }
}
