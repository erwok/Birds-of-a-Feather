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

/**
 * Adapter used to create multiple session UI pages with each session's corresponding found
 * students.
 */
public class SessionViewAdapter extends RecyclerView.Adapter<SessionViewAdapter.ViewHolder>{
    private final List<Session> sessions;

    /**
     * Constructor initializing sessions to be displayed.
     *
     * @param sessions
     */
    public SessionViewAdapter(List<Session> sessions) {
        super();
        this.sessions = sessions;
    }

    /**
     * Generate individual session page.
     *
     * @param parent
     * @param viewType
     * @return
     */
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

    /**
     * @return number of found students in the session
     */
    @Override
    public int getItemCount() {
        return this.sessions.size();
    }

    /**
     * Custom UI layout for a session.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView sessionTextView;
        private Session session;

        ViewHolder(View itemView) {
            super(itemView);
            this.sessionTextView = itemView.findViewById(R.id.session_name_textview);
            itemView.setOnClickListener(this::viewSession);
        }

        /**
         * Sets data to be displayed of a particular input session.
         *
         * @param session
         */
        public void setSession(Session session) {
            this.sessionTextView.setText(session.sessionName);
            this.session = session;
        }

        /**
         * Opens the home page with the found students of the set session.
         *
         * @param view
         */
        public void viewSession(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra(HomeActivity.HOME_SESSION_ID_EXTRA, session.sessionID);
            context.startActivity(intent);
        }
    }
}
