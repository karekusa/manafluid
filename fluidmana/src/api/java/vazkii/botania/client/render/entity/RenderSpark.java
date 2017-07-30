/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 14, 2015, 1:03:11 AM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.util.IIcon;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.item.ItemSparkUpgrade;

public class RenderSpark extends RenderSparkBase<EntitySpark> {

	@Override
	public IIcon getSpinningIcon(EntitySpark entity) {
		int upgrade = entity.getUpgrade() - 1;
		return upgrade >= 0 && upgrade < ItemSparkUpgrade.worldIcons.length ? ItemSparkUpgrade.worldIcons[upgrade] : null;
	}

}
