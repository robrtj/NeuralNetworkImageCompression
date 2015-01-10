package pl.edu.pw.mini.nn.neat;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2015-01-04.
 */
public class InnovationNumberTest {

    @Test
    public void getNextInnovationNumber() throws Exception {
        InnovationIdGenerator one = new InnovationIdGenerator();
        InnovationIdGenerator two = new InnovationIdGenerator();
        InnovationIdGenerator three = new InnovationIdGenerator();

        int count = 1000;
        for (int i = 0; i < count; i++) {
            one.generate();
            two.generate();
            three.generate();
        }
        assertEquals(3 * count, one.generate());
    }
}
