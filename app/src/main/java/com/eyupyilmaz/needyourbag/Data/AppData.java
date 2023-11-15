package com.eyupyilmaz.needyourbag.Data;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.eyupyilmaz.needyourbag.Constants.MyConstants;
import com.eyupyilmaz.needyourbag.Database.RoomDB;
import com.eyupyilmaz.needyourbag.Models.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppData extends Application {

    RoomDB database;
    String category;
    Context context;


    public static final String LAST_VERSION="LAST_VERSION";
    public static final int NEW_VERSION = 1;

    public AppData(RoomDB database) {
        this.database = database;
    }

    public AppData(RoomDB database, Context context) {
        this.database = database;
        this.context = context;
    }

    public List<Items> getBasicData(){
        category = "Basic Needs";
        List<Items> basicItem = new ArrayList<Items>();
        basicItem.clear();
        basicItem.add(new Items("Kredi Kartı",category,false));
        basicItem.add(new Items("Ehliyet",category,false));
        basicItem.add(new Items("Pasaport",category,false));
        basicItem.add(new Items("Cüzdan",category,false));
        basicItem.add(new Items("Nakit Para",category,false));
        basicItem.add(new Items("Kitap",category,false));
        basicItem.add(new Items("Harita",category,false));
        basicItem.add(new Items("Ev Anahtarı",category,false));
        basicItem.add(new Items("Kulaklık",category,false));
        return basicItem;
    }

    public List<Items> getPersonalCareData(){
        String[] data = {"Diş Fırçası","Yüz Maskesi","Güneş Kremi","Diş Macunu","Ped","Islak Mendil","Saç Kurutma Makinesi","Tırnak Makası"};
        return prepareItemsList(MyConstants.PERSONAL_CARE_CAMEL_CASE,data);
    }

    public List<Items> getClothingData(){
        String[] data = {"Çorap","İç Çamaşırı","Pantolon","T-shirt","Kapüşon","Şort","Şapka","Eldiven","Kemer","Bikini",
        "Jacket","Suit","Coat"};
        return prepareItemsList(MyConstants.CLOTHING_CAMEL_CASE,data);
    }

    public List<Items> getBabyNeedsData(){
        String[] data = {"Zıbın","Jumpsuit","Bebek Yağı","Bez","Pudra","Suluk","Oyuncak"};
        return prepareItemsList(MyConstants.BABY_NEEDS_CAMEL_CASE,data);
    }

    public List<Items> getHealthData(){
        String[] data = {"Aspirin","Vitamin","Parol","Condom","İlk Yardım Seti","Yara Bandı"};
        return prepareItemsList(MyConstants.HEALTH_CAMEL_CASE,data);
    }

    public List<Items> getTechnologyData(){
        String[] data = {"Telefon","Şarz Aleti","Tablet","Bilgisayar","Kamera","Kulaklık","Power Bank"};
        return prepareItemsList(MyConstants.TECHNOLOGY_CAMEL_CASE,data);
    }

    public List<Items> getFoodData(){
        String[] data = {"Abur-Cubur","Alkol","Sandiviç","Su","Mısır","Bebek Yemeği"};
        return prepareItemsList(MyConstants.FOOD_CAMEL_CASE,data);
    }

    public List<Items> getBeachSuppliesData(){
        String[] data = {"Deniz Gözlüğü","Güneş Kremi","Deniz Ytağı","Palet"};
        return prepareItemsList(MyConstants.BEACH_SUPPLIES_CAMEL_CASE,data);

    }

    public List<Items> getCarSuppliesData(){
        String[] data = {"Pompa","Kriko","Yedek Anahtar","Kaza Seti","Reflektör","Yedek Lastik","İlk Yardım Seti"};
        return prepareItemsList(MyConstants.CAR_SUPPLIES_CAMEL_CASE,data);
    }

    public List<Items> getNeedsData(){
        String[] data = {"Sırtı Çantası","Günlük Çanta","Çamaşır Çantası","bavul","Dikiş Seti"};
        return prepareItemsList(MyConstants.NEEDS_CAMEL_CASE,data);
    }


    public List<Items> prepareItemsList(String category,String[] data){
        List<String> list = Arrays.asList(data);
        List<Items> dataList = new ArrayList<>();
        dataList.clear();

        for (int i = 0;i<list.size();i++){
            dataList.add(new Items(list.get(i),category,false));
        }

        return dataList;
    }

    public List<List<Items>> getAllData(){
        List<List<Items>> listOfAllItems = new ArrayList<>();
        listOfAllItems.clear();
        listOfAllItems.add(getBasicData());
        listOfAllItems.add(getClothingData());
        listOfAllItems.add(getPersonalCareData());
        listOfAllItems.add(getBabyNeedsData());
        listOfAllItems.add(getHealthData());
        listOfAllItems.add(getTechnologyData());
        listOfAllItems.add(getFoodData());
        listOfAllItems.add(getBeachSuppliesData());
        listOfAllItems.add(getCarSuppliesData());
        listOfAllItems.add(getNeedsData());
        return listOfAllItems;
    }

    public void persistAllData(){
        List<List<Items>> listOfAllItems = getAllData();
        for (List<Items> list : listOfAllItems){
            for (Items items : list){
                database.mainDao().saveItem(items);
            }
        }
        System.out.println("Data Eklendi");
    }

    public void persistDataByCategory(String category, Boolean onlyDelete){
        try{
            List<Items> list = deleteAndGetListByCategory(category,onlyDelete);
            if (!onlyDelete){
                for (Items item : list){
                    database.mainDao().saveItem(item);
                }
                Toast.makeText(context, category+" Reset Başarılı", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, category+" Reset Başarılı", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, "Bir şeyler yalnış gitti", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Items> deleteAndGetListByCategory(String category,Boolean onlyDelete){
        if (onlyDelete){
            database.mainDao().deleteAllByCategoryAndAddedBy(category,MyConstants.SYSTEM_SMALL);
        }else{
            database.mainDao().deleteAllByCategory(category);
        }

        switch (category){
            case MyConstants.BASIC_NEEDS_CAMEL_CASE:
                return getBasicData();

            case MyConstants.CLOTHING_CAMEL_CASE:
                return getClothingData();

            case MyConstants.PERSONAL_CARE_CAMEL_CASE:
                return getPersonalCareData();

            case MyConstants.BABY_NEEDS_CAMEL_CASE:
                return getBabyNeedsData();

            case MyConstants.HEALTH_CAMEL_CASE:
                return getHealthData();

            case MyConstants.TECHNOLOGY_CAMEL_CASE:
                return getTechnologyData();

            case MyConstants.FOOD_CAMEL_CASE:
                return getFoodData();

            case MyConstants.BEACH_SUPPLIES_CAMEL_CASE:
                return getBeachSuppliesData();

            case MyConstants.CAR_SUPPLIES_CAMEL_CASE:
                return getCarSuppliesData();

            case MyConstants.NEEDS_CAMEL_CASE:
                return getNeedsData();

            default:
                return new ArrayList<>();
        }
    }
}
