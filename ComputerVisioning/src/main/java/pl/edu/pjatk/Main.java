package pl.edu.pjatk;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 * this application will output ALL frames containing
 * POLISH , RUSSIAN, USA Flags hidden in .mp4 file video
 * using color analysis in Java with OpenCV
 * <p>
 * Using OpenCV Java library. Program will create color masks and
 * will try to see if certain frame of video contains proper or higher than threshold
 * amount of pixels matching mask colors
 * <p>
 * if it does it will output this frames to /data directory
 * <p>
 * * @author s12901 Stanisław Kibort for NAI winter semester 2023/2024
 */
public class Main {
    private static final String MOVIE_FILE_PATH = "ComputerVisioning/src/main/resources/cars_and_flags.mp4";
    private static final String WHITE_RED_FLAG_FRAME_WRITE_PATH = "ComputerVisioning/data/frame_with_wr_flag_";
    private static final Integer WHITE_RED_FLAG_PIXEL_THRESHOLD = 2200;

    private static int whiteRedFlagFrameCounter = 0;

    /**
     * main method running Java application
     *
     * @param args console arguments
     */
    public static void main(String[] args) {
        OpenCV.loadShared();
        Mat vidMat = new Mat();
        VideoCapture capture = new VideoCapture();
        capture.open(MOVIE_FILE_PATH);
        while (capture.read(vidMat)) {
            Mat currentFrame = new Mat();
            capture.retrieve(currentFrame);
            Mat thresholdImage = getThresholdImageForWhiteRedFlagFromCurrentFrame(currentFrame);
            writeFramesWithWhiteRedFlag(thresholdImage, currentFrame);
        }
        capture.release();
    }

    /**
     * Check if a frame matches pixel threshold for colors mask
     * if it does, then save this frame as image data indicating success!
     *
     * @param whiteRedFlagThresholdImage threshold image as result of mask combination
     * @param currentFrame               current frame of video
     */
    private static void writeFramesWithWhiteRedFlag(Mat whiteRedFlagThresholdImage, Mat currentFrame) {
        // Count the number of white and red pixels
        int whitePixels = Core.countNonZero(whiteRedFlagThresholdImage);
        int redPixels = Core.countNonZero(whiteRedFlagThresholdImage);

        // Check if there are enough white and red pixels
        if (isWhiteRedFlagColorPixelThreshold(whitePixels, redPixels)) {
            System.out.println("WHITE RED FLAG FOUND");
            whiteRedFlagFrameCounter++;
            Imgcodecs.imwrite(WHITE_RED_FLAG_FRAME_WRITE_PATH + whiteRedFlagFrameCounter + ".jpg", currentFrame);
        }
    }

    /**
     * Write frame data to white red blue color masks using color analysis in Java OpenCV
     *
     * @param currentFrame Mat currentFrame of video
     * @return frame containing some red and white and blue pixels information
     */
    private static Mat getThresholdImageForWhiteRedFlagFromCurrentFrame(Mat currentFrame) {
        //  HSV (Hue, Saturation, Value) is a color space that represents colors based on their hue, saturation, and value.
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(currentFrame, hsvImage, Imgproc.COLOR_BGR2HSV);

        Mat redMask = thresholdRedChannel(hsvImage);
        Mat whiteMask = thresholdWhiteChannel(hsvImage);
        Mat blueMask = thresholdBlueChannel(hsvImage);

        return combineWhiteRedBlueMasks(whiteMask, redMask, blueMask);
    }

    /**
     * threshold red channel
     *
     * @param hsvImage hsvImage
     * @return redMask
     */
    private static Mat thresholdRedChannel(Mat hsvImage) {
        Mat redMask = new Mat();
        Core.inRange(hsvImage, new Scalar(0, 0, 255), new Scalar(0, 0, 255), redMask);
        return redMask;
    }

    /**
     * threshold white channel
     *
     * @param hsvImage hsvImage
     * @return whiteMask
     */
    private static Mat thresholdWhiteChannel(Mat hsvImage) {
        Mat whiteMask = new Mat();
        Core.inRange(hsvImage, new Scalar(255, 255, 255), new Scalar(255, 255, 255), whiteMask);
        return whiteMask;
    }

    /**
     * threshold blue channel
     *
     * @param hsvImage hsvImage
     * @return blueMask
     */
    private static Mat thresholdBlueChannel(Mat hsvImage) {
        Mat blueMask = new Mat();
        Core.inRange(hsvImage, new Scalar(255, 0, 0), new Scalar(255, 0, 0), blueMask);
        return blueMask;
    }

    /**
     * try combine all masks into one
     */
    private static Mat combineWhiteRedBlueMasks(Mat white, Mat red, Mat blue) {
        // Combine the red and white masks using core addition
        Mat mask = new Mat();
        Core.add(red, white, mask);
        Mat whiteRedBlueMask = new Mat();
        Core.add(red, white, whiteRedBlueMask, mask);
        return whiteRedBlueMask;
    }

    /**
     * @param whitePixels white pixels from frame
     * @param redPixels   red pixels from frame
     * @return is over threshold
     */
    private static boolean isWhiteRedFlagColorPixelThreshold(int whitePixels, int redPixels) {
        return whitePixels >= WHITE_RED_FLAG_PIXEL_THRESHOLD && redPixels >= WHITE_RED_FLAG_PIXEL_THRESHOLD;
    }
}