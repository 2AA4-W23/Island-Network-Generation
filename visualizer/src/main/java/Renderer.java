import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import ca.team50.adt.PolyMesh;

public class Renderer {
    private PolyMesh mesh;
    private boolean debugMode = false;
    
    public Renderer(PolyMesh mesh) {
        this.mesh = mesh;
    }
    
    public void render() {
        if (debugMode) {
            // Render polygons in black
            // Render centroids in red
            // Render neighborhood relationships in light grey
        } else {
            // Render polygons based on color and thickness properties
        }
    }
    
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
    
}