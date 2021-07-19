package gregtech.api.block;

import gregtech.api.GTValues;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineBlockEntityType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GTBlockEntityType {

    public static BlockEntityType<MachineBlockEntity> MACHINE;

    private static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(GTValues.MODID, name), blockEntityType);
    }

    static {
        MACHINE = register("machine", new MachineBlockEntityType<>(MachineBlockEntity::new));
    }
}
