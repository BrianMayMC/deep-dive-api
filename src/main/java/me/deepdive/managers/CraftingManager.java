package me.deepdive.managers;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

public class CraftingManager {

    /**
     * This registers a custom recipe with the given parameters
     * @param key This is the namespaced key of the plugin the recipe is linked to
     * @param result this is the ItemStack that represents the result of the recipe
     * @param recipe This is the shape of the recipe in the form of an array. Ensure there's 3 strings in the array, every string representing one row of the crafting table
     * @param ingredients This map represents the ingredients that are set in the recipe. The shape contains letters or numbers, and this needs to be linked to an itemstack
     */
    public static void registerRecipe(NamespacedKey key, ItemStack result, String[] recipe, Map<Character, ItemStack> ingredients){
        ShapedRecipe shapedRecipe = new ShapedRecipe(key, result);
        shapedRecipe.shape(recipe);
        for(char s : ingredients.keySet()){
            shapedRecipe.setIngredient(s, ingredients.get(s));
        }

        Bukkit.addRecipe(shapedRecipe);
    }


    /**
     * This unregisters a recipe you've added before. Make sure you execute this method on shutdown after adding a recipe
     */
    public static void unregisterRecipe(NamespacedKey key){
        Bukkit.removeRecipe(key);
    }
}
