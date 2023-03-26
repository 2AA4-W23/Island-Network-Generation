package ca.team50.soilAbsorption;
import ca.team50.water.AquiferGenerator;
import ca.team50.water.LakeGenerator;
import ca.team50.adt.Polygons;

public class Special extends SoilProfile {
    private double absorptionRate;

    public Special(double clayContent, double sandContent, double loamContent, double absorptionRate) {
        super(clayContent, sandContent, loamContent);
        this.absorptionRate = absorptionRate;
    }

    public double computeRemainingWater(Polygons polygon, LakeGenerator lakeGen, AquiferGenerator aquiferGen) {
        double distanceToWater = calculateDistanceToWater(polygon, lakeGen, aquiferGen);
        double min = 0;
        double max = 1;
        double remainingWater = 1 / (1 + this.absorptionRate * Math.pow(distanceToWater,2));
        remainingWater = (remainingWater - min) / (max - min);
        polygon.changeHumidity(remainingWater);
        return remainingWater;
    }
    @Override
    public SoilProfile generateSoilProfile(double clayContent, double sandContent, double loamContent) {
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
}