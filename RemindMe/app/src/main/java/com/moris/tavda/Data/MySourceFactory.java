package com.moris.tavda.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import dto.RemindDTO;

public class MySourceFactory extends DataSource.Factory<Long, RemindDTO> {
    public MutableLiveData<DTODataSource> datasourceLiveData = new MutableLiveData<>();

    @Override
    public DataSource<Long, RemindDTO> create() {
        DTODataSource dataSource = new DTODataSource();
        datasourceLiveData.postValue(dataSource);
        return dataSource;
    }
//public MySourceFactory{
////        return new DTODataSource();
//    @Override
//    public DataSource<Long, RemindDTO> create() {
//        DTODataSource dataSource = new DTODataSource();
//        datasourceLiveData.postValue(dataSource);
//        return dataSource;
//    }
}
