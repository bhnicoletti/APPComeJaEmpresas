package br.com.comeja.comejaempresas.model;

/**
 * Created by Nicoletti on 21/06/2016.
 */
public class Configuracao {
    private Long id;
    private Integer versaoAppAndroid;
    private Integer versaoAppIphone;
    private String tituloMensagem;
    private String mensagemMural;
    private boolean status;
    private boolean paginainicial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersaoAppAndroid() {
        return versaoAppAndroid;
    }

    public void setVersaoAppAndroid(Integer versaoAppAndroid) {
        this.versaoAppAndroid = versaoAppAndroid;
    }

    public Integer getVersaoAppIphone() {
        return versaoAppIphone;
    }

    public void setVersaoAppIphone(Integer versaoAppIphone) {
        this.versaoAppIphone = versaoAppIphone;
    }

    public String getTituloMensagem() {
        return tituloMensagem;
    }

    public void setTituloMensagem(String tituloMensagem) {
        this.tituloMensagem = tituloMensagem;
    }

    public String getMensagemMural() {
        return mensagemMural;
    }

    public void setMensagemMural(String mensagemMural) {
        this.mensagemMural = mensagemMural;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isPaginainicial() {
        return paginainicial;
    }

    public void setPaginainicial(boolean paginainicial) {
        this.paginainicial = paginainicial;
    }


}
