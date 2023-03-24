package ca.team50.soilAbsorption;

public class Special extends SoilProfile {
    private double absorptionRate;

    public Special(double clayContent, double sandContent, double loamContent, double absorptionRate) {
        super(clayContent, sandContent, loamContent);
        this.absorptionRate = absorptionRate;
    }

    public double computeRemainingWater(double distanceToWater) {
        return 1;
    }
}