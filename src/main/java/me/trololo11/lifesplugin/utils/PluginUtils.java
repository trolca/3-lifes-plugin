package me.trololo11.lifesplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

//yeahh maybe its time to delete this... but not for now
//AND IT WAS SOO USEFULL SO IM HAPPY I DIDNT :D
public class PluginUtils {

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean isInventoryEmpty(Inventory inventory){

        ItemStack[] contents = inventory.getContents();
        for(int i=0; i < contents.length-5; i++){
            ItemStack itemStack = contents[i];
            if(itemStack == null) return true;
        }

        return false;
    }
}
