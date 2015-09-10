package com.absoft.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Diego Arantes
 */
@Entity
@Table(name = "permissao" ,uniqueConstraints = {
    @UniqueConstraint(columnNames = {"codPermissao", "perfil_id"}, name = "vinculosPerfil")}
)//Define que o valor das colunas juntas devem ser único ex: 1 e 1 nao podera repetir 1 e 1 em outro registro)
public class Permissao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(length = 6)
    private String codPermissao;

    @NotNull
    @ManyToOne
    private Perfil perfil;

    public Permissao() {
        perfil = new Perfil();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodPermissao() {
        return codPermissao;
    }

    public void setCodPermissao(String codPermissao) {
        this.codPermissao = codPermissao;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Permissao other = (Permissao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
