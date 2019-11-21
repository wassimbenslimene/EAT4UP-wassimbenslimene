package com.example.myapplication6.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.myapplication6.model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

//class satabase to write function process data base
public class Database extends SQLiteAssetHelper {
    private static final String DB_Name = "EAT4UPDB.db";
    private static final int DB_VER =1;

    public Database(Context context) {
        super(context, DB_Name, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder db1 = new SQLiteQueryBuilder();
        String[] sqlSelect = {"ProductId","ProductName" , "Quantity", "Price", "Discount"};
        String SqlTable = "OrderDetail";
        db1.setTables(SqlTable);

        Cursor c = db1.query(db, sqlSelect, null, null, null, null, null);
        final List<Order> result = new ArrayList<>();


        if (c.moveToFirst()) {
            do {
                /*result.add(new Order(
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))

                ));*/
                result.add(new Order(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4)

                ));

            } while (c.moveToNext());

        }//db.close();
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount)Values('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);


    }

    public void CleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");

        db.execSQL(query);


    }
}
