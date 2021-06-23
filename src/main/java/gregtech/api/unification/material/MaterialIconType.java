package gregtech.api.unification.material;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import gregtech.api.GTValues;
import net.minecraft.util.Identifier;

public enum MaterialIconType {

    //ITEM TEXTURES
    dustTiny,
    dustSmall,
    dust,
    dustImpure,
    dustPure,
    crushed,
    crushedPurified,
    crushedCentrifuged,
    gem,
    nugget,
    ingot,
    ingotHot,
    ingotDouble,
    ingotTriple,
    ingotQuadruple,
    ingotQuintuple,
    plate,
    plateDouble,
    plateTriple,
    plateQuadruple,
    plateQuintuple,
    plateDense,
    stick,
    lens,
    round,
    bolt,
    screw,
    ring,
    cell,
    cellPlasma,
    toolHeadSword,
    toolHeadPickaxe,
    toolHeadShovel,
    toolHeadAxe,
    toolHeadHoe,
    toolHeadHammer,
    toolHeadFile,
    toolHeadSaw,
    toolHeadBuzzSaw,
    toolHeadDrill,
    toolHeadChainsaw,
    toolHeadSense,
    toolHeadArrow,
    toolHeadScrewdriver,
    toolHeadBuzSaw,
    toolHeadSoldering,
    toolHeadWrench,
    toolHeadUniversalSpade,
    wireFine,
    gearSmall,
    rotor,
    stickLong,
    springSmall,
    spring,
    arrow,
    gemChipped,
    gemFlawed,
    gemFlawless,
    gemExquisite,
    gear,
    foil,
    crateGtDust,
    crateGtIngot,
    crateGtGem,
    crateGtPlate,
    turbineBlade,
    handleMallet,
    toolHeadMallet,

    //BLOCK TEXTURES
    block,
    foilBlock,
    wire,
    ore,
    frameGt,
    frameSide,
    frameTop,
    pipeSide,
    pipeTiny,
    pipeSmall,
    pipeMedium,
    pipeLarge,
    pipeHuge;

    public static final ImmutableMap<String, MaterialIconType> values;

    static {
        ImmutableMap.Builder<String, MaterialIconType> builder = ImmutableMap.builder();
        for (MaterialIconType value : values()) {
            builder.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value.name()), value);
        }
        values = builder.build();
    }

    public Identifier getBlockPath(MaterialIconSet materialIconSet) {
        String iconSet = materialIconSet.name().toLowerCase();
        String iconType = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name());
        return new Identifier(GTValues.MODID, "blocks/material_sets/" + iconSet + "/" + iconType);
    }

    public Identifier getItemModelPath(MaterialIconSet materialIconSet) {
        String iconSet = materialIconSet.name().toLowerCase();
        String iconType = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name());
        return new Identifier(GTValues.MODID, "material_sets/" + iconSet + "/" + iconType);
    }

    public Identifier getItemOverlayPath(MaterialIconSet materialIconSet) {
        String iconSet = materialIconSet.name().toLowerCase();
        String iconType = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name());
        return new Identifier(GTValues.MODID, "material_sets/" + iconSet + "/" + iconType + "_overlay");
    }

}
