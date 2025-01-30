package com.jatora.tfg_the_climb_within;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTowerSelectionItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTowerSelectionItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "img";
    private static final String ARG_PARAM2 = "unlocked";

    // TODO: Rename and change types of parameters
    String img;
    boolean unlocked;

    public FragmentTowerSelectionItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param img Parameter 1.
     * @param unlocked Parameter 2.
     * @return A new instance of fragment FragmentTowerSelectionItem.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTowerSelectionItem newInstance(String img, boolean unlocked) {
        FragmentTowerSelectionItem fragment = new FragmentTowerSelectionItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, img);
        args.putBoolean(ARG_PARAM2, unlocked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            img = getArguments().getString(ARG_PARAM1);
            unlocked = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tower_selection_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView towerImg = view.findViewById(R.id.towerImg);
        View isLocked = view.findViewById(R.id.isLocked);

        // set tower img
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getAssets().open(this.img));
//            Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getAssets().open("img/towers/calm.png"));
            Log.d("AAAAAAAAAAAAAAAAa", "bitmap state: "+bitmap);
            Log.d("AAAAAAAAAAAAAAAAa", "image container state: "+towerImg);
            towerImg.setImageBitmap(bitmap);

            if (!unlocked) {
                isLocked.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            Log.e("FragmentTowerSelectionItem", "Error while getting bitmap image from assets: " + e);
        }
    }
}