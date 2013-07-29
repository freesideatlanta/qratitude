package org.freesideatlanta.qratitude.data;

import java.io.*;
import java.net.*;

public interface PhotoStore {
	URI create(InputStream is, String extension) throws IOException;
}
