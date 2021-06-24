package gregtech.api.metatileentity.multiblock;

import gregtech.api.capability.EnergyContainer;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

public class MultiblockAbility<T> {

    public static final MultiblockAbility<IItemHandlerModifiable> EXPORT_ITEMS = new MultiblockAbility<>();
    public static final MultiblockAbility<IItemHandlerModifiable> IMPORT_ITEMS = new MultiblockAbility<>();

    public static final MultiblockAbility<IFluidTank> EXPORT_FLUIDS = new MultiblockAbility<>();
    public static final MultiblockAbility<IFluidTank> IMPORT_FLUIDS = new MultiblockAbility<>();

    public static final MultiblockAbility<EnergyContainer> INPUT_ENERGY = new MultiblockAbility<>();
    public static final MultiblockAbility<EnergyContainer> OUTPUT_ENERGY = new MultiblockAbility<>();

}
