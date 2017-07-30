package karekusa.fluidmana.Common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class tabfluidmana extends CreativeTabs{
    public tabfluidmana(String lable) {
        super(lable);
    }

    @Override
    public Item getTabIconItem() {
        return Items.iron_ingot;
    }
}
