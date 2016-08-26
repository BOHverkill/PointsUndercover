package org.bohverkill.pointsundercover.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.bohverkill.pointsundercover.R;
import org.bohverkill.pointsundercover.model.User;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder>{
    private List<User> users;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.current_points)
        TextView currentPoints;
        @BindView(R.id.plus_points)
        TextView plusPoints;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, this.view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PointsAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PointsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.points_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.userName.setText(users.get(position).toString());
        holder.currentPoints.setText(String.format(Locale.getDefault(), "%s %d", context.getResources().getString(R.string.points), users.get(position).getPoints()));
        holder.plusPoints.setText(String.format(Locale.getDefault(), "%s %d", context.getResources().getString(R.string.plus_points), users.get(position).getPlusPoints()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.users.size();
    }

}
