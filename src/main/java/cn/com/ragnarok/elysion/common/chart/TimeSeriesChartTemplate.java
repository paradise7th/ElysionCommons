package cn.com.ragnarok.elysion.common.chart;

import java.awt.Paint;
import java.awt.Shape;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


public class TimeSeriesChartTemplate implements ChartTemplate {

  private TimeSeriesCollection tscollection;
  
  private boolean showLine=true;
  private boolean showShape=false;
  private boolean showLabel=false;
  private String dateFormat;
  private Range valueRange;
  private Paint[] linePaint;
  private Shape[] shapePaint;



  public TimeSeriesChartTemplate() {
	  tscollection=new TimeSeriesCollection();
  }



  public void clearData(int index){
    tscollection.getSeries(index).clear();
  }
  
  public void clearData(String name){
	  TimeSeries ts= tscollection.getSeries(name);
	  if(ts!=null){
		ts.clear();  
	  }
  }

  public void setData(TimeSeriesCollection tsc){
    tscollection=tsc;
  }

  public TimeSeries initData(String name){
	 TimeSeries ts= tscollection.getSeries(name);
	 if(ts==null){
		 ts=new TimeSeries(name);
		 tscollection.addSeries(ts);
	 }
	 return ts;
  }

  public void addData(TimeSeries ts, Date time,double value){
    addData(ts,new Second(time),value);
  }

  public void addData(TimeSeries ts,RegularTimePeriod time,double value){
    if(ts!=null){
      ts.addOrUpdate(time,value);
    }
  }

  public JFreeChart buildChart(String title,String x,String y,boolean legend){
    JFreeChart chart=ChartFactory.createTimeSeriesChart(title,x,y,tscollection,legend,false,false);
    
    XYPlot plot= chart.getXYPlot();
    XYItemRenderer render = plot.getRenderer();
    if (render instanceof XYLineAndShapeRenderer)
    {
      XYLineAndShapeRenderer xyrender = (XYLineAndShapeRenderer)render;
      xyrender.setBaseShapesVisible(showShape);
      xyrender.setBaseLinesVisible(showLine);
      if(showLabel){
        render.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        render.setBaseItemLabelsVisible(true);
      }

      if(linePaint!=null && linePaint.length>0){
    	for (int i = 0; i < linePaint.length; i++) {
    		if(linePaint[i]!=null){
    			xyrender.setSeriesPaint(i, linePaint[i]);			    			
    			xyrender.setSeriesFillPaint(i, linePaint[i]);
    		}
		}
      }
      if(shapePaint!=null && shapePaint.length>0){
    	 for (int i = 0; i < shapePaint.length; i++) {
    		 if(shapePaint[i]!=null){
    			 xyrender.setSeriesShape(i, shapePaint[i]);			    			 
    		 }
 		}
        
      }

    }

    DateAxis dateAxis = (DateAxis)plot.getDomainAxis();
    if(dateFormat!=null){
      dateAxis.setDateFormatOverride(new SimpleDateFormat(dateFormat));
    }
    dateAxis.setUpperMargin(0.10);
    //dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.YEAR,1));
    NumberAxis numberAxis=(NumberAxis)plot.getRangeAxis();
    numberAxis.setAutoRangeIncludesZero(true);
    if(valueRange!=null){
      numberAxis.setAutoRange(false);
      numberAxis.setRange(valueRange);
    }
    numberAxis.setUpperMargin(0.10);
    

    return chart;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public void setLinePaint(Paint... linePaint) {
    this.linePaint = linePaint;
  }

  public void setShapePaint(Shape... shapePaint) {
    this.shapePaint = shapePaint;
  }

  public void setShowLine(boolean showLine) {
    this.showLine = showLine;
  }

  public void setShowShape(boolean showShape) {
    this.showShape = showShape;
  }

  public void setShowLabel(boolean showLabel) {
    this.showLabel = showLabel;
  }

  public void setRange(double min,double max){
    this.valueRange=new Range(min,max);
  }

}
