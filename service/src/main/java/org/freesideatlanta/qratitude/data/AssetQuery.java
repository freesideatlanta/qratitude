package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.util.*;

import com.mongodb.*;
import org.bson.types.*;

import org.freesideatlanta.qratitude.model.*;

public interface AssetQuery {
	DBObject build();
}
