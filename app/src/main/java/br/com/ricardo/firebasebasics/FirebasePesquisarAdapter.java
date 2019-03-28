package br.com.ricardo.firebasebasics;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.com.ricardo.firebasebasics.Modelo.Pessoa;

public class FirebasePesquisarAdapter extends RecyclerView.Adapter<FirebasePesquisarAdapter.FirebasePesquisarHolder> {

    private List<Pessoa> pessoaPesquisadaList;

    public FirebasePesquisarAdapter(List<Pessoa> pessoaPesquisadaList) {
        this.pessoaPesquisadaList = pessoaPesquisadaList;
    }

    @NonNull
    @Override
    public FirebasePesquisarAdapter.FirebasePesquisarHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_firebase_pesquisar, viewGroup, false);

        FirebasePesquisarHolder holder = new FirebasePesquisarHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FirebasePesquisarAdapter.FirebasePesquisarHolder viewHolder, int i) {

        Pessoa p = pessoaPesquisadaList.get(i);

        viewHolder.textPessoaPesquisada.setText(p.getNome());

    }

    @Override
    public int getItemCount() {
        return pessoaPesquisadaList.size();
    }

    public class FirebasePesquisarHolder extends RecyclerView.ViewHolder{

        private TextView textPessoaPesquisada;

        public FirebasePesquisarHolder(@NonNull View itemView) {
            super(itemView);

            textPessoaPesquisada = (TextView) itemView.findViewById(R.id.item_firebase_pesquisado);
        }
    }
}
