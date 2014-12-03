package pl.edu.pw.mini.nn.image;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Pawel on 2014-11-27.
 *
 * Usage:
 * GrayImage image = new GrayImage("lena_1.png");
 * file lena_1.png exists in main project folder (the same level that /src folder is)
 * i.e. GrayImageParser image = new GrayImageParser("lena_1.png");
 */
public class GrayImageParser {
    private BufferedImage _image;
    private int _grayTable[];

    private static int INPUT_SIZE = 64;
    private double _networkInput[][];
    private int _width;
    private int _height;

    public GrayImageParser(String path, boolean print) {
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

    public GrayImageParser(String path) {
        new GrayImageParser(path, true);
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
