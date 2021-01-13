package br.com.comeja.comejaempresas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import org.json.JSONException;

import br.com.comeja.comejaempresas.MainActivity;
import br.com.comeja.comejaempresas.R;
import br.com.comeja.comejaempresas.extras.Mask;
import br.com.comeja.comejaempresas.model.Endereco;


public class EnderecoFragment extends Fragment {
    private Endereco endereco;

    private EditText rua, numero, bairro, cidade, complemento, cep;
    private Button btnSalvar;
    private Spinner estado;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        View view = inflater.inflate(R.layout.fragment_endereco, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        endereco = ((MainActivity)getActivity()).getEndereco();

        rua = (EditText) view.findViewById(R.id.edtRua);
        numero = (EditText) view.findViewById(R.id.edtNumero);
        bairro = (EditText) view.findViewById(R.id.edtBairro);
        cidade = (EditText) view.findViewById(R.id.edtCidade);
        complemento = (EditText) view.findViewById(R.id.edtComplemento);
        estado = (Spinner) view.findViewById(R.id.spEstado);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        cep = (EditText) view.findViewById(R.id.edtCep);

        rua.setText(endereco.getRuaEndereco());
        numero.setText(endereco.getNumeroEndereco());
        bairro.setText(endereco.getBairroEndereco());
        cidade.setText(endereco.getCidadeEndereco());
        complemento.setText(endereco.getComplementoEndereco());
        cep.setText(endereco.getCepEndereco());
        cep.addTextChangedListener(Mask.insert(Mask.CEP_MASK, cep));

        String[] estados = new String[]{"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
                "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, estados);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        estado.setAdapter(adapterSpinner);

        if (endereco.getIdEndereco()!= null) {
            for (int i = 0; i < estados.length; i++) {
                if (endereco.getEstadoEndereco().equals(estados[i])) {
                    estado.setSelection(i);
                }
            }
        }


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endereco.setRuaEndereco(rua.getText().toString());
                endereco.setBairroEndereco(bairro.getText().toString());
                endereco.setNumeroEndereco(numero.getText().toString());
                endereco.setCidadeEndereco(cidade.getText().toString());
                endereco.setComplementoEndereco(complemento.getText().toString());
                endereco.setEstadoEndereco(estado.getSelectedItem().toString());
                endereco.setCepEndereco(cep.getText().toString());
                endereco.setPessoa(((MainActivity)getActivity()).getEmpresaLogada().getIdPessoa());



                Gson gson = new Gson();
                try {
                    ((MainActivity) getActivity()).salvaEnderecoWeb(gson.toJson(endereco));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;

    }


    @Override
    public void onResume() { // HACKCODE TO WORK WHEN JUST COME BACK
        super.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


}
