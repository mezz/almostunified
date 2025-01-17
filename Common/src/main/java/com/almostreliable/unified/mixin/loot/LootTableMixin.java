package com.almostreliable.unified.mixin.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

import com.almostreliable.unified.api.unification.UnificationLookup;
import com.almostreliable.unified.unification.loot.LootUnificationHandler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LootTable.class)
public class LootTableMixin implements LootUnificationHandler {
    @Shadow @Final private List<LootPool> pools;

    @Override
    public boolean almostunified$unify(UnificationLookup lookup) {
        boolean unified = false;
        for (LootPool pool : pools) {
            unified |= LootUnificationHandler.cast(pool).almostunified$unify(lookup);
        }

        return unified;
    }
}
