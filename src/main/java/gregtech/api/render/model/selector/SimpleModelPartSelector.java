package gregtech.api.render.model.selector;

import com.google.common.base.Splitter;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;
import net.minecraft.state.property.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SimpleModelPartSelector implements ModelPartSelector {

    private static final Splitter VALUE_SPLITTER = Splitter.on('|')
            .trimResults()
            .omitEmptyStrings();

    private final String property;
    private final boolean negated;
    private final List<String> possibleValues;

    public SimpleModelPartSelector(String property, String value) {
        this.property = property;
        this.negated = !value.isEmpty() && value.charAt(0) == '!';
        this.possibleValues = VALUE_SPLITTER.splitToList(this.negated ? value.substring(1) : value);
    }

    @Override
    public Predicate<ModelState<?>> resolveSelector(ModelStateManager<?> stateManager) {
        Property<?> property = stateManager.getProperty(this.property);
        if (property == null) {
            throw new RuntimeException("Couldn't resolve ModelState property " + this.property + " on state " + stateManager);
        }

        List<Object> parsedValues = new ArrayList<>();

        for (String valueString : possibleValues) {
            Optional<?> parsedValue = property.parse(valueString);
            if (parsedValue.isEmpty()) {
                throw new RuntimeException("Failed to parse ModelState property " + this.property + " value " + valueString);
            }
            parsedValues.add(parsedValue.get());
        }

        if (parsedValues.isEmpty()) {
            throw new RuntimeException("No values were provided for ModelState property " + this.property);
        }

        Predicate<ModelState<?>> rawPredicate;
        if (parsedValues.size() == 1) {
            Object expectedPropertyValue = possibleValues.get(0);

            rawPredicate = (blockState) -> {
                Object currentPropertyValue = blockState.get(property);
                return expectedPropertyValue.equals(currentPropertyValue);
            };
        } else {
            rawPredicate = (blockState) -> {
                Object currentPropertyValue = blockState.get(property);

                for (Object possibleValue : parsedValues) {
                    if (possibleValue.equals(currentPropertyValue)) {
                        return true;
                    }
                }
                return false;
            };
        }
        return this.negated ? rawPredicate.negate() : rawPredicate;
    }
}
