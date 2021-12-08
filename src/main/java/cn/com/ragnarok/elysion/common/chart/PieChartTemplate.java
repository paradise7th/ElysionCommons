package cn.com.ragnarok.elysion.common.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

public class PieChartTemplate implements ChartTemplate{
  protected DefaultPieDataset pds;
  protected boolean circular=true;
  protected Rotation rotation=Rotation.CLOCKWISE;
  protected boolean ignoreNull=true;
  protected boolean ignoreZero=false;
  protected boolean showLink=true;
  protected double angle;
  protected String labelFormat;
  protected String legendFormat;

  public PieChartTemplate() {
  }

  public void setClockWiseRotation(boolean b){
    this.rotation=b?Rotation.CLOCKWISE:Rotation.ANTICLOCKWISE;
  }

  public void initData(){
    if(pds==null){
      pds=new DefaultPieDataset();
    }
  }

  public void addData(String key,double value){
    if(pds!=null){
      pds.setValue(key,value);
    }
  }

  public void clearData(){
    if(pds!=null){
      pds.clear();
    }
  }

  public JFreeChart buildChart(String tablename, String xname, String yname,
                               boolean showlegend) {
    JFreeChart chart=ChartFactory.createPieChart(tablename,pds,showlegend,false,false);
    PiePlot plot = (PiePlot)chart.getPlot();
    plot.setCircular(circular);
    plot.setDirection(rotation);
    plot.setIgnoreNullValues(ignoreNull);
    plot.setIgnoreZeroValues(ignoreZero);
//    plot.setSectionPaint("test",Color.red);
//    plot.setExplodePercent("test",0.1);
    plot.setLabelLinksVisible(showLink);
    plot.setStartAngle(angle);
    if(labelFormat!=null){
      plot.setLabelGenerator(new StandardPieSectionLabelGenerator(labelFormat));
    }
    if(legendFormat!=null){
      plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
          legendFormat));
    }
//    plot.setInteriorGap(0D);
//    plot.setLabelGap(0.1);
    return chart;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }

  public void setCircular(boolean circular) {
    this.circular = circular;
  }

  public void setIgnoreNull(boolean ignoreNull) {
    this.ignoreNull = ignoreNull;
  }

  public void setIgnoreZero(boolean ignoreZero) {
    this.ignoreZero = ignoreZero;
  }

  public void setLabelFormat(String labelFormat) {
    this.labelFormat = labelFormat;
  }

  public void setShowLink(boolean showLink) {
    this.showLink = showLink;
  }

  public void setLegendFormat(String legendFormat) {
    this.legendFormat = legendFormat;
  }
}
