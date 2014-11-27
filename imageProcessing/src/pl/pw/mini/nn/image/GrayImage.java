package pl.pw.mini.nn.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Pawel on 2014-11-27.
 */
public class GrayImage {
    private BufferedImage _image;
    private int _grayTable[];

    private static int INPUT_SIZE = 64;
    private double _networkInput[][];
    private int _width;
    private int _height;

    public GrayImage(String path, boolean print) {
        File input = new File(path);
        try {
            _image = ImageIO.read(input);
            _width = _image.getWidth();
            _height = _image.getHeight();

            _grayTable = new int[_width * _height];
            int count = 0;
            for (int i = 0; i < _width; ++i)
                for (int j = 0; j < _height; ++j) {
                    Color c = new Color(_image.getRGB(i, j));
                    _grayTable[_width * i + j] = c.getRed();

                    if (print) {
                        System.out.println("S.No: " + count + " Red: " + c.getRed() +
                                " Green: " + c.getGreen() + " Blue: " + c.getBlue());
                    }
                    ++count;
                }

            parseImageToNetworkImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GrayImage(String path) {
        new GrayImage(path, true);
    }

    private void parseImageToNetworkImage(int inputLength) throws Exception {
        int PixelsCount = _width * _height;
        if (PixelsCount % inputLength != 0) {
            throw new Exception("Image is not divisible by input length " + inputLength);
        }

        int Rows = PixelsCount / inputLength;

        int row = 0, col = 0;
        _networkInput = new double[Rows][inputLength];
        for (int i = 0; i < _width; ++i)
            for (int j = 0; j < _height; ++j) {
                int pixel = _width * i + j;
                row = pixel / inputLength;
                col = pixel % inputLength;
                _networkInput[row][col] = _grayTable[pixel];
            }
    }

    private void parseImageToNetworkImage() throws Exception {
        this.parseImageToNetworkImage(INPUT_SIZE);
    }

    public double[][] getNetworkInput(){
        return _networkInput;
    }
}
