package br.com.comeja.comejaempresas.model;

/**
 * Created by Nicoletti on 25/06/2016.
 */
public class Tamanho {
    private Long idTamanho;
    private String tamanho;
    private Double valorTamanho;


    public Long getIdTamanho() {
        return idTamanho;
    }

    public void setIdTamanho(Long idTamanho) {
        this.idTamanho = idTamanho;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Double getValorTamanho() {
        return valorTamanho;
    }

    public void setValorTamanho(Double valorTamanho) {
        this.valorTamanho = valorTamanho;
    }

    public Tamanho() {
    }
}
