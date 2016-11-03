package com.compremelhor.web.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.service.PurchaseService;
import com.compremelhor.web.util.JSFUtil;


@ManagedBean
@ViewScoped
public class HomeController {
	private DashboardModel model;
	
	private BarChartModel chartModel;
	private PieChartModel pieChartModel;
	
	private BarChartModel purchasesByMonthChart;
	private BarChartModel totalValueByMonthChart;
	
	private List<Purchase> list;
	
	@Inject
	private PurchaseService purchaseService;
	
	public HomeController() {
		model = new DefaultDashboardModel();
		
		DashboardColumn column1 = new DefaultDashboardColumn();
		DashboardColumn column2 = new DefaultDashboardColumn();

		column1.addWidget("quantityByMonth");
		column2.addWidget("purchaseValueByMonth");
		
		column1.addWidget("quantityByStatus");
		column2.addWidget("purchaseValueByStatus");
		

		
		model.addColumn(column1);
		model.addColumn(column2);
	}
	
	public void onStartPage() {
		HashMap<String, Object> params = new HashMap<>();
		params.put("freight.freightType.partner.id", JSFUtil.getLoggedUser().getPartner().getId());
	
		try {
			list = purchaseService.findAll(params);
		} catch (UnknownAttributeException e) {
			e.printStackTrace();
		}
		
		setupLineChart(list);
		setupChart(list);
	}
	
	
	private void setupLineChart(List<Purchase> list) {
		purchasesByMonthChart = new BarChartModel();
		totalValueByMonthChart = new BarChartModel();
		
		Map<Integer, List<Purchase>> purchasesByMonth = list.stream().collect(
				Collectors.groupingBy((Purchase p) -> {
					LocalDateTime d = p.getDateCreated();
					return Integer.valueOf(String.format("%d%d",d.getYear(),d.getMonth().getValue()));
				}));
		
		purchasesByMonth = new TreeMap<>(purchasesByMonth);
		
		final TreeMap<Integer, Number> sumByMonth = new TreeMap<>();
		final LineChartSeries qtdeByMonthSeries = new LineChartSeries();
		purchasesByMonth.forEach((k, purchaseList) -> {
			purchaseList.forEach( p -> {
				sumByMonth.compute(k, (key, value) -> {
					if (value == null) {
						return (Number) p.getTotalValue();
					}
					return (Number) (value.doubleValue() + p.getTotalValue());
				});
			});
			qtdeByMonthSeries.set(String.format("%s/%s", String.valueOf(k).substring(4), String.valueOf(k).substring(0,4)), (Number)  purchaseList.size());
		});
		
		final LineChartSeries valueByMonthSeries = new LineChartSeries();
		sumByMonth.forEach((k, v) -> valueByMonthSeries.set(String.format("%s/%s", String.valueOf(k).substring(4), String.valueOf(k).substring(0,4)), v));
		qtdeByMonthSeries.setLabel("Qtde Compras");
		valueByMonthSeries.setLabel("Valor Compras");
		
		purchasesByMonthChart.setAnimate(true);
		purchasesByMonthChart.addSeries(qtdeByMonthSeries);
		purchasesByMonthChart.setDatatipFormat("%d: %d");
		Axis x = purchasesByMonthChart.getAxis(AxisType.X);
		x.setLabel("Mês");
		
		Axis y = purchasesByMonthChart.getAxis(AxisType.Y);
		y.setLabel("Quantidade");
		
		totalValueByMonthChart.setAnimate(true);
		totalValueByMonthChart.addSeries(valueByMonthSeries);
		totalValueByMonthChart.setDatatipFormat("%d: R$ %.2f");
		Axis x2 = totalValueByMonthChart.getAxis(AxisType.X);
		x2.setLabel("Mês");
		
		Axis y2 = totalValueByMonthChart.getAxis(AxisType.Y);
		y2.setLabel("R$ Valor");
		
	}
	
	
	private void setupChart(List<Purchase> list) {
		chartModel = new BarChartModel();
		pieChartModel = new PieChartModel();
		
		
		Map<Object, List<Purchase>> purchasesByStatus = new HashMap<>();
		purchasesByStatus =
				list
				.stream()
				.collect(Collectors.groupingBy(p -> p.getStatus()));

		
		Map<String, Number> valueByStatus = new HashMap<>();
		Map<String, Number> qtdeByStatus = new HashMap<String, Number>();
		
		
		for (Map.Entry<Object, List<Purchase>> pair : purchasesByStatus.entrySet()) {
			Double sum = 0.0;
			for (Purchase p : pair.getValue()) {
				sum += p.getTotalValue();
			}
			valueByStatus.put(((Purchase.Status)pair.getKey()).getStatus(), (Number) sum);
			qtdeByStatus.put(((Purchase.Status)pair.getKey()).getStatus(), pair.getValue().size());
		}
		
		ChartSeries totalValue = new ChartSeries();
		
		for (Map.Entry<String, Number> pair : valueByStatus.entrySet()) {
			totalValue.set(pair.getKey(), pair.getValue());
			
		}
		
//		chartModel.setTitle("Valor total de Vendas Por Status");
		chartModel.setAnimate(true);
		
		
        Axis xAxis = chartModel.getAxis(AxisType.X);
        xAxis.setLabel("Status Compra");
         
        Axis yAxis = chartModel.getAxis(AxisType.Y);
        yAxis.setLabel("R$ Valor");
        
		chartModel.addSeries(totalValue);
		chartModel.setDatatipFormat("%d: R$ %.2f");
		
		pieChartModel.setData(qtdeByStatus);
		pieChartModel.setFill(false);
		pieChartModel.setLegendPosition("ne");
		
//        chartModel.setFill(false);
        pieChartModel.setShowDataLabels(true);
        
	}
	
	public DashboardModel getModel() {
		return model;
	}

	public void setModel(DashboardModel model) {
		this.model = model;
	}

	public BarChartModel getChartModel() {
		return chartModel;
	}

	public void setChartModel(BarChartModel chartModel) {
		this.chartModel = chartModel;
	}

	public PieChartModel getPieChartModel() {
		return pieChartModel;
	}

	public void setPieChartModel(PieChartModel pieChartModel) {
		this.pieChartModel = pieChartModel;
	}

	public BarChartModel getPurchasesByMonthChart() {
		return purchasesByMonthChart;
	}

	public void setPurchasesByMonthChart(BarChartModel purchasesByMonthChart) {
		this.purchasesByMonthChart = purchasesByMonthChart;
	}

	public BarChartModel getTotalValueByMonthChart() {
		return totalValueByMonthChart;
	}

	public void setTotalValueByMonthChart(BarChartModel totalValueByMonthChart) {
		this.totalValueByMonthChart = totalValueByMonthChart;
	}

}
