package org.bohverkill.pointsundercover.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import org.bohverkill.pointsundercover.R;
import org.bohverkill.pointsundercover.model.Countdown;

public class CountdownPicker extends DialogFragment {

    CountdownPickerListener mCallback;

    // https://code.google.com/p/android/issues/detail?id=183358#c38
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        //do what you want / cast fragment listener
        try {
            this.mCallback = (CountdownPickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CountdownPickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        //final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Dialog);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final ViewGroup nullParent = null;
        final View view = inflater.inflate(R.layout.countdown_picker, nullParent);
        builder.setView(view);

        Countdown previouslyPickedCountdown = this.mCallback.getPreviouslyPicked();

        // Set the dialog title
        builder.setTitle(R.string.round_time);

        //TODO set better max values for countdown pickers
        final NumberPicker countdownMinutesPicker = (NumberPicker) view.findViewById(R.id.countdownMinutesPicker);
        countdownMinutesPicker.setMaxValue(59);
        countdownMinutesPicker.setMinValue(0);
        countdownMinutesPicker.setValue(previouslyPickedCountdown.getMinutes());

        final NumberPicker countdownSecondsPicker = (NumberPicker) view.findViewById(R.id.countdownSecondsPicker);
        countdownSecondsPicker.setMaxValue(59);
        countdownSecondsPicker.setMinValue(0);
        countdownSecondsPicker.setValue(previouslyPickedCountdown.getSeconds());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.setDividerColor(countdownMinutesPicker, getResources().getColor(R.color.colorAccent, null));
            this.setDividerColor(countdownSecondsPicker, getResources().getColor(R.color.colorAccent, null));
        } else {
            //noinspection deprecation
            this.setDividerColor(countdownMinutesPicker, getResources().getColor(R.color.colorAccent));
            //noinspection deprecation
            this.setDividerColor(countdownSecondsPicker, getResources().getColor(R.color.colorAccent));
        }

        // Set the action buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mCallback.onCountdownPicked(new Countdown(countdownMinutesPicker.getValue(), countdownSecondsPicker.getValue()));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }


    // http://stackoverflow.com/a/29517329
    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public interface CountdownPickerListener {
        void onCountdownPicked(Countdown countdown);

        Countdown getPreviouslyPicked();
    }
}
