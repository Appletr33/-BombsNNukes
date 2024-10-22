package me.xander.bombsnnukes.init;

import me.xander.bombsnnukes.BombsnNukes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


import static me.xander.bombsnnukes.BombsnNukes.MOD_ID;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static class ModCreativeTab extends ItemGroup {
        public static final ModCreativeTab instance = new ModCreativeTab(ItemGroup.TABS.length, "BombsnNukes");

        private ModCreativeTab(int index, String label) {
            super(index, label);

        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Item.byId(46));
        }
    }

}

