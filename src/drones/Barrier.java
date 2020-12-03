/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones;

/**
 *
 * @author Jorge
 */
public class Barrier {
    private int maxNumThreads;
    private int numCalls=0;
    
    public Barrier(int numThreads){
        this.maxNumThreads = numThreads;
    }
    
    public synchronized void call(){
        numCalls++;
        if (numCalls == maxNumThreads) {
            this.notifyAll();
            numCalls = 0;
        }else{
            try{
                this.wait();
            }catch(InterruptedException ignore){}
        }
    }
}
