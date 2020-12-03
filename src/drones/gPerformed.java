package drones;

import java.util.ArrayList;
 import javax.swing.*;
 import org.jfree.chart.ChartFactory;
 import org.jfree.chart.ChartPanel;
 import org.jfree.chart.JFreeChart;
 import org.jfree.chart.plot.PlotOrientation;
 import org.jfree.data.xy.XYSeries;
 import org.jfree.data.xy.XYSeriesCollection;

public class gPerformed extends JPanel{
    XYSeries series = new XYSeries("Grafica1");
    int i= 0;
    private ChartPanel panel;
    private XYSeriesCollection dataset;

    public gPerformed(){
        dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Rendimiento de los Drones","Iteracciones->","No. Dron",
            dataset,PlotOrientation.VERTICAL,false,false,false);

        panel = new ChartPanel(chart);
        setLayout(new java.awt.BorderLayout());
        add(panel);
        validate();
    }
    public void actualizar(int nHilo){
        try{
            System.out.println("Numero de iteracion en G: "+i);
            series.add(i, nHilo);
            if(i>150){
                panel.getChart().getXYPlot().getDomainAxis().setRange(i-150, i);
            }
            panel.repaint();
            i++;
        }catch(Exception x){
            System.out.println("Error:");
        }
        
    }

    public void limpiar(){
        dataset.removeAllSeries();
        panel.repaint();
    }

}