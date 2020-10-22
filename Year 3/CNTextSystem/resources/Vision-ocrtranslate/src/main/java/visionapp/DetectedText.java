package visionapp;

import com.google.cloud.vision.v1.BoundingPoly;

public class DetectedText {
    String text;
    String language;
    BoundingPoly poly;
}
