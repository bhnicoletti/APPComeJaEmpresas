package br.com.comeja.comejaempresas.network;

import org.json.JSONArray;

import br.com.comeja.comejaempresas.model.WrapObjToNetwork;


/**
 * Created by viniciusthiengo on 7/26/15.
 */
public interface Transaction {
    WrapObjToNetwork doBefore();

    void doAfter(JSONArray jsonArray);
}
