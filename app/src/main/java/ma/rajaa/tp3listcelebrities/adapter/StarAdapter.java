package ma.rajaa.tp3listcelebrities.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.rajaa.tp3listcelebrities.R;
import ma.rajaa.tp3listcelebrities.classes.Star;
import ma.rajaa.tp3listcelebrities.service.StarService;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {
    private static final String TAG = "StarAdapter";
    private List<Star> stars;
    private List<Star> starsFull;
    private List<Star> starsFilter;
    private NewFilter mfilter;
    private Context context;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;
        this.stars = stars;
        this.starsFull = new ArrayList<>(stars);
        this.starsFilter = new ArrayList<>(stars);
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.star_item, parent, false);
        return new StarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        Star star = starsFilter.get(position);
        Glide.with(context)
                .load(star.getImg())
                .apply(new RequestOptions().override(100, 100).circleCrop())
                .into(holder.img);

        holder.name.setText(star.getName().toUpperCase());
        holder.stars.setRating(star.getStar());
        holder.idss.setText(String.valueOf(star.getId()));

        holder.itemView.setOnClickListener(v -> showRatingPopup(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return starsFilter.size();
    }

    @Override
    public Filter getFilter() {
        if (mfilter == null) {
            mfilter = new NewFilter(this);
        }
        return mfilter;
    }

    private void showRatingPopup(int position) {
        Star star = starsFilter.get(position);
        View popupView = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null, false);
        CircleImageView img = popupView.findViewById(R.id.img);
        RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);
        TextView idss = popupView.findViewById(R.id.idss);

        Glide.with(context)
                .load(star.getImg())
                .apply(new RequestOptions().override(100, 100).circleCrop())
                .into(img);
        ratingBar.setRating(star.getStar());
        idss.setText(String.valueOf(star.getId()));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Voulez-vous redonner une note à ce livre?")
                .setView(popupView)
                .setPositiveButton("Confirmer", (dialog1, which) -> {
                    float newRating = ratingBar.getRating();
                    star.setStar(newRating);
                    StarService.getInstance().update(star);
                    notifyItemChanged(position);
                })
                .setNegativeButton("Annuler", null)
                .create();

        dialog.show();
    }

    public class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        CircleImageView img;
        TextView name;
        RatingBar stars;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
        }
    }

    public class NewFilter extends Filter {
        private final RecyclerView.Adapter mAdapter;

        public NewFilter(RecyclerView.Adapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filteredList = new ArrayList<>();
            final FilterResults results = new FilterResults();

            String filterPattern = charSequence.toString().toLowerCase().trim();

            if (filterPattern.isEmpty()) {
                filteredList.addAll(starsFull);
            } else {
                for (Star star : starsFull) {
                    if (star.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(star);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter.clear();
            starsFilter.addAll((List<Star>) filterResults.values);
            mAdapter.notifyDataSetChanged();
        }
    }
}