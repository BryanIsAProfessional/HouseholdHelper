package com.example.householdhelper.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final int Version = 1;

    public static final String DATABASE_NAME = "HelperDatabase.db";

    public static final String LISTS_TABLE = "lists_table";
    public static final String[] LISTS_TABLE_COLS = {"ID", "NAME", "DATE_CREATED", "LAST_MODIFIED"};

    public static final String LIST_ITEMS_TABLE = "list_items_table";
    public static final String[] LIST_ITEMS_TABLE_COLS = {"ID", "LIST_ID", "NAME", "STATE", "RECIPE_ITEM_ID"};

    public static final String MEASUREMENTS_TABLE = "measurements_table";
    public static final String[] MEASUREMENT_TABLE_COLS = {"ID", "TABLE_NAME", "TABLE_ID", "NAME", "AMOUNT"};

    public static final String MEASUREMENT_TYPES_TABLE = "measurement_types_table";
    public static final String[] MEASUREMENT_TYPES_TABLE_COLS = {"ID", "NAME"};

    public static final String RECIPES_TABLE = "recipes_table";
    public static final String[] RECIPES_TABLE_COLS = {"ID", "NAME", "DATE_CREATED", "LAST_MADE", "PREP_TIME", "COOK_TIME", "AUTHOR", "SERVINGS", "IMAGE_LINK", "CATEGORIES", "INSTRUCTION_CATEGORIES", "IMAGE_ROTATION"};

    public static final String RECIPE_ITEMS_TABLE = "recipe_items_table";
    public static final String[] RECIPE_ITEMS_TABLE_COLS = {"ID", "NAME", "RECIPE_ID", "INGREDIENT_ID", "MEASUREMENT_ID", "CATEGORY", "SORT_ORDER"};

    public static final String INSTRUCTIONS_TABLE = "instructions_table";
    public static final String[] INSTRUCTIONS_TABLE_COLS = {"ID", "ITEM_TEXT", "RECIPE_ID", "CATEGORY", "SORT_ORDER"};

    public static final String INGREDIENTS_TABLE = "ingredients_table";
    public static final String[] INGREDIENTS_TABLE_COLS = {"ID", "NAME"};

    public static final String INGREDIENT_UNIT_TABLE = "ingredient_unit_table";
    public static final String[] INGREDIENT_UNIT_TABLE_COLS = {"ID", "INGREDIENT_ID", "MEASUREMENT_ID", "COST"};

    public static final String QUICK_ADD_TABLE = "quick_add_table";
    public static final String[] QUICK_ADD_TABLE_COLS = {"ID", "NAME", "LAST_USED"};

    public static final String MEDS_TABLE = "meds_table";
    public static final String[] MEDS_TABLE_COLS = {"ID", "NAME", "REMAINING", "TOTAL", "HOURS_BETWEEN", "AUTOMATIC", "LAST_CHANGED", "NOTIFY_AT", "DAYS_IN_ADVANCE"};

    private final Context context;
    private SQLiteDatabase db;


    public DatabaseHelper(Context ct){
        super(ct, DATABASE_NAME, null, Version);
        context = ct;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + LISTS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT UNIQUE, DATE_CREATED TEXT, LAST_MODIFIED TEXT)");
        db.execSQL("create table " + LIST_ITEMS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, LIST_ID TEXT, NAME TEXT, STATE INTEGER, RECIPE_ITEM_ID INTEGER)");
        db.execSQL("create table " + MEASUREMENTS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TABLE_NAME TEXT, TABLE_ID INTEGER, NAME TEXT, AMOUNT TEXT)");
        db.execSQL("create table " + MEASUREMENT_TYPES_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT UNIQUE)");
        db.execSQL("create table " + RECIPES_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DATE_CREATED TEXT, LAST_MADE TEXT, PREP_TIME INTEGER, COOK_TIME INTEGER, AUTHOR TEXT, SERVINGS INTEGER, IMAGE_LINK TEXT, CATEGORIES TEXT, INSTRUCTION_CATEGORIES TEXT, IMAGE_ROTATION INTEGER)");
        db.execSQL("create table " + RECIPE_ITEMS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, RECIPE_ID INTEGER, INGREDIENT_ID INTEGER, MEASUREMENT_ID INTEGER, CATEGORY TEXT, SORT_ORDER INTEGER)");
        db.execSQL("create table " + INSTRUCTIONS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_TEXT TEXT, RECIPE_ID INTEGER, CATEGORY TEXT, SORT_ORDER INT)");
        db.execSQL("create table " + INGREDIENTS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT UNIQUE)");
        db.execSQL("create table " + INGREDIENT_UNIT_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, INGREDIENT_ID INTEGER, MEASUREMENT_ID INTEGER, COST TEXT)");
        db.execSQL("create table " + QUICK_ADD_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT UNIQUE, LAST_USED TEXT)");
        db.execSQL("create table " + MEDS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT UNIQUE, REMAINING INTEGER, TOTAL INTEGER, HOURS_BETWEEN INTEGER, AUTOMATIC INTEGER, LAST_CHANGED TEXT, NOTIFY_AT TEXT, DAYS_IN_ADVANCE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + LISTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LIST_ITEMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEASUREMENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEASUREMENT_TYPES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RECIPES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INSTRUCTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RECIPE_ITEMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INGREDIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INGREDIENT_UNIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + QUICK_ADD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEDS_TABLE);
        onCreate(db);
    }

    // inserts
    public String insertList(String name){
        SQLiteDatabase db = getWritableDatabase();
        String now = getTime();

        ContentValues values = new ContentValues();
        values.put(LISTS_TABLE_COLS[1], name);
        values.put(LISTS_TABLE_COLS[2], now);
        values.put(LISTS_TABLE_COLS[3], now);
        
        return String.valueOf(db.insert(LISTS_TABLE, null, values));
    }

    public String insertListItem(String list_id, String name, @Nullable String recipe_item_id){
        SQLiteDatabase db = getWritableDatabase();
        String rId = recipe_item_id;
        if(recipe_item_id.isEmpty()){
            rId = "-1";
        }

        ContentValues values = new ContentValues();
        values.put(LIST_ITEMS_TABLE_COLS[1], list_id);
        values.put(LIST_ITEMS_TABLE_COLS[2], name);
        values.put(LIST_ITEMS_TABLE_COLS[3], 0);
        values.put(LIST_ITEMS_TABLE_COLS[4], rId);

        return String.valueOf(db.insert(LIST_ITEMS_TABLE, null, values));
    }

    public String insertMeasurementType(String name){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MEASUREMENT_TYPES_TABLE_COLS[1], name);

        return String.valueOf(db.insert(MEASUREMENTS_TABLE, null, values));
    }

    public String insertMeasurement(Table table_name, String table_id, String name, String amount){
        SQLiteDatabase db = getWritableDatabase();

        //TODO: get measurement types from database
//        String measurement_type_id;
//        Cursor ret = db.rawQuery("select id from " + MEASUREMENT_TYPES_TABLE + " where name = ?", new String[]{name});
//        if(ret.moveToFirst()){
//            measurement_type_id = ret.getString(ret.getColumnIndex(MEASUREMENT_TABLE_COLS[0]));
//        }else{
//            measurement_type_id = insertMeasurementType(name);
//        }

        ContentValues values = new ContentValues();

        switch(table_name){
            case RECIPE:
                values.put(MEASUREMENT_TABLE_COLS[1], Table.RECIPE.name());
                break;
            case LIST:
                values.put(MEASUREMENT_TABLE_COLS[1], Table.LIST.name());
                break;
            case INGREDIENT:
                values.put(MEASUREMENT_TABLE_COLS[1], Table.INGREDIENT.name());
                break;
            default:
        }

        values.put(MEASUREMENT_TABLE_COLS[2], table_id);
        values.put(MEASUREMENT_TABLE_COLS[3], name);
        values.put(MEASUREMENT_TABLE_COLS[4], amount);

        //ret.close();

        return String.valueOf(db.insert(MEASUREMENTS_TABLE, null, values));
    }

    public String insertRecipe(String name, int prepTime, int cookTime, String author, int servings, String imageLink, String categories, String instructions, int imageRotation){
        SQLiteDatabase db = getWritableDatabase();
        String now = getTime();

        ContentValues values = new ContentValues();
        values.put(RECIPES_TABLE_COLS[1], name);
        values.put(RECIPES_TABLE_COLS[2], now);
        values.put(RECIPES_TABLE_COLS[3], "never");
        values.put(RECIPES_TABLE_COLS[4], prepTime);
        values.put(RECIPES_TABLE_COLS[5], cookTime);
        values.put(RECIPES_TABLE_COLS[6], author);
        values.put(RECIPES_TABLE_COLS[7], servings);
        values.put(RECIPES_TABLE_COLS[8], imageLink);
        values.put(RECIPES_TABLE_COLS[9], categories);
        values.put(RECIPES_TABLE_COLS[10], instructions);
        values.put(RECIPES_TABLE_COLS[11], imageRotation);

        return String.valueOf(db.insert(RECIPES_TABLE, null, values));
    }

    public String insertRecipeItem(String name, String recipe_id, String ingredient_id, String measurement_id, String category, int order){


        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RECIPE_ITEMS_TABLE_COLS[1], name);
        values.put(RECIPE_ITEMS_TABLE_COLS[2], recipe_id);
        values.put(RECIPE_ITEMS_TABLE_COLS[3], ingredient_id);
        values.put(RECIPE_ITEMS_TABLE_COLS[4], measurement_id);
        values.put(RECIPE_ITEMS_TABLE_COLS[5], category);
        values.put(RECIPE_ITEMS_TABLE_COLS[6], order);

        return String.valueOf(db.insert(RECIPE_ITEMS_TABLE, null, values));
    }

    public String insertInstruction(String text, String recipe_id, String category, int order){


        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INSTRUCTIONS_TABLE_COLS[1], text);
        values.put(INSTRUCTIONS_TABLE_COLS[2], recipe_id);
        values.put(INSTRUCTIONS_TABLE_COLS[3], category);
        values.put(INSTRUCTIONS_TABLE_COLS[4], order);

        return String.valueOf(db.insert(INSTRUCTIONS_TABLE, null, values));
    }

    public String insertIngredient(String name){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INGREDIENTS_TABLE_COLS[1], name);

        return String.valueOf(db.insert(INGREDIENTS_TABLE, null, values));
    }

    public String insertIngredientUnit(String ingredient_id, String measurement_id, String cost){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INGREDIENT_UNIT_TABLE_COLS[1], ingredient_id);
        values.put(INGREDIENT_UNIT_TABLE_COLS[2], measurement_id);
        values.put(INGREDIENT_UNIT_TABLE_COLS[3], cost);

        return String.valueOf(db.insert(INGREDIENT_UNIT_TABLE, null, values));
    }

    public String insertQuickAddItem(String name){
        SQLiteDatabase db = getWritableDatabase();
        String now = getTime();

        ContentValues values = new ContentValues();
        values.put(QUICK_ADD_TABLE_COLS[1], name);
        values.put(QUICK_ADD_TABLE_COLS[2], now);

        return String.valueOf(db.insert(QUICK_ADD_TABLE, null, values));
    }

    public String insertMeds(String name, int remaining, int total, int hoursBetween, boolean automatic, String notifyAt, int daysInAdvance){
        SQLiteDatabase db = getWritableDatabase();
        String now = getTime();

        int auto = automatic ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(MEDS_TABLE_COLS[1], name);
        values.put(MEDS_TABLE_COLS[2], remaining);
        values.put(MEDS_TABLE_COLS[3], total);
        values.put(MEDS_TABLE_COLS[4], hoursBetween);
        values.put(MEDS_TABLE_COLS[5], auto);
        values.put(MEDS_TABLE_COLS[6], now);
        values.put(MEDS_TABLE_COLS[7], notifyAt);
        values.put(MEDS_TABLE_COLS[8], daysInAdvance);

        return String.valueOf(db.insert(MEDS_TABLE, null, values));
    }

    // updates
    public String updateList(String name, String last_modified){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getListByName(name);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(LISTS_TABLE_COLS[0], ret.getString(ret.getColumnIndex(LISTS_TABLE_COLS[0])));
            values.put(LISTS_TABLE_COLS[1], name);
            values.put(LISTS_TABLE_COLS[2], ret.getString(ret.getColumnIndex(LISTS_TABLE_COLS[2])));
            values.put(LISTS_TABLE_COLS[3], last_modified);

            return String.valueOf(db.update(LISTS_TABLE, values, "id = ?", new String[]{ret.getString(ret.getColumnIndex(LISTS_TABLE_COLS[0]))}));
        }
        return "-1";
    }

    public String updateListItem(String id, String name, String state, String recipe_item_id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getListItemById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(LIST_ITEMS_TABLE_COLS[0], ret.getString(ret.getColumnIndex(LIST_ITEMS_TABLE_COLS[0])));
            values.put(LIST_ITEMS_TABLE_COLS[1], ret.getString(ret.getColumnIndex(LIST_ITEMS_TABLE_COLS[1])));
            values.put(LIST_ITEMS_TABLE_COLS[2], name);
            values.put(LIST_ITEMS_TABLE_COLS[3], state);
            values.put(LIST_ITEMS_TABLE_COLS[4], recipe_item_id);

            return String.valueOf(db.update(LIST_ITEMS_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateMeasurement(String id, @Nullable String measurement_type_id, String amount){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getMeasurementById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(MEASUREMENT_TABLE_COLS[0], ret.getString(ret.getColumnIndex(MEASUREMENT_TABLE_COLS[0])));
            values.put(MEASUREMENT_TABLE_COLS[1], ret.getString(ret.getColumnIndex(MEASUREMENT_TABLE_COLS[1])));
            values.put(MEASUREMENT_TABLE_COLS[2], ret.getString(ret.getColumnIndex(MEASUREMENT_TABLE_COLS[2])));
            if(measurement_type_id == null){
                values.put(MEASUREMENT_TABLE_COLS[3], ret.getString(3));
            }else{
                values.put(MEASUREMENT_TABLE_COLS[3], measurement_type_id);
            }

            values.put(MEASUREMENT_TABLE_COLS[4], amount);

            return String.valueOf(db.update(MEASUREMENTS_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateMeasurementType(String id, String name){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getMeasurementTypeById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(MEASUREMENT_TYPES_TABLE_COLS[0], ret.getString(ret.getColumnIndex(MEASUREMENT_TYPES_TABLE_COLS[0])));
            values.put(MEASUREMENT_TYPES_TABLE_COLS[1], name);

            return String.valueOf(db.update(MEASUREMENT_TYPES_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateRecipe(String id, String name, String last_made, int prepTime, int cookTime, String author, int servings, String imageLink, String categories, String instructions, int imageRotation){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getRecipeById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(RECIPES_TABLE_COLS[0], ret.getString(ret.getColumnIndex(RECIPES_TABLE_COLS[0])));
            values.put(RECIPES_TABLE_COLS[1], name);
            values.put(RECIPES_TABLE_COLS[2], ret.getString(ret.getColumnIndex(RECIPES_TABLE_COLS[2])));
            values.put(RECIPES_TABLE_COLS[3], last_made);
            values.put(RECIPES_TABLE_COLS[4], prepTime);
            values.put(RECIPES_TABLE_COLS[5], cookTime);
            values.put(RECIPES_TABLE_COLS[6], author);
            values.put(RECIPES_TABLE_COLS[7], servings);
            values.put(RECIPES_TABLE_COLS[8], imageLink);
            values.put(RECIPES_TABLE_COLS[9], categories);
            values.put(RECIPES_TABLE_COLS[10], instructions);
            values.put(RECIPES_TABLE_COLS[11], imageRotation);

            return String.valueOf(db.update(RECIPES_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateRecipeItem(String id, String name, String measurement_id, String category, int order){


        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getRecipeItemById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(RECIPE_ITEMS_TABLE_COLS[0], ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[0])));
            values.put(RECIPE_ITEMS_TABLE_COLS[1], name);
            values.put(RECIPE_ITEMS_TABLE_COLS[2], ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[2])));
            values.put(RECIPE_ITEMS_TABLE_COLS[3], ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[3])));
            values.put(RECIPE_ITEMS_TABLE_COLS[4], measurement_id);
            values.put(RECIPE_ITEMS_TABLE_COLS[5], category);
            values.put(RECIPE_ITEMS_TABLE_COLS[6], order);

            return String.valueOf(db.update(RECIPE_ITEMS_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateInstruction(String id, String text, String recipe_id, String category, int order){


        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INSTRUCTIONS_TABLE_COLS[1], text);
        values.put(INSTRUCTIONS_TABLE_COLS[2], recipe_id);
        values.put(INSTRUCTIONS_TABLE_COLS[3], category);
        values.put(INSTRUCTIONS_TABLE_COLS[4], order);

        return String.valueOf(db.update(INSTRUCTIONS_TABLE, values, "id = ?", new String[]{id}));
    }

    public String updateIngredient(String id, String name){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getIngredientById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(INGREDIENTS_TABLE_COLS[0], ret.getString(ret.getColumnIndex(INGREDIENTS_TABLE_COLS[0])));
            values.put(INGREDIENTS_TABLE_COLS[1], name);

            return String.valueOf(db.update(INGREDIENTS_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateIngredientUnit(String id, String measurement_id, String cost){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getIngredientUnitById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(INGREDIENT_UNIT_TABLE_COLS[0], ret.getString(ret.getColumnIndex(INGREDIENT_UNIT_TABLE_COLS[0])));
            values.put(INGREDIENT_UNIT_TABLE_COLS[1], ret.getString(ret.getColumnIndex(INGREDIENT_UNIT_TABLE_COLS[1])));
            values.put(INGREDIENT_UNIT_TABLE_COLS[2], measurement_id);
            values.put(INGREDIENT_UNIT_TABLE_COLS[3], cost);

            return String.valueOf(db.update(INGREDIENT_UNIT_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateQuickAddItem(String id, String name, String last_used){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getQuickAddItemById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(QUICK_ADD_TABLE_COLS[0], ret.getString(ret.getColumnIndex(QUICK_ADD_TABLE_COLS[0])));
            values.put(QUICK_ADD_TABLE_COLS[1], name);
            values.put(QUICK_ADD_TABLE_COLS[2], last_used);

            return String.valueOf(db.update(QUICK_ADD_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateMeds(String id, String name, int remaining, int total, int hoursBetween, boolean automatic, String lastChanged, String notifyAt, int daysInAdvance){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getQuickAddItemById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(MEDS_TABLE_COLS[0], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[0])));
            values.put(MEDS_TABLE_COLS[1], name);
            values.put(MEDS_TABLE_COLS[2], remaining);
            values.put(MEDS_TABLE_COLS[3], total);
            values.put(MEDS_TABLE_COLS[4], hoursBetween);
            values.put(MEDS_TABLE_COLS[5], automatic);
            values.put(MEDS_TABLE_COLS[6], lastChanged);
            values.put(MEDS_TABLE_COLS[7], notifyAt);
            values.put(MEDS_TABLE_COLS[8], daysInAdvance);

            return String.valueOf(db.update(MEDS_TABLE, values, "id = ?", new String[]{id}));
        }
        return "-1";
    }

    public String updateMeds(String id, int remaining, String lastChanged){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getMedById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(MEDS_TABLE_COLS[0], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[0])));
            values.put(MEDS_TABLE_COLS[1], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[1])));
            values.put(MEDS_TABLE_COLS[2], remaining);
            values.put(MEDS_TABLE_COLS[3], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[3])));
            values.put(MEDS_TABLE_COLS[4], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[4])));
            values.put(MEDS_TABLE_COLS[5], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[5])));
            values.put(MEDS_TABLE_COLS[6], lastChanged);
            values.put(MEDS_TABLE_COLS[7], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[7])));
            values.put(MEDS_TABLE_COLS[8], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[8])));
            ret.close();
            return String.valueOf(db.update(MEDS_TABLE, values, "id = ?", new String[]{id}));
        }
        ret.close();
        return "-1";
    }

    public String updateMeds(String id, boolean automatic){
        SQLiteDatabase db = getWritableDatabase();
        Cursor ret = getMedById(id);
        if(ret.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(MEDS_TABLE_COLS[0], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[0])));
            values.put(MEDS_TABLE_COLS[1], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[1])));
            values.put(MEDS_TABLE_COLS[2], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[2])));
            values.put(MEDS_TABLE_COLS[3], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[3])));
            values.put(MEDS_TABLE_COLS[4], automatic);
            values.put(MEDS_TABLE_COLS[5], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[5])));
            values.put(MEDS_TABLE_COLS[6], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[6])));
            values.put(MEDS_TABLE_COLS[7], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[7])));
            values.put(MEDS_TABLE_COLS[8], ret.getString(ret.getColumnIndex(MEDS_TABLE_COLS[8])));
            ret.close();
            return String.valueOf(db.update(MEDS_TABLE, values, "id = ?", new String[]{id}));
        }
        ret.close();
        return "-1";
    }

    // reads
    public Cursor getAllDataFromTable(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + tableName, null);
    }

    public Cursor getListByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + LISTS_TABLE + " where name = ?", new String[]{name});
    }

    public Cursor getListById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + LISTS_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getAllLists(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + LISTS_TABLE, null);
    }

    public Cursor getListItems(String list_id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + LIST_ITEMS_TABLE + " where list_id = ?", new String[]{list_id});
    }

    public boolean listContainsRecipe(String recipe_id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor temp = db.rawQuery("select * from " + LIST_ITEMS_TABLE + " where RECIPE_ITEM_ID = ?", new String[]{recipe_id});
        boolean ret = temp.moveToFirst();
        temp.close();
        return ret;
    }

    public int getListItemCount(String list_id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor temp = db.rawQuery("select * from " + LIST_ITEMS_TABLE + " where list_id = ?", new String[]{list_id});
        return temp.getCount();
    }

    public int getListItemCrossedCount(String list_id){
        SQLiteDatabase db = getWritableDatabase();
        int count = 0;
        Cursor temp = db.rawQuery("select * from " + LIST_ITEMS_TABLE + " where list_id = ?", new String[]{list_id});
        if(temp.moveToFirst()){
            do{
                if(temp.getInt(3) == 1){
                    count++;
                }
            }while(temp.moveToNext());
        }
        return count;
    }

    public Cursor getListItemById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + LIST_ITEMS_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getAllMeasurementTypes(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + MEASUREMENT_TYPES_TABLE, null);
    }

    public Cursor getMeasurementTypeById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + MEASUREMENT_TYPES_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getMeasurementTypeByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + MEASUREMENT_TYPES_TABLE + " where name = ?", new String[]{name});
    }

    public Cursor getAllMeasurements(Table table_name, String table_id){
        SQLiteDatabase db = getWritableDatabase();
        switch(table_name){
            case RECIPE:
            case LIST:
            case INGREDIENT:
                return db.rawQuery("select * from " + MEASUREMENTS_TABLE + " where TABLE_NAME = ? and TABLE_ID = ?", new String[]{table_name.name(), table_id});
            default:
                return null;
        }
    }

    public Cursor getMeasurementById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + MEASUREMENTS_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getAllRecipes(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + RECIPES_TABLE, null);
    }

    public Cursor getRecipeById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + RECIPES_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getAllRecipeItems(String recipe_id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + RECIPE_ITEMS_TABLE + " where recipe_id = ?", new String[]{recipe_id});
    }

    public Cursor getAllRecipeInstructions(String recipe_id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + INSTRUCTIONS_TABLE + " where recipe_id = ?", new String[]{recipe_id});
    }

    public Cursor getRecipeItemById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + RECIPE_ITEMS_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getAllIngredients(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + INGREDIENTS_TABLE, null);
    }

    public Cursor getIngredientById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + INGREDIENTS_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getIngredientByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + INGREDIENTS_TABLE + " where name = ?", new String[]{name});
    }

    public Cursor getAllIngredientUnits(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + INGREDIENT_UNIT_TABLE, null);
    }

    public Cursor getIngredientUnitsByIngredientId(String ingredient_id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + INGREDIENT_UNIT_TABLE + " where ingredient_id = ?", new String[]{ingredient_id});
    }

    public Cursor getIngredientUnitById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + INGREDIENT_UNIT_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getAllQuickAddItems(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + QUICK_ADD_TABLE, null);
    }

    public Cursor getQuickAddItemById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + QUICK_ADD_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getMedById(String id){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + MEDS_TABLE + " where id = ?", new String[]{id});
    }

    public Cursor getAllMeds(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + MEDS_TABLE, null);
    }

    // deletes
    public boolean deleteListByName(String name){
        SQLiteDatabase db = getWritableDatabase();

        String table_id;
        Cursor ret = getListByName(name);
        if(ret.moveToFirst()){
            table_id = ret.getString(ret.getColumnIndex(LISTS_TABLE_COLS[0]));
        }else{return false;}

        ret = getListItems(table_id);
        if(ret.moveToFirst()){
            do{
                deleteListItemById(ret.getString(ret.getColumnIndex(LIST_ITEMS_TABLE_COLS[0])));
            }while(ret.moveToNext());
        }

        // delete list items with this list id
        ret = db.rawQuery("delete from " + LIST_ITEMS_TABLE + " where list_id = ?", new String[]{table_id});

        ret.close();

        return db.delete(LISTS_TABLE, "name = ?", new String[]{name}) > 0;
    }

    public boolean deleteListById(String id){
        SQLiteDatabase db = getWritableDatabase();

        // delete list items with this list id
        //db.rawQuery("delete from " + LIST_ITEMS_TABLE + " where list_id = ?", new String[]{id});
        Cursor ret = getListItems(id);
        if(ret.moveToFirst()){
            do{
                deleteListItemById(ret.getString(ret.getColumnIndex(LIST_ITEMS_TABLE_COLS[0])));
            }while(ret.moveToNext());
        }

        return db.delete(LISTS_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteListItemById(String id){
        SQLiteDatabase db = getWritableDatabase();

        // delete measurements referencing this id
        Cursor ret = db.rawQuery("delete from " + MEASUREMENTS_TABLE + " where " + MEASUREMENT_TABLE_COLS[1] + " = ?" + " and " + MEASUREMENT_TABLE_COLS[2] + " = ?", new String[]{Table.LIST.name(),id});

        ret.close();

        return db.delete(LIST_ITEMS_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteListItemByRecipeId(String recipeId) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(LIST_ITEMS_TABLE, "RECIPE_ITEM_ID = ?", new String[]{recipeId}) > 0;
    }

    public boolean deleteMeasurementById(String id){
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(MEASUREMENTS_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteMeasurementTypeByName(String name){
        SQLiteDatabase db = getWritableDatabase();

        String measurement_type_id;
        Cursor ret = getMeasurementTypeByName(name);
        if(ret.moveToFirst()){
            measurement_type_id = ret.getString(ret.getColumnIndex(MEASUREMENT_TYPES_TABLE_COLS[0]));
        }else{return false;}

        // delete measurements using this type
        ret = db.rawQuery("select * from " + MEASUREMENTS_TABLE + " where MEASUREMENT_TYPE_ID = ?", new String[]{measurement_type_id});
        if(ret.moveToFirst()){
            do{
                deleteMeasurementById(ret.getString(0));
            }while(ret.moveToNext());
        }

        ret.close();

        return db.delete(MEASUREMENT_TYPES_TABLE, "name = ?", new String[]{name}) > 0;
    }

    public boolean deleteMeasurementTypeById(String id){
        SQLiteDatabase db = getWritableDatabase();

        // delete measurements using this type
        Cursor ret = db.rawQuery("select * from " + MEASUREMENTS_TABLE + " where MEASUREMENT_TYPE_ID = ?", new String[]{id});
        if(ret.moveToFirst()){
            do{
                deleteMeasurementById(ret.getString(0));
            }while(ret.moveToNext());
        }

        ret.close();

        return db.delete(MEASUREMENT_TYPES_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteRecipeById(String id){
        SQLiteDatabase db = getWritableDatabase();

        // delete recipe items
        Cursor ret = getAllRecipeItems(id);
        if(ret.moveToFirst()){
            do{
                deleteRecipeItemById(ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[0])));
            }while(ret.moveToNext());
        }

        // delete recipe items
        Cursor ret2 = getAllRecipeInstructions(id);
        if(ret2.moveToFirst()){
            do{
                deleteRecipeItemById(ret2.getString(ret2.getColumnIndex(INSTRUCTIONS_TABLE_COLS[0])));
            }while(ret2.moveToNext());
        }

        return db.delete(RECIPES_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteRecipeItemById(String id){
        SQLiteDatabase db = getWritableDatabase();

        // delete associated measurement
//        Cursor ret = db.rawQuery("delete from " + MEASUREMENTS_TABLE + " where " + MEASUREMENT_TABLE_COLS[1] + " = ?" + " and " + MEASUREMENT_TABLE_COLS[2] + " = ?", new String[]{Table.RECIPE.name(), id});
//        ret.close();

        return db.delete(RECIPE_ITEMS_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public int deleteRecipeItemsByCategory(String recipeId, String category){
        SQLiteDatabase db = getWritableDatabase();

        int count = 0;

        Cursor ret = db.rawQuery("select * from " + RECIPE_ITEMS_TABLE + " where recipe_id = ? and category = ?", new String[]{recipeId, category});
        if(ret.moveToFirst()){
            do{
                deleteRecipeItemById(ret.getString(0));
                count++;
            }while(ret.moveToNext());
        }

        return count;
    }

    public boolean deleteIngredientByName(String name){
        SQLiteDatabase db = getWritableDatabase();

        Cursor ret = getIngredientByName(name);
        String ingredient_id;

        if(ret.moveToFirst()){
            ingredient_id = ret.getString(0);
        }else{return false;}

        // delete ingredient units that reference this
        ret = db.rawQuery("select * from " + INGREDIENT_UNIT_TABLE + " where ingredient_id = ?", new String[]{ingredient_id});
        if(ret.moveToFirst()){
            do{
                deleteIngredientUnitById(ret.getString(ret.getColumnIndex(INGREDIENT_UNIT_TABLE_COLS[0])));
            }while(ret.moveToNext());
        }

        // null recipe items' ingredient ids that reference this
        ret = db.rawQuery("select * from " + RECIPE_ITEMS_TABLE + " where ingredient_id = ?", new String[]{ingredient_id});
        if(ret.moveToFirst()){
            do{
                updateRecipeItem(ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[0])), ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[1])), "-1", ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[5])), ret.getInt(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[6])));
            }while(ret.moveToNext());
        }

        ret.close();

        return db.delete(INGREDIENTS_TABLE, "name = ?", new String[]{name}) > 0;
    }

    public boolean deleteIngredientById(String id){
        SQLiteDatabase db = getWritableDatabase();

        // delete ingredient units that reference this
        Cursor ret = db.rawQuery("select * from " + INGREDIENT_UNIT_TABLE + " where ingredient_id = ?", new String[]{id});
        if(ret.moveToFirst()){
            do{
                deleteIngredientUnitById(ret.getString(ret.getColumnIndex(INGREDIENT_UNIT_TABLE_COLS[0])));
            }while(ret.moveToNext());
        }

        // null recipe items' ingredient ids that reference this
        ret = db.rawQuery("select * from " + RECIPE_ITEMS_TABLE + " where ingredient_id = ?", new String[]{id});
        if(ret.moveToFirst()){
            do{
                updateRecipeItem(ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[0])), ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[1])), "-1", ret.getString(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[5])), ret.getInt(ret.getColumnIndex(RECIPE_ITEMS_TABLE_COLS[6])));
            }while(ret.moveToNext());
        }

        ret.close();

        return db.delete(INGREDIENTS_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteInstructionById(String id){
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(INSTRUCTIONS_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteIngredientUnitById(String id){
        SQLiteDatabase db = getWritableDatabase();

        // delete associated measurement
        Cursor ret = db.rawQuery("delete from " + MEASUREMENTS_TABLE + " where " + MEASUREMENT_TABLE_COLS[1] + " = " + Table.INGREDIENT.name() + " and " + MEASUREMENT_TABLE_COLS[2] + " = ?", new String[]{id});
        ret.close();

        return db.delete(INGREDIENT_UNIT_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteQuickAddItemByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(QUICK_ADD_TABLE, "name = ?", new String[]{name}) > 0;
    }

    public boolean deleteQuickAddItemById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(QUICK_ADD_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public boolean deleteMedById(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(MEDS_TABLE, "id = ?", new String[]{id}) > 0;
    }

    public String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void deleteDb(){
        context.deleteDatabase(DATABASE_NAME);
    }

    public boolean deleteListItemByName(String listId, String name){
        SQLiteDatabase db = getWritableDatabase();

        Cursor ret = db.rawQuery("select id from " + LIST_ITEMS_TABLE + " where " + LIST_ITEMS_TABLE_COLS[1] + " = ? and " + LIST_ITEMS_TABLE_COLS[2] + " = ?", new String[]{listId, name});

        if(ret.moveToFirst()){
            do{
                // delete measurements referencing this id
                String id = ret.getString(0);
                db.rawQuery("delete from " + MEASUREMENTS_TABLE + " where " + MEASUREMENT_TABLE_COLS[1] + " = ?" + " and " + MEASUREMENT_TABLE_COLS[2] + " = ?", new String[]{Table.LIST.name(),id});
            }while(ret.moveToNext());

        }
        ret.close();

        return db.delete(LIST_ITEMS_TABLE, "list_id = ? and name = ?", new String[]{listId, name}) > 0;
    }


}
