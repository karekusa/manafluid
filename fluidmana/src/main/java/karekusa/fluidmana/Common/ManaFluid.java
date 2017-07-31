package karekusa.fluidmana.Common;

import net.minecraftforge.fluids.Fluid;

public class ManaFluid extends Fluid {
    public ManaFluid(String fluidName) {
        super(fluidName);
        this.setDensity(3);
        this.setViscosity(20);

    }
}
