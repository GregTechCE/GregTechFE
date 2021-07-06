package gregtech.api.recipes.builders;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gregtech.api.items.material.MaterialItemForm;
import gregtech.api.items.material.MaterialItemRegistry;
import gregtech.api.recipes.MachineRecipeType;
import gregtech.api.recipes.RecipeSerializer;
import gregtech.api.recipes.recipes.RecipeSerializers;
import gregtech.api.recipes.util.ChanceEntry;
import gregtech.api.recipes.util.CountableIngredient;
import gregtech.api.unification.forms.MaterialForm;
import gregtech.api.unification.material.Material;
import gregtech.api.util.JsonUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class BasicMachineRecipeBuilder extends AbstractMachineRecipeBuilder {

    protected final List<CountableIngredient> inputs = new ArrayList<>();
    protected final List<FluidVolume> fluidInputs = new ArrayList<>();

    protected final List<ItemStack> outputs = new ArrayList<>();
    protected final List<ChanceEntry> chancedOutputs = new ArrayList<>();
    protected final List<FluidVolume> fluidOutputs = new ArrayList<>();
    protected int duration;

    public BasicMachineRecipeBuilder(Identifier id, MachineRecipeType recipeType) {
        super(id, recipeType);
    }

    //COPY PROPERTIES METHODS

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        if (otherBuilder instanceof BasicMachineRecipeBuilder other) {
            this.inputs.addAll(other.inputs);
            this.fluidInputs.addAll(other.fluidInputs);

            this.outputs.addAll(other.outputs);
            this.chancedOutputs.addAll(other.chancedOutputs);
            this.fluidOutputs.addAll(other.fluidOutputs);

            this.duration = other.duration;
        }
    }

    //STANDARD INPUT INGREDIENT METHODS

    public BasicMachineRecipeBuilder input(MaterialForm materialForm, Material material, int amount) {
        return input(materialForm.getItemTag(material), amount);
    }

    public BasicMachineRecipeBuilder input(ItemConvertible item, int amount) {
        return input(new ItemStack(item), amount);
    }

    public BasicMachineRecipeBuilder input(Tag.Identified<Item> tag, int amount) {
        return input(Ingredient.fromTag(tag), amount);
    }

    public BasicMachineRecipeBuilder input(ItemStack itemStack, int amount) {
        Preconditions.checkArgument(!itemStack.isEmpty(), "Provided ItemStack is empty");
        return input(Ingredient.ofStacks(itemStack), amount);
    }

    public BasicMachineRecipeBuilder input(Ingredient ingredient, int amount) {
        this.inputs.add(CountableIngredient.from(ingredient, amount));
        return this;
    }

    //NOT CONSUMABLE INPUT INGREDIENT METHODS

    public BasicMachineRecipeBuilder notConsumable(MaterialForm materialForm, Material material, int amount) {
        return notConsumable(materialForm.getItemTag(material), amount);
    }

    public BasicMachineRecipeBuilder notConsumable(ItemConvertible item) {
        return notConsumable(item, 1);
    }

    public BasicMachineRecipeBuilder notConsumable(ItemConvertible item, int amount) {
        return notConsumable(new ItemStack(item), amount);
    }

    public BasicMachineRecipeBuilder notConsumable(Tag.Identified<Item> tag, int amount) {
        return notConsumable(Ingredient.fromTag(tag), amount);
    }

    public BasicMachineRecipeBuilder notConsumable(ItemStack itemStack, int amount) {
        Preconditions.checkArgument(!itemStack.isEmpty(), "Provided ItemStack is empty");
        return notConsumable(Ingredient.ofStacks(itemStack), amount);
    }

    public BasicMachineRecipeBuilder notConsumable(Ingredient ingredient, int amount) {
        this.inputs.add(CountableIngredient.notConsumed(ingredient, amount));
        return this;
    }

    //FLUID INPUT INGREDIENT METHODS

    public BasicMachineRecipeBuilder fluidInput(FluidVolume fluidVolume) {
        Preconditions.checkArgument(!fluidVolume.isEmpty(), "Provided FluidVolume is empty");
        this.fluidInputs.add(fluidVolume.copy());
        return this;
    }

    //ITEM OUTPUTS METHODS

    public BasicMachineRecipeBuilder output(MaterialItemForm form, Material material, int amount) {
        Preconditions.checkArgument(form.shouldGenerateFor(material), "Form %s does not generate for material %s", form, material);
        return output(MaterialItemRegistry.INSTANCE.getMaterialItem(form, material, amount));
    }

    public BasicMachineRecipeBuilder output(ItemConvertible item, int amount) {
        return output(new ItemStack(item, amount));
    }

    public BasicMachineRecipeBuilder output(ItemStack itemStack) {
        Preconditions.checkArgument(!itemStack.isEmpty(), "Provided ItemStack is empty");
        this.outputs.add(itemStack.copy());
        return this;
    }

    //CHANCED ITEM OUTPUTS METHODS

    public BasicMachineRecipeBuilder chancedOutput(MaterialItemForm form, Material material, int amount, int chance, int boostPerTier) {
        Preconditions.checkArgument(form.shouldGenerateFor(material), "Form %s does not generate for material %s", form, material);
        return chancedOutput(MaterialItemRegistry.INSTANCE.getMaterialItem(form, material, amount), chance, boostPerTier);
    }

    public BasicMachineRecipeBuilder chancedOutput(ItemConvertible item, int amount, int chance, int boostPerTier) {
        return chancedOutput(new ItemStack(item, amount), chance, boostPerTier);
    }

    public BasicMachineRecipeBuilder chancedOutput(ItemStack itemStack, int chance, int boostPerTier) {
        Preconditions.checkArgument(!itemStack.isEmpty(), "Provided ItemStack is empty");
        this.chancedOutputs.add(new ChanceEntry(itemStack, chance, boostPerTier));
        return this;
    }

    //FLUID OUTPUTS METHODS

    public BasicMachineRecipeBuilder fluidOutput(FluidVolume fluidVolume) {
        Preconditions.checkArgument(!fluidVolume.isEmpty(), "Provided FluidVolume is empty");
        this.fluidOutputs.add(fluidVolume.copy());
        return this;
    }

    //DURATION METHODS

    public BasicMachineRecipeBuilder duration(int duration) {
        Preconditions.checkArgument(duration > 0, "Duration should be positive");
        this.duration = duration;
        return this;
    }

    //SERIALIZATION RELATED METHODS

    @Override
    protected RecipeSerializer<?> getRecipeSerializer() {
        return RecipeSerializers.BASIC_MACHINE_RECIPE;
    }

    protected void addExtraBasicRecipeProperties(JsonObject jsonObject) {
    }

    @Override
    protected final void writeRecipeTypeData(JsonObject jsonObject) {
        Preconditions.checkArgument(this.duration > 0, "Duration not set on the recipe builder");
        jsonObject.addProperty("duration", this.duration);

        addExtraBasicRecipeProperties(jsonObject);

        if (!this.inputs.isEmpty()) {
            JsonArray inputsArray = new JsonArray();
            this.inputs.forEach(ingredient -> inputsArray.add(ingredient.toJson()));
            jsonObject.add("inputs", inputsArray);
        }

        if (!this.fluidInputs.isEmpty()) {
            JsonArray fluidInputsArray = new JsonArray();
            this.fluidInputs.forEach(input -> fluidInputsArray.add(input.toJson()));
            jsonObject.add("fluid_inputs", fluidInputsArray);
        }

        if (!this.outputs.isEmpty()) {
            JsonArray outputsArray = new JsonArray();
            this.outputs.forEach(output -> outputsArray.add(JsonUtil.writeItemStack(output)));
            jsonObject.add("outputs", outputsArray);
        }

        if (!this.chancedOutputs.isEmpty()) {
            JsonArray chancedOutputsArray = new JsonArray();
            this.chancedOutputs.forEach(output -> chancedOutputsArray.add(output.toJson()));
            jsonObject.add("chanced_outputs", chancedOutputsArray);
        }

        if (!this.fluidOutputs.isEmpty()) {
            JsonArray fluidOutputsArray = new JsonArray();
            this.fluidOutputs.forEach(output -> fluidOutputsArray.add(output.toJson()));
            jsonObject.add("fluid_outputs", fluidOutputsArray);
        }
    }
}
