package com.example.acer.currencyapplication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
public class Singleton {
    private static Singleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private Singleton(Context context){
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Singleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new Singleton(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
