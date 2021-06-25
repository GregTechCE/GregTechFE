package gregtech.api.unification.material.forms;

import gregtech.api.GTValues;
import gregtech.api.unification.stack.MaterialAmount;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialForm {
    private final String name;
    private final MaterialAmount materialAmount;

    private MaterialForm(String name, MaterialAmount materialAmount) {
        this.name = name;
        this.materialAmount = materialAmount;
    }


    public static class Builder {
        public static final Registry<MaterialForm> REGISTRY =
                FabricRegistryBuilder.createSimple(MaterialForm.class,
                        new Identifier(GTValues.MODID, "material_form")).buildAndRegister();


        private final String name;
        private MaterialAmount materialAmount = MaterialAmount.DUST;

        public Builder(String name) {
            this.name = name;
        }

        public MaterialForm.Builder materialAmount(MaterialAmount materialAmount) {
            this.materialAmount = materialAmount;
            return this;
        }

        public MaterialForm build() {
            if (!validate()) {
                return null;
            }

            var materialForm = new MaterialForm(name, materialAmount);

            return Registry.register(REGISTRY, new Identifier(GTValues.MODID, name), materialForm);
        }

        private boolean validate() {
            if (materialAmount.equals(MaterialAmount.ZERO)) {
                //TODO: log error
                return false;
            }

            return true;
        }
    }
}
