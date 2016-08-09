package ke.co.edgar.waks.fragments.pages;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ke.co.edgar.waks.R;

public class ProfileFragment extends Fragment {

    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_layout, container, false);
    }
}
