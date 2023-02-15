import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

public class Renderer {
    private Mesh mesh;
    private boolean debugMode = false;
    
    public Renderer(Mesh mesh) {
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