package com.example.recyclerviewproject.adapters;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewproject.ClientActivity;
import com.example.recyclerviewproject.MainActivity;
import com.example.recyclerviewproject.R;
import com.example.recyclerviewproject.data.Client;
import com.example.recyclerviewproject.data.DBHelper;
import com.example.recyclerviewproject.databinding.ItemClientBinding;

import java.util.List;

//2 крок
//<ClientAdapter.ClientViewHolder>   !!!!!!!!!!
public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder>{
	private final MainActivity mainActivity;
	private final List<Client> list;
	
	public ClientAdapter(MainActivity mainActivity, List<Client> list) {
		this.mainActivity = mainActivity;
		this.list = list;
	}
	
	@NonNull
	@Override
	//В методах мають бути правильні типи
	//ClientViewHolder для повернення значення і в параметрах
	public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		//1
		/*//LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		LayoutInflater inflater = LayoutInflater.from(mainActivity);
		//attacheToRoot = false !!!!!!!!!!
		View item = inflater.inflate(R.layout.item_client, parent, false);
		return new ClientViewHolder(item);*/
		//2
		//!!!
		ItemClientBinding binding = ItemClientBinding.inflate(
			LayoutInflater.from(parent.getContext()),
			parent,
			false //!!!
		);
		return new ClientViewHolder(binding);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
		Client client = list.get(position);
		//1
		//String.valueOf() !!!!!!!!!!
		/*holder.idItem.setText(String.valueOf(client.getId()));
		holder.surnameItem.setText(client.getSurname());
		holder.nameItem.setText(client.getName());
		holder.phoneItem.setText(client.getPhone());*/
		//2
		holder.binding.idItem.setText(String.valueOf(client.getId()));
		holder.binding.surnameItem.setText(client.getSurname());
		holder.binding.nameItem.setText(client.getName());
		holder.binding.phoneItem.setText(client.getPhone());
		//OnClick
		//holder.binding.getRoot().setOnClickListener(v -> {});
		holder.itemView.setOnClickListener(v -> {
			Intent intent = new Intent(mainActivity, ClientActivity.class);
			intent.putExtra(ClientActivity.MODE, ClientActivity.UPDATE);
			intent.putExtra(ClientActivity.CLIENT, list.get(position));
			intent.putExtra(ClientActivity.POSITION, position);
			mainActivity.activityResultLauncher.launch(intent);
		});
		//OnLongClick
		holder.itemView.setOnLongClickListener(v -> {
			//AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
			builder.setTitle("Видалення");
			builder.setMessage("Ви впевнені що хочете видалити клієнта?");
			builder.setNegativeButton("Cancel", (dialog, which) -> { //Cancel
				//Toast
			});
			builder.setPositiveButton("Delete", (dialog, which) -> {//Delete
				//1
				try (DBHelper helper = new DBHelper(mainActivity)) {
					helper.deleteById(client.getId());
				}
				//2
				list.remove(position);
				//3
				notifyItemRemoved(position);
				//
			});
			//
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
			return true;
		});
	}
	
	@Override
	public int getItemCount() {
		return list.size();
	}
	public void insertData(Client client) {
		list.add(client);
		notifyItemInserted(list.size() - 1);
	}
	
	public void updateData(int position, Client client) {
		list.set(position, client);
		notifyItemChanged(position);
	}
	
	//1 крок
	public static class ClientViewHolder extends RecyclerView.ViewHolder {
		//1
		/*TextView idItem;
		TextView surnameItem;
		TextView nameItem;
		TextView phoneItem;
		
		public ClientViewHolder(@NonNull View itemView) {
			super(itemView);
			idItem = itemView.findViewById(R.id.idItem);
			surnameItem = itemView.findViewById(R.id.surnameItem);
			nameItem = itemView.findViewById(R.id.nameItem);
			phoneItem = itemView.findViewById(R.id.phoneItem);
		}
		*/
		
		//2
		final ItemClientBinding binding;
		
		
		public ClientViewHolder(ItemClientBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
	}
}
