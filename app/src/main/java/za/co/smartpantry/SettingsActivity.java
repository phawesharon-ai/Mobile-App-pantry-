package za.co.smartpantry;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.widget.*;

public class SettingsActivity extends Activity {
    @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_settings);
        CheckBox toggle=findViewById(R.id.expiryToggle); android.content.SharedPreferences prefs=getSharedPreferences("settings",MODE_PRIVATE);
        toggle.setChecked(prefs.getBoolean("expiry",false)); toggle.setOnCheckedChangeListener((v,checked)->prefs.edit().putBoolean("expiry",checked).apply());
        findViewById(R.id.backButton).setOnClickListener(v->finish());
    }
}
