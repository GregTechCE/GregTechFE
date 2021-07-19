package gregtech.api.block.machine.module.api;

import alexiil.mc.lib.attributes.Simulation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface OrientableMachineModule {

    boolean supportsOrientation(Direction orientation);

    boolean attemptSetOrientation(Direction newOrientation, @Nullable LivingEntity player, Simulation simulation);

    default void onOrientationSet(Direction newOrientation) {
    }
}
