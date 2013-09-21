package org.freesideatlanta.qratitude.data;

import java.util.*;
import java.util.regex.*;

import com.mongodb.*;
import org.bson.types.*;

public class AssetCodeQuery implements AssetQuery {

	private String code;

	public AssetCodeQuery(String code) throws IllegalArgumentException {
		if (code == null) {
			throw new IllegalArgumentException("AssetCodeQuery arguments cannot all be null");
		}

		this.code = code;
	}

	@Override
	public DBObject build() {
		QueryBuilder qb = QueryBuilder.start("code").is(this.code);
		DBObject dbo = qb.get();

		return dbo;
	} 
}
