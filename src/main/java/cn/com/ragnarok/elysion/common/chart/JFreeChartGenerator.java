
package cn.com.ragnarok.elysion.common.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.TimeSeries;
import org.jfree.ui.RectangleInsets;

public class JFreeChartGenerator {
  private final static org.apache.log4j.Logger log = org.apache.log4j.
      LogManager.getLogger(JFreeChartGenerator.class);
  private ChartTemplate template;
  private Font defaultFont = new Font("宋体", Font.PLAIN, 12);
  private Paint backgroundPaint;
  private Paint plotPaint;
  private Paint gridPaint;
  private boolean showLegend=true;
  private boolean showGrid=true;
  private String[] subTitles;
  private String title="";
  private String domainLabel="";
  private String rangeLabel="";
  private Font titleFont;
  private Font subTitleFont;
  private String noDataMessage;



  public JFreeChartGenerator() {
  }

  public void setTemplate(ChartTemplate template) {
    this.template = template;
  }

  public void setBackgroundPaint(Paint backgroundPaint) {
    this.backgroundPaint = backgroundPaint;
  }

  public void setDomainLabel(String domainLabel) {
    this.domainLabel = domainLabel;
  }

  public void setGridPaint(Paint gridPaint) {
    this.gridPaint = gridPaint;
  }

  public void setPlotPaint(Paint plotPaint) {
    this.plotPaint = plotPaint;
  }

  public void setRangeLabel(String rangeLabel) {
    this.rangeLabel = rangeLabel;
  }

  public void setShowGrid(boolean showGrid) {
    this.showGrid = showGrid;
  }

  public void setShowLegend(boolean showLegend) {
    this.showLegend = showLegend;
  }

  public void setSubTitleFont(Font subTitleFont) {
    this.subTitleFont = subTitleFont;
  }

  public void setSubTitles(String[] subTitles) {
    this.subTitles = subTitles;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setTitleFont(Font titleFont) {
    this.titleFont = titleFont;
  }

  public void setNoDataMessage(String noDataMessage) {
    this.noDataMessage = noDataMessage;
  }

  public JFreeChart generateChart(){
    JFreeChart chart=template.buildChart(title,domainLabel,rangeLabel,showLegend);

    repairFont(chart); //修正乱码问题
    
    if(subTitles!=null && subTitles.length>0){
      for (int i=0;i<subTitles.length;i++ ) {
        TextTitle t=new TextTitle(subTitles[i]);
        if(subTitleFont!=null){
          t.setFont(subTitleFont);
        }
        chart.addSubtitle(t);
      }
    }
    if(titleFont!=null){
      chart.getTitle().setFont(titleFont);
    }
    if(backgroundPaint!=null){
      chart.setBackgroundPaint(backgroundPaint);
    }


    Plot plot=chart.getPlot();
    plot.setInsets(new RectangleInsets(10, 10, 10, 20));
    if(noDataMessage!=null){
      plot.setNoDataMessage(noDataMessage);
    }
    if(plotPaint!=null){
      plot.setBackgroundPaint(plotPaint);
    }
    if(plot instanceof XYPlot){
      ((XYPlot)plot).setRangeGridlinesVisible(showGrid);
      ((XYPlot)plot).setDomainGridlinesVisible(showGrid);
      if(gridPaint!=null){
        ((XYPlot)plot).setRangeGridlinePaint(gridPaint);
        ((XYPlot)plot).setDomainGridlinePaint(gridPaint);
      }
    }


    return chart;
  }

  private void repairFont(JFreeChart chart) {
	  setTitleFont(new Font("黑体",Font.PLAIN,20));
	  
	  LegendTitle legend = chart.getLegend();
	  
	  legend.setItemFont(new Font("宋体", Font.PLAIN, 12));
	  	Plot p=chart.getPlot();
	  	if(p instanceof CategoryPlot){
	  		CategoryPlot plot = chart.getCategoryPlot();// 获得图表区域对象
			CategoryAxis domainAxis = plot.getDomainAxis();
			ValueAxis rAxis = plot.getRangeAxis();
			

			/*------设置X轴坐标上的文字-----------*/
			domainAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 15));
			/*------设置X轴的标题文字------------*/
			domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 15));
			/*------设置Y轴坐标上的文字-----------*/
			rAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 15));
			/*------设置Y轴的标题文字------------*/
			rAxis.setLabelFont(new Font("宋体", Font.PLAIN, 15));

			
			
	  	}else if(p instanceof XYPlot){
	  		XYPlot xy=chart.getXYPlot();
	  		ValueAxis rAxis =xy.getRangeAxis();
	  		ValueAxis dAxis=xy.getDomainAxis();
	  		
	  		/*------设置X轴坐标上的文字-----------*/
	  		dAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 15));
			/*------设置X轴的标题文字------------*/
	  		dAxis.setLabelFont(new Font("宋体", Font.PLAIN, 15));
			/*------设置Y轴坐标上的文字-----------*/
			rAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 15));
			/*------设置Y轴的标题文字------------*/
			rAxis.setLabelFont(new Font("宋体", Font.PLAIN, 15));
	  		
	  	}
	  
		
	}

public void outputTestFrame() {
   JFreeChart chart=generateChart();


    BufferedImage img = chart.createBufferedImage(640, 480);
    final JFrame f = new JFrame("测试");
    final JLabel lbl = new JLabel(new ImageIcon(img));
    f.add(lbl);
//      f.setPreferredSize(new Dimension(800,600));
    f.setSize(800, 600);
    f.getContentPane().setBackground(Color.white);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);


  }

	public static void main(String[] args) {
		TimeSeriesChartTemplate temp=new TimeSeriesChartTemplate();
		TimeSeries tsout=temp.initData("流出");
		TimeSeries tsin=temp.initData("流入");
		Random r=new Random();
		for (int i = 0; i < 20; i++) {
			temp.addData(tsout, new Date(System.currentTimeMillis()+i*60*60*1000), r.nextInt(10000));
			
		}
		
		for (int i = 0; i < 20; i++) {
			temp.addData(tsin, new Date(System.currentTimeMillis()+i*60*60*1000), r.nextInt(10000));
			
		}
		temp.setLinePaint(new Color(23,255,23),new Color(23,23,255));
		
		
		
		JFreeChartGenerator gen=new JFreeChartGenerator();
		gen.setTitle("测试");
		gen.setTemplate(temp);
		
		
		gen.outputTestFrame();
	}

  public static void mainx(String[] args) {
//    TimeSeriesChartTemplate temp = new TimeSeriesChartTemplate();
//    temp.initData("line1");
//    temp.addData(new Year(1999),200);
//    temp.addData(new Year(2000),300);
//    temp.addData(new Year(2001),400);
//    temp.addData(new Year(2002),250);
//    temp.addData(new Year(2003),200);
    long now = new Date().getTime();
//    temp.addData(new Minute(new Date(now + 1000)), 100);
//    temp.addData(new Minute(new Date(now + 2000)), 200);
//    temp.addData(new Minute(new Date(now + 60000)), 6000);
//    temp.addData(new Minute(new Date(now + 60000)), 9000);
//    temp.addData(new Minute(new Date(now + 65000)), 6500);
//    temp.addData(new Minute(new Date(now + 122000)), 12200);
//    temp.addData(new Minute(new Date(now + 123000)), 12300);
//    temp.addData(new Minute(new Date(now + 124000)), 12400);

//    temp.setLinePaint(Color.BLUE);
//    temp.setDateFormat("yyyy年度");
//    temp.setShowShape(true);
//    temp.setShowLabel(true);
//    temp.setShapePaint(new java.awt.Rectangle(2,2));


//    BarChart3DTemplate temp=new BarChart3DTemplate();
//    temp.initData();
//    temp.addData("水果","苹果",2000);
//    temp.addData("水果","香蕉",1000);
//    temp.addData("水果","桃子",900);
//    temp.addData("游戏机","PSP",500);
//    temp.addData("游戏机","NDSL",600);
//    temp.addData("游戏机","PS3",400);
//    temp.addData("原料","煤炭",300);
//    temp.addData("原料","石油",200);
//    temp.addData("原料","钢铁",100);
//    temp.addData("天才",200);
//    temp.addData("人才",200);
//    temp.addData("蠢材",200);
//    temp.setHorizontal(false);
//    temp.setShowLabel(true);
////    temp.setItemMargin(0.1);
//    temp.setLabelFormat("{1}");
//    temp.setLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER,TextAnchor.CENTER));

    PieChartTemplate temp=new PieChartTemplate();
    temp.initData();
    temp.addData("中国",100);
    temp.addData("美国",120);
    temp.addData("日本",160);
    temp.addData("test",90);
    temp.setLabelFormat("国家:{0} 价值:{1} 百分比:{2}");
    temp.setLegendFormat("国家:{0} 价值:{1} 百分比:{2}");
    


    JFreeChartGenerator g = new JFreeChartGenerator();
    g.setTemplate(temp);

    g.setTitle("测试图片");
    g.setDomainLabel("X");
    g.setRangeLabel("Y");
    g.setSubTitles(new String[]{"这是测试","没有什么区别","第三行"});
    g.setBackgroundPaint(new Color(0xff,0xff,0xcc));
    g.setPlotPaint(new Color(0xcc,0xff,0xff));
    g.setTitleFont(new Font("黑体",Font.PLAIN,22));
    g.setNoDataMessage("选择区间内无数据");

    g.outputTestFrame();
  }

  public ChartTemplate getTemplate() {
    return template;
  }
}
