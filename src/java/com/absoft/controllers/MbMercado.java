package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.ItemPedido;
import com.absoft.entities.Pedido;
import com.absoft.entities.Pessoa;
import com.absoft.entities.Produto;
import com.absoft.util.Mensagem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbMercado")
@SessionScoped
public class MbMercado implements Serializable {

    private Pedido pedido = new Pedido();
    private ItemPedido itemPedido = new ItemPedido();

    private List<ItemPedido> itensPedido = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();

    private List<Pessoa> clientes = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbMercado() {
    }

    public void novo() {
        pedido = new Pedido();
        pedido.setDesconto(BigDecimal.ZERO);
        pedido.setTotal(BigDecimal.ZERO);
        itemPedido = new ItemPedido();
        itemPedido.setQuantidade(BigDecimal.ONE);
        itemPedido.setDesconto(BigDecimal.ZERO);
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (pedido == null) {
            msg.retornaAdvertencia("Selecione um pedido!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (pedido.getId() == null) {
            dao.inserir(pedido);
            msg.retornaInfo("Pedido cadastrada com sucesso!");
        } else {
            dao.atualizar(pedido);
            msg.retornaInfo("Pedido atualizada com sucesso!");
        }
        pedido = new Pedido();
        itemPedido = new ItemPedido();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("sucesso", true);
    }

    public void excluir() {
        try {
            dao.excluir(pedido);
            msg.retornaInfo("Pedido excluído com sucesso!");
        } catch (Exception ex) {
            msg.retornaErro(ex.getMessage());
        }
        pedido = new Pedido();
        itemPedido = new ItemPedido();
    }

    public void recuperaProduto() {
        Produto pro = (Produto) dao.recupera(Produto.class, itemPedido.getProduto().getId());
        itemPedido.setValorItem(pro.getPrecoVenda());
        itemPedido.setValorTotal(pro.getPrecoVenda().multiply(itemPedido.getQuantidade()));
        itemPedido.setProduto(pro);
        /*Calcula*/

    }

    public void adicionaItem() {
        recuperaProduto();
        itensPedido.add(itemPedido);
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ItemPedido getItemPedido() {
        return itemPedido;
    }

    public void setItemPedido(ItemPedido itemPedido) {
        this.itemPedido = itemPedido;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public List<Pedido> getPedidos() {
        return dao.lista(Pedido.class);
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<Pessoa> getClientes() {
        return dao.listaCondicao(Pessoa.class, "cliFor = 'C' OR cliFor = 'A'");
    }

    public void setClientes(List<Pessoa> clientes) {
        this.clientes = clientes;
    }

}
