package com.example.irvin.trucktransport.networking;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IvanX on 16.4.2017..
 */

public class VolleyTask {

    public static final String TAG = VolleyTask.class.getSimpleName();

    public static final String QUERYTAG = "queryTypeTT";

    private RequestQueue mRequestQueue;
    private static VolleyTask sInstance;

    private static final int TIMEOUT = 5 * 2500; //volley default is only 2500ms, not suitable
    // for POST requests

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    // A static block that sets class fields
    static {
        sInstance = new VolleyTask();
    }

    private VolleyTask() {
    }

    /**
     * Returns the VolleyTask object
     *
     * @return The global PhotoManager object
     */
    public static VolleyTask getInstance() {
        return sInstance;
    }

    public <T> void addToRequestQueue(Context context, Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(context).add(req);
    }

    public <T> void addToRequestQueue(Context context, Request<T> req) {
        req.setTag(TAG);
        getRequestQueue(context).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void addQuery(Context context, final
    DataDownloadedListener dataDownloadedListener, final QueryBundle queryBundle) {
        StringRequest strReq = new StringRequest(Request.Method.POST, queryBundle.url + "?" + System
                .currentTimeMillis(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ResultBundle resultBundle = new ResultBundle();
                resultBundle.setResult(response.trim());
                dataDownloadedListener.dataDownloaded(queryBundle.query_TYPE, resultBundle);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ResultBundle resultBundle = new ResultBundle();
                resultBundle.setSuccess(false);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    resultBundle.setResult("Time out has occurred");
                } else if (error instanceof AuthFailureError) {
                    resultBundle.setResult("AuthFailureError has occurred");
                } else if (error instanceof ServerError) {
                    resultBundle.setResult("ServerError has occurred");
                } else if (error instanceof NetworkError) {
                    resultBundle.setResult("NetworkError has occurred");
                } else if (error instanceof ParseError) {
                    resultBundle.setResult("ParseError has occurred");
                }
                dataDownloadedListener.onErrorLoading(queryBundle.query_TYPE, resultBundle);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<String, String>();

                Set<String> keySet = (Set<String>) queryBundle.params.keySet();
                ArrayList<String> keyList = new ArrayList<String>();
                for (String key : keySet) {
                    keyList.add(key);
                }
                Collection<String> values = queryBundle.params.values();
                ArrayList<String> valuesList = new ArrayList<String>();
                for (Object valuestr : values) {
                    valuesList.add((String) valuestr);
                }
                for (int i = 0; i < queryBundle.params.size(); i++) {
                    String keyname = keyList.get(i);
                    String value = valuesList.get(i);
                    map.put(keyname, value);
                }
                return map;
            }
        };
        strReq.setShouldCache(true);
        strReq.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, DefaultRetryPolicy
                .DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        VolleyTask.getInstance().addToRequestQueue(context, strReq);
    }

    public static void addTask(Context context, final
    DataDownloadedListener dataDownloadedListener, final QueryBundle queryBundle){
        StringRequest strReq = new StringRequest(Request.Method.GET,
                queryBundle.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ResultBundle resultBundle = new ResultBundle();
                resultBundle.setResult(response.trim());
                //resultBundle.setSuccess(true);
                dataDownloadedListener.dataDownloaded(queryBundle.query_TYPE, resultBundle);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ResultBundle resultBundle = new ResultBundle();
                resultBundle.setSuccess(false);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    resultBundle.setResult("Time out has occurred");
                } else if (error instanceof AuthFailureError) {
                    resultBundle.setResult("AuthFailureError has occurred");
                } else if (error instanceof ServerError) {
                    resultBundle.setResult("ServerError has occurred");
                } else if (error instanceof NetworkError) {
                    resultBundle.setResult("NetworkError has occurred");
                } else if (error instanceof ParseError) {
                    resultBundle.setResult("ParseError has occurred");
                }
                dataDownloadedListener.onErrorLoading(queryBundle.query_TYPE, resultBundle);
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<String, String>();

                Set<String> keySet = (Set<String>) queryBundle.params.keySet();
                ArrayList<String> keyList = new ArrayList<String>();
                for (String key : keySet) {
                    keyList.add(key);
                }
                Collection<String> values = queryBundle.params.values();
                ArrayList<String> valuesList = new ArrayList<String>();
                for (Object valuestr : values) {
                    valuesList.add((String) valuestr);
                }
                for (int i = 0; i < queryBundle.params.size(); i++) {
                    String keyname = keyList.get(i);
                    String value = valuesList.get(i);
                    map.put(keyname, value);
                }
                return map;
            }
        };
        strReq.setShouldCache(true);
        strReq.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, DefaultRetryPolicy
                .DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        VolleyTask.getInstance().addToRequestQueue(context, strReq);
    }

    public static void errorHandler(String msg, QueryType queryType, DataDownloadedListener
            dataDownloadedListener) {
        ResultBundle bundle = new ResultBundle();
        bundle.setResult(msg);
        bundle.setSuccess(false);
        dataDownloadedListener.onErrorLoading(queryType, bundle);
    }

}