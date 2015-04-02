package com.absoft.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego Arantes
 */
public class AcaoUtil {

    private List<Acao> acoes = new ArrayList<>();

    public List<Acao> getAcoes() {
        //Sistema
        acoes.add(new Acao("sysAll", "Todas as permissões do sistema"));

        //Dashboard
        acoes.add(new Acao("dasVhj", "Visualizar vendas hoje por empresa"));
        acoes.add(new Acao("dasVu5", "Visualizar vendas dos ultimos 5 dias"));
        acoes.add(new Acao("dasEst", "Visualizar produtos com estoque baixo"));

        //Produtos
        acoes.add(new Acao("cadPro", "Visualizar Produtos"));
        acoes.add(new Acao("ediPro", "Ações Produtos"));

        //Pessoas
        acoes.add(new Acao("cadPes", "Visualizar Pessoas"));
        acoes.add(new Acao("ediPes", "Ações Pessoas"));

        //Categorias
        acoes.add(new Acao("cadCat", "Visualizar Categorias"));
        acoes.add(new Acao("ediCat", "Ações Categorias"));

        //Unidades de Medida
        acoes.add(new Acao("cadUni", "Visualizar Unidades de Medida"));
        acoes.add(new Acao("ediUni", "Ações Unidades de Medida"));

        //Formas de Pagamento
        acoes.add(new Acao("cadFrp", "Visualizar Formas de Pagamento"));
        acoes.add(new Acao("ediFrp", "Ações Formas de Pagamento"));

        //Pedidos
        acoes.add(new Acao("impCom", "Implantar Pedidos"));
        acoes.add(new Acao("cadCom", "Visualizar Pedidos"));
        acoes.add(new Acao("relCom", "Relatório de Pedidos"));
        acoes.add(new Acao("altCom", "Alterar Pedido"));
        acoes.add(new Acao("alfCom", "Alterar Pedido Fechado"));
        acoes.add(new Acao("excCom", "Excluir Pedido"));
        acoes.add(new Acao("fecCom", "Fechar Venda"));
        acoes.add(new Acao("empCom", "Vender em outra filial"));

        return acoes;
    }

    public void setAcoes(List<Acao> acoes) {
        this.acoes = acoes;
    }

}
