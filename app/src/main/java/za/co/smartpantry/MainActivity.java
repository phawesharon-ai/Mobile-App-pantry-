package za.co.smartpantry;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    DatabaseHelper db; ArrayList<DatabaseHelper.PantryItem> items; ArrayAdapter<DatabaseHelper.PantryItem> adapter; ListView list;
    @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main); db=new DatabaseHelper(this); list=findViewById(R.id.pantryList);
        findViewById(R.id.addButton).setOnClickListener(v->startActivity(new Intent(this,AddEditIngredientActivity.class)));
        findViewById(R.id.suggestButton).setOnClickListener(v->startActivity(new Intent(this,SuggestedRecipesActivity.class)));
        findViewById(R.id.settingsButton).setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));
        list.setOnItemClickListener((p,v,pos,id)->{DatabaseHelper.PantryItem item=items.get(pos); Intent i=new Intent(this,AddEditIngredientActivity.class); i.putExtra("id",item.id); i.putExtra("name",item.name); i.putExtra("quantity",item.quantity); i.putExtra("unit",item.unit); startActivity(i);});
        list.setOnItemLongClickListener((p,v,pos,id)->{DatabaseHelper.PantryItem item=items.get(pos); new AlertDialog.Builder(this).setTitle("Delete ingredient?").setMessage("Delete "+item.name+"?").setNegativeButton("Cancel",null).setPositiveButton("Delete",(d,w)->{db.deletePantry(item.id); load();}).show(); return true;});
    }
    @Override protected void onResume(){super.onResume(); if(db!=null)load();}
    void load(){items=db.getPantry(); adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items); list.setAdapter(adapter);}
}
