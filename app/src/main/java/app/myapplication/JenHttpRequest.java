package app.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JenHttpRequest {

    /**
     * type 1 = GET
     * type 2 = POST
     * type 3 = DELETE
     * */

    public static final int GET_REQUEST = 1;
    public static final int POST_REQUEST = 2;
    public static final int DELETE_REQUEST = 3;

    public JSONObject response = null;

    public JenHttpRequest(int type, String url, Intent intent){
        if( type == JenHttpRequest.GET_REQUEST ){
            sendGetRequest(url);
        }else if( type == JenHttpRequest.POST_REQUEST ){
            response = sendPostRequest(url, intent);
        }else if( type == JenHttpRequest.DELETE_REQUEST ){
            response = sendDeleteRequest(url);
        }
    }

    public String createJson( Bundle extras ){
        JSONObject obj = new JSONObject();
        for(String key: extras.keySet()){
            Object value = extras.get(key);
            try {
                obj.put(key, value.toString());
            } catch (JSONException e) {
                Log.e("test", e.getMessage());
            }
        }
        return obj.toString();
    }

    public JSONObject decodeJsonString(String json){
        Log.e("jsonResp", json);
        try {
            JSONObject jo = new JSONObject(json);
            return jo;
        } catch (JSONException e) {
            Log.e("decodeExp", e.getLocalizedMessage());
        }
        return null;
    }

    // send POST request
    public JSONObject sendPostRequest(String url, Intent intent){
        Bundle extras = intent.getExtras();

        try {
            String obj = createJson(extras);

            Log.e("post", "trying...");
            Log.e("test", "length: "+obj.toString().length());
            StringEntity entity = new StringEntity(obj.toString());
            entity.setContentEncoding(HTTP.UTF_8);
            entity.setContentType("application/json");

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("Accept", "application/json");
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity _entity = response.getEntity();
            InputStream is = _entity.getContent();

            return decodeJsonString(readInputStreamAsString(is));
        }catch (Exception e){
            Log.e("test", e.getMessage());
        }
        return null;
    }


    // send GET request
    public JSONObject sendGetRequest(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                httpget.addHeader("Content-Type", "application/json");
                httpget.addHeader("Accept", "application/json");

                try {
                    Log.e("get", "trying...");
                    HttpResponse _response = httpclient.execute(httpget);
                    HttpEntity _entity = _response.getEntity();
                    InputStream is = _entity.getContent();
                    Log.e("inputStream", is.toString());

                    response = decodeJsonString(readInputStreamAsString(is));
                }catch (IOException e) {
                    Log.e("sendGet", "IOExcp=" + e.getMessage());
                }
            }
        }).start();

        return null;
    }


    // send DELETE request
    public JSONObject sendDeleteRequest(String url){
        try {
            Log.e("delete", "trying...");

            HttpClient httpclient = new DefaultHttpClient();
            HttpDelete httpdelete = new HttpDelete(url);
            httpdelete.addHeader("Content-Type", "application/json");
            httpdelete.addHeader("Accept", "application/json");

            HttpResponse response = httpclient.execute(httpdelete);
            HttpEntity _entity = response.getEntity();
            InputStream is = _entity.getContent();

            return decodeJsonString(readInputStreamAsString(is));
        }catch (Exception e){
            Log.e("test", e.getMessage());
        }
        return null;
    }


    public static String readInputStreamAsString(InputStream in) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            byte b = (byte)result;
            buf.write(b);
            result = bis.read();
        }
        Log.e("response", buf.toString());
        return buf.toString();
    }


}
