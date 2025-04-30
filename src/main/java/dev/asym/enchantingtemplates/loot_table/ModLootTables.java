package dev.asym.enchantingtemplates.loot_table;

import dev.asym.enchantingtemplates.EnchantingTemplates;
import dev.asym.enchantingtemplates.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import java.util.Set;

public class ModLootTables {

    public static final Set<String> templateLootLocations = Set.of(
            "chests/jungle_temple",
            "chests/stronghold_corridor",
            "chests/stronghold_crossing",
            "chests/stronghold_library",
            "chests/simple_dungeon",
            "chests/abandoned_mineshaft",
            "chests/ancient_city",
            "chests/desert_pyramid",
            "chests/pillager_outpost",
            "chests/underwater_ruin_big",
            "chests/woodland_mansion"
    );

    public static void registerLootTables () {
        EnchantingTemplates.LOGGER.info("Registering Custom Loot Tables for " + EnchantingTemplates.MOD_ID);

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (templateLootLocations.contains(key.getValue().getPath())) {
                LootPool pool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(3))
                        .with(ItemEntry.builder(ModItems.ENCHANTING_TEMPLATE)
                                .conditionally(RandomChanceLootCondition.builder(0.2f))
                                .apply(EnchantRandomlyLootFunction.create().allowIncompatible())
                        )
                        .build();
                tableBuilder.pool(pool);
            }
        });
    }
}
