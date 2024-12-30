package com.example.recyclerviewproject.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
	public enum Names {
		TABLE_CLIENTS,
		ID,
		SURNAME,
		NAME,
		PHONE
	}
	
	public DBHelper(@Nullable Context context) {
		super(context, "clients_db", null, 1);
		createTable();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public void createTable() {
		String sql = "create table if not exists %s(%s integer primary key autoincrement, %s text, %s text, %s text)";
		sql = String.format(sql, Names.TABLE_CLIENTS, Names.ID, Names.SURNAME, Names.NAME, Names.PHONE);
		getWritableDatabase().execSQL(sql);
	}
	
	public void dropTable() {
		String sql = "drop table if exists " + Names.TABLE_CLIENTS;
		getWritableDatabase().execSQL(sql);
	}
	
	public Client insert(Client client) {
		ContentValues values = new ContentValues();
		values.put(Names.SURNAME.toString(), client.getSurname());
		values.put(Names.NAME.toString(), client.getName());
		values.put(Names.PHONE.toString(), client.getPhone());
		//
		long id = getWritableDatabase()
			.insert(Names.TABLE_CLIENTS.toString(), null, values);
		//
		return selectById((int) id);
	}
	
	@SuppressLint("Range")
	public Client selectById(Integer id) {
		String sql = "select * from %s where %s='%s'";
		sql = String.format(sql, Names.TABLE_CLIENTS, Names.ID, id);
		try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
			Client client;
			//while
			if (cursor.moveToNext()) {
				client = new Client(
					cursor.getInt(cursor.getColumnIndex(Names.ID.toString())),
					cursor.getString(cursor.getColumnIndex(Names.SURNAME.toString())),
					cursor.getString(cursor.getColumnIndex(Names.NAME.toString())),
					cursor.getString(cursor.getColumnIndex(Names.PHONE.toString()))
				);
				return client;
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	@SuppressLint("Range")
	public List<Client> selectAll() {
		String sql = "select * from " + Names.TABLE_CLIENTS;
		List<Client> list = new ArrayList<>();
		try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
			Client client;
			//while
			while (cursor.moveToNext()) {
				client = new Client(
					cursor.getInt(cursor.getColumnIndex(Names.ID.toString())),
					cursor.getString(cursor.getColumnIndex(Names.SURNAME.toString())),
					cursor.getString(cursor.getColumnIndex(Names.NAME.toString())),
					cursor.getString(cursor.getColumnIndex(Names.PHONE.toString()))
				);
				list.add(client);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}
	
	public void deleteById(Integer id) {
		String sql = "delete from %s where %s='%s'";
		sql = String.format(sql, Names.TABLE_CLIENTS, Names.ID, id);
		getWritableDatabase().execSQL(sql);
	}
	
	public void update(Client client) {
		String sql = "update %s set %s='%s', %s='%s', %s='%s' where %s='%s'";
		sql = String.format(sql, Names.TABLE_CLIENTS,
			Names.SURNAME, client.getSurname(),
			Names.NAME, client.getName(),
			Names.PHONE, client.getPhone(),
			Names.ID, client.getId()
		);
		getWritableDatabase().execSQL(sql);
	}
	
	public void insertAll(List<Client> list){
		for (Client client : list) {
			insert(client);
		}
	}
}
