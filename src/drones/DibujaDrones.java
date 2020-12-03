/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Jorge
 */
public class DibujaDrones extends JPanel{
    private MiX x;
    private MiY y;
    private NumDrones n;
    private BufferedImage ima;
    private ArrayList<Dron> drones;
    private int direccion;
    private int metodo;
    
    public DibujaDrones(MiX x, MiY y, NumDrones n, BufferedImage ima, int direccion, int metodo){
        this.ima= ima;
        this.x = x;
        this.y = y;
        this.n = n;
        drones = new ArrayList<>();
        this.direccion = direccion;
        this.metodo = metodo;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(ima, 0, 0, this);
        g2.setColor(Color.black);
        pintaLimites(g2);
        for (int i = 0; i < drones.size(); i++) {
            g2.fill(new Ellipse2D.Double(drones.get(i).getX().getX(), drones.get(i).getY().getY(), 15, 15));
        }
    }
    
    public void pintaLimites(Graphics2D g2){
        if(drones.size()==2){
            g2.fill(new Rectangle2D.Double(350, 0, 5, 400));
        }
        if(drones.size()==4){
            g2.fill(new Rectangle2D.Double(350, 0, 5, 400));
            g2.fill(new Rectangle2D.Double(0, 200,700, 5));
        }
        if(drones.size()==6){
            g2.fill(new Rectangle2D.Double(350, 0, 5, 400));
            g2.fill(new Rectangle2D.Double(0, 133,700, 5));
            g2.fill(new Rectangle2D.Double(0, 266,700, 5));
        }
        if(drones.size()==8){
            g2.fill(new Rectangle2D.Double(350, 0, 5, 400));
            g2.fill(new Rectangle2D.Double(0, 100,700, 5));
            g2.fill(new Rectangle2D.Double(0, 200,700, 5));
            g2.fill(new Rectangle2D.Double(0, 300,700, 5));
        }
        if(drones.size()==10){
            g2.fill(new Rectangle2D.Double(350, 0, 5, 400));
            g2.fill(new Rectangle2D.Double(0, 80,700, 5));
            g2.fill(new Rectangle2D.Double(0, 160,700, 5));
            g2.fill(new Rectangle2D.Double(0, 240,700, 5));
            g2.fill(new Rectangle2D.Double(0, 320,700, 5));
        }
    }

    public ArrayList<Dron> getDrones() {
        return drones;
    }

    public void setDrones(ArrayList<Dron> drones) {
        this.drones = drones;
    }

    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public int getMetodo() {
        return metodo;
    }

    public void setMetodo(int metodo) {
        this.metodo = metodo;
    }

}
