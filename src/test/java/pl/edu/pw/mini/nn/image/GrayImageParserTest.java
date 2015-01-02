package pl.edu.pw.mini.nn.image;

import org.junit.Test;

import static org.junit.Assert.*;

public class GrayImageParserTest {

    @Test
    public void testGetNetworkInput() throws Exception {
        GrayImageParser tester = new GrayImageParser("black8x8.png", false);
        double[][] output;
        output = new double[1][64];
        for (int j = 0; j < 64; j++) {
            output[0][j] = 0;
        }
        assertEquals("check black image", output, tester.getNetworkInput());
    }
}
