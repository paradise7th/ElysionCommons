package cn.com.ragnarok.elysion.common.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
/**
 * 3D柱状图模板
 * @author Elysion
 *
 */
public class BarChart3DTemplate extends BarChartTemplate {



  public BarChart3DTemplate() {
  }



 public JFreeChart buildChart(String tablename, String xname, String yname,
                              boolean showlegend) {
   JFreeChart chart=ChartFactory.createBarChart3D(tablename,xname,yname,cData,orientation,showlegend,false,false);
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


 }

