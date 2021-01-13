package br.com.comeja.comejaempresas.model;

/**
 * Created by Nicoletti on 08/10/2016.
 */

public class TokenEvent {
    private String token;

    public TokenEvent(String token ){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}