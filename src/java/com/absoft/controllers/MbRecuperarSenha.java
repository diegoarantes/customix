package com.absoft.controllers;

import com.absoft.converters.ConverterSHA1;
import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Usuario;
import com.absoft.util.JavaMailUtil;
import com.absoft.util.Mensagem;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbRecuperarSenha")
@RequestScoped
public class MbRecuperarSenha implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    DAOGenerico dao;

    String email;

    String confirmacao;

    Usuario usuario;

    /*Variáveis para recuperação que serão retornadas via GET*/
    String url_recCode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("recCode");
    String url_token = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("token");
    String url_user = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("user");
    String url_gerToken = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gerToken");

    Mensagem msg = new Mensagem();

    public MbRecuperarSenha() {
    }

    public void recuperarSenha() {
        Usuario user = null; // Objeto usuário

        String recCode; //Código do usuário criptografado
        String token; //Senha atual criptografada novamente em sha1
        String gerToken; //Token gerado válido até o final do dia !

        //Deve recuperar o usuário pelo e-mail
        List<Usuario> usr = dao.listaCondicao(Usuario.class, "email = '" + this.email + "'");
        for (Usuario usu : usr) {
            user = usu;
        }
        if (user == null) {
            msg.retornaErro("Este email não está cadastrado no sistema!");
        } else {
            /* REC CODE*/
            recCode = ConverterSHA1.cipher(user.getId().toString());

            /* token*/
            token = ConverterSHA1.cipher(user.getSenha());

            /* GER TOKEN
             Lógica USUÁRIO + DATA + CODIGO DO USUARIO + ABSOFT ex: DIEGO2001201515501ABSOFT
             Criptografar em SHA-1
             */
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            gerToken = user.getUsuario() + dt1.format(new Date()) + user.getId() + "ABSOFT";

            gerToken = ConverterSHA1.cipher(gerToken);

            String gerLink = "http://localhost:8080/ABSOFT/pub/recuperarsenha.jsf"
                    + "?recCode=" + recCode
                    + "&token=" + token
                    + "&user=" + user.getUsuario()
                    + "&gerToken=" + gerToken;

            new JavaMailUtil().enviaEmail(email, "ABSOFT - Recuperação de senha",
                    "Olá, " + user.getNome() + ".<br/>"
                    + "Você solicitou uma redefinição de senha recentemente.<br/> <br/>"
                    + "Para alterar sua senha, clique <a href='" + gerLink + "'>aqui</a> "
                    + "ou cole o seguinte link no seu navegador:<br/>" + gerLink
                    + "<br/> <br/>O link vencerá ás 0:00 horas, portanto, utilize-o imediatamente."
                    + "		        <br /><br/ >\n"
                    + "			Atenciosamente,<br />\n"
                    + "			<strong>ABSoft </strong>- <span style=\"color:#0066cc;\"><strong>Base</strong></span>");

            msg.retornaInfo("Email Enviado !\n Enviamos um e-mail para que você redefina sua senha com mais rapidez.");
            System.out.println(gerLink);
        }

        email = "";

    }

    public boolean verificaToken() {
        System.out.println("------------->>>>>>> VERIFICANDO TOKEEN <<<<<<<<--------------------");
        Usuario user = null;

        //Deve recuperar o usuário pelo e-mail
        List<Usuario> usr = dao.listaCondicao(Usuario.class, "usuario = '" + this.url_user + "'");
        for (Usuario usu : usr) {
            user = usu;
        }

        if (user == null) {
            return false;
        } else {
            /* REC CODE*/
            String recCode = ConverterSHA1.cipher(user.getId().toString());

            /* token*/
            String token = ConverterSHA1.cipher(user.getSenha());

            /* GER TOKEN
             Lógica USUÁRIO + DATA + CODIGO DO USUARIO + ABSOFT ex: DIEGO2001201515501ABSOFT
             Criptografar em SHA-1
             */
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            String gerToken = user.getUsuario() + dt1.format(new Date()) + user.getId() + "ABSOFT";
            gerToken = ConverterSHA1.cipher(gerToken);

            //Retorna o boleano da condição
            boolean retorno = recCode.equals(this.url_recCode)
                    && token.equals(this.url_token)
                    && gerToken.equals(this.url_gerToken);

            if (retorno == false) {
                msg.retornaErro("Token inválido! \n o tempo para a recuperação de senha expirou.\n"
                        + "Por favor preencha novamente o formulário de recuperação");
            } else {
                usuario = user;
            }

            return retorno;
        }

    }

    public void alteraSenha() {
        if (usuario.getSenha().equals(confirmacao)) {
            dao.atualizar(usuario);
            msg.retornaInfo("Senha alterada com sucesso !");
        } else {
            msg.retornaErro("As senhas não coincidem!");
        }
    }

    /* GETTERS E SETTERS */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl_recCode() {
        return url_recCode;
    }

    public void setUrl_recCode(String url_recCode) {
        this.url_recCode = url_recCode;
    }

    public String getUrl_token() {
        return url_token;
    }

    public void setUrl_token(String url_token) {
        this.url_token = url_token;
    }

    public String getUrl_user() {
        return url_user;
    }

    public void setUrl_user(String url_user) {
        this.url_user = url_user;
    }

    public String getUrl_gerToken() {
        return url_gerToken;
    }

    public void setUrl_gerToken(String url_gerToken) {
        this.url_gerToken = url_gerToken;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getConfirmacao() {
        return confirmacao;
    }

    public void setConfirmacao(String confirmacao) {
        this.confirmacao = confirmacao;
    }

}
