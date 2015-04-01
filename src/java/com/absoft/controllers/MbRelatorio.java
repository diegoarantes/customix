package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Pedido;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbRelatorio")
@RequestScoped
public class MbRelatorio {

    @EJB
    DAOGenerico dao;

    Pedido pedido = new Pedido();

    private Long idEmpresa;
    private Date dtInicial;
    private Date dtFinal;
    private boolean aberto;

    public MbRelatorio() {
    }

    public void relatorioPedidos() throws SQLException, JRException, IOException {
        HashMap param = new HashMap();
        param.put("empresa", idEmpresa);
        param.put("dtInicial", dtInicial);
        param.put("dtFinal", dtFinal);
        param.put("aberto", aberto);
        imprimeRelatorio(param, "pedidos.jasper");
    }

    public void relatorioPedido() throws SQLException, JRException, IOException {
        HashMap param = new HashMap();
        param.put("pedido_id", pedido.getId());
        imprimeRelatorio(param, "pedido.jasper");
    }

    /*
     Recebe os parámetros do tipo HashMap ex: parametros.put("data2", data2);
     Recebe o nome do arquivo do relatorio ex: relatorio.jasper
     */
    private void imprimeRelatorio(HashMap param, String relatorio) throws SQLException, JRException, IOException {

        Connection con = dao.getConnection();

        FacesContext facesContext = FacesContext.getCurrentInstance();

        facesContext.responseComplete();

        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

        JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/relatorios/" + relatorio), param, con);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.exportReport();

        byte[] bytes = baos.toByteArray();

        if (bytes != null && bytes.length > 0) {

            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            response.setContentType("application/pdf");

            response.setHeader("Content-disposition", "inline; filename=\"relatorioCustomix.pdf\"");

            response.setContentLength(bytes.length);

            ServletOutputStream outputStream = response.getOutputStream();

            outputStream.write(bytes, 0, bytes.length);

            outputStream.flush();

            outputStream.close();

            con.close(); //Fecha a Conexão

        }

    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Date getDtInicial() {
        return dtInicial;
    }

    public void setDtInicial(Date dtInicial) {
        this.dtInicial = dtInicial;
    }

    public Date getDtFinal() {
        return dtFinal;
    }

    public void setDtFinal(Date dtFinal) {
        this.dtFinal = dtFinal;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

}
