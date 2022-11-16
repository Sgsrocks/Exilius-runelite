package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public class Linkable {

	public final void unlink() {
		if (next == null) {
		} else {
			next.prev = prev;
			prev.next = next;
			prev = null;
			next = null;
		}
	}

	public Linkable() {
	}

	public long id;
	public Linkable prev;
	public Linkable next;
}
