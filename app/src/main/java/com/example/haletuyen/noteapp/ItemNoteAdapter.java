package com.example.haletuyen.noteapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haletuyen.noteapp.activity.MainActivity;
import com.example.haletuyen.noteapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class ItemNoteAdapter extends RecyclerView.Adapter<ItemNoteAdapter.RecyclerViewHolder> {

    private List<Note> list = new ArrayList<>();
    private Context context;

    public ItemNoteAdapter(Context context, List<Note> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        Note note = list.get(position);
        viewHolder.tvTitle.setText(note.getTitle());
        viewHolder.tvContent.setText(note.getContent());
        viewHolder.tvLastModified.setText(note.getLastModified());
    }

    public void addItem(int position, Note note) {
        list.add(position, note);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public LinearLayout container;
        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvLastModified;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            container = (LinearLayout) itemView.findViewById(R.id.item_container);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvLastModified = (TextView) itemView.findViewById(R.id.tv_last_modified);

            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MainActivity.showNote(context, list.get(getPosition()).getId());
        }
    }
}