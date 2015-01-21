package pl.edu.pw.mini.nn.image;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

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

    private double _networkInput[][];
    private int _width;
    private int _height;
    private boolean _isBipolar = true;
    private int _pixelsCount;
    private int _inputLength;
    private int _blocksCount;
    private int _pixelsBlockWidth;
    private int _widthDivBlockWidth;

    public GrayImageParser(String path, int inputLength, boolean print) {
        File input = new File(path);
        try {
            _image = ImageIO.read(input);
            _width = _image.getWidth();
            _height = _image.getHeight();
            _pixelsCount = _width * _height;
            _inputLength = inputLength;
            if (_pixelsCount % _inputLength != 0) {
                throw new Exception("Image is not divisible by input length " + _inputLength);
            }
            _blocksCount = _pixelsCount / _inputLength;
            _networkInput = new double[_blocksCount][_inputLength];
            _pixelsBlockWidth = (int) Math.sqrt(_inputLength);
            _widthDivBlockWidth = _width / _pixelsBlockWidth;

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

    public GrayImageParser(String path, int inputLength, boolean print, boolean isBipolar) {
        this(path, inputLength, print);
        _isBipolar = isBipolar;
    }

    private void parseImageToNetworkImage() throws Exception {

        for (int i = 0; i < _width; ++i) {
            for (int j = 0; j < _height; ++j) {
                //+1 because don't want to have 0
                int pixelBlockNumber = (i / _pixelsBlockWidth) * _widthDivBlockWidth + j / _pixelsBlockWidth;
                int numberOfPixelInBlock = (i % _pixelsBlockWidth) * _pixelsBlockWidth + j % _pixelsBlockWidth;
                _networkInput[pixelBlockNumber][numberOfPixelInBlock] = (1.0 + _grayTable[i][j]) / 256;
            }
        }

        if (_isBipolar) {
            for (int i = 0; i < _blocksCount; i++) {
                for (int j = 0; j < _inputLength; j++) {
                    _networkInput[i][j] = _networkInput[i][j] * 2 - 1;
                }
            }
        }
    }

    public void saveNetworkOutputAsImage(double[][] output, String path) {
        BufferedImage image = new BufferedImage(_width, _height, _image.getType());

        if (_isBipolar) {
            for (int i = 0; i < _blocksCount; i++) {
                for (int j = 0; j < _inputLength; j++) {
                    output[i][j] = (output[i][j] + 1) / 2;
                }
            }
        }

        int color;
        for (int i = 0; i < _width; i++) {
            for (int j = 0; j < _height; j++) {
                int pixelBlockNumber = (i / _pixelsBlockWidth) * _widthDivBlockWidth + j / _pixelsBlockWidth;
                int numberOfPixelInBlock = (i % _pixelsBlockWidth) * _pixelsBlockWidth + j % _pixelsBlockWidth;
                color = (int) (output[pixelBlockNumber][numberOfPixelInBlock] * 256 - 1);
                image.setRGB(i, j, new Color(color, color, color).getRGB());
            }
        }

        saveImageToFile(image, path);
    }

    private void saveImageToFile(BufferedImage image, String path) {
        File outputFile = new File(path);
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[][] getNetworkInput() {
        return _networkInput;
    }
}
