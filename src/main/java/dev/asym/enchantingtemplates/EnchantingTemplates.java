package dev.asym.enchantingtemplates;

import dev.asym.enchantingtemplates.item.ModItems;
import dev.asym.enchantingtemplates.loot_table.ModLootTables;
import dev.asym.enchantingtemplates.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantingTemplates implements ModInitializer {
	public static final String MOD_ID = "enchantingtemplates";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModRecipes.registerRecipes();
		ModLootTables.registerLootTables();
	}
}
