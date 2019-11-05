package com.e.mycontact.Header;

import com.e.mycontact.Header.Section;

public class ChildModel implements Section {
    byte[] imageChild;
    String child;
    int idChild;
    private int section;

    public int getIdChild() {
        return idChild;
    }

    public void setIdChild(int idChild) {
        this.idChild = idChild;
    }

    public ChildModel(int section) {
        this.section = section;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public byte[] getImageChild() {
        return imageChild;
    }

    public void setImageChild(byte[] imageChild) {
        this.imageChild = imageChild;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String getName() {
        return child;
    }

    @Override
    public int sectionPosition() {
        return section;
    }

    @Override
    public byte[] getImage() {
        return imageChild;
    }
    @Override
    public int getId() {
        return idChild;
    }


}
