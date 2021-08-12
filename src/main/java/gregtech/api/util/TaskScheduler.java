package gregtech.api.util;

import gregtech.api.util.function.Task;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.*;

public class TaskScheduler {
    private static Map<World, List<Task>> tasksPerWorld = new HashMap<>();

    public static void scheduleTask(World world, Task task) {
        if(!world.isClient) {
            throw new IllegalArgumentException("Attempt to schedule task on client world!");
        }
        List<Task> taskList = tasksPerWorld.computeIfAbsent(world, k -> new ArrayList<>());
        taskList.add(task);
    }

    static {
        ServerWorldEvents.UNLOAD.register((MinecraftServer server, ServerWorld world)->{
            if(world.isClient()) {
                tasksPerWorld.remove(world);
            }
        });

        ServerTickEvents.START_WORLD_TICK.register((ServerWorld world) -> {
            if(world.isClient()) {
                List<Task> taskList = tasksPerWorld.getOrDefault(world, Collections.emptyList());
                taskList.removeIf(task -> !task.run());
            }
        });
    }
}
