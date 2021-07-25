package gregtech.api.block.machine.module;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.render.model.state.ModelStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.*;

public class MachineModuleCollection {

    private final ImmutableSet<ConfiguredModule<?>> modules;

    protected MachineModuleCollection(Collection<ConfiguredModule<?>> modules) {
        this.modules = ImmutableSet.copyOf(modules);
    }

    public boolean needsTicking(MachineTickType tickType) {
        return this.modules.stream().anyMatch(module -> module.needsTicking(tickType));
    }

    public void appendTooltip(ItemStack stack, List<Text> tooltip, TooltipContext options) {
        this.modules.forEach(module -> module.appendTooltip(stack, tooltip, options));
    }

    public void appendModelStateProperties(ModelStateManager.Builder<Block> stateManager) {
        this.modules.forEach(module -> module.appendModelProperties(stateManager));
    }

    public MachineModuleContainer createModuleContainer(MachineBlockEntity machine) {
        return new MachineModuleContainer(machine, this.modules);
    }

    public static class Builder {
        private final Map<MachineModuleType<?, ?>, MachineModuleConfig> modules = new HashMap<>();

        public <C extends MachineModuleConfig> Builder configure(MachineModuleType<C, ?> moduleType, C config) {
            Preconditions.checkNotNull(moduleType, "moduleType");
            Preconditions.checkNotNull(config, "config");
            this.modules.put(moduleType, moduleType.castConfig(config));
            return this;
        }

        private static <C extends MachineModuleConfig> ConfiguredModule<C> makeConfiguredModule(MachineModuleType<C, ?> type, MachineModuleConfig config) {
            return ConfiguredModule.of(type, type.castConfig(config));
        }

        public MachineModuleCollection build() {
            ArrayList<ConfiguredModule<?>> resultModules = new ArrayList<>();

            for (MachineModuleType<?, ?> machineModuleType : this.modules.keySet()) {
                MachineModuleConfig config = this.modules.get(machineModuleType);
                ConfiguredModule<?> configuredModule = makeConfiguredModule(machineModuleType, config);
                resultModules.add(configuredModule);
            }
            return new MachineModuleCollection(resultModules);
        }
    }
}
