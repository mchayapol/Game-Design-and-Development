# Circle Animation Project

This project demonstrates a simple multi-threaded animation of circles moving from the top to the bottom of an 800x800 frame using Java.

## Project Structure

```
circle-animation
├── src
│   ├── main
│   │   └── java
│   │       ├── CircleAnimation.java
│   │       ├── AnimationFrame.java
│   │       ├── CircleThread.java
│   │       └── Circle.java
│   └── test
│       └── java
│           └── CircleAnimationTest.java
├── build.gradle
├── pom.xml
└── README.md
```

## Files Overview

- **CircleAnimation.java**: Entry point of the application. Initializes the animation frame and starts the threads for each circle.
  
- **AnimationFrame.java**: Extends `JFrame` to set up the 800x800 window and contains the logic to paint the circles on the frame.
  
- **CircleThread.java**: Implements `Runnable` to define the behavior of each circle's movement, updating the circle's position and repainting the frame.
  
- **Circle.java**: Defines the `Circle` class with properties such as position, radius, and color, along with methods to get the current position and draw the circle.
  
- **CircleAnimationTest.java**: Contains unit tests for the `CircleAnimation` class to ensure the animation starts correctly and that circles move as expected.

## Setup Instructions

1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd circle-animation
   ```

2. **Build the project**:
   - Using Gradle:
     ```
     ./gradlew build
     ```
   - Using Maven:
     ```
     mvn clean install
     ```

3. **Run the application**:
   ```
   java -cp build/libs/circle-animation.jar CircleAnimation
   ```

## Usage

Upon running the application, a window will appear displaying five circles that move from the top to the bottom of the frame. Each circle operates on its own thread, demonstrating the use of multi-threading in Java.

## Contributing

Feel free to submit issues or pull requests for improvements or bug fixes.