package gregtech.api.unification.element;

import gregtech.api.GTValues;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Elements {

    public static final Element H;
    public static final Element Be;
    public static final Element B;
    public static final Element C;
    public static final Element O;
    public static final Element F;
    public static final Element Al;
    public static final Element Si;
    public static final Element Cl;
    public static final Element Ar;
    public static final Element Ca;
    public static final Element Cr;
    public static final Element Mn;
    public static final Element Fe;
    public static final Element Co;
    public static final Element Ni;
    public static final Element Cu;
    public static final Element Zn;
    public static final Element As;
    public static final Element Tc;
    public static final Element Ag;
    public static final Element Cd;
    public static final Element Sn;
    public static final Element Sb;
    public static final Element Ba;
    public static final Element Au;
    public static final Element Bi;


    static {

        H = register("hydrogen", new Element(new Element.Settings().basicProperties("H", 1, 0)));

        Be = register("beryllium", new Element(new Element.Settings().basicProperties("Be", 4, 5)));
        B = register("boron", new Element(new Element.Settings().basicProperties("B", 5, 5)));
        C = register("carbon", new Element(new Element.Settings().basicProperties("C", 6, 6)));

        O = register("oxygen", new Element(new Element.Settings().basicProperties("O", 8, 8)));
        F = register("fluorine", new Element(new Element.Settings().basicProperties("F", 9, 9)));

        Al = register("aluminium", new Element(new Element.Settings().basicProperties("Al", 13, 13)));
        Si = register("silicon", new Element(new Element.Settings().basicProperties("Si", 14, 14)));

        Cl = register("chlorine", new Element(new Element.Settings().basicProperties("Cl", 17, 18)));
        Ar = register("argon", new Element(new Element.Settings().basicProperties("Ar", 18, 22)));

        Ca = register("calcium", new Element(new Element.Settings().basicProperties("Ca", 20, 20)));

        Cr = register("chromium", new Element(new Element.Settings().basicProperties("Cr", 24, 28)));
        Mn = register("manganese", new Element(new Element.Settings().basicProperties("Mn", 25, 30)));
        Fe = register("iron", new Element(new Element.Settings().basicProperties("Fe", 26, 30)));
        Co = register("cobalt", new Element(new Element.Settings().basicProperties("Co", 27, 32)));
        Ni = register("nickel", new Element(new Element.Settings().basicProperties("Ni", 28, 30)));
        Cu = register("copper", new Element(new Element.Settings().basicProperties("Cu", 29, 34)));
        Zn = register("zinc", new Element(new Element.Settings().basicProperties("Zn", 30, 35)));

        As = register("arsenic", new Element(new Element.Settings().basicProperties("As", 33, 42)));

        Tc = register("technetium", new Element(new Element.Settings().basicProperties("Tc", 43, 55)));

        Ag = register("silver", new Element(new Element.Settings().basicProperties("Ag", 47, 60)));
        Cd = register("cadmium", new Element(new Element.Settings().basicProperties("Ca", 48, 64)));

        Sn = register("tin", new Element(new Element.Settings().basicProperties("Sn", 50, 68)));
        Sb = register("antimony", new Element(new Element.Settings().basicProperties("Sb", 51, 70)));

        Ba = register("barium", new Element(new Element.Settings().basicProperties("Ba", 56, 81)));

        Au = register("gold", new Element(new Element.Settings().basicProperties("Au", 79, 117)));

        Bi = register("bismuth", new Element(new Element.Settings().basicProperties("Bi", 83, 125)));
    }

    private Elements() {
    }

    private static Element register(String name, Element element) {
        return Registry.register(Element.REGISTRY, new Identifier(GTValues.MODID, name), element);
    }
}

//    D(1, 1, -1, "H", "Deuterium", true),
//    T(1, 2, -1, "D", "Tritium", true),
//    He(2, 2, -1, null, "Helium", false),
//    He_3(2, 1, -1, "H&D", "Helium-3", true),
//    Li(3, 4, -1, null, "Lithium", false),
//    N(7, 7, -1, null, "Nitrogen", false),
//    Ne(10, 10, -1, null, "Neon", false),
//    Na(11, 11, -1, null, "Sodium", false),
//    Mg(12, 12, -1, null, "Magnesium", false),
//    P(15, 15, -1, null, "Phosphor", false),
//    S(16, 16, -1, null, "Sulfur", false),
//    K(19, 20, -1, null, "Potassium", false),
//    Sc(21, 24, -1, null, "Scandium", false),
//    Ti(22, 26, -1, null, "Titanium", false),
//    V(23, 28, -1, null, "Vanadium", false),
//    Ga(31, 39, -1, null, "Gallium", false),
//    Ge(32, 40, -1, null, "Germanium", false),
//    Se(34, 45, -1, null, "Selenium", false),
//    Br(35, 45, -1, null, "Bromine", false),
//    Kr(36, 48, -1, null, "Krypton", false),
//    Rb(37, 48, -1, null, "Rubidium", false),
//    Sr(38, 49, -1, null, "Strontium", false),
//    Y(39, 50, -1, null, "Yttrium", false),
//    Zr(40, 51, -1, null, "Zirconium", false),
//    Nb(41, 53, -1, null, "Niobium", false),
//    Mo(42, 53, -1, null, "Molybdenum", false),
//    Ru(44, 57, -1, null, "Ruthenium", false),
//    Rh(45, 58, -1, null, "Rhodium", false),
//    Pd(46, 60, -1, null, "Palladium", false),
//    In(49, 65, -1, null, "Indium", false),
//    Te(52, 75, -1, null, "Tellurium", false),
//    I(53, 74, -1, null, "Iodine", false),
//    Xe(54, 77, -1, null, "Xenon", false),
//    Cs(55, 77, -1, null, "Caesium", false),
//    La(57, 81, -1, null, "Lantanium", false),
//    Ce(58, 82, -1, null, "Cerium", false),
//    Pr(59, 81, -1, null, "Praseodymium", false),
//    Nd(60, 84, -1, null, "Neodymium", false),
//    Pm(61, 83, -1, null, "Promethium", false),
//    Sm(62, 88, -1, null, "Samarium", false),
//    Eu(63, 88, -1, null, "Europium", false),
//    Gd(64, 93, -1, null, "Gadolinium", false),
//    Tb(65, 93, -1, null, "Terbium", false),
//    Dy(66, 96, -1, null, "Dysprosium", false),
//    Ho(67, 97, -1, null, "Holmium", false),
//    Er(68, 99, -1, null, "Erbium", false),
//    Tm(69, 99, -1, null, "Thulium", false),
//    Yb(70, 103, -1, null, "Ytterbium", false),
//    Lu(71, 103, -1, null, "Lutetium", false),
//    Hf(72, 106, -1, null, "Hafnium", false),
//    Ta(73, 107, -1, null, "Tantalum", false),
//    W(74, 109, -1, null, "Wolframium", false),
//    Re(75, 111, -1, null, "Rhenium", false),
//    Os(76, 114, -1, null, "Osmium", false),
//    Ir(77, 115, -1, null, "Iridium", false),
//    Pt(78, 117, -1, null, "Platinum", false),
//    Hg(80, 120, -1, null, "Mercury", false),
//    Tl(81, 123, -1, null, "Thallium", false),
//    Pb(82, 125, -1, null, "Lead", false),
//    Po(84, 124, -1, null, "Polonium", false),
//    At(85, 124, -1, null, "Astatine", false),
//    Rn(86, 134, -1, null, "Radon", false),
//    Fr(87, 134, -1, null, "Francium", false),
//    Ra(88, 136, -1, null, "Radium", false),
//    Ac(89, 136, -1, null, "Actinium", false),
//    Th(90, 140, -1, null, "Thorium", false),
//    Pa(91, 138, -1, null, "Protactinium", false),
//    U(92, 146, -1, null, "Uranium", false),
//    U_235(92, 143, -1, null, "Uranium-235", true),
//    Np(93, 144, -1, null, "Neptunium", false),
//    Pu(94, 152, -1, null, "Plutonium", false),
//    Pu_241(94, 149, -1, null, "Plutonium-241", true),
//    Am(95, 150, -1, null, "Americium", false),
//    Cm(96, 153, -1, null, "Curium", false),
//    Bk(97, 152, -1, null, "Berkelium", false),
//    Cf(98, 153, -1, null, "Californium", false),
//    Es(99, 153, -1, null, "Einsteinium", false),
//    Fm(100, 157, -1, null, "Fermium", false),
//    Md(101, 157, -1, null, "Mendelevium", false),
//    No(102, 157, -1, null, "Nobelium", false),
//    Lr(103, 159, -1, null, "Lawrencium", false),
//    Rf(104, 161, -1, null, "Rutherfordium", false),
//    Db(105, 163, -1, null, "Dubnium", false),
//    Sg(106, 165, -1, null, "Seaborgium", false),
//    Bh(107, 163, -1, null, "Bohrium", false),
//    Hs(108, 169, -1, null, "Hassium", false),
//    Mt(109, 167, -1, null, "Meitnerium", false),
//    Ds(110, 171, -1, null, "Darmstadtium", false),
//    Rg(111, 169, -1, null, "Roentgenium", false),
//    Cn(112, 173, -1, null, "Copernicium", false),
//    Nh(113, 171, -1, null, "Nihonium", false),
//    Fl(114, 175, -1, null, "Flerovium", false),
//    Mc(115, 173, -1, null, "Moscovium", false),
//    Lv(116, 177, -1, null, "Livermorium", false),
//    Ts(117, 177, -1, null, "Tennessine", false),
//    Og(118, 176, -1, null, "Oganesson", false),
//    //stargate
//    Tr(119, 178, -1, null, "Tritanium", false),
//    Dr(120, 180, -1, null, "Duranium", false),
//    Nq(121, 172, 140, null, "Naquadah", true);

//    Element(long protons, long neutrons, long halfLifeSeconds, String decayTo, String name, boolean isIsotope)
