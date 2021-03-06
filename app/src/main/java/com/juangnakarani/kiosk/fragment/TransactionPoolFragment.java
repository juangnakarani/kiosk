package com.juangnakarani.kiosk.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juangnakarani.kiosk.R;
import com.juangnakarani.kiosk.adapter.TransactionPoolAdapter;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.TransactionHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionPoolFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionPoolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionPoolFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mTransactionPoolAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TransactionHeader> transactionHeaderList = new ArrayList<>();
    private DbHelper db;

    public TransactionPoolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionPoolFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionPoolFragment newInstance(String param1, String param2) {
        TransactionPoolFragment fragment = new TransactionPoolFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_pool, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rclv_transaction_pool);
        mRecyclerView.setHasFixedSize(false);

        mTransactionPoolAdapter = new TransactionPoolAdapter(transactionHeaderList);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mTransactionPoolAdapter);

        db = new DbHelper(getContext());
        transactionHeaderList.addAll(db.getAllTransactionHeader());

        mTransactionPoolAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
//        Log.i("chk", "onResume of TransactionPoolFragment");
        super.onResume();
        db = new DbHelper(getContext());
        transactionHeaderList.clear();
        transactionHeaderList.addAll(db.getAllTransactionHeader());

        mTransactionPoolAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
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
}
