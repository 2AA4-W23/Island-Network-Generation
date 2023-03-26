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
        this.absorptionRate = calculateAbsorptionRate();
    }

    // Set clay content and recalculate absorptionRate
    public void setClayContent(double clayContent) {
        this.clayContent = clayContent;
        this.absorptionRate = calculateAbsorptionRate();
    }

    // Set sand content and recalculate absorptionRate
    public void setSandContent(double sandContent) {
        this.sandContent = sandContent;
        this.absorptionRate = calculateAbsorptionRate();
    }

    // Set loam content and recalculate absorptionRate
    public void setLoamContent(double loamContent) {
        this.loamContent = loamContent;
        this.absorptionRate = calculateAbsorptionRate();
    }

    // Calculate absorption rate based on clay, sand, and loam content
    protected double calculateAbsorptionRate() {
        absorptionRate = clayContent * 0.6 + sandContent * 0.4 + loamContent * 0.2;
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
    abstract double computeRemainingWater(double distanceToWater);

}