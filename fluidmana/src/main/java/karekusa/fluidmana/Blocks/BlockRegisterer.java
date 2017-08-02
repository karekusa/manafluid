package karekusa.fluidmana.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class BlockRegisterer {

    public static Block ManaCondensation;
    public static Block ManaVolatilizer;

    public static void PreInit(){

        ManaCondensation = new ManaCondensation("ManaCondensation");
        GameRegistry.registerBlock(ManaCondensation,"ManaCondensation");

        ManaVolatilizer = new ManaVolatilizer("ManaVolatilizer");
        GameRegistry.registerBlock(ManaVolatilizer,"ManaVolatilizer");

    }
}
