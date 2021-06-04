package com.example.androidchallenge.util

class Util {

    companion object {
        /**
         * Creates a string from all the elements separated by a comma.
         */
        @JvmStatic
        fun joinToString(list: List<String>?): String {
            return list?.joinToString() ?: ""
        }
    }
}