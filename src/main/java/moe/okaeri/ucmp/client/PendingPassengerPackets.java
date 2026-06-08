package moe.okaeri.ucmp.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PendingPassengerPackets {
	private static final Map<Integer, Pending> PENDING = new LinkedHashMap<>();
	private static final long TTL_MS = 3_000L;

	private PendingPassengerPackets() {}

	public static void store(EntityPassengersSetS2CPacket packet) {
		if (packet.getPassengerIds().length == 0) return;

		PENDING.put(packet.getEntityId(), new Pending(packet, System.currentTimeMillis()));
	}

	public static boolean canApply(EntityPassengersSetS2CPacket packet) {
		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;

		if (world == null) return false;

		Entity vehicle = world.getEntityById(packet.getEntityId());
		if (vehicle == null) return false;

		for (int passengerId : packet.getPassengerIds()) {
			if (world.getEntityById(passengerId) == null) return false;
		}

		return true;
	}

	public static void tryApplyAll() {
		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayNetworkHandler handler = client.getNetworkHandler();

		if (client.world == null || handler == null || PENDING.isEmpty()) return;

		long now = System.currentTimeMillis();

		ArrayList<EntityPassengersSetS2CPacket> ready = new ArrayList<>();

		Iterator<Map.Entry<Integer, Pending>> iterator = PENDING.entrySet().iterator();
		while (iterator.hasNext()) {
			Pending pending = iterator.next().getValue();

			if (now - pending.createdAtMs > TTL_MS) {
				iterator.remove();
				continue;
			}

			if (canApply(pending.packet)) {
				ready.add(pending.packet);
				iterator.remove();
			}
		}

		for (EntityPassengersSetS2CPacket packet : ready) {
			handler.onEntityPassengersSet(packet);
		}
	}

	public static void clear() {
		PENDING.clear();
	}

	private record Pending(EntityPassengersSetS2CPacket packet, long createdAtMs) {}
}