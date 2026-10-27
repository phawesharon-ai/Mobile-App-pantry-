package za.co.smartpantry;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

public class SuggestedRecipesActivity extends Activity {
    DatabaseHelper db; ArrayList<DatabaseHelper.Recipe> recipes;
    @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_suggestions); db=new DatabaseHelper(this);
        ListView list=findViewById(R.id.recipeList); recipes=db.getSuggestedRecipes();
        if(recipes.isEmpty()){TextView empty=new TextView(this); empty.setText("No recipes match your pantry yet. Add more ingredients."); empty.setPadding(20,30,20,30); empty.setTextColor(0xFF352B10); ((android.view.ViewGroup)list.getParent()).addView(empty,1); list.setEmptyView(empty);}
        list.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,recipes));
        list.setOnItemClickListener((p,v,pos,id)->{Intent i=new Intent(this,RecipeDetailActivity.class); i.putExtra("recipeId",recipes.get(pos).id); startActivity(i);});
        findViewById(R.id.backButton).setOnClickListener(v->finish());
    }
}
