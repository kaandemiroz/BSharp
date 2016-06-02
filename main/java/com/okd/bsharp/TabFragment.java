package com.okd.bsharp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TabFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TabAdapter mTabAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);

        LinearLayout tabArea = (LinearLayout) rootView.findViewById(R.id.tab_area);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_tab, container, false);
        TabItemHolder holder = new TabItemHolder(view);
        holder.bindTabItem(new TabItem("E","B","G","D","A","E"));
//        holder.setUneditable();
        tabArea.addView(view,0);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mTabAdapter = new TabAdapter();
        mRecyclerView.setAdapter(mTabAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p/>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return rootView;
    }

    public void addTabItem(int string, int position){
        mTabAdapter.addTabItem(new TabItem(string, position));
        mRecyclerView.smoothScrollToPosition(mRecyclerView.getChildCount());
    }

}
