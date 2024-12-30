package com.example.recyclerviewproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclerviewproject.data.Client;
import com.example.recyclerviewproject.data.DBHelper;
import com.example.recyclerviewproject.databinding.ActivityClientBinding;

public class ClientActivity extends AppCompatActivity {
	public static final int INSERT = 0;
	public static final int UPDATE = 1;
	public static final String MODE = "mode";
	public static final String POSITION = "position";
	public static final String CLIENT = "client";
	
	private int mode;
	private Integer clientId = 0;
	
	private ActivityClientBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		//+
		binding = ActivityClientBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		//
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});
		//
		Intent intent = getIntent();
		mode = intent.getIntExtra(MODE, -1);
		if (mode == UPDATE){
			clientId = intent.getSerializableExtra(CLIENT, Client.class).getId();
			//
			try (DBHelper helper = new DBHelper(this)) {
				Client client = helper.selectById(clientId);
				binding.surnameTextClient.setText(client.getSurname());
				binding.nameTextClient.setText(client.getName());
				binding.phoneTextClient.setText(client.getPhone());
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem save = menu.add("Save");
		save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		save.setOnMenuItemClickListener(item -> {
			//Form Test
			if (
				!binding.surnameTextClient.getText().toString().isBlank() &&
					!binding.nameTextClient.getText().toString().isBlank() &&
					!binding.phoneTextClient.getText().toString().isBlank()
			) {
				Client client = new Client(
					clientId,
					binding.surnameTextClient.getText().toString(),
					binding.nameTextClient.getText().toString(),
					binding.phoneTextClient.getText().toString()
				);
				//DB
				try (DBHelper helper = new DBHelper(this)) {
					if (mode == INSERT)
						client = helper.insert(client);
					if (mode == UPDATE)
						helper.update(client);
					//
					Intent resultIntent = new Intent();
					int position = getIntent().getIntExtra(POSITION, -1);
					resultIntent.putExtra(POSITION, position);
					resultIntent.putExtra(MODE, mode);
					resultIntent.putExtra(CLIENT, client);
					//
					setResult(RESULT_OK, resultIntent);
				}
				//Закриття актівіті
				finish();
			}
			return true;
		});
		return super.onCreateOptionsMenu(menu);
	}
}