package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Perfil;
import com.absoft.entities.Permissao;
import com.absoft.util.Acao;
import com.absoft.util.AcaoUtil;
import com.absoft.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbPermissao")
@SessionScoped
public class MbPermissao implements Serializable {

    private Permissao permissao = new Permissao();
    private List<Permissao> permissoes = new ArrayList<>();

    private List<Acao> acoes = new AcaoUtil().getAcoes(); // Retorna as acoes;

    private Perfil perfil = new Perfil();
    private List<Perfil> perfis = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbPermissao() {
    }

    public void novo() {
        perfil = new Perfil();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (perfil == null) {
            msg.retornaAdvertencia("Selecione um perfil!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (perfil.getId() == null) {

            dao.inserir(perfil);
            msg.retornaInfo("Perfil cadastrado com sucesso!");
        } else {
            dao.atualizar(perfil);
            msg.retornaInfo("Perfil atualizado com sucesso!");
        }
        perfil = new Perfil();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("sucesso", true);
    }

    public void novaPermissao() {
        if (perfil != null && perfil.getId() != null) {
            permissao = new Permissao();
            permissao.setPerfil(perfil); //Seta o perfil selecionado
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('novaPermissao').show()"); //Abre o dialogo
        } else {
            msg.retornaAdvertencia("Por favor selecione um Perfil!");
        }

    }

    public void gravaPermissao() {
        try {
            dao.inserir(permissao);
            msg.retornaInfo("Permissao adicionada com sucesso!");
            permissao = new Permissao();
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.addCallbackParam("sucesso", true);
        } catch (Exception ex) {
            msg.retornaAdvertencia("Esta permissão já está vinculada á este perfil !");
        }

    }

    public void removerPermissao() {
        try {
            dao.excluir(permissao);
            msg.retornaInfo("Permissão removida com sucesso!");
        } catch (Exception ex) {
            msg.retornaErro(ex.getMessage());
        }
        permissao = new Permissao();
    }

    public void excluirPerfil() {
        try {
            List<Permissao> perms = dao.listaCondicao(Permissao.class, "perfil.id = " + perfil.getId().toString());
            for (Permissao per : perms) {
                dao.excluir(per);
            }
            dao.excluir(perfil);
            msg.retornaInfo("Perfil excluido com sucesso!");
        } catch (Exception ex) {
            msg.retornaAdvertencia("Há um usuário vinculado á este perfil!");
            msg.retornaInfo("Foram excluidas somente as permissões!");
        }

    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public List<Permissao> getPermissoes() {
        if (perfil != null && perfil.getId() != null) {
            return dao.listaCondicao(Permissao.class, "perfil.id = " + perfil.getId().toString());
        } else {
            return permissoes;
        }

    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public List<Perfil> getPerfis() {
        return dao.lista(Perfil.class);
    }

    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }

    public List<Acao> getAcoes() {
        return acoes;
    }

    public void setAcoes(List<Acao> acoes) {
        this.acoes = acoes;
    }

    public String retornaDescAcao(String acao) {
        String desc = "";
        List<Acao> acoesl = new AcaoUtil().getAcoes();
        for (Acao ac : acoesl) {
            if (acao.equals(ac.getCodAcao())) {
                desc = ac.getAcao();
                break;
            }
        }
        return desc;
    }

}
