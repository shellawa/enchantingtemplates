package dev.asym.enchantingtemplates.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.asym.enchantingtemplates.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import static java.lang.Math.min;

public class SmithingEnchantRecipe implements SmithingRecipe {

    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;

    public SmithingEnchantRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        this.template = template;
        this.base = base;
        this.addition = addition;
    }


    @Override
    public boolean testTemplate(ItemStack stack) {
        return this.template.test(stack);
    }

    @Override
    public boolean testBase(ItemStack stack) {
        return this.base.test(stack);
    }

    @Override
    public boolean testAddition(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public boolean matches(SmithingRecipeInput input, World world) {
        return this.template.test(input.template()) && this.base.test(input.base()) && this.addition.test(input.addition());
    }

    @Override
    public ItemStack craft(SmithingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack result = input.base().copy();
        ItemStack template = input.template();

        var baseEnchantments = result.getEnchantments();

        // I could've gone with stored enchantments instead but the EnchantRandomlyLootFunction only does regular enchantments.
        var templateEnchantments = template.getEnchantments();

        templateEnchantments.getEnchantments().forEach(enchantment -> {
            if (!enchantment.value().isAcceptableItem(result)) return;

            result.addEnchantment(enchantment, min(templateEnchantments.getLevel(enchantment), enchantment.value().getMaxLevel()));
        });

        if (result.getEnchantments().equals(baseEnchantments)) return ItemStack.EMPTY;

        return result;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return ModItems.ENCHANTING_TEMPLATE.getDefaultStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMITHING_ENCHANT_SERIALIZER;
    }


    public static class Serializer implements RecipeSerializer<SmithingEnchantRecipe> {
        public static final PacketCodec<RegistryByteBuf, SmithingEnchantRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                Serializer::write, Serializer::read
        );
        private static final MapCodec<SmithingEnchantRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("template").forGetter(recipe -> recipe.template),
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition)
                        )
                        .apply(instance, SmithingEnchantRecipe::new)
        );

        private static SmithingEnchantRecipe read(RegistryByteBuf buf) {
            Ingredient ingredient = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient ingredient2 = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient ingredient3 = Ingredient.PACKET_CODEC.decode(buf);
            return new SmithingEnchantRecipe(ingredient, ingredient2, ingredient3);
        }

        private static void write(RegistryByteBuf buf, SmithingEnchantRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.template);
            Ingredient.PACKET_CODEC.encode(buf, recipe.base);
            Ingredient.PACKET_CODEC.encode(buf, recipe.addition);
        }

        @Override
        public MapCodec<SmithingEnchantRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, SmithingEnchantRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }

}
