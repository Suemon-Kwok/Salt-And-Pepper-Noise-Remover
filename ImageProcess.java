package noiseremoving;

// Import required Java classes for image handling and file I/O operations
import java.awt.image.BufferedImage; // For working with images in memory
import java.io.File;                 // For file system operations
import java.io.IOException;          // For handling input/output exceptions
import javax.imageio.ImageIO;        // For reading and writing image files

/*
Name: Suemon Kwok

Student ID: 14883335

Data structures and algorithms
*/

/*
ImageProcess Class - Handles image loading, noise removal, and saving
 
Uses median filtering to remove salt-and-pepper noise from images

This class encapsulates all the core functionality for:
1. Loading images from files
2. Applying median filter for noise removal
3. Saving processed images back to files
4. Managing original and processed image data
 */
public class ImageProcess {
    
    // Instance variables to store the images in memory
    private BufferedImage originalImage;  // Stores the original loaded image
    private BufferedImage processedImage; // Stores the noise-removed processed image
    
    /**
     * Default constructor
     * Initializes both image references to null (no images loaded initially)
     */
    public ImageProcess() {
        this.originalImage = null;   // No original image loaded yet
        this.processedImage = null;  // No processed image created yet
    }
    
    /*
    Loads an image from the specified file path
    
    This method handles the complete image loading process including:
    - File validation and reading
    
    - Error handling for invalid files
    
    - Console output for user feedback
    
    @param imagePath Path to the image file (can be relative or absolute)
    
    @return true if image loaded successfully, false otherwise
     */
    public boolean loadImage(String imagePath) {
        try {
            // Create a File object from the provided path string
            File imageFile = new File(imagePath);
            
            // Use ImageIO to read the image file into a BufferedImage
            // This automatically handles different image formats (JPG, PNG, etc.)
            originalImage = ImageIO.read(imageFile);
            
            // Check if the image was successfully loaded (not null)
            if (originalImage != null) {
                // Image loaded successfully - provide feedback to user
                System.out.println("Image loaded successfully: " + imagePath);
                
                // Display image dimensions for user information
                System.out.println("Image dimensions: " + originalImage.getWidth() + 
                                 " x " + originalImage.getHeight());
                return true; // Return success status
            } else {
                // Image loading failed (file might not be a valid image)
                System.err.println("Failed to load image: " + imagePath);
                return false; // Return failure status
            }
        } catch (IOException e) {
            // Handle any input/output errors (file not found, access denied, etc.)
            System.err.println("Error loading image: " + e.getMessage());
            return false; // Return failure status
        }
    }
    
    /*
    Saves the processed image to the specified file path
    
    This method handles the complete image saving process including:
    
    - Validation that processed image exists
    
    - File format detection from extension
    
    - Error handling for save operations
    
    @param outputPath Path where the processed image will be saved
    
    @return true if image saved successfully, false otherwise
     */
    public boolean saveImage(String outputPath) {
        // Check if there's a processed image to save
        if (processedImage == null) {
            System.err.println("No processed image to save. Run cleanNoise() first.");
            return false; // Cannot save if no processed image exists
        }
        
        try {
            // Create a File object for the output path
            File outputFile = new File(outputPath);
            
            // Extract file extension to determine image format
            String formatName = "jpg"; // Default format if extension not found
            int dotIndex = outputPath.lastIndexOf('.'); // Find last dot in filename
            
            // If dot found and not at end of string, extract extension
            if (dotIndex > 0 && dotIndex < outputPath.length() - 1) {
                formatName = outputPath.substring(dotIndex + 1); // Get extension after dot
            }
            
            // Use ImageIO to write the processed image to file
            // Returns true if successful, false otherwise
            boolean success = ImageIO.write(processedImage, formatName, outputFile);
            
            // Check if the save operation was successful
            if (success) {
                System.out.println("Processed image saved successfully: " + outputPath);
                return true; // Return success status
            } else {
                System.err.println("Failed to save image: " + outputPath);
                return false; // Return failure status
            }
        } catch (IOException e) {
            // Handle any input/output errors during save operation
            System.err.println("Error saving image: " + e.getMessage());
            return false; // Return failure status
        }
    }
    
    /*
    Removes salt-and-pepper noise from the loaded image using median filtering
    
    This is the core image processing method that:
    
    1. Uses a 3x3 sliding window approach
    
    2. For each pixel, collects the 9 neighboring pixel values
    
    3. Sorts each color channel (R, G, B) separately
    
    4. Replaces the center pixel with the median values
    
    5. Handles border pixels separately
    
    The median filter is effective for salt-and-pepper noise because:
    
    - Salt-and-pepper noise appears as isolated extreme values (0 or 255)
    
    - The median value is resistant to these outliers
    
    - Preserves edges better than linear filters
     */
    public void cleanNoise() {
        // Validate that an image has been loaded
        if (originalImage == null) {
            System.err.println("No image loaded. Load an image first.");
            return; // Exit if no image to process
        }
        
        // Get image dimensions for processing loops
        int width = originalImage.getWidth();   // Number of pixels horizontally
        int height = originalImage.getHeight(); // Number of pixels vertically
        
        // Create a new BufferedImage for the processed result
        // Uses same type as original to preserve color model and transparency
        processedImage = new BufferedImage(width, height, originalImage.getType());
        
        // Inform user that processing has started
        System.out.println("Starting noise removal process...");
        
        // Process each pixel (excluding border pixels)
        // Start from (1,1) and end at (width-2, height-2) to avoid border
        // This ensures we always have a complete 3x3 neighborhood
        for (int y = 1; y < height - 1; y++) {      // Loop through rows (excluding borders)
            for (int x = 1; x < width - 1; x++) {   // Loop through columns (excluding borders)
                
                // Create arrays to store the 9 pixel values from 3x3 neighborhood
                // Using Integer arrays for compatibility with SortArray generic type
                Integer[] redValues = new Integer[9];   // Red color channel values
                Integer[] greenValues = new Integer[9]; // Green color channel values  
                Integer[] blueValues = new Integer[9];  // Blue color channel values
                
                int index = 0; // Index to track position in arrays (0-8)
                
                // Collect pixel values from 3x3 neighborhood around current pixel
                // dy ranges from -1 to +1 (three rows)
                for (int dy = -1; dy <= 1; dy++) {
                    // dx ranges from -1 to +1 (three columns)
                    for (int dx = -1; dx <= 1; dx++) {
                        // Calculate actual coordinates of neighbor pixel
                        int neighborX = x + dx; // Neighbor's x-coordinate
                        int neighborY = y + dy; // Neighbor's y-coordinate
                        
                        // Get RGB value of neighbor pixel as packed integer
                        // Format: 0xAARRGGBB (Alpha, Red, Green, Blue)
                        int rgb = originalImage.getRGB(neighborX, neighborY);
                        
                        // Extract individual color components using bit operations
                        // Right shift and mask to isolate each 8-bit color channel
                        int red = (rgb >> 16) & 0xFF;   // Extract red (bits 16-23)
                        int green = (rgb >> 8) & 0xFF;  // Extract green (bits 8-15)
                        int blue = rgb & 0xFF;          // Extract blue (bits 0-7)
                        
                        // Store extracted values in arrays for median calculation
                        redValues[index] = red;     // Store red value
                        greenValues[index] = green; // Store green value
                        blueValues[index] = blue;   // Store blue value
                        
                        index++; // Move to next position in arrays
                    }
                }
                
                // --- FILL THE GAP: Use SortArray to find median values ---
                
                // Create SortArray instances for each color channel
                // Each instance will sort its respective color channel values
                SortArray<Integer> redSorter = new SortArray<>(redValues);
                SortArray<Integer> greenSorter = new SortArray<>(greenValues);
                SortArray<Integer> blueSorter = new SortArray<>(blueValues);
                
                // Sort arrays and get median values (middle element after sorting)
                // For 9 elements, median is at index 4 (0-based indexing)
                int medianRed = redSorter.sortAndGetMedian();     // Get median red value
                int medianGreen = greenSorter.sortAndGetMedian(); // Get median green value
                int medianBlue = blueSorter.sortAndGetMedian();   // Get median blue value
                
                // --- END OF GAP FILLING ---
                
                // Combine median RGB values back into a single pixel value
                // Use bit shifting to pack values: (red << 16) | (green << 8) | blue
                // This creates the format: 0x00RRGGBB (no alpha channel modification)
                int newRGB = (medianRed << 16) | (medianGreen << 8) | medianBlue;
                
                // Set the new pixel value in the processed image at current position
                processedImage.setRGB(x, y, newRGB);
            }
            
            // Progress indicator - print status every 50 rows to show progress
            // Helps user know processing is ongoing for large images
            if (y % 50 == 0) {
                System.out.println("Processing row " + y + " of " + height);
            }
        }
        
        // Copy border pixels from original image (they are not processed by median filter)
        // Border pixels can't be processed because they don't have complete 3x3 neighborhoods
        copyBorderPixels();
        
        // Inform user that processing is complete
        System.out.println("Noise removal completed successfully!");
    }
    
    /*
    Copies border pixels from original image to processed image
    
    Border pixels (first/last row and first/last column) cannot be processed
    
    by the 3x3 median filter because they don't have complete neighborhoods.
    
    This method copies them unchanged from the original image.
     */
    private void copyBorderPixels() {
        // Get image dimensions for border copying
        int width = originalImage.getWidth();   // Image width in pixels
        int height = originalImage.getHeight(); // Image height in pixels
        
        // Copy top and bottom rows (entire width)
        for (int x = 0; x < width; x++) {
            // Copy top row (y=0) pixel by pixel
            processedImage.setRGB(x, 0, originalImage.getRGB(x, 0));
            
            // Copy bottom row (y=height-1) pixel by pixel  
            processedImage.setRGB(x, height - 1, originalImage.getRGB(x, height - 1));
        }
        
        // Copy left and right columns (entire height)
        // Note: corner pixels are copied twice but that's harmless
        for (int y = 0; y < height; y++) {
            // Copy left column (x=0) pixel by pixel
            processedImage.setRGB(0, y, originalImage.getRGB(0, y));
            
            // Copy right column (x=width-1) pixel by pixel
            processedImage.setRGB(width - 1, y, originalImage.getRGB(width - 1, y));
        }
    }
    
    /*
    Gets the original image
    
    @return BufferedImage of the original loaded image, or null if none loaded
     */
    public BufferedImage getOriginalImage() {
        return originalImage; // Return reference to original image
    }
    
    /*
     Gets the processed image
     
     @return BufferedImage of the noise-removed image, or null if not processed yet
     */
    public BufferedImage getProcessedImage() {
        return processedImage; // Return reference to processed image
    }
    
    /*
    Checks if an image has been loaded
    
    @return true if original image is loaded, false otherwise
     */
    public boolean hasImage() {
        return originalImage != null; // Check if original image reference is not null
    }
    
    /*
    Checks if image has been processed
    
    @return true if processed image exists, false otherwise
     */
    public boolean isProcessed() {
        return processedImage != null; // Check if processed image reference is not null
    }
}