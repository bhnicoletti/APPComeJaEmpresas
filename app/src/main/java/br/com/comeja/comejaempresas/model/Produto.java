package br.com.comeja.comejaempresas.model;

import java.util.List;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class Produto {
    private Long idProduto;
    private String nomeProduto;
    private String descricaoProduto;
    private Double valorProduto;
    private String imagemProduto;
    private Pessoa empresaProduto;
    private Categoria categoriaProduto;
    private List<Ingrediente> ingredientesProduto;
    private String statusProduto;
    private List<Tamanho> tamanhos;
    private Boolean alcoolico;

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public Double getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(Double valorProduto) {
        this.valorProduto = valorProduto;
    }

    public String getImagemProduto() {
        return imagemProduto;
    }

    public void setImagemProduto(String imagemProduto) {
        this.imagemProduto = imagemProduto;
    }

    public Pessoa getEmpresaProduto() {
        return empresaProduto;
    }

    public void setEmpresaProduto(Pessoa empresaProduto) {
        this.empresaProduto = empresaProduto;
    }

    public Categoria getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(Categoria categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public List<Ingrediente> getIngredientesProduto() {
        return ingredientesProduto;
    }

    public void setIngredientesProduto(List<Ingrediente> ingredientesProduto) {
        this.ingredientesProduto = ingredientesProduto;
    }

    public String getStatusProduto() {
        return statusProduto;
    }

    public void setStatusProduto(String statusProduto) {
        this.statusProduto = statusProduto;
    }

    public List<Tamanho> getTamanhos() {
        return tamanhos;
    }

    public void setTamanhos(List<Tamanho> tamanhos) {
        this.tamanhos = tamanhos;
    }

    public Boolean getAlcoolico() {
        return alcoolico;
    }

    public void setAlcoolico(Boolean alcoolico) {
        this.alcoolico = alcoolico;
    }

    public Produto() {
    }


}
