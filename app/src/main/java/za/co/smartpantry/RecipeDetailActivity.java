package za.co.smartpantry;

import android.app.*;
import android.os.Bundle;
import android.widget.*;
import java.util.*;

public class RecipeDetailActivity extends Activity {
    @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_recipe_detail); DatabaseHelper db=new DatabaseHelper(this);
        DatabaseHelper.Recipe r=db.getRecipe(getIntent().getLongExtra("recipeId",-1));
        if(r!=null){((TextView)findViewById(R.id.recipeName)).setText(r.name); StringBuilder ingredients=new StringBuilder();
            for(String part:r.ingredients.split(";")){String[] bits=part.trim().split(":"); if(bits.length==3)ingredients.append("• ").append(bits[1]).append(" ").append(bits[2]).append(" ").append(bits[0]).append("\n");}
            ((TextView)findViewById(R.id.recipeIngredients)).setText(ingredients.toString());
            ((TextView)findViewById(R.id.recipeMethod)).setText(r.method);}
        findViewById(R.id.backButton).setOnClickListener(v->finish());
    }
}
