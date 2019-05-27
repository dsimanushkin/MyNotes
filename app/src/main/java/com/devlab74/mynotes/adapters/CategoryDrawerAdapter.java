package com.devlab74.mynotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devlab74.mynotes.R;
import com.devlab74.mynotes.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDrawerAdapter extends RecyclerView.Adapter<CategoryDrawerAdapter.CategoryHolder> {

    private List<Category> categories = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public CategoryDrawerAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_drawer_list_item, parent, false);
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDrawerAdapter.CategoryHolder holder, int position) {
        Category currentCategory = categories.get(position);
        holder.categoryTitle.setText(currentCategory.getTitle());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        private TextView categoryTitle;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            categoryTitle = itemView.findViewById(R.id.drawer_category_title);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(categories.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
