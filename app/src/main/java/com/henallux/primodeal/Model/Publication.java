package com.henallux.primodeal.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bil on 26-02-18.
 */

public class Publication implements Serializable {

    private String title, description, applicationUserId;
    private Integer id;
    private PersonReturnModel applicationUser;
    private List<Response> responses;

    public Publication(String title, String description){

    }

    public Publication(Integer id, String title, String description, PersonReturnModel applicationUser, List<Response> responses){
        this.id = id;
        this.title = title;
        this.description = description;
        this.applicationUser = applicationUser;
        this.responses = responses;
    }

    public Publication(String title, String description, String applicationUserId){
        this.title = title;
        this.description = description;
        this.applicationUserId = applicationUserId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PersonReturnModel getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(PersonReturnModel applicationUser) {
        this.applicationUser = applicationUser;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public List<Response> getResponses() {
        return responses;
    }
}
