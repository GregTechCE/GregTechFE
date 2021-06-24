package gregtech.api.capability.internal;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.Attributes;
import gregtech.api.cover.Coverable;

public class GTInternalAttributes {

    public static final Attribute<Coverable> COVERABLE = Attributes.create(Coverable.class);

    public static final Attribute<Controllable> CONTROLLABLE = Attributes.create(Controllable.class);

    public static final Attribute<Workable> WORKABLE = Attributes.create(Workable.class);

    public static final Attribute<ActiveOutputSide> ACTIVE_OUTPUT_SIDE = Attributes.create(ActiveOutputSide.class);
}
