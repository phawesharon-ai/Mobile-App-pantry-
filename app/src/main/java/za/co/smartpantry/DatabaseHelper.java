package za.co.smartpantry;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "smartpantry.db";
    public DatabaseHelper(Context context) { super(context, DB_NAME, null, 1); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ingredients TEXT, method TEXT)");
        seed(db);
    }
    private void seed(SQLiteDatabase db) {
        addRecipe(db,"Tomato Egg Scramble","tomato:2:pieces;egg:2:pieces;salt:1:pinch","1. Chop the tomatoes.\n2. Beat the eggs with salt.\n3. Cook tomatoes in a pan, add eggs and stir until cooked.");
        addRecipe(db,"Tomato Salad","tomato:2:pieces;salt:1:pinch","1. Chop the tomatoes.\n2. Add salt and mix.");
        addRecipe(db,"Simple Egg Omelette","egg:2:pieces;salt:1:pinch","1. Beat eggs with salt.\n2. Cook in a lightly heated pan until set.");
        addRecipe(db,"Banana Snack","banana:1:piece","1. Peel the banana and serve.");
        addRecipe(db,"Cheese Omelette","egg:2:pieces;cheese:1:slice;salt:1:pinch","1. Beat eggs with salt.\n2. Cook, add cheese, fold and serve.");
        addRecipe(db,"Tomato Cheese Toast","bread:2:slices;tomato:1:piece;cheese:1:slice","1. Place tomato and cheese on bread.\n2. Toast until warm.");
        addRecipe(db,"Peanut Butter Toast","bread:2:slices;peanut butter:1:tablespoon","1. Spread peanut butter on bread.");
        addRecipe(db,"Apple Snack","apple:1:piece","1. Wash, slice and serve the apple.");
        addRecipe(db,"Rice and Egg","rice:1:cup;egg:1:piece;salt:1:pinch","1. Cook rice.\n2. Cook egg and serve with rice.");
        addRecipe(db,"Simple Rice","rice:1:cup;water:2:cups","1. Rinse rice.\n2. Cook in water until tender.");
        addRecipe(db,"Potato Mash","potato:2:pieces;salt:1:pinch","1. Boil potatoes until soft.\n2. Mash with salt.");
        addRecipe(db,"Carrot Salad","carrot:2:pieces","1. Wash and grate carrots.");
        addRecipe(db,"Cucumber Salad","cucumber:1:piece;salt:1:pinch","1. Slice cucumber.\n2. Add a little salt.");
        addRecipe(db,"Milk Banana Smoothie","banana:1:piece;milk:1:cup","1. Blend banana and milk until smooth.");
        addRecipe(db,"Butter Toast","bread:2:slices;butter:1:teaspoon","1. Spread butter on bread and toast.");
        addRecipe(db,"Tomato Rice","rice:1:cup;tomato:2:pieces; salt:1:pinch","1. Cook rice.\n2. Chop and cook tomatoes, add rice and salt.");
        addRecipe(db,"Fruit Bowl","apple:1:piece;banana:1:piece","1. Wash and cut the fruit into a bowl.");
        addRecipe(db,"Cheese Toast","bread:2:slices;cheese:1:slice","1. Put cheese on bread and toast.");
        addRecipe(db,"Boiled Eggs","egg:2:pieces;water:2:cups","1. Boil water.\n2. Add eggs and cook until done.");
        addRecipe(db,"Carrot Potato Soup","carrot:1:piece;potato:1:piece;water:2:cups;salt:1:pinch","1. Chop vegetables.\n2. Simmer in water until soft and season.");
    }
    private void addRecipe(SQLiteDatabase db, String name, String ingredients, String method) {
        ContentValues v = new ContentValues(); v.put("name",name); v.put("ingredients",ingredients); v.put("method",method); db.insert("recipes",null,v);
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pantry"); db.execSQL("DROP TABLE IF EXISTS recipes"); onCreate(db);
    }
    public long addPantry(String name, double quantity, String unit) {
        ContentValues v = new ContentValues(); v.put("name", normalize(name)); v.put("quantity",quantity); v.put("unit",normalize(unit));
        return getWritableDatabase().insert("pantry",null,v);
    }
    public int updatePantry(long id, String name, double quantity, String unit) {
        ContentValues v = new ContentValues(); v.put("name",normalize(name)); v.put("quantity",quantity); v.put("unit",normalize(unit));
        return getWritableDatabase().update("pantry",v,"id=?",new String[]{String.valueOf(id)});
    }
    public void deletePantry(long id) { getWritableDatabase().delete("pantry","id=?",new String[]{String.valueOf(id)}); }
    public ArrayList<PantryItem> getPantry() {
        ArrayList<PantryItem> list = new ArrayList<>(); Cursor c=getReadableDatabase().rawQuery("SELECT id,name,quantity,unit FROM pantry ORDER BY name",null);
        while(c.moveToNext()) list.add(new PantryItem(c.getLong(0),c.getString(1),c.getDouble(2),c.getString(3))); c.close(); return list;
    }
    public ArrayList<Recipe> getSuggestedRecipes() {
        ArrayList<Recipe> list = new ArrayList<>(); Cursor c=getReadableDatabase().rawQuery("SELECT id,name,ingredients,method FROM recipes ORDER BY name",null);
        ArrayList<PantryItem> pantry=getPantry();
        while(c.moveToNext()) {
            Recipe r=new Recipe(c.getLong(0),c.getString(1),c.getString(2),c.getString(3));
            if (matches(r,pantry)) list.add(r);
        }
        c.close(); return list;
    }
    private boolean matches(Recipe r, ArrayList<PantryItem> pantry) {
        String[] required=r.ingredients.split(";");
        for(String part:required) {
            String[] bits=part.trim().split(":");
            if(bits.length!=3) return false;
            String need=normalize(bits[0]); double qty;
            try { qty=Double.parseDouble(bits[1]); } catch(Exception e) { return false; }
            String unit=normalize(bits[2]); boolean found=false;
            for(PantryItem p:pantry) {
                if(normalize(p.name).equals(need) && normalize(p.unit).equals(unit) && p.quantity>=qty) { found=true; break; }
            }
            if(!found) return false;
        }
        return true;
    }
    public Recipe getRecipe(long id) {
        Cursor c=getReadableDatabase().rawQuery("SELECT id,name,ingredients,method FROM recipes WHERE id=?",new String[]{String.valueOf(id)});
        Recipe r=null; if(c.moveToFirst()) r=new Recipe(c.getLong(0),c.getString(1),c.getString(2),c.getString(3)); c.close(); return r;
    }
    public static String normalize(String s) {
        String x=s.trim().toLowerCase(Locale.ROOT);
        if(x.endsWith("ies") && x.length()>3) x=x.substring(0,x.length()-3)+"y";
        else if(x.endsWith("s") && !x.endsWith("ss") && x.length()>1) x=x.substring(0,x.length()-1);
        return x;
    }
    public static class PantryItem {
        public long id; public String name,unit; public double quantity;
        PantryItem(long id,String name,double quantity,String unit){this.id=id;this.name=name;this.quantity=quantity;this.unit=unit;}
        @Override public String toString(){return name+"  •  "+quantity+" "+unit+"\nTap to edit  |  Long-press to delete";}
    }
    public static class Recipe {
        public long id; public String name,ingredients,method;
        Recipe(long id,String name,String ingredients,String method){this.id=id;this.name=name;this.ingredients=ingredients;this.method=method;}
        @Override public String toString(){return name+"\nTap to view recipe";}
    }
}
