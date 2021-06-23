package gregtech.api.cover;

import gregtech.api.GTValues;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public final class CoverDefinition {

    public static final Registry<CoverDefinition> REGISTRY =
            FabricRegistryBuilder.createSimple(CoverDefinition.class, new Identifier(GTValues.MODID, "cover_definition"))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static void register(Identifier identifier, CoverDefinition coverDefinition) {
        Registry.register(REGISTRY, identifier, coverDefinition);
    }

    private final CoverBehaviorCreator behaviorCreator;
    private final ItemStack dropItemStack;

    public CoverDefinition(CoverBehaviorCreator behaviorCreator, ItemStack dropItemStack) {
        this.behaviorCreator = behaviorCreator;
        this.dropItemStack = dropItemStack.copy();
    }

    public ItemStack getDropItemStack() {
        return dropItemStack.copy();
    }

    public CoverBehavior createCoverBehavior(Coverable metaTileEntity, Direction side) {
        CoverBehavior coverBehavior = behaviorCreator.create(metaTileEntity, side);
        coverBehavior.setCoverDefinition(this);
        return coverBehavior;
    }

    @FunctionalInterface
    public interface CoverBehaviorCreator {
        CoverBehavior create(Coverable host, Direction side);
    }
}
