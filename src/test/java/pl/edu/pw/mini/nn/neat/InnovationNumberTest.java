package pl.edu.pw.mini.nn.neat;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2015-01-04.
 */
public class InnovationNumberTest {

    @Test
    public void getNextInnovationNumber() throws Exception {
        InnovationNumber one = new InnovationNumber();
        InnovationNumber two = new InnovationNumber();
        InnovationNumber three = new InnovationNumber();

        int count = 1000;
        for (int i = 0; i < count; i++) {
            one.getNextInnovationNumber();
            two.getNextInnovationNumber();
            three.getNextInnovationNumber();
        }
        assertEquals(3* count, one.getNextInnovationNumber());
    }
}
