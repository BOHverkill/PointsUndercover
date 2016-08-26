package org.bohverkill.pointsundercover.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import org.bohverkill.pointsundercover.R;
import org.bohverkill.pointsundercover.model.User;
import org.bohverkill.pointsundercover.util.Persistence;

import java.util.List;

public class StopPicker extends DialogFragment {

//    public static int UNSET = 0;
//    public static int AGENT = R.string.agent;
//    public static int GROUP = R.string.group;

    private StopPickListener mCallback;

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
            this.mCallback = (StopPickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CountdownPickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Persistence persistence = new Persistence(getContext());
        List<User> users = persistence.getUsers("users");
        ArrayAdapter<User> userArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, users);

        // Use the Builder class for convenient dialog construction
        //final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Dialog);
        builder.setTitle(R.string.pick_stop);
        builder.setAdapter(userArrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                mCallback.onStopPicked(index);
            }
        });

        return builder.create();
    }

    public interface StopPickListener {
        void onStopPicked(int index);
    }
}
