package com.absoft.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Diego Arantes
 */
@XmlRootElement(name = "WebCepDF")
public class WebCepDF {

    @XmlElement(name = "resultado", required = true)
    private String resultado;
    @XmlElement(name = "resultadotxt", required = true)
    private String resultadoTxt;
    @XmlElement(name = "uf", required = true)
    private String uf;
    @XmlElement(name = "cidade", required = true)
    private String cidade;
    @XmlElement(name = "bairro", required = true)
    private String bairro;
    @XmlElement(name = "tipologradouro", required = true)
    private String tipoLogradouro;
    @XmlElement(name = "logradouro", required = true)
    private String logradouro;

    public WebCepDF() {
    }

    public WebCepDF(String cep) {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(WebCepDF.class);
            Unmarshaller u = jc.createUnmarshaller();
            URL url = new URL("http://cep.desenvolvefacil.com.br/BuscarCep.php?cep=" + cep + "&ret=xml");
            WebCepDF wCep = (WebCepDF) u.unmarshal(url);

            this.resultado = wCep.getResultado();
            this.resultadoTxt = wCep.getResultadoTxt();
            this.uf = wCep.getUf();
            this.cidade = wCep.getCidade();
            this.bairro = wCep.getBairro();
            this.tipoLogradouro = wCep.getTipoLogradouro();
            this.logradouro = wCep.getLogradouro();

        } catch (JAXBException | MalformedURLException ex) {
            Logger.getLogger(WebCepDF.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getResultado() {
        return resultado;
    }

    public String getResultadoTxt() {
        return resultadoTxt;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public String getUf() {
        return uf;
    }
}
