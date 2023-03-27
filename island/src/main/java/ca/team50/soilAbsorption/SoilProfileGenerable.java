package ca.team50.soilAbsorption;

public interface SoilProfileGenerable {
    SoilProfile generateSoilProfile(double clayContent, double sandContent, double loamContent);
}
