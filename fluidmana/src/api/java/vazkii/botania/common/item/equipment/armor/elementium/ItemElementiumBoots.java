package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumBoots extends ItemElementiumArmor {

	public ItemElementiumBoots() {
		super(3, LibItemNames.ELEMENTIUM_BOOTS);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.09F;
	}

}
