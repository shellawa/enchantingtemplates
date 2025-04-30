package dev.asym.enchantingtemplates.item;

import dev.asym.enchantingtemplates.EnchantingTemplates;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;


public class ModItems {

    public static final Item ENCHANTING_TEMPLATE = registerItem(
            "enchanting_template",
            new Item(
                    new Item.Settings()
                            .rarity(Rarity.UNCOMMON)
                            .component(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
                            .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
    ));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(EnchantingTemplates.MOD_ID, name), item);
    }

    public static void registerModItems() {
        EnchantingTemplates.LOGGER.info("Registering ModItems for " + EnchantingTemplates.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            MinecraftClient.getInstance().getNetworkHandler().getRegistryManager().get(RegistryKeys.ENCHANTMENT).forEach( // I think there's a better way to do this.
                    enchantment -> {
                        var enchantmentEntry = RegistryEntry.of(enchantment);
                        ItemStack template = new ItemStack(ENCHANTING_TEMPLATE);
                        template.addEnchantment(enchantmentEntry, enchantmentEntry.value().getMaxLevel());
                        entries.add(template);
                    }
            );

        });
    }

}
