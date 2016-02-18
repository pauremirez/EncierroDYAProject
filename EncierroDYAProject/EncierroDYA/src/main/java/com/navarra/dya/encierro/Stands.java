package com.navarra.dya.encierro;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class Stands {

    private int id;
    private String description;

    public Stands(int id, String description){
        this.id = id;
        this.description = description;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public String getDescription(){
        return this.description;
    }

}