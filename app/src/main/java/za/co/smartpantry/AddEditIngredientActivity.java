package za.co.smartpantry;

import android.app.*;
import android.os.Bundle;
import android.widget.*;

public class AddEditIngredientActivity extends Activity {
    DatabaseHelper db; EditText name,quantity,unit; long id=-1;
    @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_add_edit); db=new DatabaseHelper(this);
        name=findViewById(R.id.nameInput); quantity=findViewById(R.id.quantityInput); unit=findViewById(R.id.unitInput);
        if(getIntent().hasExtra("id")){id=getIntent().getLongExtra("id",-1); name.setText(getIntent().getStringExtra("name")); quantity.setText(String.valueOf(getIntent().getDoubleExtra("quantity",0))); unit.setText(getIntent().getStringExtra("unit"));}
        findViewById(R.id.saveButton).setOnClickListener(v->save());
        findViewById(R.id.cancelButton).setOnClickListener(v->finish());
    }
    void save(){String n=name.getText().toString().trim(), q=quantity.getText().toString().trim(), u=unit.getText().toString().trim();
        if(n.isEmpty()||q.isEmpty()||u.isEmpty()){Toast.makeText(this,"Please complete all fields.",Toast.LENGTH_SHORT).show();return;}
        double amount; try{amount=Double.parseDouble(q);}catch(Exception e){Toast.makeText(this,"Enter a valid quantity.",Toast.LENGTH_SHORT).show();return;}
        if(amount<=0){Toast.makeText(this,"Quantity must be greater than zero.",Toast.LENGTH_SHORT).show();return;}
        if(id==-1) db.addPantry(n,amount,u); else db.updatePantry(id,n,amount,u);
        Toast.makeText(this,"Ingredient saved.",Toast.LENGTH_SHORT).show(); finish();
    }
}
