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
    private double _networkInput[][];
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
        int blocksCount = PixelsCount / inputLength;
        _networkInput = new double[blocksCount][inputLength];
        int pixelsBlockWidth = (int) Math.sqrt(inputLength);
        int widthDivBlockWidth = _width / pixelsBlockWidth;

        for (int i = 0; i < _width; ++i) {
            for (int j = 0; j < _height; ++j) {
                //+1 because don't want to have 0
                int pixelBlockNumber = (i / pixelsBlockWidth) * widthDivBlockWidth + j / pixelsBlockWidth;
                int numberOfPixelInBlock = (i % pixelsBlockWidth) * pixelsBlockWidth + j % pixelsBlockWidth;
                _networkInput[pixelBlockNumber][numberOfPixelInBlock] = (1.0 + _grayTable[i][j]) / 256;
            }
        }

        if (_isBipolar) {
            for (int i = 0; i < blocksCount; i++) {
                for (int j = 0; j < inputLength; j++) {
                    _networkInput[i][j] = _networkInput[i][j] * 2 - 1;
                }
            }
        }
    }

    private void parseImageToNetworkImage() throws Exception {
        this.parseImageToNetworkImage(INPUT_SIZE);
    }

    public void saveNetworkOutputAsImage(double[][] output, int outputBlockSize, String path) {

    }

    public double[][] getNetworkInput() {
        return _networkInput;
    }
}
