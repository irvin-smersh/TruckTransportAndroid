package com.example.irvin.trucktransport.model;

import com.example.irvin.trucktransport.enums.QueryType;

import java.util.Map;

/**
 * Created by IvanX on 16.4.2017..
 */

public class QueryBundle {

    public String url;
    public QueryType query_TYPE;
    public Map<String, String> params;

    // public LOGIN_TYPE login_TYPE;
    public QueryBundle(String url, QueryType query_TYPE, Map<String, String> params) {
        super();
        this.url = url;
        this.query_TYPE = query_TYPE;
        this.params = params;
        // this.login_TYPE = login_TYPE;
    }

}