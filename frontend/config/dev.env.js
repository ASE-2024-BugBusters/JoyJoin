// Is used to adjust the variables dynamically and concatenate them
const API_VERSION = "api"
const BASE_URL =  "http://localhost:9191/";
const BASE_URL_USER_SERVICE =  BASE_URL + "user-service/" + API_VERSION;
const BASE_URL_EVENT_SERVICE = BASE_URL + "event-service/" + API_VERSION;
const BASE_URL_POST_SERVICE =  BASE_URL + "post-service/" + API_VERSION;

/**
 * Is used to be accessed globally
 */
module.exports = {
    API_VERSION,
    BASE_URL,
    BASE_URL_USER_SERVICE,
    BASE_URL_EVENT_SERVICE,
    BASE_URL_POST_SERVICE,
    TOKEN_PREFIX: "Bearer ",
    INTEREST_TAGS: [
        {value: 'VeganCuisine', label: 'Vegan Cuisine'},
        {value: 'Nightlife', label: 'Nightlife'},
        {value: 'PubCrawls', label: 'Pub Crawls'},
        {value: 'Concerts', label: 'Concerts'},
        {value: 'Brunch', label: 'Brunch'},
        {value: 'Mocktails', label: 'Mocktails'},
        {value: 'Cocktails', label: 'Cocktails'},
        {value: 'Vegetarianism', label: 'Vegetarianism'},
        {value: 'CraftBeer', label: 'Craft Beer'},
        {value: 'WineTasting', label: 'Wine Tasting'},
        {value: 'Gastronomy', label: 'Gastronomy'},
        {value: 'Exercise', label: 'Exercise'},
        {value: 'Soccer', label: 'Soccer'},
        {value: 'Basketball', label: 'Basketball'},
        {value: 'HealthyEating', label: 'Healthy Eating'},
        {value: 'Hiking', label: 'Hiking'},
        {value: 'Yoga', label: 'Yoga'},
        {value: 'Meditation', label: 'Meditation'},
        {value: 'Photography', label: 'Photography'},
        {value: 'Travel', label: 'Travel'},
        {value: 'Reading', label: 'Reading'},
        {value: 'Cooking', label: 'Cooking'},
        {value: 'Gaming', label: 'Gaming'},
        {value: 'Painting', label: 'Painting'},
        {value: 'Music', label: 'Music'},
        {value: 'Dancing', label: 'Dancing'},
        {value: 'Fashion', label: 'Fashion'},
        {value: 'Writing', label: 'Writing'},
        {value: 'Gardening', label: 'Gardening'},
        {value: 'Film', label: 'Film'},
        {value: 'Theater', label: 'Theater'},
        {value: 'Architecture', label: 'Architecture'},
        {value: 'History', label: 'History'},
        {value: 'Science', label: 'Science'},
        {value: 'Technology', label: 'Technology'},
        {value: 'Wildlife', label: 'Wildlife'},
        {value: 'Conservation', label: 'Conservation'},
        {value: 'Sustainability', label: 'Sustainability'},
        {value: 'Entrepreneurship', label: 'Entrepreneurship'},
        {value: 'Volunteering', label: 'Volunteering'},
        {value: 'Surfing', label: 'Surfing'}
    ]
}