package com.preciousumeha.globallotto.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_results")
public class Results {
    @ColumnInfo
    private String game_type,game_number,played_no,game_id,game_date,game_stack,game_result;
    @ColumnInfo
    private String name,game_status;

    public String getGame_status() {
        return game_status;
    }

    public void setGame_status(String game_status) {
        this.game_status = game_status;
    }
    public String getGame_result() {
        return game_result;
    }

    public void setGame_result(String game_result) {
        this.game_result = game_result;
    }

    public String getGame_date() {
        return game_date;
    }

    public String getGame_stack() {
        return game_stack;
    }

    public void setGame_stack(String game_stack) {
        this.game_stack = game_stack;
    }

    public void setGame_date(String game_date) {
        this.game_date = game_date;
    }

    @PrimaryKey(autoGenerate = true)
    private Long id;

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public String getGame_number() {
        return game_number;
    }

    public void setGame_number(String game_number) {
        this.game_number = game_number;
    }

    public String getPlayed_no() {
        return played_no;
    }

    public void setPlayed_no(String played_no) {
        this.played_no = played_no;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
