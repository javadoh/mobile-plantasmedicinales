package com.javadoh.plantasmedicinalesnaturales.io.beans;

import java.io.Serializable;

/**
 * Created by luiseliberal on 17/07/16.
 */
public class LocationFaceBean implements Serializable{

    private String id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
