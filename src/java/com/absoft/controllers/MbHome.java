package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Empresa;
import com.absoft.entities.Pedido;
import com.absoft.entities.Produto;
import com.absoft.util.Mensagem;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbHome")
@RequestScoped
public class MbHome {

    private List<Produto> estoqueBaixo = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbHome() {
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

}
