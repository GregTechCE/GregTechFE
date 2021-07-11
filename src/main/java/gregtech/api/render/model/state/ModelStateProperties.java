package gregtech.api.render.model.state;

import gregtech.api.util.VoltageTier;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;

public class ModelStateProperties {

    public static final DirectionProperty ORIENTATION = DirectionProperty.of("orientation");
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public static final EnumProperty<VoltageTier> VOLTAGE_TIER = EnumProperty.of("voltage_tier", VoltageTier.class);

    public static final DirectionProperty OUTPUT_DIRECTION = DirectionProperty.of("output_direction");
    public static final BooleanProperty ITEM_AUTO_OUTPUT_ENABLED = BooleanProperty.of("item_auto_output_enabled");
    public static final BooleanProperty FLUID_AUTO_OUTPUT_ENABLED = BooleanProperty.of("fluid_auto_output_enabled");

    public static final DirectionProperty EXHAUST_DIRECTION = DirectionProperty.of("exhaust_direction");
}
