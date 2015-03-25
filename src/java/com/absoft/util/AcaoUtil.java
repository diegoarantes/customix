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
        acoes.add(new Acao("sysAll", "Todas as permissões do sistema"));

        return acoes;
    }

    public void setAcoes(List<Acao> acoes) {
        this.acoes = acoes;
    }

}
