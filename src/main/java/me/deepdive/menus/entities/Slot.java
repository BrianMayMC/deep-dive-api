package me.deepdive.menus.entities;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * A slot can be viewed as a button in a menu. Every item in a MenuInterface will be placed through
 * slots. Just create a slot, set the index and the item, then call setSlot() to set your slot in the menu
 */
public class Slot {

    @FunctionalInterface
    public interface SlotSelector{
        void select();
    }

    @Getter
    private final int index;
    @Getter private final ItemStack item;
    @Getter private SlotSelector  selector;
    @Getter private final UUID id;

    public Slot(int index, ItemStack item){
        this.index = index;
        this.item = item;
        id = UUID.randomUUID();
    }

    public void setAction(SlotSelector slotSelector){
        this.selector = slotSelector;
    }



    public void select(){
        if(selector != null){
            synchronized (this){
                selector.select();
            }
        }
    }
}