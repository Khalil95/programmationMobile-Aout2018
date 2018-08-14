package com.henallux.primodeal.Model;

import java.io.Serializable;

/**
 * Created by bil on 03-04-18.
 */

public class Response implements Serializable {

    public String response, applicationUserId;
    public Integer id, publicationId;

    public PersonReturnModel applicationUser;
    public Publication publication;

    public Response(String response, Integer publicationId, String applicationUserId){
        this.response = response;
        this.publicationId = publicationId;
        this.applicationUserId = applicationUserId;
    }

    public Response(Integer id, String response, Integer publicationId, String applicationUserId, Publication publication, PersonReturnModel applicationUser){
        this.id = id;
        this.response = response;
        this.publicationId = publicationId;
        this.applicationUserId = applicationUserId;
        this.publication = publication;
        this.applicationUser = applicationUser;
    }

    public String getResponse() {
        return response;
    }

    public String getApplicationUserId() {
        return applicationUserId;
    }
}
