package com.example.birdsofafeather;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.Session;

import java.util.List;

public class SessionViewAdapter extends RecyclerView.Adapter<SessionViewAdapter.ViewHolder>{
    private final List<Session> sessions;

    public SessionViewAdapter(List<Session> sessions) {
        super();
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public SessionViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.session_row, parent, false);

        return new SessionViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewAdapter.ViewHolder holder, int position) {
        holder.setSession(sessions.get(position));
    }

    @Override
    public int getItemCount() {
        return this.sessions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView sessionTextView;
        private Session session;

        ViewHolder(View itemView) {
            super(itemView);
            this.sessionTextView = itemView.findViewById(R.id.session_name_textview);
            itemView.setOnClickListener(this::viewSession);
        }

        public void setSession(Session session) {
            this.sessionTextView.setText(session.sessionName);
            this.session = session;
        }

        public void viewSession(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra(HomeActivity.HOME_SESSION_ID_EXTRA, session.sessionID);
            context.startActivity(intent);
        }
    }
}
