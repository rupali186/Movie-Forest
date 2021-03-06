package com.example.rupali.movieforest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView nameTextView;
    TextView logInOrSignUp;
    ImageView key;
    TextView toolbarTitle;
    CircleImageView loginImageView;
    boolean loginWithFb=false;
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
        logInOrSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginWithFb) {
                    logout();
                }
                else {
                    Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
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
            Log.d("HOMETAGGER",imageurl);
            loginWithFb=sharedPreferences.getBoolean(Constants.CONNECT_WITH_FACEBOOK,false);
            nameTextView.setText("Hi "+name+" !");
            if(loginWithFb&&imageurl!=null){
                logInOrSignUp.setText("Log Out");
                Picasso.get().load(imageurl).into(loginImageView);
            }
            if(!loginWithFb){
                logInOrSignUp.setText("Log In Or Sign Up!");
                logInOrSignUp.setVisibility(View.VISIBLE);
            }
            Snackbar.make(navigationView,"Welcome "+name,Snackbar.LENGTH_LONG).show();
        }
        MoviesFragment fragment = new MoviesFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_x,fragment).commit();
    }

    private void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Do you want to LogOut?");
        builder.setTitle("Confirm LogOut !");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loginImageView.setImageDrawable(null);
                loginImageView.setBackgroundResource(R.drawable.profile_i);
                nameTextView.setText("Hi Guest !");
                logInOrSignUp.setText("Log In Or Sign Up!");
                SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(Constants.LOGIN_NAME,"Guest");
                editor.putBoolean(Constants.CONNECT_WITH_FACEBOOK,false);
                editor.putString(Constants.LOGIN_PROFILE_URL,"");
                editor.putBoolean(Constants.PREVIOUSLY_STARTED,true);
                editor.commit();
                LoginManager.getInstance().logOut();
                loginWithFb=false;
                dialogInterface.dismiss();

            }
        });
        builder.show();
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