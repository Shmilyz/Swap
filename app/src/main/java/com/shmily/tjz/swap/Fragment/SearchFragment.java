package com.shmily.tjz.swap.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shmily.tjz.swap.R;

/**
 * Created by Shmily_Z on 2017/5/4.
 */

public class SearchFragment extends Fragment{
    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_fragment, container, false);
        final MaterialSearchBar searchBar = (MaterialSearchBar) rootView.findViewById(R.id.searchBar);
searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Toast.makeText(getActivity(), text.toString(), Toast.LENGTH_SHORT).show();
            searchBar.onFocusChange(rootView,false);
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
});
        return rootView;

    }

}
