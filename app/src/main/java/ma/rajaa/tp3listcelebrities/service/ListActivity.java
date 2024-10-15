package ma.rajaa.tp3listcelebrities.service;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.rajaa.tp3listcelebrities.R;
import ma.rajaa.tp3listcelebrities.adapter.StarAdapter;
import ma.rajaa.tp3listcelebrities.classes.Star;

public class ListActivity extends AppCompatActivity {
    private List<Star> stars;
    private RecyclerView recyclerView;
    private StarAdapter starAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ListActivity", "onCreate: Activity started");

        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stars = new ArrayList<>();
        StarService service = StarService.getInstance();

        init();

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stars.addAll(service.findAll());

        starAdapter = new StarAdapter(this, stars);
        recyclerView.setAdapter(starAdapter);
    }

    public void init() {
        StarService service = StarService.getInstance();

        service.create(new Star("Les Misérables", R.drawable.im1, 3.5f));
        service.create(new Star("Madame Bovary", R.drawable.im2, 3.0f));
        service.create(new Star("Le Comte de Monte-Cristo", R.drawable.im3, 5.0f));
        service.create(new Star("À la recherche du temps perdu", R.drawable.im7, 1.0f));
        service.create(new Star("L'Étranger", R.drawable.im5, 5.0f));
        service.create(new Star("Germinal", R.drawable.im6, 1.0f));
        service.create(new Star("Le Rouge et le Noir", R.drawable.im3, 5.0f));
        service.create(new Star("Les Fleurs du mal", R.drawable.im7, 1.0f));
        service.create(new Star("Candide", R.drawable.im5, 5.0f));
        service.create(new Star("Les Trois Mousquetaires", R.drawable.im6, 1.0f));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Ne rien faire ici
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (starAdapter != null) {
                    starAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        searchView.setIconifiedByDefault(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
           String txt = "https://play.google.com/store/apps/details?id=ma.rajaa.tp3listcelebrities";
            String mimeType = "text/plain";

            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle("Partager avec")
                    .setText(txt)
                    .startChooser();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}