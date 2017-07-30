package karekusa.fluidmana;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.registry.GameRegistry;
import karekusa.fluidmana.Blocks.ManaCondensation;
import karekusa.fluidmana.Blocks.ManaVolatilizer;
import karekusa.fluidmana.Common.IProxy;
import karekusa.fluidmana.Common.tabfluidmana;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;


@Mod(modid=Fluidmana_main.modid,name = "fluidmana",version = "1.0")
public class Fluidmana_main {
    public final static String modid = "fluidmana";



    @SidedProxy(clientSide = "rarejewels.ClientProxy",
            serverSide = "rarejewels.CommonProxy")
    private static IProxy proxy;//いざとなったら使おうと思ってたプロキシー（描画のときに使う）

    public static final CreativeTabs tabfluidmana = new tabfluidmana(modid);

    public static Block ManaCondensation;
    public static Block ManaVolatilizer;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){

        ManaCondensation = new ManaCondensation("ManaCondensation");
        GameRegistry.registerBlock(ManaCondensation,"ManaCondensation");

        ManaVolatilizer = new ManaVolatilizer("ManaVolatilizer");
        GameRegistry.registerBlock(ManaVolatilizer,"ManaVolatilizer");
    }

    @EventHandler
    public void Init(FMLInitializationEvent event){


    }







}
