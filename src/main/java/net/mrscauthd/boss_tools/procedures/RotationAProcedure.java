package net.mrscauthd.boss_tools.procedures;

import net.mrscauthd.boss_tools.entity.RocketTier3Entity;
import net.mrscauthd.boss_tools.entity.RocketTier2Entity;
import net.mrscauthd.boss_tools.entity.RocketEntity;
import net.mrscauthd.boss_tools.BossToolsModElements;
import net.mrscauthd.boss_tools.BossToolsMod;

import net.minecraft.entity.Entity;

import java.util.Map;

@BossToolsModElements.ModElement.Tag
public class RotationAProcedure extends BossToolsModElements.ModElement {
	public RotationAProcedure(BossToolsModElements instance) {
		super(instance, 322);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				BossToolsMod.LOGGER.warn("Failed to load dependency entity for procedure RotationA!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		if (((entity.getRidingEntity()) instanceof RocketEntity.CustomEntity)) {
			(entity.getRidingEntity()).getPersistentData().putDouble("Rotation", 1);
		}
		if (((entity.getRidingEntity()) instanceof RocketTier2Entity.CustomEntity)) {
			(entity.getRidingEntity()).getPersistentData().putDouble("Rotation", 1);
		}
		if (((entity.getRidingEntity()) instanceof RocketTier3Entity.CustomEntity)) {
			(entity.getRidingEntity()).getPersistentData().putDouble("Rotation", 1);
		}
	}
}
