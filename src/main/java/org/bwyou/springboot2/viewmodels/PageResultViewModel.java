package org.bwyou.springboot2.viewmodels;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageResultViewModel<T> {
	Iterable<T> result;
	MetaData metaData;

	public PageResultViewModel()
    {

    }
	
	public PageResultViewModel(Iterable<T> result)
    {
        this.result = result;
    }
	
	public PageResultViewModel(Iterable<T> result, MetaData metaData)
    {
        this.result = result;
        this.metaData = metaData;
    }
	
	public PageResultViewModel(Page<T> result)
    {
        this.result = result.getContent();
        this.metaData = new MetaData(result);
    }
}
