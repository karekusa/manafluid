package karekusa.fluidmana.Fluid;

import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidRegisterer {


    public static Fluid FluidMana;

    public static void PreInit(){

        FluidMana = new Fluid("fluidmana").setLuminosity(15).setDensity(600).setViscosity(6000).setTemperature(350).setRarity(EnumRarity.uncommon);
        FluidRegistry.registerFluid(FluidMana);

    }
}
