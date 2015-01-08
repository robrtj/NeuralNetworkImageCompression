package pl.edu.pw.mini.nn.image;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Pawel on 2014-11-27.
 * <p/>
 * Usage:
 * GrayImage image = new GrayImage("lena_1.png");
 * file lena_1.png exists in main project folder (the same level that /src folder is)
 * i.e. GrayImageParser image = new GrayImageParser("lena_1.png");
 */
public class GrayImageParser {
    private BufferedImage _image;
    private int _grayTable[][];

    private static int INPUT_SIZE = 64;
    private double _networkInput[];
    private int _width;
    private int _height;
    private boolean _isBipolar = false;

    public GrayImageParser(String path, boolean print) {
        File input = new File(path);
        try {
            _image = ImageIO.read(input);
            _width = _image.getWidth();
            _height = _image.getHeight();

            _grayTable = new int[_width][_height];
            int count = 0;
            for (int i = 0; i < _width; ++i)
                for (int j = 0; j < _height; ++j) {
                    Color c = new Color(_image.getRGB(i, j));
                    _grayTable[i][j] = c.getRed();

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
        this(path, false);
    }

    public GrayImageParser(String path, boolean print, boolean isBipolar) {
        this(path, print);
        _isBipolar = isBipolar;
    }

    private void parseImageToNetworkImage(int inputLength) throws Exception {
        int PixelsCount = _width * _height;
        if (PixelsCount % inputLength != 0) {
            throw new Exception("Image is not divisible by input length " + inputLength);
        }
        int blockCount = PixelsCount/inputLength;
        _networkInput = new double[blockCount];
        for (int i = 0; i < blockCount; i++) {
            _networkInput[i] = 0;
        }

        int pixelsBlockWidth = (int) Math.sqrt(inputLength);

        for (int i = 0; i < _width; ++i) {
            for (int j = 0; j < _height; ++j) {
                //+1 because don't want to have 0
                _networkInput[i - i % pixelsBlockWidth + j - j % pixelsBlockWidth] += _grayTable[i][j] + 1;
            }
        }

        for (int i = 0; i < blockCount; i++) {
            _networkInput[i] /= 256 * inputLength;
        }

        if (_isBipolar) {
            for (int i = 0; i < blockCount; i++) {
                _networkInput[i] = _networkInput[i] * 2 - 1;
            }
        }
    }


    private void parseImageToNetworkImage() throws Exception {
        this.parseImageToNetworkImage(INPUT_SIZE);
    }

    public double[] getNetworkInput() {
        return _networkInput;
    }
}
