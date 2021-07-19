package gregtech.api.block.machine;

import net.minecraft.world.World;

public enum MachineTickType {
    CLIENT,
    SERVER;

    public static MachineTickType of(World world) {
        return world.isClient() ? CLIENT : SERVER;
    }

    public boolean isClient() {
        return this == CLIENT;
    }

    public boolean isServer() {
        return this == SERVER;
    }
}
