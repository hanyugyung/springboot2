package org.bwyou.springboot2.viewmodels;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class MetaData {
	
	private long totalItemCount;
	private int totalPageCount;
	private int pageIndex;
	private int pageSize;
	private boolean hasNextPage;
	private boolean hasPreviousPage;
	private boolean isFirstPage;
	private boolean isLastPage;
	

	public MetaData(Page<?> result) {
		this.totalItemCount = result.getTotalElements();
		this.totalPageCount = result.getTotalPages();
		this.pageIndex = result.getNumber();
		this.pageSize = result.getSize();
		this.hasNextPage = result.hasNext();
		this.hasPreviousPage = result.hasPrevious();
		this.isFirstPage = result.isFirst();
		this.isLastPage = result.isLast();
	}
}
