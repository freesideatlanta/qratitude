package org.freesideatlanta.qratitude.data;

import java.util.*;

import org.freesideatlanta.qratitude.model.*;

public class CategoryStoreMongo extends StoreMongo implements CategoryStore {

	public CategoryStoreMongo(String host, int port, String database, String collection) {
		super(host, port, database, collection);
	}

	@Override
	public Collection<String> create(Collection<String> categories) {
		// TODO: override the categories collection
		return categories;
	}

	@Override
	public Collection<String> read() {
		// TODO: query for the categories 
		Collection<String> categories = new ArrayList<String>();

		categories.add("Appliances");
		categories.add("Architecture");
		categories.add("Cabinets");
		categories.add("Ceiling");
		categories.add("Doors");
		categories.add("Electrical");
		categories.add("Flooring");
		categories.add("Hardware");
		categories.add("Life Safety");
		categories.add("Lighting");
		categories.add("Lumber / Wood");
		categories.add("Office Furniture");
		categories.add("Paint");
		categories.add("Plumbing");
		categories.add("Storage");
		categories.add("Windows");

		return categories;
	}

	@Override
	public void update(Collection<String> categories) {
		// TODO: override the categories collection
	}

	@Override
	public void delete() {
		// TODO: delete all the categories
	}
}
