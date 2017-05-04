package com.shmily.tjz.swap.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.Adapter.SearchAdapter;
import com.shmily.tjz.swap.Db.ShoesSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/3.
 */

public class SearchFragment extends Fragment {
    private View rootView;
    private List<ShoesSearch> shoessearchList = new ArrayList<>();
    private SearchAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_fragment, container, false);
        initview();
        return rootView;
    }

    private void initview() {
        for (int i = 1; i <= 24; i++) {
            StringBuilder url = new StringBuilder();
            url.append("http://www.shmilyz.com/search/").append(i).append(".png");
            String urls = String.valueOf(url);
            ShoesSearch shoessearch = new ShoesSearch(urls);
            shoessearchList.add(shoessearch);

        }
        MultiColumnListView multicolumn = (MultiColumnListView) rootView.findViewById(R.id.list);
        adapter = new SearchAdapter(getActivity(), shoessearchList);
        multicolumn.setAdapter(adapter);
        multicolumn.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                String pos= String.valueOf(position);
                Toast.makeText(getActivity(), pos, Toast.LENGTH_SHORT).show();
            }
        });
    }
}