# Salt-and-Pepper Noise Remover

A Java application that removes salt-and-pepper noise from images using a median filter algorithm. This project was developed as part of a Data Structures and Algorithms course assignment.

## 📋 Overview

Salt-and-pepper noise (impulse noise) appears as randomly occurring white and black pixels in digital images. This application effectively removes such noise using a 3×3 sliding window median filter approach.

**Student:** Suemon Kwok  

**Course:** Data Structures and Algorithms

## ✨ Features

- **GUI Application**: User-friendly graphical interface for easy image processing
- **Command-Line Tool**: Batch processing support for automation
- **Median Filter Algorithm**: Removes salt-and-pepper noise while preserving image details
- **Multiple Format Support**: Works with JPG, PNG, BMP, and GIF images
- **Before/After Comparison**: Side-by-side visualization of original and processed images
- **Test Images Included**: Three sample images for immediate testing
- **Progress Tracking**: Real-time feedback during processing operations

## 🖼️ Examples

### Before Processing
![Noisy Image](https://github.com/Suemon-Kwok/Salt-And-Pepper-Noise-Remover/raw/main/test%20image%201.jpg)

### After Processing
The median filter effectively removes noise while preserving image details and edges.

## 🛠️ Technical Details

### Algorithm: Median Filter
1. For each pixel, examines a 3×3 neighborhood (9 pixels total)
2. Extracts RGB color channels separately
3. Sorts each color channel using QuickSort
4. Replaces the center pixel with median values
5. Border pixels are copied unchanged

### Key Components

- **ImageProcess.java**: Core image processing logic and median filter implementation
- **SortArray.java**: Generic QuickSort implementation for finding median values
- **NoiseRemovingGUI.java**: Swing-based graphical user interface
- **NoiseRemoving.java**: Command-line interface for batch processing

### Time Complexity
- Median filter: O(n) where n is the number of pixels
- Per-pixel sorting: O(9 log 9) = O(1) constant time for 3×3 window
- Overall: O(n) linear time complexity

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Git (for cloning the repository)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Suemon-Kwok/Salt-And-Pepper-Noise-Remover.git
cd Salt-And-Pepper-Noise-Remover
```

2. Compile the Java files:
```bash
javac -d bin src/noiseremoving/*.java
```

### Usage

#### GUI Application
Run the graphical interface:
```bash
java -cp bin noiseremoving.NoiseRemovingGUI
```

**GUI Features:**
- Load images using the file chooser or test image buttons
- Click "Remove Noise" to process the loaded image
- View before/after comparison in real-time
- Save the processed image to your desired location

#### Command-Line Application

Process test images (batch mode):
```bash
java -cp bin noiseremoving.NoiseRemoving
```

Process a custom image:
```bash
java -cp bin noiseremoving.NoiseRemoving input_image.jpg output_image.jpg
```

**Examples:**
```bash
# Process a specific image
java -cp bin noiseremoving.NoiseRemoving "my_noisy_photo.png" "cleaned_photo.jpg"

# Process all test images (no arguments)
java -cp bin noiseremoving.NoiseRemoving
```

## 📂 Project Structure
```
Salt-And-Pepper-Noise-Remover/
├── src/
│   └── noiseremoving/
│       ├── ImageProcess.java       # Core image processing logic
│       ├── SortArray.java          # Generic sorting implementation
│       ├── NoiseRemovingGUI.java   # GUI application
│       └── NoiseRemoving.java      # CLI application
├── test image 1.jpg                # Sample noisy image (JPG)
├── test image 2.png                # Sample noisy image (PNG)
├── test image 3.jpg                # Sample noisy image (JPG)
└── README.md                       # This file
```

## 🎯 How It Works

### Median Filter Process

1. **Image Loading**: The application reads the input image into memory
2. **Sliding Window**: For each pixel (excluding borders):
   - Collects the 3×3 neighborhood (9 pixels)
   - Extracts RGB color channels separately
3. **Sorting**: Each color channel is sorted independently using QuickSort
4. **Median Selection**: The middle value (5th element) becomes the new pixel value
5. **Result**: The median values replace the center pixel, effectively removing noise

### Why Median Filter?

The median filter is particularly effective for salt-and-pepper noise because:
- Outlier pixels (extreme black/white values) are naturally filtered out
- Median values are robust against extreme values
- Edge preservation is better than averaging methods
- No new colors are introduced (uses existing pixel values)

## 📊 Algorithm Analysis

### QuickSort for Median Finding

**Question 1:** Is QuickSort the best way of finding the median?

**Answer:** No, QuickSort is not optimal for finding median because:
- Time complexity: O(n log n) average case
- Sorts the entire array when we only need the middle element
- Performs unnecessary work on elements we don't need

### Better Alternatives

1. **QuickSelect Algorithm**
   - Time complexity: O(n) average case
   - Only recurses on the side containing the median
   - More efficient for single-element selection

2. **Median of Medians**
   - Guaranteed O(n) worst-case performance
   - Divides array into groups of 5 elements
   - Provides deterministic linear time

3. **For Small Arrays (9 elements)**
   - Simple sorting networks or hardcoded comparisons
   - Lower overhead than recursive algorithms
   - Optimal for fixed-size windows

## 🧪 Testing

The project includes three test images:
- `test image 1.jpg` - Noisy photograph (JPG format)
- `test image 2.png` - Noisy image (PNG format)
- `test image 3.jpg` - Another noisy sample (JPG format)

Run the application without arguments to process all test images automatically.

## 🤝 Contributing

This is an academic project, but suggestions and improvements are welcome:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/improvement`)
3. Commit your changes (`git commit -am 'Add some improvement'`)
4. Push to the branch (`git push origin feature/improvement`)
5. Open a Pull Request

## 📝 License

This project is created for educational purposes as part of a university assignment.

## 🙏 Acknowledgments

- Course: Data Structures and Algorithms

- References: [Salt-and-pepper noise - Wikipedia](https://en.wikipedia.org/wiki/Salt-and-pepper_noise)

## 📧 Contact

**Suemon Kwok**  

GitHub: [@Suemon-Kwok](https://github.com/Suemon-Kwok)

---

Question 2) Sorting: Noise Removing Application (20%)
Problem: Salt-and-pepper noise, also known as impulse noise, is a form of noise sometimes seen on
digital images. This noise can be caused by sharp and sudden disturbances in the image signal. It
presents itself as sparsely occurring white and black pixels. (See more information:
https://en.wikipedia.org/wiki/Salt-and-
pepper_noise#:~:text=Salt%2Dand%2Dpepper%20noise%2C,occurring%20white%20and%20black%2
0pixels.)
Example (image with Salt-and-pepper noise)
An effective noise reduction method for this type of noise is a median filter.
Median Filter:
A program reads a pixel and its 8 surrounding pixels (This 3x3 block of pixels is called sliding window
in digital image processing) from an image then find the median of these 9 pixels. Finally, the median
replaces the central pixel. Sliding window moves onto the next pixel and repeats this process until all
pixels has been changed (To make the assignment simple, it excludes the bound pixels).
Median: the middle score when all the scores are arranged in order of size from the smallest to the
highest. If there are an even number of scores, then it is the average of the two middle scores (It will
not be the case in this assignment).
The code of reading image, getting pixels, saving pixels to an image has been done for you. Your task
is to find median for given pixels. In order to find the median, You need to sort an array of 9 pixels.
Example (output image after removing noise)
Your task:
Please download NoiseRemoving.zip file and extract it to a folder. The project contains two .java
files. “ImageProcess.java” and “NoiseRemoving.java”. If you run the project, it loads an image and
generates a .jpg file, named “noise_removed.jpg”, but the generated image still contains noise for
now. You need to complete the method of “cleanNoise” to clean the noise in the ImageProcess class.
“ImageProcess.java” deals with an image. It has a method named “cleanNoise”. There is a gap in the
method. You need to add your ArraySort Class to the project and fill the gap to complete the
ImageProcess Class (3%).
You need to write a “ArraySort” Class. It takes a generic Comparable type of array and sorts array in
order.
ArraySort Class has an “array” field. It stores a reference of an array. (1%)
ArraySort Class has “setArray” method. It takes a reference of an array and passes the reference to
the “array” field. (1%)
ArraySort Class has “quickSort” method. It runs quick sort algorithm and sorts array in order. (3%)

DO NOT COPY THE CODE FROM THE CHAPTER 3 EXAMPLES
ArraySort <E extends Comparable>
+ array : E[ ]
+ setArray(E[ ] array) : void
+ quickSort() : void
PLEASE FULLY COMMENT YOUR CODE (3%)
Answer following questions in your Comments at the beginning of your ArraySort Class code.
1. Is quick sort the best way of finding median? Why? (3%)
2. What is another good way of finding median? Please provide your explanation. (3%)
Finally, you need to design a GUI of noise removing application (2%). So, the application can load an
image, clean the noise, and save to a new image. And produce a .jar file of this project. (1%)
