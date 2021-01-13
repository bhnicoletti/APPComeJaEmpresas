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
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import br.com.comeja.comejaempresas.MainActivity;
import br.com.comeja.comejaempresas.R;
import br.com.comeja.comejaempresas.extras.Mask;
import br.com.comeja.comejaempresas.model.Endereco;
import br.com.comeja.comejaempresas.model.Pessoa;


public class EmpresaFragment extends Fragment {

    private Pessoa empresa;
    private EditText edtNomeFantasia, edtCelular, edtTelefone, edtValorEntrega, edtObservacoes, edtHorarioFuncionamento, edtTempoPreparo;
    private Button btnSalvar;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresa, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        empresa = ((MainActivity) getActivity()).empresaLogada;

        edtNomeFantasia = (EditText) view.findViewById(R.id.edtNomeFantasia);
        edtCelular = (EditText) view.findViewById(R.id.edtTelefoneCelular);
        edtTelefone = (EditText) view.findViewById(R.id.edtTelefoneFixo);
        edtValorEntrega = (EditText) view.findViewById(R.id.edtValorEntrega);
        edtObservacoes = (EditText) view.findViewById(R.id.edtObservacoes);
        edtHorarioFuncionamento = (EditText) view.findViewById(R.id.edtHorarioFuncionamento);
        edtTempoPreparo = (EditText) view.findViewById(R.id.edttempoPreparo);
        edtTelefone.addTextChangedListener(Mask.insert(Mask.TELEFONE_MASK, edtTelefone));
        edtCelular.addTextChangedListener(Mask.insert(Mask.CELULAR_MASK, edtCelular));
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);

        edtNomeFantasia.setText(empresa.getNomeFantasiaPessoa());
        edtCelular.setText(empresa.getCelularPessoa());
        edtTelefone.setText(empresa.getTelefonePessoa());
        edtValorEntrega.setText(empresa.getValorEntrega().toString());
        edtObservacoes.setText(empresa.getObs());
        edtHorarioFuncionamento.setText(empresa.getHorarioFuncionamento());
        edtTempoPreparo.setText(empresa.getTempoPreparo());

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()) {
                    empresa.setNomeFantasiaPessoa(edtNomeFantasia.getText().toString());
                    empresa.setCelularPessoa(edtCelular.getText().toString());
                    empresa.setTelefonePessoa(edtTelefone.getText().toString());
                    empresa.setObs(edtObservacoes.getText().toString());
                    empresa.setHorarioFuncionamento(edtHorarioFuncionamento.getText().toString());
                    empresa.setValorEntrega(Double.parseDouble(edtValorEntrega.getText().toString()));
                    empresa.setTempoPreparo(edtTempoPreparo.getText().toString());

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd")
                            .create();
                    try {
                        ((MainActivity) getActivity()).atualizarUsuario(gson.toJson(empresa));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
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

    public boolean validar() {
        if (edtNomeFantasia.getText().length() > 0
                && edtTempoPreparo.getText().length() > 0
                && edtHorarioFuncionamento.getText().length() > 0
                && edtObservacoes.getText().length() > 0
                && edtValorEntrega.getText().length() > 0
                && edtCelular.getText().length() > 0) {
            return true;
        } else {
            return false;
        }

    }


}
