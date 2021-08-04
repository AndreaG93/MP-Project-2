package settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import java.util.Vector;
import mb.livewallpaper.WallpaperObserver;

public class MySettingActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static Vector<WallpaperObserver> observer = new Vector<WallpaperObserver>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for (WallpaperObserver object : observer) {
            object.update();
        }
    }

    public static void registerToObserverList(WallpaperObserver arg0) {
        observer.add(arg0);
    }
}



