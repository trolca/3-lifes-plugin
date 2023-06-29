package me.trololo11.lifesplugin;

import me.trololo11.lifesplugin.utils.PluginUtils;
import me.trololo11.lifesplugin.utils.RecipesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class RecipesManager {

    private ShapedRecipe lifesRecipe;
    private ShapedRecipe reviveCardRecipe;
    private ItemStack life;
    private ItemStack playerLife;
    private ItemStack reviveCard;
    private ItemStack reviveShard;
    private ItemStack lifeShard;
    private final RecipesUtils recipesUtils;
    private final LifesPlugin plugin = LifesPlugin.getPlugin();

    public RecipesManager(){
        recipesUtils = new RecipesUtils();
    }

    private void setCustomItems(){
        //setsup the custom items names and models
        //you know lives, revive cards, revive shards and life shards
        ArrayList<String> lore = new ArrayList<>();
        life = new ItemStack(Material.SCUTE);
        reviveCard = new ItemStack(Material.SCUTE);
        playerLife = new ItemStack(Material.SCUTE);
        reviveShard = new ItemStack(Material.IRON_NUGGET);
        lifeShard = new ItemStack(Material.GOLD_NUGGET);

        ItemMeta lifeMeta = life.getItemMeta();
        lifeMeta.setDisplayName(PluginUtils.chat("&c&lLife"));
        lifeMeta.setCustomModelData(8760001);
        lore.clear();
        lore.add(ChatColor.WHITE + "Left click to reedem!");
        lifeMeta.setLore(lore);
        lifeMeta.setLocalizedName("life-default-string");

        ItemMeta reviveMeta = reviveCard.getItemMeta();
        reviveMeta.setDisplayName(PluginUtils.chat("&b&lRevive card"));
        reviveMeta.setCustomModelData(8760005);
        lore.clear();
        lore.add(ChatColor.WHITE + "Left click to revive a random dead person!");
        reviveMeta.setLore(lore);
        reviveMeta.setLocalizedName("life-default-string");

        ItemMeta pLifeMeta = playerLife.getItemMeta();
        pLifeMeta.setCustomModelData(8760002);
        lore.clear();
        lore.add(ChatColor.WHITE + "Left click to add a life!");
        pLifeMeta.setLore(lore);
        pLifeMeta.setLocalizedName("life-default-string");


        ItemMeta reviveShardMeta = reviveShard.getItemMeta();
        reviveShardMeta.setDisplayName(ChatColor.AQUA + "Revive shard");
        reviveShardMeta.setCustomModelData(8760001);
        reviveShardMeta.setLocalizedName("revive-card-shard");

        ItemMeta lifeShardMeta = lifeShard.getItemMeta();
        lifeShardMeta.setDisplayName(ChatColor.RED + "Life shard");
        lifeShardMeta.setCustomModelData(8760001);
        lifeShardMeta.setLocalizedName("life-shard");

        life.setItemMeta(lifeMeta);
        reviveCard.setItemMeta(reviveMeta);
        playerLife.setItemMeta(pLifeMeta);
        lifeShard.setItemMeta(lifeShardMeta);
        reviveShard.setItemMeta(reviveShardMeta);

    }

    public void initililazeRecipes(){
        //makes the custom recipes for lifes and revive cards
        NamespacedKey lifesNameKey = new NamespacedKey(plugin, "life_recipe");
        NamespacedKey reviveNameKey = new NamespacedKey(plugin, "revive_card_recipe");

        setCustomItems();
        lifesRecipe = new ShapedRecipe(lifesNameKey, life);
        reviveCardRecipe = new ShapedRecipe(reviveNameKey, reviveCard);


        lifesRecipe.shape("###", "#D#", "###");
        lifesRecipe.setIngredient('#', new RecipeChoice.ExactChoice(lifeShard));
        lifesRecipe.setIngredient('D', Material.DIAMOND);

        reviveCardRecipe.shape("###", "#D#", "###");
        reviveCardRecipe.setIngredient('#', new RecipeChoice.ExactChoice(reviveShard));
        reviveCardRecipe.setIngredient('D', Material.NETHERITE_INGOT);

        //adds them to the server so player can use them
        Bukkit.getServer().addRecipe(lifesRecipe);
        Bukkit.getServer().addRecipe(reviveCardRecipe);

    }

    public ShapedRecipe getLifesRecipe() {
        return lifesRecipe;
    }

    public ShapedRecipe getReviveCardRecipe() {
        return reviveCardRecipe;
    }

    public ItemStack getLifeItem() {
        ItemStack lifeCopy = life.clone();
        ItemMeta lifeMeta = life.getItemMeta();
        lifeMeta.setLocalizedName(UUID.randomUUID().toString());

        lifeCopy.setItemMeta(lifeMeta);

        return lifeCopy;
    }

    public ItemStack getReviveCardItem() {
        //soo in order to make the items unstackable i needed to make the nbt's different
        //so when other functions requests a revive card it creates a clona and make the localized a random UUID to make them ustackable
        ItemStack reviveCopy = reviveCard.clone();
        ItemMeta reviveMeta = reviveCard.getItemMeta();
        reviveMeta.setLocalizedName(UUID.randomUUID().toString());

        reviveCopy.setItemMeta(reviveMeta);
        return reviveCopy;
    }

    public ItemStack getPlayersLife(Player player){
        ItemStack pLifeCopy = playerLife.clone();
        ItemMeta pLifeMeta = pLifeCopy.getItemMeta();
        //so when other functions requests a revive card it creates a clona and make the localized a random UUID to make them ustackable

        pLifeMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + player.getName() + "'s life");
        pLifeMeta.setLocalizedName(UUID.randomUUID().toString());

        pLifeCopy.setItemMeta(pLifeMeta);
        return pLifeCopy;
    }

    public ItemStack getReviveShardItem() {
        return reviveShard;
    }

    public ItemStack getLifeShardItem() {
        return lifeShard;
    }


    public RecipesUtils getRecipesUtils() {
        return recipesUtils;
    }



}
