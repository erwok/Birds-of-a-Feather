package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SessionDao {

    @Query("SELECT * from sessions WHERE session_id=:id")
    Session get(int id);

    @Insert
    void insert(Session session);

    @Query("SELECT * from sessions")
    List<Session> getAll();

    @Query("SELECT * from sessions ORDER BY session_id DESC LIMIT 1")
    Session getLast();

    @Query("UPDATE sessions SET session_name=:name WHERE session_id=:id")
    void renameSession(int id, String name);
}
