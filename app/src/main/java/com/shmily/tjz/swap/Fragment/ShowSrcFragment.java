package com.shmily.tjz.swap.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.shmily.tjz.swap.Adapter.SpecialAdapter;
import com.shmily.tjz.swap.Db.ShoesSpecial;
import com.shmily.tjz.swap.R;
import com.shmily.tjz.swap.ShoesActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shmily_Z on 2017/5/15.
 */

public class ShowSrcFragment extends Fragment {
    private View rootView;
    private List<ShoesSpecial> shoessearchList = new ArrayList<>();
    private SpecialAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.show_src_fragment, container, false);
        initview();
        return rootView;
    }
    private void initview() {
        for (int i = 1; i <= 10; i++) {
            StringBuilder url = new StringBuilder();
            url.append("http://www.shmilyz.com/search/").append(i).append(".png");
            String urls = String.valueOf(url);
            ShoesSpecial shoessearch = new ShoesSpecial(urls);
            shoessearchList.add(shoessearch);

        }
        MultiColumnListView multicolumn = (MultiColumnListView) rootView.findViewById(R.id.show_sec_list);
        adapter = new SpecialAdapter(getActivity(), shoessearchList);
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
