package com.example.lawbot.activities.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.List;

public class ProcessosResponse {

    @Expose
    @SerializedName("id")
    private Long id;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("dataAtualizacao")
    private String dataAtualizacao;
    @Expose
    @SerializedName("descricao")
    private String descricao;
    @Expose
    @SerializedName("numProcesso")
    private String numProcesso;
    @Expose
    @SerializedName("assunto")
    private String assunto;
    @Expose
    @SerializedName("classe")
    private String classe;
    @Expose
    @SerializedName("nomeReu")
    private List<Reu> nomeReu;
    @Expose
    @SerializedName("nomeAutor")
    private List<Autor> nomeAutor;

    public List<Reu> getNomeReu() {
        return nomeReu;
    }

    public void setNomeReu(List<Reu> nomeReu) {
        this.nomeReu = nomeReu;
    }

    public List<Autor> getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(List<Autor> nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(String dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNumProcesso() {
        return numProcesso;
    }

    public void setNumProcesso(String numProcesso) {
        this.numProcesso = numProcesso;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

}
