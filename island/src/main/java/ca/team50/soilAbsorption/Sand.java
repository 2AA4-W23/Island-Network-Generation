package ca.team50.soilAbsorption;

public class Sand extends SoilProfile {
    private double absorptionRate;

    public Sand(double clayContent, double sandContent, double loamContent, double absorptionRate) {
        super(clayContent, sandContent, loamContent);
        this.absorptionRate = absorptionRate;
    }

    public double computeRemainingWater(double distanceToWater) {
        // need to add logic
        return 1;
    }
}