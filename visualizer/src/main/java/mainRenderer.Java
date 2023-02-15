// I did not want to touch the actual main so I created a template main for now to just portray the logic 
// I was just wondering since in the actual main we strip the input args into two separate values args[0] and args[1]
// Not sure which one would include the "-X" because ideally we would need to then check that

public class Main {
    public static void main(String[] args) {
        // Parse the command-line arguments to read and check for the -X flag
        boolean debugMode = false;
        for (String arg : args) {
            // Check to see if the -X is included
            if (arg.contains("-X")) {
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