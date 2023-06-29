package me.trololo11.lifesplugin.utils;

import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class RecipesUtils {

    public int getMaxCraftAmount(CraftingInventory inv) {
        if (inv.getResult() == null)
            return 0;

        int resultCount = inv.getResult().getAmount();
        int materialCount = Integer.MAX_VALUE;

        for (ItemStack is : inv.getMatrix())
            if (is != null && is.getAmount() < materialCount)
                materialCount = is.getAmount();

        return resultCount * materialCount;
    }


    public int getEmptySlots(ItemStack[] contents){
        int emptySlots = 0;

        for(ItemStack item : contents){
            if(item == null) emptySlots++;
        }

        return emptySlots-5;

    }
}
