package com.arrg.android.app.ugallery.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.arrg.android.app.ugallery.R;
import com.arrg.android.app.ugallery.UGallery;
import com.arrg.android.app.ugallery.interfaces.SearchView;
import com.arrg.android.app.ugallery.model.entity.PhoneAlbum;
import com.arrg.android.app.ugallery.presenter.ISearchPresenter;
import com.arrg.android.app.ugallery.view.adapter.MediaSearchAdapter;
import com.example.jackmiras.placeholderj.library.PlaceHolderJ;
import com.thefinestartist.utils.content.Res;
import com.zhy.autolayout.AutoLinearLayout;

import org.fingerlinks.mobile.android.navigator.Navigator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thefinestartist.Base.getContext;

public class SearchActivity extends AppCompatActivity implements MediaSearchAdapter.OnItemClickListener, SearchView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.searchView)
    EditText searchView;
    @BindView(R.id.btnSearch)
    ImageButton btnSearch;
    @BindView(R.id.searchMedia)
    DragSelectRecyclerView searchMedia;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBackActionMode)
    ImageButton btnBackActionMode;
    @BindView(R.id.tvTitleActionMode)
    TextView tvTitleActionMode;
    @BindView(R.id.searchContainer)
    AutoLinearLayout searchContainer;
    @BindView(R.id.actionModeContainer)
    AutoLinearLayout actionModeContainer;

    private ArrayList<PhoneAlbum> phoneAlbumArrayList;
    private Boolean onLongClickPressed = false;
    private Boolean showMicrophone = true;
    private MediaSearchAdapter mediaSearchAdapter;
    private ISearchPresenter iSearchPresenter;
    private PlaceHolderJ placeHolderJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();

        phoneAlbumArrayList = (ArrayList<PhoneAlbum>) bundle.getSerializable(getString(R.string.albums));

        iSearchPresenter = new ISearchPresenter(this);
        iSearchPresenter.onCreate();
    }

    @Override
    public void onBackPressed() {
        if (toolbar.getVisibility() == View.VISIBLE) {
            unSelectAll();
            toggleActionMode();
        } else {
            Navigator.with(this).utils().finishWithAnimation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery_action_mode, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_select_all:
                return true;
            case R.id.app_bar_delete:
                return true;
            case R.id.app_bar_move_to_private:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void configViews() {
        setSupportActionBar(toolbar);

        toolbar.setVisibility(View.INVISIBLE);

        placeHolderJ = new PlaceHolderJ(this, R.id.searchMedia, UGallery.getPlaceHolderManager());
        placeHolderJ.init(R.id.view_loading, R.id.view_empty, R.id.view_error);

        placeHolderJ.viewEmptyTryAgainButton.setBackgroundTintList(ColorStateList.valueOf(Res.getColor(R.color.colorAccent)));
        placeHolderJ.viewEmptyTryAgainButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        placeHolderJ.viewErrorTryAgainButton.setBackgroundTintList(ColorStateList.valueOf(Res.getColor(R.color.colorAccent)));
        placeHolderJ.viewErrorTryAgainButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        placeHolderJ.showEmpty(null);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mediaSearchAdapter = iSearchPresenter.makeQuery(s, phoneAlbumArrayList);
                mediaSearchAdapter.setOnItemClickListener(SearchActivity.this);

                searchMedia.setAdapter(mediaSearchAdapter);
                searchMedia.setHasFixedSize(true);
                searchMedia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    showMicrophone = true;
                    btnSearch.setImageDrawable(Res.getDrawable(R.drawable.ic_keyboard_voice_black_24dp));
                    placeHolderJ.showEmpty(null);

                    unSelectAll();
                } else {
                    showMicrophone = false;
                    btnSearch.setImageDrawable(Res.getDrawable(R.drawable.ic_close_black_24dp));
                    placeHolderJ.hideEmpty();
                }
            }
        });
    }

    @Override
    public void toggleActionMode() {
        if (onLongClickPressed) {
            showActionMode();
        } else {
            hideActionMode();
        }
    }

    @Override
    public void showActionMode() {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_gallery_action_mode);

        toolbar.setVisibility(View.VISIBLE);

        actionModeContainer.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideActionMode() {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_gallery);

        toolbar.setVisibility(View.INVISIBLE);

        actionModeContainer.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void toggleSelection() {
        if (onLongClickPressed) {
            unSelectAll();
        } else {
            selectAll();
        }
    }

    @Override
    public void selectAll() {
        onLongClickPressed = true;

        for (int i = 0; i < mediaSearchAdapter.getItemCount(); i++) {
            mediaSearchAdapter.setSelected(i, true);
        }

        mediaSearchAdapter.notifyDataSetChanged();

        onClickPressed(mediaSearchAdapter.getSelectedItems());
        onLongClickPressed(onLongClickPressed);
    }

    @Override
    public void unSelectAll() {
        onLongClickPressed = false;

        mediaSearchAdapter.clearSelected();

        for (int i = 0; i < mediaSearchAdapter.getItemCount(); i++) {
            mediaSearchAdapter.setChecked(i, false);
        }

        mediaSearchAdapter.notifyDataSetChanged();

        onClickPressed(mediaSearchAdapter.getSelectedItems());
        onLongClickPressed(onLongClickPressed);
    }

    @OnClick({R.id.btnBack, R.id.btnSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnSearch:
                if (showMicrophone) {

                } else {
                    showMicrophone = true;
                    btnSearch.setImageDrawable(Res.getDrawable(R.drawable.ic_keyboard_voice_black_24dp));
                    placeHolderJ.showEmpty(null);
                    searchView.getText().clear();
                }
                break;
        }
    }

    @OnClick(R.id.btnBackActionMode)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onItemClick(MediaSearchAdapter.ViewHolder viewHolder, View itemView, int position) {
        if (onLongClickPressed) {
            mediaSearchAdapter.toggleSelected(position);

            onClickPressed(mediaSearchAdapter.getSelectedItems());
        } else {
            //showMessage("Open Album");
        }
    }

    private void onClickPressed(int selectedItems) {
        if (selectedItems == 0) {
            tvTitleActionMode.setText(getString(R.string.select_item_title));
        } else {
            tvTitleActionMode.setText(String.valueOf(selectedItems));
        }
    }

    @Override
    public void onLongItemClick(MediaSearchAdapter.ViewHolder viewHolder, View itemView, int position) {
        if (onLongClickPressed) {
            unSelectAll();
        } else {
            select(position);
        }

        onLongClickPressed(onLongClickPressed);
    }

    private void select(int position) {
        onLongClickPressed = true;

        mediaSearchAdapter.clearSelected();

        mediaSearchAdapter.setSelected(position, true);

        mediaSearchAdapter.notifyDataSetChanged();

        onClickPressed(mediaSearchAdapter.getSelectedItems());
        onLongClickPressed(onLongClickPressed);
    }

    private void onLongClickPressed(Boolean onLongClickPressed) {
        this.onLongClickPressed = onLongClickPressed;

        toggleActionMode();
    }
}
