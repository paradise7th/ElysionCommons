package cn.com.ragnarok.elysion.common.chart;

import org.jfree.chart.JFreeChart;

public interface ChartTemplate {
  JFreeChart buildChart(String tablename,String xname,String yname,boolean showlegend);
}
