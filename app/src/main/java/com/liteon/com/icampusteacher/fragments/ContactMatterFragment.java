package com.liteon.com.icampusteacher.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liteon.com.icampusteacher.App;
import com.liteon.com.icampusteacher.LoginActivity;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.db.DBHelper;
import com.liteon.com.icampusteacher.util.ConfirmDeleteDialog;
import com.liteon.com.icampusteacher.util.ContactItem;
import com.liteon.com.icampusteacher.util.ContactItemAdapter;
import com.liteon.com.icampusteacher.util.Def;
import com.liteon.com.icampusteacher.util.GuardianApiClient;
import com.liteon.com.icampusteacher.util.HealthyItemAdapter;
import com.liteon.com.icampusteacher.util.JSONResponse;
import com.liteon.com.icampusteacher.util.Utils;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactMatterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactMatterFragment extends Fragment {

    private RecyclerView mContactList;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ImageView mCancel;
    private ImageView mConfirm;
    private TextView mDateTitle;
    private EditText mInputField;
    private List<JSONResponse.Contents> mContactItems;
    private JSONResponse.Contents mCurrentItem;
    private ContactItemAdapter.ViewHolder.IHealthViewHolderClicks mOnItemClickListener;
    private BottomSheetBehavior bottomSheetBehavior;
    private Button mDelete;
    private Button mEdit;
    private View mSepLine;
    private ConfirmDeleteDialog mConfirmDialog;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private TextView mTitleView;
    private DBHelper mDbHelper;
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
        View rootView = inflater.inflate(R.layout.fragment_contact_matter, container, false);
        findViews(rootView);
        setListener();
        mTitleView.setText(R.string.contact_matters);
        mDbHelper = DBHelper.getInstance(getActivity());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreData();
        resetItemToday();
        setRecyclerView();
        mConfirm.setEnabled(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void resetItemToday() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd EE HH:mm");
        String currentDate = sdf.format(date);
        mDateTitle.setText(currentDate);
        mCurrentItem = new JSONResponse.Contents();
        mCurrentItem.setCreated_date(currentDate);
        mCurrentItem.setComments("");
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    private void findViews(View rootView) {
        mCancel = rootView.findViewById(R.id.btn_cancel);
        mConfirm = rootView.findViewById(R.id.btn_confirm);
        mDateTitle = rootView.findViewById(R.id.date_title);
        mContactList = rootView.findViewById(R.id.list_contact);
        mInputField =rootView.findViewById(R.id.matter_input);
        bottomSheetBehavior = BottomSheetBehavior.from(rootView.findViewById(R.id.bottom_sheet_layout));
        mDelete = rootView.findViewById(R.id.delete);
        mEdit = rootView.findViewById(R.id.edit);
        mSepLine = rootView.findViewById(R.id.sep_line);
        mToolbar = getActivity().findViewById(R.id.toolbar);
        mTitleView = getActivity().findViewById(R.id.toolbar_title);
        mDrawer = getActivity().findViewById(R.id.drawer_layout);
    }

    private void restoreData() {
//        SharedPreferences sp = App.getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
//        String listStr = sp.getString(Def.SP_CONTACT_LIST, "");
//        Gson gson = new GsonBuilder().create();
//        Type typeOfList = new TypeToken<List<ContactItem>>() { }.getType();
//        mContactItems = gson.fromJson(listStr, typeOfList);
//        if (mContactItems == null) {
//            mContactItems = new ArrayList<>();
//        }
        mContactItems = mDbHelper.queryReminderData(mDbHelper.getReadableDatabase());
    }

    private void saveData() {
//        Gson gson = new Gson();
//        String input = gson.toJson(mContactItems);
//        SharedPreferences sp = App.getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(Def.SP_CONTACT_LIST, input);
//        editor.commit();
    }

    private void setListener() {
        mToolbar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> mDrawer.openDrawer(Gravity.LEFT));

        mCancel.setOnClickListener(v -> {
            mInputField.setText("");
            mCancel.setEnabled(false);
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mInputField.getWindowToken(), 0);
            resetItemToday();
        });

        mConfirm.setOnClickListener( v -> {
            String content = mInputField.getText().toString();
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd EE HH:mm");
            String currentDate = sdf.format(date);
            mCurrentItem.setComments(content);
            mCurrentItem.setCreated_date(currentDate);
            mInputField.setText("");
            new CreateReminderTask().execute(mCurrentItem);

        });

        mInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCancel.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (TextUtils.isEmpty(input) || TextUtils.equals(mCurrentItem.getComments(), input)) {
                    mConfirm.setEnabled(false);
                } else {
                    mConfirm.setEnabled(true);
                    mConfirm.setVisibility(View.VISIBLE);
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd EE HH:mm");
                    String currentDate = sdf.format(date);
                    mDateTitle.setText(currentDate);
                }
            }
        });

        mOnItemClickListener = item -> {
            mCurrentItem = item;
            mDateTitle.setText(mCurrentItem.getCreated_date());
            mInputField.setText(mCurrentItem.getComments());
            mConfirm.setVisibility(View.INVISIBLE);
            mConfirm.setEnabled(false);
            mContactList.setVisibility(View.GONE);
            mSepLine.setVisibility(View.GONE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        };

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mContactList.setVisibility(View.VISIBLE);
                    mSepLine.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    mContactList.setVisibility(View.GONE);
                    mSepLine.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mDelete.setOnClickListener(v -> {
            showConfirmDialog();
        });

        mEdit.setOnClickListener(v -> {
            mConfirm.setVisibility(View.VISIBLE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            mContactList.setVisibility(View.VISIBLE);
            mSepLine.setVisibility(View.VISIBLE);
        });
    }

    private void setRecyclerView() {
        mContactList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mContactList.setLayoutManager(mLayoutManager);
        mAdapter = new ContactItemAdapter(mContactItems, mOnItemClickListener);
        mContactList.setAdapter(mAdapter);
    }

    private void showConfirmDialog() {
        mConfirmDialog = new ConfirmDeleteDialog();
        mConfirmDialog.setOnConfirmEventListener(v -> {
            new DeleteItemTask().execute(mCurrentItem);
            mConfirmDialog.dismiss();
        });
        mConfirmDialog.setmOnCancelListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            mConfirmDialog.dismiss();
        });
        mConfirmDialog.setmTitleText(getString(R.string.delete_confirm) + "\n" + mCurrentItem.getCreated_date());
        mConfirmDialog.setmBtnConfirmText(getString(android.R.string.ok));
        mConfirmDialog.setmBtnCancelText(getString(android.R.string.cancel));
        mConfirmDialog.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
    }

    class DeleteItemTask extends AsyncTask<JSONResponse.Contents, Void, Boolean> {

        private String mErrorMessage;
        @Override
        protected Boolean doInBackground(JSONResponse.Contents... contents) {
            JSONResponse.Contents item = contents[0];
            GuardianApiClient apiClient = GuardianApiClient.getInstance(getActivity());
            if (!TextUtils.isEmpty(item.getReminder_id())) {
                JSONResponse response = apiClient.deleteReminder(item.getReminder_id());
                if (response == null) {
                    mErrorMessage = getString(R.string.login_error_no_server_connection);
                    return false;
                }
                if (TextUtils.equals(response.getReturn().getResponseSummary().getStatusCode(), Def.RET_ERR_01)) {
                    mErrorMessage = getString(R.string.healthy_sync_failed);
                    return false;
                }
                if (TextUtils.equals(Def.RET_SUCCESS_1, response.getReturn().getResponseSummary().getStatusCode())){
                    mDbHelper.deleteReminderById(mDbHelper.getWritableDatabase(), item.getReminder_id());
                    if (item != null && mContactItems.indexOf(item) != -1) {
                        mContactItems.remove(item);
                        return true;
                    }
                } else {
                    mErrorMessage = response.getReturn().getResponseSummary().getErrorMessage();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            resetItemToday();
            mInputField.setText("");
            mAdapter.notifyDataSetChanged();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            if (aBoolean.booleanValue() == false) {
                //show sync failed
                Utils.showErrorDialog(mErrorMessage);
            }
        }
    }

    class CreateReminderTask extends AsyncTask<JSONResponse.Contents, Void, Boolean> {

        private String mErrorMessage;
        @Override
        protected Boolean doInBackground(JSONResponse.Contents... contents) {
            JSONResponse.Contents item = contents[0];
            GuardianApiClient apiClient = GuardianApiClient.getInstance(getActivity());
            JSONResponse response = apiClient.createReminder(item.getComments(), item.getReminder_id());
            if (response == null) {
                mErrorMessage = getString(R.string.login_error_no_server_connection);
                return false;
            }
            if (TextUtils.equals(response.getReturn().getResponseSummary().getStatusCode(), Def.RET_ERR_01)) {
                mErrorMessage = getString(R.string.healthy_sync_failed);
                return false;
            }
            if (TextUtils.equals(response.getReturn().getResponseSummary().getStatusCode(), Def.RET_SUCCESS_1)){
                if (TextUtils.isEmpty(item.getReminder_id())) {
                    //New Item, Sync data again
                    JSONResponse response_reminder = apiClient.getReminders();
                    if (TextUtils.equals(response_reminder.getReturn().getResponseSummary().getStatusCode(), Def.RET_SUCCESS_1)) {
                        List<JSONResponse.Contents> reminderList = Arrays.asList(response_reminder.getReturn().getResults().getReminders()[0].getContents());
                        List<JSONResponse.Contents> newList = new ArrayList<>();
                        for (JSONResponse.Contents newItem : reminderList){
                            boolean isExist = false;
                            for (JSONResponse.Contents originItem : mContactItems){
                                if (TextUtils.equals(newItem.getReminder_id(), originItem.getReminder_id())){
                                    isExist = true;
                                    break;
                                }
                            }
                            if (!isExist) {
                                newList.add(0, newItem);
                            }
                        }
                        if (newList.size() > 0) {
                            mContactItems.addAll(newList);
                            mDbHelper.insertReminderList(mDbHelper.getWritableDatabase(), newList);
                        }
                    }
                } else {
                    if (mContactItems.indexOf(item) != -1) {
                        mDbHelper.updateReminderById(mDbHelper.getWritableDatabase(), item);
                    }
                }
                return true;
            } else {
                mErrorMessage = response.getReturn().getResponseSummary().getErrorMessage();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mCancel.setEnabled(false);
            mConfirm.setEnabled(false);
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mInputField.getWindowToken(), 0);
            resetItemToday();
            mAdapter.notifyDataSetChanged();
            if (aBoolean.booleanValue() == false) {
                //show sync failed
                Utils.showErrorDialog(mErrorMessage);
            }
        }
    }
}
