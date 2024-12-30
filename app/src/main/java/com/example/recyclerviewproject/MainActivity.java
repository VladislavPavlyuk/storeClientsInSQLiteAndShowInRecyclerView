package com.example.recyclerviewproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewproject.adapters.ClientAdapter;
import com.example.recyclerviewproject.data.Client;
import com.example.recyclerviewproject.data.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private ClientAdapter clientAdapter;
	
	public final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
		new ActivityResultContracts.StartActivityForResult(),
		(ActivityResult result) -> {
			Intent data = result.getData();
			if (result.getResultCode() == RESULT_OK && data != null) {
				int position = data.getIntExtra(ClientActivity.POSITION, -1);
				int mode = data.getIntExtra(ClientActivity.MODE, -1);
				Client client = data.getSerializableExtra(ClientActivity.CLIENT, Client.class);
				if (client != null) {
					if (mode == ClientActivity.INSERT) {
						clientAdapter.insertData(client);
					}
					if (mode == ClientActivity.UPDATE) {
						clientAdapter.updateData(position, client);
					}
				}
			}
		}
	);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_main);
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});
		//List
		/*List<Client> list = new ArrayList<>(List.of(
			new Client(1, "A", "a", "111"),
			new Client(2, "B", "b", "222"),
			new Client(3, "C", "c", "333")
		));*/
		//DB
		List<Client> list;
		try (DBHelper helper = new DBHelper(this)) {
			list = helper.selectAll();
		}
		//Adapter
		clientAdapter = new ClientAdapter(this, list);
		//
		RecyclerView recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setAdapter(clientAdapter);
		//LayoutManager !!!!!!!!!!
		recyclerView.setLayoutManager(
			new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem add = menu.add("Add Client");
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		add.setOnMenuItemClickListener(item -> {
			Intent intent = new Intent(this, ClientActivity.class);
			intent.putExtra(ClientActivity.MODE, ClientActivity.INSERT);
			activityResultLauncher.launch(intent);
			return true;
		});
		return super.onCreateOptionsMenu(menu);
	}
}