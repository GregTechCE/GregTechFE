package gregtech.api.module;

public interface MachineModuleConfig {

    abstract class ConfigurationBuilder {

        protected abstract void copyPropertiesFrom(ConfigurationBuilder builder);

        public abstract ConfigurationBuilder copy();

        public abstract MachineModuleConfig build();
    }
}
