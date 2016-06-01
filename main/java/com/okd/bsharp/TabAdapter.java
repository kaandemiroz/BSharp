package com.okd.bsharp;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OKD on 1.6.2016.
 */
public class TabAdapter extends Adapter<TabItemHolder> {

    private ArrayList<TabItem> list;

    public TabAdapter(){
        list = new ArrayList<>();
    }

    public void addTabItem(TabItem tabItem){
        list.add(tabItem);
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public TabItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab, parent, false);
        return new TabItemHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TabItemHolder holder, int position) {
        TabItem tabItem = list.get(position);
        holder.bindTabItem(tabItem);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

}

class TabItemHolder extends ViewHolder implements View.OnClickListener{

    private TabItem tabItem;
    private EditText string1;
    private EditText string2;
    private EditText string3;
    private EditText string4;
    private EditText string5;
    private EditText string6;

    public TabItemHolder(View itemView) {
        super(itemView);
        string1 = (EditText) itemView.findViewById(R.id.string1);
        string2 = (EditText) itemView.findViewById(R.id.string2);
        string3 = (EditText) itemView.findViewById(R.id.string3);
        string4 = (EditText) itemView.findViewById(R.id.string4);
        string5 = (EditText) itemView.findViewById(R.id.string5);
        string6 = (EditText) itemView.findViewById(R.id.string6);
        string1.setOnClickListener(this);
        string2.setOnClickListener(this);
        string3.setOnClickListener(this);
        string4.setOnClickListener(this);
        string5.setOnClickListener(this);
        string6.setOnClickListener(this);
    }

    public void bindTabItem(TabItem tabItem){
        this.tabItem = tabItem;
        string1.setText(tabItem.getString1());
        string2.setText(tabItem.getString2());
        string3.setText(tabItem.getString3());
        string4.setText(tabItem.getString4());
        string5.setText(tabItem.getString5());
        string6.setText(tabItem.getString6());
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(),"Click",Toast.LENGTH_SHORT).show();
    }
}
