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
import java.util.Date;
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
    private ItemPedido itemSelecionado = new ItemPedido();

    private List<ItemPedido> itensPedido = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();

    private List<Pessoa> clientes = new ArrayList<>();
    
    private List<Produto> produtos = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbMercado() {
    }

    public void novo() {
        pedido = new Pedido();
        pedido.setDesconto(BigDecimal.ZERO);
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setValor(BigDecimal.ZERO);
        
        pedido.setUsuario(new MbLogin().usuarioLogado());
        
        itemPedido = new ItemPedido();

        itemPedido.setQuantidade(BigDecimal.ONE);
        itemPedido.setDesconto(BigDecimal.ZERO);
        itensPedido.clear();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (pedido == null) {
            msg.retornaAdvertencia("Selecione um pedido!");
        } else {
            itemPedido = new ItemPedido();
            itemPedido.setQuantidade(BigDecimal.ONE);
            itemPedido.setDesconto(BigDecimal.ZERO);

            itensPedido = dao.listaCondicao(ItemPedido.class, "pedido.id = " + pedido.getId().toString()); // Recupera a lista de itens
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (!itensPedido.isEmpty()) {
            if (pedido.getId() == null) {
                pedido.setDataPedido(new Date());
                pedido.setHora(new Date());
                pedido.setAberto(false);
                

                dao.inserir(pedido);
                gravaItens();

                msg.retornaInfo("Pedido cadastrado com sucesso!");
            } else {
                atualizaItens();
                dao.atualizar(pedido);
                gravaItens();
                msg.retornaInfo("Pedido atualizado com sucesso!");
            }
            pedido = new Pedido();
            itemPedido = new ItemPedido();
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.addCallbackParam("sucesso", true);
        } else {
            msg.retornaAdvertencia("O pedido está vazio, é necessário adicionar itens para gravar.");
        }

    }

    private void gravaItens() {
        for (ItemPedido pro : itensPedido) {
            pro.setPedido(pedido);
            baixaItemEstoque(pro.getProduto(), pro.getQuantidade());
            dao.inserir(pro);
        }
    }

    private void atualizaItens() {
        try {
            List<ItemPedido> itensPedidoBanco = dao.listaCondicao(ItemPedido.class, "pedido.id = " + pedido.getId().toString()); // Recupera a lista de itens
            for (ItemPedido pro : itensPedidoBanco) {
                estornaItemEstoque(pro.getProduto(), pro.getQuantidade()); //
                dao.excluir(pro); // Exclui item por item
            }

            for (ItemPedido prod : itensPedido) {
                prod.setId(null);//Anula os IDS
            }

        } catch (Exception ex) {
            msg.retornaErro("Erro interno ! \n " + ex.getMessage());
        }
    }

    public void excluir() {
        try {

            itensPedido = dao.listaCondicao(ItemPedido.class, "pedido.id = " + pedido.getId().toString()); // Recupera a lista de itens
            for (ItemPedido pro : itensPedido) {
                estornaItemEstoque(pro.getProduto(), pro.getQuantidade());
                dao.excluir(pro); // Exclui item por item
            }

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
    }

    public void adicionaItem() {
        boolean existe = false;
        for (ItemPedido pro : itensPedido) {
            if (pro.getProduto().getId() == itemPedido.getProduto().getId()) {
                existe = true;
                break;
            }
        }

        if (!existe) {//Verifica se este produto já está adicionado
            recuperaProduto();

            itensPedido.add(itemPedido);

            //Recalcula os itens do pedido
            calculaPedido();

            itemPedido = new ItemPedido();
            itemPedido.setQuantidade(BigDecimal.ONE);
            itemPedido.setDesconto(BigDecimal.ZERO);
        } else {
            msg.retornaInfo("Este produto já está adicionado á este pedido!");
        }

    }

    public void excluirItem() {
        itensPedido.remove(itemSelecionado);
        calculaPedido();
    }

    private void calculaPedido() {
        pedido.setDesconto(BigDecimal.ZERO);
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setValor(BigDecimal.ZERO);
        for (ItemPedido pro : itensPedido) {
            pedido.setDesconto(pedido.getDesconto().add(pro.getDesconto()));
            pedido.setTotal(pedido.getTotal().add(pro.getValorTotal()));
            pedido.setValor(pedido.getValor().add(pro.getValorItem()));
        }
    }

    private void baixaItemEstoque(Produto prod, BigDecimal quant) {
        Produto produto = (Produto) dao.recupera(Produto.class, prod.getId());
        produto.setEstoque(produto.getEstoque().subtract(quant));
        dao.atualizar(produto);
    }

    private void estornaItemEstoque(Produto prod, BigDecimal quant) {
        Produto produto = (Produto) dao.recupera(Produto.class, prod.getId());
        produto.setEstoque(produto.getEstoque().add(quant));
        dao.atualizar(produto);
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

    public List<Produto> getProdutos() {
        return dao.listaCondicao(Produto.class, "empresa.id = " + pedido.getEmpresa().getId());
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    
    
    public ItemPedido getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(ItemPedido itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }

}
