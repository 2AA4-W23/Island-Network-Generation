package ca.team50.soilAbsorption;

public abstract class SoilProfile implements SoilProfileGenerable {

    // Rate of water absorption, ranging from 0 to 1
    protected double absorptionRate;

    public SoilProfile(double clayContent, double sandContent, double loamContent) {
    }

    // Implementation of the generateSoilProfile method from the SoilProfileGenerable interface
    public SoilProfile generateSoilProfile(double clayContent, double sandContent, double loamContent) {
        absorptionRate = clayContent * 0.6 + sandContent * 0.2 + loamContent * 0.2;
        if (clayContent <= 1.0 && clayContent >= 0.5 && sandContent < 0.3 && loamContent < 0.3) {
            return new Clay(clayContent, sandContent, loamContent, absorptionRate);
        } else if (sandContent <= 1 && sandContent >= 0.5 && clayContent < 0.3 && loamContent < 0.3) {
            return new Sand(clayContent, sandContent, loamContent, absorptionRate);
        } else if (loamContent <= 1 && loamContent >= 0.5 && clayContent < 0.3 && sandContent < 0.3) {
            return new Loam(clayContent, sandContent, loamContent, absorptionRate);
        } else {
            return new Special(clayContent, sandContent, loamContent, absorptionRate);
        }
    }

    // Abstract method for computing the remaining water in the soil
    abstract double computeRemainingWater(double distanceToWater);

}