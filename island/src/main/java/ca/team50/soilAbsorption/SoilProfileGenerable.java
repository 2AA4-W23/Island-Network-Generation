package ca.team50.soilAbsorption;


import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public interface SoilProfileGenerable {
    SoilProfile generateSoilProfile(double clayContent, double sandContent, double loamContent);

}
