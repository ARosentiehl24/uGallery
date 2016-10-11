package com.arrg.android.app.ugallery.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;

import com.arrg.android.app.ugallery.R;
import com.arrg.android.app.ugallery.interfaces.GalleryPresenter;
import com.arrg.android.app.ugallery.interfaces.GalleryView;
import com.arrg.android.app.ugallery.model.entity.PhoneAlbum;
import com.arrg.android.app.ugallery.view.activity.SearchActivity;
import com.arrg.android.app.ugallery.view.fragment.GalleryFragment;
import com.kennyc.bottomsheet.menu.BottomSheetMenuItem;
import com.shawnlin.preferencesmanager.PreferencesManager;
import com.thefinestartist.Base;
import com.thefinestartist.utils.content.Res;

import org.fingerlinks.mobile.android.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

import static com.thefinestartist.utils.content.ContextUtil.getPackageManager;

public class IGalleryPresenter implements GalleryPresenter {

    private GalleryView galleryView;

    public IGalleryPresenter(GalleryView galleryView) {
        this.galleryView = galleryView;
    }

    @Override
    public void onCreate() {
        galleryView.configViews();
    }

    @Override
    public void onBackPressed() {
        Navigator.with(Base.getContext()).utils().finishWithAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onOptionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.app_bar_mode_private:
                break;
            case R.id.app_bar_settings:
                break;
        }
    }

    @Override
    public void onCentreButtonClick(Context context) {
        String cameraPackage = PreferencesManager.getString(Res.getString(R.string.default_camera));

        if (cameraPackage.length() == 0) {
            Intent imageCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Integer id = 0;

            ArrayList<BottomSheetMenuItem> bottomSheetMenuItems = new ArrayList<>();
            List<ResolveInfo> infoList = getPackageManager().queryIntentActivities(imageCapture, 0);

            for (ResolveInfo resolveInfo : infoList) {
                CharSequence name = resolveInfo.loadLabel(getPackageManager());
                Drawable icon = resolveInfo.loadIcon(getPackageManager());

                bottomSheetMenuItems.add(new BottomSheetMenuItem(context, id, name, icon));

                id++;
            }

            if (infoList.size() == 1) {
                galleryView.launchPackage(infoList.get(0).activityInfo.packageName);
            } else {
                galleryView.showCameraSelector(bottomSheetMenuItems, infoList);
            }
        } else {
            if (isPackageInstalled(cameraPackage)) {
                galleryView.launchPackage(cameraPackage);
            } else {
                PreferencesManager.putString(Res.getString(R.string.default_camera), "");

                onCentreButtonClick(context);
            }
        }
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        GalleryFragment galleryFragment = (GalleryFragment) galleryView.getFragment(GalleryFragment.class);

        switch (itemIndex) {
            case 0:
                if (galleryFragment == null) {
                    galleryView.showHome();
                }
                break;
            case 1:
                if (galleryFragment != null) {
                    galleryFragment.showFavoriteMedia();
                }
                break;
            case 2:
                ArrayList<PhoneAlbum> phoneAlbumArrayList = galleryFragment.getAlbums();

                Bundle bundle = new Bundle();
                bundle.putSerializable(Res.getString(R.string.albums), phoneAlbumArrayList);

                galleryView.launchActivity(SearchActivity.class, bundle);
                break;
            case 3:
                if (galleryFragment != null) {
                    galleryFragment.toggleSelection();
                }
                break;
        }
    }

    private boolean isPackageInstalled(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
}
