package pl.edu.pw.mini.nn.neat;

/**
 * Created by pawel.bielicki on 2014-12-13.
 *
 * This class is container for compress image network,
 * it contains two subnetworks, one for compression, second for decompression
 */
public class NeatCompressNetwork {
    private oldNeatPopulation compressPopulation;
    private oldNeatPopulation decompressPopulation;
    private int inputCount;
    private int outputCount;

    public NeatCompressNetwork(int inputCount, int outputCount) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;

        compressPopulation = new oldNeatPopulation(this);
        decompressPopulation = new oldNeatPopulation(this);
    }

    public void iteration(){
        //TODO
    }
}
