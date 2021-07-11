package gregtech.api.render.model.function;

import gregtech.api.render.model.StateModelManager;
import gregtech.api.render.model.component.ModelComponent;
import gregtech.api.render.model.part.ModelPart;
import gregtech.api.render.model.part.ModelPartWrapper;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ApplyOrientationFunction implements ModelComponentFunction {

    private final String facingProperty;

    public ApplyOrientationFunction(String facingProperty) {
        this.facingProperty = facingProperty;
    }

    private static AffineTransformation getRotationForDirection(Direction direction) {
        return switch (direction) {
            case DOWN -> ModelRotation.X0_Y90.getRotation();
            case UP -> ModelRotation.X0_Y270.getRotation();
            case NORTH -> ModelRotation.X0_Y0.getRotation();
            case SOUTH -> ModelRotation.X180_Y0.getRotation();
            case WEST -> ModelRotation.X90_Y0.getRotation();
            case EAST -> ModelRotation.X270_Y0.getRotation();
        };
    }

    @Override
    public Iterable<ModelComponent> apply(ModelComponent component, ModelStateManager<?> stateManager, StateModelManager modelManager) {
        Property<?> property = stateManager.getProperty(this.facingProperty);
        if (property == null) {
            throw new RuntimeException("Failed to find facing property named " + facingProperty + " in " + stateManager);
        }
        if (!(property instanceof DirectionProperty directionProperty)) {
            throw new RuntimeException("Facing property named " + facingProperty + " in " + stateManager + " is not a direction property");
        }

        ArrayList<ModelComponent> resultComponents = new ArrayList<>();

        for (Direction direction : directionProperty.getValues()) {
            AffineTransformation rotation = getRotationForDirection(direction);
            ModelPart modelPart = new ModelPartWrapper(component.getModelPart(), rotation, true);

            Predicate<ModelState<?>> predicate = (modelState) -> {
                Direction resultDirection = modelState.get(directionProperty);
                return direction == resultDirection;
            };

            Predicate<ModelState<?>> resultPredicate = component.getPredicate().and(predicate);
            ModelComponent rotatedComponent = new ModelComponent(modelPart, resultPredicate, component.isStatic());
            resultComponents.add(rotatedComponent);
        }
        return resultComponents;
    }
}
