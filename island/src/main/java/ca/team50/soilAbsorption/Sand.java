package ca.team50.soilAbsorption;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.water.AquiferGenerator;
import ca.team50.water.LakeGenerator;
import ca.team50.adt.Polygons;

public class Sand extends SoilProfile {


    private double absorptionRate;


    public Sand(double clayContent, double sandContent, double loamContent, double absorptionRate) {
        super(clayContent, sandContent, loamContent);
        this.absorptionRate = absorptionRate;
    }

    public double calculateAbsorptionRate(double clayContent, double sandContent, double loamContent) {
        absorptionRate = clayContent * 0.1 + sandContent * 0.7 + loamContent * 0.2;
        return absorptionRate;
    }

    public double computeRemainingWater(Polygons polygon, LakeGenerator lakeGen, AquiferGenerator aquiferGen, double maxDistanceFromIsland) {
        double distanceToWater = calculateDistanceToWater(polygon, lakeGen, aquiferGen);
        distanceToWater = (distanceToWater - 0)/(maxDistanceFromIsland-0);
        double remainingWater = 1 - ((0.1*absorptionRate)*distanceToWater);
        double polygonAltitude = extractProperties(polygon.getCentroid().getPropertiesList(), "altitude");
        double humidity = polygonAltitude-0.5*remainingWater;
        if (humidity < 0) {
            humidity = 0;
        }
        polygon.changeHumidity(String.valueOf(humidity));
        return distanceToWater;
    }
    @Override
    public SoilProfile generateSoilProfile(double clayContent, double sandContent, double loamContent) {
        absorptionRate = calculateAbsorptionRate(clayContent,sandContent,loamContent);
        if (sandContent <= 1.0 && sandContent >= 0.5 && clayContent < 0.3 && loamContent < 0.3) {
            return new Sand(clayContent, sandContent, loamContent, absorptionRate);
        } else if (clayContent <= 1 && clayContent >= 0.5 && sandContent < 0.3 && loamContent < 0.3) {
            return new Clay(clayContent, sandContent, loamContent, absorptionRate);
        } else if (loamContent <= 1 && loamContent >= 0.5 && clayContent < 0.3 && sandContent < 0.3) {
            return new Loam(clayContent, sandContent, loamContent, absorptionRate);
        } else {
            return new Special(clayContent, sandContent, loamContent, absorptionRate);
        }
    }

    private static double extractProperties(java.util.List<Structs.Property> properties, String property){

        String val = "0";
        for(Structs.Property p: properties) {
            if (p.getKey().equals(property)) {
                val = p.getValue();
            }
        }

        return  Double.parseDouble(val);
    }

}