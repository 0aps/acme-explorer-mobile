package com.mis.acmeexplorer.trips;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mis.acmeexplorer.R;
import com.mis.acmeexplorer.core.DatePickerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "FilterDialog";

    public interface FilterListener {
        void onFilter(Filters filters);
    }

    final private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    private View mRootView;

    private EditText mTitleEditText;
    private EditText mMinPriceEditText;
    private EditText mMaxPriceEditText;
    private EditText mMinDateEditText;
    private EditText mMaxDateEditText;

    private FilterListener mFilterListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_filters, container, false);

        mTitleEditText = mRootView.findViewById(R.id.input_title);
        mMinPriceEditText = mRootView.findViewById(R.id.input_min_price);
        mMaxPriceEditText = mRootView.findViewById(R.id.input_max_price);
        mMinDateEditText = mRootView.findViewById(R.id.input_min_date);
        mMaxDateEditText = mRootView.findViewById(R.id.input_max_date);

        mMinDateEditText.setOnClickListener(this);
        mMaxDateEditText.setOnClickListener(this);
        mRootView.findViewById(R.id.button_search).setOnClickListener(this);
        mRootView.findViewById(R.id.button_cancel).setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_search:
                onSearchClicked();
                break;

            case R.id.button_cancel:
                onCancelClicked();
                break;

            case R.id.input_min_date:
                onDateFieldClicked(mMinDateEditText);
                break;

            case R.id.input_max_date:
                onDateFieldClicked(mMaxDateEditText);
                break;
        }
    }

    public Filters getFilters() {
        Filters filters = new Filters();

        if (mRootView != null) {
            filters.setTitle(getSelectedTitle());
            filters.setMinPrice(getSelectedMinPrice());
            filters.setMaxPrice(getSelectedMaxPrice());
            try {
                filters.setMinDate(getSelectedMinDate());
                filters.setMaxDate(getSelectedMaxDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return filters;
    }

    public void onSearchClicked() {
        if (mFilterListener != null) {
            mFilterListener.onFilter(getFilters());
        }

        dismiss();
    }

    public void onCancelClicked() {
        dismiss();
    }


    public void resetFilters() {
        if (mRootView != null) {
            mTitleEditText.setText("");
            mMinPriceEditText.setText("");
            mMaxPriceEditText.setText("");
            mMinDateEditText.setText("");
            mMaxDateEditText.setText("");
        }
    }

    @Nullable
    private String getSelectedTitle() {
        return mTitleEditText.getText().toString();
    }

    private int getSelectedMinPrice() {
        final String value = mMinPriceEditText.getText().toString();
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    private int getSelectedMaxPrice() {
        final String value = mMaxPriceEditText.getText().toString();
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    private Date getSelectedMinDate() throws ParseException {
        final String value = mMinDateEditText.getText().toString();
        return formatter.parse(value);
    }

    private Date getSelectedMaxDate() throws ParseException {
        final String value = mMaxDateEditText.getText().toString();
        return formatter.parse(value);
    }

    private void onDateFieldClicked(EditText editText) {
        DatePickerFragment newFragment = new DatePickerFragment(new DatePickerFragment.OnDateListener() {
            @Override
            public void onSelected(int year, int month, int day) {
                editText.setText(String.format(Locale.getDefault(),
                        "%d/%d/%d", month + 1, day, year));
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

}