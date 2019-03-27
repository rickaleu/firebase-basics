package br.com.ricardo.firebasebasics;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.ricardo.firebasebasics.Modelo.Pessoa;

public class FirebaseCrudAdapter extends RecyclerView.Adapter<FirebaseCrudAdapter.FirebaseCrudHolder> {

    private List<Pessoa> pessoaList;
    private OnItemClickListener clickListener;


    public FirebaseCrudAdapter(List<Pessoa> pessoaList) {
        this.pessoaList = pessoaList;
    }

    @NonNull
    @Override
    public FirebaseCrudAdapter.FirebaseCrudHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_firebase, viewGroup, false);

        FirebaseCrudHolder holder = new FirebaseCrudHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseCrudAdapter.FirebaseCrudHolder viewHolder, int i) {

        Pessoa p = pessoaList.get(i);

        viewHolder.textItemId.setText(p.getUid());
        viewHolder.textItemNome.setText(p.getNome());
        viewHolder.textItemEmail.setText(p.getEmail());

    }

    @Override
    public int getItemCount() {
        return pessoaList.size();
    }

    public class FirebaseCrudHolder extends RecyclerView.ViewHolder{

        private TextView textItemId;
        private TextView textItemNome;
        private TextView textItemEmail;

        public FirebaseCrudHolder(@NonNull View itemView) {
            super(itemView);

            textItemId = (TextView) itemView.findViewById(R.id.text_item_firebase_id);
            textItemNome = (TextView) itemView.findViewById(R.id.text_item_firebase_nome);
            textItemEmail = (TextView) itemView.findViewById(R.id.text_item_firebase_email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickListener.onItemClick(getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }
}
