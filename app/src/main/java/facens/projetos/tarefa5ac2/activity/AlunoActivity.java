package facens.projetos.tarefa5ac2.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import facens.projetos.ac2.R;
import facens.projetos.tarefa5ac2.cep.Cep;
import facens.projetos.tarefa5ac2.api.AlunoService;
import facens.projetos.tarefa5ac2.api.ApiClient;
import facens.projetos.tarefa5ac2.model.Aluno;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoActivity extends AppCompatActivity {
    Button btnSalvar, searchButton;
    AlunoService apiService;
    TextView txtra, txtnome, txtcep, txtlogradouro, txtbairro, txtcidade, txtcomplemento, txtuf;
    int id;

    private void inserirAluno(Aluno aluno) {
        Call<Aluno> call = apiService.postUsuario(aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {

                    Aluno createdPost = response.body();
                    Toast.makeText(AlunoActivity.this, "Inserido Com Sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Log.e("Inserir", "Falha ao criar " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {

                Log.e("Inserir", "Falha ao criar " + t.getMessage());
            }
        });
    }

    private void editarAluno(Aluno aluno) {
        Call<Aluno> call = apiService.putAluno(id,aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {

                    Aluno createdPost = response.body();
                    Toast.makeText(AlunoActivity.this, "Alterado Com Sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Log.e("Editar", "Falhou ao Editar. " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {

                Log.e("Editar", "Falhou ao Editar. " + t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        apiService = ApiClient.getAlunoService();
        txtra = findViewById(R.id.txtRA);
        txtnome = findViewById(R.id.txtNome);
        txtcep = findViewById(R.id.txtCEP);
        txtlogradouro = findViewById(R.id.txtLogradouro);
        txtcomplemento = findViewById(R.id.txtComplemento);
        txtbairro = findViewById(R.id.txtBairro);
        txtcidade = findViewById(R.id.txtCidade);
        txtuf = findViewById(R.id.txtUf);
        searchButton = findViewById(R.id.searchButton);
        id = getIntent().getIntExtra("id", 0);
        if (id > 0) {
            apiService.getAlunoPorId(id).enqueue(new Callback<Aluno>() {
                @Override
                public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                    if (response.isSuccessful()) {
                        txtra.setText(response.body().getRa());
                        txtnome.setText(response.body().getNome());
                        txtcep.setText(response.body().getCep());
                        txtlogradouro.setText(response.body().getLogradouro());
                        txtcomplemento.setText(response.body().getComplemento());
                        txtbairro.setText(response.body().getBairro());
                        txtcidade.setText(response.body().getCidade());
                        txtuf.setText(response.body().getUf());
                    }
                }

                @Override
                public void onFailure(Call<Aluno> call, Throwable t) {
                    Log.e("Obter usuario", "Falha ao obter usuario");
                }
            });
        }
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aluno aluno = new Aluno();
                aluno.setRa(Integer.parseInt(txtra.getText().toString()));
                aluno.setNome(txtnome.getText().toString());
                aluno.setCep(txtcep.getText().toString());
                aluno.setLogradouro(txtlogradouro.getText().toString());
                aluno.setComplemento(txtcomplemento.getText().toString());
                aluno.setBairro(txtbairro.getText().toString());
                aluno.setCidade(txtcidade.getText().toString());
                aluno.setUf(txtuf.getText().toString());
                if (id == 0)
                    inserirAluno(aluno);
                else {
                    aluno.setRa(id);
                    editarAluno(aluno);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = txtcep.getText().toString();
                if (cep.isEmpty()) {
                    Toast.makeText(AlunoActivity.this, "Informe o CEP", Toast.LENGTH_LONG).show();
                    return;
                }

                Cep.getEnderecoPorCep(cep, new Cep.CepResponseListener() {
                    @Override
                    public void onCepResponse(JsonObject endereco) {
                        txtlogradouro.setText(endereco.get("logradouro").getAsString());
                        txtcomplemento.setText(endereco.has("complemento") ? endereco.get("complemento").getAsString() : "");
                        txtbairro.setText(endereco.get("bairro").getAsString());
                        txtcidade.setText(endereco.get("localidade").getAsString());
                        txtuf.setText(endereco.get("uf").getAsString());
                    }

                    @Override
                    public void onCepError(String error) {
                        Toast.makeText(AlunoActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }}