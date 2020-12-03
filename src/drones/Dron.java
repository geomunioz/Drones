/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Jorge
 */
public class Dron extends Thread{
    private DibujaDrones panel;
    private gPerformed panelG;
    private MiX x;
    private MiY y;
    private int v=10;
    private int top;
    private int bottom;
    private int lef;
    private int right;
    private double[] datosX;
    private double[] datosY;
    int direccion; //direccion de movimiento*/
    private int numero;
    private int totalDrones;
    private int metodo;
    private Semaphore semaforo;
    private Lock mutex;
    private Lock mutexVC;
    private Condition condicion;
    private boolean bandera;
    private Barrier barrera;
    int iter = 0;
            
    Dron(DibujaDrones panel, gPerformed panelG,MiX x, MiY y, int a, Lock mutex, Semaphore semaforo, Barrier barrera, Lock mutexVC, Condition condicion, boolean bandera){ 
        this.panel = panel;
        this.panelG = panelG;
        this.x = x;
        this.y = y;
        this.datosX = datosX;
        this.datosY = datosY;
        this.numero = a;
        /*direccion = panel.getDireccion();*/
        this.totalDrones = 0;
        this.semaforo = semaforo;
        this.mutex = mutex;
        this.barrera = barrera;
        this.mutexVC = mutexVC;
        this.condicion = condicion;
        this.bandera = bandera;
    }
    
    public void run(){
        while(true){ 
            direccion = panel.getDireccion();
            metodo = panel.getMetodo();
            System.out.println("Metodo: "+metodo);
            switch(metodo){
                case 0:
                    sinMetodo();
                    break;
                case 1:
                    sincroMutex();
                    break;
                case 2:
                    sincroSemaforo();
                    break;
                case 3:
                    sincroMonitores();
                    break;
                case 4:
                    sincroVCondicion();
                    break;
                case 5:
                    sincroBarrera();
                    break;
                default:
                    System.out.println("Error");
                    break;
            }
            
        }
    }
    
    public void sincroMutex(){
        try{
            if (mutex.tryLock()) {
                try{
                    verificarLimites();
                    panelG.actualizar(numero);
                    panel.repaint();                
                    sleep(50+(int) Math.random()*200);
                }finally{
                    mutex.unlock();
                }
            } 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void sincroSemaforo(){
        try{
            if(semaforo.tryAcquire()){
                verificarLimites();
                panelG.actualizar(numero);
                panel.repaint();                
                sleep(100+(int) Math.random()*200);
                semaforo.release();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        iter++;
    }
    
    public void sincroMonitores(){
        synchronized(this){
            try{
                verificarLimites();
                synchronized(panelG){
                    panelG.actualizar(numero);
                }
                synchronized(panel){
                    panel.repaint(); 
                }        
                sleep(100+(int) Math.random()*200);
            }catch(Exception e){
                System.out.println("Error: ");
            }
            
        }
        iter++;
    }
    
    public void sincroVCondicion(){
        if(mutexVC.tryLock()){    //mutexVC.lock();
            try{
                while(bandera==false){
                    try{
                        System.out.println("wait");
                        condicion.await();
                    }catch(InterruptedException e){ }
                }
                bandera=false;
                verificarLimites();
                panelG.actualizar(numero);
                panel.repaint();                
                try{
                    sleep(100+(int) Math.random()*200);
                }catch(Exception e){}
                condicion.signalAll();
            }finally{
                mutexVC.unlock();
                bandera=true;
            }
        }
        iter++;
    }
    
    public void sincroBarrera(){
        try{
            barrera.call();
            verificarLimites();
            panelG.actualizar(numero);
            panel.repaint();                
            sleep(100+(int) Math.random()*200);    
        }catch(Exception e){
            e.printStackTrace();
        }
        iter++;
    }
    
    public void sinMetodo(){
        try{
            verificarLimites();
            panel.repaint();                
            sleep(100+(int) Math.random()*200);    
        }catch(Exception e){
            e.printStackTrace();
        }
        iter++;
    }
    
    public void verificarLimites(){
        if(getY().getY() <= top){
            int i = (int) (Math.random()*3)+1; 
            direccion = i == 1 ? 6 : i == 2 ? 0 : 4; 
            movimiento();                                                    // dentro de la funcion dependiendo el movimiento se realizara la suma o resta corespondiente.
        }else if(getX().getX() <= lef){
            int i = (int) (Math.random()*3)+1;
            direccion = i == 1 ? 7 : i == 2 ? 2 : 4;
            movimiento();
        }else if(getY().getY() >= bottom-15){                                   //panel.getHeight()
            int i = (int) (Math.random()*3)+1;
            direccion = i == 1 ? 5 : i == 2 ? 1 : 7;
            movimiento();
        }else if(getX().getX() >= right-15){                                     /*&& numero%2==0*///panel.getX()
            int i = (int) (Math.random()*3)+1;
            direccion = i == 1 ? 6 : i == 2 ? 3 : 5;
            movimiento();
        }else{
            movimiento();
        }
    }
    
    public void movimiento(){
        switch(direccion){
            case 0:
                getY().setY(getY().getY()+v);                                   // en direccion a las hacia abajo
                break;
            case 1:
                getY().setY(getY().getY()-v);                                   //en direccion hacia arriba
                break;
            case 2:
                getX().setX(getX().getX()+v);                                    //en direccion a la derecha
                break;
            case 3:
                getX().setX(getX().getX()-v);                                   //en direccion a la izquierda
                break;
            case 4:
                getX().setX(getX().getX()+v);                                    //en diagonal de la esquina superior izquierda a la esquina inferior derecha 
                getY().setY(getY().getY()+v);
                break;
            case 5:
                getX().setX(getX().getX()-v);                                    //en diagonal de la esquina inferior derecha a la esquina superior izquierda 
                getY().setY(getY().getY()-v);
                break;
            case 6:
                getX().setX(getX().getX()-v);                                   //en diagonal de la esquina superior derecha a la esquina inferior izquierda
                getY().setY(getY().getY()+v);
                break;
            case 7:
                getX().setX(getX().getX()+v);                                   //en diagonal de la esquina inferior izquierda a la esquina superior derecha 
                getY().setY(getY().getY()-v);
                break;
            default:
                System.out.println("Error");
                break;
        }
        panel.setDireccion(direccion);
    }
    
    public void guardarXY(int i){
        datosX[i]=getX().getX();
        datosY[i]=getY().getY();
    }

    /**
     * @return the datosX
     */
    public double[] getDatosX() {
        return datosX;
    }

    /**
     * @param datosX the datosX to set
     */
    public void setDatosX(double[] datosX) {
        this.datosX = datosX;
    }

    /**
     * @return the datosY
     */
    public double[] getDatosY() {
        return datosY;
    }

    /**
     * @param datosY the datosY to set
     */
    public void setDatosY(double[] datosY) {
        this.datosY = datosY;
    }

    /**
     * @return the x
     */
    public MiX getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(MiX x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public MiY getY() {
        return y;
    }
    
    public void setY(MiY y) {
        this.y = y;
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * @return the totalDrones
     */
    public int getTotalDrones() {
        return totalDrones;
    }

    /**
     * @param totalDrones the totalDrones to set
     */
    public void setTotalDrones(int totalDrones) {
        this.totalDrones = totalDrones;
    }

    /**
     * @return the top
     */
    public int getTop() {
        return top;
    }

    /**
     * @param top the top to set
     */
    public void setTop(int top) {
        this.top = top;
    }

    /**
     * @return the bottom
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * @param bottom the bottom to set
     */
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    /**
     * @return the lef
     */
    public int getLef() {
        return lef;
    }

    /**
     * @param lef the lef to set
     */
    public void setLef(int lef) {
        this.lef = lef;
    }

    /**
     * @return the right
     */
    public int getRight() {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(int right) {
        this.right = right;
    }

}
