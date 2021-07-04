package gregtech.api.recipes.context;

public interface GeneratorMachineContext extends RecipeContext {

    long getMaxVoltage();

    void setGeneratedEUt(int EUt);
}
