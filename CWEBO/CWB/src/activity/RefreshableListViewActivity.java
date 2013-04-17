package activity;

import java.util.ArrayList;

import widget.RefreshableListView;
import widget.RefreshableListView.OnRefreshListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cuixbo.cweibo.R;

public class RefreshableListViewActivity extends Activity {
	private ArrayList<String> mItems;
	private RefreshableListView mListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mItems = new ArrayList<String>();
		mItems.add("Diary of a Wimpy Kid 6: Cabin Fever");
		mItems.add("Steve Jobs");
		mItems.add("Inheritance (The Inheritance Cycle)");
		mItems.add("11/22/63: A Novel");
		mItems.add("The Hunger Games");
		mItems.add("The LEGO Ideas Book");
		mItems.add("Explosive Eighteen: A Stephanie Plum Novel");
		mItems.add("Catching Fire (The Second Book of the Hunger Games)");
		mItems.add("Elder Scrolls V: Skyrim: Prima Official Game Guide");
		mItems.add("Death Comes to Pemberley");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);

		mListView = (RefreshableListView) findViewById(R.id.listview);
		// add top/footview
		TextView layout = new TextView(this);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 600));
		layout.setBackgroundColor(Color.BLUE);
		mListView.addFooterView(layout);

		mListView.setAdapter(adapter);

		// Callback to refresh the list
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(RefreshableListView listView) {
				// TODO Auto-generated method stub
				new NewDataTask().execute();

			}

		});
	}

	private class NewDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}

			return "A new list item";
		}

		@Override
		protected void onPostExecute(String result) {
			mItems.add(0, result);
			// This should be called after refreshing finished
			mListView.completeRefreshing();

			super.onPostExecute(result);
		}
	}

}