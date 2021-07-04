package gregtech.api.recipes.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gregtech.api.recipes.context.RecipeContext;
import gregtech.api.util.JsonUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;

public class ChanceEntry {

    public static final int MAX_CHANCE = 10000;
    private final ItemStack itemStack;
    private final int chance;
    private final int boostPerTier;

    public ChanceEntry(ItemStack itemStack, int chance, int boostPerTier) {
        Preconditions.checkArgument(chance > 0, "chance (%d) is lower than or equals to zero", chance);
        Preconditions.checkArgument(chance <= MAX_CHANCE, "chance (%d) is bigger than maximum: %d", chance, MAX_CHANCE);
        Preconditions.checkArgument(boostPerTier >= 0, "boostPerTier (%d) is lower than zero", boostPerTier);

        this.itemStack = itemStack.copy();
        this.chance = chance;
        this.boostPerTier = boostPerTier;
    }

    public static ChanceEntry fromPacket(PacketByteBuf buf) {
        ItemStack itemStack = buf.readItemStack();
        int chance = buf.readVarInt();
        int boostPerTier = buf.readVarInt();

        return new ChanceEntry(itemStack, chance, boostPerTier);
    }

    public static ChanceEntry fromJson(JsonObject jsonObject) {
        ItemStack itemStack = JsonUtil.readItemStack(JsonHelper.getObject(jsonObject, "item"));
        int chance = JsonHelper.getInt(jsonObject, "chance");
        int boostPerTier = JsonHelper.getInt(jsonObject, "boost_per_tier", 0);

        return new ChanceEntry(itemStack, chance, boostPerTier);
    }

    public void toPacket(PacketByteBuf buf) {
        buf.writeItemStack(this.itemStack);
        buf.writeVarInt(this.chance);
        buf.writeVarInt(this.boostPerTier);
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("item", JsonUtil.writeItemStack(this.itemStack));
        jsonObject.addProperty("chance", this.chance);

        if (this.boostPerTier > 0) {
            jsonObject.addProperty("boost_per_tier", this.boostPerTier);
        }
        return jsonObject;
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
