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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author javie
 */
@Data
@Entity
@Table(name = "predioservicio")
@NamedQueries({
    @NamedQuery(name = "Predioservicio.findAll", query = "SELECT p FROM Predioservicio p")})
public class Predioservicio implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "idpredio", referencedColumnName = "id")
    @ManyToOne
    private Predio idpredio;
    @JoinColumn(name = "idservicio", referencedColumnName = "id")
    @ManyToOne
    private Servicios idservicio;

    public Predioservicio() {
    }

    public Predioservicio(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Predioservicio)) {
            return false;
        }
        Predioservicio other = (Predioservicio) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.rivdu.entidades.Predioservicio[ id=" + id + " ]";
    }
    
}
