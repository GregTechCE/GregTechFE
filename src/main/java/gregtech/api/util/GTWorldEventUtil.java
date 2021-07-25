package gregtech.api.util;

import com.google.common.base.Preconditions;
import gregtech.api.network.PacketWorldEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class GTWorldEventUtil {

    /** Plays steam exhaust effect from steam machines. Data is id of the target side of the machine */
    public static final int WORLD_EVENT_STEAM_EXHAUST = 1000;

    public static void emitWorldEvent(ServerWorld world, int eventId, BlockPos pos, int data) {
        PacketWorldEvent packet = new PacketWorldEvent(eventId, pos, data);
        packet.sendToAllAround(world, Vec3d.ofCenter(pos), 64.0);
    }

    public static void handleWorldEvent(MinecraftClient client, PacketWorldEvent packet) {
        client.submit(() -> {
            ClientWorld world = Preconditions.checkNotNull(client.world);
            playWorldEvent(world, packet.getEventId(), packet.getPos(), packet.getData());
        });
    }

    @Environment(EnvType.CLIENT)
    public static void playWorldEvent(ClientWorld world, int eventId, BlockPos pos, int data) {
        Random random = world.getRandom();

        if (eventId == WORLD_EVENT_STEAM_EXHAUST) {
            Direction dir = Direction.byId(data);
            int particleCount = 2 + random.nextInt(3);

            for (int i = 0; i < particleCount; i++) {
                double particleSpeed = random.nextGaussian() * 0.4;

                world.addParticle(ParticleTypes.LARGE_SMOKE,
                        pos.getX() + random.nextGaussian(), pos.getY() + random.nextGaussian(), pos.getZ() + random.nextGaussian(),
                        dir.getOffsetX() * particleSpeed, dir.getOffsetY() * particleSpeed, dir.getOffsetZ() * particleSpeed);
            }
            world.playSound(pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0f, false);
        }
    }
}
