package com.W9221214.AppLet.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.W9221214.AppLet.model.AppLet;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.W9221214.AppLet.R;
import com.W9221214.AppLet.adapters.MediaDisplayAdapter;
import com.W9221214.AppLet.adapters.AppLetAdapter;
import com.W9221214.AppLet.model.FilterParams;
import com.W9221214.AppLet.repository.Repository;
import com.W9221214.AppLet.utils.Constants;
import com.W9221214.AppLet.utils.Utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.W9221214.AppLet.utils.Constants.BundleKeys.BUNDLE_CURRENCY_KEY;
import static com.W9221214.AppLet.utils.Constants.BundleKeys.BUNDLE_EXTRA;
import static com.W9221214.AppLet.utils.Constants.BundleKeys.FILTERED_PARAMS_KEY;
import static com.W9221214.AppLet.utils.Constants.BundleKeys.APPLET_OBJECT_KEY;
import static com.W9221214.AppLet.utils.Constants.BundleKeys.SEARCH_PARAM_KEY;
import static com.W9221214.AppLet.utils.Constants.TypesList.SEARCH;
import static com.W9221214.AppLet.utils.Constants.TypesList.TYPE_LIST_KEY;
import static com.W9221214.AppLet.utils.Utils.formatDate;

public class Navigation extends AppCompatActivity {

    private static final String TAG = "Navigation";
    private FirebaseAuth auth;
    private List<AppLet> listings;
    private Repository repository;
    private AppLetAdapter recyclerViewAdapter;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView itemDescription;
    private TextView noEntries;
    private TextView areaSqft;
    private TextView numOfRooms;
    private TextView numOfBedrooms;
    private TextView title;

    private TextView status;
    private TextView addedDate;
    private RecyclerView mediaRecyclerView;
    private TextView location;
    private int appLetIndex;
    private int listType;
    private Bundle extras;
    private MediaDisplayAdapter mediaDisplayAdapter;
    private String currency;
    private TextView contact;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            goToMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setConfigs();
        setViews();
        setListingRecyclerView();
        addDataObservers();
        setToolbar();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        configureDrawer();
    }

    private void setConfigs() {
        auth = FirebaseAuth.getInstance();
        repository = new Repository(Navigation.this);
        appLetIndex = 0;

        currency = getIntent().getStringExtra(BUNDLE_CURRENCY_KEY);
        if (currency == null) {
            currency = Utils.getCurrency(Navigation.this);
        }

        extras = getIntent().getBundleExtra(BUNDLE_EXTRA);
        if (extras != null) {
            listType = extras.getInt(Constants.TypesList.TYPE_LIST_KEY, Constants.TypesList.ALL);
        } else {
            listType = Constants.TypesList.ALL;
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
    }



    private void configureDrawer() {
        configureDrawerLayout();
        configureNavigationView();
    }

    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_navigation_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }

            @Override
            public void onDrawerOpened(View view) {
                // blurBackground();
                TextView userEmailTextView = findViewById(R.id.drawer_header_user_email);
                try {
                    userEmailTextView.setText(Objects.requireNonNull(FirebaseAuth.getInstance()
                            .getCurrentUser()).getEmail());
                } catch (Exception e) {
                    Log.e(TAG, "configureDrawerLayout: " + e.getMessage());
                }
            }

            @Override
            public void onDrawerClosed(View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }

    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        Log.d(TAG, "onClick: user signOut");
        Intent intent = new Intent(Navigation.this
                , Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.activity_navigation_nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        Intent intent = null;

                        switch (id) {
                            case R.id.menu_drawer_all_properties:
                                intent = new Intent(Navigation.this
                                        , Navigation.class);
                                intent.putExtra(Constants.TypesList.TYPE_LIST_KEY, Constants.TypesList.ALL);
                                break;
                            case R.id.menu_drawer_map:
                                intent = new Intent(Navigation.this
                                        , Map.class);
                                break;
                            case R.id.menu_drawer_filter:
                                intent = new Intent(Navigation.this
                                        , Filter.class);
                                break;
                            case R.id.menu_drawer_sign_out:
                                signOutUser();
                                break;
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        if (intent != null) {
                            startActivity(intent);
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setViews() {
        toolbar = findViewById(R.id.navigation_activity_toolbar);
        itemDescription = findViewById(R.id.navigation_activity_description);

        noEntries = findViewById(R.id.no_entries);
        areaSqft = findViewById(R.id.navigation_activity_area);
        numOfRooms = findViewById(R.id.navigation_activity_num_of_rooms);
        numOfBedrooms = findViewById(R.id.navigation_activity_num_of_bedrooms);
        location = findViewById(R.id.navigation_activity_location);
        mediaRecyclerView = findViewById(R.id.activity_navigation_media_recycler_view);
        title = findViewById(R.id.navigation_activity_title);

        status = findViewById(R.id.navigation_activity_status);
        addedDate = findViewById(R.id.navigation_activity_added_date);

        contact = findViewById(R.id.navigation_activity_contact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_toolbar_search);


        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        final MenuItem menuItemEdit = menu.findItem(R.id.menu_toolbar_edit);
        final MenuItem menuItemAdd = menu.findItem(R.id.menu_toolbar_add);
        recyclerViewAdapter.setCurrency(currency);


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search is expanded
                menuItemEdit.setVisible(false);
                menuItemAdd.setVisible(false);

                toggle.setDrawerIndicatorEnabled(false);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                menuItemEdit.setVisible(true);
                menuItemAdd.setVisible(true);
                toggle.setDrawerIndicatorEnabled(true);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchTerm(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //It must return true for the menu to be displayed; if you return false it will not be show
        return true;
    }

    private void searchTerm(String term) {
        Intent intent = new Intent(Navigation.this
                , Navigation.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_LIST_KEY, SEARCH);
        bundle.putString(SEARCH_PARAM_KEY, term);
        intent.putExtra(BUNDLE_EXTRA, bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_toolbar_add:
                goToAdd_ModifyActivity(null);
                break;
            case R.id.menu_toolbar_edit:
                try {
                    String user_email =  Objects.requireNonNull(FirebaseAuth.getInstance()
                            .getCurrentUser()).getEmail();
                   AppLet appLet = listings.get(appLetIndex);

                    if(appLet.getEmail().equals(user_email)) {
                        goToAdd_ModifyActivity(listings.get(appLetIndex));
                    }
                    else {
                        Toast.makeText(this, "Sorry! Only Property Owner can edit"
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "It is not possible to edit"
                            , Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return false;
    }

    private void goToAdd_ModifyActivity(AppLet appLet) {
        Intent intent = new Intent(Navigation.this
                , Add_ModifyProperty.class);
        intent.putExtra(APPLET_OBJECT_KEY, appLet);
        intent.putExtra(BUNDLE_CURRENCY_KEY, currency);
        startActivity(intent);

    }

    private void addDataObservers() {
        LiveData<List<AppLet>> listLiveData = null;
        switch (listType) {
            case Constants.TypesList.ALL:
                listLiveData = repository.getAllListings();
                break;
            case Constants.TypesList.FILTERED:
                listLiveData = getFilteredList();
                break;
            case Constants.TypesList.SEARCH:
                listLiveData = getSearchList();
                break;
        }

        if (listLiveData != null) {
            listLiveData.observe(Navigation.this,
                    new Observer<List<AppLet>>() {
                        @Override
                        public void onChanged(@Nullable List<AppLet> appLets) {
                            if (listings.size() > 0) {
                                listings.clear();
                            }
                            if (appLets != null) {
                                listings.addAll(appLets);
                                recyclerViewAdapter.notifyDataSetChanged();
                                displayAppletInformation();
                            }
                        }
                    });
        }
    }

    private LiveData<List<AppLet>> getSearchList() {
        LiveData<List<AppLet>> listLiveData;
        String term = extras.getString(SEARCH_PARAM_KEY, "");
        listLiveData = repository.geSearchedListings(term);
        return listLiveData;
    }

    private LiveData<List<AppLet>> getFilteredList() {
        LiveData<List<AppLet>> listLiveData;
        FilterParams filterParams = extras.getParcelable(FILTERED_PARAMS_KEY);
        listLiveData = repository.filterList(filterParams);
        return listLiveData;
    }

    private void setListingRecyclerView() {
        listings = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.activity_navigation_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new AppLetAdapter(Navigation.this,
                listings, currency);
        recyclerViewAdapter.setOnSelectionItem(new AppLetAdapter.OnItemSelectedListener() {
            @Override
            public void onSelection(int position) {
                appLetIndex = position;
                displayAppletInformation();
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }



    private void displayAppletInformation() {
        if (listings.size() > 0) {
            AppLet appLet = listings.get(appLetIndex);
            itemDescription.setText(appLet.getLongDescription());

            int Area = appLet.getArea();
            if (Area > 0) {
                areaSqft.setText(String.valueOf(Area));
            } else {
                areaSqft.setText("");
            }
            int numberOfRooms = appLet.getNumberOfRooms();
            if (numberOfRooms > 0) {
                numOfRooms.setText(String.valueOf(numberOfRooms));
            } else {
                numOfRooms.setText("");
            }
            int numbOfBedRooms = appLet.getNumbOfBedRooms();
            if (numbOfBedRooms > 0) {
                numOfBedrooms.setText(String.valueOf(numbOfBedRooms));
            } else {
                numOfBedrooms.setText("");
            }
;
            contact.setText(String.valueOf(appLet.getContact()));
            location.setText(appLet.getAddress());

            title.setText(appLet.getTitle());
            setMediaRecyclerView(appLet);
            status.setText(appLet.getStatus());
            addedDate.setText(formatDate(appLet.getDatePutInMarket()));

            noEntries.setVisibility(View.GONE);
        } else {
            noEntries.setVisibility(View.VISIBLE);
        }
    }

    private void setMediaRecyclerView(final AppLet appLet) {
        mediaDisplayAdapter = new MediaDisplayAdapter(appLet.getPhotos(), false
                , getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false);
        mediaRecyclerView.setLayoutManager(layoutManager);

        mediaDisplayAdapter.setOnDeleteIconListener(new MediaDisplayAdapter.ItemDeleteListener() {
            @Override
            public void deleteIconClicked(int position) {
                appLet.getPhotos().remove(position);
                mediaDisplayAdapter.notifyDataSetChanged();
            }
        });

        mediaRecyclerView.setAdapter(mediaDisplayAdapter);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(Navigation.this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
