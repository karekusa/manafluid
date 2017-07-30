package karekusa.fluidmana.Blocks;

import karekusa.fluidmana.Fluidmana_main;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;


public class Blockbase extends Block {
    protected Blockbase(String name ) {
        super(Material.rock);
        this.setCreativeTab(Fluidmana_main.tabfluidmana);
        this.setBlockName(name);
        this.setBlockTextureName(Fluidmana_main.modid + ":" + name);
        this.setHardness(30.0F);
        this.setLightLevel(0.1F);
        this.setResistance(20.0F);
        this.setHarvestLevel("pickaxe", 0);


    }
}
