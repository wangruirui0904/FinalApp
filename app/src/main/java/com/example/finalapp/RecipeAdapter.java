package com.example.finalapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private int resourceLayout;
    private LayoutInflater layoutInflater;

    public RecipeAdapter(Context context, int resource, List<Recipe> recipes) {
        super(context, resource, recipes);
        this.resourceLayout = resource;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(resourceLayout, null);
        }

        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewIngredients = convertView.findViewById(R.id.textViewIngredients);

        Recipe recipe = getItem(position);
        textViewTitle.setText(recipe.getTitle());
        textViewIngredients.setText(recipe.getIngredients());

        return convertView;
    }
}