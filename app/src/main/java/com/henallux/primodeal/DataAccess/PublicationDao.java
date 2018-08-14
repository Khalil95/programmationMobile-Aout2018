package com.henallux.primodeal.DataAccess;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henallux.primodeal.Exception.AuthorizationException;
import com.henallux.primodeal.Exception.BadRequestException;
import com.henallux.primodeal.Exception.NotFoundException;
import com.henallux.primodeal.Exception.ServerErrorException;
import com.henallux.primodeal.Model.Publication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bil on 26-02-18.
 */

public class PublicationDao {

    private static String getResult(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder result = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        return result.toString();
    }

    public List<Publication> Get() throws Exception {
        URL url = new URL("https://webapplicationbetterdeal20180130015708.azurewebsites.net/api/publications");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer "+PersonDao.tokenString);
        connection.connect();


        String result = getResult(connection.getInputStream());
        connection.disconnect();

        parseStatusCode(connection);

        Type listType = new TypeToken<List<Publication>>(){}.getType();
        List<Publication> list = new Gson().fromJson(result, listType);


        return list;
    }

    public Publication Get(Integer idPublication) throws Exception {
        URL url = new URL("https://webapplicationbetterdeal20180130015708.azurewebsites.net/api/publications/"+idPublication);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer "+PersonDao.tokenString);

        connection.connect();
        parseStatusCode(connection);

        String result = getResult(connection.getInputStream());
        connection.disconnect();

        Type type = new TypeToken<Publication>(){}.getType();
        Publication publication = new Gson().fromJson(result, type);

        return publication;
    }

    public String deletePublication(Integer idPublication) throws Exception{
        URL url = new URL("https://webapplicationbetterdeal20180130015708.azurewebsites.net/api/publications/"+idPublication);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer "+PersonDao.tokenString);

        connection.connect();

        parseStatusCode(connection);

        String result = getResult(connection.getInputStream());
        connection.disconnect();

        return result;
    }

    public int postPublication(String title, String description, Integer yes, Integer no, Integer dontknow) throws Exception{
        int code = 0;
        Gson gson = new Gson();
        Publication model = new Publication(title,description, PersonDao._user.id);
        String stringJSON = gson.toJson(model);

        try{
        URL url = new URL("https://webapplicationbetterdeal20180130015708.azurewebsites.net/api/publications");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer "+PersonDao.tokenString);

        connection.setDoOutput(true);
        OutputStream out = connection.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out);
        //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        connection.connect();

        writer.write(stringJSON.toString());
        writer.flush();
        writer.close();
        out.close();
        code=connection.getResponseCode();
        String result = getResult(connection.getInputStream());
        connection.disconnect();}
        catch(Exception e){

        }

        return code;
    }

    private static void parseStatusCode(HttpURLConnection connection) throws Exception {
        int code = connection.getResponseCode();
        switch (code){
            case 400:
                // Parse model state errors
                String err = getResult(connection.getErrorStream());
                JSONObject jobj = new JSONObject(err);
                if(jobj.has("ModelState")) {
                    String msg = "";
                    JSONObject state = jobj.getJSONObject("ModelState");
                    Iterator it = state.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        JSONArray arr = state.getJSONArray(key);
                        for (int i = 0; i < arr.length(); i++) {
                            msg += arr.get(i) + " ";
                        }
                    }
                    throw new BadRequestException(msg);
                } else if (jobj.has("Message")){
                    throw new BadRequestException(jobj.getString("Message"));
                } else {
                    throw new BadRequestException(err);
                }
            case 401:
                throw new AuthorizationException();
            case 404:
                throw new NotFoundException();
            case 200:
                break;
            case 201:
                break;
            default:
                throw new ServerErrorException();
        }
    }

}
