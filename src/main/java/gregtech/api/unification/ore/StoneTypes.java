package gregtech.api.unification.ore;

import gregtech.api.unification.Materials;
import net.minecraft.block.Blocks;

public class StoneTypes {

    public static StoneType STONE = new StoneType(0, "stone", new ResourceLocation("blocks/stone"), SoundType.STONE, MaterialForm.ore, Materials.Stone, "pickaxe", 0,
        () -> Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, EnumType.STONE),
        state -> state.getBlock() instanceof BlockStone && state.getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE);

    public static StoneType GRAVEL = new StoneType(4, "gravel", new ResourceLocation("blocks/gravel"), SoundType.SAND, MaterialForm.oreGravel, Materials.Flint, "shovel", AFFECTED_BY_GRAVITY,
        Blocks.GRAVEL::getDefaultState,
        state -> state.getBlock() instanceof BlockGravel);

    public static StoneType BEDROCK = new StoneType(5, "bedrock", new ResourceLocation("blocks/bedrock"), SoundType.STONE, MaterialForm.ore, Materials.Stone, "pickaxe", UNBREAKABLE,
        Blocks.BEDROCK::getDefaultState,
        state -> state.getBlock() == Blocks.BEDROCK);

    public static StoneType NETHERRACK = new StoneType(6, "netherrack", new ResourceLocation("blocks/netherrack"), SoundType.STONE, MaterialForm.oreNetherrack, Materials.Netherrack, "pickaxe", 0,
        Blocks.NETHERRACK::getDefaultState,
        state -> state.getBlock() == Blocks.NETHERRACK);

    public static StoneType ENDSTONE = new StoneType(7, "endstone", new ResourceLocation("blocks/end_stone"), SoundType.STONE, MaterialForm.oreEndstone, Materials.Endstone, "pickaxe", 0,
        Blocks.END_STONE::getDefaultState,
        state -> state.getBlock() == Blocks.END_STONE);

    public static StoneType SANDSTONE = new StoneType(8, "sandstone", new ResourceLocation("blocks/sandstone_normal"), new ResourceLocation("blocks/sandstone_top"), SoundType.STONE, MaterialForm.oreSand, Materials.SiliconDioxide, "pickaxe", 0,
        () -> Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.DEFAULT),
        state -> state.getBlock() instanceof BlockSandStone && state.getValue(BlockSandStone.TYPE) == BlockSandStone.EnumType.DEFAULT);
}
