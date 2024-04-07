package com.joyjoin.eventservice.model;

public enum Tag {
    VEGAN_CUISINE("Vegan Cuisine"),
    NIGHTLIFE("Nightlife"),
    PUB_CRAWLS("Pub Crawls"),
    CONCERTS("Concerts"),
    BRUNCH("Brunch"),
    MOCKTAILS("Mocktails"),
    COCKTAILS("Cocktails"),
    VEGETARIANISM("Vegetarianism"),
    CRAFT_BEER("Craft Beer"),
    WINE_TASTING("Wine Tasting"),
    GASTRONOMY("Gastronomy"),
    EXERCISE("Exercise"),
    SOCCER("Soccer"),
    BASKETBALL("Basketball"),
    HEALTHY_EATING("Healthy Eating"),
    HIKING("Hiking"),
    YOGA("Yoga"),
    MEDITATION("Meditation"),
    PHOTOGRAPHY("Photography"),
    TRAVEL("Travel"),
    READING("Reading"),
    COOKING("Cooking"),
    GAMING("Gaming"),
    PAINTING("Painting"),
    MUSIC("Music"),
    DANCING("Dancing"),
    FASHION("Fashion"),
    WRITING("Writing"),
    GARDENING("Gardening"),
    FILM("Film"),
    THEATER("Theater"),
    ARCHITECTURE("Architecture"),
    HISTORY("History"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    WILDLIFE("Wildlife"),
    CONSERVATION("Conservation"),
    SUSTAINABILITY("Sustainability"),
    ENTREPRENEURSHIP("Entrepreneurship"),
    VOLUNTEERING("Volunteering"),
    SURFING("Surfing");


    private final String name;

    Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
