package ca.team50.soilAbsorption;

public class Loam extends SoilProfile {
    private double absorptionRate;

    public Loam(double clayContent, double sandContent, double loamContent, double absorptionRate) {
        super(clayContent, sandContent, loamContent);
        this.absorptionRate = absorptionRate;
    }

    public double computeRemainingWater(double distanceToWater) {
        // need to add logic
        return 1;
    }
}