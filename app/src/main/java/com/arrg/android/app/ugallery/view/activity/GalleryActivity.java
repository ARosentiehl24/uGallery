package com.arrg.android.app.ugallery.view.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arrg.android.app.ugallery.R;
import com.arrg.android.app.ugallery.interfaces.GalleryView;
import com.arrg.android.app.ugallery.presenter.IGalleryPresenter;
import com.arrg.android.app.ugallery.view.fragment.GalleryFragment;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.menu.BottomSheetMenuItem;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.shawnlin.preferencesmanager.PreferencesManager;
import com.thefinestartist.utils.content.Res;
import com.zhy.autolayout.AutoLinearLayout;

import org.fingerlinks.mobile.android.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActivity extends AppCompatActivity implements BottomSheetListener, GalleryFragment.GalleryFragmentListener, GalleryView, SpaceOnClickListener {

    @BindView(R.id.btnBackActionMode)
    ImageButton btnBackActionMode;
    @BindView(R.id.tvItemCount)
    TextView tvItemCount;
    @BindView(R.id.actionModeContainer)
    AutoLinearLayout actionModeContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spaceNavigationView)
    SpaceNavigationView spaceNavigationView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    private Boolean isLongClickPressed = false;
    private List<ResolveInfo> resolveInfoList;
    private IGalleryPresenter iGalleryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        iGalleryPresenter = new IGalleryPresenter(this);
        iGalleryPresenter.onCreate();
    }

    @Override
    public void onBackPressed() {
        if (isLongClickPressed) {
            GalleryFragment galleryFragment = (GalleryFragment) getFragment(GalleryFragment.class);
            if (galleryFragment != null) {
                galleryFragment.unSelectAll();
            }
        } else {
            Navigator.with(this).utils().finishWithAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        iGalleryPresenter.onOptionsItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void configViews() {
        setSupportActionBar(toolbar);

        showHome();

        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_home), R.drawable.ic_home_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_favorite), R.drawable.ic_favorite_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_search), R.drawable.ic_search_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.space_nv_item_edit), R.drawable.ic_edit_black_24dp));

        spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setCentreButtonIcon(R.drawable.ic_photo_camera_black_24dp);
        spaceNavigationView.setCentreButtonIconColor(ContextCompat.getColor(this, R.color.colorAccent));
        spaceNavigationView.setCentreButtonRippleColor(ContextCompat.getColor(this, R.color.black75PercentColor));
        spaceNavigationView.setSpaceBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        spaceNavigationView.setActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setInActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setSpaceOnClickListener(this);
    }

    @Override
    public void showHome() {
        Navigator.with(this).build().goTo(new GalleryFragment(), R.id.container).tag(GalleryFragment.class.getSimpleName()).replace().commit();
    }

    @Override
    public void launchActivity(Class activity, Bundle bundle) {
        if (bundle != null) {
            Navigator.with(this).build().goTo(activity, bundle).animation().commit();
        } else {
            Navigator.with(this).build().goTo(activity).animation().commit();
        }
    }

    @Override
    public void launchPackage(String packageName) {
        Intent launchPackage = getPackageManager().getLaunchIntentForPackage(packageName);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(launchPackage);
    }

    @Override
    public void showCameraSelector(ArrayList<BottomSheetMenuItem> bottomSheetMenuItems, List<ResolveInfo> infoList) {
        this.resolveInfoList = infoList;

        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setTitle(R.string.select_camera_title);

        for (BottomSheetMenuItem bottomSheetMenuItem : bottomSheetMenuItems) {
            builder.addMenuItem(bottomSheetMenuItem);
        }

        if (bottomSheetMenuItems.size() > Res.getInteger(R.integer.default_bottom_sheet_grid)) {
            builder.setColumnCountResource(R.integer.default_bottom_sheet_grid);
        } else {
            builder.setColumnCount(bottomSheetMenuItems.size());
        }

        builder.grid();
        builder.setListener(this);
        builder.create();
        builder.show();
    }

    @Override
    public void showMessage(Integer message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Fragment getFragment(Class fragment) {
        return getSupportFragmentManager().findFragmentByTag(fragment.getSimpleName());
    }

    @Override
    public void onCentreButtonClick() {
        iGalleryPresenter.onCentreButtonClick(this);
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        iGalleryPresenter.onItemClick(itemIndex, itemName);
    }

    @Override
    public void onItemReselected(int itemIndex, String itemName) {
        iGalleryPresenter.onItemClick(itemIndex, itemName);
    }

    @Override
    public void onSheetShown(@NonNull BottomSheet bottomSheet) {

    }

    @Override
    public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {
        ResolveInfo resolveInfo = resolveInfoList.get(menuItem.getItemId());

        launchPackage(resolveInfo.activityInfo.packageName);

        PreferencesManager.putString(getString(R.string.default_camera), resolveInfo.activityInfo.packageName);
    }

    @Override
    public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @DismissEvent int i) {

    }

    @Override
    public void onClickPressed(int itemsSelected) {
        tvItemCount.setText(String.valueOf(itemsSelected));
    }

    @Override
    public void onLongClickPressed(boolean onLongClickPressed) {
        this.isLongClickPressed = onLongClickPressed;

        toggleActionMode();
    }

    @Override
    public void toggleActionMode() {
        if (isLongClickPressed) {
            showActionMode();
        } else {
            hideActionMode();
        }
    }

    @Override
    public void hideActionMode() {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_gallery);

        actionModeContainer.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void showActionMode() {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_gallery_action_mode);

        actionModeContainer.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btnBackActionMode)
    public void onClick() {
        hideActionMode();
    }
}
