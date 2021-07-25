package gregtech.api.block.machine.module.impl.type;

import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.config.EnergyConsumerConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class EnergyConsumerModuleType<C extends EnergyConsumerConfig, T extends MachineModule<C>> extends MachineModuleType<C, T> {

    public EnergyConsumerModuleType(Settings<C, T> settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, List<Text> tooltip, TooltipContext options, C config) {
        super.appendTooltip(stack, tooltip, options, config);

        Text tierText = new TranslatableText(config.getTier().getTranslationKey());
        tooltip.add(new TranslatableText("gregtech.tooltip.energy_container.voltage_in", config.getTier().getVoltage(), tierText));
        tooltip.add(new TranslatableText("gregtech.tooltip.energy_container.amperage_in", config.getMaxInputAmperage()));
        tooltip.add(new TranslatableText("gregtech.tooltip.energy_container.capacity", config.getCapacity()));
    }
}
