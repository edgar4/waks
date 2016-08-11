package ke.co.edgar.waks.fragments.pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ke.co.edgar.waks.R;
import ke.co.edgar.waks.app.AppConfig;
import ke.co.edgar.waks.fragments.MainFragment;


public class DashBoard extends Fragment  {
    private List<Job> jobList = new ArrayList<>();
    private RecyclerView recyclerView;
    private JobsAdapter mAdapter;
    private ProgressDialog pDialog;
    private static final String TAG = DashBoard.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SwipeRefreshLayout rootView = (SwipeRefreshLayout ) inflater.inflate(R.layout.dashboard_layout, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new JobsAdapter(jobList);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                prepareMovieData();

            }
        });
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        prepareMovieData();

                                    }
                                }
        );

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Job job = jobList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), job.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("jobID", job.getID());
                Log.d(TAG, "IntentDone: "+ job.getID());
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        prepareMovieData();
        return rootView;
    }

    private void prepareMovieData() {

        // Tag used to cancel the request
        pDialog.setMessage("Downloading New Jobs");
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_JOBS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());

                        try {
                            JSONObject jObj = response;
                            boolean error = false;
                            if (!error) {
                                Log.d(TAG, "we never get here ");
                                JSONArray jobs = jObj.getJSONArray("jobs");
                                Log.d(TAG, "get array from obj" + jobs);
                                Log.d(TAG, "size" + jobs.length());
                                for (int i = 0; i < jobs.length(); i++) {
                                    Log.d(TAG, "LoopJob: ");
                                    JSONObject j = (JSONObject) jobs.get(i);
                                    String id = j.getString("id");
                                    String title = j.getString("title");
                                    String description = j.getString("description");
                                    String type = j.getString("type");
                                    String salaryAmount = j.getString("salary");
                                    String location = j.getString("location");
                                    String created_at = j.getString("time_posted");
                                    Log.d(TAG, "LoopDone: "+id);
                                    Job addjob = new Job(id, title, description, created_at, location, salaryAmount, type);
                                    jobList.add(addjob);

                                    Log.d(TAG, "addingJobs: " + title);
                                }

                                mAdapter.notifyDataSetChanged();
                                // stopping swipe refresh
                                swipeRefreshLayout.setRefreshing(false);
                            } else {
                                // Error in login. Get the error message
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getActivity().getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(), "Error Downloading Jobs: ", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "could not process json");
                        Toast.makeText(getActivity().getApplicationContext(), "something went wrong: ", Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(getRequest);
        hideDialog();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
