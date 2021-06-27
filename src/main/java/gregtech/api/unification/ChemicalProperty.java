package gregtech.api.unification;

import gregtech.api.util.SmallDigits;

import java.util.HashSet;
import java.util.Set;

public class ChemicalProperty {
    private long mass;
    private String chemicalFormula;
    private boolean isRadioactive;
    private boolean isComposition;

    private ChemicalProperty() {
    }


    public long getMass() {
        return mass;
    }

    public String getChemicalFormula() {
        return chemicalFormula;
    }

    public boolean isRadioactive() {
        return isRadioactive;
    }

    public boolean isComposition() {
        return isComposition;
    }

    public static class Builder {
        private final Set<CountedChemicalProperty> chemicalComposition = new HashSet<>();
        private long mass;
        private boolean isComposition;
        private boolean isRadioactive;
        private String chemicalFormula;

        public ChemicalProperty.Builder isElement(String chemicalFormula) {
            this.chemicalFormula = chemicalFormula;
            this.isComposition = false;

            return this;
        }

        public ChemicalProperty.Builder isCompositionOf(Set<CountedChemicalProperty> countedChemicalProperties) {
            this.chemicalComposition.addAll(countedChemicalProperties);
            this.isComposition = true;

            return this;
        }

        public ChemicalProperty.Builder setMass(long mass) {
            this.mass = mass;

            return this;
        }

        public ChemicalProperty.Builder radioactive() {
            this.isRadioactive = true;

            return this;
        }

        public ChemicalProperty build() {
            var chemicalProperty = new ChemicalProperty();
            chemicalProperty.chemicalFormula = chemicalFormula != null ? chemicalFormula : generateChemicalFormula();
            chemicalProperty.mass = mass != 0 ? mass : calculateMass();
            chemicalProperty.isRadioactive = this.isRadioactive || calculateIsRadioactive();
            chemicalProperty.isComposition = this.isComposition;

            return chemicalProperty;
        }

        private String generateChemicalFormula() {
            var generatedChemicalFormula = new StringBuilder();

            for (CountedChemicalProperty countedChemicalProperty : chemicalComposition) {
                if (countedChemicalProperty.amount() > 1) {
                    if (countedChemicalProperty.chemicalProperty().isComposition()) {
                        generatedChemicalFormula.append('(').append(countedChemicalProperty.chemicalProperty().chemicalFormula).append(')');
                    } else {
                        generatedChemicalFormula.append(countedChemicalProperty.chemicalProperty().chemicalFormula);
                    }

                    generatedChemicalFormula.append(SmallDigits.toSmallDownNumbers(Long.toString(countedChemicalProperty.amount())));

                } else {
                    generatedChemicalFormula.append(countedChemicalProperty.chemicalProperty().chemicalFormula);
                }
            }

            return generatedChemicalFormula.toString();
        }

        private long calculateMass() {
            long calculatedMass = 0;

            for (CountedChemicalProperty countedChemicalProperty : chemicalComposition) {
                calculatedMass += countedChemicalProperty.chemicalProperty().mass * countedChemicalProperty.amount();
            }

            return calculatedMass;
        }

        private boolean calculateIsRadioactive() {
            for (CountedChemicalProperty countedChemicalProperty : chemicalComposition) {
                if (countedChemicalProperty.chemicalProperty().isRadioactive())
                    return true;
            }

            return false;
        }
    }
}
