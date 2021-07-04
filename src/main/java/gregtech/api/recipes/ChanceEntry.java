package gregtech.api.recipes;

import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;

public class ChanceEntry {

    public static final int MAX_CHANCE = 10000;
    private final ItemStack itemStack;
    private final int chance;
    private final int boostPerTier;

    public ChanceEntry(ItemStack itemStack, int chance, int boostPerTier) {
        Preconditions.checkArgument(chance > 0, "chance (%d) is lower than or equals to zero", chance);
        Preconditions.checkArgument(chance <= MAX_CHANCE, "chance (%d) is bigger than maximum: %d", chance, MAX_CHANCE);

        this.itemStack = itemStack.copy();
        this.chance = chance;
        this.boostPerTier = boostPerTier;
    }

    public ItemStack getItemStack() {
        return itemStack.copy();
    }

    public int getChance() {
        return chance;
    }

    public int getBoostPerTier() {
        return boostPerTier;
    }

    public boolean rollChanceEntry(RecipeContext context) {
        int totalChance = this.chance + this.boostPerTier * context.getTierForBoosting();
        return context.getRandom().nextInt(MAX_CHANCE) <= totalChance;
    }
}
