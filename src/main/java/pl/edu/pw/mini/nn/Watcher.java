package pl.edu.pw.mini.nn;

import pl.edu.pw.mini.nn.neat.NeatPopulation;

import java.util.Scanner;

/**
 * Created by Pawel on 2015-01-14.
 */
public class Watcher implements Runnable {
    public NeatPopulation net;

    public Watcher(NeatPopulation net) {
        this.net = net;
    }

    @Override
    public void run() {
        while(true) {
            Scanner keyboard = new Scanner(System.in);
            if(keyboard.hasNextInt()) {
                net.stopComputing();
            }
        }
    }
}
