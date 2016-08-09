package ke.co.edgar.waks.fragments.pages;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ke.co.edgar.waks.R;

public class DashBoard extends Fragment  {
    private List<Job> jobList = new ArrayList<>();
    private RecyclerView recyclerView;
    private JobsAdapter mAdapter;
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        RelativeLayout rootView =  (RelativeLayout) inflater.inflate(R.layout.dashboard_layout, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new JobsAdapter(jobList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Job job = jobList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), job.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();

        return rootView;
    }

    private void prepareMovieData() {
        Job job = new Job("3D Motion Graphics Designer (Stamford, CT)", "Whatever the time, place or conditions, just lift up your head, breathe and stride out! Running is a great way to feel better in body and mind, push yourself, unwind and explore. Designed for runners of all abilities and aspirations", "2 second ago","Tinker IO , Nairobi","500,000");
        jobList.add(job);

        job = new Job("Sr. Visual Designer (UI/UX)", "Whatever the time, place or conditions, just lift up your head, breathe and stride out! Running is a great way to feel better in body and mind, push yourself, unwind and explore. Designed for runners of all abilities and aspirations", "10 second ago","Tinker IO , Nairobi","100,000");
        jobList.add(job);

        job = new Job("SHOES DESIGNER", "Whatever the time, place or conditions, just lift up your head, breathe and stride out! Running is a great way to feel better in body and mind, push yourself, unwind and explore. Designed for runners of all abilities and aspirations", "14 minutes ago","Tinker IO , Nairobi","20,000");
        jobList.add(job);

        job = new Job("Shaun the Sheep", "Whatever the time, place or conditions, just lift up your head, breathe and stride out! Running is a great way to feel better in body and mind, push yourself, unwind and explore. Designed for runners of all abilities and aspirationsn", "30 minutes ago","Tinker IO , Nairobi","300,000");
        jobList.add(job);

        job = new Job("The Martian", "Whatever the time, place or conditions, just lift up your head, breathe and stride out! Running is a great way to feel better in body and mind, push yourself, unwind and explore. Designed for runners of all abilities and aspirations", "1hour ago","Tinker IO , Nairobi","1000,000");
        jobList.add(job);

        job = new Job("Mission: Impossible Rogue Nation", "Whatever the time, place or conditions, just lift up your head, breathe and stride out! Running is a great way to feel better in body and mind, push yourself, unwind and explore. Designed for runners of all abilities and aspirations", "2hours ago","Tinker IO , Nairobi","200,000");
        jobList.add(job);

        job = new Job("Up", "Whatever the time, place or conditions, just lift up your head, breathe and stride out! Running is a great way to feel better in body and mind, push yourself, unwind and explore. Designed for runners of all abilities and aspirations", "3days ag0","Tinker IO , Nairobi","150,000");
        jobList.add(job);



        mAdapter.notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private DashBoard.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DashBoard.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
