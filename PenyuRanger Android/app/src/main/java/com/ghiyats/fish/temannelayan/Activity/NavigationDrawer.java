package com.ghiyats.fish.temannelayan.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ghiyats.fish.temannelayan.Fragment.AboutFragment;
import com.ghiyats.fish.temannelayan.Fragment.AccountFragment;
import com.ghiyats.fish.temannelayan.Fragment.KonservasiFragment;
import com.ghiyats.fish.temannelayan.Fragment.LegalFragment;
import com.ghiyats.fish.temannelayan.Fragment.LocationFragment;
import com.ghiyats.fish.temannelayan.Fragment.MainFragment;
import com.ghiyats.fish.temannelayan.Fragment.RangerFragment;
import com.ghiyats.fish.temannelayan.Helper.RangerDbHelper;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.R;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

/**
 * Created by Ghiyats on 5/24/2015.
 */
public class NavigationDrawer extends MaterialNavigationDrawer {



    Bitmap avatar = Bitmap.createBitmap(60,60, Bitmap.Config.ARGB_8888);
    MaterialAccount acc;
    @Override
    public void init(Bundle savedInstanceState) {
        allowArrowAnimation();

        final SessionManager session = new SessionManager(this);
        RangerDbHelper rangerHelper = new RangerDbHelper(this);
        final RangerModel ranger = rangerHelper.get(session.getUserId());
        setDrawerHeaderImage(R.drawable.header);

        MaterialSectionListener logoutListener = new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                session.destroySession();
                Log.d(this.getClass().getSimpleName(), "user logged out");
                Intent in = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(in);
                finish();
            }
        };


        acc = new MaterialAccount(getResources(), session.getUsername().toUpperCase(), ranger.getMemberOf().getNamaKonservasi(),null, R.drawable.penyu_header);
        addAccount(acc);

        Glide.with(this)
                .load(ranger.getThumbnail())
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.account_placeholder)
                .into(new SimpleTarget<Bitmap>(100,100) {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        acc.setPhoto(resource);
                    }
                });



        MaterialSection location_section = this.newSection(getResources().getString(R.string.menu_daftar_lokasi),
                getResources().getDrawable(R.drawable.ic_location_on_white_48dp),
                new LocationFragment());
        addSection(location_section);

        MaterialSection compass_section = this.newSection(getResources().getString(R.string.menu_konservasi),
                getResources().getDrawable(R.drawable.ic_people_white_48dp),
                new KonservasiFragment());
        addSection(compass_section);

        MaterialSection acc_section = newSection(getResources().getString(R.string.menu_akun),
                getResources().getDrawable(R.drawable.ic_account_box_white_48dp),
                new AccountFragment());
        addSection(acc_section);

        this.addDivisor();

        MaterialSection about_section = newSection("About",
                getResources().getDrawable(R.drawable.ic_info_white_48dp),
                new AboutFragment());
        addSection(about_section);

        MaterialSection legal_section = newSection("Legal",
                getResources().getDrawable(R.drawable.ic_subject_white_48dp),
                new LegalFragment());
        addSection(legal_section);

        MaterialSection logout_section = newSection("Logout",logoutListener);
        addBottomSection(logout_section);

        setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

    }
}

