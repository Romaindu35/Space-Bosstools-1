package net.mrscauthd.boss_tools.procedures;

import net.mrscauthd.boss_tools.BossToolsModElements;
import net.mrscauthd.boss_tools.BossToolsMod;

import net.minecraft.entity.Entity;

import java.util.Map;

@BossToolsModElements.ModElement.Tag
public class RocketGuiArrow15Procedure extends BossToolsModElements.ModElement {
	public RocketGuiArrow15Procedure(BossToolsModElements instance) {
		super(instance, 252);
	}

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				BossToolsMod.LOGGER.warn("Failed to load dependency entity for procedure RocketGuiArrow15!");
			return false;
		}
		Entity entity = (Entity) dependencies.get("entity");
		return ((entity.getPersistentData().getDouble("loading")) >= 315);
	}
}
