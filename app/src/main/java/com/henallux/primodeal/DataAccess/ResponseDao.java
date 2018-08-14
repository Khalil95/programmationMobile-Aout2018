package com.henallux.primodeal.DataAccess;

import com.google.gson.Gson;
import com.henallux.primodeal.Model.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bil on 04-04-18.
 */

public class ResponseDao {

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


    public int postResponse(String response, int publicationId) throws Exception{
        int code = 0;
        Gson gson = new Gson();
        Response model = new Response(response,publicationId, PersonDao._user.id);
        String stringJSON = gson.toJson(model);
        try{
            URL url = new URL("https://webapplicationbetterdeal20180130015708.azurewebsites.net/api/responses");
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

}
