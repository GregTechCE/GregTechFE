package gregtech.api.unification.material.forms;

import gregtech.api.GTValues;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.MaterialAmount;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialForm {
    private MaterialAmount materialAmount;

    private MaterialForm() {
    }

    public MaterialAmount getMaterialAmount() {
        return materialAmount;
    }

    public Tag.Identified<Item> getItemTag(Material material) {

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

            var materialForm = new MaterialForm();
            materialForm.materialAmount = this.materialAmount;

            return Registry.register(REGISTRY, new Identifier(GTValues.MODID, this.name), materialForm);
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
