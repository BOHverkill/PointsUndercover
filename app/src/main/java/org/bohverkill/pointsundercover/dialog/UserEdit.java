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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.bohverkill.pointsundercover.R;
import org.bohverkill.pointsundercover.model.User;

public class UserEdit extends DialogFragment {

    UserEditListener mCallback;

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
            this.mCallback = (UserEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement UserEditListener");
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
        final View view = inflater.inflate(R.layout.user_edit, nullParent);
        builder.setView(view);

        final int position = getArguments().getInt("position");
        User selectedUser = this.mCallback.getSelectedUser(position);

        // Set the dialog title
        builder.setTitle(R.string.round_time);

        final EditText editUserName = (EditText) view.findViewById(R.id.editUserName);

        editUserName.setText(selectedUser.toString());

        // Set the action buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mCallback.onUserChanged(new User(editUserName.getText().toString()), position);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // Set the neutral button to the delete button if there are enough users in the list
        if (this.mCallback.getUserCount() > getResources().getInteger(R.integer.user_default_min_list_size)) {
            builder.setNeutralButton(R.string.delete_user, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    mCallback.onDeleteUser(position);
                }
            });
        }

        return builder.create();
    }

    public interface UserEditListener {
        void onUserChanged(User user, int position);

        void onDeleteUser(int position);

        User getSelectedUser(int position);

        int getUserCount();
    }
}
