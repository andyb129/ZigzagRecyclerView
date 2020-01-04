package uk.co.barbuzz.zigzagrecyclerview.sample

import uk.co.barbuzz.zigzagrecyclerview.ZigzagImage

class SnowImage(private val snowImageResourceId: Int) : ZigzagImage {
    override val zigzagImageUrl: String?
        get() = ""
    override val zigzagImageResourceId: Int
        get() = snowImageResourceId

}