package com.liteon.com.icampusteacher.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liteon.com.icampusteacher.App;
import com.liteon.com.icampusteacher.MainActivity;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.db.DBHelper;
import com.liteon.com.icampusteacher.util.ContactItem;
import com.liteon.com.icampusteacher.util.ContactItemAdapter;
import com.liteon.com.icampusteacher.util.ContactItemSearchAdapter;
import com.liteon.com.icampusteacher.util.Def;
import com.liteon.com.icampusteacher.util.JSONResponse;
import com.liteon.com.icampusteacher.util.SearchItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static final Comparator<SearchItem> ALPHABETICAL_COMPARATOR = (a, b) -> a.getContent().compareTo(b.getContent());
    private ImageView mCancel;
    private EditText mInput;
    private RecyclerView mSearchList;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<JSONResponse.Contents> mContactItems;
    private List<JSONResponse.Student> mStudentItems;
    private List<SearchItem> mSearchItems;
    private ContactItemSearchAdapter.ViewHolder.IHealthViewHolderClicks mOnItemClickListener;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private TextView mTitleView;
    private DBHelper mDbHelper;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        findViews(rootView);
        setListener();
        mTitleView.setText(R.string.search);
        mDbHelper = DBHelper.getInstance(getActivity());
        return rootView;
    }

    private void findViews(View rootView) {
        mCancel = rootView.findViewById(R.id.btn_cancel);
        mInput = rootView.findViewById(R.id.search_title);
        mSearchList = rootView.findViewById(R.id.list_search);
        mToolbar = getActivity().findViewById(R.id.toolbar);
        mTitleView = getActivity().findViewById(R.id.toolbar_title);
        mDrawer = getActivity().findViewById(R.id.drawer_layout);
    }

    private void setRecyclerView() {
        mSearchList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mSearchList.setLayoutManager(mLayoutManager);
        mAdapter = new ContactItemSearchAdapter(mOnItemClickListener, ALPHABETICAL_COMPARATOR);
        ((ContactItemSearchAdapter)mAdapter).add(mSearchItems);
        mSearchList.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreData();
        setRecyclerView();
    }

    @Override
    public void onPause() {
        super.onPause();
        //saveData();
    }

    private void setListener() {

        mToolbar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> mDrawer.openDrawer(Gravity.LEFT));

        mCancel.setOnClickListener( v -> {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
            mInput.setText("");
        });

        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final List<SearchItem> filteredModelList = filter(mSearchItems, editable.toString());
                ((ContactItemSearchAdapter)mAdapter).replaceAll(filteredModelList);
                mSearchList.scrollToPosition(0);
            }
        });

        mOnItemClickListener = item -> {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
            SharedPreferences sp = getActivity().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (item.isStudent()) {
                editor.putString(Def.SP_SEARCH_STUDENT_NAME, item.getId());
                editor.commit();
                BottomNavigationView bottomView = ((MainActivity)getActivity()).findViewById(R.id.bottom_navigation);
                bottomView.setSelectedItemId(R.id.action_myclass);
                ((MainActivity)getActivity()).changeFragment(StudentDetailFragment.newInstance(item.getContent(), item.getId()));
                editor.putString(Def.SP_STUDENT_NAME, item.getContent());
                editor.putString(Def.SP_STUDENT_ID, item.getId());
                editor.commit();
            } else {
                editor.putString(Def.SP_SEARCH_REMINDER_ID, item.getId());
                editor.commit();
                BottomNavigationView bottomView = ((MainActivity)getActivity()).findViewById(R.id.bottom_navigation);
                bottomView.setSelectedItemId(R.id.action_contact);
            }


        };
    }

    private void restoreData() {
        mContactItems = mDbHelper.queryReminderData(mDbHelper.getReadableDatabase());
        mStudentItems = mDbHelper.queryChildList(mDbHelper.getReadableDatabase());
        mSearchItems = new ArrayList<>();
        for (JSONResponse.Contents item : mContactItems) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId(item.getReminder_id());
            searchItem.setContent(item.getComments());
            mSearchItems.add(searchItem);
        }
        for (JSONResponse.Student item : mStudentItems) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId(item.getStudent_id());
            searchItem.setContent(item.getName());
            searchItem.setStudent(true);
            mSearchItems.add(searchItem);
        }
    }

    private static List<SearchItem> filter(List<SearchItem> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<SearchItem> filteredModelList = new ArrayList<>();
        for (SearchItem model : models) {
            final String text = model.getContent().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
