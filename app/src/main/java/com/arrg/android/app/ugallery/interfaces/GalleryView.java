package com.arrg.android.app.ugallery.interfaces;

import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kennyc.bottomsheet.menu.BottomSheetMenuItem;

import java.util.ArrayList;
import java.util.List;

public interface GalleryView {

    void configViews();

    void showHome();

    void launchActivity(Class activity, Bundle bundle);

    void launchPackage(String packageName);

    void showCameraSelector(ArrayList<BottomSheetMenuItem> bottomSheetMenuItems, List<ResolveInfo> infoList);

    void showMessage(Integer message);

    void showMessage(String message);

    void toggleActionMode();

    void showActionMode();

    void hideActionMode();

    Fragment getFragment(Class fragment);
}
