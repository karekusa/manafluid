package karekusa.fluidmana;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.Mod.*;
import karekusa.fluidmana.Blocks.BlockRegisterer;
import karekusa.fluidmana.Common.IProxy;
import karekusa.fluidmana.Common.tabfluidmana;
import karekusa.fluidmana.Fluid.FluidRegisterer;
import net.minecraft.creativetab.CreativeTabs;


@Mod(modid=Fluidmana_main.modid,name = "fluidmana",version = "1.0")
public class Fluidmana_main {
    public final static String modid = "fluidmana";



    @SidedProxy(clientSide = "karekusa.fluidmana.Client.ClientProxy",
            serverSide = "karekusa.fluidmana.Common.CommonProxy")
    private static IProxy proxy;//いざとなったら使おうと思ってたプロキシー（描画のときに使う）

    public static final CreativeTabs tabfluidmana = new tabfluidmana(modid);


    @EventHandler
    public void preInit(FMLPreInitializationEvent event){

        FluidRegisterer.PreInit();

        BlockRegisterer.PreInit();
    }

    @EventHandler
    public void Init(FMLInitializationEvent event){


    }







}
