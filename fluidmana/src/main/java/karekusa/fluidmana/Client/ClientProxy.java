package karekusa.fluidmana.Client;

import cpw.mods.fml.common.network.IGuiHandler;
import karekusa.fluidmana.Common.CommonProxy;
import karekusa.fluidmana.Common.IProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientProxy implements IProxy,IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void registerRenderers() {

    }
}
