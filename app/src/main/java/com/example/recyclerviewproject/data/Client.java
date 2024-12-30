package com.example.recyclerviewproject.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client implements Serializable {
	private Integer id;
	private String surname;
	private String name;
	private String phone;
}
