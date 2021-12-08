package cn.com.ragnarok.elysion.common.chart;

import java.text.MessageFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
/**
 * 柱状图模板
 * @author Elysion
 *
 */
public class BarChartTemplate implements ChartTemplate {
  protected DefaultCategoryDataset cData;
  protected PlotOrientation orientation=PlotOrientation.VERTICAL;
  protected boolean showLabel=false;
  protected String labelFormat;  //标签格式{0} 数值 {1}行名称 {2}列名称
  protected int categoryLabelDegree;
  protected boolean showOutline=true;
  protected double itemMargin=-1;
  protected ItemLabelPosition labelPosition;
  protected ItemLabelPosition nagativeLabelPosition;





  public BarChartTemplate() {
  }
/**
 * 设置方向
 * @param b true横向 false 纵向
 */
  public void setHorizontal(boolean b){
    orientation=b?PlotOrientation.HORIZONTAL:PlotOrientation.VERTICAL;
  }

  public void setCategoryLabelDegree(int degree){
    this.categoryLabelDegree=degree;
  }

  public void setShowLabel(boolean showLabel) {
    this.showLabel = showLabel;
  }

  public void setShowOutline(boolean showOutline) {
    this.showOutline = showOutline;
  }

  public void setLabelFormat(String labelFormat) {
    this.labelFormat = labelFormat;
  }

  public void setItemMargin(double itemMargin) {
    this.itemMargin = itemMargin;
  }

  public void setLabelPosition(ItemLabelPosition labelPosition) {
    this.labelPosition = labelPosition;
  }

  public void setNagativeLabelPosition(ItemLabelPosition nagativeLabelPosition) {
    this.nagativeLabelPosition = nagativeLabelPosition;
  }

  public void initData(){
    this.cData=new DefaultCategoryDataset();
  }

  public void setData(DefaultCategoryDataset cd){
    this.cData=cd;
  }

  public void addData(String rowName,String colName,double value){
    if(cData!=null ){
      cData.addValue(value,rowName,colName);
    }

  }

  public void addData(String name,double value){
    addData(name,"",value);
  }

  public void clearData(){
    cData.clear();
  }



  public JFreeChart buildChart(String tablename, String xname, String yname,
                               boolean showlegend) {
    JFreeChart chart=ChartFactory.createBarChart(tablename,xname,yname,cData,orientation,showlegend,false,false);
    CategoryPlot cplot=(CategoryPlot)chart.getPlot();

    CategoryAxis caxis=cplot.getDomainAxis();
    if(categoryLabelDegree>0){
      caxis.setCategoryLabelPositions(CategoryLabelPositions.
                                      createDownRotationLabelPositions( (categoryLabelDegree/90.0)*(Math.PI/2.0)));
    }else if(categoryLabelDegree<0){
      caxis.setCategoryLabelPositions(CategoryLabelPositions.
                                      createUpRotationLabelPositions( (-categoryLabelDegree/90.0)*(Math.PI/2.0)));
    }
    caxis.setUpperMargin(0.1);
    caxis.setCategoryMargin(0);

    ValueAxis vaxis=cplot.getRangeAxis();
    vaxis.setUpperMargin(0.1);

    BarRenderer render = (BarRenderer)cplot.getRenderer();
    if(showLabel){
      LabelGenerator lg=new LabelGenerator();
      if(labelFormat!=null){
        lg.setLabelFormat(labelFormat);
      }
      render.setBaseItemLabelGenerator(lg);
      render.setBaseItemLabelsVisible(true);
    }

    render.setDrawBarOutline(showOutline);
    if(itemMargin>=0){
      render.setItemMargin(itemMargin);
    }
    if(labelPosition!=null){
      render.setBasePositiveItemLabelPosition(labelPosition);
      render.setPositiveItemLabelPositionFallback(labelPosition);
    }
    if(nagativeLabelPosition!=null){
      render.setBaseNegativeItemLabelPosition(nagativeLabelPosition);
      render.setNegativeItemLabelPositionFallback(nagativeLabelPosition);
    }






    return chart;
  }

  static class LabelGenerator extends StandardCategoryItemLabelGenerator
  {
    String baseFormat="{0,number}";

    public LabelGenerator(){

    }

    public void setLabelFormat(String format){
      this.baseFormat=format;
    }

    public String generateLabel(CategoryDataset ds, int row, int col)
    {
      String rowkey=ds.getRowKey(row).toString();
      String colkey=ds.getColumnKey(col).toString();
      Number value=ds.getValue(row,col);
      String result=MessageFormat.format(baseFormat,value,rowkey,colkey);
      return result;
    }
  }

}
