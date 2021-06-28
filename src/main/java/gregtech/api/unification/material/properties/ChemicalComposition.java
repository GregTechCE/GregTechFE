package gregtech.api.unification.material.properties;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import gregtech.api.unification.Element;
import gregtech.api.unification.Elements;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChemicalComposition {

    public static final ChemicalComposition EMPTY = new ChemicalComposition(null, Collections.emptyList());

    private final Element element;
    private final List<MaterialComponent> components;

    private final ChemicalProperties chemicalProperties;
    private final String chemicalFormula;

    private ChemicalComposition(Element element, List<MaterialComponent> components) {
        this.element = element;
        this.components = ImmutableList.copyOf(components);

        this.chemicalProperties = computeChemicalProperties();
        this.chemicalFormula = computeChemicalFormula();
    }

    public static ChemicalComposition element(Element element) {
        Preconditions.checkNotNull(element);
        return new ChemicalComposition(element, Collections.emptyList());
    }

    public static ChemicalComposition composite(MaterialComponent... components) {
        Preconditions.checkArgument(components.length > 0);
        return new ChemicalComposition(null, Arrays.asList(components));
    }

    public boolean isElement() {
        return this.element != null;
    }

    public boolean isCompound() {
        return !this.components.isEmpty();
    }

    public boolean isEmpty() {
        return this.element == null && this.components.isEmpty();
    }

    @Nullable
    public Element getElement() {
        return element;
    }

    public List<MaterialComponent> getComponents() {
        return components;
    }

    public ChemicalProperties getChemicalProperties() {
        return chemicalProperties;
    }

    public String getChemicalFormula() {
        return chemicalFormula;
    }

    private ChemicalProperties computeChemicalProperties() {
        if (element != null) {
            return new ChemicalProperties(element);
        }
        if (components.isEmpty()) {
            return new ChemicalProperties(Elements.Tc);
        }

        ChemicalProperties properties = new ChemicalProperties();

        for (MaterialComponent component : components) {
            int componentAmount = component.getAmount();
            ChemicalProperties other = component.getMaterial()
                    .queryPropertyChecked(MaterialProperties.CHEMICAL_COMPOSITION)
                    .getChemicalProperties();

            properties.protons += other.protons * componentAmount;
            properties.neutrons += other.neutrons * componentAmount;
            properties.mass += other.mass * componentAmount;
            properties.isRadioactive |= other.isRadioactive;

            properties.componentAmount++;
        }
        return properties;
    }

    private String computeChemicalFormula() {
        if (element != null) {
            return element.getElementSymbol();
        }

        if (!components.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            for (MaterialComponent component : components) {
                builder.append(component.toString());
            }
            return builder.toString();
        }
        return "";
    }

    public static class ChemicalProperties {
        private long protons;
        private long neutrons;
        private long mass;
        private boolean isRadioactive;
        private int componentAmount;

        private ChemicalProperties(Element element) {
            this.protons = element.getProtons();
            this.neutrons = element.getNeutrons();
            this.mass = element.getMass();
            this.isRadioactive = element.isRadioactive();
            this.componentAmount = 1;
        }

        private ChemicalProperties() {
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

        public long getAverageMass() {
            return mass / componentAmount;
        }
    }
}
