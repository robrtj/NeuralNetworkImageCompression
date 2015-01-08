package pl.edu.pw.mini.nn.image;

import org.junit.Test;

import static org.junit.Assert.*;

public class GrayImageParserTest {

    @Test
    public void testGetNetworkInputBlack8x8() throws Exception {
        GrayImageParser tester = new GrayImageParser("black8x8.png", false);
        double[][] output;
        output = new double[1][64];
        for (int j = 0; j < 64; j++) {
            output[0][j] = 1.0/256;
        }
        assertArrayEquals(output, tester.getNetworkInput());
    }

    @Test
    public void testGetNetworkInputLenaBlockCount() throws Exception {
        GrayImageParser tester = new GrayImageParser("lena_1.png", false);
        assertEquals(1024, tester.getNetworkInput().length);
    }
}
