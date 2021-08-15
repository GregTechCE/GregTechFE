package gregtech.common.items;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import gregtech.api.GTValues;
import gregtech.api.items.GTItem;
import gregtech.api.items.GTItemSettings;
import gregtech.api.items.stats.ElectricStats;
import gregtech.api.items.stats.FluidStats;
import gregtech.api.util.VoltageTier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class GTItems {

    public static final Item DYNAMITE;

    public static final Item CREDIT_COPPER;
    public static final Item CREDIT_CUPRONICKEL;
    public static final Item CREDIT_SILVER;
    public static final Item CREDIT_GOLD;
    public static final Item CREDIT_PLATINUM;
    public static final Item CREDIT_OSMIUM;
    public static final Item CREDIT_NAQUADAH;
    public static final Item CREDIT_DARMSTADTIUM;

    public static final Item COIN_GOLD_ANCIENT;
    public static final Item COIN_DOGE;
    public static final Item COIN_CHOCOLATE;

    public static final Item COMPRESSED_CLAY;
    public static final Item COMPRESSED_FIRECLAY;
    public static final Item FIRECLAY_BRICK;
    public static final Item COKE_OVEN_BRICK;

    public static final Item WOODEN_FORM_EMPTY;
    public static final Item WOODEN_FORM_BRICK;

    public static final Item SHAPE_EMPTY;

    public static final Item[] SHAPE_MOLDS = new Item[13];
    public static final Item SHAPE_MOLD_PLATE;
    public static final Item SHAPE_MOLD_GEAR;
    public static final Item SHAPE_MOLD_CREDIT;
    public static final Item SHAPE_MOLD_BOTTLE;
    public static final Item SHAPE_MOLD_INGOT;
    public static final Item SHAPE_MOLD_BALL;
    public static final Item SHAPE_MOLD_BLOCK;
    public static final Item SHAPE_MOLD_NUGGET;
    public static final Item SHAPE_MOLD_CYLINDER;
    public static final Item SHAPE_MOLD_ANVIL;
    public static final Item SHAPE_MOLD_NAME;
    public static final Item SHAPE_MOLD_GEAR_SMALL;
    public static final Item SHAPE_MOLD_ROTOR;

    public static final Item[] SHAPE_EXTRUDERS = new Item[22];
    public static final Item SHAPE_EXTRUDER_PLATE;
    public static final Item SHAPE_EXTRUDER_ROD;
    public static final Item SHAPE_EXTRUDER_BOLT;
    public static final Item SHAPE_EXTRUDER_RING;
    public static final Item SHAPE_EXTRUDER_CELL;
    public static final Item SHAPE_EXTRUDER_INGOT;
    public static final Item SHAPE_EXTRUDER_WIRE;
    public static final Item SHAPE_EXTRUDER_PIPE_TINY;
    public static final Item SHAPE_EXTRUDER_PIPE_SMALL;
    public static final Item SHAPE_EXTRUDER_PIPE_MEDIUM;
    public static final Item SHAPE_EXTRUDER_PIPE_LARGE;
    public static final Item SHAPE_EXTRUDER_BLOCK;
    public static final Item SHAPE_EXTRUDER_SWORD;
    public static final Item SHAPE_EXTRUDER_PICKAXE;
    public static final Item SHAPE_EXTRUDER_SHOVEL;
    public static final Item SHAPE_EXTRUDER_AXE;
    public static final Item SHAPE_EXTRUDER_HOE;
    public static final Item SHAPE_EXTRUDER_HAMMER;
    public static final Item SHAPE_EXTRUDER_FILE;
    public static final Item SHAPE_EXTRUDER_SAW;
    public static final Item SHAPE_EXTRUDER_GEAR;
    public static final Item SHAPE_EXTRUDER_BOTTLE;

    public static final Item SPRAY_EMPTY;

    public static final Item LARGE_FLUID_CELL_STEEL;
    public static final Item LARGE_FLUID_CELL_TUNGSTEN_STEEL;

    public static final Item TOOL_MATCHES;
    public static final Item TOOL_MATCHBOX;
    public static final Item TOOL_LIGHTER_INVAR;
    public static final Item TOOL_LIGHTER_PLATINUM;

    public static final Item INGOT_MIXED_METAL;
    public static final Item ADVANCED_ALLOY_PLATE;

    public static final Item INGOT_IRIDIUM_ALLOY;
    public static final Item PLATE_IRIDIUM_ALLOY;

    public static final Item CARBON_FIBERS;
    public static final Item CARBON_MESH;
    public static final Item CARBON_PLATE;
    public static final Item GLASS_FIBER;

    public static final Item NEUTRON_REFLECTOR;

    public static final Item BATTERY_HULL_LV;
    public static final Item BATTERY_HULL_MV;
    public static final Item BATTERY_HULL_HV;

    public static final Item BATTERY_ULV_TANTALUM;
    public static final Item BATTERY_LV_CADMIUM;
    public static final Item BATTERY_LV_LITHIUM;
    public static final Item BATTERY_LV_SODIUM;
    public static final Item BATTERY_MV_CADMIUM;
    public static final Item BATTERY_MV_LITHIUM;
    public static final Item BATTERY_MV_SODIUM;
    public static final Item BATTERY_HV_CADMIUM;
    public static final Item BATTERY_HV_LITHIUM;
    public static final Item BATTERY_HV_SODIUM;
    public static final Item ENERGY_CRYSTAL;
    public static final Item LAPOTRON_CRYSTAL;

    public static final Item ENERGY_LAPOTRONIC_ORB;
    public static final Item ENERGY_LAPOTRONIC_ORB2;
    public static final Item ZPM;
    public static final Item ZPM2;

    public static final Item ELECTRIC_MOTOR_LV;
    public static final Item ELECTRIC_MOTOR_MV;
    public static final Item ELECTRIC_MOTOR_HV;
    public static final Item ELECTRIC_MOTOR_EV;
    public static final Item ELECTRIC_MOTOR_IV;
    public static final Item ELECTRIC_MOTOR_LUV;
    public static final Item ELECTRIC_MOTOR_ZPM;
    public static final Item ELECTRIC_MOTOR_UV;

    public static final Item ELECTRIC_PUMP_LV;
    public static final Item ELECTRIC_PUMP_MV;
    public static final Item ELECTRIC_PUMP_HV;
    public static final Item ELECTRIC_PUMP_EV;
    public static final Item ELECTRIC_PUMP_IV;
    public static final Item ELECTRIC_PUMP_LUV;
    public static final Item ELECTRIC_PUMP_ZPM;
    public static final Item ELECTRIC_PUMP_UV;

    public static final Item FLUID_REGULATOR_LV;
    public static final Item FLUID_REGULATOR_MV;
    public static final Item FLUID_REGULATOR_HV;
    public static final Item FLUID_REGULATOR_EV;
    public static final Item FLUID_REGULATOR_IV;
    public static final Item FLUID_REGULATOR_LUV;
    public static final Item FLUID_REGULATOR_ZPM;
    public static final Item FLUID_REGULATOR_UV;

    public static final Item FLUID_FILTER;

    public static final Item CONVEYOR_MODULE_LV;
    public static final Item CONVEYOR_MODULE_MV;
    public static final Item CONVEYOR_MODULE_HV;
    public static final Item CONVEYOR_MODULE_EV;
    public static final Item CONVEYOR_MODULE_IV;
    public static final Item CONVEYOR_MODULE_LUV;
    public static final Item CONVEYOR_MODULE_ZPM;
    public static final Item CONVEYOR_MODULE_UV;

    public static final Item ELECTRIC_PISTON_LV;
    public static final Item ELECTRIC_PISTON_MV;
    public static final Item ELECTRIC_PISTON_HV;
    public static final Item ELECTRIC_PISTON_EV;
    public static final Item ELECTRIC_PISTON_IV;
    public static final Item ELECTRIC_PISTON_LUV;
    public static final Item ELECTRIC_PISTON_ZPM;
    public static final Item ELECTRIC_PISTON_UV;

    public static final Item ROBOT_ARM_LV;
    public static final Item ROBOT_ARM_MV;
    public static final Item ROBOT_ARM_HV;
    public static final Item ROBOT_ARM_EV;
    public static final Item ROBOT_ARM_IV;
    public static final Item ROBOT_ARM_LUV;
    public static final Item ROBOT_ARM_ZPM;
    public static final Item ROBOT_ARM_UV;

    public static final Item FIELD_GENERATOR_LV;
    public static final Item FIELD_GENERATOR_MV;
    public static final Item FIELD_GENERATOR_HV;
    public static final Item FIELD_GENERATOR_EV;
    public static final Item FIELD_GENERATOR_IV;
    public static final Item FIELD_GENERATOR_LUV;
    public static final Item FIELD_GENERATOR_ZPM;
    public static final Item FIELD_GENERATOR_UV;

    public static final Item EMITTER_LV;
    public static final Item EMITTER_MV;
    public static final Item EMITTER_HV;
    public static final Item EMITTER_EV;
    public static final Item EMITTER_IV;
    public static final Item EMITTER_LUV;
    public static final Item EMITTER_ZPM;
    public static final Item EMITTER_UV;

    public static final Item SENSOR_LV;
    public static final Item SENSOR_MV;
    public static final Item SENSOR_HV;
    public static final Item SENSOR_EV;
    public static final Item SENSOR_IV;
    public static final Item SENSOR_LUV;
    public static final Item SENSOR_ZPM;
    public static final Item SENSOR_UV;

    public static final Item TOOL_DATA_STICK;
    public static final Item TOOL_DATA_ORB;

    public static final Item GLOWSTONE_BOULE;
    public static final Item NAQUADAH_BOULE;
    public static final Item SILICON_BOULE;
    public static final Item SILICON_WAFER;
    public static final Item GLOWSTONE_WAFER;
    public static final Item NAQUADAH_WAFER;

    public static final Item ADVANCED_SYSTEM_ON_CHIP_WAFER;
    public static final Item INTEGRATED_LOGIC_CIRCUIT_WAFER;
    public static final Item CENTRAL_PROCESSING_UNIT_WAFER;
    public static final Item HIGH_POWER_INTEGRATED_CIRCUIT_WAFER;
    public static final Item NAND_MEMORY_CHIP_WAFER;
    public static final Item NANO_CENTRAL_PROCESSING_UNIT_WAFER;
    public static final Item NOR_MEMORY_CHIP_WAFER;
    public static final Item POWER_INTEGRATED_CIRCUIT_WAFER;
    public static final Item QBIT_CENTRAL_PROCESSING_UNIT_WAFER;
    public static final Item RANDOM_ACCESS_MEMORY_WAFER;
    public static final Item SYSTEM_ON_CHIP_WAFER;
    public static final Item ENGRAVED_CRYSTAL_CHIP;
    public static final Item ENGRAVED_LAPOTRON_CHIP;

    public static final Item ADVANCED_SYSTEM_ON_CHIP;
    public static final Item INTEGRATED_LOGIC_CIRCUIT;
    public static final Item CENTRAL_PROCESSING_UNIT;
    public static final Item HIGH_POWER_INTEGRATED_CIRCUIT;
    public static final Item NAND_MEMORY_CHIP;
    public static final Item NANO_CENTRAL_PROCESSING_UNIT;
    public static final Item NOR_MEMORY_CHIP;
    public static final Item POWER_INTEGRATED_CIRCUIT;
    public static final Item QBIT_CENTRAL_PROCESSING_UNIT;
    public static final Item RANDOM_ACCESS_MEMORY;
    public static final Item SYSTEM_ON_CHIP;
    public static final Item CRYSTAL_CENTRAL_PROCESSING_UNIT;
    public static final Item CRYSTAL_SYSTEM_ON_CHIP;

    public static final Item COATED_BOARD;
    public static final Item EPOXY_BOARD;
    public static final Item FIBER_BOARD;
    public static final Item MULTILAYER_FIBER_BOARD;
    public static final Item PHENOLIC_BOARD;
    public static final Item PLASTIC_BOARD;
    public static final Item WETWARE_BOARD;

    public static final Item VACUUM_TUBE;
    public static final Item GLASS_TUBE;
    public static final Item RESISTOR;
    public static final Item DIODE;
    public static final Item CAPACITOR;
    public static final Item SMALL_COIL;
    public static final Item SMD_CAPACITOR;
    public static final Item SMD_DIODE;
    public static final Item SMD_RESISTOR;
    public static final Item SMD_TRANSISTOR;
    public static final Item TRANSISTOR;

    public static final Item BASIC_CIRCUIT_LV;
    public static final Item BASIC_ELECTRONIC_CIRCUIT_LV;
    public static final Item ADVANCED_CIRCUIT_PARTS_LV;
    public static final Item GOOD_INTEGRATED_CIRCUIT_MV;
    public static final Item ADVANCED_CIRCUIT_MV;
    public static final Item PROCESSOR_ASSEMBLY_HV;
    public static final Item NANO_PROCESSOR_HV;
    public static final Item NANO_PROCESSOR_ASSEMBLY_EV;
    public static final Item QUANTUM_PROCESSOR_EV;
    public static final Item DATA_CONTROL_CIRCUIT_IV;
    public static final Item CRYSTAL_PROCESSOR_IV;
    public static final Item ENERGY_FLOW_CIRCUIT_LUV;
    public static final Item WETWARE_PROCESSOR_LUV;
    public static final Item WETWARE_PROCESSOR_ASSEMBLY_ZPM;
    public static final Item WETWARE_SUPER_COMPUTER_UV;
    public static final Item WETWARE_MAINFRAME_MAX;

    public static final Item COMPONENT_SAW_BLADE_DIAMOND;
    public static final Item COMPONENT_GRINDER_DIAMOND;
    public static final Item COMPONENT_GRINDER_TUNGSTEN;

    public static final Item QUANTUM_EYE;
    public static final Item QUANTUM_STAR;
    public static final Item GRAVI_STAR;

    public static final Item ITEM_FILTER;
    public static final Item ORE_DICTIONARY_FILTER;
    public static final Item SMART_FILTER;

    public static final Item COVER_SHUTTER;
    public static final Item COVER_MACHINE_CONTROLLER;
    public static final Item COVER_FACADE;

    //public static final Item COVER_ACTIVITY_DETECTOR;
    //public static final Item COVER_FLUID_DETECTOR;
    //public static final Item COVER_ITEM_DETECTOR;
    //public static final Item COVER_ENERGY_DETECTOR;

    //public static final Item COVER_SCREEN;
    //public static final Item COVER_CRAFTING;
    //public static final Item COVER_DRAIN;

    public static final Item COVER_SOLAR_PANEL;
    public static final Item COVER_SOLAR_PANEL_ULV;
    public static final Item COVER_SOLAR_PANEL_LV;

    public static final Item INTEGRATED_CIRCUIT;

    public static final Item FLUID_CELL;

    public static final Item FOAM_SPRAYER;

    public static final Item GELLED_TOLUENE;

    public static final Item BOTTLE_PURPLE_DRINK;

    public static final Item DYE_INDIGO;
    public static final Item PLANT_BALL;
    public static final Item RUBBER_DROP;
    public static final Item ENERGIUM_DUST;

    public static final Item POWER_UNIT_LV;
    public static final Item POWER_UNIT_MV;
    public static final Item POWER_UNIT_HV;
    public static final Item JACKHAMMER_BASE;

    public static final Item NANO_SABER;
    public static final Item ENERGY_FIELD_PROJECTOR;
    public static final Item SCANNER;

    //public static final Item[] DYE_ONLY_ITEMS = new MetaItem.MetaValueItem[EnumDyeColor.values().length];
    //public static final Item[] SPRAY_CAN_DYES = new MetaItem.MetaValueItem[EnumDyeColor.values().length];

    public static final Item TURBINE_ROTOR;
/*
    public static ArmorMetaItem.ArmorMetaValueItem REBREATHER;

    public static ToolMetaItem<?>.MetaToolValueItem SWORD;
    public static ToolMetaItem<?>.MetaToolValueItem PICKAXE;
    public static ToolMetaItem<?>.MetaToolValueItem SHOVEL;
    public static ToolMetaItem<?>.MetaToolValueItem AXE;
    public static ToolMetaItem<?>.MetaToolValueItem HOE;
    public static ToolMetaItem<?>.MetaToolValueItem SAW;
    public static ToolMetaItem<?>.MetaToolValueItem HARD_HAMMER;
    public static ToolMetaItem<?>.MetaToolValueItem SOFT_HAMMER;
    public static ToolMetaItem<?>.MetaToolValueItem WRENCH;
    public static ToolMetaItem<?>.MetaToolValueItem FILE;
    public static ToolMetaItem<?>.MetaToolValueItem CROWBAR;
    public static ToolMetaItem<?>.MetaToolValueItem SCREWDRIVER;
    public static ToolMetaItem<?>.MetaToolValueItem MORTAR;
    public static ToolMetaItem<?>.MetaToolValueItem WIRE_CUTTER;
    public static ToolMetaItem<?>.MetaToolValueItem SCOOP;
    public static ToolMetaItem<?>.MetaToolValueItem BRANCH_CUTTER;
    public static ToolMetaItem<?>.MetaToolValueItem UNIVERSAL_SPADE;
    public static ToolMetaItem<?>.MetaToolValueItem KNIFE;
    public static ToolMetaItem<?>.MetaToolValueItem BUTCHERY_KNIFE;
    public static ToolMetaItem<?>.MetaToolValueItem SENSE;
    public static ToolMetaItem<?>.MetaToolValueItem PLUNGER;
    public static ToolMetaItem<?>.MetaToolValueItem DRILL_LV;
    public static ToolMetaItem<?>.MetaToolValueItem DRILL_MV;
    public static ToolMetaItem<?>.MetaToolValueItem DRILL_HV;
    public static ToolMetaItem<?>.MetaToolValueItem CHAINSAW_LV;
    public static ToolMetaItem<?>.MetaToolValueItem CHAINSAW_MV;
    public static ToolMetaItem<?>.MetaToolValueItem CHAINSAW_HV;
    public static ToolMetaItem<?>.MetaToolValueItem WRENCH_LV;
    public static ToolMetaItem<?>.MetaToolValueItem WRENCH_MV;
    public static ToolMetaItem<?>.MetaToolValueItem WRENCH_HV;
    public static ToolMetaItem<?>.MetaToolValueItem JACKHAMMER;
    public static ToolMetaItem<?>.MetaToolValueItem BUZZSAW;
    public static ToolMetaItem<?>.MetaToolValueItem SCREWDRIVER_LV;
*/
    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(GTValues.MODID, name), item);
    }

    private static Item register(String name) {
        return register(name, new Item(new Item.Settings()));
    }

    public static Item getIntegratedCircuit(int configuration) {

    }

    static {
        CREDIT_COPPER = register("credit_copper");
        CREDIT_CUPRONICKEL = register("credit_cupronickel");
        CREDIT_SILVER = register("credit_silver", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
        CREDIT_GOLD = register("credit_gold", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
        CREDIT_PLATINUM = register("credit_platinum", new Item(new Item.Settings().rarity(Rarity.RARE)));
        CREDIT_OSMIUM = register("credit_osmium", new Item(new Item.Settings().rarity(Rarity.RARE)));
        CREDIT_NAQUADAH = register("credit_naquadah", new Item(new Item.Settings().rarity(Rarity.EPIC)));
        CREDIT_DARMSTADTIUM = register("credit_darmstadtium", new Item(new Item.Settings().rarity(Rarity.EPIC)));

        COIN_GOLD_ANCIENT = register("coin_gold_ancient", new Item(new Item.Settings().rarity(Rarity.RARE)));
        COIN_DOGE = register("coin_doge", new Item(new Item.Settings().rarity(Rarity.EPIC)));
        COIN_CHOCOLATE = register("coin_chocolate", new Item(new Item.Settings().food(new FoodComponent.Builder()
                .hunger(1).saturationModifier(0.1f).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 1), 10).build())));

        SHAPE_EMPTY = register("shape_empty");

        SHAPE_MOLDS[0] = SHAPE_MOLD_PLATE = register("shape_mold_plate");
        SHAPE_MOLDS[1] = SHAPE_MOLD_GEAR = register("shape_mold_gear");
        SHAPE_MOLDS[2] = SHAPE_MOLD_CREDIT = register("shape_mold_credit");
        SHAPE_MOLDS[3] = SHAPE_MOLD_BOTTLE = register("shape_mold_bottle");
        SHAPE_MOLDS[4] = SHAPE_MOLD_INGOT = register("shape_mold_ingot");
        SHAPE_MOLDS[5] = SHAPE_MOLD_BALL = register("shape_mold_ball");
        SHAPE_MOLDS[6] = SHAPE_MOLD_BLOCK = register("shape_mold_block");
        SHAPE_MOLDS[7] = SHAPE_MOLD_NUGGET = register("shape_mold_nugget");
        SHAPE_MOLDS[8] = SHAPE_MOLD_CYLINDER = register("shape_mold_cylinder");
        SHAPE_MOLDS[9] = SHAPE_MOLD_ANVIL = register("shape_mold_anvil");
        SHAPE_MOLDS[10] = SHAPE_MOLD_NAME = register("shape_mold_name");
        SHAPE_MOLDS[11] = SHAPE_MOLD_GEAR_SMALL = register("shape_mold_gear_small");
        SHAPE_MOLDS[12] = SHAPE_MOLD_ROTOR = register("shape_mold_rotor");

        SHAPE_EXTRUDERS[0] = SHAPE_EXTRUDER_PLATE = register("shape_extruder_plate");
        SHAPE_EXTRUDERS[1] = SHAPE_EXTRUDER_ROD = register("shape_extruder_rod");
        SHAPE_EXTRUDERS[2] = SHAPE_EXTRUDER_BOLT = register("shape_extruder_bolt");
        SHAPE_EXTRUDERS[3] = SHAPE_EXTRUDER_RING = register("shape_extruder_ring");
        SHAPE_EXTRUDERS[4] = SHAPE_EXTRUDER_CELL = register("shape_extruder_cell");
        SHAPE_EXTRUDERS[5] = SHAPE_EXTRUDER_INGOT = register("shape_extruder_ingot");
        SHAPE_EXTRUDERS[6] = SHAPE_EXTRUDER_WIRE = register("shape_extruder_wire");
        SHAPE_EXTRUDERS[7] = SHAPE_EXTRUDER_PIPE_TINY = register("shape_extruder_pipe_tiny");
        SHAPE_EXTRUDERS[8] = SHAPE_EXTRUDER_PIPE_SMALL = register("shape_extruder_pipe_small");
        SHAPE_EXTRUDERS[9] = SHAPE_EXTRUDER_PIPE_MEDIUM = register("shape_extruder_pipe_medium");
        SHAPE_EXTRUDERS[10] = SHAPE_EXTRUDER_PIPE_LARGE = register("shape_extruder_pipe_large");
        SHAPE_EXTRUDERS[11] = SHAPE_EXTRUDER_BLOCK = register("shape_extruder_block");
        SHAPE_EXTRUDERS[12] = SHAPE_EXTRUDER_SWORD = register("shape_extruder_sword");
        SHAPE_EXTRUDERS[13] = SHAPE_EXTRUDER_PICKAXE = register("shape_extruder_pickaxe");
        SHAPE_EXTRUDERS[14] = SHAPE_EXTRUDER_SHOVEL = register("shape_extruder_shovel");
        SHAPE_EXTRUDERS[15] = SHAPE_EXTRUDER_AXE = register("shape_extruder_axe");
        SHAPE_EXTRUDERS[16] = SHAPE_EXTRUDER_HOE = register("shape_extruder_hoe");
        SHAPE_EXTRUDERS[17] = SHAPE_EXTRUDER_HAMMER = register("shape_extruder_hammer");
        SHAPE_EXTRUDERS[18] = SHAPE_EXTRUDER_FILE = register("shape_extruder_file");
        SHAPE_EXTRUDERS[19] = SHAPE_EXTRUDER_SAW = register("shape_extruder_saw");
        SHAPE_EXTRUDERS[20] = SHAPE_EXTRUDER_GEAR = register("shape_extruder_gear");
        SHAPE_EXTRUDERS[21] = SHAPE_EXTRUDER_BOTTLE = register("shape_extruder_bottle");

        SPRAY_EMPTY = register("spray_empty");

        LARGE_FLUID_CELL_STEEL = register("large_fluid_cell_steel", new GTItem(new GTItemSettings().fluidStats(new FluidStats(FluidAmount.ofWhole(64), true)).setMaxStackSize(16)));
        LARGE_FLUID_CELL_TUNGSTEN_STEEL = register("large_fluid_cell_tungstensteel", new GTItem(new GTItemSettings().fluidStats(new FluidStats(FluidAmount.ofWhole(256), true)).setMaxStackSize(16)));

        /*
        for (int i = 0; i < EnumDyeColor.values().length; i++) {
            EnumDyeColor dyeColor = EnumDyeColor.values()[i];
            SPRAY_CAN_DYES[i] = addItem(430 + 2 * i, "spray_can_dyes_" + dyeColor.getName()).setMaxStackSize(1);
            ColorSprayBehaviour behaviour = new ColorSprayBehaviour(SPRAY_EMPTY.getStackForm(), 512, i);
            SPRAY_CAN_DYES[i].addComponents(behaviour);
        }
*/
        // todo .addComponents(new LighterBehaviour(1));
        TOOL_MATCHES = register("tool_matches");
        // todo .addComponents(new LighterBehaviour(16))
        TOOL_MATCHBOX = register("tool_matchbox", new Item(new Item.Settings().maxCount(1)));
        // todo .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Invar, GTValues.L * 2)))
        TOOL_LIGHTER_INVAR = register("tool_lighter_invar", new Item(new Item.Settings().maxCount(1)));
        // todo .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Platinum, GTValues.L * 2))).addComponents(new LighterBehaviour(1000));
        TOOL_LIGHTER_PLATINUM = register("tool_lighter_platinum", new Item(new Item.Settings().maxCount(1)));


        BATTERY_ULV_TANTALUM = register("battery_ulv_tantalum", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1000, VoltageTier.ULV))));

        // todo .setModelAmount(8)
        BATTERY_LV_CADMIUM = register("battery_lv_cadmium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(120000L, VoltageTier.LV))));
        BATTERY_LV_LITHIUM = register("battery_lv_lithium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(100000L, VoltageTier.LV))));
        BATTERY_LV_SODIUM = register("battery_lv_sodium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(80000L, VoltageTier.LV))));

        // todo .setModelAmount(8)
        BATTERY_MV_CADMIUM = register("battery_mv_cadmium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(420000L, VoltageTier.MV))));
        BATTERY_MV_LITHIUM = register("battery_mv_lithium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(400000L, VoltageTier.MV))));
        BATTERY_MV_SODIUM = register("battery_mv_sodium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(360000L, VoltageTier.MV))));

        // todo .setModelAmount(8)
        BATTERY_HV_CADMIUM = register("battery_hv_cadmium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1800000L, VoltageTier.HV))));
        BATTERY_HV_LITHIUM = register("battery_hv_lithium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1600000L, VoltageTier.HV))));
        BATTERY_HV_SODIUM = register("battery_hv_sodium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1200000L, VoltageTier.HV))));

        // TODO .setModelAmount(8)
        ENERGY_CRYSTAL = register("energy_crystal", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(4000000L, VoltageTier.HV)).maxCount(1)));
        LAPOTRON_CRYSTAL = register("lapotron_crystal", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(10000000L, VoltageTier.EV)).maxCount(1)));

        // todo .setModelAmount(8), .setUnificationData(OrePrefix.battery, MarkerMaterials.Tier.Ultimate)
        ENERGY_LAPOTRONIC_ORB = register("energy_lapotronicorb", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(100000000L, VoltageTier.IV))));
        ENERGY_LAPOTRONIC_ORB2 = register("energy_lapotronicorb2", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1000000000L, VoltageTier.LuV))));

        // todo .setModelAmount(8)
        ZPM = register("zpm", new GTItem(new GTItemSettings().electricStats(ElectricStats.createBattery(2000000000000L, VoltageTier.ZPM, false))));
        ZPM2 = register("zpm2", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, VoltageTier.UV))));

        BATTERY_HULL_LV = register("battery_hull_lv");
        BATTERY_HULL_MV = register("battery_hull_mv");
        BATTERY_HULL_HV = register("battery_hull_hv");

        ELECTRIC_MOTOR_LV = register("electric_motor_lv");
        ELECTRIC_MOTOR_MV = register("electric_motor_mv");
        ELECTRIC_MOTOR_HV = register("electric_motor_hv");
        ELECTRIC_MOTOR_EV = register("electric_motor_ev");
        ELECTRIC_MOTOR_IV = register("electric_motor_iv");
        ELECTRIC_MOTOR_LUV = register("electric_motor_luv");
        ELECTRIC_MOTOR_ZPM = register("electric_motor_zpm");
        ELECTRIC_MOTOR_UV = register("electric_motor_uv");

        ELECTRIC_PUMP_LV = register("electric_pump_lv");
        ELECTRIC_PUMP_MV = register("electric_pump_mv");
        ELECTRIC_PUMP_HV = register("electric_pump_hv");
        ELECTRIC_PUMP_EV = register("electric_pump_ev");
        ELECTRIC_PUMP_IV = register("electric_pump_iv");
        ELECTRIC_PUMP_LUV = register("electric_pump_luv");
        ELECTRIC_PUMP_ZPM = register("electric_pump_zpm");
        ELECTRIC_PUMP_UV = register("electric_pump_uv");

        RUBBER_DROP = register("rubber_drop"); // todo .setBurnValue(200);

        FLUID_FILTER = register("fluid_filter");

        DYNAMITE = register("dynamite", new Item(new Item.Settings().maxCount(16))); // todo .addComponents(new DynamiteBehaviour())

        CONVEYOR_MODULE_LV = register("conveyor_module_lv");
        CONVEYOR_MODULE_MV = register("conveyor_module_mv");
        CONVEYOR_MODULE_HV = register("conveyor_module_hv");
        CONVEYOR_MODULE_EV = register("conveyor_module_ev");
        CONVEYOR_MODULE_IV = register("conveyor_module_iv");
        CONVEYOR_MODULE_LUV = register("conveyor_module_luv");
        CONVEYOR_MODULE_ZPM = register("conveyor_module_zpm");
        CONVEYOR_MODULE_UV = register("conveyor_module_uv");

        ELECTRIC_PISTON_LV = register("electric_piston_lv");
        ELECTRIC_PISTON_MV = register("electric_piston_mv");
        ELECTRIC_PISTON_HV = register("electric_piston_hv");
        ELECTRIC_PISTON_EV = register("electric_piston_ev");
        ELECTRIC_PISTON_IV = register("electric_piston_iv");
        ELECTRIC_PISTON_LUV = register("electric_piston_luv");
        ELECTRIC_PISTON_ZPM = register("electric_piston_zpm");
        ELECTRIC_PISTON_UV = register("electric_piston_uv");

        ROBOT_ARM_LV = register("robot_arm_lv");
        ROBOT_ARM_MV = register("robot_arm_mv");
        ROBOT_ARM_HV = register("robot_arm_hv");
        ROBOT_ARM_EV = register("robot_arm_ev");
        ROBOT_ARM_IV = register("robot_arm_iv");
        ROBOT_ARM_LUV = register("robot_arm_luv");
        ROBOT_ARM_ZPM = register("robot_arm_zpm");
        ROBOT_ARM_UV = register("robot_arm_uv");

        FIELD_GENERATOR_LV = register("field_generator_lv");
        FIELD_GENERATOR_MV = register("field_generator_mv");
        FIELD_GENERATOR_HV = register("field_generator_hv");
        FIELD_GENERATOR_EV = register("field_generator_ev");
        FIELD_GENERATOR_IV = register("field_generator_iv");
        FIELD_GENERATOR_LUV = register("field_generator_luv");
        FIELD_GENERATOR_ZPM = register("field_generator_zpm");
        FIELD_GENERATOR_UV = register("field_generator_uv");

        EMITTER_LV = register("emitter_lv");
        EMITTER_MV = register("emitter_mv");
        EMITTER_HV = register("emitter_hv");
        EMITTER_EV = register("emitter_ev");
        EMITTER_IV = register("emitter_iv");
        EMITTER_LUV = register("emitter_luv");
        EMITTER_ZPM = register("emitter_zpm");
        EMITTER_UV = register("emitter_uv");

        SENSOR_LV = register("sensor_lv");
        SENSOR_MV = register("sensor_mv");
        SENSOR_HV = register("sensor_hv");
        SENSOR_EV = register("sensor_ev");
        SENSOR_IV = register("sensor_iv");
        SENSOR_LUV = register("sensor_luv");
        SENSOR_ZPM = register("sensor_zpm");
        SENSOR_UV = register("sensor_uv");

        FLUID_REGULATOR_LV = register("fluid_regulator_lv");
        FLUID_REGULATOR_MV = register("fluid_regulator_mv");
        FLUID_REGULATOR_HV = register("fluid_regulator_hv");
        FLUID_REGULATOR_EV = register("fluid_regulator_ev");
        FLUID_REGULATOR_IV = register("fluid_regulator_iv");
        FLUID_REGULATOR_LUV = register("fluid_regulator_luv");
        FLUID_REGULATOR_ZPM = register("fluid_regulator_zpm");
        FLUID_REGULATOR_UV = register("fluid_regulator_uv");

        TOOL_DATA_STICK = register("tool_datastick");
        TOOL_DATA_ORB = register("tool_dataorb");

        COMPONENT_SAW_BLADE_DIAMOND = register("component_sawblade_diamond"); // todo .addOreDict(OreDictNames.craftingDiamondBlade);
        COMPONENT_GRINDER_DIAMOND = register("component_grinder_diamond"); // todo .addOreDict(OreDictNames.craftingGrinder);
        COMPONENT_GRINDER_TUNGSTEN = register("component_grinder_tungsten"); // todo .addOreDict(OreDictNames.craftingGrinder);

        QUANTUM_EYE = register("quantumeye");
        QUANTUM_STAR = register("quantumstar");
        GRAVI_STAR = register("gravistar");

        ITEM_FILTER = register("cover_item_filter");
        ORE_DICTIONARY_FILTER = register("cover_ore_dictionary_filter");
        SMART_FILTER = register("cover_smart_item_filter");

        COVER_MACHINE_CONTROLLER = register("cover_controller");

        //COVER_ACTIVITY_DETECTOR = addItem(731, "cover_activity_detector").setInvisible();
        //COVER_FLUID_DETECTOR = addItem(732, "cover_fluid_detector").setInvisible();
        //COVER_ITEM_DETECTOR = addItem(733, "cover_item_detector").setInvisible();
        //COVER_ENERGY_DETECTOR = addItem(734, "cover_energy_detector").setInvisible();

        //COVER_SCREEN = addItem(740, "cover_screen").setInvisible();
        //COVER_CRAFTING = addItem(744, "cover_crafting").setInvisible();
        //COVER_DRAIN = addItem(745, "cover_drain").setInvisible();

        COVER_SHUTTER = register("cover_shutter");

        COVER_SOLAR_PANEL = register("cover_solar_panel");
        COVER_SOLAR_PANEL_ULV = register("cover_solar_panel_ulv");
        COVER_SOLAR_PANEL_LV = register("cover_solar_panel_lv");

        FLUID_CELL = register("fluid_cell", new GTItem(new GTItemSettings().fluidStats(new FluidStats(FluidAmount.BUCKET, false))));
        INTEGRATED_CIRCUIT = register("circuit_integrated"); // todo .addComponents(new IntCircuitBehaviour());
        FOAM_SPRAYER = register("foam_sprayer"); // todo .addComponents(new FoamSprayerBehavior());

        GELLED_TOLUENE = register("gelled_toluene");

        //IItemContainerItemProvider selfContainerItemProvider = itemStack -> itemStack;
        WOODEN_FORM_EMPTY = register("wooden_form_empty");
        WOODEN_FORM_BRICK = register("wooden_form_brick"); // todo .addComponents(selfContainerItemProvider);

        COMPRESSED_CLAY = register("compressed_clay");
        COMPRESSED_FIRECLAY = register("compressed_fireclay");
        FIRECLAY_BRICK = register("brick_fireclay");
        COKE_OVEN_BRICK = register("brick_coke");

        BOTTLE_PURPLE_DRINK = register("bottle_purple_drink", new Item(new Item.Settings().food(new FoodComponent.Builder()
                .hunger(8).saturationModifier(0.2f)
                .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 800, 1), 90).build())
                .recipeRemainder(Items.GLASS_BOTTLE))); // todo will this work in inventory?

        //DYE_INDIGO = addItem(410, "dye.indigo").addOreDict("dyeBlue").setInvisible();
        //for (int i = 0; i < EnumDyeColor.values().length; i++) {
        //    EnumDyeColor dyeColor = EnumDyeColor.values()[i];
        //    DYE_ONLY_ITEMS[i] = addItem(414 + i, "dye_" + dyeColor.getName()).addOreDict(getOrdictColorName(dyeColor));
        //}

        PLANT_BALL = register("plant_ball"); // todo .setBurnValue(75);
        ENERGIUM_DUST = register("energium_dust");

        POWER_UNIT_LV = register("power_unit_lv", new GTItem(new GTItemSettings().electricStats(ElectricStats.createElectricItem(100000L, VoltageTier.LV)).setMaxStackSize(8)));
        POWER_UNIT_MV = register("power_unit_mv", new GTItem(new GTItemSettings().electricStats(ElectricStats.createElectricItem(400000L, VoltageTier.MV)).setMaxStackSize(8)));
        POWER_UNIT_HV = register("power_unit_hv", new GTItem(new GTItemSettings().electricStats(ElectricStats.createElectricItem(1600000L, VoltageTier.HV)).setMaxStackSize(8)));
        JACKHAMMER_BASE = register("jackhammer_base", new GTItem(new GTItemSettings().electricStats(ElectricStats.createElectricItem(1600000L, VoltageTier.HV)).setMaxStackSize(4)));
        ENERGY_FIELD_PROJECTOR = register("energy_field_projector", new GTItem(new GTItemSettings().electricStats(ElectricStats.createElectricItem(16000000L, VoltageTier.EV)).setMaxStackSize(1)));

        NANO_SABER = register("nano_saber", new GTItem(new GTItemSettings().electricStats(ElectricStats.createElectricItem(4000000L, VoltageTier.HV)).setMaxStackSize(1))); // todo .addComponents(new NanoSaberBehavior())
        SCANNER = register("scanner", new GTItem(new GTItemSettings().electricStats(ElectricStats.createElectricItem(200000L, VoltageTier.LV)))); // todo .addComponents(new ScannerBehavior(50))

        CARBON_FIBERS = register("carbon_fibers");
        CARBON_MESH = register("carbon_mesh");
        CARBON_PLATE = register("carbon_plate");
        INGOT_MIXED_METAL = register("ingot_mixed_metal");
        ADVANCED_ALLOY_PLATE = register("plate_advanced_alloy");
        INGOT_IRIDIUM_ALLOY = register("ingot_iridium_alloy");
        PLATE_IRIDIUM_ALLOY = register("plate_iridium_alloy");
        NEUTRON_REFLECTOR = register("neutron_reflector");

        SILICON_BOULE = register("boule_silicon");
        GLOWSTONE_BOULE = register("boule_glowstone");
        NAQUADAH_BOULE = register("boule_naquadah");
        SILICON_WAFER = register("wafer_silicon");
        GLOWSTONE_WAFER = register("wafer_glowstone");
        NAQUADAH_WAFER = register("wafer_naquadah");

        COATED_BOARD = register("board_coated");
        EPOXY_BOARD = register("board_epoxy");
        FIBER_BOARD = register("board_fiber_reinforced");
        MULTILAYER_FIBER_BOARD = register("board_multilayer.fiber_reinforced");
        PHENOLIC_BOARD = register("board_phenolic");
        PLASTIC_BOARD = register("board_plastic");
        WETWARE_BOARD = register("board_wetware");

        VACUUM_TUBE = register("circuit_vacuum_tube"); // todo.setUnificationData(OrePrefix.circuit, Tier.Primitive);
        DIODE = register("component_diode");
        CAPACITOR = register("component_capacitor");
        GLASS_FIBER = register("component_glass_fiber");
        GLASS_TUBE = register("component_glass_tube");
        RESISTOR = register("component_resistor");
        SMALL_COIL = register("component_small_coil");
        SMD_DIODE = register("component_smd_diode");
        SMD_CAPACITOR = register("component_smd_capacitor");
        SMD_RESISTOR = register("component_smd_resistor");
        SMD_TRANSISTOR = register("component_smd_transistor");
        TRANSISTOR = register("component_transistor");

        ADVANCED_SYSTEM_ON_CHIP_WAFER = register("wafer_advanced_system_on_chip");
        INTEGRATED_LOGIC_CIRCUIT_WAFER = register("wafer_integrated_logic_circuit");
        CENTRAL_PROCESSING_UNIT_WAFER = register("wafer_central_processing_unit");
        HIGH_POWER_INTEGRATED_CIRCUIT_WAFER = register("wafer_high_power_integrated_circuit");
        NAND_MEMORY_CHIP_WAFER = register("wafer_nand_memory_chip");
        NANO_CENTRAL_PROCESSING_UNIT_WAFER = register("wafer_nano_central_processing_unit");
        NOR_MEMORY_CHIP_WAFER = register("wafer_nor_memory_chip");
        POWER_INTEGRATED_CIRCUIT_WAFER = register("wafer_power_integrated_circuit");
        QBIT_CENTRAL_PROCESSING_UNIT_WAFER = register("wafer_qbit_central_processing_unit");
        RANDOM_ACCESS_MEMORY_WAFER = register("wafer_random_access_memory");
        SYSTEM_ON_CHIP_WAFER = register("wafer_system_on_chip");

        CRYSTAL_CENTRAL_PROCESSING_UNIT = register("crystal_central_processing_unit");
        CRYSTAL_SYSTEM_ON_CHIP = register("crystal_system_on_chip");
        ADVANCED_SYSTEM_ON_CHIP = register("plate_advanced_system_on_chip");
        INTEGRATED_LOGIC_CIRCUIT = register("plate_integrated_logic_circuit");
        CENTRAL_PROCESSING_UNIT = register("plate_central_processing_unit");
        HIGH_POWER_INTEGRATED_CIRCUIT = register("plate_high_power_integrated_circuit");
        NAND_MEMORY_CHIP = register("plate_nand_memory_chip");
        NANO_CENTRAL_PROCESSING_UNIT = register("plate_nano_central_processing_unit");
        NOR_MEMORY_CHIP = register("plate_nor_memory_chip");
        POWER_INTEGRATED_CIRCUIT = register("plate_power_integrated_circuit");
        QBIT_CENTRAL_PROCESSING_UNIT = register("plate_qbit_central_processing_unit");
        RANDOM_ACCESS_MEMORY = register("plate_random_access_memory");
        SYSTEM_ON_CHIP = register("plate_system_on_chip");

        BASIC_CIRCUIT_LV = register("circuit_basic"); // todo .setUnificationData(OrePrefix.circuit, Tier.Basic);
        BASIC_ELECTRONIC_CIRCUIT_LV = register("circuit_basic_electronic"); // todo .setUnificationData(OrePrefix.circuit, Tier.Basic);
        ADVANCED_CIRCUIT_PARTS_LV = register("circuit_advanced_parts"); // todo .setUnificationData(OrePrefix.circuit, Tier.Basic);

        GOOD_INTEGRATED_CIRCUIT_MV = register("circuit_good"); // todo .setUnificationData(OrePrefix.circuit, Tier.Good);
        ADVANCED_CIRCUIT_MV = register("circuit_advanced"); // todo .setUnificationData(OrePrefix.circuit, Tier.Good);

        PROCESSOR_ASSEMBLY_HV = register("circuit_processor_assembly"); // todo .setUnificationData(OrePrefix.circuit, Tier.Advanced);
        NANO_PROCESSOR_HV = register("circuit_nano_processor"); // todo .setUnificationData(OrePrefix.circuit, Tier.Advanced);

        NANO_PROCESSOR_ASSEMBLY_EV = register("circuit_nano_processor_assembly"); // todo .setUnificationData(OrePrefix.circuit, Tier.Extreme);
        QUANTUM_PROCESSOR_EV = register("circuit_quantum_processor"); // todo .setUnificationData(OrePrefix.circuit, Tier.Extreme);

        DATA_CONTROL_CIRCUIT_IV = register("circuit_data_control"); // todo .setUnificationData(OrePrefix.circuit, Tier.Elite);
        CRYSTAL_PROCESSOR_IV = register("circuit_crystal_processor"); // todo .setUnificationData(OrePrefix.circuit, Tier.Elite);

        ENERGY_FLOW_CIRCUIT_LUV = register("circuit_energy_flow"); // todo .setUnificationData(OrePrefix.circuit, Tier.Master);
        WETWARE_PROCESSOR_LUV = register("circuit_wetware_processor"); // todo .setUnificationData(OrePrefix.circuit, Tier.Master);

        WETWARE_PROCESSOR_ASSEMBLY_ZPM = register("circuit_wetware_assembly"); // todo .setUnificationData(OrePrefix.circuit, Tier.Ultimate);
        WETWARE_SUPER_COMPUTER_UV = register("circuit_wetware_super_computer"); // todo .setUnificationData(OrePrefix.circuit, Tier.Superconductor);
        WETWARE_MAINFRAME_MAX = register("circuit_wetware_mainframe"); // todo .setUnificationData(OrePrefix.circuit, Tier.Infinite);

        ENGRAVED_CRYSTAL_CHIP = register("engraved_crystal_chip");
        ENGRAVED_LAPOTRON_CHIP = register("engraved_lapotron_chip");

        TURBINE_ROTOR = register("turbine_rotor"); // todo .addComponents(new TurbineRotorBehavior());
        //COVER_FACADE = addItem(509, "cover.facade").addComponents(new FacadeItem()).disableModelLoading();
    }
}
