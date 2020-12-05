package com.example.mobiledatacolection.model;

import android.text.Editable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mobiledatacolection.utils.AESUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String company;

    public User(String username, String password, String company) {
        this.company = company;
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCompany() {
        return company;
    }

    public static List<User> parseUserData(JSONArray users) {
        List<User> userList = new ArrayList<User>();
        try {

            if (users != null) {
                for (int i = 0; i < users.length(); i++) {
                    String username = users.getJSONObject(i).getString("username");
                    String password = users.getJSONObject(i).getString("password");
                    String company = users.getJSONObject(i).getString("company");
                    //the value of progress is a placeholder here....
                    User user = new User(username, AESUtils.encrypt(password), company);
                    userList.add(user);
                    Log.v("bookList", "Username " + username + "password " + AESUtils.encrypt(password) + "company " + company);
                }

            }

        } catch(JSONException e) {
            Log.e("CatalogClient", "unexpected JSON exception", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        User u = (User) obj;
        return u.username.equals(username) && u.password.equals(password) && u.company.equals(company);
    }
}
