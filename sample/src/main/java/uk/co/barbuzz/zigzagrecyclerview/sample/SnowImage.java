package uk.co.barbuzz.zigzagrecyclerview.sample;

import uk.co.barbuzz.zigzagrecyclerview.ZigzagImage;

/**
 * Created by andy.barber on 13/02/2018.
 */
public class SnowImage extends ZigzagImage {

    private int spaceImageResourceId;

    public SnowImage(int spaceImageResourceId) {
        this.spaceImageResourceId = spaceImageResourceId;
    }

    @Override
    protected String getZigzagImageUrl() {
        return null;
    }

    @Override
    protected int getZigzagImageResourceId() {
        return spaceImageResourceId;
    }
}
