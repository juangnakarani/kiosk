package com.juangnakarani.kiosk.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.juangnakarani.kiosk.R;
import com.juangnakarani.kiosk.adapter.ProductAdapter;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Category;
import com.juangnakarani.kiosk.model.ViewPagerEvent;
import com.juangnakarani.kiosk.model.Product;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductAllFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductAllFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Category category;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mProductAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private List<Product> products = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    private DbHelper db;
    private int transactionOrigin;

    public ProductAllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductAllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductAllFragment newInstance(String param1, String param2) {
        ProductAllFragment fragment = new ProductAllFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("chk", "allProduct onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        mProgressBar = view.findViewById(R.id.loadingBarProduct);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rclv_product_all);
        mRecyclerView.setHasFixedSize(false);

        mProductAdapter = new ProductAdapter(products);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mProductAdapter);
        db = new DbHelper(getContext());

        if(products.size()==0){
            new AsyncGetProductOperation().execute();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("chk", "onResume all fragment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewPagerEvent event) {
        Log.d("chk", "evenbus getAllProducts(): " + event.tabPosition);
        transactionOrigin = event.tabPosition;

        if (event.tabPosition == 0) {
            products.clear();
            mProductAdapter.notifyDataSetChanged();
            new AsyncGetProductOperation().execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("chk", "onStart Product All Fragment");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        Log.d("chk", "onStop Product All Fragment");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

        // TODO: Rename method, update argument and hook method into UI event
        public void onButtonPressed (Uri uri){
//        Log.i("chkEvent", "allProduct onButtonPressed()");
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }

        @Override
        public void onAttach (Context context){
            super.onAttach(context);

        }

        @Override
        public void onDetach () {
            super.onDetach();
//        Log.i("chkEvent", "allProduct onDetach()");
            mListener = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }

        private class AsyncGetProductOperation extends AsyncTask<Void, Void, Boolean>{

            @Override
            protected Boolean doInBackground(Void... voids) {
                // Gets the data repository in write mode

                products.addAll(db.getAllProducts());

                return true;
            }

            @Override
            protected void onPostExecute(Boolean result){
                if(result==Boolean.TRUE){
                    Log.d("chk","onPostExecute all product return true");
                    mProductAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

            }
        }
    }
