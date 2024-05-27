package facens.projetos.tarefa5ac2.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import facens.projetos.ac2.R;
import facens.projetos.tarefa5ac2.activity.AlunoActivity;
import facens.projetos.tarefa5ac2.api.AlunoService;
import facens.projetos.tarefa5ac2.api.ApiClient;
import facens.projetos.tarefa5ac2.model.Aluno;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoHolder> {
    private final List<Aluno> alunos;
    Context context;

    private void removerItem(int position) {
        int id = alunos.get(position).getId();
        AlunoService apiService = ApiClient.getAlunoService();
        Call<Void> call = apiService.deleteAluno(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    alunos.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, alunos.size());
                    Toast.makeText(context, "Sucesso ao Excluir", Toast.LENGTH_SHORT).show();
                } else {

                    Log.e("Exclusão","Falha ao Excluir");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Log.e("Exclusão","Falha ao Excluir");
            }
        });
    }

    private void editarItem(int position) {
        int id = alunos.get(position).getRa();
        Intent i = new Intent(context, AlunoActivity.class);
        i.putExtra("id",id);
        context.startActivity(i);
    }

    public AlunoAdapter(List<Aluno> alunos, Context context) {
        this.alunos = alunos;
        this.context = context;
    }
    @Override
    public AlunoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlunoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_listagem, parent, false));
    }
    @Override
    public void onBindViewHolder(AlunoHolder holder, int position) {
        holder.ra.setText(alunos.get(position).getId() + " - " +alunos.get(position).getRa());
        holder.nome.setText(alunos.get(position).getNome());
        holder.cep.setText(alunos.get(position).getCep());
        holder.logradouro.setText(alunos.get(position).getLogradouro());
        holder.complemento.setText(alunos.get(position).getComplemento());
        holder.bairro.setText(alunos.get(position).getBairro());
        holder.cidade.setText(alunos.get(position).getCidade());
        holder.uf.setText(alunos.get(position).getUf());
        holder.btnexcluir.setOnClickListener(view -> removerItem(position));
        holder.btnEditar.setOnClickListener(view -> editarItem(position));
    }
    @Override
    public int getItemCount() {
        return alunos != null ? alunos.size() : 0;
    }
}
