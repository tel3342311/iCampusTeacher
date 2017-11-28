package com.liteon.com.icampusteacher.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liteon.com.icampusteacher.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactMatterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactMatterFragment extends Fragment {

    public ContactMatterFragment() {
        // Required empty public constructor
    }

    public static ContactMatterFragment newInstance() {
        ContactMatterFragment fragment = new ContactMatterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_matter, container, false);
    }

}
