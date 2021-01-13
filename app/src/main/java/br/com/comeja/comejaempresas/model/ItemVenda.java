package br.com.comeja.comejaempresas.model;

import java.util.List;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class ItemVenda {
    private Long idItemVenda;
    private Double quantItemVenda;
    private Produto produtoItemVenda;
    private Double vlrItemVenda;
    private Double vlrUnitarioProduto;
    private Long vendaItemVenda;
    private List<Ingrediente> ingredientesProduto;
    private List<Ingrediente> ingredientesAdicionais;
    private List<Ingrediente> ingredientesRetirados;
    private Boolean metade;
    private String tamanho;

    public Long getIdItemVenda() {
        return idItemVenda;
    }

    public void setIdItemVenda(Long idItemVenda) {
        this.idItemVenda = idItemVenda;
    }

    public Double getQuantItemVenda() {
        return quantItemVenda;
    }

    public void setQuantItemVenda(Double quantItemVenda) {
        this.quantItemVenda = quantItemVenda;
    }

    public Produto getProdutoItemVenda() {
        return produtoItemVenda;
    }

    public void setProdutoItemVenda(Produto produtoItemVenda) {
        this.produtoItemVenda = produtoItemVenda;
    }

    public Double getVlrItemVenda() {
        return vlrItemVenda;
    }

    public void setVlrItemVenda(Double valor) {
        this.vlrItemVenda = valor;

    }

    public Long getVendaItemVenda() {
        return vendaItemVenda;
    }

    public void setVendaItemVenda(Long vendaItemVenda) {
        this.vendaItemVenda = vendaItemVenda;
    }

    public List<Ingrediente> getIngredientesProduto() {
        return ingredientesProduto;
    }

    public void setIngredientesProduto(List<Ingrediente> ingredientesProduto) {
        this.ingredientesProduto = ingredientesProduto;
    }

    public List<Ingrediente> getIngredientesAdicionais() {
        return ingredientesAdicionais;
    }

    public void setIngredientesAdicionais(List<Ingrediente> ingredientesAdicionais) {
        this.ingredientesAdicionais = ingredientesAdicionais;
    }

    public Double getVlrUnitarioProduto() {
        return vlrUnitarioProduto;
    }

    public void setVlrUnitarioProduto(Double vlrUnitarioProduto) {
        this.vlrUnitarioProduto = vlrUnitarioProduto;
    }

    public Boolean getMetade() {
        return metade;
    }

    public void setMetade(Boolean metade) {
        this.metade = metade;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public List<Ingrediente> getIngredientesRetirados() {
        return ingredientesRetirados;
    }

    public void setIngredientesRetirados(List<Ingrediente> ingredientesRetirados) {
        this.ingredientesRetirados = ingredientesRetirados;
    }

    public ItemVenda() {
    }

}
