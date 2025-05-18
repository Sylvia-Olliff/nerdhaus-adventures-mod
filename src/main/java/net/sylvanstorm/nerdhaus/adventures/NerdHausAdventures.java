package net.sylvanstorm.nerdhaus.adventures;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.SkillsAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NerdHausAdventures implements ModInitializer {
	public static final String MOD_ID = "nerdhaus-adventures";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			LOGGER.info("[NerdHaus] Received Server player JOIN event");
			processPlayer(handler.player);
		}));
	}

	private void processPlayer(ServerPlayerEntity player) {
		SkillsAPI.streamUnlockedCategories(player)
				.forEach(category -> processCategory(player, category));
	}

	/**
	 * Disables simplyskills default tree. This allows for the use of their abilities without using their tree.
	 * @param player
	 * @param category
	 */
	private void processCategory(ServerPlayerEntity player, Category category) {
		String categoryId = category.getId().toString();
		if (categoryId.equals("simplyskills:tree")) {
			LOGGER.info("[NerdHaus] Found SimplySkills base tree, disabling...");
			SkillsAPI.getCategory(new Identifier(categoryId)).ifPresent(categoryObj -> {
				categoryObj.erase(player);
				categoryObj.lock(player);
				LOGGER.info("[NerdHaus] Disabled SimplySkills base tree!");
			});
		}
	}
}