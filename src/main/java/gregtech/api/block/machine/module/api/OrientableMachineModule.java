package gregtech.api.block.machine.module.api;

import alexiil.mc.lib.attributes.Simulation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

public interface OrientableMachineModule {

    OrientationKind getOrientationKind();

    Direction getOrientation();

    ActionResult attemptSetOrientation(Direction newOrientation, LivingEntity player, Simulation simulation);
}
