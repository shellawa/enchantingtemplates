package dev.asym.enchantingtemplates.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import static dev.asym.enchantingtemplates.item.ModItems.ENCHANTING_TEMPLATE;

public class EnchantingTemplateDuplicationRecipe extends SpecialCraftingRecipe {

    final Ingredient template = Ingredient.ofItems(ENCHANTING_TEMPLATE);
    final Ingredient diamond = Ingredient.ofItems(Items.DIAMOND);

    /**
     * Over-complicating this a bit because I want to make it so that you need to use different items for different enchantments.
     * But I still can't think of a way to properly balancing it.
     * Right now Mending is very easy to duplicate, so this will need to be solved eventually.
     * */
    final Ingredient duplicationIngredient;

    public EnchantingTemplateDuplicationRecipe(Ingredient duplicationIngredient) {
        super(CraftingRecipeCategory.MISC);
        this.duplicationIngredient = duplicationIngredient;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (input.getSize() < 9) return false;

        for (int i = 0; i < input.getSize(); i++) {
            switch (i) {
                case 1: // middle-top
                    if (!template.test(input.getStackInSlot(1))) return false;
                    break;
                case 4: // center
                    if (!duplicationIngredient.test(input.getStackInSlot(4))) return false;
                    break;
                default:
                    if (!diamond.test(input.getStackInSlot(i))) return false;
                    break;
            }
        }

        return true;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack template = input.getStackInSlot(1).copy(); // middle-top

        template.setCount(2);
        return template;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public boolean isIgnoredInRecipeBook() { return false; }; // Still don't appear in recipe book.

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return ENCHANTING_TEMPLATE.getDefaultStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ENCHANTING_TEMPLATE_DUPLICATION_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<EnchantingTemplateDuplicationRecipe> {
        public static final PacketCodec<RegistryByteBuf, EnchantingTemplateDuplicationRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                EnchantingTemplateDuplicationRecipe.Serializer::write, EnchantingTemplateDuplicationRecipe.Serializer::read
        );
        private static final MapCodec<EnchantingTemplateDuplicationRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("duplication_ingredient").forGetter(recipe -> recipe.duplicationIngredient)
                        )
                        .apply(instance, EnchantingTemplateDuplicationRecipe::new)
        );

        private static EnchantingTemplateDuplicationRecipe read(RegistryByteBuf buf) {
            Ingredient ingredient = Ingredient.PACKET_CODEC.decode(buf);
            return new EnchantingTemplateDuplicationRecipe(ingredient);
        }

        private static void write(RegistryByteBuf buf, EnchantingTemplateDuplicationRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.duplicationIngredient);
        }

        @Override
        public MapCodec<EnchantingTemplateDuplicationRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, EnchantingTemplateDuplicationRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
