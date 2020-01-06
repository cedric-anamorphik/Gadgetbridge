package nodomain.freeyourgadget.gadgetbridge.service.devices.qhybrid.requests.fossil_hr.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorSpace;

import androidx.annotation.ColorInt;

import java.io.IOException;

public class AssetImageFactory {
    public static AssetImage createAssetImage(String fileName, byte[] fileData, int angle, int distance, int indexZ){
        return new AssetImage(fileName, fileData, angle, distance, indexZ);
    }

    public static AssetImage createAssetImage(String fileName, Bitmap fileData, boolean RLEencode, int angle, int distance, int indexZ) throws IOException {
        int height = fileData.getHeight();
        int width = fileData.getWidth();

        // if(fileData.getConfig() != Bitmap.Config.ALPHA_8) throw new RuntimeException("Bitmap is not ALPHA_8");

        int[] pixels = new int[height * width];

        fileData.getPixels(pixels, 0, width, 0, 0, width, height);

        byte[] pixelBytes = new byte[width * height];

        for(int i = 0; i < pixels.length; i++){
            int monochrome = convertToMonochrome(pixels[i]);
            monochrome >>= 6;

            pixelBytes[i] = (byte) monochrome;
        }

        if(RLEencode){
            return new AssetImage(fileName, ImageConverter.encodeToRLEImage(pixelBytes, height, width), angle, distance, indexZ);
        }

        return null;
    }

    private static @ColorInt int convertToMonochrome(@ColorInt int color){
        int sum = Color.red(color) + Color.green(color) + Color.blue(color);

        sum /= 3;

        return sum;
    }
}