package com.joaopaulofcc.gt4w.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;

@Entity
@Table(name="Pessoa")
public class Pessoa
{
    @Id
    @NotBlank
    @Size(max = 11)
    private String cpf;

    @NotBlank
    @Size(max = 60)
    private String nome;

    @NotBlank
    @Size(min = 2, max = 2)
    private String uf;

    @Digits(integer = 3, fraction = 2)
    private Float peso;

    private Date dataNasc;

    public String getCpf()
    {
        return cpf;
    }

    public void setCpf(String cpf)
    {
        this.cpf = cpf;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getUf()
    {
        return uf;
    }

    public void setUf(String uf)
    {
        this.uf = uf;
    }

    public Float getPeso()
    {
        return peso;
    }

    public void setPeso(Float peso)
    {
        this.peso = peso;
    }

    public Date getDataNasc()
    {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc)
    {
        this.dataNasc = dataNasc;
    }
}
