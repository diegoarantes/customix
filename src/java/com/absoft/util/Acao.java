package com.absoft.util;

/**
 * Estrutura para Ações no Sistema usadas em Permissões
 *
 * @author Diego Arantes
 */
public class Acao {

    private String acao;
    private String codAcao;

    public Acao(String codAcao, String acao) {
        this.acao = acao;
        this.codAcao = codAcao;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getCodAcao() {
        return codAcao;
    }

    public void setCodAcao(String codAcao) {
        this.codAcao = codAcao;
    }

}
