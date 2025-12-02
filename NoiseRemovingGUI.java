package noiseremoving;

/*
Name: Suemon Kwok

Student ID: 14883335

Data structures and algorithms
*/


// Import Swing components for GUI creation
import javax.swing.*;                           // Core Swing classes for GUI

import javax.swing.UIManager;                  // For setting look and feel

import javax.swing.UnsupportedLookAndFeelException; // Exception for look and feel errors
import javax.swing.filechooser.FileNameExtensionFilter; // For file type filtering in dialogs

// Import AWT classes for layout and event handling
import java.awt.*;                             // Layout managers and basic GUI classes

import java.awt.event.ActionEvent;            // Action event class for button clicks

import java.awt.event.ActionListener;         // Interface for handling action events

import java.awt.image.BufferedImage;          // For image manipulation and display

// Import file I/O classes
import java.io.File;                          // For file system operations

/*
NoiseRemovingGUI - Graphical User Interface for Salt-and-Pepper Noise Removal

This class creates a comprehensive Swing-based GUI application that provides:
1. Intuitive interface for loading images via file chooser or test buttons
2. Visual before/after comparison with scaled image display
3. Progress indication during processing operations
4. Error handling with user-friendly dialog messages
5. Background processing to maintain UI responsiveness

The GUI is designed with modern usability principles:
- Clear visual feedback for all operations
- Disabled states for unavailable actions
- Progress indication for long operations
- Comprehensive error messages with helpful suggestions

Test images are located in the src folder with correct file formats
 */
public class NoiseRemovingGUI extends JFrame {
    
    // GUI Components - Main layout panels
    private JPanel mainPanel;      // Root panel containing all other panels
    
    private JPanel imagePanel;     // Panel for displaying before/after images
    
    private JPanel controlPanel;   // Panel containing all control buttons
    
    // Image display components
    private JLabel originalLabel;   // Displays the original image with border
    
    private JLabel processedLabel;  // Displays the processed image with border
    
    // Control buttons for main functionality
    private JButton loadButton;     // Button to open file chooser for loading images
    
    private JButton processButton;  // Button to start noise removal processing
    
    private JButton saveButton;     // Button to save processed image to file
    
    private JButton exitButton;     // Button to exit the application
    
    // Status and progress components
    private JProgressBar progressBar; // Shows processing progress (indeterminate)
    
    private JLabel statusLabel;       // Displays current status messages
    
    // Test image buttons for quick loading of sample images
    private JButton test1Button;   // Load test image 1 (JPG format)
    
    private JButton test2Button;   // Load test image 2 (PNG format)  
    
    private JButton test3Button;   // Load test image 3 (JPG format)
    
    // Image processing backend
    private ImageProcess processor; // Instance of ImageProcess class for actual processing
    
    private String currentImagePath; // Stores path of currently loaded image
    
    // Image display configuration constants
    private final int MAX_IMAGE_WIDTH = 400;  // Maximum width for displayed images
    
    private final int MAX_IMAGE_HEIGHT = 300; // Maximum height for displayed images
    
    // Test image paths (in folder with correct file formats)
    // These paths point to sample images for testing the application
    private final String[] TEST_IMAGE_PATHS = {
        "test image 1.jpg",  // JPG format - most common image format
        "test image 2.png",  // PNG format - supports transparency
        "test image 3.jpg"   // JPG format - another sample image
    };
    
    /*
    Constructor - Initialize the GUI
     
    This constructor sets up the complete GUI by:
    
    1. Creating the ImageProcess backend instance
    
    2. Initializing all GUI components and layout
    
    3. Setting up event handlers for user interactions
     */
    public NoiseRemovingGUI() {
        
        processor = new ImageProcess(); // Create the image processing backend
        
        initializeGUI();               // Set up all GUI components and layout
        
        setupEventHandlers();          // Connect buttons to their action methods
    }
    
    /*
    Initialize GUI components and layout
     
    This method creates the entire user interface structure:
    
    1. Main window properties (title, size, close behavior)
    
    2. Panel hierarchy and layout managers
    
    3. All GUI components with proper configuration
    
    4. Initial button states based on application state
     */
    private void initializeGUI() {
        // Configure main window properties
        setTitle("Salt-and-Pepper Noise Removal Tool"); // Set window title
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit app when window closed
        
        setLayout(new BorderLayout());                   // Use BorderLayout for main frame
        
        // Create main panel to contain all other panels
        mainPanel = new JPanel(new BorderLayout());
        
        // Create and configure all sub-panels
        createImagePanel();   // Create the before/after image display area
        
        createControlPanel(); // Create the button control area
        
        createStatusPanel();  // Create the status bar and progress indicator
        
        // Add panels to main frame using BorderLayout positions
        add(mainPanel, BorderLayout.CENTER);      // Main content in center
        
        add(controlPanel, BorderLayout.SOUTH);    // Controls at bottom
        
        // Set window properties for proper display
        setSize(950, 650);        // Set window dimensions (width x height)
        
        setLocationRelativeTo(null); // Center window on screen
        
        setResizable(true);          // Allow user to resize window
        
        // Set initial button states based on current application state
        updateButtonStates(); // Enable/disable buttons appropriately
    }
    
    /*
    Creates the image display panel with before/after views
     
    This method sets up the main visual area of the application:
    1. Creates a side-by-side layout for original and processed images
    
    2. Adds titled borders for clear labeling
    
    3. Sets up image labels with proper sizing and borders
    
    4. Configures default "no image" states
     */
    private void createImagePanel() {
        // Create main image panel with 1 row, 2 columns layout
        imagePanel = new JPanel(new GridLayout(1, 2, 10, 10)); // 10px gaps between panels
        imagePanel.setBorder(BorderFactory.createTitledBorder("Image Preview"));
        
        // Create original image panel (left side)
        JPanel originalPanel = new JPanel(new BorderLayout());
        originalPanel.setBorder(BorderFactory.createTitledBorder("Original Image"));
        
        // Create label to display original image
        originalLabel = new JLabel("No image loaded", SwingConstants.CENTER);
        originalLabel.setPreferredSize(new Dimension(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT));
        originalLabel.setBorder(BorderFactory.createLoweredBevelBorder()); // 3D border effect
        originalPanel.add(originalLabel, BorderLayout.CENTER);
        
        // Create processed image panel (right side)  
        JPanel processedPanel = new JPanel(new BorderLayout());
        processedPanel.setBorder(BorderFactory.createTitledBorder("Processed Image"));
        
        // Create label to display processed image
        processedLabel = new JLabel("No processed image", SwingConstants.CENTER);
        processedLabel.setPreferredSize(new Dimension(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT));
        processedLabel.setBorder(BorderFactory.createLoweredBevelBorder()); // 3D border effect
        processedPanel.add(processedLabel, BorderLayout.CENTER);
        
        // Add both panels to the main image panel
        imagePanel.add(originalPanel);  // Left side
        imagePanel.add(processedPanel); // Right side
        
        // Add image panel to the main panel's center area
        mainPanel.add(imagePanel, BorderLayout.CENTER);
    }
    
    /*
    Creates the control panel with buttons
    
    This method sets up all the interactive controls:
    
    1. Main functionality buttons (load, process, save, exit)
    
    2. Test image quick-load buttons
    
    3. Proper layout and grouping with borders
    
    4. Tooltips for user guidance
     */
    private void createControlPanel() {
        // Create main control panel with BorderLayout
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        
        // Create panel for main control buttons
        JPanel mainButtons = new JPanel(new FlowLayout()); // Flow layout centers buttons
        
        // Create and configure load button
        loadButton = new JButton("Load Image");
        loadButton.setToolTipText("Load an image file to remove noise from");
        
        // Create and configure process button
        processButton = new JButton("Remove Noise");
        processButton.setToolTipText("Apply median filter to remove salt-and-pepper noise");
        
        // Create and configure save button
        saveButton = new JButton("Save Image");
        saveButton.setToolTipText("Save the processed image to file");
        
        // Create and configure exit button
        exitButton = new JButton("Exit");
        exitButton.setToolTipText("Exit the application");
        
        // Add main buttons to the main buttons panel
        mainButtons.add(loadButton);
        mainButtons.add(processButton);
        mainButtons.add(saveButton);
        mainButtons.add(new JSeparator(SwingConstants.VERTICAL)); // Visual separator
        mainButtons.add(exitButton);
        
        // Create panel for test image buttons
        JPanel testPanel = new JPanel(new FlowLayout());
        testPanel.setBorder(BorderFactory.createTitledBorder("Test Images (Located in folder)"));
        
        // Create test image buttons with descriptive labels
        test1Button = new JButton("Load Test Image 1 (JPG)");
        test1Button.setToolTipText("Load test image 1.jpg");
        
        test2Button = new JButton("Load Test Image 2 (PNG)");
        test2Button.setToolTipText("Load test image 2.png");
        
        test3Button = new JButton("Load Test Image 3 (JPG)");
        test3Button.setToolTipText("Load test image 3.jpg");
        
        // Add test buttons to test panel
        testPanel.add(test1Button);
        testPanel.add(test2Button);
        testPanel.add(test3Button);
        
        // Organize control panel layout
        controlPanel.add(testPanel, BorderLayout.NORTH);     // Test buttons at top
        controlPanel.add(mainButtons, BorderLayout.CENTER);  // Main buttons in center
    }
    
    /*
    Creates the status panel with progress bar and status label
     
    This method sets up the status area at the bottom:
    
    1. Status label for text messages
    
    2. Progress bar for operation feedback
    
    3. Proper layout and visual borders
     */
    private void createStatusPanel() {
        // Create status panel with BorderLayout for flexible component positioning
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder()); // 3D sunken border
        
        // Create status label for displaying current operation status
        statusLabel = new JLabel("Ready - Load an image to begin");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding around text
        
        // Create progress bar for showing operation progress
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);  // Show percentage text on progress bar
        progressBar.setVisible(false);       // Initially hidden until processing starts
        
        // Add components to status panel
        statusPanel.add(statusLabel, BorderLayout.WEST);   // Status text on left
        statusPanel.add(progressBar, BorderLayout.CENTER); // Progress bar in center
        
        // Add status panel to main panel at bottom
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
    }
    
    /*
    Set up event handlers for buttons
     
    This method connects each button to its corresponding action method
    
    using anonymous ActionListener classes. Each listener defines what
    
    happens when the button is clicked.
     */
    private void setupEventHandlers() {
        // Load button handler - opens file chooser dialog
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadImage(); // Call method to handle image loading
            }
        });
        
        // Process button handler - starts noise removal processing
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processImage(); // Call method to handle image processing
            }
        });
        
        // Save button handler - opens save dialog and saves processed image
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage(); // Call method to handle image saving
            }
        });
        
        // Exit button handler - terminates the application
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit with status code 0 (normal termination)
            }
        });
        
        // Test image button handlers - load predefined test images
        test1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTestImage(0); // Load first test image (index 0)
            }
        });
        
        test2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTestImage(1); // Load second test image (index 1)
            }
        });
        
        test3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTestImage(2); // Load third test image (index 2)
            }
        });
    }
    
    /*
    Load a test image
    
    This method handles loading of predefined test images with proper
    
    error handling and user feedback. It uses background threading to
    
    prevent UI freezing during file I/O operations.
    
    @param testIndex Index of the test image (0=JPG, 1=PNG, 2=JPG)
     */
    private void loadTestImage(int testIndex) {
        // Validate the test index parameter
        if (testIndex < 0 || testIndex >= TEST_IMAGE_PATHS.length) {
            JOptionPane.showMessageDialog(this,
                "Invalid test image index.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit early if invalid index
        }
        
        // Get the file path for the specified test image
        currentImagePath = TEST_IMAGE_PATHS[testIndex];
        String[] formats = {"JPG", "PNG", "JPG"}; // Format labels for user feedback
        statusLabel.setText("Loading test image " + (testIndex + 1) + " (" + formats[testIndex] + ")...");
        
        // Load image in background thread to prevent UI blocking
        // SwingWorker allows background processing while maintaining UI responsiveness
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // This runs in background thread - perform file I/O operation
                return processor.loadImage(currentImagePath);
            }
            
            @Override
            protected void done() {
                // This runs in EDT after background work completes
                try {
                    boolean success = get(); // Get result from background operation
                    if (success) {
                        // Image loaded successfully - update UI
                        displayOriginalImage(); // Show the loaded image
                        statusLabel.setText("Test image " + (testIndex + 1) + " (" + formats[testIndex] + ") loaded successfully");
                        processedLabel.setIcon(null);      // Clear processed image display
                        processedLabel.setText("No processed image"); // Reset processed label
                    } else {
                        // Image loading failed - show error message
                        statusLabel.setText("Failed to load test image " + (testIndex + 1));
                        JOptionPane.showMessageDialog(NoiseRemovingGUI.this,
                            "Failed to load test image " + (testIndex + 1) + " (" + formats[testIndex] + ").\n" +
                            "Please ensure the test image files are located in the project root folder:\n" +
                            "- " + TEST_IMAGE_PATHS[testIndex] + "\n\n" +
                            "Expected file formats:\n" +
                            "- test image 1.jpg (JPG format)\n" +
                            "- test image 2.png (PNG format)\n" +
                            "- test image 3.jpg (JPG format)",
                            "Test Image Error", JOptionPane.ERROR_MESSAGE);
                    }
                    updateButtonStates(); // Update button enabled/disabled states
                } catch (Exception ex) {
                    // Handle any exceptions that occurred during processing
                    statusLabel.setText("Error loading test image " + (testIndex + 1));
                    ex.printStackTrace(); // Print stack trace for debugging
                }
            }
        };
        worker.execute(); // Start the background worker
    }
    
    /*
    Load an image file using file chooser
    
    This method opens a file selection dialog allowing users to choose
    
    any supported image file from their system. It includes file filtering
    
    to show only valid image formats.
     */
    private void loadImage() {
        // Create file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image File");
        
        // Set file filters to show only image files
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png", "bmp", "gif");
        fileChooser.setFileFilter(filter);
        
        // Show open dialog and get user's choice
        int result = fileChooser.showOpenDialog(this);
        
        // Check if user selected a file (didn't cancel)
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();      // Get selected file
            currentImagePath = selectedFile.getAbsolutePath();     // Store full path
            
            statusLabel.setText("Loading image..."); // Update status
            
            // Load image in background thread to maintain UI responsiveness
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    // Background task - load the image file
                    return processor.loadImage(currentImagePath);
                }
                
                @Override
                protected void done() {
                    // UI thread - handle results and update interface
                    try {
                        boolean success = get(); // Get background task result
                        if (success) {
                            // Successfully loaded image
                            displayOriginalImage(); // Display the loaded image
                            statusLabel.setText("Image loaded: " + selectedFile.getName());
                            processedLabel.setIcon(null);      // Clear any previous processed image
                            processedLabel.setText("No processed image");
                        } else {
                            // Failed to load image
                            statusLabel.setText("Failed to load image");
                            JOptionPane.showMessageDialog(NoiseRemovingGUI.this,
                                "Failed to load the selected image file.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        updateButtonStates(); // Update button availability
                    } catch (Exception ex) {
                        // Handle any exceptions
                        statusLabel.setText("Error loading image");
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute(); // Start background loading
        }
    }
    
    /*
    Process the loaded image to remove noise
     
    This method applies the median filter algorithm to remove salt-and-pepper
    
    noise from the currently loaded image. It uses background processing to
    
    maintain UI responsiveness during the potentially time-consuming operation.
     */
    private void processImage() {
        // Check if image is loaded before processing
        if (!processor.hasImage()) {
            JOptionPane.showMessageDialog(this,
                "Please load an image first.",
                "No Image", JOptionPane.WARNING_MESSAGE);
            return; // Exit if no image loaded
        }
        
        // Update UI to show processing state
        statusLabel.setText("Processing image - removing noise...");
        progressBar.setVisible(true);        // Show progress bar
        progressBar.setIndeterminate(true);  // Show indeterminate progress (spinning)
        
        // Disable buttons during processing to prevent interference
        setButtonsEnabled(false);
        
        // Process image in background thread
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Background task - apply noise removal algorithm
                processor.cleanNoise(); // This calls the median filter implementation
                return null;
            }
            
            @Override
            protected void done() {
                // UI thread - handle completion and update interface
                try {
                    displayProcessedImage(); // Show the processed result
                    statusLabel.setText("Noise removal completed successfully");
                    progressBar.setVisible(false); // Hide progress bar
                } catch (Exception ex) {
                    // Handle any processing errors
                    statusLabel.setText("Error processing image");
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(NoiseRemovingGUI.this,
                        "An error occurred while processing the image.",
                        "Processing Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Always re-enable buttons and update states
                    setButtonsEnabled(true);
                    updateButtonStates();
                }
            }
        };
        worker.execute(); // Start background processing
    }
    
    /*
    Save the processed image to file
    
    This method opens a save dialog allowing users to specify where
    
    to save the processed image. It includes proper file extension
    
    handling and background saving to maintain UI responsiveness.
     */
    private void saveImage() {
        // Check if processed image exists
        if (!processor.isProcessed()) {
            JOptionPane.showMessageDialog(this,
                "Please process an image first.",
                "No Processed Image", JOptionPane.WARNING_MESSAGE);
            return; // Exit if no processed image
        }
        
        // Create file chooser for save operation
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Processed Image");
        
        // Suggest filename based on current image
        String suggestedName = "noise_removed.jpg"; // Default name
        if (currentImagePath != null) {
            // Extract base name from current image path
            String baseName = new File(currentImagePath).getName();
            int dotIndex = baseName.lastIndexOf('.'); // Find extension separator
            if (dotIndex > 0) {
                baseName = baseName.substring(0, dotIndex); // Remove extension
            }
            suggestedName = baseName + "_cleaned.jpg"; // Add "_cleaned" suffix
        }
        fileChooser.setSelectedFile(new File(suggestedName));
        
        // Set file filters for save dialog
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPEG Images", "jpg", "jpeg");
        fileChooser.setFileFilter(filter);
        
        // Show save dialog
        int result = fileChooser.showSaveDialog(this);
        
        // Check if user chose to save (didn't cancel)
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String savePath = selectedFile.getAbsolutePath();
            
            // Ensure .jpg extension is present
            if (!savePath.toLowerCase().endsWith(".jpg") && 
                !savePath.toLowerCase().endsWith(".jpeg")) {
                savePath += ".jpg"; // Add .jpg extension if missing
            }
            
            // Make savePath final for use in anonymous class
            final String finalSavePath = savePath;
            
            statusLabel.setText("Saving image..."); // Update status
            
            // Save image in background thread
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    // Background task - save the processed image
                    return processor.saveImage(finalSavePath);
                }
                
                @Override
                protected void done() {
                    // UI thread - handle save completion
                    try {
                        boolean success = get(); // Get save operation result
                        if (success) {
                            // Save successful
                            statusLabel.setText("Image saved successfully: " + 
                                              new File(finalSavePath).getName());
                            JOptionPane.showMessageDialog(NoiseRemovingGUI.this,
                                "Image saved successfully!\n" +
                                "Location: " + finalSavePath,
                                "Save Complete", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // Save failed
                            statusLabel.setText("Failed to save image");
                            JOptionPane.showMessageDialog(NoiseRemovingGUI.this,
                                "Failed to save the image file.",
                                "Save Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        // Handle save errors
                        statusLabel.setText("Error saving image");
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute(); // Start background save operation
        }
    }
    
    /*
    Display the original image in the GUI
   
    This method takes the loaded original image and displays it
    
    in the left panel with appropriate scaling to fit the display area.
     */
    private void displayOriginalImage() {
        BufferedImage image = processor.getOriginalImage(); // Get original image
        if (image != null) {
            ImageIcon icon = createScaledImageIcon(image); // Scale image for display
            originalLabel.setIcon(icon); // Set image as label icon
            originalLabel.setText("");   // Clear any text (like "No image loaded")
        }
    }
    
    /*
    Display the processed image in the GUI
     
    This method takes the processed image and displays it
    
    in the right panel with appropriate scaling to fit the display area.
     */
    private void displayProcessedImage() {
        BufferedImage image = processor.getProcessedImage(); // Get processed image
        if (image != null) {
            ImageIcon icon = createScaledImageIcon(image); // Scale image for display
            processedLabel.setIcon(icon); // Set image as label icon
            processedLabel.setText("");   // Clear any text (like "No processed image")
        }
    }
    
    /*
    Create a scaled ImageIcon from BufferedImage
    
    This method handles image scaling to fit within the display area
    
    while preserving the original aspect ratio. It prevents images
    
    from being displayed too large and distorting the GUI layout.
    
    @param image The BufferedImage to scale
    
    @return Scaled ImageIcon ready for display
     */
    private ImageIcon createScaledImageIcon(BufferedImage image) {
        int originalWidth = image.getWidth();   // Get original image width
        int originalHeight = image.getHeight(); // Get original image height
        
        // Calculate scaling to fit within max dimensions while preserving aspect ratio
        double scaleX = (double) MAX_IMAGE_WIDTH / originalWidth;   // X-axis scaling factor
        double scaleY = (double) MAX_IMAGE_HEIGHT / originalHeight; // Y-axis scaling factor
        double scale = Math.min(scaleX, scaleY); // Use smaller scale to fit both dimensions
        
        if (scale >= 1.0) {
            // No scaling needed - image is already small enough
            return new ImageIcon(image);
        }
        
        // Calculate new dimensions after scaling
        int scaledWidth = (int) (originalWidth * scale);   // New width
        int scaledHeight = (int) (originalHeight * scale); // New height
        
        // Create scaled version of the image using smooth scaling algorithm
        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage); // Return as ImageIcon
    }
    
    /*
    Update button states based on current application state
    
    This method enables or disables buttons based on what operations
    
    are currently valid. This provides clear visual feedback to users
    
    about what actions they can take at any given time.
     */
    private void updateButtonStates() {
        // Process button: enabled only if image is loaded
        processButton.setEnabled(processor.hasImage());
        
        // Save button: enabled only if image has been processed  
        saveButton.setEnabled(processor.isProcessed());
        
        // Load and exit buttons are always available (not controlled here)
        // Test buttons are always available (not controlled here)
    }
    
    /*
    Enable or disable all control buttons
    
    This method is used to disable all buttons during processing
    
    operations to prevent user interference with ongoing tasks.
    
    @param enabled true to enable buttons, false to disable
     */
    private void setButtonsEnabled(boolean enabled) {
        // Main functionality buttons
        loadButton.setEnabled(enabled);    // Always follows enabled parameter
        processButton.setEnabled(enabled && processor.hasImage());    // Enabled if parameter true AND image loaded
        saveButton.setEnabled(enabled && processor.isProcessed());    // Enabled if parameter true AND image processed
        exitButton.setEnabled(enabled);    // Always follows enabled parameter
        
        // Test image buttons
        test1Button.setEnabled(enabled);   // Always follows enabled parameter
        test2Button.setEnabled(enabled);   // Always follows enabled parameter  
        test3Button.setEnabled(enabled);   // Always follows enabled parameter
    }
    
    /*
    Main method to run the GUI application
    
    This is the entry point for the GUI version of the application.
    
    It sets up the look and feel and creates the main window on the
    
    proper thread for Swing applications.
    
    @param args Command line arguments (not used in GUI version)
     */
    public static void main(String[] args) {
        // Set system look and feel for native appearance
        try {
            // Use the operating system's native look and feel
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system L&F fails - this is acceptable
            e.printStackTrace();
        }
        
        // Create and show GUI on Event Dispatch Thread (EDT)
        // This is required for thread safety in Swing applications
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create new GUI instance and make it visible
                new NoiseRemovingGUI().setVisible(true);
            }
        });
    }
}