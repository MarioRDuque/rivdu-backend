/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rivdu.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "estadocliente")
@NamedQueries({
    @NamedQuery(name = "Estadocliente.findAll", query = "SELECT e FROM Estadocliente e")})
public class Estadocliente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;

    public Estadocliente() {
    }

    public Estadocliente(Long id) {
        this.id = id;
    }

    public Estadocliente(Long id, boolean estado, String nombre) {
        this.id = id;
        this.estado = estado;
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Estadocliente)) {
            return false;
        }
        Estadocliente other = (Estadocliente) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.rivdu.entidades.Estadocliente[ id=" + id + " ]";
    }

//    public List<Personacompra> getPersonacompraList() {
//        return personacompraList;
//    }
//
//    public void setPersonacompraList(List<Personacompra> personacompraList) {
//        this.personacompraList = personacompraList;
//    }
    
}
