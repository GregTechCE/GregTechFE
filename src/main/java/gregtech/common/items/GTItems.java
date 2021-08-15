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

    public static final Item COVER_ACTIVITY_DETECTOR;
    public static final Item COVER_FLUID_DETECTOR;
    public static final Item COVER_ITEM_DETECTOR;
    public static final Item COVER_ENERGY_DETECTOR;

    public static final Item COVER_SCREEN;
    public static final Item COVER_CRAFTING;
    public static final Item COVER_DRAIN;

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
        CREDIT_COPPER = register("credit.copper");
        CREDIT_CUPRONICKEL = register("credit.cupronickel");
        CREDIT_SILVER = register("credit.silver", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
        CREDIT_GOLD = register("credit.gold", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
        CREDIT_PLATINUM = register("credit.platinum", new Item(new Item.Settings().rarity(Rarity.RARE)));
        CREDIT_OSMIUM = register("credit.osmium", new Item(new Item.Settings().rarity(Rarity.RARE)));
        CREDIT_NAQUADAH = register("credit.naquadah", new Item(new Item.Settings().rarity(Rarity.EPIC)));
        CREDIT_DARMSTADTIUM = register("credit.darmstadtium", new Item(new Item.Settings().rarity(Rarity.EPIC)));

        COIN_GOLD_ANCIENT = register("coin.gold.ancient", new Item(new Item.Settings().rarity(Rarity.RARE)));
        COIN_DOGE = register("coin.doge", new Item(new Item.Settings().rarity(Rarity.EPIC)));
        COIN_CHOCOLATE = register("coin.chocolate", new Item(new Item.Settings().food(new FoodComponent.Builder()
                .hunger(1).saturationModifier(0.1f).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 1), 10).build())));

        SHAPE_EMPTY = register("shape.empty");

        SHAPE_MOLDS[0] = SHAPE_MOLD_PLATE = register("shape.mold.plate");
        SHAPE_MOLDS[1] = SHAPE_MOLD_GEAR = register("shape.mold.gear");
        SHAPE_MOLDS[2] = SHAPE_MOLD_CREDIT = register("shape.mold.credit");
        SHAPE_MOLDS[3] = SHAPE_MOLD_BOTTLE = register("shape.mold.bottle");
        SHAPE_MOLDS[4] = SHAPE_MOLD_INGOT = register("shape.mold.ingot");
        SHAPE_MOLDS[5] = SHAPE_MOLD_BALL = register("shape.mold.ball");
        SHAPE_MOLDS[6] = SHAPE_MOLD_BLOCK = register("shape.mold.block");
        SHAPE_MOLDS[7] = SHAPE_MOLD_NUGGET = register("shape.mold.nugget");
        SHAPE_MOLDS[8] = SHAPE_MOLD_CYLINDER = register("shape.mold.cylinder");
        SHAPE_MOLDS[9] = SHAPE_MOLD_ANVIL = register("shape.mold.anvil");
        SHAPE_MOLDS[10] = SHAPE_MOLD_NAME = register("shape.mold.name");
        SHAPE_MOLDS[11] = SHAPE_MOLD_GEAR_SMALL = register("shape.mold.gear.small");
        SHAPE_MOLDS[12] = SHAPE_MOLD_ROTOR = register("shape.mold.rotor");

        SHAPE_EXTRUDERS[0] = SHAPE_EXTRUDER_PLATE = register("shape.extruder.plate");
        SHAPE_EXTRUDERS[1] = SHAPE_EXTRUDER_ROD = register("shape.extruder.rod");
        SHAPE_EXTRUDERS[2] = SHAPE_EXTRUDER_BOLT = register("shape.extruder.bolt");
        SHAPE_EXTRUDERS[3] = SHAPE_EXTRUDER_RING = register("shape.extruder.ring");
        SHAPE_EXTRUDERS[4] = SHAPE_EXTRUDER_CELL = register("shape.extruder.cell");
        SHAPE_EXTRUDERS[5] = SHAPE_EXTRUDER_INGOT = register("shape.extruder.ingot");
        SHAPE_EXTRUDERS[6] = SHAPE_EXTRUDER_WIRE = register("shape.extruder.wire");
        SHAPE_EXTRUDERS[7] = SHAPE_EXTRUDER_PIPE_TINY = register("shape.extruder.pipe.tiny");
        SHAPE_EXTRUDERS[8] = SHAPE_EXTRUDER_PIPE_SMALL = register("shape.extruder.pipe.small");
        SHAPE_EXTRUDERS[9] = SHAPE_EXTRUDER_PIPE_MEDIUM = register("shape.extruder.pipe.medium");
        SHAPE_EXTRUDERS[10] = SHAPE_EXTRUDER_PIPE_LARGE = register("shape.extruder.pipe.large");
        SHAPE_EXTRUDERS[11] = SHAPE_EXTRUDER_BLOCK = register("shape.extruder.block");
        SHAPE_EXTRUDERS[12] = SHAPE_EXTRUDER_SWORD = register("shape.extruder.sword");
        SHAPE_EXTRUDERS[13] = SHAPE_EXTRUDER_PICKAXE = register("shape.extruder.pickaxe");
        SHAPE_EXTRUDERS[14] = SHAPE_EXTRUDER_SHOVEL = register("shape.extruder.shovel");
        SHAPE_EXTRUDERS[15] = SHAPE_EXTRUDER_AXE = register("shape.extruder.axe");
        SHAPE_EXTRUDERS[16] = SHAPE_EXTRUDER_HOE = register("shape.extruder.hoe");
        SHAPE_EXTRUDERS[17] = SHAPE_EXTRUDER_HAMMER = register("shape.extruder.hammer");
        SHAPE_EXTRUDERS[18] = SHAPE_EXTRUDER_FILE = register("shape.extruder.file");
        SHAPE_EXTRUDERS[19] = SHAPE_EXTRUDER_SAW = register("shape.extruder.saw");
        SHAPE_EXTRUDERS[20] = SHAPE_EXTRUDER_GEAR = register("shape.extruder.gear");
        SHAPE_EXTRUDERS[21] = SHAPE_EXTRUDER_BOTTLE = register("shape.extruder.bottle");

        SPRAY_EMPTY = register("spray.empty");

        // todo .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Steel, OrePrefix.plate.materialAmount * 2L + 2L * OrePrefix.ring.materialAmount)));
        LARGE_FLUID_CELL_STEEL = register("large_fluid_cell.steel", new GTItem((GTItemSettings) new GTItemSettings().fluidStats(new FluidStats(FluidAmount.ofWhole(64000), true)).maxCount(16)));

        // todo .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.TungstenSteel, OrePrefix.plate.materialAmount * 2L + 2L * OrePrefix.ring.materialAmount)));
        LARGE_FLUID_CELL_TUNGSTEN_STEEL = register("large_fluid_cell.tungstensteel", new GTItem((GTItemSettings) new GTItemSettings().fluidStats(new FluidStats(FluidAmount.ofWhole(256000), true)).maxCount(16)));

        /*
        for (int i = 0; i < EnumDyeColor.values().length; i++) {
            EnumDyeColor dyeColor = EnumDyeColor.values()[i];
            SPRAY_CAN_DYES[i] = addItem(430 + 2 * i, "spray.can.dyes." + dyeColor.getName()).setMaxStackSize(1);
            ColorSprayBehaviour behaviour = new ColorSprayBehaviour(SPRAY_EMPTY.getStackForm(), 512, i);
            SPRAY_CAN_DYES[i].addComponents(behaviour);
        }
*/
        // todo .addComponents(new LighterBehaviour(1));
        TOOL_MATCHES = register("tool.matches");
        // todo .addComponents(new LighterBehaviour(16))
        TOOL_MATCHBOX = register("tool.matchbox", new Item(new Item.Settings().maxCount(1)));
        // todo .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Invar, GTValues.L * 2)))
        TOOL_LIGHTER_INVAR = register("tool.lighter.invar", new Item(new Item.Settings().maxCount(1)));
        // todo .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Platinum, GTValues.L * 2))).addComponents(new LighterBehaviour(1000));
        TOOL_LIGHTER_PLATINUM = register("tool.lighter.platinum", new Item(new Item.Settings().maxCount(1)));


        BATTERY_ULV_TANTALUM = register("battery.re.ulv.tantalum", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1000, VoltageTier.ULV))));

        // todo .setModelAmount(8)
        BATTERY_LV_CADMIUM = register("battery.re.lv.cadmium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(120000L, VoltageTier.LV))));
        BATTERY_LV_LITHIUM = register("battery.re.lv.lithium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(100000L, VoltageTier.LV))));
        BATTERY_LV_SODIUM = register("battery.re.lv.sodium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(80000L, VoltageTier.LV))));

        // todo .setModelAmount(8)
        BATTERY_MV_CADMIUM = register("battery.re.mv.cadmium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(420000L, VoltageTier.MV))));
        BATTERY_MV_LITHIUM = register("battery.re.mv.lithium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(400000L, VoltageTier.MV))));
        BATTERY_MV_SODIUM = register("battery.re.mv.sodium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(360000L, VoltageTier.MV))));

        // todo .setModelAmount(8)
        BATTERY_HV_CADMIUM = register("battery.re.hv.cadmium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1800000L, VoltageTier.HV))));
        BATTERY_HV_LITHIUM = register("battery.re.hv.lithium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1600000L, VoltageTier.HV))));
        BATTERY_HV_SODIUM = register("battery.re.hv.sodium", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1200000L, VoltageTier.HV))));

        // TODO .setModelAmount(8)
        ENERGY_CRYSTAL = register("energy_crystal", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(4000000L, VoltageTier.HV)).maxCount(1)));
        LAPOTRON_CRYSTAL = register("energy_crystal", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(10000000L, VoltageTier.EV)).maxCount(1)));

        // todo .setModelAmount(8), .setUnificationData(OrePrefix.battery, MarkerMaterials.Tier.Ultimate)
        ENERGY_LAPOTRONIC_ORB = register("energy.lapotronicorb", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(100000000L, VoltageTier.IV))));
        ENERGY_LAPOTRONIC_ORB2 = register("energy.lapotronicorb2", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(1000000000L, VoltageTier.LuV))));

        // todo .setModelAmount(8)
        ZPM = register("zpm", new GTItem(new GTItemSettings().electricStats(ElectricStats.createBattery(2000000000000L, VoltageTier.ZPM, false))));
        ZPM2 = register("zpm2", new GTItem(new GTItemSettings().electricStats(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, VoltageTier.UV))));

        BATTERY_HULL_LV = register("battery.hull.lv");
        BATTERY_HULL_MV = register("battery.hull.mv");
        BATTERY_HULL_HV = register("battery.hull.hv");

        ELECTRIC_MOTOR_LV = register("electric.motor.lv");
        ELECTRIC_MOTOR_MV = register("electric.motor.mv");
        ELECTRIC_MOTOR_HV = register("electric.motor.hv");
        ELECTRIC_MOTOR_EV = register("electric.motor.ev");
        ELECTRIC_MOTOR_IV = register("electric.motor.iv");
        ELECTRIC_MOTOR_LUV = register("electric.motor.luv");
        ELECTRIC_MOTOR_ZPM = register("electric.motor.zpm");
        ELECTRIC_MOTOR_UV = register("electric.motor.uv");

        ELECTRIC_PUMP_LV = register("electric.pump.lv");
        ELECTRIC_PUMP_MV = register("electric.pump.mv");
        ELECTRIC_PUMP_HV = register("electric.pump.hv");
        ELECTRIC_PUMP_EV = register("electric.pump.ev");
        ELECTRIC_PUMP_IV = register("electric.pump.iv");
        ELECTRIC_PUMP_LUV = register("electric.pump.luv");
        ELECTRIC_PUMP_ZPM = register("electric.pump.zpm");
        ELECTRIC_PUMP_UV = register("electric.pump.uv");

        RUBBER_DROP = register("rubber_drop"); // todo .setBurnValue(200);

        FLUID_FILTER = register("fluid_filter");

        DYNAMITE = register("dynamite", new Item(new Item.Settings().maxCount(16))); // todo .addComponents(new DynamiteBehaviour())

        CONVEYOR_MODULE_LV = register("conveyor.module.lv");
        CONVEYOR_MODULE_MV = register("conveyor.module.mv");
        CONVEYOR_MODULE_HV = register("conveyor.module.hv");
        CONVEYOR_MODULE_EV = register("conveyor.module.ev");
        CONVEYOR_MODULE_IV = register("conveyor.module.iv");
        CONVEYOR_MODULE_LUV = register("conveyor.module.luv");
        CONVEYOR_MODULE_ZPM = register("conveyor.module.zpm");
        CONVEYOR_MODULE_UV = register("conveyor.module.uv");

        ELECTRIC_PISTON_LV = register("electric.piston.lv");
        ELECTRIC_PISTON_MV = register("electric.piston.mv");
        ELECTRIC_PISTON_HV = register("electric.piston.hv");
        ELECTRIC_PISTON_EV = register("electric.piston.ev");
        ELECTRIC_PISTON_IV = register("electric.piston.iv");
        ELECTRIC_PISTON_LUV = register("electric.piston.luv");
        ELECTRIC_PISTON_ZPM = register("electric.piston.zpm");
        ELECTRIC_PISTON_UV = register("electric.piston.uv");

        ROBOT_ARM_LV = register("robot.arm.lv");
        ROBOT_ARM_MV = register("robot.arm.mv");
        ROBOT_ARM_HV = register("robot.arm.hv");
        ROBOT_ARM_EV = register("robot.arm.ev");
        ROBOT_ARM_IV = register("robot.arm.iv");
        ROBOT_ARM_LUV = register("robot.arm.luv");
        ROBOT_ARM_ZPM = register("robot.arm.zpm");
        ROBOT_ARM_UV = register("robot.arm.uv");

        FIELD_GENERATOR_LV = register("field.generator.lv");
        FIELD_GENERATOR_MV = register("field.generator.mv");
        FIELD_GENERATOR_HV = register("field.generator.hv");
        FIELD_GENERATOR_EV = register("field.generator.ev");
        FIELD_GENERATOR_IV = register("field.generator.iv");
        FIELD_GENERATOR_LUV = register("field.generator.luv");
        FIELD_GENERATOR_ZPM = register("field.generator.zpm");
        FIELD_GENERATOR_UV = register("field.generator.uv");

        EMITTER_LV = register("emitter.lv");
        EMITTER_MV = register("emitter.mv");
        EMITTER_HV = register("emitter.hv");
        EMITTER_EV = register("emitter.ev");
        EMITTER_IV = register("emitter.iv");
        EMITTER_LUV = register("emitter.luv");
        EMITTER_ZPM = register("emitter.zpm");
        EMITTER_UV = register("emitter.uv");

        SENSOR_LV = register("sensor.lv");
        SENSOR_MV = register("sensor.mv");
        SENSOR_HV = register("sensor.hv");
        SENSOR_EV = register("sensor.ev");
        SENSOR_IV = register("sensor.iv");
        SENSOR_LUV = register("sensor.luv");
        SENSOR_ZPM = register("sensor.zpm");
        SENSOR_UV = register("sensor.uv");

        FLUID_REGULATOR_LV = register("fluid.regulator.lv");
        FLUID_REGULATOR_MV = register("fluid.regulator.mv");
        FLUID_REGULATOR_HV = register("fluid.regulator.hv");
        FLUID_REGULATOR_EV = register("fluid.regulator.ev");
        FLUID_REGULATOR_IV = register("fluid.regulator.iv");
        FLUID_REGULATOR_LUV = register("fluid.regulator.luv");
        FLUID_REGULATOR_ZPM = register("fluid.regulator.zpm");
        FLUID_REGULATOR_UV = register("fluid.regulator.uv");

        TOOL_DATA_STICK = register("tool.datastick");
        TOOL_DATA_ORB = register("tool.dataorb");

        COMPONENT_SAW_BLADE_DIAMOND = register("component.sawblade.diamond"); // todo .addOreDict(OreDictNames.craftingDiamondBlade);
        COMPONENT_GRINDER_DIAMOND = register("component.grinder.diamond"); // todo .addOreDict(OreDictNames.craftingGrinder);
        COMPONENT_GRINDER_TUNGSTEN = register("component.grinder.tungsten"); // todo .addOreDict(OreDictNames.craftingGrinder);

        QUANTUM_EYE = register("quantumeye");
        QUANTUM_STAR = register("quantumstar");
        GRAVI_STAR = register("gravistar");

        ITEM_FILTER = register("item_filter");
        ORE_DICTIONARY_FILTER = register("ore_dictionary_filter");
        SMART_FILTER = register("smart_item_filter");

        COVER_MACHINE_CONTROLLER = register("cover.controller");

        //COVER_ACTIVITY_DETECTOR = addItem(731, "cover.activity.detector").setInvisible();
        //COVER_FLUID_DETECTOR = addItem(732, "cover.fluid.detector").setInvisible();
        //COVER_ITEM_DETECTOR = addItem(733, "cover.item.detector").setInvisible();
        //COVER_ENERGY_DETECTOR = addItem(734, "cover.energy.detector").setInvisible();

        //COVER_SCREEN = addItem(740, "cover.screen").setInvisible();
        //COVER_CRAFTING = addItem(744, "cover.crafting").setInvisible();
        //COVER_DRAIN = addItem(745, "cover.drain").setInvisible();

        COVER_SHUTTER = register("cover.shutter");

        COVER_SOLAR_PANEL = register("cover.solar.panel");
        COVER_SOLAR_PANEL_ULV = register("cover.solar.panel.ulv");
        COVER_SOLAR_PANEL_LV = register("cover.solar.panel.lv");

        FLUID_CELL = register("fluid_cell", new GTItem(new GTItemSettings().fluidStats(new FluidStats(FluidAmount.BUCKET, false))));
        INTEGRATED_CIRCUIT = register("circuit.integrated"); // todo .addComponents(new IntCircuitBehaviour());
        FOAM_SPRAYER = register("foam_sprayer"); // todo .addComponents(new FoamSprayerBehavior());

        GELLED_TOLUENE = register("gelled_toluene");

        //IItemContainerItemProvider selfContainerItemProvider = itemStack -> itemStack;
        WOODEN_FORM_EMPTY = register("wooden_form.empty");
        WOODEN_FORM_BRICK = register("wooden_form.brick"); // todo .addComponents(selfContainerItemProvider);

        COMPRESSED_CLAY = register("compressed.clay");
        COMPRESSED_FIRECLAY = register("compressed.fireclay");
        FIRECLAY_BRICK = register("brick.fireclay");
        COKE_OVEN_BRICK = register("brick.coke");

        BOTTLE_PURPLE_DRINK = register("bottle.purple.drink", new Item(new Item.Settings().food(new FoodComponent.Builder()
                .hunger(8).saturationModifier(0.2f)
                .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 800, 1), 90).build())
                .recipeRemainder(Items.GLASS_BOTTLE))); // todo will this work in inventory?

        //DYE_INDIGO = addItem(410, "dye.indigo").addOreDict("dyeBlue").setInvisible();
        //for (int i = 0; i < EnumDyeColor.values().length; i++) {
        //    EnumDyeColor dyeColor = EnumDyeColor.values()[i];
        //    DYE_ONLY_ITEMS[i] = addItem(414 + i, "dye." + dyeColor.getName()).addOreDict(getOrdictColorName(dyeColor));
        //}

        PLANT_BALL = register("plant_ball"); // todo .setBurnValue(75);
        ENERGIUM_DUST = register("energium_dust");

        POWER_UNIT_LV = register("power_unit.lv", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createElectricItem(100000L, VoltageTier.LV)).maxCount(8)));
        POWER_UNIT_MV = register("power_unit.mv", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createElectricItem(400000L, VoltageTier.MV)).maxCount(8)));
        POWER_UNIT_HV = register("power_unit.hv", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createElectricItem(1600000L, VoltageTier.HV)).maxCount(8)));
        JACKHAMMER_BASE = register("jackhammer_base", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createElectricItem(1600000L, VoltageTier.HV)).maxCount(4)));
        ENERGY_FIELD_PROJECTOR = register("energy_field_projector", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createElectricItem(16000000L, VoltageTier.EV)).maxCount(1)));

        NANO_SABER = register("nano_saber", new GTItem((GTItemSettings) new GTItemSettings().electricStats(ElectricStats.createElectricItem(4000000L, VoltageTier.HV)).maxCount(1))); // todo .addComponents(new NanoSaberBehavior())
        SCANNER = register("scanner", new GTItem(new GTItemSettings().electricStats(ElectricStats.createElectricItem(200000L, VoltageTier.LV)))); // todo .addComponents(new ScannerBehavior(50))

        CARBON_FIBERS = register("carbon.fibers");
        CARBON_MESH = register("carbon.mesh");
        CARBON_PLATE = register("carbon.plate");
        INGOT_MIXED_METAL = register("ingot.mixed_metal");
        ADVANCED_ALLOY_PLATE = register("plate.advanced_alloy");
        INGOT_IRIDIUM_ALLOY = register("ingot.iridium_alloy");
        PLATE_IRIDIUM_ALLOY = register("plate.iridium_alloy");
        NEUTRON_REFLECTOR = register("neutron_reflector");

        SILICON_BOULE = register("boule.silicon");
        GLOWSTONE_BOULE = register("boule.glowstone");
        NAQUADAH_BOULE = register("boule.naquadah");
        SILICON_WAFER = register("wafer.silicon");
        GLOWSTONE_WAFER = register("wafer.glowstone");
        NAQUADAH_WAFER = register("wafer.naquadah");

        COATED_BOARD = register("board.coated");
        EPOXY_BOARD = register("board.epoxy");
        FIBER_BOARD = register("board.fiber_reinforced");
        MULTILAYER_FIBER_BOARD = register("board.multilayer.fiber_reinforced");
        PHENOLIC_BOARD = register("board.phenolic");
        PLASTIC_BOARD = register("board.plastic");
        WETWARE_BOARD = register("board.wetware");

        VACUUM_TUBE = register("circuit.vacuum_tube"); // todo.setUnificationData(OrePrefix.circuit, Tier.Primitive);
        DIODE = register("component.diode");
        CAPACITOR = register("component.capacitor");
        GLASS_FIBER = register("component.glass.fiber");
        GLASS_TUBE = register("component.glass.tube");
        RESISTOR = register("component.resistor");
        SMALL_COIL = register("component.small_coil");
        SMD_DIODE = register("component.smd.diode");
        SMD_CAPACITOR = register("component.smd.capacitor");
        SMD_RESISTOR = register("component.smd.resistor");
        SMD_TRANSISTOR = register("component.smd.transistor");
        TRANSISTOR = register("component.transistor");

        ADVANCED_SYSTEM_ON_CHIP_WAFER = register("wafer.advanced_system_on_chip");
        INTEGRATED_LOGIC_CIRCUIT_WAFER = register("wafer.integrated_logic_circuit");
        CENTRAL_PROCESSING_UNIT_WAFER = register("wafer.central_processing_unit");
        HIGH_POWER_INTEGRATED_CIRCUIT_WAFER = register("wafer.high_power_integrated_circuit");
        NAND_MEMORY_CHIP_WAFER = register("wafer.nand_memory_chip");
        NANO_CENTRAL_PROCESSING_UNIT_WAFER = register("wafer.nano_central_processing_unit");
        NOR_MEMORY_CHIP_WAFER = register("wafer.nor_memory_chip");
        POWER_INTEGRATED_CIRCUIT_WAFER = register("wafer.power_integrated_circuit");
        QBIT_CENTRAL_PROCESSING_UNIT_WAFER = register("wafer.qbit_central_processing_unit");
        RANDOM_ACCESS_MEMORY_WAFER = register("wafer.random_access_memory");
        SYSTEM_ON_CHIP_WAFER = register("wafer.system_on_chip");

        CRYSTAL_CENTRAL_PROCESSING_UNIT = register("crystal.central_processing_unit");
        CRYSTAL_SYSTEM_ON_CHIP = register("crystal.system_on_chip");
        ADVANCED_SYSTEM_ON_CHIP = register("plate.advanced_system_on_chip");
        INTEGRATED_LOGIC_CIRCUIT = register("plate.integrated_logic_circuit");
        CENTRAL_PROCESSING_UNIT = register("plate.central_processing_unit");
        HIGH_POWER_INTEGRATED_CIRCUIT = register("plate.high_power_integrated_circuit");
        NAND_MEMORY_CHIP = register("plate.nand_memory_chip");
        NANO_CENTRAL_PROCESSING_UNIT = register("plate.nano_central_processing_unit");
        NOR_MEMORY_CHIP = register("plate.nor_memory_chip");
        POWER_INTEGRATED_CIRCUIT = register("plate.power_integrated_circuit");
        QBIT_CENTRAL_PROCESSING_UNIT = register("plate.qbit_central_processing_unit");
        RANDOM_ACCESS_MEMORY = register("plate.random_access_memory");
        SYSTEM_ON_CHIP = register("plate.system_on_chip");

        BASIC_CIRCUIT_LV = register("circuit.basic"); // todo .setUnificationData(OrePrefix.circuit, Tier.Basic);
        BASIC_ELECTRONIC_CIRCUIT_LV = register("circuit.basic_electronic"); // todo .setUnificationData(OrePrefix.circuit, Tier.Basic);
        ADVANCED_CIRCUIT_PARTS_LV = register("circuit.advanced_parts"); // todo .setUnificationData(OrePrefix.circuit, Tier.Basic);

        GOOD_INTEGRATED_CIRCUIT_MV = register("circuit.good"); // todo .setUnificationData(OrePrefix.circuit, Tier.Good);
        ADVANCED_CIRCUIT_MV = register("circuit.advanced"); // todo .setUnificationData(OrePrefix.circuit, Tier.Good);

        PROCESSOR_ASSEMBLY_HV = register("circuit.processor_assembly"); // todo .setUnificationData(OrePrefix.circuit, Tier.Advanced);
        NANO_PROCESSOR_HV = register("circuit.nano_processor"); // todo .setUnificationData(OrePrefix.circuit, Tier.Advanced);

        NANO_PROCESSOR_ASSEMBLY_EV = register("circuit.nano_processor_assembly"); // todo .setUnificationData(OrePrefix.circuit, Tier.Extreme);
        QUANTUM_PROCESSOR_EV = register("circuit.quantum_processor"); // todo .setUnificationData(OrePrefix.circuit, Tier.Extreme);

        DATA_CONTROL_CIRCUIT_IV = register("circuit.data_control"); // todo .setUnificationData(OrePrefix.circuit, Tier.Elite);
        CRYSTAL_PROCESSOR_IV = register("circuit.crystal_processor"); // todo .setUnificationData(OrePrefix.circuit, Tier.Elite);

        ENERGY_FLOW_CIRCUIT_LUV = register("circuit.energy_flow"); // todo .setUnificationData(OrePrefix.circuit, Tier.Master);
        WETWARE_PROCESSOR_LUV = register("circuit.wetware_processor"); // todo .setUnificationData(OrePrefix.circuit, Tier.Master);

        WETWARE_PROCESSOR_ASSEMBLY_ZPM = register("circuit.wetware_assembly"); // todo .setUnificationData(OrePrefix.circuit, Tier.Ultimate);
        WETWARE_SUPER_COMPUTER_UV = register("circuit.wetware_super_computer"); // todo .setUnificationData(OrePrefix.circuit, Tier.Superconductor);
        WETWARE_MAINFRAME_MAX = register("circuit.wetware_mainframe"); // todo .setUnificationData(OrePrefix.circuit, Tier.Infinite);

        ENGRAVED_CRYSTAL_CHIP = register("engraved.crystal_chip");
        ENGRAVED_LAPOTRON_CHIP = register("engraved.lapotron_chip");

        TURBINE_ROTOR = register("turbine_rotor"); // todo .addComponents(new TurbineRotorBehavior());
        //COVER_FACADE = addItem(509, "cover.facade").addComponents(new FacadeItem()).disableModelLoading();
    }
}
