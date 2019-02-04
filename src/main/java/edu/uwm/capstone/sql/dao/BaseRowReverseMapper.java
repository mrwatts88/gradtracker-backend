package edu.uwm.capstone.sql.dao;

import java.util.Map;

public interface BaseRowReverseMapper<T> {

    Map<String, Object> mapObject(T object);

}
