package gregtech.api.block.machine.module.impl.type;

import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.config.EnergyProducerConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class EnergyProducerModuleType<C extends EnergyProducerConfig, T extends MachineModule<C>> extends MachineModuleType<C, T> {

    public EnergyProducerModuleType(Settings<C, T> settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, List<Text> tooltip, TooltipContext options, C config) {
        super.appendTooltip(stack, tooltip, options, config);

        Text tierText = new TranslatableText(config.getTier().getTranslationKey());
        tooltip.add(new TranslatableText("gregtech.tooltip.energy_container.voltage_out", config.getTier().getVoltage(), tierText));
        tooltip.add(new TranslatableText("gregtech.tooltip.energy_container.amperage_out", config.getMaxOutputAmperage()));
        tooltip.add(new TranslatableText("gregtech.tooltip.energy_container.capacity", config.getCapacity()));
    }
}
