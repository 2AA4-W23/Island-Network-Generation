package ca.team50.soilAbsorption;
import ca.team50.water.AquiferGenerator;
import ca.team50.water.LakeGenerator;
import ca.team50.adt.Polygons;

public class Loam extends SoilProfile {
    private double absorptionRate;

    public Loam(double clayContent, double sandContent, double loamContent, double absorptionRate) {
        super(clayContent, sandContent, loamContent);
        this.absorptionRate = absorptionRate;
    }

    public double computeRemainingWater() {
        double distanceToWater = calculateDistanceToWater(polygon, lakeGen, aquiferGen);
        double remainingWater = 1 / (1 + this.absorptionRate * Math.pow(distanceToWater,2));
        return remainingWater;
    }
    @Override
    public SoilProfile generateSoilProfile(double clayContent, double sandContent, double loamContent) {
        if (loamContent <= 1.0 && loamContent >= 0.5 && sandContent < 0.3 && clayContent < 0.3) {
            return new Loam(clayContent, sandContent, loamContent, absorptionRate);
        } else if (sandContent <= 1 && sandContent >= 0.5 && clayContent < 0.3 && loamContent < 0.3) {
            return new Sand(clayContent, sandContent, loamContent, absorptionRate);
        } else if (clayContent <= 1 && clayContent >= 0.5 && loamContent < 0.3 && sandContent < 0.3) {
            return new Loam(clayContent, sandContent, loamContent, absorptionRate);
        } else {
            return new Special(clayContent, sandContent, loamContent, absorptionRate);
        }
    }
}