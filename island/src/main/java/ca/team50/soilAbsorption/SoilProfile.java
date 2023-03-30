package ca.team50.soilAbsorption;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.team50.water.AquiferGenerator;
import ca.team50.water.LakeGenerator;
import ca.team50.adt.Polygons;

import java.util.ArrayList;

public abstract class SoilProfile implements SoilProfileGenerable {

    // Rate of water absorption, ranging from 0 to 1
    protected double absorptionRate;
    protected double clayContent;
    protected double sandContent;
    protected double loamContent;

    public SoilProfile(double clayContent, double sandContent, double loamContent) {
        this.clayContent = clayContent;
        this.sandContent = sandContent;
        this.loamContent = loamContent;
        this.absorptionRate = calculateAbsorptionRate(clayContent,sandContent,loamContent);
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

    // Calculate absorption rate based on clay, sand, and loam content
    public double calculateAbsorptionRate(double clayContent, double sandContent, double loamContent) {
        absorptionRate = clayContent * 0.1 + sandContent * 0.7 + loamContent * 0.2;
        return absorptionRate;
    }

    protected double calculateDistanceToWater(Polygons polygon, LakeGenerator lakeGen, AquiferGenerator aquiferGen) {

        // Retrieve the centroid of the given polygon
        Structs.Vertex polygonCentroid = polygon.getCentroid();

        double shortestDistance = Double.MAX_VALUE;

        // Iterate through each aquifer and calculate the distance between the polygon centroid and the aquifer centroid
        for (Polygons aquifer : aquiferGen.getAquifers()) {
            Structs.Vertex aquiferCentroid = aquifer.getCentroid();
            double aquiferDistance = Math.sqrt(Math.pow((aquiferCentroid.getX() - polygonCentroid.getX()), 2) + Math.pow((aquiferCentroid.getY() - polygonCentroid.getY()), 2));
            if (aquiferDistance < shortestDistance) {
                shortestDistance = aquiferDistance;
            }
        }

        // Iterate through each lake and calculate the distance between the polygon centroid and the lake centroid
        for (ArrayList<Polygons> lakes : lakeGen.getLakes()) {
            for (Polygons lake : lakes) {
                Structs.Vertex lakeCentroid = lake.getCentroid();
                double lakeDistance = Math.sqrt(Math.pow((lakeCentroid.getX() - polygonCentroid.getX()), 2) + Math.pow((lakeCentroid.getY() - polygonCentroid.getY()), 2));
                if (lakeDistance < shortestDistance) {
                    shortestDistance = lakeDistance;
                }
            }
        }

        return shortestDistance;
    }

    // Abstract method for computing the remaining water in the soil
    public abstract double computeRemainingWater(Polygons polygon, LakeGenerator lakeGen, AquiferGenerator aquiferGen, double maxDistanceOnIsland);

}