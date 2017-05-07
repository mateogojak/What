package com.fer.ppij.what.database;

public enum QuestionType {
        MULTIPLE_CHOICE("multiple_choice"),
        FILL_IN("fill_in"),
        IMAGE_MULTIPLE_CHOICE("image_multiple_choice"),
        IMAGE_FILL_IN("image_fill_in");

        private String name;

        QuestionType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }