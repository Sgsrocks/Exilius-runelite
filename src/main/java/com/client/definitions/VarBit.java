package com.client.definitions;

import com.client.Buffer;
import com.client.StreamLoader;

public final class VarBit {

	public static void unpackConfig(StreamLoader streamLoader) {
		Buffer stream = new Buffer(streamLoader.getArchiveData("varbit.dat"));
		int cacheSize = stream.readUShort();
		if (cache == null)
			cache = new VarBit[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null)
				cache[j] = new VarBit();
			cache[j].readValues(stream);
		}

		if (stream.currentOffset != stream.buffer.length)
			System.out.println("varbit load mismatch");
	}

	private void readValues(Buffer stream) {
		stream.readUnsignedByte();
		configID = stream.readUShort();
		lsb = stream.readUnsignedByte();
		msb = stream.readUnsignedByte();
	}

	private VarBit() {
		
	}

	public static VarBit cache[];
	public int configID;
	public int lsb;
	public int msb;
	
}
