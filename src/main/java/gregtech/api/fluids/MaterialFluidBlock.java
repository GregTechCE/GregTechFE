package gregtech.api.fluids;

import com.google.common.base.Preconditions;
import gregtech.api.unification.material.Material;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

public class MaterialFluidBlock extends FluidBlock {

    protected final Material material;
    protected final MaterialFluidHolder fluidHolder;
    protected final MaterialFluidProperties properties;
    protected final Text fluidName;

    public MaterialFluidBlock(Material material, MaterialFluidHolder fluidHolder, MaterialFluidProperties properties) {
        super(Preconditions.checkNotNull(fluidHolder.getStill()), createFluidBlockSettings(properties));
        this.material = material;
        this.fluidHolder = fluidHolder;
        this.properties = properties;
        this.fluidName = fluidHolder.getStill().getFluidName();
        this.fluidHolder.setBlock(this);
    }

    private static Settings createFluidBlockSettings(MaterialFluidProperties properties) {
        return FabricBlockSettings.of(properties.isHotFluid() ?
                net.minecraft.block.Material.LAVA :
                net.minecraft.block.Material.WATER)
                .strength(properties.getBlastResistance())
                .luminance(properties.getLuminosity())
                .noCollision().dropsNothing();
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public MutableText getName() {
        return new LiteralText("").append(this.fluidName);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.getTime() % 20 == 0L) {
            if (properties.isHotFluid()) {
                entity.setOnFireFromLava();
            }
            Pair<DamageSource, Float> damageToEntities = properties.getDamageToEntities();
            if (damageToEntities != null) {
                entity.damage(damageToEntities.getLeft(), damageToEntities.getRight());
            }
        }
    }
}
