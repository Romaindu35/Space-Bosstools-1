package net.mrscauthd.boss_tools.procedures;

import net.mrscauthd.boss_tools.BossToolsModElements;
import net.mrscauthd.boss_tools.BossToolsMod;

import net.minecraft.entity.Entity;

import java.util.Map;

@BossToolsModElements.ModElement.Tag
public class OpenTier1SpaceStationMenuProcedure extends BossToolsModElements.ModElement {
	public OpenTier1SpaceStationMenuProcedure(BossToolsModElements instance) {
		super(instance, 610);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				BossToolsMod.LOGGER.warn("Failed to load dependency entity for procedure OpenTier1SpaceStationMenu!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		entity.getPersistentData().putDouble("Tier_1_open_main_menu_back", 0);
		entity.getPersistentData().putDouble("Tier_1_open_main_menu", 0);
		entity.getPersistentData().putDouble("Tier_1_open_main_menu_2", 0);
		entity.getPersistentData().putDouble("Tier_1_space_station_open", 1);
	}
}
