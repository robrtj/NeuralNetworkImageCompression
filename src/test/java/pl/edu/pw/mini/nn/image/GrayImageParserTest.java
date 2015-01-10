package pl.edu.pw.mini.nn.image;

import org.junit.Test;

import static org.junit.Assert.*;

public class GrayImageParserTest {

    @Test
    public void testGetNetworkInputBlack8x8() throws Exception {
        GrayImageParser tester = new GrayImageParser("black8x8.png", 64, false);
        double[][] output;
        output = new double[1][64];
        for (int j = 0; j < 64; j++) {
            output[0][j] = 1.0 / 256;
        }
        assertArrayEquals(output, tester.getNetworkInput());

        tester.saveNetworkOutputAsImage(output, "out_test.png");
    }

    @Test
    public void testGetNetworkInputLenaBlockCount() throws Exception {
        GrayImageParser tester = new GrayImageParser("lena_1.png", 64, false);
        assertEquals(1024, tester.getNetworkInput().length);
    }
}
