package com.e.mycontact.Header;

public interface Section {
    boolean isHeader();
    String getName();
    int getId();

    byte[] getImage();

    int sectionPosition();
    int getFavourite();
}
