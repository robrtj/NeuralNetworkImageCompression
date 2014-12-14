package pl.edu.pw.mini.nn.neat;

import org.encog.util.identity.BasicGenerateID;
import org.encog.util.identity.GenerateID;

/**
 * Created by pawel.bielicki on 2014-12-13.
 *
 * This class is container for compress image network,
 * it contains two subnetworks, one for compression, second for decompression
 */
public class NeatCompressNetwork {
    private NeatPopulation compressPopulation;
    private NeatPopulation decompressPopulation;
    private int inputCount;
    private int outputCount;

    public NeatCompressNetwork(int inputCount, int outputCount) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;

        compressPopulation = new NeatPopulation(this);
        decompressPopulation = new NeatPopulation(this);
    }

    public void iteration(){
        //TODO
    }
}
