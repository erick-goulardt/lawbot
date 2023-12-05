package com.example.lawbot.activities.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lawbot.R;
import com.example.lawbot.activities.dtos.ProcessosResponse;

import java.util.List;

public class ProcessosAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private Context context;
    private List<ProcessosResponse> processosResponseList;
    private Dialog dialog;

    public ProcessosAdapter(Context context, List<ProcessosResponse> list) {
        this.context = context;
        this.processosResponseList = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ultimoEvento.setText(processosResponseList.get(position).getStatus());
        holder.numeroProcesso.setText(processosResponseList.get(position).getNumProcesso());
        holder.cardProcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView tituloModal = dialog.findViewById(R.id.titulomodal);
                TextView dataAtualizacao = dialog.findViewById(R.id.dataatualizacao);
                TextView assunto = dialog.findViewById(R.id.assunto);
                TextView descricaoProcesso = dialog.findViewById(R.id.descricaoprocesso);
                TextView descricao = dialog.findViewById(R.id.descricao);
                TextView data = dialog.findViewById(R.id.data);
                TextView tituloData = dialog.findViewById(R.id.titulodata);

                ProcessosResponse processosResponse = processosResponseList.get(holder.getAdapterPosition());

                tituloModal.setText("Informação Processual");
                dataAtualizacao.setText("Última Atualização");
                assunto.setText(processosResponse.getAssunto());
                descricaoProcesso.setText("Descrição do Processo");
                descricao.setText(processosResponse.getDescricao());
                if (TextUtils.isEmpty(processosResponse.getDescricao())) {
                    descricao.setText("Você não possui atualizações ou descrições!");
                } else {
                    descricao.setText(processosResponse.getDescricao());
                }
                data.setText(processosResponse.getDataAtualizacao());
                tituloData.setText("Assunto do Processo");
                dialog.show();

                ImageView closeButton = dialog.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return processosResponseList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    TextView numeroProcesso, ultimoEvento;
    CardView cardProcesso;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        cardProcesso = itemView.findViewById(R.id.cardview);
        numeroProcesso = itemView.findViewById(R.id.numprocesso);
        ultimoEvento = itemView.findViewById(R.id.ultimoevento);
    }
}
