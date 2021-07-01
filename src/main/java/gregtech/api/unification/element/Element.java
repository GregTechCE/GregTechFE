package gregtech.api.unification.element;


import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Element {

    public static final Registry<Element> REGISTRY = FabricRegistryBuilder
            .createSimple(Element.class, new Identifier(GTValues.MODID, "elements"))
            .buildAndRegister();

    private final String chemicalSymbol;
    private final long protons;
    private final long neutrons;
    private final long mass;

    private final boolean isRadioactive;
    private final boolean isIsotope;

    public Element(Settings settings) {
        Preconditions.checkNotNull(settings.chemicalSymbol, "setBasicProperties is not called");

        this.chemicalSymbol = settings.chemicalSymbol;
        this.protons = settings.protons;
        this.neutrons = settings.neutrons;
        this.mass = settings.getMass();

        this.isRadioactive = settings.isRadioactive;
        this.isIsotope = settings.isIsotope;
    }

    public String getChemicalSymbol() {
        return chemicalSymbol;
    }

    public long getProtons() {
        return protons;
    }

    public long getNeutrons() {
        return neutrons;
    }

    public long getMass() {
        return mass;
    }

    public boolean isRadioactive() {
        return isRadioactive;
    }

    public boolean isIsotope() {
        return isIsotope;
    }

    public static class Settings {
        private long protons;
        private long neutrons;

        private boolean massOverride;
        private long mass;

        private String chemicalSymbol;
        private boolean isRadioactive;
        private boolean isIsotope;

        public Settings basicProperties(String chemicalSymbol, long protons, long neutrons) {
            Preconditions.checkNotNull(chemicalSymbol, "chemicalSymbol");
            Preconditions.checkArgument(protons > 0, "protons > 0");
            Preconditions.checkArgument(neutrons >= 0, "neutrons >= 0");

            this.chemicalSymbol = chemicalSymbol;
            this.protons = protons;
            this.neutrons = neutrons;
            return this;
        }

        public Settings mass(long mass) {
            Preconditions.checkArgument(mass > 0, "mass > 0");
            this.mass = mass;
            this.massOverride = true;
            return this;
        }

        public Settings radioactive() {
            this.isRadioactive = true;
            return this;
        }

        public Settings isotope() {
            this.isIsotope = true;
            return this;
        }

        private long getMass() {
            return this.massOverride ? this.mass : (this.protons + this.neutrons);
        }
    }

}
