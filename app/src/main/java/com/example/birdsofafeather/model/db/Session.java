package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity(tableName = "sessions")
public class Session {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "session_id")
    public int sessionID = 0;

    @ColumnInfo(name = "session_name")
    public String sessionName;

    /**
     * Create a session, with the name defaulting to the current date and time.
     */
    public Session() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yy H:mm");
        this.sessionName = dtf.format(LocalDateTime.now());
    }
}
