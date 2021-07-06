package gregtech.api.recipes.builders;

import com.google.common.base.Preconditions;
import gregtech.api.recipes.MachineRecipeTypes;
import gregtech.common.items.GTItems;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

public class ImplosionCompressorRecipeBuilder extends ElectricMachineRecipeBuilder {

    protected int explosivesAmount;

    public ImplosionCompressorRecipeBuilder(Identifier id) {
        super(id, MachineRecipeTypes.IMPLOSION_COMPRESSOR);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        super.copyFrom(otherBuilder);
        if (otherBuilder instanceof ImplosionCompressorRecipeBuilder other) {
            this.explosivesAmount = other.explosivesAmount;
        }
    }

    public ImplosionCompressorRecipeBuilder explosivesAmount(int explosivesAmount) {
        Preconditions.checkArgument(explosivesAmount > 0, "explosivesAmount should be positive");
        this.explosivesAmount = explosivesAmount;
        return this;
    }

    private MachineRecipeBuilder createExplosiveRecipe(String explosiveType, ItemConvertible explosive, int amount) {
        Identifier recipeId = new Identifier(this.id.getNamespace(), this.id.getPath() + "_" + explosiveType);

        ElectricMachineRecipeBuilder builder = new ElectricMachineRecipeBuilder(recipeId, this.recipeType);
        builder.copyFrom(this);
        return builder.input(explosive, amount);
    }

    @Override
    public Stream<MachineRecipeBuilder> flatMapBuilder() {
        if (this.explosivesAmount > 0) {
            int tntBlockAmount = Math.max(1, this.explosivesAmount / 2);
            int dynamiteAmount = Math.min(128, this.explosivesAmount * 2);

            MachineRecipeBuilder tntBlockRecipe = createExplosiveRecipe("tnt", Blocks.TNT, tntBlockAmount);
            MachineRecipeBuilder dynamiteRecipe = createExplosiveRecipe("dynamite", GTItems.DYNAMITE, dynamiteAmount);

            return Stream.of(tntBlockRecipe, dynamiteRecipe);
        }
        return super.flatMapBuilder();
    }
}
