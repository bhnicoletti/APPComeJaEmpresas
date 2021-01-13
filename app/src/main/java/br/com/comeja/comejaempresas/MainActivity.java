package br.com.comeja.comejaempresas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import br.com.comeja.comejaempresas.extras.UtilTCM;
import br.com.comeja.comejaempresas.fragments.EmpresaFragment;
import br.com.comeja.comejaempresas.fragments.EnderecoFragment;
import br.com.comeja.comejaempresas.fragments.EnderecosFragment;
import br.com.comeja.comejaempresas.fragments.HomeFragment;
import br.com.comeja.comejaempresas.fragments.ProdutosFragment;
import br.com.comeja.comejaempresas.fragments.UltimosPedidosFragment;
import br.com.comeja.comejaempresas.fragments.VendaFragment;
import br.com.comeja.comejaempresas.model.Configuracao;
import br.com.comeja.comejaempresas.model.Dispositivo;
import br.com.comeja.comejaempresas.model.Endereco;
import br.com.comeja.comejaempresas.model.Pessoa;
import br.com.comeja.comejaempresas.model.TokenEvent;
import br.com.comeja.comejaempresas.model.Venda;
import br.com.comeja.comejaempresas.network.CustomRequest;
import br.com.comeja.comejaempresas.network.CustomRequestObject;
import br.com.comeja.comejaempresas.network.NetworkConnection;
import br.com.comeja.comejaempresas.service.FirebaseInstanceIDService;
import br.com.comeja.comejaempresas.service.FirebaseMessagingService;

public class MainActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;
    public Pessoa empresaLogada;
    private NetworkConnection networkConnection;
    private ProgressBar progressBar;
    private String url = "www.comeja.com.br";
    private List<Configuracao> mural;
    private boolean logado;
    public static Integer versao;
    private ProgressDialog pd;
    private Integer pos = -1;
    private Venda vendaSelecionada;
    private Endereco endereco;
    private Dispositivo dispositivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_main);


        empresaLogada = LoginActivity.getUsuario();
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("");
        mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);
        networkConnection = new NetworkConnection(getApplication());
        networkConnection.getRequestQueue();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        try {
            versao = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        pd = new ProgressDialog(MainActivity.this, R.style.estiloDialog);
        pd.setMessage("Processando...");

        pd.setCancelable(false);


        if (!UtilTCM.verifyConnection(getApplicationContext())) {
            exibirAlerta();

        } else {
            SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
            String temp = shared.getString("usuario", "");
            if (temp.length() > 5) {
                Gson gson = new Gson();
                empresaLogada = gson.fromJson(temp, Pessoa.class);
                logado = true;
            }

            buscarMural();

        }


        startService(new Intent(this, FirebaseMessagingService.class));
        startService(new Intent(this, FirebaseInstanceIDService.class));




        SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
        String tempD = shared.getString("dispositivo", "");
        if (tempD.length() > 5) {
            Gson gson = new Gson();
            dispositivo = gson.fromJson(tempD, Dispositivo.class);

            try {
                atualizarDispositivo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            dispositivo = new Dispositivo();
            dispositivo.setTokenDispositivo(FirebaseInstanceId.getInstance().getToken());
            dispositivo.setIdEmpresa(empresaLogada.getIdPessoa());

            try {
                atualizarDispositivo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        headerNavigationLeft = new AccountHeader()
                .withActivity(this)
                .withCompactStyle(true)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.color.red)
                .withProfileImagesVisible(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(empresaLogada.getNomeFantasiaPessoa()).withEmail(empresaLogada.getEmailPessoa())

                )
                .build();


        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(-1)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerNavigationLeft)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (pos == position) {

                        } else {
                            pos = position;
                            switch (position) {
                                case 0:
                                    abrirFragmentoProdutos();
                                    break;
                                case 1:
                                    abrirFragmentoUltimosPedidos();
                                    break;

                                case 3:
                                    abrirFragmentoEmpresa();
                                    break;
                                case 4:
                                    abrirFragmentoEnderecos();
                                    break;

                                case 5:
                                    SharedPreferences preferences = getSharedPreferences("info", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear().commit();
                                    logado = false;
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                })
                .build();


        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Produtos"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Pedidos"));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Editar Dados").withIcon(R.drawable.account));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Endereços").withIcon(R.drawable.earth));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Sair"));
    }


    public void exibirAlerta() {
        AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this, R.style.MaterialDrawerBaseTheme_Dialog);

        build.setTitle("Alerta");
        build.setMessage("Sem conexão com o servidor, tente novamente mais tarde");
        build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        build.create().show();
    }

    @Subscribe
    public void onNewToken(TokenEvent tokenEvent){
        dispositivo.setTokenDispositivo(tokenEvent.getToken());
        try {
            updateToken(dispositivo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void alterarMenu() {
        headerNavigationLeft.getProfiles().get(0).setName(empresaLogada.getNomeFantasiaPessoa());
        headerNavigationLeft.getProfiles().get(0).setName(empresaLogada.getEmailPessoa());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = navigationDrawerLeft.saveInstanceState(outState);
        headerNavigationLeft.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        if (navigationDrawerLeft.isDrawerOpen()) {
            navigationDrawerLeft.closeDrawer();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                HomeFragment frag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
                if (frag != null && frag.isVisible()) {
                    finish();

                } else {
                    fecharFragmentos();
                }
            }
        }
    }

    public void fecharFragmentos() {

        getSupportFragmentManager().getFragments().clear();
        navigationDrawerLeft.setSelection(-1);
        networkConnection.getRequestQueue().cancelAll("request");
        HomeFragment frag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
        if (frag == null) {
            frag = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "homeFrag");
            ft.commit();
        }
    }

    public void iniciarFragmentoHome() {
        HomeFragment frag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
        if (frag == null) {
            frag = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "homeFrag");
            ft.commit();
        }
    }

    public void iniciarFragVenda() {
        fecharFragmentos();
        VendaFragment frag = (VendaFragment) getSupportFragmentManager().findFragmentByTag("vendaFrag");
        if (frag == null) {
            frag = new VendaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "vendaFrag").addToBackStack(null);
            ft.commit();
        } else {
            frag = new VendaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "vendaFrag").addToBackStack(null);
            ft.commit();
        }
    }

    public void abrirFragmentoEndereco(Endereco end) {
        endereco = end;
        fecharFragmentos();
        EnderecoFragment frag = (EnderecoFragment) getSupportFragmentManager().findFragmentByTag("enderecoFrag");
        if (frag == null) {
            frag = new EnderecoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "enderecoFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "enderecoFrag").addToBackStack(null);
            ft.commit();
        }
    }

    public void abrirFragmentoEnderecos() {
        fecharFragmentos();
        EnderecosFragment frag = (EnderecosFragment) getSupportFragmentManager().findFragmentByTag("enderecosFrag");
        if (frag == null) {
            frag = new EnderecosFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "enderecosFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "enderecosFrag").addToBackStack(null);
            ft.commit();

        }
    }

    public void abrirFragmentoEmpresa() {
        fecharFragmentos();
        EmpresaFragment frag = (EmpresaFragment) getSupportFragmentManager().findFragmentByTag("empresaFrag");
        if (frag == null) {
            frag = new EmpresaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "empresaFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "empresaFrag").addToBackStack(null);
            ft.commit();

        }
    }

    public void abrirFragmentoUltimosPedidos() {
        fecharFragmentos();
        UltimosPedidosFragment frag = (UltimosPedidosFragment) getSupportFragmentManager().findFragmentByTag("ultimosPedidosFrag");
        if (frag == null) {
            frag = new UltimosPedidosFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "ultimosPedidosFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "ultimosPedidosFrag").addToBackStack(null);
            ft.commit();
        }
    }

    public void abrirFragmentoProdutos() {
        fecharFragmentos();
        ProdutosFragment frag = (ProdutosFragment) getSupportFragmentManager().findFragmentByTag("produtosFrag");
        if (frag == null) {
            frag = new ProdutosFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "produtosFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "produtosFrag").addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onStop() {
        if (logado) {
            SharedPreferences pref;
            pref = getSharedPreferences("info", MODE_PRIVATE);
            Gson gson = new Gson();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("usuario", gson.toJson(empresaLogada));
            editor.commit();

            SharedPreferences pref2;
            pref2 = getSharedPreferences("info", MODE_PRIVATE);

            SharedPreferences.Editor editor2 = pref2.edit();
            editor.putString("dispositivo", gson.toJson(dispositivo));
            editor.commit();
        }
        super.onStop();
    }


    //Buscar
    public void buscarMural() {
        exibeProgressBar();

        CustomRequest request = new CustomRequest(Request.Method.GET,
                "http://" + url + "/service/usuario/mural",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ocultaProgressBar();
                        Gson gson = new Gson();
                        mural = new ArrayList<>();
                        for (int i = 0, tamI = response.length(); i < tamI; i++) {
                            try {
                                Configuracao configuracao = new Configuracao();
                                configuracao = gson.fromJson(response.getString(i), Configuracao.class);
                                mural.add(configuracao);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        iniciarFragmentoHome();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Erro", error.toString());
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void buscarVenda(Integer id) {
        exibeProgressBar();
        CustomRequestObject request = new CustomRequestObject(Request.Method.GET,
                "http://" + url + "/service/venda/" + "porid?idVenda=" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Gson gson = new Gson();
                        vendaSelecionada = gson.fromJson(response.toString(), Venda.class);
                        iniciarFragVenda();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer venda");
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void salvaEnderecoWeb(String json) throws JSONException {
        exibeProgressBar();
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/usuario/editarEndereco", new JSONObject(json),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Log.e("Resposta", response.toString());
                        Gson gson = new Gson();
                        Endereco endereco = gson.fromJson(response.toString(), Endereco.class);
                        boolean exec = false;
                        for (int i = 0; i < getEmpresaLogada().getEnderecos().size(); i++) {
                            if (endereco.getIdEndereco().equals(getEmpresaLogada().getEnderecos().get(i).getIdEndereco())) {
                                getEmpresaLogada().getEnderecos().remove(i);
                                getEmpresaLogada().getEnderecos().add(endereco);
                                exec = true;
                            }

                        }
                        if (!exec) {
                            getEmpresaLogada().getEnderecos().add(endereco);
                        }
                        fecharFragmentos();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ocultaProgressBar();
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void atualizarUsuario(String s) throws JSONException {
        exibeProgressBar();
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/usuario/editar", new JSONObject(s),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Log.e("Resposta", response.toString());
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd")
                                .create();
                        empresaLogada = gson.fromJson(response.toString(), Pessoa.class);
                        alterarMenu();
                        fecharFragmentos();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                ocultaProgressBar();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void atualizarDispositivo() throws JSONException {
        exibeProgressBar();
        Gson gson = new Gson();
        String s = gson.toJson(dispositivo);
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/usuario/token", new JSONObject(s),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Log.e("Resposta", response.toString());
                        Gson gson = new Gson();
                        dispositivo = gson.fromJson(response.toString(), Dispositivo.class);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                ocultaProgressBar();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void finalizarVenda(Long idVenda) {

        exibeProgressBar();
        CustomRequestObject request = new CustomRequestObject(Request.Method.GET,
                "http://" + url + "/service/venda/" + "finalizarVenda?idVenda=" + idVenda,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        fecharFragmentos();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer venda");
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void aceitarVenda(Long idVenda) {

        exibeProgressBar();
        CustomRequestObject request = new CustomRequestObject(Request.Method.GET,
                "http://" + url + "/service/venda/" + "aceitarVenda?idVenda=" + idVenda,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        fecharFragmentos();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer venda");
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void recusarVenda(Long idVenda) {

        exibeProgressBar();
        CustomRequestObject request = new CustomRequestObject(Request.Method.GET,
                "http://" + url + "/service/venda/" + "recusarVenda?idVenda=" + idVenda,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        fecharFragmentos();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer venda");
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void updateToken(Dispositivo d) throws JSONException {
        Gson gson = new Gson();
        d.setIdEmpresa(empresaLogada.getIdPessoa());
        String s = gson.toJson(d);
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/usuario/token", new JSONObject(s),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Log.e("Resposta", response.toString());
                        Gson gson = new Gson();
                        dispositivo = gson.fromJson(response.toString(), Dispositivo.class);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                ocultaProgressBar();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void atualizarProduto(Long id) {

        StringRequest request = new StringRequest(Request.Method.GET, "http://" + url + "/service/produto/statusProduto?idProduto="+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();
                        fecharFragmentos();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }



    public Pessoa getEmpresaLogada() {
        return empresaLogada;
    }

    public void setEmpresaLogada(Pessoa empresaLogada) {
        this.empresaLogada = empresaLogada;
    }

    public List<Configuracao> getMural() {
        return mural;
    }

    public void setMural(List<Configuracao> mural) {
        this.mural = mural;
    }

    public void exibeProgressBar() {
        pd.show();
    }

    public void ocultaProgressBar() {
        pd.dismiss();
    }

    public Venda getVendaSelecionada() {
        return vendaSelecionada;
    }

    public void setVendaSelecionada(Venda vendaSelecionada) {
        this.vendaSelecionada = vendaSelecionada;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }



}
