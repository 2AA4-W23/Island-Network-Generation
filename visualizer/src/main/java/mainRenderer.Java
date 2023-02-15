public class Main {
    public static void main(String[] args) {
        // Parse the command-line arguments to read and check for the -X flag
        boolean debugMode = false;
        for (String arg : args) {
            if (arg.equals("-X")) {
                // Set to True if we can find the appropriate trigger flag and then we exit.
                debugMode = true;
                break;
            }
        }
        
        Mesh mesh = // create a 3D mesh object
        
        Renderer renderer = new Renderer(mesh);
        // Update the value of debugMode based on if the previous flag was found or not
        renderer.setDebugMode(debugMode);
        renderer.render();
    }
}