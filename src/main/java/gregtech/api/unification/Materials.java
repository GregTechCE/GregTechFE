package gregtech.api.unification;

import gregtech.api.GTValues;
import gregtech.api.enchants.EnchantmentData;
import gregtech.api.fluids.MaterialFluidProperties;
import gregtech.api.fluids.MaterialFluidTexture;
import gregtech.api.unification.element.Element;
import gregtech.api.unification.element.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.*;
import gregtech.api.util.VoltageTier;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

import static com.google.common.collect.ImmutableList.of; //TODO: switch to java.util.list.of();
import static net.minecraft.enchantment.Enchantments.*;
import static gregtech.api.unification.util.MaterialIconSets.*;
import static gregtech.api.unification.material.flags.MaterialFlags.*;
import static gregtech.api.util.Colors.*;

public class Materials {

    /**
     * Direct Elements
     */
    public static final Material Aluminium;
    public static final Material Antimony;
    public static final Material Argon;
    public static final Material Arsenic;
    public static final Material Barium;
    public static final Material Beryllium;
    public static final Material Bismuth;
    public static final Material Boron;
    public static final Material Calcium;
    public static final Material Cadmium;
    public static final Material Carbon;
    public static final Material Chlorine;
    public static final Material Chrome;
    public static final Material Copper;
    public static final Material Cobalt;
    public static final Material Fluorine;
    public static final Material Gallium;
    public static final Material Gold;
    public static final Material Helium;
    public static final Material Hydrogen;
    public static final Material Indium;
    public static final Material Iridium;
    public static final Material Iron;
    public static final Material Lead;
    public static final Material Lithium;
    public static final Material Magnesium;
    public static final Material Manganese;
    public static final Material Mercury;
    public static final Material Molybdenum;
    public static final Material Neodymium;
    public static final Material Nickel;
    public static final Material Niobium;
    public static final Material Nitrogen;
    public static final Material Osmium;
    public static final Material Oxygen;
    public static final Material Palladium;
    public static final Material Phosphorus;
    public static final Material Platinum;
    public static final Material Plutonium;
    public static final Material Potassium;
    public static final Material Silicon;
    public static final Material Silver;
    public static final Material Sodium;
    public static final Material Sulfur;
    public static final Material Tantalum;
    public static final Material Thorium;
    public static final Material Tin;
    public static final Material Titanium;
    public static final Material Tungsten;
    public static final Material Vanadium;
    public static final Material Yttrium;
    public static final Material Zinc;

    /**
     * Compounds
     */

    public static final Material AnnealedCopper;
    public static final Material Brass;
    public static final Material Bronze;
    public static final Material Charcoal;
    public static final Material Coal;
    public static final Material CobaltBrass;
    public static final Material Diamond;
    public static final Material Electrum;
    public static final Material Emerald;
    public static final Material Graphite;
    public static final Material Invar;
    public static final Material MagneticIron;
    public static final Material MagneticNeodymium;
    public static final Material MagneticSteel;
    public static final Material NetherQuartz;
    public static final Material Paper;
    public static final Material RawRubber;
    public static final Material Rubber;
    public static final Material RedAlloy;
    public static final Material Steel;
    public static final Material StainlessSteel;
    public static final Material Quartzite;
    public static final Material Water;
    public static final Material WroughtIron;

    private Materials(){
    }

    private static <T extends Material> T register(String name, T material) {
        return Registry.register(Material.REGISTRY, new Identifier(GTValues.MODID, name), material);
    }

    static {
        Aluminium = register("aluminium", new Material(new Material.Settings()
                .visual(0x80C8F0, DULL)
                .element(Elements.Al)
                .metal(2)
                .canCreateTools(10.0F,2.0F, 128)
                .canCreateCables(VoltageTier.EV, 1, 1)
                .smeltsInBlastFurnace(1700)
                .baseForElectricComponents()
                .flags(GENERATE_FOIL, GENERATE_GEAR))); //Has ore; Byproducts: Bauxite; GENERATE_FRAME

        Antimony = register("antimony", new Material(new Material.Settings()
                .visual(0xDCDCC8, SHINY)
                .element(Elements.Sb)
                .metal(2)
                .mortarGrindable()));

        Argon = register("argon", new Material(new Material.Settings()
                .visual(0xBBBB00)
                .element(Elements.Ar)
                .gas()
                .plasma()));

        Arsenic = register("arsenic", new Material(new Material.Settings()
                .visual(0xBBBB00, SAND)
                .element(Elements.As)
                .dust(2)));

        Barium = register("barium", new Material(new Material.Settings()
                .visual(COLOR_WHITE, SHINY)
                .element(Elements.Ba)
                .metal(2)));

        Beryllium = register("beryllium", new Material(new Material.Settings()
                .visual(0x64B464, METALLIC)
                .element(Elements.Be)
                .metal(2)
                .flags(GENERATE_PLATE))); //Has ore; Byproducts: Emerald

        Bismuth = register("bismuth", new Material(new Material.Settings()
                .visual(0x64A0A0, METALLIC)
                .element(Elements.Bi)
                .metal(1)));//Has ore

        Boron = register("boron", new Material(new Material.Settings()
                .visual(0xD2F0D2, SAND)
                .element(Elements.B)
                .dust(2)));

        Calcium = register("calcium", new Material(new Material.Settings()
                .visual(0xDDDDAA, METALLIC)
                .element(Elements.Ca)
                .metal(2)));

        Cadmium = register("cadmium", new Material(new Material.Settings()
                .visual(0x505060, SHINY)
                .element(Elements.Cd)
                .metal(2)));

        Carbon = register("carbon", new Material(new Material.Settings()
                .visual(0x333333, DULL)
                .element(Elements.C)
                .dust(2)));

        Chlorine = register("chlorine", new Material(new Material.Settings()
                .visual(0xEEEECC)
                .element(Elements.Cl)
                .gas()));

        Chrome = register("chrome", new Material(new Material.Settings()
                .visual(0xFFAAAB, SHINY)
                .element(Elements.Cr)
                .metal(3)
                .smeltsInBlastFurnace(1700)
                .canCreateTools(12.0f, 3.0f, 512)
                .flags(GENERATE_GEAR)));

        Copper = register("copper", new Material(new Material.Settings()
                .visual(COLOR_ORANGE, SHINY)
                .element(Elements.Cu)
                .metal(1)
                .canCreateCables(VoltageTier.MV, 1, 2)
                .canCreateFluidPipe(25, 1000, true)
                .mortarGrindable()
                .smeltsInArcFurnace(new Supplier<>() {
                    @Override
                    public Material get() {
                        return AnnealedCopper;
                    }
                })
                .flags(GENERATE_FOIL, GENERATE_FINE_WIRE))); //Has ore; Byproducts: Cobalt, Gold, Nickel

        Cobalt = register("cobalt", new Material(new Material.Settings()
                .visual(0x2929BC, METALLIC)
                .element(Elements.Co)
                .metal(2)
                .canCreateTools(10.0F, 3.0f, 256)
                .canCreateCables(VoltageTier.LV, 2, 2))); //Has ore; Byproducts: Cobaltite

        Fluorine = register("fluorine", new Material(new Material.Settings()
                .visual(0xFFFFAA)
                .element(Elements.F)
                .gas()));//Temperature 253

        Gallium = register("gallium", new Material(new Material.Settings()
                .visual(0xEEEEFF, SHINY)
                .element(Elements.Ga)
                .metal(2)
                .flags(GENERATE_PLATE)));

        Gold = register("gold", new Material(new Material.Settings()
                .visual(COLOR_YELLOW, SHINY)
                .element(Elements.Au)
                .metal(2)
                .canCreateCables(VoltageTier.HV, 2, 2)
                .mortarGrindable()
                .flags(GENERATE_PLATE, GENERATE_FOIL))); //Has ore; Byproducts: Copper, Nickel; Foil used only in Chocolate Coin

        Helium = register("helium", new Material(new Material.Settings()
                .visual(0xDDDD00)
                .element(Elements.He)
                .gas()
                .plasma()));

        Hydrogen = register("hydrogen", new Material(new Material.Settings()
                .visual(0x00FFAA)
                .element(Elements.H)
                .gas()));

        Indium = register("indium", new Material(new Material.Settings()
                .visual(0x6600BB, METALLIC)
                .element(Elements.In)
                .metal(2)));

        Iridium = register("iridium", new Material(new Material.Settings()
                .visual(0x6600BB, DULL)
                .element(Elements.Ir)
                .metal(3)
                .canCreateTools(7.0F, 3.0f, 2560)
                .smeltsInBlastFurnace(2719)));//Has ore; Byproducts: Platinum, Osmium

        Iron = register("iron", new Material(new Material.Settings()
                .visual(0xAAAAAA, METALLIC)
                .element(Elements.Fe)
                .metal(2)
                .canCreateToolsWithDefaultEnchant(7.0F, 2.5F, 256, 14)
                .canCreateCables(VoltageTier.MV, 2, 3)
                .mortarGrindable()
                .polarizeInto(new Supplier<>() {
                    @Override
                    public Material get() {
                        return MagneticIron;
                    }
                })
                .smeltsInArcFurnace(new Supplier<>() {
                    @Override
                    public Material get() {
                        return WroughtIron;
                    }
                })
                .plasma()
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_RING))); //Has ore; Ring used only for Tripwire Hook; GENERATE_FRAME

        Lead = register("lead", new Material(new Material.Settings()
                .visual(0x8C648C, DULL)
                .element(Elements.Pb)
                .metal(1)
                .canCreateCables(VoltageTier.ULV, 2,1)
                .mortarGrindable()
                .flags(GENERATE_PLATE))); //Has ore; Byproducts: Silver, Sulfur

        Lithium = register("lithium", new Material(new Material.Settings()
                .visual(0xCBCBCB, DULL)
                .element(Elements.Li)
                .metal(2))); //Has ore

        Magnesium = register("magnesium", new Material(new Material.Settings()
                .visual(0xFFBBBB, METALLIC)
                .element(Elements.Mg)
                .metal(2)));

        Manganese = register("manganese", new Material(new Material.Settings()
                .visual(0xEEEEEE, DULL)
                .element(Elements.Mn)
                .metal(2)
                .canCreateTools(7.0F, 2.0f, 512)
                .flags(GENERATE_FOIL)));

        Mercury = register("mercury", new Material(new Material.Settings()
                .visual(0xFFDDDD)
                .element(Elements.Hg)
                .fluid())); //TODO: create unique fluid, something metallic (like molten)

        Molybdenum = register("molybdenum", new Material(new Material.Settings()
                .visual(0xAAAADD, DULL)
                .element(Elements.Mo)
                .metal(2)
                .canCreateTools(7.0F, 2.0f, 512))); //Has ore - but it is not spawned (as this is obtained from Molybdenite)

        Neodymium = register("neodymium", new Material(new Material.Settings()
                .visual(0x777777, METALLIC)
                .element(Elements.Nd)
                .metal(2)
                .canCreateTools(7.0F, 2.0f, 512)
                .smeltsInBlastFurnace(1297)
                .polarizeInto(new Supplier<>() {
                    @Override
                    public Material get() {
                        return MagneticNeodymium;
                    }
                }))); //Has ore; Byproducts: Monazite, RareEarth

        Nickel = register("nickel", new Material(new Material.Settings()
                .visual(0xAAAAFF, METALLIC)
                .element(Elements.Ni)
                .metal(2)
                .canCreateCables(VoltageTier.MV, 3, 3)
                .mortarGrindable()
                .plasma())); //Has ore; Byproducts: Cobalt, Platinum, Iron

        Niobium = register("niobium", new Material(new Material.Settings()
                .visual(0x9486AA, METALLIC)
                .element(Elements.Nb)
                .metal(2)
                .smeltsInBlastFurnace(2750))); //Has ore - but it is not spawned (as this is obtained from Tantalite)

        Nitrogen = register("nitrogen", new Material(new Material.Settings()
                .visual(0x7090AF)
                .element(Elements.N)
                .gas()
                .plasma()));

        Osmium = register("osmium", new Material(new Material.Settings()
                .visual(0x5050FF, METALLIC)
                .element(Elements.Os)
                .metal(4)
                .canCreateTools(16.0F, 4.0f, 1280)
                .canCreateCables(VoltageTier.IV, 4, 2)
                .smeltsInBlastFurnace(3306))); //Has ore; Byproducts: Iridium

        Oxygen = register("oxygen", new Material(new Material.Settings()
                .visual(0x90AAEE)
                .element(Elements.O)
                .gas()
                .plasma()));

        Palladium = register("palladium", new Material(new Material.Settings()
                .visual(0xCED0DD, METALLIC)
                .element(Elements.Pd)
                .metal(2)
                .canCreateTools(8.0f, 2.0f, 512)
                .canCreateCables(VoltageTier.IV, 2, 1)
                .smeltsInBlastFurnace(1228))); //Has ore

        Phosphorus = register("phosphorus", new Material(new Material.Settings()
                .visual(COLOR_DARK_YELLOW, SAND)
                .element(Elements.P)
                .dust(2)));

        Platinum = register("platinum", new Material(new Material.Settings()
                .visual(0xFFFF99, SHINY)
                .element(Elements.Pt)
                .metal(2)
                .canCreateCables(VoltageTier.IV, 2, 1)
                .flags(GENERATE_PLATE, GENERATE_FINE_WIRE))); //Has ore; Byproducts: Nickel, Iridium

        Plutonium = register("plutonium", new Material(new Material.Settings()
                .visual(0xF03232, METALLIC)
                .element(Elements.Pu)
                .metal(3)));

        Potassium = register("pottasium", new Material(new Material.Settings()
                .visual(0xCECECE, METALLIC)
                .element(Elements.K)
                .metal(1)));

        Silicon = register("silicon", new Material(new Material.Settings()
                .visual(0x3C3C50, METALLIC)
                .element(Elements.Si)
                .metal(2)
                .smeltsInBlastFurnace(1687)
                .flags(GENERATE_PLATE, GENERATE_FOIL)));

        Silver = register("silver", new Material(new Material.Settings()
                .visual(0xDCDCFF, SHINY)
                .element(Elements.Ag)
                .metal(2)
                .canCreateCables(VoltageTier.HV, 1, 1)
                .mortarGrindable()
                .flags(GENERATE_PLATE))); //Has ore; Byproducts: Lead, Sulfur

        Sodium = register("sodium", new Material(new Material.Settings()
                .visual(0x000096, METALLIC)
                .element(Elements.Na)
                .metal(2)));

        Sulfur = register("sulfur", new Material(new Material.Settings()
                .visual(COLOR_DARK_YELLOW, SAND)
                .element(Elements.S)
                .flammable()
                .dust(2))); //Has ore

        Tantalum = register("tantalum", new Material(new Material.Settings()
                .visual(COLOR_WHITE, METALLIC)
                .element(Elements.Ta)
                .metal(2)));

        Thorium = register("thorium", new Material(new Material.Settings()
                .visual(0x001E00, SHINY)
                .element(Elements.Th)
                .metal(2)
                .canCreateTools(6.0F, 2.0f, 512))); //Has ore; Byproducts: Uranium, Lead

        Tin = register("tin", new Material(new Material.Settings()
                .visual(COLOR_VERY_LIGHT_GREY, DULL)
                .element(Elements.Sn)
                .metal(1)
                .canCreateCables(VoltageTier.LV, 1, 1)
                .baseForPumpComponent()
                .mortarGrindable()
                .flags(GENERATE_FINE_WIRE, GENERATE_FOIL))); //Has ore; Byproducts: Iron, Zinc

        Titanium = register("titanium", new Material(new Material.Settings()
                .visual(0xDCA0F0, METALLIC)
                .element(Elements.Ti)
                .metal(3)
                .canCreateTools(7.0F, 3.0f, 1600)
                .canCreateCables(VoltageTier.EV, 4, 2)
                .canCreateFluidPipe(200, 5000, true)
                .smeltsInBlastFurnace(1941)
                .baseForElectricComponents()
                .flags(GENERATE_DENSE_PLATE, GENERATE_LONG_ROD, GENERATE_ROTOR, GENERATE_GEAR, GENERATE_SPRING))); //GENERATE_FRAME

        Tungsten = register("tungsten", new Material(new Material.Settings()
                .visual(COLOR_LIGHT_BLACK, METALLIC)
                .element(Elements.W)
                .metal(3)
                .canCreateTools(7.0F, 3.0f, 2560)
                .canCreateCables(VoltageTier.IV, 2, 2)
                .smeltsInBlastFurnace(3000))); //GENERATE_FRAME

        Vanadium = register("vanadium", new Material(new Material.Settings()
                .visual(COLOR_LIGHT_BLACK, METALLIC)
                .element(Elements.V)
                .metal(2)
                .smeltsInBlastFurnace(2183)));

        Yttrium = register("yttrium", new Material(new Material.Settings()
                .visual(0xDCFADC, METALLIC)
                .element(Elements.Y)
                .metal(2)
                .smeltsInBlastFurnace(1799)));

        Zinc = register("zinc", new Material(new Material.Settings()
                .visual(0xFAF0F0, METALLIC)
                .element(Elements.Zn)
                .metal(1)
                .canCreateCables(VoltageTier.LV, 1, 1)
                .mortarGrindable()
                .flags(GENERATE_FOIL))); //Has ore; Byproducts: Tin, Gallium

        //Compounds

        AnnealedCopper = register("annealed_copper", new Material(new Material.Settings()
                .visual(0xFF7814, SHINY)
                .composition(MaterialComponent.of(Copper))
                .metal(1)
                .canCreateCables(VoltageTier.MV, 1, 1)
                .mortarGrindable()
                .flags(GENERATE_FINE_WIRE)));

        Brass = register("brass", new Material(new Material.Settings()
                .visual(0xFFB400, METALLIC)
                .composition(MaterialComponent.of(Zinc), MaterialComponent.of(Copper))
                .metal(1)
                .canCreateTools(8.0F, 3.0f, 152)
                .mortarGrindable()));

        Bronze = register("bronze", new Material(new Material.Settings()
                .visual(COLOR_ORANGE, DULL)
                .composition(MaterialComponent.of(Tin), MaterialComponent.of(Copper, 3))
                .metal(2)
                .canCreateTools(6.0F, 2.5f, 192)
                .canCreateFluidPipe(35, 2000, true)
                .mortarGrindable()
                .baseForPumpComponent()));

        Charcoal = register("charcoal", new Material(new Material.Settings()
                .visual(0x644646, LIGNITE)
                .composition(MaterialComponent.of(Carbon))
                .gem(1, false, false)
                .flammable(1600)
                .mortarGrindable()));

        Coal = register("coal", new Material(new Material.Settings()
                .visual(0x464646, LIGNITE)
                .composition(MaterialComponent.of(Carbon))
                .gem(1, false, false)
                .flammable(1600)
                .mortarGrindable()));//Has Ore; Byproducts: Lignite, Thorium

        CobaltBrass = register("cobalt_brass", new Material(new Material.Settings()
                .visual(0xB4B4A0, METALLIC)
                .composition(MaterialComponent.of(Brass, 7), MaterialComponent.of(Aluminium), MaterialComponent.of(Cobalt))
                .metal(2)
                .canCreateTools(8.0F, 2.0f, 256)
                .flags(GENERATE_GEAR)));

        Diamond = register("diamond", new Material(new Material.Settings()
                .visual(0xC8FFFF, DIAMOND)
                .composition(MaterialComponent.of(Carbon))
                .gem(3, false, true)
                .canCreateToolsWithDefaultEnchant(8.0F, 3.0f, 1280, 10)
                .flags(GENERATE_LENS))); //Has ore; Byproducts: Graphite

        Electrum = register("electrum", new Material(new Material.Settings()
                .visual(0xFFFF64, SHINY)
                .composition(MaterialComponent.of(Silver), MaterialComponent.of(Gold))
                .metal(2)
                .canCreateCables(VoltageTier.HV, 3, 2)
                .mortarGrindable()
                .flags(GENERATE_ROD, GENERATE_FOIL, GENERATE_FINE_WIRE)));

        Emerald = register("emerald", new Material(new Material.Settings()
                .visual(0x50FF50, EMERALD)
                .composition(MaterialComponent.of(Beryllium, 3), MaterialComponent.of(Aluminium, 2), MaterialComponent.of(Silicon, 6), MaterialComponent.of(Oxygen, 18))
                .gem(2, false, true)
                .canCreateTools(10.0F, 2.0f, 368)
                .flags(GENERATE_LENS)));// Has ore; Byproducts: Beryllium, Aluminium

        Graphite = register("graphite", new Material(new Material.Settings()
                .visual(COLOR_DARK_GREY, DULL)
                .composition(MaterialComponent.of(Carbon))
                .metal(2)
                .flammable())); //Has ore; Byproducts: Carbon

        Invar = register("invar", new Material(new Material.Settings()
                .visual(0xB4B478, METALLIC)
                .composition(MaterialComponent.of(Iron, 2), MaterialComponent.of(Nickel))
                .metal(2)
                .canCreateToolsWithDefaultEnchant(7.0F, 3.0f, 512, 14, new EnchantmentData(BANE_OF_ARTHROPODS, 3))
                .mortarGrindable())); //GENERATE_FRAME

        MagneticIron = register("magnetic_iron", new Material(new Material.Settings()
                .visual(COLOR_LIGHT_GREY, MAGNETIC)
                .composition(MaterialComponent.of(Iron))
                .metal(2)
                .demagnetizeInto(()-> Iron)
                .smeltsInArcFurnace(new Supplier<>() {
                    @Override
                    public Material get() {
                        return WroughtIron;
                    }
                })
                .flags(GENERATE_ROD)));

        MagneticNeodymium = register("magnetic_neodymium", new Material(new Material.Settings()
                .visual(COLOR_VERY_DARK_GREY, MAGNETIC)
                .composition(MaterialComponent.of(Neodymium))
                .metal(2)
                .demagnetizeInto(()-> Neodymium)
                .flags(GENERATE_ROD)));

        MagneticSteel = register("magnetic_steel", new Material(new Material.Settings()
                .visual(COLOR_DARK_GREY, MAGNETIC)
                .composition(MaterialComponent.of(Steel))
                .metal(2)
                .smeltsInBlastFurnace(1000)
                .demagnetizeInto(() -> Steel)
                .flags(GENERATE_ROD)));

        NetherQuartz = register("nether_quartz", new Material(new Material.Settings()
                .visual(0xE6D2D2, QUARTZ)
                .gem(1, true, false))); //Has ore; OreMultiplier=2; Byproducts: Netherrack

        Paper = register("paper", new Material(new Material.Settings()
                .visual(COLOR_WHITE, PAPER)
                .polymer(1)
                .mortarGrindable()
                .flammable()
                .flags(GENERATE_PLATE, GENERATE_RING)));

        RawRubber = register("raw_rubber", new Material(new Material.Settings()
                .visual(0xCCC789, SAND)
                .composition(MaterialComponent.of(Carbon, 5), MaterialComponent.of(Hydrogen, 8))
                .dust(1))); //DISABLE_DECOMPOSITION

        Rubber = register("rubber", new Material(new Material.Settings()
                .visual(0x151515, ROUGH)
                .composition(MaterialComponent.of(Carbon, 5), MaterialComponent.of(Hydrogen, 8))
                .polymer(1)
                .property(FLUID_PROPERTIES, FluidProperties.create(new MaterialFluidProperties(new MaterialFluidProperties.Settings().temperature(200)), MaterialFluidTexture.DEFAULT_FLUID)) //TODO: create texture for rubber, tweak values
                .flammable()
                .flags(GENERATE_PLATE, GENERATE_RING)));

        RedAlloy = register("red_alloy", new Material(new Material.Settings()
                .visual(COLOR_DARK_RED, DULL)
                .composition(MaterialComponent.of(Copper), MaterialComponent.of(Redstone))
                .metal(1)
                .canCreateCables(VoltageTier.ULV, 1, 0)
                .flags(GENERATE_FINE_WIRE)));

        Steel = register("steel", new Material(new Material.Settings()
                .visual(0x505050, DULL)
                .composition(MaterialComponent.of(Iron))
                .metal(2)
                .smeltsInBlastFurnace(1000)
                .canCreateTools(6, 3, 512)
                .canCreateCables(VoltageTier.EV, 2, 2)
                .canCreateFluidPipe(50, 2500, true)
                .canCreateCell(256000)
                .mortarGrindable()
                .baseForPumpComponent()
                .baseForElectricComponents()
                .baseForElectricToolHeads()
                .polarizeInto(() ->MagneticSteel)));

        StainlessSteel = register("stainless_steel", new Material(new Material.Settings()
                .visual(0xC8C8DC, SHINY)
                .composition(MaterialComponent.of(Iron, 6), MaterialComponent.of(Chrome), MaterialComponent.of(Manganese), MaterialComponent.of(Nickel))
                .metal(2)
                .smeltsInBlastFurnace(1700)
                .canCreateTools(7.0F, 4.0f, 480)
                .canCreateFluidPipe(100, 3000, true)
                .baseForElectricComponents()
                .baseForPumpComponent()
                .flags(GENERATE_GEAR)));

        Quartzite = register("quartzite", new Material(new Material.Settings()
                .visual(0xD2E6D2, QUARTZ)
                .gem(1, true, false))); //Has ore; OreMultiplier=2; Byproducts: CertusQuartz, Barite

        Water = register("water", new Material(new Material.Settings()
                .visual(COLOR_BLUE)
                .composition(MaterialComponent.of(Hydrogen, 2), MaterialComponent.of(Oxygen))
                .fluid())); //DISABLE_DECOMPOSITION, NO_RECYCLING

        WroughtIron = register("wrought_iron", new Material(new Material.Settings()
                .visual(0xC8B4B4, METALLIC)
                .composition(MaterialComponent.of(Iron))
                .metal(2)
                .canCreateTools(6.0F, 3.5f, 384)
                .mortarGrindable()
                .flags(GENERATE_RING))); //Ring used only for Tripwire Hook
    }

    /**
     * Direct Elements
     */
    public static IngotMaterial Caesium = new IngotMaterial(10, "caesium", 0xFFFFFC, DULL, 2, of(), 0, Element.Cs); //Only available as byproduct; No usage
    public static IngotMaterial Cerium = new IngotMaterial(14, "cerium", 0xEEEEEE, METALLIC, 2, of(), 0, Element.Ce, 1068); //Only available via electrolyzing; No usage
    public static FluidMaterial Deuterium = new FluidMaterial(19, "deuterium", 0xEEEE00, FLUID, of(), STATE_GAS, Element.D); //Only used to obtain Tritium
    public static FluidMaterial Helium3 = new FluidMaterial(30, "helium3", 0xDDDD00, GAS, of(), STATE_GAS, Element.He_3); //No usage
    public static IngotMaterial Darmstadtium = new IngotMaterial(43, "darmstadtium", 0xAAAAAA, METALLIC, 6, of(), EXT2_METAL | GENERATE_RING | GENERATE_ROTOR | GENERATE_GEAR_SMALL | GENERATE_ROD_LONG | GENERATE_FRAME, Element.Ds, 24.0F, 6.0f, 155360); //Unobtainable; Used only for JEI default tools
    public static FluidMaterial Radon = new FluidMaterial(57, "radon", 0xFF00FF, FLUID, of(), STATE_GAS, Element.Rn); //No usage; Unobtainable
    public static IngotMaterial Uranium = new IngotMaterial(75, "uranium", 0x32F032, METALLIC, 3, of(), STD_METAL | GENERATE_ORE, Element.U, 6.0F, 3.0f, 512); //Spawns in world; Has tools (seems wrong); Centrifuges in small piles of Plutonium & Uranium 235
    public static IngotMaterial Uranium235 = new IngotMaterial(76, "uranium235", 0x46FA46, SHINY, 3, of(), STD_METAL | GENERATE_ORE | GENERATE_ROD, Element.U_235, 6.0F, 3.0f, 512); //Spawns in world; Has tools (seems wrong)

    /**
     * First Degree Compounds
     */
    public static FluidMaterial Methane = new FluidMaterial(80, "methane", 0xFFFFFF, FLUID, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Hydrogen, 4)), 0);
    public static FluidMaterial CarbonDioxide = new FluidMaterial(81, "carbon_dioxide", 0xA9D0F5, FLUID, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Oxygen, 2)), 0);
    public static FluidMaterial NobleGases = new FluidMaterial(82, "noble_gases", 0xA9D0F5, FLUID, of(new MaterialComponent(CarbonDioxide, 25), new MaterialComponent(Helium, 11), new MaterialComponent(Methane, 4), new MaterialComponent(Deuterium, 2), new MaterialComponent(Radon, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Air = new FluidMaterial(83, "air", 0xA9D0F5, FLUID, of(new MaterialComponent(Nitrogen, 40), new MaterialComponent(Oxygen, 11), new MaterialComponent(Argon, 1), new MaterialComponent(NobleGases, 1)), STATE_GAS | DISABLE_DECOMPOSITION);
    public static FluidMaterial LiquidAir = new FluidMaterial(84, "liquid_air", 0xA9D0F5, FLUID, of(new MaterialComponent(Nitrogen, 40), new MaterialComponent(Oxygen, 11), new MaterialComponent(Argon, 1), new MaterialComponent(NobleGases, 1)), STATE_GAS | DISABLE_DECOMPOSITION);
    public static GemMaterial Almandine = new GemMaterial(85, "almandine", 0xFF0000, GEM_VERTICAL, 1, of(new MaterialComponent(Aluminium, 2), new MaterialComponent(Iron, 3), new MaterialComponent(Silicon, 3), new MaterialComponent(Oxygen, 12)), STD_GEM);
    public static DustMaterial Andradite = new DustMaterial(86, "andradite", 0x967800, GEM_VERTICAL, 1, of(new MaterialComponent(Calcium, 3), new MaterialComponent(Iron, 2), new MaterialComponent(Silicon, 3), new MaterialComponent(Oxygen, 12)), 0); //Only available as byproduct; Only used for electrolyzing
    public static DustMaterial Asbestos = new DustMaterial(88, "asbestos", 0xE6E6E6, SAND, 1, of(new MaterialComponent(Magnesium, 3), new MaterialComponent(Silicon, 2), new MaterialComponent(Hydrogen, 4), new MaterialComponent(Oxygen, 9)), 0); //Only available after electrolyzing; Only used for electrolyzing
    public static DustMaterial Ash = new DustMaterial(89, "ash", 0x969696, SAND, 1, of(new MaterialComponent(Carbon, 1)), DISABLE_DECOMPOSITION);
    public static DustMaterial BandedIron = new DustMaterial(90, "banded_iron", 0x915A5A, DULL, 2, of(new MaterialComponent(Iron, 2), new MaterialComponent(Oxygen, 3)), GENERATE_ORE);
    public static IngotMaterial BatteryAlloy = new IngotMaterial(91, "battery_alloy", 0x9C7CA0, DULL, 1, of(new MaterialComponent(Lead, 4), new MaterialComponent(Antimony, 1)), EXT_METAL);
    public static GemMaterial BlueTopaz = new GemMaterial(92, "blue_topaz", COLOR_BLUE, GEM_HORIZONTAL, 3, of(new MaterialComponent(Aluminium, 2), new MaterialComponent(Silicon, 1), new MaterialComponent(Fluorine, 2), new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 6)), STD_GEM | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT, 7.0F, 3.0f, 256);
    public static DustMaterial Bone = new DustMaterial(93, "bone", 0xFFFFFF, ROUGH, 1, of(new MaterialComponent(Calcium, 1)), MORTAR_GRINDABLE | EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES);
    public static DustMaterial BrownLimonite = new DustMaterial(96, "brown_limonite", 0xC86400, METALLIC, 1, of(new MaterialComponent(Iron, 1), new MaterialComponent(Hydrogen, 1), new MaterialComponent(Oxygen, 2)), GENERATE_ORE);
    public static DustMaterial Calcite = new DustMaterial(97, "calcite", 0xFAE6DC, DULL, 1, of(new MaterialComponent(Calcium, 1), new MaterialComponent(Carbon, 1), new MaterialComponent(Oxygen, 3)), GENERATE_ORE);
    public static DustMaterial Cassiterite = new DustMaterial(98, "cassiterite", COLOR_VERY_LIGHT_GREY, METALLIC, 1, of(new MaterialComponent(Tin, 1), new MaterialComponent(Oxygen, 2)), GENERATE_ORE);
    public static DustMaterial Chalcopyrite = new DustMaterial(100, "chalcopyrite", 0xA07828, DULL, 1, of(new MaterialComponent(Copper, 1), new MaterialComponent(Iron, 1), new MaterialComponent(Sulfur, 2)), GENERATE_ORE | INDUCTION_SMELTING_LOW_OUTPUT);
    public static DustMaterial Chromite = new DustMaterial(102, "chromite", 0x23140F, METALLIC, 1, of(new MaterialComponent(Iron, 1), new MaterialComponent(Chrome, 2), new MaterialComponent(Oxygen, 4)), GENERATE_ORE, null);
    public static GemMaterial Cinnabar = new GemMaterial(103, "cinnabar", 0x960000, EMERALD, 1, of(new MaterialComponent(Mercury, 1), new MaterialComponent(Sulfur, 1)), GENERATE_ORE | CRYSTALLISABLE);
    public static DustMaterial Clay = new DustMaterial(105, "clay", 0xC8C8DC, ROUGH, 1, of(new MaterialComponent(Sodium, 2), new MaterialComponent(Lithium, 1), new MaterialComponent(Aluminium, 2), new MaterialComponent(Silicon, 2), new MaterialComponent(Water, 6)), MORTAR_GRINDABLE);
    public static DustMaterial Cobaltite = new DustMaterial(107, "cobaltite", 0x5050FA, ROUGH, 1, of(new MaterialComponent(Cobalt, 1), new MaterialComponent(Arsenic, 1), new MaterialComponent(Sulfur, 1)), GENERATE_ORE);
    public static DustMaterial Cooperite = new DustMaterial(108, "cooperite", 0xFFFFC8, METALLIC, 1, of(new MaterialComponent(Platinum, 3), new MaterialComponent(Nickel, 1), new MaterialComponent(Sulfur, 1), new MaterialComponent(Palladium, 1)), GENERATE_ORE);
    public static IngotMaterial Cupronickel = new IngotMaterial(109, "cupronickel", 0xE39680, METALLIC, 1, of(new MaterialComponent(Copper, 1), new MaterialComponent(Nickel, 1)), EXT_METAL);
    public static DustMaterial DarkAsh = new DustMaterial(110, "dark_ash", COLOR_LIGHT_BLACK, SAND, 1, of(new MaterialComponent(Carbon, 1)), DISABLE_DECOMPOSITION);
    public static DustMaterial Galena = new DustMaterial(114, "galena", 0x643C64, ROUGH, 3, of(new MaterialComponent(Lead, 3), new MaterialComponent(Silver, 3), new MaterialComponent(Sulfur, 2)), GENERATE_ORE | NO_SMELTING);
    public static DustMaterial Garnierite = new DustMaterial(115, "garnierite", 0x32C846, ROUGH, 3, of(new MaterialComponent(Nickel, 1), new MaterialComponent(Oxygen, 1)), GENERATE_ORE);
    public static FluidMaterial Glyceryl = new FluidMaterial(116, "glyceryl", 0x009696, FLUID, of(new MaterialComponent(Carbon, 3), new MaterialComponent(Hydrogen, 5), new MaterialComponent(Nitrogen, 3), new MaterialComponent(Oxygen, 9)), FLAMMABLE | EXPLOSIVE | NO_SMELTING | NO_SMASHING);
    public static GemMaterial GreenSapphire = new GemMaterial(117, "green_sapphire", 0x64C882, GEM_HORIZONTAL, 2, of(new MaterialComponent(Aluminium, 2), new MaterialComponent(Oxygen, 3)), GENERATE_ORE | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT | GENERATE_LENS, 8.0F, 3.0f, 368);
    public static DustMaterial Grossular = new DustMaterial(118, "grossular", 0xC86400, GEM_VERTICAL, 1, of(new MaterialComponent(Calcium, 3), new MaterialComponent(Aluminium, 2), new MaterialComponent(Silicon, 3), new MaterialComponent(Oxygen, 12)), GENERATE_ORE);
    public static Material DistilledWater = new FluidMaterial(119, "distilled_water", COLOR_BLUE, FLUID, of(new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 1)), NO_RECYCLING | DISABLE_DECOMPOSITION);
    public static DustMaterial Ice = new DustMaterial(120, "ice", 0xC8C8FF, ROUGH, 0, of(new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 1)), NO_SMASHING | NO_RECYCLING | SMELT_INTO_FLUID | EXCLUDE_BLOCK_CRAFTING_RECIPES | DISABLE_DECOMPOSITION);
    public static DustMaterial Ilmenite = new DustMaterial(121, "ilmenite", 0x463732, ROUGH, 3, of(new MaterialComponent(Iron, 1), new MaterialComponent(Titanium, 1), new MaterialComponent(Oxygen, 3)), GENERATE_ORE | DISABLE_DECOMPOSITION);
    public static GemMaterial Rutile = new GemMaterial(122, "rutile", 0xD40D5C, GEM_HORIZONTAL, 2, of(new MaterialComponent(Titanium, 1), new MaterialComponent(Oxygen, 2)), STD_GEM | DISABLE_DECOMPOSITION);
    public static DustMaterial Bauxite = new DustMaterial(123, "bauxite", 0xC86400, ROUGH, 1, of(new MaterialComponent(Rutile, 2), new MaterialComponent(Aluminium, 16), new MaterialComponent(Hydrogen, 10), new MaterialComponent(Oxygen, 11)), GENERATE_ORE | DISABLE_DECOMPOSITION);
    public static FluidMaterial TitaniumTetrachloride = new FluidMaterial(124, "titanium_tetrachloride", 0xD40D5C, FLUID, of(new MaterialComponent(Titanium, 1), new MaterialComponent(Chlorine, 4)), DISABLE_DECOMPOSITION).setFluidTemperature(2200);
    public static DustMaterial MagnesiumChloride = new DustMaterial(125, "magnesium_chloride", 0xD40D5C, ROUGH, 2, of(new MaterialComponent(Magnesium, 1), new MaterialComponent(Chlorine, 2)), 0);
    public static IngotMaterial Kanthal = new IngotMaterial(127, "kanthal", 0xC2D2DF, METALLIC, 2, of(new MaterialComponent(Iron, 1), new MaterialComponent(Aluminium, 1), new MaterialComponent(Chrome, 1)), EXT_METAL, null, 1800);
    public static GemMaterial Lazurite = new GemMaterial(128, "lazurite", 0x6478FF, LAPIS, 1, of(new MaterialComponent(Aluminium, 6), new MaterialComponent(Silicon, 6), new MaterialComponent(Calcium, 8), new MaterialComponent(Sodium, 8)), GENERATE_PLATE | GENERATE_ORE | NO_SMASHING | NO_SMELTING | CRYSTALLISABLE | GENERATE_ROD | DECOMPOSITION_BY_ELECTROLYZING);
    public static IngotMaterial Magnalium = new IngotMaterial(129, "magnalium", 0xC8BEFF, DULL, 2, of(new MaterialComponent(Magnesium, 1), new MaterialComponent(Aluminium, 2)), EXT2_METAL | GENERATE_ROD_LONG, 6.0F, 2.0f, 256); //Only used as tools and turbine
    public static DustMaterial Magnesite = new DustMaterial(130, "magnesite", 0xFAFAB4, METALLIC, 2, of(new MaterialComponent(Magnesium, 1), new MaterialComponent(Carbon, 1), new MaterialComponent(Oxygen, 3)), GENERATE_ORE);
    public static DustMaterial Magnetite = new DustMaterial(131, "magnetite", 0x1E1E1E, METALLIC, 2, of(new MaterialComponent(Iron, 3), new MaterialComponent(Oxygen, 4)), GENERATE_ORE);
    public static DustMaterial Molybdenite = new DustMaterial(132, "molybdenite", 0x191919, METALLIC, 2, of(new MaterialComponent(Molybdenum, 1), new MaterialComponent(Sulfur, 2)), GENERATE_ORE); //Only used for getting molybdenum
    public static IngotMaterial Nichrome = new IngotMaterial(133, "nichrome", 0xCDCEF6, METALLIC, 2, of(new MaterialComponent(Nickel, 4), new MaterialComponent(Chrome, 1)), EXT_METAL, null, 2700);
    public static IngotMaterial NiobiumTitanium = new IngotMaterial(135, "niobium_titanium", 0x1D1D29, DULL, 2, of(new MaterialComponent(Niobium, 1), new MaterialComponent(Titanium, 1)), EXT2_METAL, null, 4500);
    public static FluidMaterial NitrogenDioxide = new FluidMaterial(137, "nitrogen_dioxide", 0x64AFFF, FLUID, of(new MaterialComponent(Nitrogen, 1), new MaterialComponent(Oxygen, 2)), 0);
    public static DustMaterial Obsidian = new DustMaterial(138, "obsidian", 0x503264, DULL, 3, of(new MaterialComponent(Magnesium, 1), new MaterialComponent(Iron, 1), new MaterialComponent(Silicon, 2), new MaterialComponent(Oxygen, 8)), NO_SMASHING | EXCLUDE_BLOCK_CRAFTING_RECIPES);
    public static DustMaterial Phosphate = new DustMaterial(139, "phosphate", COLOR_YELLOW, ROUGH, 1, of(new MaterialComponent(Phosphorus, 1), new MaterialComponent(Oxygen, 4)), GENERATE_ORE | NO_SMASHING | NO_SMELTING | FLAMMABLE | EXPLOSIVE);
    public static IngotMaterial PigIron = new IngotMaterial(140, "pig_iron", 0xC8B4B4, METALLIC, 2, of(new MaterialComponent(Iron, 1)), EXT_METAL | GENERATE_RING, 6.0F, 4.0f, 384); //Unobtainable; Used only in tools (inferior to Iron in damage and durability)
    public static IngotMaterial Plastic = new IngotMaterial(141, "plastic", COLOR_LIGHT_GREY, DULL, 1, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Hydrogen, 2)), GENERATE_PLATE | FLAMMABLE | NO_SMASHING | SMELT_INTO_FLUID | DISABLE_DECOMPOSITION);
    public static IngotMaterial Epoxid = new IngotMaterial(142, "epoxid", 0xC88C14, DULL, 1, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 4), new MaterialComponent(Oxygen, 1)), EXT2_METAL | DISABLE_DECOMPOSITION);
    public static DustMaterial Silicone = new DustMaterial(143, "silicone", COLOR_VERY_LIGHT_GREY, DULL, 1, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Hydrogen, 1), new MaterialComponent(Silicon, 2), new MaterialComponent(Oxygen, 1)), GENERATE_PLATE | FLAMMABLE | NO_SMASHING | SMELT_INTO_FLUID | DISABLE_DECOMPOSITION);
    public static IngotMaterial Polycaprolactam = new IngotMaterial(144, "polycaprolactam", COLOR_LIGHT_BLACK, DULL, 1, of(new MaterialComponent(Carbon, 6), new MaterialComponent(Hydrogen, 11), new MaterialComponent(Nitrogen, 1), new MaterialComponent(Oxygen, 1)), GENERATE_PLATE | DISABLE_DECOMPOSITION); //Created from Naptha and Saltpeter Used only to produce Strings
    public static IngotMaterial Polytetrafluoroethylene = new IngotMaterial(145, "polytetrafluoroethylene", COLOR_VERY_DARK_GREY, DULL, 1, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Fluorine, 4)), GENERATE_PLATE | SMELT_INTO_FLUID | NO_WORKING | DISABLE_DECOMPOSITION);
    public static DustMaterial Powellite = new DustMaterial(146, "powellite", COLOR_YELLOW, ROUGH, 2, of(new MaterialComponent(Calcium, 1), new MaterialComponent(Molybdenum, 1), new MaterialComponent(Oxygen, 4)), GENERATE_ORE); //Spawns in world; Only used for electrolyzing
    public static DustMaterial Pyrite = new DustMaterial(148, "pyrite", 0x967828, ROUGH, 1, of(new MaterialComponent(Iron, 1), new MaterialComponent(Sulfur, 2)), GENERATE_ORE | INDUCTION_SMELTING_LOW_OUTPUT);
    public static DustMaterial Pyrolusite = new DustMaterial(149, "pyrolusite", 0x9696AA, ROUGH, 2, of(new MaterialComponent(Manganese, 1), new MaterialComponent(Oxygen, 2)), GENERATE_ORE);
    public static DustMaterial Pyrope = new DustMaterial(150, "pyrope", 0x783264, ROUGH, 2, of(new MaterialComponent(Aluminium, 2), new MaterialComponent(Magnesium, 3), new MaterialComponent(Silicon, 3), new MaterialComponent(Oxygen, 12)), GENERATE_ORE);
    public static DustMaterial RockSalt = new DustMaterial(151, "rock_salt", 0xF0C8C8, FINE, 1, of(new MaterialComponent(Potassium, 1), new MaterialComponent(Chlorine, 1)), GENERATE_ORE | NO_SMASHING);
    public static GemMaterial Ruby = new GemMaterial(154, "ruby", 0xBD4949, RUBY, 2, of(new MaterialComponent(Chrome, 1), new MaterialComponent(Aluminium, 2), new MaterialComponent(Oxygen, 3)), STD_GEM | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT, 8.5F, 3.0f, 256);
    public static DustMaterial Salt = new DustMaterial(155, "salt", 0xFFFFFF, SAND, 1, of(new MaterialComponent(Sodium, 1), new MaterialComponent(Chlorine, 1)), GENERATE_ORE | NO_SMASHING);
    public static DustMaterial Saltpeter = new DustMaterial(156, "saltpeter", 0xE6E6E6, FINE, 1, of(new MaterialComponent(Potassium, 1), new MaterialComponent(Nitrogen, 1), new MaterialComponent(Oxygen, 3)), GENERATE_ORE | NO_SMASHING | NO_SMELTING | FLAMMABLE);
    public static GemMaterial Sapphire = new GemMaterial(157, "sapphire", 0x6464C8, GEM_VERTICAL, 2, of(new MaterialComponent(Aluminium, 2), new MaterialComponent(Oxygen, 3)), STD_GEM | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT, null, 7.5F, 4.0f, 256);
    public static DustMaterial Scheelite = new DustMaterial(158, "scheelite", 0xC88C14, DULL, 3, of(new MaterialComponent(Tungsten, 1), new MaterialComponent(Calcium, 2), new MaterialComponent(Oxygen, 4)), GENERATE_ORE | DECOMPOSITION_REQUIRES_HYDROGEN);
    public static DustMaterial SiliconDioxide = new DustMaterial(159, "silicon_dioxide", COLOR_LIGHT_GREY, QUARTZ, 1, of(new MaterialComponent(Silicon, 1), new MaterialComponent(Oxygen, 2)), NO_SMASHING | NO_SMELTING | CRYSTALLISABLE); //Only available after electrolyzing; Only use in electrolyzing - easy source of Silicon (available in LV, though you can't use silicon in LV)
    public static GemMaterial Sodalite = new GemMaterial(161, "sodalite", 0x1414FF, LAPIS, 1, of(new MaterialComponent(Aluminium, 3), new MaterialComponent(Silicon, 3), new MaterialComponent(Sodium, 4), new MaterialComponent(Chlorine, 1)), GENERATE_ORE | GENERATE_PLATE | GENERATE_ROD | NO_SMASHING | NO_SMELTING | CRYSTALLISABLE | GENERATE_ROD | DECOMPOSITION_BY_ELECTROLYZING);
    public static FluidMaterial SodiumPersulfate = new FluidMaterial(162, "sodium_persulfate", 0xFFFFFF, FLUID, of(new MaterialComponent(Sodium, 2), new MaterialComponent(Sulfur, 2), new MaterialComponent(Oxygen, 8)), 0);
    public static DustMaterial SodiumSulfide = new DustMaterial(163, "sodium_sulfide", 0xAAAA00, DULL, 1, of(new MaterialComponent(Sodium, 2), new MaterialComponent(Sulfur, 1)), 0);
    public static FluidMaterial HydrogenSulfide = new FluidMaterial(164, "hydrogen_sulfide", 0xFFFFFF, FLUID, of(new MaterialComponent(Hydrogen, 2), new MaterialComponent(Sulfur, 1)), 0);
    public static FluidMaterial Steam = new FluidMaterial(346, "steam", 0xFFFFFF, GAS, of(new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 1)), NO_RECYCLING | GENERATE_FLUID_BLOCK | DISABLE_DECOMPOSITION).setFluidTemperature(380);
    public static FluidMaterial Epichlorhydrin = new FluidMaterial(349, "epichlorhydrin", 0xFFFFFF, FLUID, of(new MaterialComponent(Carbon, 3), new MaterialComponent(Hydrogen, 5), new MaterialComponent(Chlorine, 1), new MaterialComponent(Oxygen, 1)), 0);
    public static FluidMaterial NitricAcid = new FluidMaterial(351, "nitric_acid", 0xCCCC00, FLUID, of(new MaterialComponent(Hydrogen, 1), new MaterialComponent(Nitrogen, 1), new MaterialComponent(Oxygen, 3)), 0);
    public static GemMaterial Coke = new GemMaterial(357, "coke", 0x666666, LIGNITE, 1, of(new MaterialComponent(Carbon, 1)), FLAMMABLE | NO_SMELTING | NO_SMASHING | MORTAR_GRINDABLE);

    public static Material SolderingAlloy = new IngotMaterial(180, "soldering_alloy", 0xDCDCE6, DULL, 1, of(new MaterialComponent(Tin, 9), new MaterialComponent(Antimony, 1)), EXT_METAL | GENERATE_FINE_WIRE, null);
    public static DustMaterial Spessartine = new DustMaterial(181, "spessartine", 0xFF6464, GEM_VERTICAL, 2, of(new MaterialComponent(Aluminium, 2), new MaterialComponent(Manganese, 3), new MaterialComponent(Silicon, 3), new MaterialComponent(Oxygen, 12)), GENERATE_ORE);
    public static DustMaterial Sphalerite = new DustMaterial(182, "sphalerite", 0xFFFFFF, ROUGH, 1, of(new MaterialComponent(Zinc, 1), new MaterialComponent(Sulfur, 1)), GENERATE_ORE | INDUCTION_SMELTING_LOW_OUTPUT | DISABLE_DECOMPOSITION);
    public static DustMaterial Stibnite = new DustMaterial(185, "stibnite", 0x464646, ROUGH, 2, of(new MaterialComponent(Antimony, 2), new MaterialComponent(Sulfur, 3)), GENERATE_ORE);
    public static FluidMaterial SulfuricAcid = new FluidMaterial(186, "sulfuric_acid", COLOR_ORANGE, FLUID, of(new MaterialComponent(Hydrogen, 2), new MaterialComponent(Sulfur, 1), new MaterialComponent(Oxygen, 4)), 0);
    public static GemMaterial Tanzanite = new GemMaterial(187, "tanzanite", 0x4000C8, GEM_VERTICAL, 2, of(new MaterialComponent(Calcium, 2), new MaterialComponent(Aluminium, 3), new MaterialComponent(Silicon, 3), new MaterialComponent(Hydrogen, 1), new MaterialComponent(Oxygen, 13)), EXT_METAL | GENERATE_ORE | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT, null, 7.0F, 2.0f, 256); //Has ore, does not spawn use only in tools and electrolyzing
    public static DustMaterial Tetrahedrite = new DustMaterial(188, "tetrahedrite", 0xC82000, ROUGH, 2, of(new MaterialComponent(Copper, 3), new MaterialComponent(Antimony, 1), new MaterialComponent(Sulfur, 3), new MaterialComponent(Iron, 1)), GENERATE_ORE | INDUCTION_SMELTING_LOW_OUTPUT);
    public static GemMaterial Topaz = new GemMaterial(190, "topaz", COLOR_ORANGE, GEM_HORIZONTAL, 3, of(new MaterialComponent(Aluminium, 2), new MaterialComponent(Silicon, 1), new MaterialComponent(Fluorine, 2), new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 6)), STD_GEM | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT, null, 7.0F, 2.0f, 256);
    public static DustMaterial Tungstate = new DustMaterial(191, "tungstate", 0x373223, DULL, 3, of(new MaterialComponent(Tungsten, 1), new MaterialComponent(Lithium, 2), new MaterialComponent(Oxygen, 4)), GENERATE_ORE | DECOMPOSITION_REQUIRES_HYDROGEN, null);
    public static IngotMaterial Ultimet = new IngotMaterial(192, "ultimet", 0xB4B4E6, SHINY, 4, of(new MaterialComponent(Cobalt, 5), new MaterialComponent(Chrome, 2), new MaterialComponent(Nickel, 1), new MaterialComponent(Molybdenum, 1)), EXT2_METAL, null, 9.0F, 4.0f, 2048, 2700); //Used only as tool
    public static DustMaterial Uraninite = new DustMaterial(193, "uraninite", 0x232323, ROUGH, 3, of(new MaterialComponent(Uranium, 1), new MaterialComponent(Oxygen, 2)), GENERATE_ORE | DISABLE_DECOMPOSITION);
    public static DustMaterial Uvarovite = new DustMaterial(194, "uvarovite", 0xB4FFB4, GEM_VERTICAL, 2, of(new MaterialComponent(Calcium, 3), new MaterialComponent(Chrome, 2), new MaterialComponent(Silicon, 3), new MaterialComponent(Oxygen, 12)), 0); //Only obtainable by electrolyzing; Only used in electrolyzing
    public static IngotMaterial VanadiumGallium = new IngotMaterial(195, "vanadium_gallium", 0x80808C, SHINY, 2, of(new MaterialComponent(Vanadium, 3), new MaterialComponent(Gallium, 1)), STD_METAL | GENERATE_FOIL | GENERATE_ROD, null, 4500); //Used only as cables
    public static DustMaterial Wulfenite = new DustMaterial(198, "wulfenite", COLOR_ORANGE, DULL, 3, of(new MaterialComponent(Lead, 1), new MaterialComponent(Molybdenum, 1), new MaterialComponent(Oxygen, 4)), GENERATE_ORE); //Spawns in world; Only used for electrolyzing
    public static DustMaterial YellowLimonite = new DustMaterial(199, "yellow_limonite", COLOR_DARK_YELLOW, METALLIC, 2, of(new MaterialComponent(Iron, 1), new MaterialComponent(Hydrogen, 1), new MaterialComponent(Oxygen, 2)), GENERATE_ORE | INDUCTION_SMELTING_LOW_OUTPUT);
    public static IngotMaterial YttriumBariumCuprate = new IngotMaterial(200, "yttrium_barium_cuprate", 0x504046, METALLIC, 2, of(new MaterialComponent(Yttrium, 1), new MaterialComponent(Barium, 2), new MaterialComponent(Copper, 3), new MaterialComponent(Oxygen, 7)), EXT_METAL | GENERATE_FOIL | GENERATE_FINE_WIRE, null, 4500);
    public static GemMaterial CertusQuartz = new GemMaterial(202, "certus_quartz", 0xD2D2E6, QUARTZ, 1, of(), STD_SOLID | NO_SMELTING | CRYSTALLISABLE | GENERATE_ORE);
    public static IngotMaterial Graphene = new IngotMaterial(205, "graphene", COLOR_DARK_GREY, SHINY, 2, of(), GENERATE_PLATE); //Unobtainable used in cables
    public static GemMaterial Jasper = new GemMaterial(206, "jasper", 0xC85050, EMERALD, 2, of(), STD_GEM | NO_SMELTING | HIGH_SIFTER_OUTPUT); //Has ore used only as lens
    public static IngotMaterial Osmiridium = new IngotMaterial(207, "osmiridium", 0x6464FF, METALLIC, 3, of(new MaterialComponent(Iridium, 3), new MaterialComponent(Osmium, 1)), EXT2_METAL, null, 9.0F, 3.0f, 3152, 2500);
    public static FluidMaterial NitrationMixture = new FluidMaterial(352, "nitration_mixture", 0xEEEEAA, FLUID, of(new MaterialComponent(NitricAcid, 1), new MaterialComponent(SulfuricAcid, 1)), DISABLE_DECOMPOSITION);
    public static DustMaterial Tenorite = new DustMaterial(358, "tenorite", 0x606060, FINE, 1, of(new MaterialComponent(Copper, 1), new MaterialComponent(Oxygen, 1)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Cuprite = new DustMaterial(359, "cuprite", 0x770000, RUBY, 2, of(new MaterialComponent(Copper, 2), new MaterialComponent(Oxygen, 1)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Bornite = new DustMaterial(360, "bornite", 0xC11800, DULL, 1, of(new MaterialComponent(Copper, 5), new MaterialComponent(Iron, 1), new MaterialComponent(Sulfur, 4)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Chalcocite = new DustMaterial(361, "chalcocite", 0x353535, GEM_VERTICAL, 2, of(new MaterialComponent(Copper, 2), new MaterialComponent(Sulfur, 1)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Enargite = new DustMaterial(362, "enargite", 0xBBBBBB, METALLIC, 2, of(new MaterialComponent(Copper, 3), new MaterialComponent(Arsenic, 1), new MaterialComponent(Sulfur, 4)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Tennantite = new DustMaterial(363, "tennantite", 0x909090, METALLIC, 2, of(new MaterialComponent(Copper, 12), new MaterialComponent(Arsenic, 4), new MaterialComponent(Sulfur, 13)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing

    public static DustMaterial PhosphorousPentoxide = new DustMaterial(466, "phosphorous_pentoxide", 8158464, DULL, 1, of(new MaterialComponent(Phosphorus, 4), new MaterialComponent(Oxygen, 10)), 0);
    public static FluidMaterial PhosphoricAcid = new FluidMaterial(467, "phosphoric_acid", 11447824, FLUID, of(new MaterialComponent(Hydrogen, 3), new MaterialComponent(Phosphorus, 1), new MaterialComponent(Oxygen, 4)), 0);
    public static DustMaterial SodiumHydroxide = new DustMaterial(373, "sodium_hydroxide", 6466, DULL, 1, of(new MaterialComponent(Sodium, 1), new MaterialComponent(Oxygen, 1), new MaterialComponent(Hydrogen, 1)), 0);
    public static DustMaterial Quicklime = new DustMaterial(374, "quicklime", 8421504, SAND, 1, of(new MaterialComponent(Calcium, 1), new MaterialComponent(Oxygen, 1)), 0);
    public static FluidMaterial SulfurTrioxide = new FluidMaterial(376, "sulfur_trioxide", 8618781, GAS, of(new MaterialComponent(Sulfur, 1), new MaterialComponent(Oxygen, 3)), STATE_GAS);
    public static FluidMaterial SulfurDioxide = new FluidMaterial(377, "sulfur_dioxide", 10263584, GAS, of(new MaterialComponent(Sulfur, 1), new MaterialComponent(Oxygen, 2)), STATE_GAS);
    public static FluidMaterial CarbonMonoxde = new FluidMaterial(380, "carbon_monoxide", 1655660, GAS, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Oxygen, 1)), STATE_GAS);
    public static FluidMaterial DilutedSulfuricAcid = new FluidMaterial(381, "diluted_sulfuric_acid", 9987366, FLUID, of(new MaterialComponent(Hydrogen, 2), new MaterialComponent(Sulfur, 1), new MaterialComponent(Oxygen, 4)), DISABLE_DECOMPOSITION);
    public static DustMaterial SodiumBisulfate = new DustMaterial(382, "sodium_bisulfate", 10291, DULL, 1, of(new MaterialComponent(Sodium, 1), new MaterialComponent(Hydrogen, 1), new MaterialComponent(Sulfur, 1), new MaterialComponent(Oxygen, 4)), DISABLE_DECOMPOSITION);
    public static FluidMaterial HydrochloricAcid = new FluidMaterial(400, "hydrochloric_acid", 9477273, FLUID, of(new MaterialComponent(Hydrogen, 1), new MaterialComponent(Chlorine, 1)), 0);
    public static FluidMaterial DilutedHydrochloricAcid = new FluidMaterial(384, "diluted_hydrochloric_acid", 8160900, FLUID, of(new MaterialComponent(Hydrogen, 1), new MaterialComponent(Chlorine, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial HypochlorousAcid = new FluidMaterial(385, "hypochlorous_acid", 6123637, FLUID, of(new MaterialComponent(Hydrogen, 1), new MaterialComponent(Chlorine, 1), new MaterialComponent(Oxygen, 1)), 0);
    public static FluidMaterial Ammonia = new FluidMaterial(386, "ammonia", 4011371, GAS, of(new MaterialComponent(Nitrogen, 1), new MaterialComponent(Hydrogen, 3)), STATE_GAS);
    public static FluidMaterial Chloramine = new FluidMaterial(387, "chloramine", 4031340, GAS, of(new MaterialComponent(Nitrogen, 1), new MaterialComponent(Hydrogen, 1), new MaterialComponent(HydrochloricAcid, 1)), STATE_GAS);
    public static IngotMaterial GalliumArsenide = new IngotMaterial(410, "gallium_arsenide", 7500402, DULL, 1, of(new MaterialComponent(Arsenic, 1), new MaterialComponent(Gallium, 1)), DECOMPOSITION_BY_CENTRIFUGING | GENERATE_PLATE, null, 1200);
    public static DustMaterial Potash = new DustMaterial(402, "potash", 5057059, SAND, 1, of(new MaterialComponent(Potassium, 2), new MaterialComponent(Oxygen, 1)), 0);
    public static DustMaterial SodaAsh = new DustMaterial(403, "soda_ash", 7697800, SAND, 1, of(new MaterialComponent(Sodium, 2), new MaterialComponent(Carbon, 1), new MaterialComponent(Oxygen, 3)), 0);
    public static FluidMaterial NickelSulfateSolution = new FluidMaterial(412, "nickel_sulfate_water_solution", 4109888, FLUID, of(new MaterialComponent(Nickel, 1), new MaterialComponent(Sulfur, 1), new MaterialComponent(Oxygen, 4), new MaterialComponent(Water, 6)), 0);
    public static FluidMaterial CopperSulfateSolution = new FluidMaterial(413, "blue_vitriol_water_solution", 4761024, FLUID, of(new MaterialComponent(Copper, 1), new MaterialComponent(Sulfur, 1), new MaterialComponent(Oxygen, 4), new MaterialComponent(Water, 5)), 0);
    public static IngotMaterial IndiumGalliumPhosphide = new IngotMaterial(421, "indium_gallium_phosphide", 8220052, DULL, 1, of(new MaterialComponent(Indium, 1), new MaterialComponent(Gallium, 1), new MaterialComponent(Phosphorus, 1)), DECOMPOSITION_BY_CENTRIFUGING | GENERATE_PLATE);
    public static DustMaterial FerriteMixture = new DustMaterial(423, "ferrite_mixture", 9803157, METALLIC, 1, of(new MaterialComponent(Nickel, 1), new MaterialComponent(Zinc, 1), new MaterialComponent(Iron, 4)), DECOMPOSITION_BY_CENTRIFUGING);
    public static IngotMaterial NickelZincFerrite = new IngotMaterial(424, "nickel_zinc_ferrite", 3092271, METALLIC, 0, of(new MaterialComponent(Nickel, 1), new MaterialComponent(Zinc, 1), new MaterialComponent(Iron, 4), new MaterialComponent(Oxygen, 8)), EXT_METAL, null, 1500);
    public static FluidMaterial LeadZincSolution = new FluidMaterial(426, "lead_zinc_solution", 3213570, FLUID, of(new MaterialComponent(Lead, 1), new MaterialComponent(Silver, 1), new MaterialComponent(Zinc, 1), new MaterialComponent(Sulfur, 3), new MaterialComponent(Water, 1)), DECOMPOSITION_BY_CENTRIFUGING);
    public static DustMaterial Magnesia = new DustMaterial(460, "magnesia", 8943736, SAND, 1, of(new MaterialComponent(Magnesium, 1), new MaterialComponent(Oxygen, 1)), 0);
    public static FluidMaterial HydrofluoricAcid = new FluidMaterial(404, "hydrofluoric_acid", 946055, FLUID, of(new MaterialComponent(Hydrogen, 1), new MaterialComponent(Fluorine, 1)), 0);
    public static FluidMaterial NitricOxide = new FluidMaterial(405, "nitric_oxide", 6790328, GAS, of(new MaterialComponent(Nitrogen, 1), new MaterialComponent(Oxygen, 1)), STATE_GAS);

    /**
     * Organic chemistry
     */
    public static FluidMaterial Chloroform = new FluidMaterial(383, "chloroform", 7351936, FLUID, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Hydrogen, 1), new MaterialComponent(Chlorine, 3)), 0);
    public static FluidMaterial Cumene = new FluidMaterial(420, "cumene", 4924684, FLUID, of(new MaterialComponent(Carbon, 9), new MaterialComponent(Hydrogen, 12)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Tetrafluoroethylene = new FluidMaterial(427, "tetrafluoroethylene", 6776679, GAS, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Fluorine, 4)), STATE_GAS | DISABLE_DECOMPOSITION);
    public static FluidMaterial Chloromethane = new FluidMaterial(450, "chloromethane", 10301057, GAS, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Hydrogen, 3), new MaterialComponent(Chlorine, 1)), STATE_GAS | DISABLE_DECOMPOSITION);
    public static FluidMaterial AllylChloride = new FluidMaterial(451, "allyl_chloride", 7450250, FLUID, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Methane, 1), new MaterialComponent(HydrochloricAcid, 1)), 0);
    public static FluidMaterial Isoprene = new FluidMaterial(452, "isoprene", 1907997, FLUID, of(new MaterialComponent(Carbon, 5), new MaterialComponent(Hydrogen, 8)), 0);
    public static FluidMaterial Propane = new FluidMaterial(414, "propane", 12890952, GAS, of(new MaterialComponent(Carbon, 3), new MaterialComponent(Hydrogen, 8)), STATE_GAS);
    public static FluidMaterial Propene = new FluidMaterial(415, "propene", 12954956, GAS, of(new MaterialComponent(Carbon, 3), new MaterialComponent(Hydrogen, 6)), STATE_GAS);
    public static FluidMaterial Ethane = new FluidMaterial(416, "ethane", 10329540, GAS, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 6)), STATE_GAS);
    public static FluidMaterial Butene = new FluidMaterial(417, "butene", 10700561, GAS, of(new MaterialComponent(Carbon, 4), new MaterialComponent(Hydrogen, 8)), STATE_GAS);
    public static FluidMaterial Butane = new FluidMaterial(418, "butane", 9385508, GAS, of(new MaterialComponent(Carbon, 4), new MaterialComponent(Hydrogen, 10)), STATE_GAS);
    public static FluidMaterial CalciumAcetate = new FluidMaterial(419, "calcium_acetate", 11444113, FLUID, of(new MaterialComponent(Calcium, 1), new MaterialComponent(Carbon, 4), new MaterialComponent(Oxygen, 4), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Water, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial VinylAcetate = new FluidMaterial(409, "vinyl_acetate", 13144428, FLUID, of(new MaterialComponent(Carbon, 4), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Oxygen, 2)), DISABLE_DECOMPOSITION);
    public static IngotMaterial PolyphenyleneSulfide = new IngotMaterial(411, "polyphenylene_sulfide", 8743424, DULL, 1, of(new MaterialComponent(Carbon, 6), new MaterialComponent(Hydrogen, 4), new MaterialComponent(Sulfur, 1)), DISABLE_DECOMPOSITION | EXT_METAL | GENERATE_FOIL);
    public static FluidMaterial MethylAcetate = new FluidMaterial(406, "methyl_acetate", 12427150, FLUID, of(new MaterialComponent(Carbon, 3), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Oxygen, 2)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Ethenone = new FluidMaterial(407, "ethenone", 1776449, FLUID, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Tetranitromethane = new FluidMaterial(408, "tetranitromethane", 1715244, FLUID, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Nitrogen, 4), new MaterialComponent(Oxygen, 8)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Dimethylamine = new FluidMaterial(388, "dimethylamine", 4931417, GAS, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 7), new MaterialComponent(Nitrogen, 1)), STATE_GAS | DISABLE_DECOMPOSITION);
    public static FluidMaterial Dimethylhydrazine = new FluidMaterial(389, "dimethylhidrazine", 1052748, FLUID, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 8), new MaterialComponent(Nitrogen, 2)), DISABLE_DECOMPOSITION);
    public static FluidMaterial DinitrogenTetroxide = new FluidMaterial(390, "dinitrogen_tetroxide", 998766, GAS, of(new MaterialComponent(Nitrogen, 2), new MaterialComponent(Oxygen, 4)), STATE_GAS);
    public static IngotMaterial SiliconeRubber = new IngotMaterial(391, "silicon_rubber", 11316396, DULL, 1, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Oxygen, 1), new MaterialComponent(Silicon, 1)), GENERATE_PLATE | GENERATE_GEAR | GENERATE_RING | FLAMMABLE | NO_SMASHING | GENERATE_FOIL | DISABLE_DECOMPOSITION);
    public static DustMaterial Polydimethylsiloxane = new DustMaterial(392, "polydimethylsiloxane", 9211020, DULL, 1, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Oxygen, 1), new MaterialComponent(Silicon, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Dimethyldichlorosilane = new FluidMaterial(393, "dimethyldichlorosilane", 4070471, FLUID, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Chlorine, 2), new MaterialComponent(Silicon, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Styrene = new FluidMaterial(394, "styrene", 10722453, FLUID, of(new MaterialComponent(Carbon, 8), new MaterialComponent(Hydrogen, 8)), DISABLE_DECOMPOSITION);
    public static IngotMaterial Polystyrene = new IngotMaterial(395, "polystyrene", 8945785, DULL, 1, of(new MaterialComponent(Carbon, 8), new MaterialComponent(Hydrogen, 8)), DISABLE_DECOMPOSITION | GENERATE_FOIL);
    public static FluidMaterial Butadiene = new FluidMaterial(396, "butadiene", 11885072, GAS, of(new MaterialComponent(Carbon, 4), new MaterialComponent(Hydrogen, 6)), DISABLE_DECOMPOSITION);
    public static DustMaterial RawStyreneButadieneRubber = new DustMaterial(397, "raw_styrene_butadiene_rubber", 5192762, SAND, 1, of(new MaterialComponent(Carbon, 8), new MaterialComponent(Hydrogen, 8), new MaterialComponent(Butadiene, 3)), DISABLE_DECOMPOSITION);
    public static IngotMaterial StyreneButadieneRubber = new IngotMaterial(398, "styrene_butadiene_rubber", 1906453, ROUGH, 1, of(new MaterialComponent(Carbon, 8), new MaterialComponent(Hydrogen, 8), new MaterialComponent(Butadiene, 3)), GENERATE_PLATE | GENERATE_GEAR | GENERATE_RING | FLAMMABLE | NO_SMASHING | DISABLE_DECOMPOSITION);
    public static FluidMaterial Dichlorobenzene = new FluidMaterial(399, "dichlorobenzene", 868171, FLUID, of(new MaterialComponent(Carbon, 6), new MaterialComponent(Hydrogen, 4), new MaterialComponent(Chlorine, 2)), DISABLE_DECOMPOSITION);
    public static FluidMaterial AceticAcid = new FluidMaterial(401, "acetic_acid", 10260096, FLUID, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 4), new MaterialComponent(Oxygen, 2)), DISABLE_DECOMPOSITION);
    public static FluidMaterial PolyvinylAcetate = new FluidMaterial(471, "polyvinyl_acetate", 13139532, FLUID, of(new MaterialComponent(Carbon, 4), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Oxygen, 2)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Phenol = new FluidMaterial(468, "phenol", 6635559, FLUID, of(new MaterialComponent(Carbon, 6), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Oxygen, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial BisphenolA = new FluidMaterial(469, "bisphenol_a", 10848014, FLUID, of(new MaterialComponent(Carbon, 15), new MaterialComponent(Hydrogen, 16), new MaterialComponent(Oxygen, 2)), DISABLE_DECOMPOSITION);
    public static IngotMaterial ReinforcedEpoxyResin = new IngotMaterial(470, "reinforced_epoxy_resin", 7491595, DULL, 1, of(new MaterialComponent(Carbon, 6), new MaterialComponent(Hydrogen, 4), new MaterialComponent(Oxygen, 1)), GENERATE_PLATE | DISABLE_DECOMPOSITION);
    public static IngotMaterial BorosilicateGlass = new IngotMaterial(364, "borosilicate_glass", 13424588, METALLIC, 1, of(new MaterialComponent(Boron, 1), new MaterialComponent(SiliconDioxide, 7)), DISABLE_DECOMPOSITION);
    public static IngotMaterial PolyvinylChloride = new IngotMaterial(965, "polyvinyl_chloride", 10069156, DULL, 1, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 3), new MaterialComponent(Chlorine, 1)), EXT_METAL | GENERATE_FOIL | DISABLE_DECOMPOSITION);
    public static FluidMaterial VinylChloride = new FluidMaterial(366, "vinyl_chloride", 11582395, GAS, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 3), new MaterialComponent(Chlorine, 1)), STATE_GAS | DISABLE_DECOMPOSITION);
    public static FluidMaterial Ethylene = new FluidMaterial(367, "ethylene", 11382189, GAS, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 4)), STATE_GAS);
    public static FluidMaterial Benzene = new FluidMaterial(368, "benzene", 2039583, FLUID, of(new MaterialComponent(Carbon, 6), new MaterialComponent(Hydrogen, 6)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Acetone = new FluidMaterial(375, "acetone", 9342606, FLUID, of(new MaterialComponent(Carbon, 3), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Oxygen, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Glycerol = new FluidMaterial(378, "glycerol", 7384944, FLUID, of(new MaterialComponent(Carbon, 3), new MaterialComponent(Hydrogen, 8), new MaterialComponent(Oxygen, 3)), 0);
    public static FluidMaterial Methanol = new FluidMaterial(379, "methanol", 8941584, FLUID, of(new MaterialComponent(Carbon, 1), new MaterialComponent(Hydrogen, 4), new MaterialComponent(Oxygen, 1)), 0);

    /**
     * Not possible to determine exact Components
     */
    public static FluidMaterial SaltWater = new FluidMaterial(428, "salt_water", 255, FLUID, of(new MaterialComponent(Salt, 1), new MaterialComponent(Water, 1)), DISABLE_DECOMPOSITION);
    public static RoughSolidMaterial Wood = new RoughSolidMaterial(196, "wood", 0x896727, WOOD, 0, of(), STD_SOLID | FLAMMABLE | NO_SMELTING | GENERATE_GEAR | GENERATE_ROD_LONG | GENERATE_FRAME, () -> MaterialForm.plank);
    public static FluidMaterial WoodGas = new FluidMaterial(370, "wood_gas", 0xB1A571, GAS, of(), STATE_GAS | DISABLE_DECOMPOSITION);
    public static FluidMaterial WoodVinegar = new FluidMaterial(371, "wood_vinegar", 0xA54B0F, FLUID, of(), 0);
    public static FluidMaterial WoodTar = new FluidMaterial(372, "wood_tar", 0x2D2118, FLUID, of(), 0);
    public static FluidMaterial CharcoalByproducts = new FluidMaterial(461, "charcoal_byproducts", 0x664027, FLUID, of(), 0);

    public static FluidMaterial Biomass = new FluidMaterial(315, "biomass", 0x00FF00, FLUID, of(), 0);
    public static FluidMaterial BioDiesel = new FluidMaterial(314, "bio_diesel", 0xC3690F, FLUID, of(), 0);
    public static FluidMaterial FermentedBiomass = new FluidMaterial(472, "fermented_biomass", 0x3F4B0D, FLUID, of(), 0);

    public static FluidMaterial Creosote = new FluidMaterial(316, "creosote", 0x804000, FLUID, of(), 0);
    public static FluidMaterial Ethanol = new FluidMaterial(317, "ethanol", COLOR_ORANGE, FLUID, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Hydrogen, 6), new MaterialComponent(Oxygen, 1)), DISABLE_DECOMPOSITION);
    public static FluidMaterial Fuel = new FluidMaterial(318, "fuel", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial RocketFuel = new FluidMaterial(474, "rocket_fuel", 0xBDB78C, FLUID, of(), 0);
    public static FluidMaterial Glue = new FluidMaterial(319, "glue", 0xC8C400, FLUID, of(), 0);
    public static DustMaterial Gunpowder = new DustMaterial(320, "gunpowder", COLOR_DARK_GREY, SAND, 0, of(), FLAMMABLE | EXPLOSIVE | NO_SMELTING | NO_SMASHING);
    public static Material Lubricant = new FluidMaterial(321, "lubricant", 0xFFC400, FLUID, of(), 0);
    public static FluidMaterial Oil = new FluidMaterial(323, "oil", 0x666666, FLUID, of(), 0);
    public static DustMaterial Oilsands = new DustMaterial(324, "oilsands", 0x0A0A0A, SAND, 1, of(new MaterialComponent(Oil, 1L)), GENERATE_ORE);
    public static DustMaterial RareEarth = new DustMaterial(326, "rare_earth", 0x808064, ROUGH, 0, of(), 0);
    public static DustMaterial PlatinumGroupSludge = new DustMaterial(422, "platinum_group_sludge", 4864, ROUGH, 1, of(), DISABLE_DECOMPOSITION);
    public static FluidMaterial IndiumConcentrate = new FluidMaterial(425, "indium_concentrate", 205130, FLUID, of(), 0);
    public static FluidMaterial SeedOil = new FluidMaterial(327, "seed_oil", 0xC4FF00, FLUID, of(), 0);
    public static DustMaterial Stone = new DustMaterial(328, "stone", 0xCDCDCD, ROUGH, 1, of(), MORTAR_GRINDABLE | GENERATE_GEAR | GENERATE_PLATE | NO_SMASHING | NO_RECYCLING);
    public static Material Lava = new FluidMaterial(329, "lava", 0xFF4000, FLUID, of(), 0);
    public static DustMaterial Glowstone = new DustMaterial(330, "glowstone", COLOR_YELLOW, SHINY, 1, of(), NO_SMASHING | SMELT_INTO_FLUID | GENERATE_PLATE | EXCLUDE_PLATE_COMPRESSOR_RECIPE);
    public static GemMaterial NetherStar = new GemMaterial(331, "nether_star", 0xFFFFFF, NETHERSTAR, 4, of(), STD_SOLID | GENERATE_LENS | NO_SMASHING | NO_SMELTING);
    public static DustMaterial Endstone = new DustMaterial(332, "endstone", 0xFFFFFF, DULL, 1, of(), NO_SMASHING);
    public static DustMaterial Netherrack = new DustMaterial(333, "netherrack", 0xC80000, ROUGH, 1, of(), NO_SMASHING | FLAMMABLE);
    public static FluidMaterial DrillingFluid = new FluidMaterial(348, "drilling_fluid", 0xFFFFAA, FLUID, of(), 0);
    public static FluidMaterial ConstructionFoam = new FluidMaterial(347, "construction_foam", 0x333333, FLUID, of(), 0);

    /**
     * Oil refining sources & products
     */
    public static FluidMaterial HydroCrackedEthane = new FluidMaterial(429, "hydrocracked_ethane", 9868988, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedEthylene = new FluidMaterial(430, "hydrocracked_ethylene", 10724256, GAS, of(), STATE_GAS);
    public static FluidMaterial HydroCrackedPropene = new FluidMaterial(431, "hydrocracked_propene", 12494144, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedPropane = new FluidMaterial(432, "hydrocracked_propane", 12494144, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedLightFuel = new FluidMaterial(433, "hydrocracked_light_fuel", 12037896, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedButane = new FluidMaterial(434, "hydrocracked_butane", 8727576, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedNaphtha = new FluidMaterial(435, "hydrocracked_naphtha", 12563976, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedHeavyFuel = new FluidMaterial(436, "hydrocracked_heavy_fuel", 16776960, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedGas = new FluidMaterial(437, "hydrocracked_gas", 11842740, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedButene = new FluidMaterial(438, "hydrocracked_butene", 10042885, FLUID, of(), 0);
    public static FluidMaterial HydroCrackedButadiene = new FluidMaterial(439, "hydrocracked_butadiene", 11358723, FLUID, of(), 0);
    public static FluidMaterial SteamCrackedEthane = new FluidMaterial(440, "steamcracked_ethane", 9868988, FLUID, of(), 0);
    public static FluidMaterial SteamCrackedEthylene = new FluidMaterial(441, "steamcracked_ethylene", 10724256, GAS, of(), 0);
    public static FluidMaterial SteamCrackedPropene = new FluidMaterial(442, "steamcracked_propene", 12494144, FLUID, of(), 0);
    public static FluidMaterial SteamCrackedPropane = new FluidMaterial(443, "steamcracked_propane", 12494144, FLUID, of(), 0);
    public static FluidMaterial SteamCrackedButane = new FluidMaterial(444, "steamcracked_butane", 8727576, FLUID, of(), 0);
    public static FluidMaterial SteamCrackedNaphtha = new FluidMaterial(445, "steamcracked_naphtha", 12563976, FLUID, of(), 0);
    public static FluidMaterial SteamCrackedGas = new FluidMaterial(446, "steamcracked_gas", 11842740, FLUID, of(), 0);
    public static FluidMaterial SteamCrackedButene = new FluidMaterial(447, "steamcracked_butene", 10042885, FLUID, of(), 0);
    public static FluidMaterial SteamCrackedButadiene = new FluidMaterial(448, "steamcracked_butadiene", 11358723, FLUID, of(), 0);

    public static FluidMaterial OilHeavy = new FluidMaterial(165, "oil_heavy", 0x666666, FLUID, of(), GENERATE_FLUID_BLOCK);
    public static FluidMaterial OilMedium = new FluidMaterial(166, "oil_medium", 0x666666, FLUID, of(), GENERATE_FLUID_BLOCK);
    public static FluidMaterial OilLight = new FluidMaterial(167, "oil_light", 0x666666, FLUID, of(), GENERATE_FLUID_BLOCK);
    public static FluidMaterial NaturalGas = new FluidMaterial(168, "natural_gas", 0xFFFFFF, FLUID, of(), STATE_GAS | GENERATE_FLUID_BLOCK);
    public static FluidMaterial SulfuricGas = new FluidMaterial(169, "sulfuric_gas", 0xFFFFFF, FLUID, of(), STATE_GAS);
    public static FluidMaterial Gas = new FluidMaterial(170, "gas", 0xFFFFFF, FLUID, of(), STATE_GAS);
    public static FluidMaterial SulfuricNaphtha = new FluidMaterial(171, "sulfuric_naphtha", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial SulfuricLightFuel = new FluidMaterial(172, "sulfuric_light_fuel", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial SulfuricHeavyFuel = new FluidMaterial(173, "sulfuric_heavy_fuel", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial Naphtha = new FluidMaterial(174, "naphtha", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial LightFuel = new FluidMaterial(175, "light_fuel", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial HeavyFuel = new FluidMaterial(176, "heavy_fuel", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial LPG = new FluidMaterial(177, "lpg", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial CrackedLightFuel = new FluidMaterial(464, "cracked_light_fuel", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial CrackedHeavyFuel = new FluidMaterial(465, "cracked_heavy_fuel", COLOR_YELLOW, FLUID, of(), 0);
    public static FluidMaterial Toluene = new FluidMaterial(350, "toluene", 0xFFFFFF, FLUID, of(new MaterialComponent(Carbon, 7), new MaterialComponent(Hydrogen, 8)), DISABLE_DECOMPOSITION);

    /**
     * Second Degree Compounds
     */
    public static DustMaterial Borax = new DustMaterial(313, "borax", 0xFFFFFF, SAND, 1, of(new MaterialComponent(Sodium, 2), new MaterialComponent(Boron, 4), new MaterialComponent(Water, 10), new MaterialComponent(Oxygen, 7)), 0); //Byproduct of ore; Used only in Electrolyzing
    public static GemMaterial Lignite = new GemMaterial(211, "lignite", 0x644646, LIGNITE, 0, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Water, 4), new MaterialComponent(DarkAsh, 1)), GENERATE_ORE | FLAMMABLE | NO_SMELTING | NO_SMASHING | MORTAR_GRINDABLE);
    public static GemMaterial Olivine = new GemMaterial(212, "olivine", 0x66FF66, RUBY, 2, of(new MaterialComponent(Magnesium, 2), new MaterialComponent(Iron, 1), new MaterialComponent(SiliconDioxide, 2)), STD_GEM | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT, 7.5F, 3.0f, 312);
    public static GemMaterial Opal = new GemMaterial(213, "opal", COLOR_BLUE, OPAL, 2, of(new MaterialComponent(SiliconDioxide, 1)), STD_GEM | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT, 7.5F, 3.0f, 312); //Has ore; Used only as tool and lens
    public static GemMaterial Amethyst = new GemMaterial(214, "amethyst", 0xD232D2, RUBY, 3, of(new MaterialComponent(SiliconDioxide, 4), new MaterialComponent(Iron, 1)), STD_GEM | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT, 7.5F, 3.0f, 312); //Has ore; Used only as tool and lens (no usage)
    public static DustMaterial Redstone = new DustMaterial(215, "redstone", 0xC80000, ROUGH, 2, of(new MaterialComponent(Silicon, 1), new MaterialComponent(Pyrite, 5), new MaterialComponent(Ruby, 1), new MaterialComponent(Mercury, 3)), GENERATE_PLATE | GENERATE_ORE | NO_SMASHING | SMELT_INTO_FLUID | EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES);
    public static GemMaterial Lapis = new GemMaterial(216, "lapis", 0x4646DC, LAPIS, 1, of(new MaterialComponent(Lazurite, 12), new MaterialComponent(Sodalite, 2), new MaterialComponent(Pyrite, 1), new MaterialComponent(Calcite, 1)), STD_GEM | NO_SMASHING | NO_SMELTING | CRYSTALLISABLE | NO_WORKING | DECOMPOSITION_BY_ELECTROLYZING | EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES);
    public static GemMaterial EnderPearl = new GemMaterial(218, "ender_pearl", 0x6CDCC8, GEM_VERTICAL, 1, of(new MaterialComponent(Beryllium, 1), new MaterialComponent(Potassium, 4), new MaterialComponent(Nitrogen, 5)), GENERATE_PLATE | GENERATE_LENS | NO_SMASHING | NO_SMELTING);
    public static GemMaterial EnderEye = new GemMaterial(219, "ender_eye", 0x66FF66, GEM_VERTICAL, 1, of(new MaterialComponent(EnderPearl, 1), new MaterialComponent(Blaze, 1)), GENERATE_PLATE | GENERATE_LENS | NO_SMASHING | NO_SMELTING);
    public static RoughSolidMaterial Flint = new RoughSolidMaterial(220, "flint", 0x002040, FLINT, 1, of(new MaterialComponent(SiliconDioxide, 1)), NO_SMASHING | MORTAR_GRINDABLE, () -> MaterialForm.gem);
    public static DustMaterial Tantalite = new DustMaterial(224, "tantalite", 0x915028, METALLIC, 3, of(new MaterialComponent(Manganese, 1), new MaterialComponent(Tantalum, 2), new MaterialComponent(Oxygen, 6)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static GemMaterial Apatite = new GemMaterial(226, "apatite", 0xC8C8FF, EMERALD, 1, of(new MaterialComponent(Calcium, 5), new MaterialComponent(Phosphate, 3), new MaterialComponent(Chlorine, 1)), GENERATE_ORE | NO_SMASHING | NO_SMELTING | CRYSTALLISABLE);
    public static IngotMaterial SterlingSilver = new IngotMaterial(227, "sterling_silver", 0xFADCE1, SHINY, 2, of(new MaterialComponent(Copper, 1), new MaterialComponent(Silver, 4)), EXT2_METAL, null, 13.0F, 2.0f, 196, 1700);
    public static IngotMaterial RoseGold = new IngotMaterial(228, "rose_gold", 0xFFE61E, SHINY, 2, of(new MaterialComponent(Copper, 1), new MaterialComponent(Gold, 4)), EXT2_METAL, null, 14.0F, 2.0f, 152, 1600);
    public static IngotMaterial BlackBronze = new IngotMaterial(229, "black_bronze", 0x64327D, DULL, 2, of(new MaterialComponent(Gold, 1), new MaterialComponent(Silver, 1), new MaterialComponent(Copper, 3)), EXT2_METAL, null, 12.0F, 2.0f, 256, 2000);
    public static IngotMaterial BismuthBronze = new IngotMaterial(230, "bismuth_bronze", 0x647D7D, DULL, 2, of(new MaterialComponent(Bismuth, 1), new MaterialComponent(Zinc, 1), new MaterialComponent(Copper, 3)), EXT2_METAL, null, 8.0F, 3.0f, 256, 1100);
    public static IngotMaterial BlackSteel = new IngotMaterial(231, "black_steel", COLOR_VERY_DARK_GREY, DULL, 2, of(new MaterialComponent(Nickel, 1), new MaterialComponent(BlackBronze, 1), new MaterialComponent(Steel, 3)), EXT_METAL, null, 6.5F, 6.5f, 768, 1200);
    public static IngotMaterial RedSteel = new IngotMaterial(232, "red_steel", 0x8C6464, DULL, 2, of(new MaterialComponent(SterlingSilver, 1), new MaterialComponent(BismuthBronze, 1), new MaterialComponent(Steel, 2), new MaterialComponent(BlackSteel, 4)), EXT_METAL, null, 7.0F, 4.5f, 896, 1300);
    public static IngotMaterial BlueSteel = new IngotMaterial(233, "blue_steel", 0x64648C, DULL, 2, of(new MaterialComponent(RoseGold, 1), new MaterialComponent(Brass, 1), new MaterialComponent(Steel, 2), new MaterialComponent(BlackSteel, 4)), EXT_METAL | GENERATE_FRAME, null, 7.5F, 5.0f, 1024, 1400);
    public static IngotMaterial DamascusSteel = new IngotMaterial(234, "damascus_steel", 0x6E6E6E, METALLIC, 2, of(new MaterialComponent(Steel, 1)), EXT_METAL, null, 8.0F, 5.0f, 1280, 1500);
    public static IngotMaterial TungstenSteel = new IngotMaterial(235, "tungsten_steel", 0x6464A0, METALLIC, 4, of(new MaterialComponent(Steel, 1), new MaterialComponent(Tungsten, 1)), EXT2_METAL | GENERATE_RING | GENERATE_ROTOR | GENERATE_GEAR_SMALL | GENERATE_ROD_LONG | GENERATE_DENSE | GENERATE_FRAME, null, 8.0F, 4.0f, 2560, 3000);
    public static FluidMaterial NitroFuel = new FluidMaterial(236, "nitro_fuel", 0xC8FF00, FLUID, of(), FLAMMABLE | EXPLOSIVE | NO_SMELTING | NO_SMASHING);
    public static DustMaterial Phosphor = new DustMaterial(239, "phosphor", COLOR_YELLOW, FLINT, 2, of(new MaterialComponent(Calcium, 3), new MaterialComponent(Phosphate, 2)), GENERATE_ORE | NO_SMASHING | NO_SMELTING | FLAMMABLE | EXPLOSIVE);
    public static DustMaterial Basalt = new DustMaterial(240, "basalt", 0x1E1414, ROUGH, 1, of(new MaterialComponent(Olivine, 1), new MaterialComponent(Calcite, 3), new MaterialComponent(Flint, 8), new MaterialComponent(DarkAsh, 4)), NO_SMASHING);
    public static DustMaterial Andesite = new DustMaterial(241, "andesite", 0xBEBEBE, ROUGH, 2, of(), NO_SMASHING);
    public static DustMaterial Diorite = new DustMaterial(242, "diorite", 0xFFFFFF, ROUGH, 2, of(), NO_SMASHING);
    public static DustMaterial Granite = new DustMaterial(449, "granite", 0xCFA18C, ROUGH, 2, of(), NO_SMASHING);
    public static GemMaterial GarnetRed = new GemMaterial(243, "garnet_red", 0xC85050, RUBY, 2, of(new MaterialComponent(Pyrope, 3), new MaterialComponent(Almandine, 5), new MaterialComponent(Spessartine, 8)), STD_SOLID | GENERATE_LENS | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT | GENERATE_ORE, null, 7.5F, 3.0f, 156);
    public static GemMaterial GarnetYellow = new GemMaterial(244, "garnet_yellow", 0xC8C850, RUBY, 2, of(new MaterialComponent(Andradite, 5), new MaterialComponent(Grossular, 8), new MaterialComponent(Uvarovite, 3)), STD_SOLID | GENERATE_LENS | NO_SMASHING | NO_SMELTING | HIGH_SIFTER_OUTPUT | GENERATE_ORE, null, 7.5F, 3.0f, 156);
    public static DustMaterial Marble = new DustMaterial(245, "marble", COLOR_LIGHT_GREY, FINE, 1, of(new MaterialComponent(Magnesium, 1), new MaterialComponent(Calcite, 7)), NO_SMASHING);
    public static DustMaterial Sugar = new DustMaterial(246, "sugar", 0xFAFAFA, SAND, 1, of(new MaterialComponent(Carbon, 2), new MaterialComponent(Water, 5), new MaterialComponent(Oxygen, 25)), 0);
    public static GemMaterial Vinteum = new GemMaterial(247, "vinteum", 0x64C8FF, EMERALD, 3, of(), STD_GEM | NO_SMASHING | NO_SMELTING, 12.0F, 3.0f, 128); //Has ore; Used only in tools and lens
    public static DustMaterial Redrock = new DustMaterial(248, "redrock", 0xFF5032, ROUGH, 1, of(new MaterialComponent(Calcite, 2), new MaterialComponent(Flint, 1), new MaterialComponent(Clay, 1)), NO_SMASHING);
    public static DustMaterial PotassiumFeldspar = new DustMaterial(249, "potassium_feldspar", 0x782828, FINE, 1, of(new MaterialComponent(Potassium, 1), new MaterialComponent(Aluminium, 1), new MaterialComponent(Silicon, 3), new MaterialComponent(Oxygen, 8)), 0); //Obtained only through electrolyzing; Used only for electrolyzing
    public static DustMaterial Biotite = new DustMaterial(250, "biotite", 0x141E14, METALLIC, 1, of(new MaterialComponent(Potassium, 1), new MaterialComponent(Magnesium, 3), new MaterialComponent(Aluminium, 3), new MaterialComponent(Fluorine, 2), new MaterialComponent(Silicon, 3), new MaterialComponent(Oxygen, 10)), 0); //Obtained only through electrolyzing; Used only for electrolyzing
    public static DustMaterial GraniteBlack = new DustMaterial(251, "granite_black", 0x0A0A0A, ROUGH, 3, of(new MaterialComponent(SiliconDioxide, 4), new MaterialComponent(Biotite, 1)), NO_SMASHING);
    public static DustMaterial GraniteRed = new DustMaterial(252, "granite_red", 0xFF0080, ROUGH, 3, of(new MaterialComponent(Aluminium, 2), new MaterialComponent(PotassiumFeldspar, 1), new MaterialComponent(Oxygen, 3)), NO_SMASHING);
    public static DustMaterial VanadiumMagnetite = new DustMaterial(255, "vanadium_magnetite", 0x23233C, METALLIC, 2, of(new MaterialComponent(Magnetite, 1), new MaterialComponent(Vanadium, 1)), GENERATE_ORE);
    public static DustMaterial Bastnasite = new DustMaterial(270, "bastnasite", 0xC86E2D, FINE, 2, of(new MaterialComponent(Cerium, 1), new MaterialComponent(Carbon, 1), new MaterialComponent(Fluorine, 1), new MaterialComponent(Oxygen, 3)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Pentlandite = new DustMaterial(271, "pentlandite", 0xA59605, ROUGH, 2, of(new MaterialComponent(Nickel, 9), new MaterialComponent(Sulfur, 8)), GENERATE_ORE | INDUCTION_SMELTING_LOW_OUTPUT); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Spodumene = new DustMaterial(272, "spodumene", 0xBEAAAA, ROUGH, 2, of(new MaterialComponent(Lithium, 1), new MaterialComponent(Aluminium, 1), new MaterialComponent(Silicon, 2), new MaterialComponent(Oxygen, 6)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Lepidolite = new DustMaterial(274, "lepidolite", 0xF0328C, FINE, 2, of(new MaterialComponent(Potassium, 1), new MaterialComponent(Lithium, 3), new MaterialComponent(Aluminium, 4), new MaterialComponent(Fluorine, 2), new MaterialComponent(Oxygen, 10)), GENERATE_ORE);
    public static DustMaterial Glauconite = new DustMaterial(275, "glauconite", 0x82B43C, DULL, 2, of(new MaterialComponent(Potassium, 1), new MaterialComponent(Magnesium, 2), new MaterialComponent(Aluminium, 4), new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 12)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Bentonite = new DustMaterial(278, "bentonite", 0xF5D7D2, ROUGH, 2, of(new MaterialComponent(Sodium, 1), new MaterialComponent(Magnesium, 6), new MaterialComponent(Silicon, 12), new MaterialComponent(Hydrogen, 4), new MaterialComponent(Water, 5), new MaterialComponent(Oxygen, 36)), GENERATE_ORE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Pitchblende = new DustMaterial(280, "pitchblende", 0xC8D200, ROUGH, 3, of(new MaterialComponent(Uraninite, 3), new MaterialComponent(Thorium, 1), new MaterialComponent(Lead, 1)), GENERATE_ORE);
    public static GemMaterial Monazite = new GemMaterial(281, "monazite", 0x324632, GEM_VERTICAL, 1, of(new MaterialComponent(RareEarth, 1), new MaterialComponent(Phosphate, 1)), GENERATE_ORE | NO_SMASHING | NO_SMELTING | CRYSTALLISABLE); //Spawns in world; Used only for byproducts and electrolyzing
    public static DustMaterial Malachite = new DustMaterial(282, "malachite", 0x055F05, ROUGH, 2, of(new MaterialComponent(Copper, 2), new MaterialComponent(Carbon, 1), new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 5)), GENERATE_ORE | INDUCTION_SMELTING_LOW_OUTPUT);
    public static DustMaterial Barite = new DustMaterial(286, "barite", 0xE6EBFF, DULL, 2, of(new MaterialComponent(Barium, 1), new MaterialComponent(Sulfur, 1), new MaterialComponent(Oxygen, 4)), GENERATE_ORE); //Spawns in world; Used only for electrolyzing
    public static DustMaterial Talc = new DustMaterial(294, "talc", 0x5AB45A, FINE, 2, of(new MaterialComponent(Magnesium, 3), new MaterialComponent(Silicon, 4), new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 12)), GENERATE_ORE);
    public static DustMaterial Soapstone = new DustMaterial(295, "soapstone", 0x5F915F, ROUGH, 1, of(new MaterialComponent(Magnesium, 3), new MaterialComponent(Silicon, 4), new MaterialComponent(Hydrogen, 2), new MaterialComponent(Oxygen, 12)), GENERATE_ORE);
    public static DustMaterial Concrete = new DustMaterial(296, "concrete", COLOR_VERY_DARK_GREY, ROUGH, 1, of(new MaterialComponent(Stone, 1)), NO_SMASHING | SMELT_INTO_FLUID);
    public static IngotMaterial TungstenCarbide = new IngotMaterial(300, "tungsten_carbide", 0x330066, METALLIC, 4, of(new MaterialComponent(Tungsten, 1), new MaterialComponent(Carbon, 1)), EXT2_METAL, null, 12.0F, 4.0f, 1280, 2460);
    public static IngotMaterial VanadiumSteel = new IngotMaterial(301, "vanadium_steel", 0xC0C0C0, METALLIC, 3, of(new MaterialComponent(Vanadium, 1), new MaterialComponent(Chrome, 1), new MaterialComponent(Steel, 7)), EXT2_METAL, null, 7.0F, 3.0f, 1920, 1453);
    public static IngotMaterial HSSG = new IngotMaterial(302, "hssg", 0x999900, METALLIC, 3, of(new MaterialComponent(TungstenSteel, 5), new MaterialComponent(Chrome, 1), new MaterialComponent(Molybdenum, 2), new MaterialComponent(Vanadium, 1)), EXT2_METAL | GENERATE_RING | GENERATE_ROTOR | GENERATE_GEAR_SMALL | GENERATE_ROD_LONG | GENERATE_FRAME, null, 10.0F, 5.5f, 4000, 4500);
    public static IngotMaterial HSSE = new IngotMaterial(303, "hsse", 0x336600, METALLIC, 4, of(new MaterialComponent(HSSG, 6), new MaterialComponent(Cobalt, 1), new MaterialComponent(Manganese, 1), new MaterialComponent(Silicon, 1)), EXT2_METAL | GENERATE_RING | GENERATE_ROTOR | GENERATE_GEAR_SMALL | GENERATE_ROD_LONG | GENERATE_FRAME, null, 10.0F, 8.0f, 5120, 5400);
    public static IngotMaterial HSSS = new IngotMaterial(304, "hsss", 0x660033, METALLIC, 4, of(new MaterialComponent(HSSG, 6), new MaterialComponent(Iridium, 2), new MaterialComponent(Osmium, 1)), EXT2_METAL | GENERATE_GEAR, null, 15.0F, 7.0f, 3000, 5400);
    /**
     * Clear matter materials
     */
    public static FluidMaterial UUAmplifier = new FluidMaterial(305, "uuamplifier", 0xAA00AA, FLUID, of(), 0);
    public static FluidMaterial UUMatter = new FluidMaterial(306, "uumatter", 0x770077, FLUID, of(), 0);

    /**
     * Stargate materials
     */
    public static IngotMaterial Naquadah = new IngotMaterial(307, "naquadah", COLOR_LIGHT_BLACK, METALLIC, 4, of(), EXT_METAL | GENERATE_ORE, Element.Nq, 6.0F, 4.0f, 1280, 5400);
    public static IngotMaterial NaquadahAlloy = new IngotMaterial(308, "naquadah_alloy", 0x282828, METALLIC, 5, of(new MaterialComponent(Naquadah, 1), new MaterialComponent(Osmiridium, 1)), EXT2_METAL, null, 8.0F, 5.0f, 5120, 7200);
    public static IngotMaterial NaquadahEnriched = new IngotMaterial(309, "naquadah_enriched", 0x282828, METALLIC, 4, of(), EXT_METAL | GENERATE_ORE, null, 6.0F, 4.0f, 1280, 4500);
    public static IngotMaterial Naquadria = new IngotMaterial(310, "naquadria", 0x1E1E1E, SHINY, 3, of(), EXT_METAL, Element.Nq, 9000);
    public static IngotMaterial Tritanium = new IngotMaterial(311, "tritanium", 0xFFFFFF, METALLIC, 6, of(), EXT_METAL, Element.Tr, 20.0F, 6.0f, 10240);//No usage
    public static IngotMaterial Duranium = new IngotMaterial(312, "duranium", 0xFFFFFF, METALLIC, 5, of(), EXT_METAL, Element.Dr, 16.0F, 5.0f, 5120);

    /**
     * Actual food
     */
    public static FluidMaterial Milk = new FluidMaterial(339, "milk", 0xFEFEFE, FINE, of(), 0);
    public static FluidMaterial Honey = new FluidMaterial(341, "honey", 0xD2C800, FLUID, of(), 0);
    public static FluidMaterial Juice = new FluidMaterial(473, "juice", 0xA8C972, FLUID, of(), 0);
    public static DustMaterial Cocoa = new DustMaterial(343, "cocoa", 0xBE5F00, ROUGH, 0, of(), 0);
    public static DustMaterial Wheat = new DustMaterial(345, "wheat", 0xFFFFC4, FINE, 0, of(), 0);

    static {
        for (DustMaterial dustMaterial : new DustMaterial[]{Bastnasite, Monazite}) {
            dustMaterial.separatedOnto = Neodymium;
        }
        for (DustMaterial dustMaterial : new DustMaterial[]{Magnetite, VanadiumMagnetite}) {
            dustMaterial.separatedOnto = Gold;
        }
        for (DustMaterial dustMaterial : new DustMaterial[]{YellowLimonite, BrownLimonite, Pyrite, BandedIron, Nickel, Glauconite, Pentlandite, Tin, Antimony, Ilmenite, Manganese, Chrome, Chromite, Andradite}) {
            dustMaterial.separatedOnto = Iron;
        }
        for (DustMaterial dustMaterial : new DustMaterial[]{Pyrite, YellowLimonite}) {
            dustMaterial.addFlag(BLAST_FURNACE_CALCITE_DOUBLE);
        }
        for (DustMaterial dustMaterial : new DustMaterial[]{Iron, PigIron, WroughtIron, BrownLimonite}) {
            dustMaterial.addFlag(BLAST_FURNACE_CALCITE_TRIPLE);
        }
        for (DustMaterial dustMaterial : new DustMaterial[]{Gold, Silver, Osmium, Platinum, Cooperite, Chalcopyrite, Bornite}) {
            dustMaterial.washedIn = Mercury;
        }
        for (DustMaterial dustMaterial : new DustMaterial[]{Zinc, Nickel, Copper, Cobalt, Cobaltite, Tetrahedrite, Sphalerite}) {
            dustMaterial.washedIn = SodiumPersulfate;
        }

        Tetrahedrite.setDirectSmelting(Copper);
        Malachite.setDirectSmelting(Copper);
        Chalcopyrite.setDirectSmelting(Copper);
        Tenorite.setDirectSmelting(Copper);
        Bornite.setDirectSmelting(Copper);
        Chalcocite.setDirectSmelting(Copper);
        Cuprite.setDirectSmelting(Copper);
        Pentlandite.setDirectSmelting(Nickel);
        Sphalerite.setDirectSmelting(Zinc);
        Pyrite.setDirectSmelting(Iron);
        Magnetite.setDirectSmelting(Iron);
        YellowLimonite.setDirectSmelting(Iron);
        BrownLimonite.setDirectSmelting(Iron);
        BandedIron.setDirectSmelting(Iron);
        Cassiterite.setDirectSmelting(Tin);
        Garnierite.setDirectSmelting(Nickel);
        Cobaltite.setDirectSmelting(Cobalt);
        Stibnite.setDirectSmelting(Antimony);
        Cooperite.setDirectSmelting(Platinum);
        Pyrolusite.setDirectSmelting(Manganese);
        Magnesite.setDirectSmelting(Magnesium);
        Molybdenite.setDirectSmelting(Molybdenum);

        Salt.setOreMultiplier(3);
        RockSalt.setOreMultiplier(3);
        Lepidolite.setOreMultiplier(5);

        Spodumene.setOreMultiplier(2);
        Spessartine.setOreMultiplier(2);
        Soapstone.setOreMultiplier(3);

        Almandine.setOreMultiplier(6);
        Grossular.setOreMultiplier(6);
        Bentonite.setOreMultiplier(7);
        Pyrope.setOreMultiplier(4);

        GarnetYellow.setOreMultiplier(4);
        GarnetRed.setOreMultiplier(4);
        Olivine.setOreMultiplier(2);
        Topaz.setOreMultiplier(2);

        Bastnasite.setOreMultiplier(2);
        Tennantite.setOreMultiplier(2);
        Enargite.setOreMultiplier(2);
        Tantalite.setOreMultiplier(2);
        Tanzanite.setOreMultiplier(2);
        Pitchblende.setOreMultiplier(2);

        Scheelite.setOreMultiplier(2);
        Tungstate.setOreMultiplier(2);
        Ilmenite.setOreMultiplier(3);
        Bauxite.setOreMultiplier(3);
        Rutile.setOreMultiplier(3);

        Cassiterite.setOreMultiplier(2);
        CertusQuartz.setOreMultiplier(2);

        Phosphor.setOreMultiplier(3);
        Saltpeter.setOreMultiplier(4);
        Apatite.setOreMultiplier(5);
        Apatite.setByProductMultiplier(2);
        Redstone.setOreMultiplier(6);

        Lapis.setOreMultiplier(6);
        Lapis.setByProductMultiplier(4);
        Sodalite.setOreMultiplier(6);
        Sodalite.setByProductMultiplier(4);
        Lazurite.setOreMultiplier(6);
        Lazurite.setByProductMultiplier(4);
        Monazite.setOreMultiplier(8);
        Monazite.setByProductMultiplier(2);

        Lignite.setBurnTime(1200); //2/3 of burn time of coal
        Coke.setBurnTime(3200); //2x burn time of coal
        Wood.setBurnTime(300); //default wood burn time in vanilla

        Tenorite.addOreByProducts(Iron, Manganese, Malachite);
        Bornite.addOreByProducts(Pyrite, Cobalt, Cadmium, Gold);
        Chalcocite.addOreByProducts(Sulfur, Lead, Silver);
        Cuprite.addOreByProducts(Iron, Antimony, Malachite);
        Enargite.addOreByProducts(Pyrite, Zinc, Quartzite);
        Tennantite.addOreByProducts(Iron, Antimony, Zinc);

        Chalcopyrite.addOreByProducts(Pyrite, Cobalt, Cadmium, Gold);
        Sphalerite.addOreByProducts(GarnetYellow, Cadmium, Gallium, Zinc);
        Glauconite.addOreByProducts(Sodium, Aluminium, Iron);
        Bentonite.addOreByProducts(Aluminium, Calcium, Magnesium);
        Uraninite.addOreByProducts(Uranium, Thorium, Uranium235);
        Pitchblende.addOreByProducts(Thorium, Uranium, Lead);
        Galena.addOreByProducts(Sulfur, Silver, Lead);
        Lapis.addOreByProducts(Lazurite, Sodalite, Pyrite);
        Pyrite.addOreByProducts(Sulfur, Phosphor, Iron);
        GarnetRed.addOreByProducts(Spessartine, Pyrope, Almandine);
        GarnetYellow.addOreByProducts(Andradite, Grossular, Uvarovite);
        Cooperite.addOreByProducts(Palladium, Nickel, Iridium);
        Cinnabar.addOreByProducts(Redstone, Sulfur, Glowstone);
        Tantalite.addOreByProducts(Manganese, Niobium, Tantalum);
        Asbestos.addOreByProducts(Asbestos, Silicon, Magnesium);
        Pentlandite.addOreByProducts(Iron, Sulfur, Cobalt);
        Uranium.addOreByProducts(Lead, Uranium235, Thorium);
        Scheelite.addOreByProducts(Manganese, Molybdenum, Calcium);
        Tungstate.addOreByProducts(Manganese, Silver, Lithium);
        Bauxite.addOreByProducts(Grossular, Rutile, Gallium);
        CertusQuartz.addOreByProducts(Quartzite, Barite);
        Redstone.addOreByProducts(Cinnabar, RareEarth, Glowstone);
        Monazite.addOreByProducts(Thorium, Neodymium, RareEarth);
        Malachite.addOreByProducts(Copper, BrownLimonite, Calcite);
        YellowLimonite.addOreByProducts(Nickel, BrownLimonite, Cobalt);
        BrownLimonite.addOreByProducts(Malachite, YellowLimonite);
        Bastnasite.addOreByProducts(Neodymium, RareEarth);
        Glowstone.addOreByProducts(Redstone, Gold);
        Lepidolite.addOreByProducts(Lithium, Caesium);
        Ilmenite.addOreByProducts(Iron, Rutile);
        Sapphire.addOreByProducts(Aluminium, GreenSapphire);
        GreenSapphire.addOreByProducts(Aluminium, Sapphire);
        Olivine.addOreByProducts(Pyrope, Magnesium, Manganese);
        Chromite.addOreByProducts(Iron, Magnesium);
        Tetrahedrite.addOreByProducts(Antimony, Zinc);
        Magnetite.addOreByProducts(Iron, Gold);
        Basalt.addOreByProducts(Olivine, DarkAsh);
        VanadiumMagnetite.addOreByProducts(Magnetite, Vanadium);
        Lazurite.addOreByProducts(Sodalite, Lapis);
        Sodalite.addOreByProducts(Lazurite, Lapis);
        Spodumene.addOreByProducts(Aluminium, Lithium);
        Ruby.addOreByProducts(Chrome, GarnetRed);
        Phosphor.addOreByProducts(Apatite, Phosphate);
        Pyrope.addOreByProducts(GarnetRed, Magnesium);
        Almandine.addOreByProducts(GarnetRed, Aluminium);
        Spessartine.addOreByProducts(GarnetRed, Manganese);
        Andradite.addOreByProducts(GarnetYellow, Iron);
        Grossular.addOreByProducts(GarnetYellow, Calcium);
        Uvarovite.addOreByProducts(GarnetYellow, Chrome);
        Calcite.addOreByProducts(Andradite, Malachite);
        NaquadahEnriched.addOreByProducts(Naquadah, Naquadria);
        Naquadah.addOreByProducts(NaquadahEnriched);
        Pyrolusite.addOreByProducts(Manganese);
        Molybdenite.addOreByProducts(Molybdenum);
        Stibnite.addOreByProducts(Antimony);
        Garnierite.addOreByProducts(Nickel);
        Lignite.addOreByProducts(Coal);
        Apatite.addOreByProducts(Phosphor);
        Magnesite.addOreByProducts(Magnesium);
        PigIron.addOreByProducts(Iron);
        Netherrack.addOreByProducts(Sulfur);
        Flint.addOreByProducts(Obsidian);
        Cobaltite.addOreByProducts(Cobalt);
        Saltpeter.addOreByProducts(Saltpeter);
        Endstone.addOreByProducts(Helium3);
        Obsidian.addOreByProducts(Olivine);
        Ash.addOreByProducts(Carbon);
        DarkAsh.addOreByProducts(Carbon);
        Redrock.addOreByProducts(Clay);
        Marble.addOreByProducts(Calcite);
        Clay.addOreByProducts(Clay);
        Cassiterite.addOreByProducts(Tin, Bismuth);
        GraniteBlack.addOreByProducts(Biotite);
        GraniteRed.addOreByProducts(PotassiumFeldspar);
        Phosphate.addOreByProducts(Phosphorus);
        Tanzanite.addOreByProducts(Opal);
        Opal.addOreByProducts(Tanzanite);
        Amethyst.addOreByProducts(Amethyst);
        Topaz.addOreByProducts(BlueTopaz);
        BlueTopaz.addOreByProducts(Topaz);
        Vinteum.addOreByProducts(Vinteum);
        Salt.addOreByProducts(RockSalt, Borax);
        RockSalt.addOreByProducts(Salt, Borax);
        Andesite.addOreByProducts(Basalt);
        Diorite.addOreByProducts(NetherQuartz);
        Lepidolite.addOreByProducts(Boron);

        Vinteum.addEnchantmentForTools(FORTUNE, 2);
        BlackBronze.addEnchantmentForTools(SMITE, 2);
        RoseGold.addEnchantmentForTools(SMITE, 4);
        BismuthBronze.addEnchantmentForTools(BANE_OF_ARTHROPODS, 5);

        SolderingAlloy.setCableProperties(GTValues.V[1], 1, 1);

        Cupronickel.setCableProperties(GTValues.V[2], 2, 3);

        Kanthal.setCableProperties(GTValues.V[3], 4, 3);

        Nichrome.setCableProperties(GTValues.V[4], 4, 4);
        BlackSteel.setCableProperties(GTValues.V[4], 3, 2);

        Graphene.setCableProperties(GTValues.V[5], 1, 1);
        TungstenSteel.setCableProperties(GTValues.V[5], 3, 2);

        HSSG.setCableProperties(GTValues.V[6], 4, 2);
        NiobiumTitanium.setCableProperties(GTValues.V[6], 4, 2);
        VanadiumGallium.setCableProperties(GTValues.V[6], 4, 2);
        YttriumBariumCuprate.setCableProperties(GTValues.V[6], 4, 4);

        Naquadah.setCableProperties(GTValues.V[7], 2, 2);

        NaquadahAlloy.setCableProperties(GTValues.V[8], 2, 4);
        Duranium.setCableProperties(GTValues.V[8], 1, 8);

        TungstenSteel.setFluidPipeProperties(300, 7500, true);

        Plastic.setFluidPipeProperties(200, 350, true);
        Polytetrafluoroethylene.setFluidPipeProperties(200, 600, true);
    }
}
