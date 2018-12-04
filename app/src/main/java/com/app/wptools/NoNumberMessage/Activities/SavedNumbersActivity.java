package com.app.wptools.NoNumberMessage.Activities;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.RelativeLayout;

import com.app.wptools.NoNumberMessage.Adapters.NumbersAdapter;
import com.app.wptools.NoNumberMessage.Models.SqLiteText;
import com.app.wptools.NoNumberMessage.helper.DatabaseHandler;
import com.app.wptools.NoNumberMessage.helper.RecyclerItemTouchHelper;
import com.app.wptools.R;

import java.util.ArrayList;

public class SavedNumbersActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    private static final String TAG = SavedNumbersActivity.class.getSimpleName();

    RecyclerView recycler_saved_numbers;

    LinearLayoutManager linearLayoutManager;

    private ArrayList<SqLiteText> numberList;
    private NumbersAdapter mAdapter;
    private RelativeLayout relative_layout;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_numbers);

        db = new DatabaseHandler(this);

        numberList = new ArrayList<SqLiteText>();

        recycler_saved_numbers = (RecyclerView) findViewById(R.id.recycler_saved_numbers);

        relative_layout = (RelativeLayout) findViewById(R.id.relative_layout);

        linearLayoutManager = new LinearLayoutManager(this);

        numberList = db.getAllNumbers();

        mAdapter = new NumbersAdapter(this, numberList);
        recycler_saved_numbers.setLayoutManager(linearLayoutManager);
        recycler_saved_numbers.setItemAnimator(new DefaultItemAnimator());
        recycler_saved_numbers.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler_saved_numbers.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_saved_numbers);

//        new getNumbers().execute((Object[]) null);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NumbersAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            int id = numberList.get(viewHolder.getAdapterPosition()).getId();
            String code = numberList.get(viewHolder.getAdapterPosition()).getCode();
            String number = numberList.get(viewHolder.getAdapterPosition()).getNumber();

            // backup of removed item for undo purpose
            final SqLiteText deletedItem = numberList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            db.deleteNumber(id);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(relative_layout, code+number + " removed!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    // GetNotes AsyncTask
    private class getNumbers extends AsyncTask<Object, Object, ArrayList<SqLiteText>> {

        @Override
        protected ArrayList<SqLiteText> doInBackground(Object... params) {
            numberList = db.getAllNumbers();

            return numberList;
        }

        @Override
        protected void onPostExecute(ArrayList<SqLiteText> result) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
