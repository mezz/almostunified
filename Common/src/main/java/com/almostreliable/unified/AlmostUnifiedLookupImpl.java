package com.almostreliable.unified;

import com.almostreliable.unified.api.AlmostUnifiedLookup;
import com.almostreliable.unified.utils.UnifyTag;
import com.google.auto.service.AutoService;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

@AutoService(AlmostUnifiedLookup.class)
public class AlmostUnifiedLookupImpl implements AlmostUnifiedLookup {

    @Override
    public boolean isLoaded() {
        return AlmostUnified.isRuntimeLoaded();
    }

    @Nullable
    @Override
    public Item getReplacementForItem(ItemLike itemLike) {
        ResourceLocation id = Registry.ITEM.getKey(itemLike.asItem());
        ResourceLocation result = AlmostUnified.getRuntime().getReplacementMap().getReplacementForItem(id);
        return Registry.ITEM.getOptional(result).orElse(null);
    }

    @Nullable
    @Override
    public Item getPreferredItemForTag(TagKey<Item> tag) {
        UnifyTag<Item> asUnifyTag = UnifyTag.item(tag.location());
        ResourceLocation result = AlmostUnified
                .getRuntime()
                .getReplacementMap()
                .getPreferredItemForTag(asUnifyTag, $ -> true);
        return Registry.ITEM.getOptional(result).orElse(null);
    }

    @Nullable
    @Override
    public TagKey<Item> getPreferredTagForItem(ItemLike itemLike) {
        ResourceLocation id = Registry.ITEM.getKey(itemLike.asItem());
        UnifyTag<Item> unifyTag = AlmostUnified.getRuntime().getReplacementMap().getPreferredTagForItem(id);
        if (unifyTag == null) {
            return null;
        }
        return TagKey.create(Registry.ITEM_REGISTRY, unifyTag.location());
    }

    @Override
    public Set<Item> getPotentialItems(TagKey<Item> tag) {
        UnifyTag<Item> asUnifyTag = UnifyTag.item(tag.location());
        return AlmostUnified
                .getRuntime()
                .getFilteredTagMap()
                .getItems(asUnifyTag)
                .stream()
                .flatMap(rl -> Registry.ITEM.getOptional(rl).stream())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<TagKey<Item>> getConfiguredTags() {
        return AlmostUnified
                .getRuntime()
                .getFilteredTagMap()
                .getTags()
                .stream()
                .map(ut -> TagKey.create(Registry.ITEM_REGISTRY, ut.location()))
                .collect(Collectors.toSet());
    }
}