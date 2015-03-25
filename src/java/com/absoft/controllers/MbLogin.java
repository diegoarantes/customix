package com.absoft.controllers;

import com.absoft.converters.ConverterSHA1;
import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Usuario;
import com.absoft.util.Mensagem;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbLogin")
@RequestScoped
public class MbLogin {

    @EJB
    DAOGenerico dao;

    String usuario;
    String senha;

    Mensagem msg = new Mensagem();

    public MbLogin() {
    }

    public String efetuarLogin() {

        //Prevenção de SQL INJECTION
        usuario = usuario.replaceAll("'", "/");

        Usuario usu = null;

        List<Usuario> usrBanco = dao.listaCondicao(Usuario.class, "(usuario = '" + usuario + "') AND senha = '" + ConverterSHA1.cipher(senha) + "'");
        for (Usuario usr : usrBanco) {
            usu = usr;
        }

        if (usu != null && usu.isAtivo()) { //Se usuário for diferente de Nulo e está ativo
            //Adiciona o usuário logado na sessão
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", usu);

            //Verifica se o usuário está usando a senha padrão
            if (usu.getSenha().equals(ConverterSHA1.cipher("mudar.123"))) {
                msg.retornaAdvertencia("Você esta usando a senha padrão!");
                msg.retornaAdvertencia("Para sua segurança altere sua senha imediatamente!"
                        + " Clique no seu nome no canto superior direito e vá em Informações do Usuáio.");
            }

            return "/app/home.jsf";
        } else if (usu != null && !usu.isAtivo()) {//Se usuário for diferente de Nulo e está Inativo 
            msg.retornaAdvertencia("Usuário Inativo!");

            return "";
        } else {
            msg.retornaErro("Usuário ou senha inválidos!");

            return "";
        }
    }

    /**
     * Efetua logout do usuário do sistema
     *
     * @return retorna para página de login
     */
    public String efetuarLogout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "/login.jsf";
    }

    public Usuario usuarioLogado() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    /* GETTERS E SETTERS */
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
