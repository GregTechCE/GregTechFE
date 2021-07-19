package gregtech.api.block.machine.module;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import gregtech.api.GTValues;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.render.model.state.ModelStateManager;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class MachineModuleType<C extends MachineModuleConfig, T extends MachineModule<C>> {

    @SuppressWarnings("unchecked")
    public static final Registry<MachineModuleType<?, ?>> REGISTRY = FabricRegistryBuilder
            .createSimple((Class<MachineModuleType<?, ?>>) (Object) MachineModuleType.class, new Identifier(GTValues.MODID, "machine_modules"))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    private final Class<C> configClass;
    private final ModuleFactory<C, T> moduleFactory;
    private final List<Pair<Property<?>, Comparable<?>>> modelStateProperties;
    private final boolean needsClientTicking;
    private final boolean needsServerTicking;

    public MachineModuleType(Settings<C, T> settings) {
        Preconditions.checkNotNull(settings.moduleFactory, "moduleFactory not set");
        Preconditions.checkNotNull(settings.configClass, "configClass not set");
        this.configClass = settings.configClass;
        this.moduleFactory = settings.moduleFactory;
        this.modelStateProperties = ImmutableList.copyOf(settings.modelStateProperties);
        this.needsClientTicking = settings.needsClientTicking;
        this.needsServerTicking = settings.needsServerTicking;
    }

    public Identifier getId() {
        return Preconditions.checkNotNull(REGISTRY.getId(this), "MachineModuleType not registered");
    }

    @SuppressWarnings("unchecked")
    public T cast(MachineModule<?> machineModule) {
        Preconditions.checkArgument(machineModule.getType() == this);
        return (T) machineModule;
    }

    public C castConfig(MachineModuleConfig config) {
        return this.configClass.cast(config);
    }

    public void appendModelProperties(ModelStateManager.Builder<?> stateManager, C config) {
        for (var pair : this.modelStateProperties) {
            appendModelProperty(stateManager, pair.getFirst(), pair.getSecond());
        }
    }

    public boolean needsTicking(MachineTickType tickType, C config) {
        if (tickType.isClient()) {
            return this.needsClientTicking;
        }
        if (tickType.isServer()) {
            return this.needsServerTicking;
        }
        return false;
    }

    private static <C extends Comparable<C>> void appendModelProperty(ModelStateManager.Builder<?> stateManager, Property<C> property, Object object) {
        stateManager.property(property, property.getType().cast(object));
    }

    public T createModule(MachineBlockEntity machine, C config) {
        return this.moduleFactory.create(machine, this, config);
    }

    public interface ModuleFactory<C extends MachineModuleConfig, T extends MachineModule<C>> {
        T create(MachineBlockEntity machine, MachineModuleType<C, T> type, C config);
    }

    public static class Settings<C extends MachineModuleConfig, T extends MachineModule<C>> {
        private Class<C> configClass;
        private ModuleFactory<C, T> moduleFactory;
        private final List<Pair<Property<?>, Comparable<?>>> modelStateProperties = new ArrayList<>();
        private boolean needsClientTicking;
        private boolean needsServerTicking;

        public Settings<C, T> configClass(Class<C> configClass) {
            Preconditions.checkNotNull(configClass, "configClass");
            this.configClass = configClass;
            return this;
        }

        public Settings<C, T> factory(ModuleFactory<C, T> factory) {
            Preconditions.checkNotNull(factory, "factory");
            this.moduleFactory = factory;
            return this;
        }

        public Settings<C, T> needsClientTicking() {
            this.needsClientTicking = true;
            return this;
        }

        public Settings<C, T> needsServerTicking() {
            this.needsServerTicking = true;
            return this;
        }

        public <F extends Comparable<F>> Settings<C, T> modelStateProperty(Property<F> property, F defaultValue) {
            Preconditions.checkNotNull(property, "property");
            Preconditions.checkNotNull(defaultValue, "defaultValue");
            this.modelStateProperties.add(Pair.of(property, defaultValue));
            return this;
        }
    }
}
