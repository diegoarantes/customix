package com.absoft.controllers;

import com.absoft.converters.ConverterSHA1;
import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Usuario;
import com.absoft.util.Mensagem;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbUsuario")
@RequestScoped
public class MbUsuario {

    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios = new ArrayList<>();
    Mensagem msg = new Mensagem();

    /*Para a alteração de senha*/
    private String senhaAntiga;
    private String senhaNova;
    private String confirmacao;

    @EJB
    DAOGenerico dao;

    private boolean editando;

    public MbUsuario() {
    }

    public void novo() {
        usuario = new Usuario();
        usuario.setAtivo(true);
        editando = false; // ativar o campo usuário
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (usuario == null) {
            msg.retornaAdvertencia("Selecione um usuário!");
        } else {
            editando = true; // desativar o campo usuário
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (usuario.getId() == null) {
            usuario.setSenha(ConverterSHA1.cipher("mudar.123"));
            dao.inserir(usuario);
            msg.retornaInfo("Usuário cadastrado com sucesso!");
            msg.retornaInfo("A senha padrão é 'mudar.123'");
        } else {
            dao.atualizar(usuario);
            msg.retornaInfo("Usuário atualizado com sucesso!");
        }
        usuario = new Usuario();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("sucesso", true);
    }

    public void inativar() {
        if (usuario == null) {
            msg.retornaAdvertencia("Selecione um usuário!");
        } else {
            usuario.setAtivo(false);
            dao.atualizar(usuario);
            msg.retornaInfo("Usuário inativado com sucesso!");
            usuario = new Usuario();
        }

    }

    public void redefinirSenha() {
        if (usuario == null) {
            msg.retornaAdvertencia("Selecione um usuário!");
        } else {
            usuario.setSenha(ConverterSHA1.cipher("mudar.123"));
            dao.atualizar(usuario);
            msg.retornaInfo("Senha redefinida com sucesso! A senha padrão é 'mudar.123'");
            usuario = new Usuario();
        }

    }

    public void alterarSenha() {
        Usuario user = new MbLogin().usuarioLogado();
        if (user.getSenha().equals(ConverterSHA1.cipher(senhaAntiga))) {

            if (senhaNova.equals(confirmacao)) {
                user.setSenha(senhaNova);
                dao.atualizar(user);
                msg.retornaInfo("Senha alterada com sucesso !");
                RequestContext requestContext = RequestContext.getCurrentInstance();
                requestContext.addCallbackParam("sucesso", true);
            } else {
                msg.retornaAdvertencia("A senha nova não confere com a confirmação !");
            }

        } else {
            msg.retornaAdvertencia("A senha antiga não confere !");
        }
    }

    /* GETTERS E SETTERS */
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        return dao.lista(Usuario.class);
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }

    /* Para Alteracao de Senha*/
    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public String getConfirmacao() {
        return confirmacao;
    }

    public void setConfirmacao(String confirmacao) {
        this.confirmacao = confirmacao;
    }

}
