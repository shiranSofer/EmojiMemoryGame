package com.shira.emojimemorygame;

public class Record implements Comparable<Record>{
    public static int id;
    private String playerName;
    private int time;
    private String address;

    public Record(String playerName, int time, String address, int id) {
        this.playerName = playerName;
        this.time = time;
        this.address = address;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int compareTo(Record record) {
        if (record == null){
            return 1;
        }
        if(record.equals(this)){
            return 0;
        }else if( record.time > this.time){
            return -1;
        }else if(record.time < this.time){
            return 1;
        }else {
            if (this.id > record.id) {
                return 1;
            } else if (this.id < record.id) {
                return -1;
            }
        }
        return 0;
    }
    @Override
    public String toString() {
        return this.playerName + " " + this.time + " " + this.address + ".\n";
    }
}
