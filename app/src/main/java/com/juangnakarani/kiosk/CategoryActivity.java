package com.juangnakarani.kiosk;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juangnakarani.kiosk.adapter.CategoryAdapter;
import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Category;
import com.juangnakarani.kiosk.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mCategoryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Category> categories = new ArrayList<>();
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rclv_category);
        mRecyclerView.setHasFixedSize(true);

        mCategoryAdapter = new CategoryAdapter(categories);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mCategoryAdapter);

//        Category c1 = new Category(123, "category A");
//        categories.add(c1);

        db = new DbHelper(getApplicationContext());
        categories.addAll(db.getAllCategory());

        mCategoryAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog(false, null, -1);
            }
        });
    }

    private void showCategoryDialog(final boolean shouldUpdate, final Category category, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_category, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(CategoryActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView dialogTitle = view.findViewById(R.id.dialog_category_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.dialog_title_category_new) : getString(R.string.dialog_title_category_edit));

        final EditText editTextCategoryID = view.findViewById(R.id.dialog_category_edit_id);
        final EditText editTextCategoryDesc = view.findViewById(R.id.dialog_category_edit_description);

        if (shouldUpdate && category != null) {
            editTextCategoryID.setText(category.getId());
            editTextCategoryDesc.setText(category.getDescription());
        }


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(editTextCategoryID.getText().toString())) {
                    Toast.makeText(CategoryActivity.this, "Enter category ID!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && category != null) {
                    // update note by it's id
//                    updateNote(inputNote.getText().toString(), position);
                } else {
                    // create new note
//                    createNote(inputNote.getText().toString());
                    Log.i("chk","test new category");
                    Category c = new Category();
                    c.setId(Integer.valueOf(editTextCategoryID.getText().toString()));
                    c.setDescription(editTextCategoryDesc.getText().toString());
                    createCategory(c);
                }
            }
        });


    }

    private void createCategory(Category c) {
        long id = db.insertCategory(c);

        Category category = db.getCategoryByID(c.getId());
        Log.i("chk get category", String.valueOf(category.getId()));
        Log.i("chk get category", category.getDescription());

        if (category != null) {
            categories.add(category);
            mCategoryAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
