package com.example.rupali.movieforest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView nameTextView;
    TextView logInOrSignUp;
    ImageView key;
    TextView toolbarTitle;
    ImageView loginImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.home_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Movies");
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nameTextView=(TextView) navigationView.getHeaderView(0).findViewById(R.id.login_profile_name);
        logInOrSignUp=(TextView) navigationView.getHeaderView(0).findViewById(R.id.logInOrSignUp);
        key=navigationView.getHeaderView(0).findViewById(R.id.key);
        loginImageView=navigationView.getHeaderView(0).findViewById(R.id.loginImageView);
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF_NAME,MODE_PRIVATE);
        Boolean ispreviouslyStarted =sharedPreferences.getBoolean(Constants.PREVIOUSLY_STARTED, false);

        if (!ispreviouslyStarted) {
            //show start activity
            startActivity(new Intent(HomeActivity.this, MainActivity.class));

        }
        else {
            String name = sharedPreferences.getString(Constants.LOGIN_NAME, "name");
            String imageurl=sharedPreferences.getString(Constants.LOGIN_PROFILE_URL,"");
            boolean loginWithFb=sharedPreferences.getBoolean(Constants.CONNECT_WITH_FACEBOOK,false);
            nameTextView.setText("Hi "+name+" !");
            if(loginWithFb&&imageurl!=null){
//                ConstraintLayout.LayoutParams layoutParams=new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(120,120,0,0);
//                loginImageView.setLayoutParams(layoutParams);
                new DownloadImageTask(loginImageView)
                        .execute(imageurl);
            }
            if(!loginWithFb){
                logInOrSignUp.setVisibility(View.VISIBLE);
                key.setVisibility(View.VISIBLE);
            }
            Snackbar.make(navigationView,"Welcome "+name,Snackbar.LENGTH_LONG).show();
        }
        MoviesFragment fragment = new MoviesFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_x,fragment).commit();

//        getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).edit()
//                .putBoolean(Constants.PREVIOUSLY_STARTED, true).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Search Movies,Tv Shows,Celebs");
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Bundle bundle=new Bundle();
                toolbarTitle.setText(query);
                bundle.putString(Constants.SEARCH_QUERY,query);
                SearchFragment fragment = new SearchFragment();
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_x,fragment).commit();
                // Toast like print
//                UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        Fragment fragment=null;
//        Class fragmentClass;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_movies) {
            MoviesFragment fragment = new MoviesFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_x,fragment).commit();
            toolbarTitle.setText("Movies");
        } else if (id == R.id.nav_tv) {
            TvFragment fragment = new TvFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_x,fragment).commit();
            toolbarTitle.setText("Tv Shows");

        } else if (id == R.id.nav_people) {
            CelebsFragment fragment = new CelebsFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_x,fragment).commit();
            toolbarTitle.setText("Celebs");

        } else if(id==R.id.nav_favourites){
            FavouritesFragment fragment = new FavouritesFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_x,fragment).commit();
            toolbarTitle.setText("Favourites");
        } else if (id == R.id.nav_about) {
            AboutFragment fragment = new AboutFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_x,fragment).commit();
            toolbarTitle.setText("About");

        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "MovieForest");
                String sAux = "\nGet information about your favourite movies, tv shows, celebs just by downloading a simple app. " +
                        "Simple design and easy to use" +
                        "\n";
                sAux = sAux + "https://play.google.com/store/apps/details?1234=the.MovieForest.1234 \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        } else if (id == R.id.nav_feedback) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","rupalichawla186@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: MovieForest");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
 class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        icon=getCroppedBitmap(icon);
        return icon;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
     public Bitmap getCroppedBitmap(Bitmap bitmap) {
         Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                 bitmap.getHeight(), Bitmap.Config.ARGB_8888);
         Canvas canvas = new Canvas(output);

         final int color = 0xff424242;
         final Paint paint = new Paint();
         final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

         paint.setAntiAlias(true);
         canvas.drawARGB(0, 0, 0, 0);
         paint.setColor(color);
         // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
         canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                 bitmap.getWidth() / 2, paint);
         paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
         canvas.drawBitmap(bitmap, rect, rect, paint);
         //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
         //return _bmp;
         return output;
     }
}
