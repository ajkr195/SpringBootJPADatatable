package com.spring.jpa.datatables.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTable<T> {
	
	private int draw;
    private int start;
    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;

}
