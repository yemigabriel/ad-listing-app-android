//package com.doxa360.android.dutch.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.doxa360.android.dutch.R;
//import com.doxa360.android.dutch.adapter.viewholder.CategoryViewHolder;
//import com.doxa360.android.dutch.adapter.viewholder.SubCategoryViewHolder;
//import com.doxa360.android.dutch.model.Category;
//import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
//import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
//
//import java.util.List;
//
///**
// * Created by Apple on 03/06/2017.
// */
//
//public class CategoryAdapter extends ExpandableRecyclerViewAdapter<CategoryViewHolder, SubCategoryViewHolder> {
//
//    private LayoutInflater mLayoutInflater;
//    Context mContext;
//
//    public CategoryAdapter(List<? extends ExpandableGroup> groups, Context context) {
//        super(groups);
//        mContext = context;
//        mLayoutInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
//        View view = mLayoutInflater.inflate(R.layout.list_item_genre, parent, false);
//        return new CategoryViewHolder(view);
//    }
//
//    @Override
//    public SubCategoryViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
//        View view = mLayoutInflater.inflate(R.layout.list_item_artist, parent, false);
//        return new SubCategoryViewHolder(view);
//    }
//
//    @Override
//    public void onBindChildViewHolder(SubCategoryViewHolder holder, int flatPosition, ExpandableGroup group,
//                                      int childIndex) {
//        Category artist = ((Category) group).getItems().get(childIndex);
//        holder.setSubCategoryName(artist.getName());
//    }
//
//    @Override
//    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition,
//                                      ExpandableGroup group) {
//        holder.setCategoryTitle(group);
//    }
//}