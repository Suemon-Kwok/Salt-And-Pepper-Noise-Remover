package noiseremoving;

/*
Name: Suemon Kwok

Student ID: 14883335

Data structures and algorithms
*/

/*
NoiseRemoving Class - Main class for command-line noise removal

This class serves as the entry point for the console-based version of the
noise removal application. It demonstrates the basic functionality of the 
noise removal system with predefined test images and also supports 
command-line arguments for processing custom images.

Key responsibilities:
1. Handle command-line arguments for flexible usage
2. Process multiple test images in batch mode
3. Provide user feedback and error handling
4. Demonstrate the ImageProcess class functionality
 */
public class NoiseRemoving {
    
    /*
    Main method - Entry point for the command-line application
     
    This method handles two modes of operation:
    
    1. No arguments: Process all predefined test images
    
    2. With arguments: Process a custom image specified by user
    
    @param args Command line arguments
               
    args[0] = input image path (required if any args provided)
               
    args[1] = output image path (optional, defaults to "noise_removed.jpg")
     */
    public static void main(String[] args) {
        // Print application header and information
        System.out.println("=== Salt-and-Pepper Noise Removal Application ===");
        System.out.println("This is the command-line version. For GUI version, run NoiseRemovingGUI.");
        System.out.println(); // Empty line for readability
        
        // Create ImageProcess instance to handle all image operations
        // This single instance will be reused for processing multiple images
        ImageProcess processor = new ImageProcess();
        
        // Define test image paths - corrected to match project structure
        // These images should be placed in the project root directory
        String[] testImages = {
            "test image 1.jpg",  // Images should be in project root
            "test image 2.png",  // Different format to test PNG support
            "test image 3.jpg"   // Another JPG image
        };
        
        // Define corresponding output paths for processed images
        // Each output has "_cleaned" suffix to distinguish from originals
        String[] outputImages = {
            "test image 1 cleaned.jpg",  // Processed version of test image 1
            "test image 2 cleaned.jpg",  // Processed version of test image 2 (converted to JPG)
            "test image 3 cleaned.jpg"   // Processed version of test image 3
        };
        
        // Handle command-line arguments
        // Check if user provided any command-line arguments
        if (args.length >= 1) {
            // If arguments provided, use custom image processing mode
            String inputPath = args[0];  // First argument is input image path
            
            // Second argument is output path, or use default if not provided
            String outputPath = args.length >= 2 ? args[1] : "noise_removed.jpg";
            
            // Process the custom image specified by user
            System.out.println("Processing custom image:");
            processImage(processor, inputPath, outputPath);
        } else {
            // No arguments provided - process all test images in batch mode
            System.out.println("Processing all test images:");
            
            // Loop through all test images and process each one
            for (int i = 0; i < testImages.length; i++) {
                // Print header for current test image
                System.out.println("\n--- Processing Test Image " + (i + 1) + " ---");
                
                // Process current test image using corresponding paths
                processImage(processor, testImages[i], outputImages[i]);
            }
            
            // Provide summary after processing all images
            System.out.println("\n=== Processing Summary ===");
            System.out.println("All test images have been processed.");
            System.out.println("Output files saved in project root:");
            
            // List all output files for user reference
            for (String output : outputImages) {
                System.out.println("  - " + output);  // Indented list of output files
            }
        }
        
        // Print completion message and usage information
        System.out.println("\n=== Process Complete ===");
        System.out.println("Usage: java NoiseRemoving [input_path] [output_path]");
        System.out.println("       (without arguments, processes all test images)");
    }
    
    /*
    Process a single image for noise removal
     
    This private helper method encapsulates the complete workflow for
    
    processing a single image, including error handling and user feedback.
    
    It follows these steps:
    
    1. Load the input image
    
    2. Apply noise removal processing  
    
    3. Save the processed image
    
    4. Provide timing and status information
     
    
    @param processor The ImageProcess instance to use for operations
    
    @param inputPath Path to input image file
    
    @param outputPath Path for output image file
     */
    private static void processImage(ImageProcess processor, String inputPath, String outputPath) {
        // Display the file paths being processed
        System.out.println("Input image: " + inputPath);   // Show source file
        System.out.println("Output image: " + outputPath); // Show destination file
        
        // Attempt to load the input image
        if (!processor.loadImage(inputPath)) {
            // If loading failed, provide detailed error information
            System.err.println("Failed to load image: " + inputPath);
            System.err.println("Please check if the file exists and is a valid image format.");
            System.err.println("Make sure test images are placed in the project root directory.");
            return; // Exit this method early - no point continuing without an image
        }
        
        // Process the image to remove noise and measure processing time
        long startTime = System.currentTimeMillis();  // Record start time
        processor.cleanNoise();                        // Apply median filtering
        long endTime = System.currentTimeMillis();    // Record end time
        
        // Calculate and display processing time for performance monitoring
        System.out.println("Processing time: " + (endTime - startTime) + " ms");
        
        // Attempt to save the processed image
        if (processor.saveImage(outputPath)) {
            // If saving succeeded, confirm success to user
            System.out.println("Success! Noise-free image saved as: " + outputPath);
            System.out.println("Cleaned image location: Project root directory");
        } else {
            // If saving failed, notify user of the error
            System.err.println("Failed to save the processed image: " + outputPath);
        }
    }
}