package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;

public class CraftItemListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onCraft(CraftItemEvent e) throws SQLException {
        if(e.getClick() == ClickType.MIDDLE) return;
        if(e.getClick() == ClickType.NUMBER_KEY){
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) return;

        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        ItemStack test = e.getRecipe().getResult().clone();
        int recipeAmount = test.getAmount();

        //tyyy ezeiger92 for the code :D (yeahhhh i copied it :P)
        if(e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT){

            int maxCraftable = getMaxCraftAmount(e.getInventory());
            int capacity = fits(test, e.getView().getBottomInventory());


            if (capacity < maxCraftable)
                maxCraftable = ((capacity + recipeAmount - 1) / recipeAmount) * recipeAmount;

            recipeAmount = maxCraftable;

            checkTarget(item.getType(), p, recipeAmount);
            return;
        }

        checkTarget(item.getType(), p);


    }

    private int getMaxCraftAmount(CraftingInventory inv) {
        if (inv.getResult() == null)
            return 0;

        int resultCount = inv.getResult().getAmount();
        int materialCount = Integer.MAX_VALUE;

        for (ItemStack is : inv.getMatrix())
            if (is != null && is.getAmount() < materialCount)
                materialCount = is.getAmount();

        return resultCount * materialCount;
    }

    private int fits(ItemStack stack, Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int result = 0;

        for (ItemStack is : contents)
            if (is == null)
                result += stack.getMaxStackSize();
            else if (is.isSimilar(stack))
                result += Math.max(stack.getMaxStackSize() - is.getAmount(), 0);

        return result;
    }


    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getCraftItemQuests();
    }
}
