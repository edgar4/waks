package ke.co.edgar.waks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.util.HashMap;

import ke.co.edgar.waks.LoginActivity;
import ke.co.edgar.waks.R;
import ke.co.edgar.waks.fragments.pages.DashBoard;
import ke.co.edgar.waks.fragments.pages.ProfileFragment;
import ke.co.edgar.waks.helper.SQLiteHandler;
import ke.co.edgar.waks.helper.SessionManager;

public class MainFragment extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SessionManager session;
    private SQLiteHandler db;
    private TextView txtName;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_container, new DashBoard());
        tx.commit();
        setTitle("DashBoard");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main_fragment, null);
        navigationView.addHeaderView(header);
         txtName= (TextView) header.findViewById(R.id.userName);
         txtEmail= (TextView) header.findViewById(R.id.userEmail);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        if (!session.isLoggedIn()) {
            logoutUser();
            Intent intent = new Intent(MainFragment.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_dashboard) {

            DashBoard dashBoard = new DashBoard();
            transaction.replace(R.id.fragment_container, dashBoard).commit();
            setTitle("DashBoard");

        } else if (id == R.id.nav_application) {
            ProfileFragment profile= new ProfileFragment();
            transaction.replace(R.id.fragment_container,profile).commit();
            setTitle("profile");

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
            session.setLogin(false);
            Intent intent = new Intent(MainFragment.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(MainFragment.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
