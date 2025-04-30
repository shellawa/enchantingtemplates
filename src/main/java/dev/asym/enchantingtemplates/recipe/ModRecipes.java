package dev.asym.enchantingtemplates.recipe;

import dev.asym.enchantingtemplates.EnchantingTemplates;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

    public static final RecipeSerializer<SmithingEnchantRecipe> SMITHING_ENCHANT_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(EnchantingTemplates.MOD_ID, "smithing_enchant"),
            new SmithingEnchantRecipe.Serializer());

    public static final RecipeSerializer<EnchantingTemplateDuplicationRecipe> ENCHANTING_TEMPLATE_DUPLICATION_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(EnchantingTemplates.MOD_ID, "duplication_enchanting_recipe"),
            new EnchantingTemplateDuplicationRecipe.Serializer());

    public static void registerRecipes() {
        EnchantingTemplates.LOGGER.info("Registering Custom Recipes for " + EnchantingTemplates.MOD_ID);
    }
}