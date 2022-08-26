package com.example.hdida.firebaseauth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mEmail;
    private String mUser;
    private String mPrice;
    private String mDate;
    private String mDesc;
    private List<String> mselectedOptions = new ArrayList<>();
    private String mstring_villes, mstring_carbu, mstring_marque, mstring_model, mstring_puissance, mstring_boite, mstring_nbporte,
            mstring_main, mstring_kilo, mstring_annee, mstring_tel;


    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl, String price, String desc, String string_villes, String string_carbu,
                  String string_marque, String string_model, String string_puissance, String string_boite, String string_nbporte, String string_main,
                  String string_kilo, String string_annee, String string_tel, List<String> selectedOptions) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mselectedOptions = selectedOptions;
        mName = name;
        mImageUrl = imageUrl;
        mEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mDesc = desc;
        mstring_villes = string_villes;
        mstring_carbu = string_carbu;
        mstring_model = string_model;
        mstring_marque = string_marque;
        mstring_puissance = string_puissance;
        mstring_boite = string_boite;
        mstring_nbporte = string_nbporte;
        mstring_marque = string_marque;
        mstring_main = string_main;
        mstring_kilo = string_kilo;
        mstring_annee = string_annee;
        mstring_tel = string_tel;
        mUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mPrice = price;
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        mDate = df.format(date);


    }
    public List<String> getSelectedOptions() {
        return mselectedOptions;
    }

    public void setSelectedOptions(List<String> selectedOptions) {
        this.mselectedOptions = selectedOptions;
    }
    public String getString_model() {
        return mstring_model;
    }

    public void setString_model(String string_model) {
        this.mstring_model = string_model;
    }

    public String getString_villes() {
        return mstring_villes;
    }

    public void setString_villes(String string_villes) {
        mstring_villes = string_villes;
    }

    public String getString_carbu() {
        return mstring_carbu;
    }

    public void setString_carbu(String string_carbu) {
        mstring_carbu = string_carbu;
    }

    public String getString_marque() {
        return mstring_marque;
    }

    public void setString_marque(String string_marque) {
        mstring_marque = string_marque;
    }

    public String getString_puissance() {
        return mstring_puissance;
    }

    public void setString_puissance(String string_puissance) {
        mstring_puissance = string_puissance;
    }

    public String getString_boite() {
        return mstring_boite;
    }

    public void setString_boite(String string_boite) {
        mstring_boite = string_boite;
    }

    public String getString_nbporte() {
        return mstring_nbporte;
    }

    public void setString_nbporte(String string_nbporte) {
        mstring_nbporte = string_nbporte;
    }

    public String getString_main() {
        return mstring_main;
    }

    public void setString_main(String string_main) {
        mstring_main = string_main;
    }

    public String getString_kilo() {
        return mstring_kilo;
    }

    public void setString_kilo(String string_kilo) {
        mstring_kilo = string_kilo;
    }

    public String getString_annee() {
        return mstring_annee;
    }

    public void setString_annee(String string_annee) {
        mstring_annee = string_annee;
    }

    public String getString_tel() {
        return mstring_tel;
    }

    public void setString_tel(String string_tel) {
        mstring_tel = string_tel;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getUserName() {
        return mUser;
    }

    public void setUserName(String userName) {
        mUser = userName;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}