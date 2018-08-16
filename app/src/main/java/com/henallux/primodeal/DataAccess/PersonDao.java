package com.henallux.primodeal.DataAccess;


import com.auth0.android.jwt.JWT;
import com.henallux.primodeal.Exception.AuthorizationException;
import com.henallux.primodeal.Exception.BadRequestException;
import com.henallux.primodeal.Exception.NotFoundException;
import com.henallux.primodeal.Exception.ServerErrorException;
import com.henallux.primodeal.Model.LoginForm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import com.google.gson.*;
import com.henallux.primodeal.Model.Person;
import com.henallux.primodeal.Model.PersonReturnModel;
import com.henallux.primodeal.Model.TokenPersonReturnModel;
// import com.henallux.primodeal.Model.UserNamePerson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by bil on 19-11-17.
 */

public class PersonDao {

    public static TokenPersonReturnModel userToken;
    public static PersonReturnModel _user;
   // public static UserNamePerson userNamePerson;
    public static String tokenString;


    private static <T> T jsonToObject(String jsonString, Class<T> clas)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clas);
    }

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


    public PersonReturnModel login(String email, String password) throws  Exception {

        String result ="";
        Gson gson = new Gson();
        LoginForm model = new LoginForm(email,password);
        String stringJSON = gson.toJson(model);
        try{

            URL url = new URL("https://webapplicationbetterdeal20180130015708.azurewebsites.net/api/jwt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type","application/json");
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            connection.connect();



            writer.write(stringJSON.toString());
            writer.flush();
            writer.close();
            out.close();
            int code=connection.getResponseCode();
            result = getResult(connection.getInputStream());

            if(code ==200){

                userToken =  (TokenPersonReturnModel)jsonToObject(result, TokenPersonReturnModel.class);

                if(!userToken.getAccess_token().equals(""))
                {
                    tokenString = userToken.getAccess_token();
                     JWT jwt = new JWT(userToken.getAccess_token());

                     _user = getPerson(jwt.getSubject());


                }



            }

            connection.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return _user;
    }

    public PersonReturnModel getPerson(String email) throws Exception{

        URL url = new URL("https://webapplicationbetterdeal20180130015708.azurewebsites.net/api/ApplicationUsers/"+email);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer "+PersonDao.tokenString);
        connection.connect();


        String result = getResult(connection.getInputStream());
        connection.disconnect();

        _user =  (PersonReturnModel)jsonToObject(result, PersonReturnModel.class);


        return _user;

    }

    public int inscription(String userName,String password,String nameShop, String addressShop ,String status) throws Exception
    {
        int code =-1;

        Gson gson = new Gson();
        Person model;
        if(nameShop == null || nameShop.equals(""))
        {
            model = new Person(userName,password, status);
        }
        else
        {
            model = new Person(userName,password,nameShop,addressShop,status);
        }
        String stringJSON = gson.toJson(model);

        try{
            URL url = new URL("https://webapplicationbetterdeal20180130015708.azurewebsites.net/api/Account");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type","application/json");
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            connection.connect();

            writer.write(stringJSON.toString());
            writer.flush();
            writer.close();
            out.close();
            code=connection.getResponseCode();
            connection.disconnect();
            if(code ==200){
                //_user = getPerson(userName);
                 _user = this.login(userName, password);
            }
        }catch(Exception e){ /*e.printStackTrace();*/ }
        return code;
    }

    public static void set_user(PersonReturnModel _user) {
        PersonDao._user = _user;
    }

    public static void setUserToken(TokenPersonReturnModel _userToken){
        PersonDao.userToken = _userToken;
    }
}
