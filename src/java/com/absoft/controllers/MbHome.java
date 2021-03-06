package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Empresa;
import com.absoft.entities.Pedido;
import com.absoft.entities.Produto;
import com.absoft.util.Mensagem;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbHome")
@RequestScoped
public class MbHome {

    private String dia1;
    private String dia2;
    private String dia3;
    private String dia4;
    private String dia5;

    SimpleDateFormat fData = new SimpleDateFormat("dd/MM/yyyy");

    private List<Produto> estoqueBaixo = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    private LineChartModel graficoFaturamento;

    public MbHome() {
    }

    @PostConstruct
    public void init() {
        createLineModels();
    }

    public List<Produto> getEstoqueBaixo() {
        return dao.listaCondicao(Produto.class, "avisaEstoqueBaixo = true AND estoque < estoqueMinimo");
    }

    public void setEstoqueBaixo(List<Produto> estoqueBaixo) {
        this.estoqueBaixo = estoqueBaixo;
    }

    public BigDecimal faturamentoHoje(Empresa emp) {
        String hoje = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        List<Pedido> listPed = dao.listaCondicao(Pedido.class, "dataPedido = '" + hoje + "' AND empresa.id = " + emp.getId());
        BigDecimal faturamento = BigDecimal.ZERO;

        for (Pedido pedido : listPed) {
            faturamento = faturamento.add(pedido.getTotal());
        }
        return faturamento;

    }

    public BigDecimal faturamento1(Empresa emp) {
        String hoje = new SimpleDateFormat("yyyy-MM-dd").format(removeDias(new Date(), 5));

        List<Pedido> listPed = dao.listaCondicao(Pedido.class, "dataPedido = '" + hoje + "' AND empresa.id = " + emp.getId());
        BigDecimal faturamento = BigDecimal.ZERO;

        for (Pedido pedido : listPed) {
            faturamento = faturamento.add(pedido.getTotal());
        }
        return faturamento;

    }

    public BigDecimal faturamento2(Empresa emp) {
        String hoje = new SimpleDateFormat("yyyy-MM-dd").format(removeDias(new Date(), 4));

        List<Pedido> listPed = dao.listaCondicao(Pedido.class, "dataPedido = '" + hoje + "' AND empresa.id = " + emp.getId());
        BigDecimal faturamento = BigDecimal.ZERO;

        for (Pedido pedido : listPed) {
            faturamento = faturamento.add(pedido.getTotal());
        }
        return faturamento;

    }

    public BigDecimal faturamento3(Empresa emp) {
        String hoje = new SimpleDateFormat("yyyy-MM-dd").format(removeDias(new Date(), 3));

        List<Pedido> listPed = dao.listaCondicao(Pedido.class, "dataPedido = '" + hoje + "' AND empresa.id = " + emp.getId());
        BigDecimal faturamento = BigDecimal.ZERO;

        for (Pedido pedido : listPed) {
            faturamento = faturamento.add(pedido.getTotal());
        }
        return faturamento;

    }

    public BigDecimal faturamento4(Empresa emp) {
        String hoje = new SimpleDateFormat("yyyy-MM-dd").format(removeDias(new Date(), 2));

        List<Pedido> listPed = dao.listaCondicao(Pedido.class, "dataPedido = '" + hoje + "' AND empresa.id = " + emp.getId());
        BigDecimal faturamento = BigDecimal.ZERO;

        for (Pedido pedido : listPed) {
            faturamento = faturamento.add(pedido.getTotal());
        }
        return faturamento;

    }

    public BigDecimal faturamento5(Empresa emp) {
        String hoje = new SimpleDateFormat("yyyy-MM-dd").format(removeDias(new Date(), 1));

        List<Pedido> listPed = dao.listaCondicao(Pedido.class, "dataPedido = '" + hoje + "' AND empresa.id = " + emp.getId());
        BigDecimal faturamento = BigDecimal.ZERO;

        for (Pedido pedido : listPed) {
            faturamento = faturamento.add(pedido.getTotal());
        }
        return faturamento;

    }

    private Date removeDias(Date date, Integer dias) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(Calendar.DATE, gc.get(Calendar.DATE) - dias);
        return gc.getTime();
    }

    public String getDia1() {
        dia1 = fData.format(removeDias(new Date(), 5));
        return dia1;
    }

    public String getDia2() {
        dia2 = fData.format(removeDias(new Date(), 4));
        return dia2;
    }

    public String getDia3() {
        dia3 = fData.format(removeDias(new Date(), 3));
        return dia3;
    }

    public String getDia4() {
        dia4 = fData.format(removeDias(new Date(), 2));
        return dia4;
    }

    public String getDia5() {
        dia5 = fData.format(removeDias(new Date(), 1));
        return dia5;
    }

    public LineChartModel getGraficoFaturamento() {
        return graficoFaturamento;
    }

    private void createLineModels() {
        graficoFaturamento = initCategoryModel();
        graficoFaturamento.setTitle("Faturamento ultimos 5 dias");
        graficoFaturamento.setLegendPosition("e");
        graficoFaturamento.setShowPointLabels(true);
        graficoFaturamento.getAxes().put(AxisType.X, new CategoryAxis("Dias"));
        Axis yAxis = graficoFaturamento.getAxis(AxisType.Y);
        yAxis.setLabel("Valor");
        yAxis.setMin(0);
        yAxis.setMax(3000);
    }

    private LineChartModel initCategoryModel() {

        LineChartModel model = new LineChartModel();

        List<Empresa> empresas = dao.lista(Empresa.class);

        for (Empresa emp : empresas) {
            ChartSeries cSer = new ChartSeries();
            cSer.setLabel(emp.getNomeFantasia());
            cSer.set(getDia1(), faturamento1(emp));
            cSer.set(getDia2(), faturamento2(emp));
            cSer.set(getDia3(), faturamento3(emp));
            cSer.set(getDia4(), faturamento4(emp));
            cSer.set(getDia5(), faturamento5(emp));

            model.addSeries(cSer);
        }

        return model;
    }

}
