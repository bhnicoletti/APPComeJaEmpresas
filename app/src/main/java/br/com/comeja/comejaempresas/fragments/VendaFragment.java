package br.com.comeja.comejaempresas.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.comeja.comejaempresas.MainActivity;
import br.com.comeja.comejaempresas.R;
import br.com.comeja.comejaempresas.adapter.ItemIngredienteAdpater;
import br.com.comeja.comejaempresas.adapter.VendaAdapter;
import br.com.comeja.comejaempresas.interfaces.RecyclerViewOnClickListenerHack;
import br.com.comeja.comejaempresas.model.ItemVenda;
import br.com.comeja.comejaempresas.model.Venda;


public class VendaFragment extends Fragment implements RecyclerViewOnClickListenerHack {
    TextView txtDataVenda, txtStatusVenda, txtVlrVenda, txtIdVenda, txtFormaPagamento, txtEnderecoEntrega, txtTelefone;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewDialogo;
    private List<ItemVenda> mList;
    private ArrayList<HashMap<String,String>> mListDialogo;
    private Venda venda;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_venda, container, false);

        txtIdVenda = (TextView) view.findViewById(R.id.txtIdVenda);
        txtDataVenda = (TextView) view.findViewById(R.id.txtDataVenda);
        txtStatusVenda = (TextView) view.findViewById(R.id.txtstatusVenda);
        txtVlrVenda = (TextView) view.findViewById(R.id.txtvlrVenda);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listaProdutos);

        txtFormaPagamento = (TextView) view.findViewById(R.id.txtFormaPagamento);
        txtEnderecoEntrega = (TextView) view.findViewById(R.id.txtEnderecoEntrega);
        txtTelefone = (TextView) view.findViewById(R.id.txtTelefone);


        venda = ((MainActivity)getActivity()).getVendaSelecionada();


        txtIdVenda.setText("Cliente: "+venda.getCliente().getNomeFantasiaPessoa());
        txtTelefone.setText(venda.getCliente().getCelularPessoa()+" | "+venda.getCliente().getTelefonePessoa());
        Locale ptBr = new Locale("pt", "BR");
        String valorString = NumberFormat.getCurrencyInstance(ptBr).format(venda.getVlrTotalVenda());
        txtVlrVenda.setText("Valor Total: "+valorString);
        txtStatusVenda.setText("Status: "+venda.getStatusVenda());
        txtFormaPagamento.setText("Forma de Pagamento: "+venda.getFormPagamento());
        if(venda.getEndereco() != null) {
            txtEnderecoEntrega.setText("Logradouro: " + venda.getEndereco().getRuaEndereco()
                    + ", nº " + venda.getEndereco().getNumeroEndereco()
                    + "\n" + venda.getEndereco().getBairroEndereco()
                    + " - " + venda.getEndereco().getCidadeEndereco());
        }
        else{
            txtEnderecoEntrega.setText("Retirar no local");
        }

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        String data = formataData.format(venda.getDataVenda());
        txtDataVenda.setText(data+" - "+venda.getHora());


        FloatingActionButton fabFinalizar = (FloatingActionButton) view.findViewById(R.id.fabFinalizar);
        fabFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   ((MainActivity)getActivity()).finalizarVenda(venda.getIdVenda());

            }
        });

        FloatingActionButton fabNot = (FloatingActionButton) view.findViewById(R.id.fabNot);
        fabNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).recusarVenda(venda.getIdVenda());

            }
        });

        FloatingActionButton fabAceitar = (FloatingActionButton) view.findViewById(R.id.fabAceitar);
        fabAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).aceitarVenda(venda.getIdVenda());


            }
        });

        if(venda.getStatusVenda().equals("Aceito")){
            fabNot.setVisibility(View.INVISIBLE);
            fabAceitar.setVisibility(View.INVISIBLE);

        }
        else{
            fabFinalizar.setVisibility(View.INVISIBLE);
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listaProdutos);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(llm);

        mList = ((MainActivity) getActivity()).getVendaSelecionada().getCarrinho();
        VendaAdapter adapter = new VendaAdapter(getActivity(), mList);

        mRecyclerView.setAdapter(adapter);


        return view;

    }

    @Override
    public void onClickListener(View view, int position) {


        mListDialogo = new ArrayList<>();
        LayoutInflater inflaterDialogo = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDialogo  = inflaterDialogo.inflate(R.layout.dialogo, null);
        mRecyclerViewDialogo = (RecyclerView) viewDialogo.findViewById(R.id.rv_listaIngredientes);


        mRecyclerViewDialogo.setClickable(false);

        mRecyclerViewDialogo.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerViewDialogo.setLayoutManager(llm);

        for (int i = 0; i < mList.get(position).getIngredientesProduto().size(); i++) {
            HashMap<String, String> lista = new HashMap<>();
            lista.put("tipo" , "produto");
            lista.put("ingrediente" , mList.get(position).getIngredientesProduto().get(i).getNomeIngrediente());
            mListDialogo.add(lista);

        }

        for (int i = 0; i < mList.get(position).getIngredientesRetirados().size(); i++) {
            HashMap<String, String> lista = new HashMap<>();
            lista.put("tipo" , "retirado");
            lista.put("ingrediente" , mList.get(position).getIngredientesRetirados().get(i).getNomeIngrediente());
            mListDialogo.add(lista);

        }
        for (int i = 0; i < mList.get(position).getIngredientesAdicionais().size(); i++) {
            HashMap<String, String> lista = new HashMap<>();
            lista.put("tipo" , "adicional");
            lista.put("ingrediente" , mList.get(position).getIngredientesAdicionais().get(i).getNomeIngrediente());
            mListDialogo.add(lista);

        }


        ItemIngredienteAdpater adapter = new ItemIngredienteAdpater(getActivity(), mListDialogo);

        mRecyclerViewDialogo.setAdapter(adapter);

        Dialog dl = new Dialog(getActivity());
        dl.setContentView(viewDialogo);
        dl.setTitle("Ingredientes");
        dl.show();
    }

    @Override
    public void onLongPressClickListener(View view, int position) {

    }


    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh) {
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                rv.getChildPosition(cv));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv));
                    }

                    return (true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
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
